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
              label="Resource types"
              icon="fa-solid fa-microchip"
            />
          </q-breadcrumbs>
          <div class="col-grow" />
          <q-btn
            color="primary"
            class="q-mr-sm"
            icon="refresh"
            @click="queryBackend"
          />
          <q-btn color="green" icon="add" @click="showAddResourceTypeDialog"
          >Add new resource type</q-btn
          >
        </q-toolbar>
        <div class="flex column">
          <template v-for="resourceType in resourceTypeList" :key="resourceType.id">
            <q-btn
              color="primary"
              outline
              class="resourceType-button"
              @click="showEditResourceTypeDialog(resourceType)"
              align="left"
              no-caps
            >
              <div class="text-left">
                <div>
                  <q-icon :name="resourceType.icon || 'fa-solid fa-tag'" />
                  {{ resourceType.name }}
                </div>
                <div class="text-weight-regular">
                  <q-icon name="" />
                  {{ resourceType.description }}
                </div>
                <div class="text-weight-regular">
                  <q-icon name="" />
                  ID {{ resourceType.id }}
                </div>
              </div>
            </q-btn>
          </template>
          <q-btn
            outline
            color="green"
            class="resourceType-button"
            align="left"
            no-caps
            @click="showAddResourceTypeDialog"
          >
            <div class="text-left">
              <div>
                <q-icon name="add" />
                Add new resource type
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
  <!-- Add/edit resourceType dialog -->
  <q-dialog v-model="displayAddEditResourceTypeDialog" persistent>
    <q-card class="dialog-add-edit-resourceType">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ addEditResourceTypeDialogTitle }}</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>
      <q-card-section>
        <q-form
          class="q-gutter-md"
          @submit="doAddEditResourceType"
          autocorrect="off"
          autocapitalize="off"
          autocomplete="off"
          spellcheck="false"
        >
          <q-input
            type="text"
            v-model="currentlyEditedResourceTypeData.name"
            filled
            label="Name"
            :rules="[(val) => !!val || 'Field is required']"
            autocomplete="off"
          />
          <q-input
            type="text"
            v-model="currentlyEditedResourceTypeData.description"
            filled
            label="Description"
            autocomplete="off"
          />
          <q-btn
            label="Delete"
            color="red-4"
            v-if="addEditResourceTypeDialogEditMode"
            @click="doDeleteResourceType"
          />
          <q-btn
            :label="addEditResourceTypeDialogAction"
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
import { onDialogYes } from 'src/types/dialog';
import { ResourceTypePayload } from 'src/types/payloads/resource_type';

const resourceTypeList = ref<ResourceTypePayload[]>([]);
const addEditResourceTypeDialogEditMode = ref<boolean>(false);
const addEditResourceTypeDialogAction = ref('Add');
const addEditResourceTypeDialogTitle = ref('Add resourceType');
const displayAddEditResourceTypeDialog = ref(false);
const currentlyEditedResourceTypeData = ref<ResourceTypePayload>(new ResourceTypePayload());
const registrationDataValid = computed(() => {
  if (!currentlyEditedResourceTypeData.value.name) {
    return false;
  }
  return true;
});

function doAddEditResourceType() {
  if (!registrationDataValid.value) {
    sendFailureNotification('The entered values are not valid!');
    return;
  }

  api
    .post( addEditResourceTypeDialogEditMode.value ? 'admin/edit-resource-type' : 'admin/create-resource-type', instanceToPlain(currentlyEditedResourceTypeData.value))
    .then((response) => {
      sendSuccessNotification(response.data);
    })
    .catch((reason) => {
      sendFailureNotification('Unable to create/edit: ' + reason);
    })
    .finally(() => {
      displayAddEditResourceTypeDialog.value = false;
      queryBackend();
    });
}

function doDeleteResourceType() {
  onDialogYes("Delete resource type '" + currentlyEditedResourceTypeData.value.name + "'", "Do you really want to delete this resource type? You cannot undo this operation.").then(() => {
    api
      .post( 'admin/delete-resource-type', instanceToPlain(currentlyEditedResourceTypeData.value))
      .then((response) => {
        sendSuccessNotification(response.data);
      })
      .catch((reason) => {
        sendFailureNotification('Unable to delete: ' + reason);
      })
      .finally(() => {
        displayAddEditResourceTypeDialog.value = false;
        queryBackend();
      });
  })
}

function showAddResourceTypeDialog() {
  addEditResourceTypeDialogTitle.value = 'Add new resource type';
  currentlyEditedResourceTypeData.value = new ResourceTypePayload();
  addEditResourceTypeDialogAction.value = 'Create';
  displayAddEditResourceTypeDialog.value = true;
  addEditResourceTypeDialogEditMode.value = false;
}

function showEditResourceTypeDialog(resourceType: ResourceTypePayload) {
  addEditResourceTypeDialogTitle.value = `Edit resource type ID=${resourceType.id}`;
  currentlyEditedResourceTypeData.value = resourceType;
  addEditResourceTypeDialogAction.value = 'Edit';
  displayAddEditResourceTypeDialog.value = true;
  addEditResourceTypeDialogEditMode.value = true;
}

function queryBackend() {
  loadPayloadInstanceFromApi(
    '/admin/list-resource-types',
    ResourceTypePayload,
    resourceTypeList
  ).catch((err) => console.log(err));
}

onMounted(() => {
  queryBackend();
});
</script>
<style scoped lang="scss">
.dialog-add-edit-resourceType {
  width: 700px;
  max-width: 80vw;
}

.resourceType-button {
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
