<script setup>
import { storeToRefs } from "pinia";
import { useAuthStore } from "@/stores/auth";

import CountProduct from "@/components/About/CountProduct.vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import CategoryBarChart from "@/components/About/CategoryBarChart.vue";
import LowStockList from "@/components/About/LowStockList.vue";
import { useProductStats } from "@/composables/useProductStats";

const authStore = useAuthStore();
const { isLoggedIn } = storeToRefs(authStore);
const { totalCount, totalPrice, categoryStats, lowStockProducts, loading } =
  useProductStats();
</script>

<template>
  <section class="dashboard">
    <!-- 로그인 안 함 -->
    <h2 v-if="!isLoggedIn" class="emptyText">
      로그인 후 상품 현황을 조회할 수 있습니다.
    </h2>

    <div v-else>
      <LoadingSpinner v-if="loading" />

      <div v-else class="dashboardGrid">
        <!-- KPI -->
        <div class="kpiSection">
          <CountProduct :total-count="totalCount" :total-price="totalPrice" />
        </div>

        <!-- 차트 -->
        <div class="chartSection">
          <CategoryBarChart :stats="categoryStats" />
        </div>

        <!-- 재고 부족 테이블 -->
        <div class="lowStockSection">
          <LowStockList :products="lowStockProducts" />
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.dashboard {
  width: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

/* 그리드 설정 */
.dashboardGrid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: auto 1fr;
  gap: 24px;
}

/* KPI */
.kpiSection {
  grid-column: 1 / 2;
}

/* 차트 */
.chartSection {
  grid-column: 2 / 3;
}

/* 재고 부족 테이블 (전체) */
.lowStockSection {
  grid-column: 1 / 3;
}
</style>
