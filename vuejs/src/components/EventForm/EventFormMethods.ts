import { ref, Ref, watchEffect } from "vue";
import CalendarEvent from "../../types/CalendarEvent";
import { EventApi } from "@fullcalendar/vue3";

//eventApi管理外のカスタム項目の値
interface extendedPrps {
  comment?: string;
}

interface returnType {
  updatedData: Ref<CalendarEvent>;
  startDate: Ref<Date>;
  endDate: Ref<Date>;
}

export default function EventFormMethods(
  event: EventApi,
  dateStr: string | undefined
): returnType {
  //v-model用の変数に詰め替える
  const updatedData = ref<CalendarEvent>({
    id: 0,
    title: "",
    start: "",
    end: "",
    comment: "",
    backgroundColor: "",
  });

  const startDate = ref(dateStr ? new Date(dateStr) : new Date());
  const endDate = ref(dateStr ? new Date(dateStr) : new Date());

  //更新の場合
  if (event) {
    // eventオブジェクトの必要なデータを含むデータを全てコピー
    const form = {
      ...event._def,
      ...event._instance,
      ...event._instance?.range,
      ...(event.extendedProps as extendedPrps),
    };

    //fullCalendarのtimeZone設定がよくわからず、時間がずれるので調整
    const startDateObj = adjustTimeZone(form.start);
    const endDateObj = adjustTimeZone(form.end);

    //datePickerのv-modelに渡す表示用の変数
    startDate.value = startDateObj;
    endDate.value = endDateObj;

    //v-model用の変数に詰め替える
    updatedData.value = {
      id: form.publicId as unknown as number,
      title: form.title as string,
      start: "",
      end: "",
      comment: form.comment as string,
      backgroundColor: form.ui.backgroundColor,
    };
  }

  watchEffect(() => {
    updatedData.value.start = getTimeStr(startDate.value);
    updatedData.value.end = getTimeStr(endDate.value);
  });

  return {
    updatedData,
    startDate,
    endDate,
  };
}

//yyyy-MM-dd hh:mm:ss に変換
export function getTimeStr(d: Date) {
  const yyyy = d.getUTCFullYear();
  const MM = ("0" + (d.getMonth() + 1)).slice(-2);
  const dd = ("0" + d.getDate()).slice(-2);
  const hh = ("0" + d.getHours()).slice(-2);
  const mm = ("0" + d.getMinutes()).slice(-2);
  const ss = ("0" + d.getSeconds()).slice(-2);
  const result = yyyy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss;
  return result;
}

//9時間のズレを調整
function adjustTimeZone(target: Date | undefined) {
  if (!target) target = new Date();
  const date = new Date(target);
  date.setHours(date.getHours() - 9);
  return date;
}
