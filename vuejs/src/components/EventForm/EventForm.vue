<template>
  <Card style="width: 100%; margin: 0.5em 0">
    <template #content>
      <form class="grid p-fluid m-8">
        <span class="p-float-label input-m">
          <InputText id="title" type="text" v-model="updatedData.title" />
          <label for="title">Title</label>
        </span>
        <div class="field p-grid" style="width: 100%; margin: 0.5em 0 0 0">
          <span class="p-float-label input-m p-col-6" style="padding: 0">
            <Calendar
              id="start"
              inputId="time24"
              v-model="startDate"
              :showTime="true"
              :showSeconds="true"
            />
            <label for="start">Start</label>
          </span>
          <span class="p-float-label input-m p-col-6" style="padding: 0">
            <Calendar
              id="end"
              inputId="time24"
              v-model="endDate"
              :showTime="true"
              :showSeconds="true"
            />
            <label for="end">End</label>
          </span>
        </div>
        <div class="p-float-label" style="margin: 1.5em auto 0 auto">
          <Textarea
            id="comment"
            v-model="updatedData.comment"
            :autoResize="true"
            rows="5"
            cols="30"
          />
          <label for="comment">comment</label>
        </div>
        <span class="p-float-label input-m">
          <InputText
            id="backgroundColor"
            type="text"
            v-model="updatedData.backgroundColor"
          />
          <label for="backgroundColor">Background Color</label>
        </span>
      </form>
    </template>
  </Card>
</template>

<script lang="ts" setup>
import { inject, watch } from "vue";
import Textarea from "primevue/textarea";
import Calendar from "primevue/calendar";
import { EventClickArg } from "@fullcalendar/vue3";
import CalendarEvent from "../../types/CalendarEvent";
import EventFormMethods from "./EventFormMethods";

const dialogRef = inject("dialogRef");
const { event, dateStr } = dialogRef.value.data.selectedEvent as EventClickArg;
const { updatedData, startDate, endDate } = EventFormMethods(event, dateStr);
const updateData = dialogRef.value.data.updateData as (
  data: CalendarEvent
) => void;

//???????????????????????????
const setNewData = () => {
  updateData(updatedData.value);
};

//???????????????????????????
setNewData();

//??????????????????????????????
watch(updatedData, () => {
  setNewData();
});
</script>
