<script setup lang="ts">
import { IFormGroup, IFormLabel, ISelect } from "@inkline/inkline";

import {
  DEFAULT_MAPPING_CONFIG,
  FieldConfiguration,
  FIREFLY_FIELDS,
  MappingConfigSpecFieldName,
} from "./options";
import { ISelectOption } from "@/typings/ISelectOption";
import { ref, Ref } from "vue";

const props = defineProps<{
  sampleRow: { [key: string]: string };
}>();

const fields: Ref<FieldConfiguration[]> = ref(DEFAULT_MAPPING_CONFIG);

const fieldSearchVariants = ref<{
  [key: string]: ISelectOption[];
}>();

function onFieldSearch(
  field: MappingConfigSpecFieldName,
  query: string | null
) {
  if (query == null || query.trim() == "") {
    fieldSearchVariants.value = {
      ...fieldSearchVariants.value,
      [field]: FIREFLY_FIELDS,
    };
    return;
  }

  const trimmedValue = query.trim().toLocaleLowerCase();

  fieldSearchVariants.value = {
    ...fieldSearchVariants.value,
    [field]: FIREFLY_FIELDS.filter((field) =>
      field.label.includes(trimmedValue)
    ),
  };
}
</script>

<template>
  <div>
    <div v-for="(field, index) in fields" :key="field.fieldName">
      <IFormGroup>
        <IFormLabel>Select CSV delimeter</IFormLabel>
        <ISelect
          v-model="fields[index]"
          :options="fieldSearchVariants[field.fieldName]"
          placeholder="Choose file delimeter"
          autocomplete
          @search="(query: string|null) => onFieldSearch(field.fieldName, query)"
          name="file-csv-delimeter"
        ></ISelect>
      </IFormGroup>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 2em;
}
</style>
