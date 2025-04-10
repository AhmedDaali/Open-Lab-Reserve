<template>
  <q-btn to="/" size="lg" flat>
    <img :src="logo" class="logo"/>
<!--    <q-icon left size="3em" name="fa-solid fa-chart-simple" />-->
<!--    <div>OpenLabReserve</div>-->
  </q-btn>
</template>
<script setup lang="ts">
import logo from "assets/logo-olr-full.svg"
</script>
<style scoped lang="scss">
.logo {
  height: 3em;
}
</style>
