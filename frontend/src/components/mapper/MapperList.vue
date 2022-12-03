<template>
  <div class="loader-container" v-if="loading">
    <i-loader color="primary" />
  </div>
  <div class="mapper-list" v-else-if="isLoaded && mappingConfigs.length > 0">
    <div>asd</div>
  </div>
  <div v-else>
    <NothingFound>
      <template #heading>No mapping config has been created yet</template>
      Create your first mapper here or on
      <RouterLink to="/imports">imports</RouterLink> page.
      <template #button>
        <IButton color="primary" @click="$router.push('/mappers/new')">
          New mapping config
        </IButton>
      </template>
    </NothingFound>
  </div>
</template>

<style scoped>
.loader-container {
  padding-top: 4rem;
  display: flex;
  justify-content: center;
}
.mapper-item {
  margin: 1em 0;
  max-width: 500px;
}
</style>

<script lang="ts">
import { mapState } from "pinia";
import { useMappingConfigsStore } from "@/stores/mappingConfigs";
import { RouterLink } from "vue-router";
import NothingFound from "../NothingFound.vue";
import { IButton } from "@inkline/inkline";

export default {
  setup() {
    const mappingConfigStore = useMappingConfigsStore();
    mappingConfigStore.fetchMappingConfigs();
  },
  computed: {
    ...mapState(useMappingConfigsStore, [
      "mappingConfigs",
      "loading",
      "isLoaded",
    ]),
  },
  components: { NothingFound, RouterLink, IButton },
};
</script>
