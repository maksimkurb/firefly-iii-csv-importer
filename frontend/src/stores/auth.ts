import { throwStatement } from '@babel/types'
import { defineStore } from 'pinia'

interface AuthStore {
  fireflyUrl: String | null
  clientId: Number | null
  clientToken: String | null

  username: String | null
  token: String | null
}

export const useAuthStore = defineStore({
  id: 'auth',
  state: () => ({
    fireflyUrl: null,
    clientId: null,
    clientToken: null,

    username: null,
    token: null,
  } as AuthStore),
  getters: {
    isFireflyBackendConfigured: (state) => state.fireflyUrl != null && state.clientId != null && state.clientToken != null,
    isLoggedIn: (state) => state.token != null,
  },
  actions: {
    setFireflySettings(fireflyUrl: String, clientId: Number, clientToken: String) {
      this.fireflyUrl = fireflyUrl;
      this.clientId = clientId;
      this.clientToken = clientToken;
    },
    setUserDetails(username: String, token: String) {
      this.username = username;
      this.token = token;
    }
  }
})
