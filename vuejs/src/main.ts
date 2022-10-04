import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import PrimeVue from "primevue/config";
import { store, key } from "./store";
import { jwtInterceptor } from "./plugins/axios";

import "primevue/resources/themes/saga-blue/theme.css"; // テーマ
import "primevue/resources/primevue.min.css"; // ベース
import "primeicons/primeicons.css"; // アイコン
import "primeflex/primeflex.css"; // primeflex

const app = createApp(App).use(router).use(PrimeVue);
app.use(store, key);
app.use(jwtInterceptor);

app.mount("#app");
