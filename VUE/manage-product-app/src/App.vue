<script setup>
import { ref, reactive, computed, watch } from "vue";

import JoinManager from "@/components/JoinManager.vue";
import AddProduct from "@/components/AddProduct.vue";
import ProductList from "@/components/ProductList.vue";
import ProductFilter from "@/components/ProductFilter.vue";
import CountProduct from "@/components/CountProduct.vue";

const manager = ref("");
const products = reactive([]);
const search = ref("");
const category = ref("all");
const sort = ref("latest");
const alertedProducts = new Set();

// 관리자 등록을 감지하는 함수
function enrollManager(name) {
  manager.value = name;
}

// 상품을 추가하는 함수
function addProduct(newProduct) {
  // 관리자를 등록하지 않으면 상품 추가 불가
  if (!manager.value) {
    alert("관리자를 먼저 등록해주세요!");
    return;
  }

  products.push({
    id: crypto.randomUUID(),
    name: newProduct.name,
    category: newProduct.category,
    price: Number(newProduct.price),
    stock: Number(newProduct.stock),
    describe: newProduct.describe,
    createdAt: Date.now(),
  });
}

// 상품을 수정하는 함수
function updateProduct(updated) {
  const index = products.findIndex((p) => p.id === updated.id);
  if (index === -1) return;

  products[index] = {
    ...products[index],
    ...updated,
  };
}

// 상품을 삭제하는 함수
function deleteProduct(id) {
  const index = products.findIndex((p) => p.id === id);
  if (index !== -1) products.splice(index, 1);
}

// 상품 총 가격
const totalPrice = computed(() => {
  return products.reduce(
    (sum, product) => sum + product.price * product.stock,
    0,
  );
});

// 상품 항목 총 개수
const totalCount = computed(() => products.length);

// 상품 재고 부족 감시하기
watch(
  products,
  (newProducts) => {
    newProducts.forEach((p) => {
      if (p.stock <= 2 && !alertedProducts.has(p.id)) {
        alert(`⚠️ 재고 부족: ${p.name} (${p.stock}개)`);
        alertedProducts.add(p.id);
      }

      // 재고가 다시 회복되면 alert 기록 제거
      if (p.stock > 2 && alertedProducts.has(p.id)) {
        alertedProducts.delete(p.id);
      }
    });
  },
  { deep: true },
);
</script>

<template>
  <header>
    <JoinManager @manager="enrollManager" />
    <CountProduct
      class="countProduct"
      :total-count="totalCount"
      :total-price="totalPrice"
    />
    <ProductFilter
      v-model:search="search"
      v-model:category="category"
      v-model:sort="sort"
    />
  </header>
  <main>
    <section>
      <AddProduct :canAdd="!!manager" @add-product="addProduct" />
    </section>
    <section>
      <ProductList
        :products="products"
        :search="search"
        :category="category"
        :sort="sort"
        @update="updateProduct"
        @delete="deleteProduct"
      />
    </section>
  </main>
</template>
