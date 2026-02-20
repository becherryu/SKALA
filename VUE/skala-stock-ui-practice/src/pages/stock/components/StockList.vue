<script setup>
import { ref, reactive, watch, onMounted, computed } from "vue";
import apiCall from "@/scripts/api-call";

const stockName = ref("");
const stockPrice = ref("");
const searchKeyword = ref("");
const sortKey = ref("id_asc");

const table = reactive({
  headers: [
    { label: "주식ID", value: "id" },
    { label: "주식명", value: "stockName" },
    { label: "주식가격", value: "stockPrice" },
  ],
  items: [],
});

// 10개씩 5개의 그룹 사이즈로 보여줌
const page = reactive({
  total: 0,
  current: 1,
  count: 10,
  groupSize: 5,
});

// 전체 페이지 수 가져와서 페이지수 계산
const getTotalPage = (totalCount) => {
  page.total = Math.ceil(totalCount / page.count);
};

// 주식 목록을 가져와서 표 형식으로 보여주기
const getStockList = async () => {
  table.items.length = 0;

  const params = {};

  if (searchKeyword.value.trim()) {
    params.q = searchKeyword.value.trim();
  }

  const response = await apiCall.get("/stocks", null, params);
  if (!Array.isArray(response)) return;

  // 가격, 이름, id 를 기준으로 정렬
  const sorted = [...response];
  switch (sortKey.value) {
    case "price_asc":
      sorted.sort((a, b) => a.stockPrice - b.stockPrice);
      break;
    case "price_desc":
      sorted.sort((a, b) => b.stockPrice - a.stockPrice);
      break;
    case "name_asc":
      sorted.sort((a, b) =>
        String(a.stockName).localeCompare(String(b.stockName)),
      );
      break;
    case "id_asc":
    default:
      sorted.sort((a, b) => a.id - b.id);
      break;
  }

  // 페이지 수를 계산해서 보여주기
  getTotalPage(sorted.length);

  const start = (page.current - 1) * page.count;
  const end = start + page.count;
  table.items.push(...sorted.slice(start, end));
};

// 주식 정보 추가
const addStock = async () => {
  if (!stockName.value || !stockPrice.value) return;

  const name = String(stockName.value).trim();
  const price = Number(stockPrice.value);

  // 기존에 있는 주식 이름과 동일한지 확인
  const existsRes = await apiCall.get("/stocks", null, {
    stockName: name,
  });

  // 이미 존재하는 경우에는 해당 주식의 가격을 갱신하기
  if (Array.isArray(existsRes) && existsRes[0]) {
    const existing = existsRes[0];
    await apiCall.put(`/stocks/${existing.id}`, null, {
      ...existing,
      stockPrice: price,
    });
  } else {
    await apiCall.post("/stocks", null, {
      stockName: name,
      stockPrice: price,
    });
  }

  stockName.value = "";
  stockPrice.value = "";

  getStockList();
};

const pageGroup = computed(() => {
  const start =
    Math.floor((page.current - 1) / page.groupSize) * page.groupSize + 1;
  const end = Math.min(start + page.groupSize - 1, page.total);

  return Array.from({ length: end - start + 1 }, (_, i) => start + i);
});

// 주식 목록 갱신하기
const refreshStockList = async () => {
  searchKeyword.value = "";
  page.current = 1;
  getStockList();
};

// 주식 목록 페이지 버튼 조작용
const goToPage = (p) => {
  page.current = p;
};

const prevGroup = () => {
  const prev = pageGroup.value[0] - 1;
  if (prev >= 1) page.current = prev;
};

const nextGroup = () => {
  const next = pageGroup.value[pageGroup.value.length - 1] + 1;
  if (next <= page.total) page.current = next;
};

const searchStock = async () => {
  page.current = 1;
  getStockList();
};

// 현재 페이지와 정렬 기준으로 주식 목록 가져오기
watch(
  () => [page.current, sortKey.value],
  () => {
    getStockList();
  },
);

watch(
  () => page.count,
  () => {
    page.current = 1;
    getStockList();
  },
);

onMounted(async () => {
  getStockList();
});
</script>

<template>
  <div class="row mt-2">
    <span class="fs-4"> <i class="bi bi-graph-up m-2"></i>주식목록 </span>
  </div>

  <div class="row g-2 m-2 align-items-center p-2">
    <div class="col">
      <input
        class="form-control form-control-sm"
        v-model="searchKeyword"
        placeholder="주식명 검색"
        @keyup.enter="searchStock"
      />
    </div>

    <div class="col-auto">
      <button class="btn btn-sm btn-secondary" @click="searchStock">
        검색
      </button>
    </div>

    <div class="col-auto ms-auto">
      <button
        class="btn btn-sm btn-outline-secondary"
        @click="refreshStockList"
      >
        갱신
      </button>
    </div>
  </div>

  <div class="row g-2 m-2 border-top p-2">
    <div class="col-2 d-flex justify-content-end">
      <label class="col-form-label form-control-sm p-1">주식정보</label>
    </div>
    <div class="col">
      <input
        class="form-control form-control-sm"
        v-model="stockName"
        placeholder="주식명"
      />
    </div>
    <div class="col">
      <input
        class="form-control form-control-sm"
        v-model="stockPrice"
        placeholder="주식가격"
      />
    </div>
    <div class="col d-flex justify-content-start">
      <button class="btn btn-sm btn-outline-primary me-2" @click="addStock">
        주식 추가
      </button>
    </div>
  </div>

  <div class="col-auto">
    <div class="d-flex justify-content-start mb-2">
      <select class="form-select form-select-sm w-auto" v-model="sortKey">
        <option value="id_asc">주식ID</option>
        <option value="price_asc">주식 가격 낮은순</option>
        <option value="price_desc">주식 가격 높은순</option>
        <option value="name_asc">주식명</option>
      </select>
    </div>
  </div>

  <table class="table table-sm mt-2">
    <thead>
      <tr>
        <th v-for="h in table.headers" :key="h.value">
          {{ h.label }}
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in table.items" :key="item.id">
        <td>{{ item.id }}</td>
        <td>{{ item.stockName }}</td>
        <td>{{ item.stockPrice }}</td>
      </tr>
    </tbody>
  </table>

  <div v-if="table.items.length === 0" class="text-muted small py-3">
    주식 목록이 없습니다.
  </div>

  <div
    v-if="table.items.length > 0"
    class="d-flex justify-content-center align-items-center gap-1 mt-2"
  >
    <button
      class="btn btn-sm btn-outline-secondary"
      @click="prevGroup"
      :disabled="pageGroup[0] === 1"
    >
      &laquo;
    </button>

    <button
      v-for="p in pageGroup"
      :key="p"
      class="btn btn-sm"
      :class="p === page.current ? 'btn-primary' : 'btn-outline-secondary'"
      @click="goToPage(p)"
    >
      {{ p }}
    </button>

    <button
      class="btn btn-sm btn-outline-secondary"
      @click="nextGroup"
      :disabled="pageGroup[pageGroup.length - 1] === page.total"
    >
      &raquo;
    </button>
  </div>
</template>
