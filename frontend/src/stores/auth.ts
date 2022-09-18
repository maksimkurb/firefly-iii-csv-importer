import { defineStore } from "pinia";
import { getFireflyInstanceUrl } from "../services/infoApiClient";
import { getCurrentUser } from "../services/userApiClient";

export interface AuthStore {
  fireflyUrl: string | null;
  userId: number | null;
  email: string | null;
}

export const useAuthStore = defineStore({
  id: "auth",
  state: () =>
    ({
      fireflyUrl: null,
      userId: null,
      email: null,
    } as AuthStore),
  getters: {
    isLoggedIn: (state) => state.userId != null,
  },
  actions: {
    setFireflySettings(fireflyUrl: string) {
      this.fireflyUrl = fireflyUrl;
    },
    setUserDetails(userId: number, email: string) {
      this.userId = userId;
      this.email = email;
    },
    async fetchFireflySettings() {
      const fireflyUrl = await getFireflyInstanceUrl();
      this.setFireflySettings(fireflyUrl);
    },
    async fetchUserDetails() {
      const currentUser = await getCurrentUser();
      this.setUserDetails(currentUser.userId, currentUser.name);
    },
  },
});
