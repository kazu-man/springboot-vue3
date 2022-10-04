// store.ts
import { InjectionKey } from "vue";
import { createStore, useStore as baseUseStore, Store } from "vuex";

export interface State {
  token: string;
}

export const key: InjectionKey<Store<State>> = Symbol();

export const store = createStore<State>({
  state: {
    token: "",
  },
  mutations: {
    saveToken(state, token) {
      state.token = token;
    },
    removeToken(state) {
      state.token = "";
    },
  },
  actions: {
    saveToken({ commit }, token) {
      commit("saveToken", token);
    },
    removeToken({ commit }) {
      commit("removeToken");
    },
  },
  getters: {
    getToken(state) {
      return state.token;
    },
  },
});

// define your own `useStore` composition function
export function useStore() {
  return baseUseStore(key);
}
