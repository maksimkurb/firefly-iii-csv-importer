<script setup lang="ts">
import MapperEditor from "../../components/mapper/MapperEditor.vue";

defineProps<{
  isNew: boolean;
}>();
</script>

<template>
  <main>
    <MapperEditor :isNew="isNew" />
  </main>
</template>

<style scoped>
main {
  margin-top: 1em;
}
</style>
