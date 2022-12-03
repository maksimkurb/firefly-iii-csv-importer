<template>
  <div>
    <i-card class="mapper-item">
      <template #header>
        {{ mappingConfig.name }}
        {{ " " }}
        <i-badge size="sm" color="primary" v-if="mappingConfig.global">
          Global
        </i-badge>
        <i-badge size="sm" color="secondary" v-else>Personal</i-badge>
      </template>

      <span v-if="mappingConfig.description">
        {{ mappingConfig.description }}
      </span>
      <span class="_color:gray" v-else>No description is provided</span>

      <template #footer>
        <div class="_display:flex _flex-direction:row-reverse">
          <i-button size="sm" color="primary">Edit</i-button>
        </div>
      </template>
    </i-card>
  </div>
</template>

<style scoped>
.mapper-item {
  margin: 1em 0;
  max-width: 500px;
}
</style>

<script setup lang="ts">
import { MappingConfig } from "@/api";

defineProps<{
  mappingConfig: MappingConfig;
}>();
</script>

<script lang="ts">
export default {};
</script>
