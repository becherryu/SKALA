<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  initData: {
    type: Object,
    default: null,
  },
});

const emit = defineEmits(["submit"]);

const category = ref("");
const price = ref(0);
const stock = ref(0);
const describe = ref("");

watch(
  () => props.initData,
  (data) => {
    if (!data) return;

    category.value = data.category ?? "";
    price.value = data.price ?? 0;
    stock.value = data.stock ?? 0;
    describe.value = data.describe ?? "";
  },
  { immediate: true },
);

// 카테고리 옵션
const categories = ["전자기기", "의류", "식품", "도서", "기타"];

function getFormData() {
  return {
    category: category.value,
    price: price.value,
    stock: stock.value,
    describe: describe.value.trim(),
  };
}

// 부모에서 접근 가능하게 노출
defineExpose({
  getFormData,
});
</script>

<template>
  <div class="inputForm">
    <section class="formSection">
      카테고리:
      <select v-model="category">
        <option value="" disabled>선택하세요</option>
        <option v-for="item in categories" :key="item" :value="item">
          {{ item }}
        </option>
      </select>
    </section>
    <section class="formSection">
      가격: <input type="number" v-model="price" />
    </section>
    <section class="formSection">
      재고: <input type="number" v-model="stock" />
    </section>
    <section class="formSection">
      설명:
      <textarea v-model="describe"></textarea>
    </section>
  </div>
</template>

<style>
select {
  border-radius: 8px;
  border: 1px solid gray;
  padding: 6px 8px;
  font-size: 14px;
  font-family: inherit;
  width: 200px;
}

textarea {
  width: 200px;
  min-height: 60px;
  resize: vertical;
}

.inputForm {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  margin: 15px 0px;
}

.formSection {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 270px;
  gap: 10px;
}
</style>
