import { MappingConfig } from "@/api";
import { mappingConfigApi } from "./apiClient";

export async function getMappingConfigList(): Promise<MappingConfig[]> {
  const resp = await mappingConfigApi.list();

  return resp.data;
}
