<template>
  <Calendar :events="attendData" @addEvents="addEvents" />
</template>

<script lang="ts" setup>
import axios from "axios";
import { onMounted, Ref, ref } from "vue";
import { useStore } from "../store";
import CalendarEvents from "../types/CalendarEvents";
import Calendar from "../components/Calendar.vue";

const store = useStore();

let attendData: Ref<CalendarEvents[]> = ref<CalendarEvents[]>([]);

onMounted(() => {
  const data = {
    id: store.getters.getUserId,
  };

  //@RequestParam String id, @RequestParam String name で受けたい場合はFormDataを使う
  // const form = new FormData();
  // form.append("id", "store.getters.getUserId");
  // form.append("name", "store.getters.getUserId");

  const headers = {
    "X-AUTH-TOKEN": "Bearer " + store.getters.getToken,
    "Content-Type": "application/json",
  };

  axios
    .post("http://localhost:8080/api/attendanceByUserId", data, {
      headers: headers,
    })
    .then((res) => {
      console.log(res.data);
      attendData.value = res.data;
    })
    .catch(() => {
      console.log("fail to get attend data");
    });
});

const addEvents = () => {
  attendData.value = [{ id: 13, title: "NEW", start: "2022-10-02 12:00:00" }];
};
</script>
