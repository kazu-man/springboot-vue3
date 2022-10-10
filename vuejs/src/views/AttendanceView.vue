<template>
  <Calendar :events="attendData" @addEvents="addEvents" />
</template>

<script lang="ts" setup>
import axios from "axios";
import { onMounted, Ref, ref, provide } from "vue";
import { useStore } from "../store";
import CalendarEvents from "../types/CalendarEvent";
import Calendar from "../components/Calendar.vue";

const store = useStore();

let attendData: Ref<CalendarEvents[]> = ref<CalendarEvents[]>([]);
const data = {
  id: store.getters.getUserId,
};

//@RequestParam String id, @RequestParam String name で受けたい場合はFormDataを使う
// const form = new FormData();
// form.append("id", "store.getters.getUserId");
// form.append("name", "store.getters.getUserId");

const getAttendData = () => {
  const headers = {
    "X-AUTH-TOKEN": "Bearer " + store.getters.getToken,
    "Content-Type": "application/json",
  };
  axios
    .post("http://localhost:8080/api/attendanceByUserId", data, {
      headers: headers,
    })
    .then((res) => {
      attendData.value = [attendData.value, ...res.data];
    })
    .catch(() => {
      alert("fail to get attend data");
    });
};

onMounted(() => {
  getAttendData();
});

const addEvents = (data: CalendarEvents) => {
  attendData.value = [attendData.value, data];
};

// addEvents();

provide("getAttendData", getAttendData);
provide("addEvents", addEvents);
</script>
