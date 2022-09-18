import { infoApi } from "./apiClient";

export async function getFireflyInstanceUrl(): Promise<string> {
  const resp = await infoApi.getFireflyInstanceUrl();

  return resp.data;
}
