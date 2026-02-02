<script setup>
import { ref } from "vue";

import AddTodo from "./components/AddTodo.vue";
import JoinUser from "./components/JoinUser.vue";
import TodoList from "./components/TodoList.vue";

const userName = ref("");
const todos = ref([]);

// 이름을 감지하는 함수
function updateName(name) {
  userName.value = name;
}

// 할 일을 목록을 추가하는 함수
function addTodo(todo) {
  // 이름을 등록하지 않으면 할 일 목록 추가 불가
  if (!userName.value) {
    alert("이름을 먼저 등록해주세요!");
    return;
  }

  todos.value.push({
    id: Date.now(),
    text: todo.trim(),
    done: false,
  });
}

// done을 감지 하는 함수
function toggleTodo(id) {
  const todo = todos.value.find((t) => t.id === id);
  if (todo) todo.done = !todo.done;
}

// 삭제를 감지하는 함수
function removeTodo(id) {
  todos.value = todos.value.filter((t) => t.id !== id);
}
</script>

<template>
  <JoinUser class="box" @update-name="updateName" :todoNum="todos.length" />
  <AddTodo class="box" :canAdd="!!userName" @add-todo="addTodo" />
  <TodoList
    class="box"
    :list="todos"
    @toggle="toggleTodo"
    @remove="removeTodo"
  />
</template>
