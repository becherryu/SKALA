<template>
  <div>
    <h2>📝 할 일 목록</h2>
    <h3 class="innerBox" v-if="list.length === 0">
      📝 할 일이 없습니다. 위에서 추가해보세요!
    </h3>

    <div v-if="list.length !== 0" class="todoNum">
      남은 할 일 개수 : {{ remainingCount }}
    </div>
    <div v-for="item in list" :key="item.id" class="innerBox">
      <input
        type="checkbox"
        :checked="item.done"
        @change="emit('toggle', item.id)"
      />

      <span :class="{ done: item.done }">
        {{ item.text }}
      </span>

      <button @click="emit('remove', item.id)">삭제</button>
    </div>
  </div>
</template>
<script setup>
import { computed } from "vue";

const props = defineProps({
  list: {
    type: Array,
    default: () => [],
  },
});

// done과 삭제 감지
const emit = defineEmits(["toggle", "remove"]);

// 남은 할 일의 개수를 세는 함수
const remainingCount = computed(() => {
  return props.list.filter((item) => !item.done).length;
});
</script>
<style>
.innerBox {
  gap: 10px;
  width: 45vw;
  display: flex;
  align-items: center;
  border: 1px solid #d9d9d9;
  border-radius: 15px;
  padding: 20px;
  margin-bottom: 15px;
  font-size: 1em;
  font-weight: bold;
}

.done {
  text-decoration: line-through;
  color: #9e9e9e;
}

.todoNum {
  margin-bottom: 12px;
  font-weight: 600;
  font-size: 1.2em;
  color: #333;
}
</style>
