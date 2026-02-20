<script setup>
import { ref, reactive } from "vue";
import { storeToRefs } from "pinia";
import { useAuthStore } from "@/stores/auth";

import AddProduct from "@/components/Home/AddProduct.vue";
import ProductList from "@/components/Home/ProductList.vue";
import ProductFilter from "@/components/Home/ProductFilter.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";

import { useManager } from "@/composables/useManager";
import { useProductFilter } from "@/composables/useProductFilter";
import { useProducts } from "@/composables/useProducts";
import { useProductAlert } from "@/composables/useProductAlert";

const { manager } = useManager();
const { search, category, sort } = useProductFilter();
const authStore = useAuthStore();
const { isLoggedIn } = storeToRefs(authStore);

const page = ref(1);
const limit = 6;

const filters = reactive({ search, category, sort, page, limit });

const {
  products,
  loading,
  totalCount,
  addProduct,
  editProduct,
  deleteProduct,
} = useProducts(manager, filters);

useProductAlert(products, isLoggedIn);

function changePage(nextPage) {
  page.value = nextPage;
}
</script>

<template>
  <header>
    <ProductFilter
      v-model:search="search"
      v-model:category="category"
      v-model:sort="sort"
    />
    <AddProduct :canAdd="!!manager" @add-product="addProduct" />
  </header>

  <main class="main-area">
    <LoadingSpinner v-if="loading" />

    <ProductList
      :products="products"
      :is-logged-in="isLoggedIn"
      :page="page"
      :limit="limit"
      :total-count="totalCount"
      @changePage="changePage"
      @edit="editProduct"
      @delete="deleteProduct"
    />
  </main>
</template>

<style scoped>
header {
  display: flex;
  flex-direction: column;
  width: 45%;
}

.main-area {
  position: relative;
}
</style>
