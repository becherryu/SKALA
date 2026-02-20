<template>
  <div>
    <h2>👤 사용자 등록</h2>
    <h3>
      이름:
      <input
        type="text"
        v-model="name"
        placeholder="이름을 입력하세요"
        @input="handleInput"
      />
    </h3>
    <h3 v-if="name !== ''">
      안녕하세요, {{ name }}님! 오늘 할 일은 {{ todoNum }}개 입니다.
    </h3>
    <h3 v-else>이름을 입력해주세요.</h3>
  </div>
</template>

<script setup>
import { ref } from "vue";

const emit = defineEmits(["update-name"]);
const name = ref("");

// 할 일의 개수를 받는 props
const props = defineProps({
  todoNum: {
    type: Number,
    default: 0,
  },
});

// 이름 입력을 감지하는 함수
function handleInput(e) {
  emit("update-name", e.target.value.trim());
}
</script>
