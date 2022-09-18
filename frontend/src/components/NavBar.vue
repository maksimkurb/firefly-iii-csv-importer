<template>
  <i-navbar fluid class="navbar">
    <i-navbar-brand to="/">Yet Another Firefly III Importer</i-navbar-brand>
    <i-navbar-collapsible>
      <i-nav>
        <i-nav-item to="/">Home</i-nav-item>
        <i-nav-item to="/about">About</i-nav-item>
        <i-nav-item target="_blank" :href="fireflyUrl">
          Go to Firefly III
        </i-nav-item>
      </i-nav>
      <i-nav>
        <i-dropdown v-if="isLoggedIn">
          <i-nav-item>{{ email }}</i-nav-item>
          <template #body>
            <i-dropdown-item disabled>
              Logout is not implemented yet
            </i-dropdown-item>
          </template>
        </i-dropdown>
        <i-nav-item :href="loginUrl" v-else>Login</i-nav-item>
      </i-nav>
    </i-navbar-collapsible>
  </i-navbar>
</template>

<style scoped>
.navbar {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
}
</style>

<script lang="ts">
import { mapState } from "pinia";
import { SERVER_BASE_URL } from "../services/apiClient";
import { useAuthStore } from "../stores/auth";

export default {
  setup() {
    const authStore = useAuthStore();
    authStore.fetchFireflySettings();
    authStore.fetchUserDetails();
  },
  computed: {
    ...mapState(useAuthStore, ["fireflyUrl", "isLoggedIn", "email"]),
    loginUrl() {
      return `${SERVER_BASE_URL}/oauth2/authorization/firefly`;
    },
  },
};
</script>
