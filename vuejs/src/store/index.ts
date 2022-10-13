// store.ts
import { User } from "@/types/User";
import { InjectionKey } from "vue";
import { createStore, useStore as baseUseStore, Store } from "vuex";
import { useCookies } from "vue3-cookies";
const { cookies } = useCookies();

export interface State {
  token: string;
  loginUser?: User;
}

export const key: InjectionKey<Store<State>> = Symbol();

export const store = createStore<State>({
  state: {
    token: "",
    loginUser: undefined,
  },
  mutations: {
    updateToken(state, token) {
      state.token = token;
    },
    updateLoginUser(state, loginUser) {
      state.loginUser = loginUser;
    },
  },
  actions: {
    updateToken({ commit }, token) {
      cookies.set("token", token);
      commit("updateToken", token);
    },
    removeToken({ commit }) {
      commit("updateToken", "");
    },
    updateLoginUser({ commit }, loginUser) {
      cookies.set("loginUser", loginUser);
      commit("updateLoginUser", loginUser);
    },
    removeLoginUser({ commit }) {
      commit("updateLoginUser", undefined);
    },
  },
  getters: {
    getToken(state) {
      return state.token;
    },
    getLoginUser(state) {
      return state.loginUser;
    },
    getUserId(state) {
      return state.loginUser ? state.loginUser.id : undefined;
    },
  },
});

// define your own `useStore` composition function
export function useStore() {
  return baseUseStore(key);
}
