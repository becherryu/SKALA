<script setup>
import LoginManager from "@/components/Sidebar/LoginManager.vue";
</script>

<template>
  <aside class="sidebar">
    <h2 class="logo">📦 상품 관리 시스템</h2>

    <LoginManager />

    <nav class="menu">
      <RouterLink to="/">상품 관리</RouterLink>
      <RouterLink to="/about">상품 현황</RouterLink>
    </nav>
  </aside>
</template>

<style scoped>
.sidebar {
  height: 90%;
  width: 220px;
  padding: 20px;
  background: #0c2340;
  color: white;
  border-radius: 12px;
}

.menu {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
}

.menu a {
  color: white;
  text-decoration: none;
  opacity: 0.7;
  font-size: 23px;
}

.menu a.router-link-active {
  opacity: 1;
  font-weight: bold;
}
</style>
