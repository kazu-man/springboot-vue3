<template>
  <Card style="width: 25rem; margin-bottom: 2em; margin: auto">
    <template #title> Login </template>
    <template #content>
      <form class="grid p-fluid m-8">
        <span class="p-float-label col-12 md:col-12" style="margin: 1.5em auto">
          <InputText id="username" type="text" v-model="userName" />
          <label for="username">Username</label>
        </span>
        <span class="p-float-label col-12 md:col-12" style="margin: 1.5em auto">
          <InputText id="username" type="text" v-model="password" />
          <label for="username">Password</label>
        </span>
        <Button
          label="Submit"
          class="p-button-raised p-button-rounded"
          style="width: 40%"
          @click="submit"
        />
      </form>
    </template>
  </Card>
  <LoginStateCheckVue />
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import InputText from "primevue/inputtext";
import Card from "primevue/card";
import Button from "primevue/button";
import axios from "axios";
import { useStore } from "../store";
import { useRouter } from "vue-router";
import LoginStateCheckVue from "../components/LoginStateCheck.vue";
import { useCookies } from "vue3-cookies";

export default defineComponent({
  components: { InputText, Card, Button, LoginStateCheckVue },
  setup() {
    const userName = ref("");
    const password = ref("");
    const store = useStore();
    const router = useRouter();
    const { cookies } = useCookies();

    const submit = () => {
      axios
        .post("http://localhost:8080/api/login", {
          username: userName.value,
          password: password.value,
        })
        .then((res) => {
          const newToken = res.headers["x-auth-token"];
          store.commit("saveToken", newToken);
          cookies.set("token", newToken);

          // ホームに戻す
          router.push("/");
        })
        .catch((e) => {
          alert("ログインに失敗しました");
          console.log(e);
        });
    };

    return {
      userName,
      password,
      submit,
    };
  },
});
</script>
