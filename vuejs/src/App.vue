<template>
  <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link> |
    <router-link to="/login">Login</router-link> |
    <router-link to="/attendance">Attendance</router-link> |
  </nav>
  <router-view />
</template>
<script lang="ts">
import { defineComponent, onMounted } from "vue";
import { useCookies } from "vue3-cookies";
import { useStore } from "./store";

export default defineComponent({
  setup() {
    const { cookies } = useCookies();
    const store = useStore();
    //cookieに保存されているtoken,userを取り出す
    onMounted(() => {
      let tokenInCookie = cookies.get("token");
      store.commit("updateToken", tokenInCookie);
      let userInCookie = cookies.get("loginUser");
      store.commit("updateLoginUser", userInCookie);
    });
  },
});
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
