<template>
  <q-layout view="hHh lpR fFf">
    <q-header>
      <q-toolbar>
        <q-toolbar-title class="row items-center q-gutter-sm">
          <HeaderLogoButtonComponent />
        </q-toolbar-title>
        <AuthManagerComponent />
        <DocumentationComponent />
      </q-toolbar>
    </q-header>
    <q-page-container>
      <q-page padding>
        <q-toolbar class="bg-primary text-white rounded-borders q-mb-lg">
          <q-breadcrumbs active-color="white">
            <q-breadcrumbs-el
              label="Admin"
              icon="fa-solid fa-cog"
              to="/admin"
            />
            <q-breadcrumbs-el
              label="Groups"
              icon="fa-solid fa-users-rectangle"
            />
          </q-breadcrumbs>
          <div class="col-grow" />
          <q-btn
            color="primary"
            class="q-mr-sm"
            icon="refresh"
            @click="queryBackend"
          />
          <q-btn color="green" icon="add" @click="showAddGroupDialog"
            >Add new group</q-btn
          >
        </q-toolbar>
        <div class="flex column">
          <template v-for="group in groupList" :key="group.id">
            <q-btn
              color="primary"
              outline
              class="group-button"
              @click="showEditGroupDialog(group)"
              align="left"
              no-caps
            >
              <div class="text-left">
                <div>
                  <q-icon name="fa-solid fa-users-rectangle" />
                  {{ group.name }}
                </div>
                <div class="text-weight-regular">
                  <q-icon name="" />
                  {{ group.description }}
                </div>
                <div class="text-weight-regular">
                  <q-icon name="" />
                  ID {{ group.id }}
                </div>
              </div>
            </q-btn>
          </template>
          <q-btn
            outline
            color="green"
            class="group-button"
            align="left"
            no-caps
            @click="showAddGroupDialog"
          >
            <div class="text-left">
              <div>
                <q-icon name="add" />
                Add new group
              </div>
              <div class="text-weight-regular">
                <q-icon name="" />
              </div>
            </div>
          </q-btn>
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
  <!-- Add/edit group dialog -->
  <q-dialog v-model="displayAddEditGroupDialog" persistent>
    <q-card class="dialog-add-edit-group">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ addEditGroupDialogTitle }}</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>
      <q-card-section>
        <q-form
          class="q-gutter-md"
          @submit="doAddEditGroup"
          autocorrect="off"
          autocapitalize="off"
          autocomplete="off"
          spellcheck="false"
        >
          <q-input
            type="text"
            v-model="currentlyEditedGroupData.name"
            filled
            label="Name"
            :rules="[(val) => !!val || 'Field is required']"
            autocomplete="off"
          />
          <q-input
            type="text"
            v-model="currentlyEditedGroupData.description"
            filled
            label="Description"
            autocomplete="off"
          />
          <q-btn
            label="Delete"
            color="red-4"
            v-if="addEditGroupDialogEditMode"
            @click="doDeleteGroup"
          />
          <q-btn
            :label="addEditGroupDialogAction"
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
import HeaderLogoButtonComponent from 'components/layout/HeaderLogoButtonComponent.vue';
import AuthManagerComponent from 'components/layout/AuthManagerComponent.vue';
import { computed, onMounted, ref } from 'vue';
import { loadPayloadInstanceFromApi } from 'src/types/common';
import {
  sendFailureNotification,
  sendSuccessNotification,
} from 'src/types/notification';
import { api } from 'boot/axios';
import { instanceToPlain } from 'class-transformer';
import DocumentationComponent from 'components/layout/DocumentationComponent.vue';
import { GroupPayload } from 'src/types/payloads/group';
import { onDialogYes } from 'src/types/dialog';

const groupList = ref<GroupPayload[]>([]);
const addEditGroupDialogEditMode = ref<boolean>(false);
const addEditGroupDialogAction = ref('Add');
const addEditGroupDialogTitle = ref('Add group');
const displayAddEditGroupDialog = ref(false);
const currentlyEditedGroupData = ref<GroupPayload>(new GroupPayload());
const registrationDataValid = computed(() => {
  if (!currentlyEditedGroupData.value.name) {
    return false;
  }
  return true;
});

function doAddEditGroup() {
  if (!registrationDataValid.value) {
    sendFailureNotification('The entered values are not valid!');
    return;
  }

  api
    .post( addEditGroupDialogEditMode.value ? 'admin/edit-group' : 'admin/create-group', instanceToPlain(currentlyEditedGroupData.value))
    .then((response) => {
      sendSuccessNotification(response.data);
    })
    .catch((reason) => {
      sendFailureNotification('Unable to create/edit: ' + reason);
    })
    .finally(() => {
      displayAddEditGroupDialog.value = false;
      queryBackend();
    });
}

function doDeleteGroup() {
  onDialogYes("Delete group '" + currentlyEditedGroupData.value.name + "'", "Do you really want to delete this group? You cannot undo this operation.").then(() => {
    api
      .post( 'admin/delete-group', instanceToPlain(currentlyEditedGroupData.value))
      .then((response) => {
        sendSuccessNotification(response.data);
      })
      .catch((reason) => {
        sendFailureNotification('Unable to delete: ' + reason);
      })
      .finally(() => {
        displayAddEditGroupDialog.value = false;
        queryBackend();
      });
  })
}

function showAddGroupDialog() {
  addEditGroupDialogTitle.value = 'Add new group';
  currentlyEditedGroupData.value = new GroupPayload();
  addEditGroupDialogAction.value = 'Create';
  displayAddEditGroupDialog.value = true;
  addEditGroupDialogEditMode.value = false;
}

function showEditGroupDialog(group: GroupPayload) {
  addEditGroupDialogTitle.value = `Edit group ID=${group.id}`;
  currentlyEditedGroupData.value = group;
  addEditGroupDialogAction.value = 'Edit';
  displayAddEditGroupDialog.value = true;
  addEditGroupDialogEditMode.value = true;
}

function queryBackend() {
  loadPayloadInstanceFromApi(
    '/admin/list-groups',
    GroupPayload,
    groupList
  ).catch((err) => console.log(err));
}

onMounted(() => {
  queryBackend();
});
</script>
<style scoped lang="scss">
.dialog-add-edit-group {
  width: 700px;
  max-width: 80vw;
}

.group-button {
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
