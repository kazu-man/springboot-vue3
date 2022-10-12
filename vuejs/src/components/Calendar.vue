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
import { moveEvent } from "./EventRegisterModal/EventRegisterModalMethods";
import { useToast } from "primevue/usetoast";
import { useStore } from "../store";
const toast = useToast();
const store = useStore();
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
    //popoverは無理やり消す
    for (let popOverCloseBtn of popOvers) {
      popOverCloseBtn.click();
    }
    showModal.value = true;
    selectedEvent.value = info;
  },
  dateClick: function (info: EventClickArg) {
    showModal.value = true;
    selectedEvent.value = info;
  },
  eventDrop: async function (info: EventClickArg) {
    const toastInfo = await moveEvent(info, store);
    toast.add({ ...toastInfo, life: 3000 });
  },
});
</script>
