<script setup lang="ts">
import IconSearch from "./icons/IconSearch.vue";
</script>

<template>
  <div class="item _border-radius">
    <i>
      <IconSearch />
    </i>
    <div class="details">
      <h3>
        <slot name="heading"></slot>
      </h3>
      <slot></slot>
      <div class="button-container">
        <slot name="button"></slot>
      </div>
    </div>
  </div>
</template>

<style scoped>
.item {
  margin: 0 10px;
  padding: 0 2rem;
  display: flex;
  flex-direction: column;
}

.details {
  text-align: center;
}

.button-container {
  margin-top: 1em;
}

i {
  display: flex;
  place-items: center;
  place-content: center;

  color: var(--color-text);
}

h3 {
  font-size: 1.2rem;
  font-weight: 500;
  margin-bottom: 0.4rem;
  color: var(--color-heading);
}
</style>
