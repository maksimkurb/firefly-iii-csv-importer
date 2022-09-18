import { FireflyUser } from "../api";
import { userApi } from "./apiClient";

export async function getCurrentUser(): Promise<FireflyUser> {
  const resp = await userApi.getCurrentUser();

  return resp.data;
}
