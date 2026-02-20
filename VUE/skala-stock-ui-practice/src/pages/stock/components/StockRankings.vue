<script setup>
import { ref, reactive, onMounted } from "vue";
import apiCall from "@/scripts/api-call";

const loading = ref(false);

const rankings = reactive({
  owned: [],
  volume: [],
  recommend: [],
  search: [],
});

// 랜덤 추출 계산
const shuffle = (arr) => {
  const copy = [...arr];
  for (let i = copy.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
};

// 주식 보유 순위 랭킹
const buildOwnedRanking = (stocks, playerStocks) => {
  const qtyMap = new Map();
  for (const ps of playerStocks) {
    const current = qtyMap.get(ps.stockId) || 0;
    qtyMap.set(ps.stockId, current + (ps.quantity || 0));
  }

  const result = [];
  for (const stock of stocks) {
    const qty = qtyMap.get(stock.id) || 0;
    if (qty <= 0) continue;
    result.push({
      id: stock.id,
      stockName: stock.stockName,
      stockPrice: stock.stockPrice,
      totalQuantity: qty,
    });
  }

  result.sort((a, b) => b.totalQuantity - a.totalQuantity);
  return result.slice(0, 10);
};

// 거래량이 많은 주식 랭킹
const buildRandomVolume = (stocks) => {
  const result = stocks.map((s) => ({
    id: s.id,
    stockName: s.stockName,
    stockPrice: s.stockPrice,
    volume: Math.floor(Math.random() * 1000) + 1,
  }));
  result.sort((a, b) => b.volume - a.volume);
  return result.slice(0, 10);
};

// 검색량이 많은 주식 랭킹
const buildRandomSearch = (stocks) => {
  const result = stocks.map((s) => ({
    id: s.id,
    stockName: s.stockName,
    stockPrice: s.stockPrice,
    search: Math.floor(Math.random() * 1000) + 1,
  }));
  result.sort((a, b) => b.search - a.search);
  return result.slice(0, 10);
};

// 추천 주식 랭킹
const buildRecommend = (stocks) => {
  return shuffle(
    stocks.map((s) => ({
      id: s.id,
      stockName: s.stockName,
      stockPrice: s.stockPrice,
    })),
  ).slice(0, 10);
};

// 갱신 버튼 클릭시 작동
const refreshRankings = async () => {
  loading.value = true;
  try {
    const [stockRes, playerStockRes] = await Promise.all([
      apiCall.get("/stocks", null, null),
      apiCall.get("/playerStocks", null, null),
    ]);

    const stocks = Array.isArray(stockRes) ? stockRes : [];
    const playerStocks = Array.isArray(playerStockRes) ? playerStockRes : [];

    // 모든 순위를 다시 새로고침
    rankings.owned = buildOwnedRanking(stocks, playerStocks);
    rankings.volume = buildRandomVolume(stocks);
    rankings.recommend = buildRecommend(stocks);
    rankings.search = buildRandomSearch(stocks);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  refreshRankings();
});
</script>

<template>
  <div class="d-flex align-items-center mb-2 gap-3">
    <div class="fs-4">주식 순위</div>
    <button
      class="btn btn-sm btn-outline-secondary"
      @click="refreshRankings"
      title="갱신"
    >
      <i class="bi bi-arrow-repeat fs-5"></i>
    </button>
  </div>

  <div v-if="loading" class="text-muted small py-2">로딩 중...</div>

  <div v-else class="row g-3">
    <div class="col-12 col-xl-3">
      <div class="border rounded-3 p-2 h-100">
        <div class="fw-semibold mb-2 fs-5">가장 많이 보유한 주식 TOP 10</div>
        <div v-if="rankings.owned.length === 0" class="text-muted small py-2">
          보유 데이터가 없습니다.
        </div>
        <table v-else class="table table-sm table-striped mb-0">
          <thead>
            <tr>
              <th>순위</th>
              <th>주식명</th>
              <th class="text-end">수량</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, idx) in rankings.owned" :key="item.id">
              <td>#{{ idx + 1 }}</td>
              <td>{{ item.stockName }}</td>
              <td class="text-end">{{ item.totalQuantity }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="col-12 col-xl-3">
      <div class="border rounded-3 p-2 h-100">
        <div class="fw-semibold mb-2 fs-5">현재 거래량이 많은 주식 TOP 10</div>
        <div v-if="rankings.volume.length === 0" class="text-muted small py-2">
          주식 데이터가 없습니다.
        </div>
        <table v-else class="table table-sm table-striped mb-0">
          <thead>
            <tr>
              <th>순위</th>
              <th>주식명</th>
              <th class="text-end">거래량</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, idx) in rankings.volume" :key="item.id">
              <td>#{{ idx + 1 }}</td>
              <td>{{ item.stockName }}</td>
              <td class="text-end">{{ item.volume }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="col-12 col-xl-3">
      <div class="border rounded-3 p-2 h-100">
        <div class="fw-semibold mb-2 fs-5">검색량 많은 주식 TOP 10</div>
        <div v-if="rankings.search.length === 0" class="text-muted small py-2">
          주식 데이터가 없습니다.
        </div>
        <table v-else class="table table-sm table-striped mb-0">
          <thead>
            <tr>
              <th>순위</th>
              <th>주식명</th>
              <th class="text-end">검색량</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, idx) in rankings.search" :key="item.id">
              <td>#{{ idx + 1 }}</td>
              <td>{{ item.stockName }}</td>
              <td class="text-end">{{ item.search }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="col-12 col-xl-3">
      <div class="border rounded-3 p-2 h-100">
        <div class="fw-semibold mb-2 fs-5">추천 주식 TOP 10</div>
        <div
          v-if="rankings.recommend.length === 0"
          class="text-muted small py-2"
        >
          주식 데이터가 없습니다.
        </div>
        <table v-else class="table table-sm table-striped mb-0">
          <thead>
            <tr>
              <th>순위</th>
              <th>주식명</th>
              <th class="text-end">현재가</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, idx) in rankings.recommend" :key="item.id">
              <td>#{{ idx + 1 }}</td>
              <td>{{ item.stockName }}</td>
              <td class="text-end">{{ item.stockPrice }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
