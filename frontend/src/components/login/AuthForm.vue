<script setup lang="ts">
import { IForm, IFormGroup, IInput, IButton } from "@inkline/inkline";
</script>

<template>
  <IForm :classes="['login-form']">
    <IFormGroup>
      <IFormLabel>Login</IFormLabel>
      <IInput type="text" />
    </IFormGroup>
    <IFormGroup>
      <IFormLabel>Password</IFormLabel>
      <IInput type="password" />
    </IFormGroup>
    <IFormGroup>
      <i-button>Test</i-button>
    </IFormGroup>
  </IForm>
</template>

<style scoped>
.login-form {
  width: 300px;
}
</style>
