<template>
  <Button
    label="Login Check"
    class="p-button-raised p-button-rounded"
    style="width: 40%"
    @click="submit"
  />
</template>

<script lang="ts">
import { defineComponent } from "vue";
import Button from "primevue/button";
import axios from "axios";
import { useStore } from "../store";

export default defineComponent({
  components: { Button },
  setup() {
    const store = useStore();
    const submit = () => {
      axios
        .get("http://localhost:8080/api/test", {
          headers: {
            "X-AUTH-TOKEN": "Bearer " + store.getters.getToken,
          },
        })
        .then((res) => {
          console.log("取得した");
          console.log(res);
          alert("ログイン中");
        })
        .catch((e) => {
          alert("ログインしていません");
          console.log(e);
        });
    };
    return { submit };
  },
});
</script>
