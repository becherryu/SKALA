<script setup>
import { ref } from "vue";

const emit = defineEmits(["manager"]);
const manager = ref("");

// 이름 입력을 감지하는 함수
function handleInputName(e) {
  emit("manager", e.target.value.trim());
}
</script>

<template>
  <div class="joinManager">
    <h2>🛍️ 상품 관리 시스템</h2>
    <section class="nameSection">
      <h3>
        관리자 이름 :
        <input
          type="text"
          v-model="manager"
          placeholder="이름을 입력하세요"
          @input="handleInputName"
        />
      </h3>
      <h3 v-if="manager !== ''">안녕하세요, {{ manager }}님!</h3>
      <h3 v-else>관리자 이름을 입력해주세요.</h3>
    </section>
  </div>
</template>

<style scoped>
.joinManager {
  width: 350px;
  border: 1px solid #d9d9d9;
  border-radius: 15px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 15px;
}

.nameSection {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
