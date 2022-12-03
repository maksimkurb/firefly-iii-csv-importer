import { fileURLToPath, URL } from "url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";
import monacoEditorPlugin, {
  type IMonacoEditorOpts,
} from "vite-plugin-monaco-editor";

const monacoOptions: IMonacoEditorOpts = {
  languageWorkers: ["typescript"],
  customWorkers: [
    { label: "javascript", entry: "" },
    { label: "csv", entry: "" },
  ],
};

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx(), monacoEditorPlugin(monacoOptions)],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
});
