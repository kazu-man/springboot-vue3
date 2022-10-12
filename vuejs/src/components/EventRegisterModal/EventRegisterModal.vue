<template>
  <div>
    <Toast />
    <DynamicDialog style="z-index: 999999999" />
  </div>
</template>

<script lang="ts">
import { h, watchEffect, toRefs, inject, defineComponent } from "vue";
import { useDialog } from "primevue/usedialog";
import { useToast } from "primevue/usetoast";
import Toast from "primevue/toast";
import Button from "primevue/button";
import EventForm from "../EventForm/EventForm.vue";
import DynamicDialog from "primevue/dynamicdialog";
import { closeEvent, updateEvent } from "./EventRegisterModalMethods";
import CalendarEvent from "../../types/CalendarEvent";
import { useStore } from "../../store";

export default defineComponent({
  components: { DynamicDialog, Toast },
  props: {
    showModal: {
      type: Boolean,
      required: true,
    },
    selectedEvent: {
      type: Object,
      required: true,
    },
  },
  emits: {
    closeModal: Function,
  },
  setup(props, { emit }) {
    const dialog = useDialog();
    const toast = useToast();
    const getAttendData = inject("getAttendData") as () => void;
    const addEvents = inject("addEvents") as (data: CalendarEvent) => void;
    const { showModal, selectedEvent } = toRefs(props);
    const store = useStore();

    let newData: CalendarEvent = {
      id: 0,
      title: "",
      start: "",
      end: "",
      comment: "",
      backgroundColor: "",
    };

    const updateData = (data: CalendarEvent) => {
      newData = data;
    };

    const showProducts = () => {
      const dialogRef = dialog.open(EventForm, {
        props: {
          header: "Modify Evetns",
          style: {
            width: "50vw",
            zIndex: "999999",
          },
          breakpoints: {
            "960px": "75vw",
            "640px": "90vw",
          },
          modal: true,
        },
        data: {
          selectedEvent: selectedEvent.value,
          updateData,
        },
        templates: {
          footer: () => {
            return [
              h(Button, {
                label: "No",
                icon: "pi pi-times",
                onClick: () => dialogRef.close({ buttonType: "CLOSE" }),
                class: "p-button-text",
              }),
              h(Button, {
                label: "Yes",
                icon: "pi pi-check",
                onClick: () =>
                  dialogRef.close({
                    buttonType: "UPDATE",
                    targetData: newData,
                  }),
                // autofocus: true,
              }),
            ];
          },
        },
        onClose: async (options) => {
          if (options?.data && options?.data.buttonType != "CLOSE") {
            const result = await updateEvent(newData, store);
            if (result) {
              addEvents(newData);
              closeEvent(options, toast, getAttendData);
              emit("closeModal");
            } else {
              alert("UPDATE FAIL");
            }
          }
        },
      });
    };

    watchEffect(() => {
      if (showModal.value) {
        showProducts();
      }
    });

    return { dialog, showProducts, Toast };
  },
});
</script>
