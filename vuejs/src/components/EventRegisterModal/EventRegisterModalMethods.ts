import { ToastServiceMethods } from "primevue/toastservice";
import axios, { AxiosRequestHeaders } from "axios";
import CalendarEvent from "../../types/CalendarEvent";
import { Store } from "vuex";
import { State } from "@/store";

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

const createEventFunc = async (
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

const updateEventFunc = async (
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
