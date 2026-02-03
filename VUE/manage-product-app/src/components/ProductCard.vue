<script setup>
import { ref, watch } from "vue";
import ProductForm from "./ProductForm.vue";
import { validateProductForm } from "@/utils/formValidator";

const emit = defineEmits(["update", "delete"]);

const props = defineProps({
  product: {
    type: Object,
    required: true,
  },
});

const isEdit = ref(false);
const editName = ref("");
const formRef = ref(null);
const stockNum = ref(0);

// 수정 모드 진입 시 기존 값 세팅
watch(
  () => props.product,
  (p) => {
    editName.value = p.name;
  },
  { immediate: true },
);

// 저장된 date를 변환하는 함수
function formatDate(timestamp) {
  return new Date(timestamp).toLocaleString();
}

// 수정시 저장 버튼을 눌렀을 때 동작하는 함수
function handleSave() {
  if (!editName.value.trim()) {
    alert("상품명을 입력해주세요.");
    return;
  }

  const formData = formRef.value?.getFormData() || {};

  // 유효성 검사
  if (
    !validateProductForm({
      name: editName.value,
      ...formData,
    })
  ) {
    return;
  }

  emit("update", {
    id: props.product.id,
    name: editName.value.trim(),
    ...formData,
    updatedAt: Date.now(),
  });

  isEdit.value = false;
}

// 삭제버튼을 눌렀을 때 동작하는 함수
function handleCancel() {
  editName.value = props.product.name;
  isEdit.value = false;
}

// 재고를 조정하는 함수
function applyStockChange(amount) {
  if (amount === 0) {
    stockNum.value = 0;
    return;
  }

  if (props.product.stock + amount < 0) {
    alert("재고는 음수 상태가 될 수 없습니다!");
    stockNum.value = 0;
    return;
  }

  emit("update", {
    id: props.product.id,
    stock: props.product.stock + amount,
    updatedAt: Date.now(),
  });

  stockNum.value = 0;
}
</script>

<template>
  <div class="cardBorder">
    <section class="card">
      <!-- 헤더 -->
      <div class="cardHeader">
        <!-- 이름 -->
        <input v-if="isEdit" v-model="editName" type="text" />
        <strong v-else>{{ product.name }}</strong>

        <!-- 버튼 영역 -->
        <div>
          <!-- 읽기 모드 -->
          <template v-if="!isEdit">
            <button @click="isEdit = true">수정</button>
            <button @click="$emit('delete', product.id)">삭제</button>
          </template>

          <!-- 수정 모드 -->
          <template v-else>
            <button @click="handleSave">저장</button>
            <button @click="handleCancel">취소</button>
          </template>
        </div>
      </div>

      <!-- 본문 -->
      <div class="cardBody">
        <!-- 수정 모드 -->
        <template v-if="isEdit">
          <ProductForm ref="formRef" :initData="product" />
        </template>

        <!-- 읽기 모드 -->
        <template v-else>
          <p>
            카테고리 : <span>{{ product.category }}</span>
          </p>
          <p>
            가격 : <span>{{ product.price.toLocaleString() }} 원</span>
          </p>
          <p>
            재고 : <span>{{ product.stock }} 개</span>
          </p>
          <p>{{ product.describe || "설명 없음" }}</p>
          <p>
            등록일 : <span>{{ formatDate(product.createdAt) }}</span>
          </p>
          <p v-if="product.updatedAt">
            수정일 : <span>{{ formatDate(product.updatedAt) }}</span>
          </p>
        </template>
      </div>
    </section>

    <!-- 재고 조정 -->
    <section class="stock">
      <p>재고 조정</p>

      <input
        type="number"
        v-model.number="stockNum"
        placeholder="변경량(+ / -)"
      />

      <button @click="applyStockChange(stockNum)">적용</button>

      <button @click="applyStockChange(10)">입고(+10)</button>
    </section>
  </div>
</template>

<style>
.cardBorder {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 16px;
  transition: all 0.2s ease;
  box-sizing: border-box;
}

.cardBorder:hover {
  border-color: #555;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 카드 전체 */
.card {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 14px;
  background-color: #fff;
  margin-bottom: 12px;
  width: 290px;
  box-sizing: border-box;
}

/* 카드 헤더 */
.cardHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

/* 상품명 */
.cardHeader strong {
  font-size: 16px;
  max-width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 수정 시 이름 input */
.cardHeader input {
  padding: 6px 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
  width: 130px;
  box-sizing: border-box;
}

/* 버튼 영역 */
.cardHeader > div {
  display: flex;
  gap: 6px;
}

/* 공통 버튼 */
.cardHeader button,
.stock button {
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #d0d0d0;
  background-color: #f5f5f5;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 13px;
}

/* hover */
.cardHeader button:hover,
.stock button:hover {
  background-color: #e0e0e0;
}

/* 카드 본문 */
.cardBody {
  font-size: 14px;
  color: #333;
}

.cardBody p {
  margin: 3px 0;
  word-break: break-all;
}

.cardBody span {
  font-weight: 500;
}

/* 재고 조정 영역 */
.stock {
  margin-top: 10px;
  padding: 10px;
  border-radius: 10px;
  background-color: #fafafa;
  border: 1px dashed #d9d9d9;

  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  box-sizing: border-box;
}

.stock p {
  margin: 0;
  font-weight: 500;
  font-size: 14px;
}

/* 재고 input */
.stock input {
  width: 60px;
  padding: 6px 6px;
  border-radius: 6px;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

/* 입고 버튼 강조 */
.stock button:last-child {
  background-color: #555;
  color: #fff;
  border-color: #555;
}

.stock button:last-child:hover {
  background-color: #444;
}
</style>
