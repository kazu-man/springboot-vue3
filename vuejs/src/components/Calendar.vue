<template>
  <div>
    <FullCalendar :options="options" />
  </div>
</template>

<script lang="ts" setup>
import { ref, toRefs, PropType, defineProps, defineEmits, Ref } from "vue";
import "@fullcalendar/vue3";
import FullCalendar, {
  CalendarOptions,
  EventClickArg,
} from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import CalendarEvents from "../types/CalendarEvents";

//propsの型定義
type Props = {
  events: {
    type: PropType<CalendarEvents[]>;
    required: true;
  };
};
//definePropsでpropsを取得
const props = defineProps<Props>();
//propsからevetnsをrefにして取得
const { events } = toRefs(props);

//emitの型定義
type Emits = {
  (e: "addEvents"): void;
};
//emitをdefineEmitsから取得
const emit = defineEmits<Emits>();

//カレンダーのオプション設定
const options: Ref<CalendarOptions> = ref({
  plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
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
    alert("Event: " + info.event.title);
    alert("Coordinates: " + info.jsEvent.pageX + "," + info.jsEvent.pageY);
    alert("View: " + info.view.type);

    // change the border color just for fun
    info.el.style.borderColor = "red";
  },
  dateClick: function (info: EventClickArg) {
    // alert("Clicked on: " + info.dateStr);
    // alert("Coordinates: " + info.jsEvent.pageX + "," + info.jsEvent.pageY);
    // alert("Current view: " + info.view.type);
    // // change the day's background color just for fun
    // info.dayEl.style.backgroundColor = "red";
    emit("addEvents");
  },
});
</script>
