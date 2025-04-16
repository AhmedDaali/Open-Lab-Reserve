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
            <q-breadcrumbs-el label="Admin" icon="fa-solid fa-cog"/>
          </q-breadcrumbs>
        </q-toolbar>
        <div class="flex column">
          <q-btn class="menu-button" align="left" color="primary" outline size="xl" icon="fa-solid fa-user" label="Users" to="/admin/users"/>
          <q-btn class="menu-button" align="left" color="primary" outline size="xl" icon="fa-solid fa-users-rectangle" label="Groups" to="/admin/groups"/>
          <q-btn class="menu-button" align="left" color="primary" outline size="xl" icon="fa-solid fa-sitemap" label="Facilities" to="/admin/facilities"/>
          <q-btn class="menu-button" align="left" color="primary" outline size="xl" icon="fa-solid fa-microchip" label="Resources" to="/admin/resources"/>
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import HeaderLogoButtonComponent from "components/layout/HeaderLogoButtonComponent.vue";
import AuthManagerComponent from "components/layout/AuthManagerComponent.vue";
import DocumentationComponent from "components/layout/DocumentationComponent.vue";

</script>
<style scoped lang="scss">
.menu-button {
  flex-grow: 1;
  width: 100%;
  height: 100%;
  overflow: hidden;
  margin-bottom: 4px;
}
</style>
