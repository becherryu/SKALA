<script setup>
import { ref } from "vue";
import { useManager } from "@/composables/useManager";

const name = ref("");
const { manager, loginManager, logoutManager, loading } = useManager();

function handleLogin() {
  loginManager(name.value);
}
</script>

<template>
  <div class="login-box">
    <!-- 로그인 전 -->
    <div v-if="!manager">
      <input v-model="name" placeholder="관리자 이름 입력" />
      <button @click="handleLogin" :disabled="loading">등록하기</button>
    </div>

    <!-- 로그인 후 -->
    <div v-else>
      <p>
        <strong>{{ manager.name }}</strong> 관리자
      </p>
      <button class="logout" @click="logoutManager">로그아웃</button>
    </div>
  </div>
</template>

<style scoped>
.login-box {
  padding: 16px;
  border-radius: 12px;
  background: #133763;
  color: white;
  font-size: large;
}

input {
  width: 92%;
  padding: 8px;
  margin-bottom: 8px;
}

button {
  width: 100%;
  padding: 8px;
  cursor: pointer;
}

.logout {
  margin-top: 8px;
  background: #1a4c89;
  color: white;
}
</style>
