import { ToastServiceMethods } from "primevue/toastservice";
import axios, { AxiosRequestHeaders } from "axios";
import CalendarEvent from "../../types/CalendarEvent";
import { Store } from "vuex";
import { State } from "@/store";
import EventFormMethods from "../EventForm/EventFormMethods";
import { EventClickArg } from "@fullcalendar/vue3";

export const closeEvent = (
  options: any,
  toast: ToastServiceMethods,
  getAttendData: () => void
) => {
  const data = options.data;
  const buttonType = data.buttonType;
  let summary_and_detail = {};
  switch (data.buttonType) {
    case "UPDATE":
      getAttendData();
      summary_and_detail = {
        summary: "UPDATED",
        detail: `Pressed '${buttonType}' button`,
      };
      break;
    case "CLOSE":
      summary_and_detail = {
        summary: "No Product Selected",
        detail: `Pressed '${buttonType}' button`,
      };
      break;
  }

  toast.add({ severity: "info", ...summary_and_detail, life: 3000 });
};

export const updateEvent = async (
  newData: CalendarEvent,
  store: Store<State>
) => {
  const headers = {
    "X-AUTH-TOKEN": "Bearer " + store.getters.getToken,
    "Content-Type": "application/json",
  };
  let result = false;

  if (newData.id > 0) {
    result = await updateEventFunc(headers, newData);
  } else {
    result = await createEventFunc(headers, newData);
  }

  return result;
};

export const moveEvent = async (info: EventClickArg, store: Store<State>) => {
  const { event } = info;
  const { updatedData } = EventFormMethods(event, undefined);
  const result = await updateEvent(updatedData.value, store);
  const toastInfo = {
    summary: "",
    detail: "",
    severity: "",
  };

  if (result) {
    toastInfo.summary = "Event Move";
    toastInfo.detail = `${updatedData.value.start} ~ ${updatedData.value.end}`;
    toastInfo.severity = "info";
  } else {
    toastInfo.summary = "Something went wrong..";
    toastInfo.detail = "Failed to update the event";
    toastInfo.severity = "danger";
  }

  return toastInfo;
};

export const createEventFunc = async (
  headers: AxiosRequestHeaders,
  newData: CalendarEvent
) => {
  let result = false;
  await axios
    .post("http://localhost:8080/api/attendance", newData, {
      headers: headers,
    })
    .then(() => {
      result = true;
    })
    .catch(() => {
      alert("fail to create");
      result = false;
    });
  return result;
};

export const updateEventFunc = async (
  headers: AxiosRequestHeaders,
  newData: CalendarEvent
) => {
  let result = false;
  await axios
    .put("http://localhost:8080/api/attendance", newData, {
      headers: headers,
    })
    .then(() => {
      result = true;
    })
    .catch(() => {
      alert("fail to update");
      result = false;
    });
  return result;
};

export default { closeEvent, moveEvent, createEventFunc, updateEventFunc };
