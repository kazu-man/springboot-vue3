export default interface CalendarEvent {
  id: number;
  title?: string;
  start: string;
  end?: string;
  comment?: string;
  backgroundColor: string;
}
