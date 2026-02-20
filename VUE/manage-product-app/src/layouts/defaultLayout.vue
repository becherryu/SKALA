<script setup>
import Sidebar from "@/components/Sidebar/Sidebar.vue";
</script>

<template>
  <div class="layout">
    <Sidebar />
    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.content {
  flex: 1;
  padding: 0 24px;
  overflow: hidden;
}
</style>
