import axios from "axios";
import { store } from "../store";

export function jwtInterceptor() {
  //tokenが含まれる場合はstoreに保存する
  axios.interceptors.response.use((response) => {
    const newToken = response.headers["x-auth-token"];
    if (newToken) {
      store.commit("updateToken", newToken);
    }

    return response;
  });
}
