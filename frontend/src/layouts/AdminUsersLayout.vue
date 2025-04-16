<template>
  <q-layout view="hHh lpR fFf">
    <q-header>
      <q-toolbar>
        <q-toolbar-title class="row items-center q-gutter-sm">
          <HeaderLogoButtonComponent/>
        </q-toolbar-title>
        <AuthManagerComponent/>
        <DocumentationComponent/>
      </q-toolbar>
    </q-header>
    <q-page-container>
      <q-page padding>
        <q-toolbar class="bg-primary text-white rounded-borders q-mb-lg">
          <q-breadcrumbs active-color="white">
            <q-breadcrumbs-el label="Admin" icon="fa-solid fa-cog" to="/admin" />
            <q-breadcrumbs-el label="Users" icon="fa-solid fa-user"/>
          </q-breadcrumbs>
          <div class="col-grow" />
          <q-btn color="primary" class="q-mr-sm" icon="refresh" @click="queryBackend"/>
          <q-btn color="green" icon="add" @click="showAddUserDialog">Add new user</q-btn>
        </q-toolbar>
        <div class="flex column">
          <q-btn outline color="secondary" class="user-button" align="left" no-caps>
            <div class="text-left">
              <div><q-icon name="person"/> <i>Administrator</i></div>
              <div class="text-weight-regular"><q-icon name=""/> Administrator</div>
              <div class="text-weight-regular"><q-icon name=""/> -</div>
            </div>
          </q-btn>
          <template v-for="user in userList" :key="user.id">
            <q-btn outline color="primary" class="user-button" @click="showEditUserDialog(user)" align="left" no-caps>
              <div class="text-left">
                <div><q-icon name="person"/> {{ user.email }}</div>
                <div class="text-weight-regular"><q-icon name=""/> {{ user.role }}{{ !user.allowLogin ? " (Inactive)" : "" }}</div>
                <div class="text-weight-regular"><q-icon name=""/>
                  <template v-if="user.groups.length == 0">-</template>
                  <template v-else>
                    <q-chip dense v-for="group in user.groups" v-bind:key="group.id">{{ group.name }}</q-chip>
                  </template>
                </div>
              </div>
            </q-btn>
          </template>
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
  <!-- Add/edit user dialog -->
  <q-dialog v-model="displayAddEditUserDialog" persistent>
    <q-card class="dialog-add-edit-group">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ addEditUserDialogTitle }}</div>
        <q-space/>
        <q-btn icon="close" flat round dense v-close-popup/>
      </q-card-section>
      <q-card-section>
        <q-form
          class="q-gutter-md"
          @submit="doAddEditUser"
          autocorrect="off"
          autocapitalize="off"
          autocomplete="off"
          spellcheck="false"
        >
          <q-input
            type="text"
            v-model="currentlyEditedUserData.firstName"
            filled
            label="First name"
            :rules="[(val) => !!val || 'Field is required']"
            autocomplete="off"
          />
          <q-input
            type="text"
            v-model="currentlyEditedUserData.lastName"
            filled
            label="Last name"
            :rules="[(val) => !!val || 'Field is required']"
            autocomplete="off"
          />
          <q-input
            type="text"
            v-model="currentlyEditedUserData.email"
            filled
            label="E-Mail"
            :rules="[
              (val) => EmailValidator.validate(val) || 'Not a valid email',
            ]"
            :disable="addEditUserDialogEditMode"
            autocomplete="off"
          />
          <q-input
            type="password"
            v-model="currentlyEditedUserData.newPassword"
            filled
            :label="addEditUserDialogEditMode ? 'Password (leave empty to keep unchanged)' : 'Password'"
            :rules="[
              (val) => (!!val || addEditUserDialogEditMode) || 'Field is required',
              (val) => ((val && val.length >= 6) || addEditUserDialogEditMode) || 'Password too short',
            ]"
            autocomplete="new-password"
          />
          <q-input
            type="password"
            v-model="currentlyEditedUserData.newPasswordConfirm"
            filled
            label="Confirm password"
            :rules="[
              (val) =>
                (val == currentlyEditedUserData.newPassword || addEditUserDialogEditMode) || 'Passwords do not match',
            ]"
            autocomplete="off"
          />
          <q-input
            type="text"
            v-model="currentlyEditedUserData.affiliation"
            filled
            label="Affiliation"
            :rules="[(val) => !!val || 'Field is required']"
            autocomplete="off"
          />
          <q-select
            v-model="currentlyEditedUserData.role"
            :options="[UserRole.User, UserRole.Admin, UserRole.Guest]"
            filled
            label="Role"
          />
          <q-select
            v-model="currentlyEditedUserData.groups"
            :options="groupList"
            multiple
            filled
            use-chips
            option-label="name"
            label="Groups"
          >
          </q-select>
          <div>
            <q-checkbox
              v-model="currentlyEditedUserData.allowLogin"
              label="Allow Login" />
          </div>
          <q-btn
            :label="addEditUserDialogAction"
            type="submit"
            color="primary"
            :disable="!registrationDataValid"
          />
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import HeaderLogoButtonComponent from "components/layout/HeaderLogoButtonComponent.vue";
import AuthManagerComponent from "components/layout/AuthManagerComponent.vue";
import * as EmailValidator from "email-validator";
import {UserPayload, UserRole} from "src/types/registration";
import {computed, onMounted, ref} from "vue";
import {loadPayloadInstanceFromApi} from "src/types/common";
import {sendFailureNotification, sendSuccessNotification} from "src/types/notification";
import {api} from "boot/axios";
import {instanceToPlain} from "class-transformer";
import DocumentationComponent from "components/layout/DocumentationComponent.vue";
import { GroupPayload } from 'src/types/payloads/group';

const userList = ref<UserPayload[]>([])
const groupList = ref<GroupPayload[]>([])
const addEditUserDialogEditMode = ref<boolean>(false)
const addEditUserDialogAction = ref("Add")
const addEditUserDialogTitle = ref("Add user")
const displayAddEditUserDialog = ref(false)
const currentlyEditedUserData = ref<UserPayload>(
  new UserPayload()
);
const registrationDataValid = computed(() => {
  if (!EmailValidator.validate(currentlyEditedUserData.value.email)) {
    return false;
  }
  if (
    !currentlyEditedUserData.value.newPassword ||
    currentlyEditedUserData.value.newPassword.length < 6
  ) {
    return addEditUserDialogEditMode.value;
  }
  if (!currentlyEditedUserData.value.firstName) {
    return false;
  }
  if (!currentlyEditedUserData.value.lastName) {
    return false;
  }
  if (!currentlyEditedUserData.value.affiliation) {
    return false;
  }
  if (!currentlyEditedUserData.value.role) {
    return false;
  }
  return true;
});

function doAddEditUser() {
  if (addEditUserDialogEditMode.value) {
    if (!registrationDataValid.value) {
      sendFailureNotification('The entered values are not valid!');
      return;
    }

    api
      .post('admin/edit-user', instanceToPlain(currentlyEditedUserData.value))
      .then((response) => {
        sendSuccessNotification(response.data);
      })
      .catch((reason) => {
        sendFailureNotification('Unable to edit: ' + reason);
      })
      .finally(() => {
        displayAddEditUserDialog.value = false;
        queryBackend()
      });

  } else {
    if (!registrationDataValid.value) {
      sendFailureNotification('The entered values are not valid!');
      return;
    }

    api
      .post('auth/register', instanceToPlain(currentlyEditedUserData.value))
      .then((response) => {
        sendSuccessNotification(response.data);
      })
      .catch((reason) => {
        sendFailureNotification('Unable to register: ' + reason);
      })
      .finally(() => {
        displayAddEditUserDialog.value = false;
        queryBackend()
      });
  }
}

function showAddUserDialog() {
  addEditUserDialogTitle.value = "Add new user";
  currentlyEditedUserData.value = new UserPayload()
  addEditUserDialogAction.value = "Create"
  displayAddEditUserDialog.value = true;
  addEditUserDialogEditMode.value = false
}

function showEditUserDialog(user: UserPayload) {
  addEditUserDialogTitle.value = `Edit user ${user.email}`
  currentlyEditedUserData.value = user;
  addEditUserDialogAction.value = "Edit"
  displayAddEditUserDialog.value = true;
  addEditUserDialogEditMode.value = true
  currentlyEditedUserData.value.newPasswordConfirm = "";
}

function queryBackend() {
  loadPayloadInstanceFromApi("/admin/list-users", UserPayload, userList).catch(err => console.log(err));
  loadPayloadInstanceFromApi("/admin/list-groups", GroupPayload, groupList).catch(err => console.log(err));
}

onMounted(() => {
  queryBackend()
})

</script>
<style scoped lang="scss">
.dialog-add-edit-group {
  width: 700px;
  max-width: 80vw;
}

.user-button {
  flex-grow: 1;
  width: 100%;
  height: 100%;
  overflow: hidden;
  margin-bottom: 4px;
}

.menu-button {
  flex-grow: 1;
  width: 100%;
  height: 100%;
  overflow: hidden;
  margin-bottom: 4px;
}
</style>
