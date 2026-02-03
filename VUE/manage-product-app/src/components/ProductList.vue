<script setup>
import { computed } from "vue";
import ProductCard from "./ProductCard.vue";

const props = defineProps({
  products: {
    type: Array,
    default: () => [],
  },
  search: String,
  category: String,
  sort: String,
});

const emit = defineEmits(["update", "delete"]);

// 필터 + 정렬 적용
const filteredProducts = computed(() => {
  let list = [...props.products];

  // 검색 (상품명 / 설명)
  if (props.search?.trim()) {
    const keyword = props.search.toLowerCase();
    list = list.filter(
      (product) =>
        product.name.toLowerCase().includes(keyword) ||
        (product.describe && product.describe.toLowerCase().includes(keyword)),
    );
  }

  // 카테고리
  if (props.category && props.category !== "all") {
    list = list.filter((product) => product.category === props.category);
  }

  // 정렬
  switch (props.sort) {
    case "name":
      list.sort((a, b) => a.name.localeCompare(b.name, "ko"));
      break;

    case "price":
      list.sort((a, b) => b.price - a.price);
      break;

    case "stock":
      list.sort((a, b) => b.stock - a.stock);
      break;

    case "oldest":
      list.sort((a, b) => a.createdAt - b.createdAt);
      break;

    case "latest":
    default:
      list.sort((a, b) => b.createdAt - a.createdAt);
      break;
  }

  return list;
});
</script>

<template>
  <div>
    <h2>📦 상품 목록</h2>

    <!-- 상품 없음 -->
    <h3 v-if="products.length === 0">
      📝 등록된 상품이 없습니다. 위에서 새 상품을 추가해보세요!
    </h3>

    <!-- 필터 결과 없음 -->
    <h3 v-if="products.length > 0 && filteredProducts.length === 0">
      📭 조건에 맞는 상품이 없습니다.
    </h3>

    <!-- 가로 스크롤 영역 -->
    <div class="productScroll" v-if="filteredProducts.length > 0">
      <div
        v-for="product in filteredProducts"
        :key="product.id"
        class="productBox"
      >
        <ProductCard
          :product="product"
          @update="$emit('update', $event)"
          @delete="$emit('delete', $event)"
        />
      </div>
    </div>
  </div>
</template>

<style>
/* 가로 스크롤 컨테이너 */
.productScroll {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding: 15px;
  max-width: 100%;
  box-sizing: border-box;
}

/* 카드 박스 (고정 너비) */
.productBox {
  flex: 0 0 auto;
  width: 320px;
}

/* 스크롤바 스타일 (선택) */
.productScroll::-webkit-scrollbar {
  height: 6px;
}

.productScroll::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 4px;
}

.productScroll::-webkit-scrollbar-track {
  background-color: #f0f0f0;
}
</style>
