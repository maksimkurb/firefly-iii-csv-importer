<script setup lang="ts">
import {IButton, IFormGroup, IFormLabel, IInput, ISelect, ITable,} from "@inkline/inkline";</script>

<template>
  <div>
    <IFormGroup>
      <IFormLabel>Select CSV file</IFormLabel>
      <IInput
        type="file"
        @change="onFileChosen"
        accept="text/csv"
        name="file"
      />
    </IFormGroup>
    <template v-if="sampleBuffer != null">
      <div class="csv-options">
        <div>
          <IFormGroup>
            <IFormLabel>Select file encoding</IFormLabel>
            <ISelect
              v-model="encoding"
              :options="encodingVariants"
              placeholder="Choose file encoding"
              autocomplete
              @search="onEncodingSearch"
              name="file-encoding"
            ></ISelect>
          </IFormGroup>
        </div>
        <div>
          <IFormGroup>
            <IFormLabel>Select CSV delimeter</IFormLabel>
            <ISelect
              v-model="delimeter"
              :options="delimeterVariants"
              placeholder="Choose file delimeter"
              autocomplete
              @search="onDelimeterSearch"
              name="file-csv-delimeter"
            ></ISelect>
          </IFormGroup>
        </div>
        <div>
          <IFormGroup>
            <IFormLabel>Select file encoding</IFormLabel>
            <ISelect
              v-model="quoteChar"
              :options="quoteCharVariants"
              placeholder="Choose CSV quote char"
              autocomplete
              @search="onQuoteCharSearch"
              name="file-csv-quote-char"
            ></ISelect>
          </IFormGroup>
        </div>
      </div>
      <IFormGroup class="sample-buffer-preview">
        <IFormLabel>
          CSV content preview (some rows can be trimmed for performance)
        </IFormLabel>
        <ITable
          v-if="sampleCsv != null"
          border
          hover
          condensed
          nowrap
          class="preview-table"
        >
          <thead>
            <th
              v-for="(header, key, index) in sampleCsv.meta.fields"
              :key="index"
            >
              {{ header }}
            </th>
          </thead>
          <tbody>
            <tr v-for="(row, key, index) in sampleCsv.data" :key="index">
              <td v-for="header in sampleCsv.meta.fields" :key="header">
                {{ row[header] }}
              </td>
            </tr>
          </tbody>
        </ITable>
      </IFormGroup>
      <p class="_color:info-60">
        Please, ensure that your CSV is displaying correctly.<br />
        If all is OK, you can continue
      </p>
      <IButton
        type="button"
        @click="onFileSelect"
        color="primary"
        class="_float:right"
      >
        Continue
      </IButton>
    </template>
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
.csv-options {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  column-gap: var(--spacing);
  row-gap: var(--spacing);
  margin: var(--spacing) 0;
}
.csv-options > div {
  min-width: 300px;
  flex-grow: 1;
}
.preview-table {
  max-width: 100%;
  max-height: 400px;
  overflow: scroll;
}
.preview-table:deep(table) {
  margin-bottom: 0;
}
</style>

<script lang="ts">
import { parse, ParseResult } from "papaparse";
import { ENCODINGS, DELIMETERS, QUOTE_CHARS, ISelectOption } from "./options";

interface IData {
  encodingVariants: readonly ISelectOption[];
  delimeterVariants: readonly ISelectOption[];
  quoteCharVariants: readonly ISelectOption[];

  sampleBuffer: ArrayBuffer | null;
  encoding: ISelectOption;
  delimeter: ISelectOption | null;
  quoteChar: ISelectOption;
}

export default {
  data(): IData {
    return {
      encodingVariants: ENCODINGS,
      delimeterVariants: DELIMETERS,
      quoteCharVariants: QUOTE_CHARS,

      sampleBuffer: null,
      encoding: ENCODINGS[0],
      delimeter: null,
      quoteChar: QUOTE_CHARS[0],
    };
  },
  computed: {
    sampleText(): string {
      if (this.sampleBuffer == null) {
        return "";
      }

      const decoder = new TextDecoder(this.encoding.id);
      const text = decoder.decode(this.sampleBuffer);
      const lastLine = text.lastIndexOf("\n");
      if (lastLine > 0) {
        return text.substring(0, lastLine - 1);
      }

      return text;
    },
    sampleIsEmpty(): boolean {
      return this.sampleText == null || this.sampleText.length === 0;
    },
    sampleCsv(): ParseResult<Record<string, string>> | null {
      if (this.sampleIsEmpty) {
        return null;
      }

      const delimeterToUse = this.delimeter ? this.delimeter.id : undefined;
      const quoteCharToUse = this.quoteChar ? this.quoteChar.id : '"';

      const csv = parse<Record<string, string>>(this.sampleText, {
        header: true,
        delimiter: delimeterToUse,
        quoteChar: quoteCharToUse,
      });

      this.delimeter = DELIMETERS.find(
        (opt) => opt.id === csv.meta.delimiter
      ) || {
        id: csv.meta.delimiter,
        label: `Custom separator (${csv.meta.delimiter})`,
      };

      return csv;
    },
  },
  methods: {
    onFileChosen(e: InputEvent) {
      const files = (e.target as HTMLInputElement).files;
      if (!files || files.length === 0) {
        return;
      }

      const file = files[0];
      if (!file) {
        return;
      }

      const reader = new FileReader();
      reader.addEventListener("load", () => {
        this.sampleBuffer = reader.result as ArrayBuffer;
      });
      reader.readAsArrayBuffer(file.slice(0, 4096, "text/csv"));
    },
    onEncodingSearch(query: string | null) {
      if (query === null || query.trim() === "") {
        this.encodingVariants = ENCODINGS;
        return;
      }

      const trimmedQuery = query.trim().toLocaleLowerCase();
      this.encodingVariants = ENCODINGS.filter((encoding) =>
        encoding.id.toLocaleLowerCase().includes(trimmedQuery)
      );
    },
    onDelimeterSearch(query: string | null) {
      if (query === null || query.trim() === "") {
        this.delimeterVariants = DELIMETERS;
        return;
      }

      const trimmedQuery = query.trim().toLocaleLowerCase();
      let variants = DELIMETERS.filter((delimeter) =>
        delimeter.label.toLocaleLowerCase().includes(trimmedQuery)
      );

      if (
        query.length === 1 &&
        variants.find((v) => v.id === query) === undefined
      ) {
        variants.push({ id: query, label: `Custom separator (${query})` });
      }

      this.delimeterVariants = variants;
    },
    onQuoteCharSearch(query: string | null) {
      if (query === null || query.trim() === "") {
        this.quoteCharVariants = QUOTE_CHARS;
        return;
      }

      const trimmedQuery = query.trim().toLocaleLowerCase();
      let variants = QUOTE_CHARS.filter((quoteChar) =>
        quoteChar.label.toLocaleLowerCase().includes(trimmedQuery)
      );

      if (
        query.length === 1 &&
        variants.find((v) => v.id === query) === undefined
      ) {
        variants.push({ id: query, label: `Custom quote char (${query})` });
      }

      this.quoteCharVariants = variants;
    },
    onFileSelect() {
      this.$emit("fileConfigured");
    },
  },
};
</script>
