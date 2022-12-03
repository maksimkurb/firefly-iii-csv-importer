<template>
  <div class="item _border-radius">
    <i>
      <slot name="icon"></slot>
    </i>
    <div class="details">
      <h3>
        <slot name="heading"></slot>
      </h3>
      <slot></slot>
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
