<script setup lang="ts">
import FileChooser from "../file/FileChooser.vue";

defineProps<{
  isNew: boolean;
}>();
</script>

<template>
  <div class="container">
    <div v-if="sample == null && !continueWithoutSample">
      <h3>Step 1: Upload CSV sample</h3>
      <p>
        Please select CSV file on your computer so you can preview your mapping
        configuration.
      </p>
      <FileChooser />
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 2em;
}
.sample-buffer-preview:deep(textarea) {
  font-family: monospace;
  min-height: 300px;
  overflow-x: scroll;
  white-space: pre;
  line-height: 1.7;
}
</style>

<script lang="ts">
interface IData {
  sample: Record<string, string> | null;
  continueWithoutSample: boolean;
}

export default {
  data() {
    const data: IData = {
      sample: null,
      continueWithoutSample: false,
    };

    return data;
  },
  methods: {
    onSampleSubmit() {
      //
    },
  },
};
</script>
