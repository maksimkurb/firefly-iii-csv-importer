<template>
  <i-card class="mapper-item">
    <template #header>
      <slot name="name">Mapper Name</slot>
      {{ " " }}
      <i-badge color="primary">Global</i-badge>
      <i-badge>Personal</i-badge>
    </template>

    Mapper Description

    <template #footer> Card Footer </template>
  </i-card>
</template>

<style scoped>
.mapper-item {
  margin: 1em 0;
  max-width: 500px;
}
</style>
