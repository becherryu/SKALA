<template>
  <div>
    <h2>➕ 새 할 일 추가</h2>
    <div class="addSection">
      <input type="text" v-model="todo" placeholder="할 일을 입력하세요" />
      <button :disabled="todo.trim() === ''" @click="handleAdd">추가</button>
    </div>
    <h3 v-if="todo.trim() !== ''">추가할 내용 : {{ todo }}</h3>
  </div>
</template>
<script setup>
import { ref } from "vue";

const emit = defineEmits(["add-todo"]);
const todo = ref("");

// 할 일을 추가하는 함수
function handleAdd() {
  emit("add-todo", todo.value);
  todo.value = "";
}
</script>
<style>
.addSection {
  display: flex;
  flex-direction: row;
  gap: 20px;
}
</style>
