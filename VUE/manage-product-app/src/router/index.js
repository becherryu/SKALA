import { createRouter, createWebHistory } from "vue-router";
import DefaultLayout from "@/layouts/defaultLayout.vue";
import Home from "@/pages/Home.vue";
import About from "@/pages/About.vue";

const routes = [
  {
    path: "/",
    component: DefaultLayout,
    children: [
      {
        path: "",
        name: "Home",
        component: Home,
      },
      {
        path: "about",
        name: "About",
        component: About,
      },
    ],
  },
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
