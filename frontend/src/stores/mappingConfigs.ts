import { defineStore } from "pinia";
import { MappingConfig } from "../api";
import { getMappingConfigList } from "../services/mappingConfigApiClient";

export interface MappingConfigsStore {
  loading: boolean;
  mappingConfigs: Array<MappingConfig> | null;
}

export const useMappingConfigsStore = defineStore({
  id: "mappers",
  state: () =>
    ({
      loading: false,
      mappingConfigs: null,
    } as MappingConfigsStore),
  getters: {
    isLoaded: (state) => state.loading == false && state.mappingConfigs == null,
  },
  actions: {
    async fetchMappingConfigs() {
      if (this.loading) {
        return;
      }

      this.loading = true;
      try {
        this.mappingConfigs = await getMappingConfigList();
      } catch (e) {
        console.log("Failed to load mapping config list", e);
      }

      this.loading = false;
    },
  },
});
