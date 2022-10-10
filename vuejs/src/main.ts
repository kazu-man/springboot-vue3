import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import PrimeVue from "primevue/config";
import { store, key } from "./store";
import { jwtInterceptor } from "./plugins/axios";
import DialogService from "primevue/dialogservice";
import ToastService from "primevue/toastservice";
import InputText from "primevue/inputtext";
import Card from "primevue/card";
import Button from "primevue/button";

import "primevue/resources/themes/saga-blue/theme.css"; // テーマ
import "primevue/resources/primevue.min.css"; // ベース
import "primeicons/primeicons.css"; // アイコン
import "primeflex/primeflex.css"; // primeflex
import "./style/grobal.css";

const app = createApp(App).use(router).use(PrimeVue);
app.use(store, key);
app.use(jwtInterceptor);
app.use(DialogService);
app.use(ToastService);
app.component("Button", Button);
app.component("Card", Card);
app.component("InputText", InputText);

app.mount("#app");
