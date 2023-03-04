<script setup lang="ts">
import FileChooser from "../file/FileChooser.vue";
import { IFileChooserConfigurationInfo } from "../file/options";
import FieldMappingConfigurator from "./field/FieldMappingConfigurator.vue";

defineProps<{
  isNew: boolean;
}>();
</script>

<template>
  <div class="container">
    <div v-if="fileConfigurationInfo == null">
      <h3>Step 1: Configure CSV read options</h3>
      <p>Please select CSV file on your computer and configure it's options.</p>
      <FileChooser @file-configured="onSampleSubmit" />
    </div>
    <div v-else>
      <h3>Step 2: Configure columns mapping</h3>
      <p>
        Please configure how CSV columns will be mapped into Firefly III fields.
      </p>
      <FieldMappingConfigurator :sample-row="{ a: '2', b: 'asd' }" />
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
  fileConfigurationInfo: IFileChooserConfigurationInfo | null;
}

export default {
  data() {
    const data: IData = {
      sample: null,
      fileConfigurationInfo: null,
    };
    return data;
  },
  methods: {
    onSampleSubmit(e: IFileChooserConfigurationInfo) {
      console.log("File is configured", e);
      this.fileConfigurationInfo = e;
      e.clearFile();
    },
  },
  components: { FieldMappingConfigurator },
};
</script>
