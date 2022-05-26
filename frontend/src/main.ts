import { createApp } from 'vue'
import { createPinia } from 'pinia'

import { Inkline, components } from '@inkline/inkline';
import '@inkline/inkline/inkline.scss';

import "./main.scss";


import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())

app.use(Inkline, {
  components
});

app.use(router)

app.mount('#app')
