<template>
  <q-btn class="q-ml-sm" icon="help" @click="showDocumentation = true" flat/>
  <q-dialog full-height seamless :position="isLeft ? 'left' : 'right'" v-model="showDocumentation">
    <q-card class="column full-height" style="width: 20vw; max-width: 500px">
      <q-card-section class="row items-center">
        <div class="text-h6">Documentation</div>
        <q-space/>
        <q-btn v-if="!isLeft" size="sm" flat icon="fa-solid fa-chevron-left" @click="isLeft = true">
          <q-tooltip>Move to left side</q-tooltip>
        </q-btn>
        <q-btn v-if="isLeft" size="sm" flat icon="fa-solid fa-chevron-right" @click="isLeft = false">
          <q-tooltip>Move to right side</q-tooltip>
        </q-btn>
        <q-btn flat icon="close" v-close-popup/>
      </q-card-section>

      <q-card-section>
        <q-icon name="info"/>
        If you experience issues, please contact {{ registrationFeatures.adminContact }}
      </q-card-section>
      <q-card-section class="col-grow">
        <q-scroll-area style="width: 100%; height: 100%">
          <q-list bordered class="rounded-borders">
            <q-expansion-item
              expand-separator
              icon="help"
              label="What is OpenLabReserve?"
              caption="Basic information"
              group="documentation"
            >
              <q-card>
                <q-card-section>
                  The <strong>OpenLabReserve</strong> platform allows to manage available lab spaces and bookings.
                </q-card-section>
              </q-card>
            </q-expansion-item>
          </q-list>
        </q-scroll-area>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import {UserRegistrationAllowedFeaturesPayload} from "src/types/registration";
import {loadPayloadInstanceFromApi} from "src/types/common";

const showDocumentation = ref(false)
const registrationFeatures = ref<UserRegistrationAllowedFeaturesPayload>(
  new UserRegistrationAllowedFeaturesPayload()
);
const isLeft = ref(false);

onMounted(() => {
  loadPayloadInstanceFromApi(
    '/auth/registration-features',
    UserRegistrationAllowedFeaturesPayload,
    registrationFeatures
  )
})

</script>
<style scoped lang="scss">

</style>
