<script setup lang="ts">
import { RouterLink } from "vue-router";
import WelcomeItem from "./WelcomeItem.vue";
import IconMapping from "./icons/IconMapping.vue";
import IconImport from "./icons/IconImport.vue";
</script>

<template>
  <div class="welcome-container">
    <RouterLink to="/imports" class="_border-right">
      <WelcomeItem>
        <template #heading>Imports</template>
        <template #icon><IconImport /></template>

        List of your recent imports with their statuses. Go here if you want to
        import another file.
      </WelcomeItem>
    </RouterLink>
    <RouterLink to="/mappers">
      <WelcomeItem>
        <template #heading>Mapping configurations</template>
        <template #icon><IconMapping /></template>

        List of your mapping configurations with rules on how to process your
        CSV files.
      </WelcomeItem>
    </RouterLink>
  </div>
</template>

<style scoped>
.welcome-container {
  display: flex;
  justify-content: center;
}
.welcome-container > a {
  color: inherit;
}
.welcome-container > a:hover,
.welcome-container > a:focus {
  text-decoration: none;
}
.welcome-container > a > div {
  display: flex;
  flex-direction: column;
  min-height: 500px;
  justify-content: center;
  transition-property: background-color;
  transition-duration: var(--transition-duration);
}
.welcome-container > a:hover > div,
.welcome-container > a:focus > div {
  text-decoration: none;
  background-color: var(--color--primary-05);
}
</style>
