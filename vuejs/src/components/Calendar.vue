<template>
  <div>
    <FullCalendar :options="options" />
  </div>
  <EventRegisterModal
    :selectedEvent="selectedEvent"
    :showModal="showModal"
    @closeModal="closeModal"
  />
</template>

<script lang="ts" setup>
import { ref, toRefs, PropType, defineProps, Ref } from "vue";
import "@fullcalendar/vue3";
import FullCalendar, {
  CalendarOptions,
  EventClickArg,
} from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import CalendarEvent from "../types/CalendarEvent";
import EventRegisterModal from "./EventRegisterModal/EventRegisterModal.vue";

//propsの型定義
type Props = {
  events: {
    type: PropType<CalendarEvent[]>;
    required: true;
  };
};
//definePropsでpropsを取得
const props = defineProps<Props>();
//propsからevetnsをrefにして取得
const { events } = toRefs(props);

const showModal = ref(false);
const closeModal = () => {
  showModal.value = false;
};
const selectedEvent = ref({});

//カレンダーのオプション設定
const options: Ref<CalendarOptions> = ref({
  // timeZone: "UTC",
  plugins: [
    dayGridPlugin,
    timeGridPlugin,
    interactionPlugin,
    // momentTimezonePlugin,
  ],
  headerToolbar: {
    left: "prev,next today",
    center: "title",
    right: "dayGridMonth,timeGridWeek,timeGridDay",
  },
  editable: true,
  selectable: true,
  selectMirror: true,
  dayMaxEvents: true,
  events: events,
  eventClick: function (info: EventClickArg) {
    const popOvers = document.getElementsByClassName("fc-popover-close");
    for (let popOverCloseBtn of popOvers) {
      popOverCloseBtn.click();
    }
    showModal.value = true;
    selectedEvent.value = info;
  },
  dateClick: function (info: EventClickArg) {
    // alert("Clicked on: " + info.dateStr);
    // alert("Coordinates: " + info.jsEvent.pageX + "," + info.jsEvent.pageY);
    // alert("Current view: " + info.view.type);
    // // change the day's background color just for fun
    // info.dayEl.style.backgroundColor = "red";
    // emit("addEvents");
    showModal.value = true;
    selectedEvent.value = info;
  },
  eventDrop: function (info: EventClickArg) {
    console.log(info);
  },
});
</script>
