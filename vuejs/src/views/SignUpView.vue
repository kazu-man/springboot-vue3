<template>
  <Card style="width: 25rem; margin-bottom: 2em; margin: auto">
    <template #title> SignUp </template>
    <template #content>
      <form class="grid p-fluid m-8">
        <span class="p-float-label col-12 md:col-12 input-m">
          <InputText
            :class="{ 'has-error': errors.username }"
            id="username"
            type="text"
            v-model="newUser.username"
          />
          <label for="username">Username</label>
        </span>
        <div class="error-msg" v-if="errors.username">
          {{ errors.username }}
        </div>
        <span class="p-float-label col-12 md:col-12 input-m">
          <InputText
            id="email"
            type="email"
            v-model="newUser.email"
            :class="{ 'has-error': errors.email }"
          />
          <label for="email">Email</label>
        </span>
        <div class="error-msg" v-if="errors.email">{{ errors.email }}</div>
        <span class="p-float-label col-12 md:col-12 input-m">
          <InputText
            id="role"
            type="text"
            v-model="newUser.role"
            :class="{ 'has-error': errors.role }"
          />
          <label for="role">Role</label>
        </span>
        <div class="error-msg" v-if="errors.role">{{ errors.role }}</div>
        <span class="p-float-label col-12 md:col-12 input-m">
          <InputText
            id="passowrd"
            type="password"
            v-model="newUser.password"
            :class="{ 'has-error': errors.password }"
          />
          <label for="password">Password</label>
        </span>
        <div class="error-msg" v-if="errors.password">
          {{ errors.password }}
        </div>
        <Button
          label="Submit"
          class="p-button-raised p-button-rounded"
          style="width: 40%"
          @click="sendSignUp"
        />
      </form>
    </template>
  </Card>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import InputText from "primevue/inputtext";
import Card from "primevue/card";
import Button from "primevue/button";
import axios from "axios";
import { useStore } from "../store";
// import { useRouter } from "vue-router";
import { useCookies } from "vue3-cookies";

const store = useStore();
// const router = useRouter();
const { cookies } = useCookies();

const newUser = ref({
  username: "",
  email: "",
  password: "",
  role: "",
});
const errors = ref({});

const sendSignUp = () => {
  axios
    .post("http://localhost:8080/api/signUp", newUser.value)
    .then((res) => {
      store.commit("updateLoginUser", res.data);
      cookies.set("loginUser", res.data);
    })
    .catch((err) => {
      errors.value = { ...err.response.data.errors };
    });
};
</script>

<style scoped>
.error-msg {
  font-size: 0.5em;
  color: red;
}
.input-m {
  margin: 1.5em auto 0 auto;
  display: inline-block;
  width: 100%;
}
.has-error {
  border-color: red;
}
</style>
