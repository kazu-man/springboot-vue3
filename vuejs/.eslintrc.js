module.exports = {
  env: {
    browser: true,
    es2021: true,
  },
  extends: [
    "plugin:vue/vue3-essential",
    "eslint:recommended",
    "@vue/typescript/recommended",
    "plugin:prettier/recommended",
  ],
  //   parserOptions: {
  //     ecmaVersion: 12,//env: es2021: true を設定している場合、内容が重複するため、設定する必要はない
  //     parser: "@typescript-eslint/parser", // "eslint:recommended"内のeslint-plugin-vueがvue-eslint-parserに変更するので削除
  //     sourceType: "module", // "eslint:recommended"内のeslint-plugin-vueなどが同じものを定義するので削除
  //   },
  plugins: ["vue", "@typescript-eslint"],
  rules: {},
};
