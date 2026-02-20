<script setup>
import { computed } from "vue";
import ProductCard from "./ProductCard.vue";

const props = defineProps({
  products: {
    type: Array,
    default: () => [],
  },
  isLoggedIn: {
    type: Boolean,
    required: true,
  },
  page: Number,
  totalCount: Number,
  limit: Number,
});

const emit = defineEmits(["edit", "delete", "changePage"]);

const totalPages = computed(() => Math.ceil(props.totalCount / props.limit));
</script>

<template>
  <div class="productList">
    <h2>상품 목록</h2>

    <!-- 로그인 안 함 -->
    <p v-if="!isLoggedIn" class="emptyText">
      로그인 후 상품 목록을 조회할 수 있습니다.
    </p>

    <!-- 로그인 O · 상품 없음 -->
    <p v-else-if="products.length === 0" class="emptyText">
      조회 가능한 상품이 없습니다.
    </p>

    <!-- 상품 목록 -->
    <div v-else class="productScroll">
      <div v-for="product in products" :key="product.id" class="productBox">
        <ProductCard
          :product="product"
          @edit="$emit('edit', $event)"
          @delete="$emit('delete', $event)"
        />
      </div>
    </div>

    <!-- 페이지네이션 -->
    <div v-if="totalPages >= 1 && isLoggedIn" class="pagination">
      <button
        v-for="p in totalPages"
        :key="p"
        :class="{ active: p === page }"
        @click="$emit('changePage', p)"
      >
        {{ p }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.productList {
  width: 680px;
  height: 92%;
  border: 1px solid #d9d9d9;
  border-radius: 15px;
  padding: 0 20px 20px 20px;
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 500px;
  overflow: hidden;
}

.productScroll {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 2열 */
  gap: 16px;
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  box-sizing: border-box;
  padding: 10px;
}

.productBox {
  width: 100%;
}

.emptyText {
  margin-top: 40px;
  text-align: center;
  color: #8c8c8c;
  font-size: large;
}

.productScroll::-webkit-scrollbar {
  width: 6px;
}

.productScroll::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 4px;
}

.productScroll::-webkit-scrollbar-track {
  background-color: #f0f0f0;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 12px;
}

.pagination button {
  padding: 6px 12px;
  border-radius: 6px;
  border: 1px solid #d9d9d9;
  background: white;
  cursor: pointer;
}

.pagination button.active {
  background: #133763;
  color: white;
  font-weight: bold;
}
</style>
