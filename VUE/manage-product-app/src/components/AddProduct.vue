<script setup>
import { ref } from "vue";
import ProductForm from "./ProductForm.vue";
import { validateProductForm } from "@/utils/formValidator";

const emit = defineEmits(["add-product"]);

const productName = ref("");
const isDetail = ref(false);
const productFormRef = ref(null);

function hasDetailInput(formData) {
  return (
    formData.category ||
    Number(formData.price) > 0 ||
    Number(formData.stock) > 0 ||
    formData.describe?.trim()
  );
}

function handleAddBtn() {
  if (!productName.value.trim()) {
    alert("상품을 입력해주세요!");
    return;
  }

  let detail = {
    category: "기타",
    price: 0,
    stock: 0,
    describe: "",
  };

  // 상세모드일 때만 자식 데이터 가져오기
  if (isDetail.value && productFormRef.value) {
    const formData = productFormRef.value.getFormData();

    if (hasDetailInput(formData)) {
      if (!validateProductForm(formData)) return;

      detail = {
        ...detail,
        ...formData,
      };
    }
  }

  emit("add-product", {
    name: productName.value.trim(),
    ...detail,
  });

  // 초기화
  productName.value = "";
  isDetail.value = false;
}
</script>
<template>
  <div class="addProduct">
    <h2>➕ 새 상품 추가</h2>
    <div class="addSection">
      <input
        type="text"
        v-model="productName"
        placeholder="상품명을 작성해주세요"
      />
      <button :disabled="productName.trim() === ''" @click="handleAddBtn">
        추가
      </button>
    </div>
    <div class="detailButton">
      <button @click="isDetail = true">상세 모드</button>
    </div>

    <div v-if="isDetail">
      <ProductForm ref="productFormRef" />
      <div class="simpleButton">
        <button @click="isDetail = false">간단 모드</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.addProduct {
  width: 350px;
  border: 1px solid #d9d9d9;
  border-radius: 15px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 15px;
  min-height: 500px;
}

.addSection {
  display: flex;
  flex-direction: row;
  gap: 20px;
  border: 1px solid #d9d9d9;
  border-radius: 15px;
  padding: 20px;
  margin-bottom: 20px;
}

.detailButton,
.simpleButton {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}
</style>
