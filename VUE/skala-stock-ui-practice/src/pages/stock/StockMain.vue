<script setup>
import { useRouter } from "vue-router";

import { ref } from "vue";
import PlayerStocks from "./components/PlayerStocks.vue";
import StockList from "./components/StockList.vue";
import StockRankings from "./components/StockRankings.vue";
import { clearPlayer } from "@/scripts/store-player";

const router = useRouter();
const activeTab = ref("market");

// 로그아웃 버튼
const logout = () => {
  clearPlayer();
  router.push("/");
};
</script>

<template>
  <div class="container-fluid">
    <!-- 헤더 -->
    <div class="row bg-body-tertiary align-items-center px-2">
      <div class="col">
        <span class="fs-2">SKALA STOCK Market</span>
      </div>

      <div class="col-auto ms-auto">
        <button class="btn btn-sm btn-outline-secondary" @click="logout">
          로그아웃
        </button>
      </div>
    </div>

    <div class="row px-3 pt-3">
      <div class="col">
        <ul class="nav nav-tabs">
          <li class="nav-item">
            <button
              class="nav-link"
              :class="{ active: activeTab === 'market' }"
              @click="activeTab = 'market'"
            >
              주식 목록/보유
            </button>
          </li>
          <li class="nav-item">
            <button
              class="nav-link"
              :class="{ active: activeTab === 'rank' }"
              @click="activeTab = 'rank'"
            >
              주식 순위
            </button>
          </li>
        </ul>
      </div>
    </div>

    <div v-if="activeTab === 'market'" class="row p-3">
      <div class="col border m-1 p-3 rounded-4">
        <StockList />
      </div>
      <div class="col border m-1 p-3 rounded-4">
        <PlayerStocks />
      </div>
    </div>

    <div v-else class="row p-3">
      <div class="col border m-1 p-3 rounded-4">
        <StockRankings />
      </div>
    </div>
  </div>
</template>
