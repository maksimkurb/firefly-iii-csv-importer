<template>
  <i-navbar fluid class="navbar">
    <i-navbar-brand to="/">Yet Another Firefly III Importer</i-navbar-brand>
    <i-navbar-collapsible>
      <i-nav>
        <i-nav-item to="/">Home</i-nav-item>
        <i-nav-item to="/about">About</i-nav-item>
      </i-nav>
      <i-nav>
        <i-nav-item to="/login">Login</i-nav-item>
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
