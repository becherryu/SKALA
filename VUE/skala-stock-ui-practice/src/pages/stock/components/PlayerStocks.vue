<script setup>
import { ref, reactive, onMounted, watch, computed } from "vue";
import { usePlayer } from "@/scripts/store-player";
import apiCall from "@/scripts/api-call";

const stockId = ref("");
const stockQuantity = ref("");
const playerMoney = ref(0);
const playerAssetTotal = ref(0);
const selectedStockPrice = ref(0);
const selectedOwnedQuantity = ref(0);
const ownedSortKey = ref("id_asc");

const table = reactive({
  headers: [
    { label: "주식ID", value: "stockId" },
    { label: "주식명", value: "stockName" },
    { label: "주식가격", value: "stockPrice" },
    { label: "보유수량", value: "quantity" },
  ],
  items: [],
});

const player = usePlayer();

// 보유 주식 목록 페이지네이션
const ownedPage = reactive({
  total: 0,
  current: 1,
  count: 5,
  groupSize: 5,
});

// 전체 보유 주식 목록 페이지 계산
const getOwnedTotalPage = (totalCount) => {
  ownedPage.total = Math.ceil(totalCount / ownedPage.count);
};

// 보유 주식 목록 가져오기
const getOwnedStockList = async () => {
  table.items.length = 0;

  // 사용자의 id를 기준으로 동일한 항목 가져오기
  const psRes = await apiCall.get("/playerStocks", null, {
    playerId: player.playerId,
  });

  if (!Array.isArray(psRes)) return;

  const allItems = [];

  // 가져온 내용을 바탕으로 stocks에서 주식 가격 가져오기
  for (const ps of psRes) {
    const stockRes = await apiCall.get("/stocks", null, {
      id: ps.stockId,
    });
    if (!Array.isArray(stockRes) || !stockRes[0]) continue;

    const stock = stockRes[0];

    allItems.push({
      stockId: ps.stockId,
      stockName: stock.stockName,
      stockPrice: stock.stockPrice,
      quantity: ps.quantity,
    });
  }

  // 주식 자산 계산 (주식 목록 내의 주식가격 * 보유 수량의 합계)
  playerAssetTotal.value = allItems.reduce(
    (sum, item) => sum + item.stockPrice * item.quantity,
    0,
  );

  // 가격, 수량, id를 기준으로 정렬
  const sorted = [...allItems];
  switch (ownedSortKey.value) {
    case "price_asc":
      sorted.sort((a, b) => a.stockPrice - b.stockPrice);
      break;
    case "price_desc":
      sorted.sort((a, b) => b.stockPrice - a.stockPrice);
      break;
    case "qty_desc":
      sorted.sort((a, b) => b.quantity - a.quantity);
      break;
    case "id_asc":
    default:
      sorted.sort((a, b) => a.stockId - b.stockId);
      break;
  }

  // 보유 주식 목록 페이지네이션
  getOwnedTotalPage(sorted.length);

  const start = (ownedPage.current - 1) * ownedPage.count;
  const end = start + ownedPage.count;
  table.items.push(...sorted.slice(start, end));
};

// 회원 정보에서 예수금 불러오기
const getPlayerInfo = async () => {
  const playerRes = await apiCall.get("/players", null, {
    playerId: player.playerId,
  });
  if (!Array.isArray(playerRes) || !playerRes[0]) return;

  playerMoney.value = playerRes[0].playerMoney;

  getOwnedStockList();
};

// 주식 구매
const buyPlayerStock = async () => {
  const quantity = Number(stockQuantity.value);
  if (!stockId.value || quantity <= 0) return;

  // 사용자가 입력한 주식이 있는지 id 조회
  const stockRes = await apiCall.get("/stocks", null, {
    id: Number(stockId.value),
  });
  if (!Array.isArray(stockRes) || !stockRes[0]) return;

  // 존재하면 id 저장
  const stock = stockRes[0];
  const totalPrice = stock.stockPrice * quantity;

  // 사용자의 예수금 가져오기
  const playerRes = await apiCall.get("/players", null, {
    playerId: player.playerId,
  });
  if (!Array.isArray(playerRes) || !playerRes[0]) return;

  // 총 주식 구매 가격이 예수금보다 크면 비활성화
  const currentPlayer = playerRes[0];
  if (currentPlayer.playerMoney < totalPrice) return;

  // 사용자의 주식 목록 가져오기
  const psRes = await apiCall.get("/playerStocks", null, {
    playerId: player.playerId,
    stockId: stock.id,
  });

  // 사려는 주식이 목록에 있으면 기존 목록에 개수 추가
  if (Array.isArray(psRes) && psRes[0]) {
    await apiCall.put(`/playerStocks/${psRes[0].id}`, null, {
      ...psRes[0],
      quantity: psRes[0].quantity + quantity,
    });
  } else {
    // 사려는 주식이 목록에 없으면 새로 추가
    await apiCall.post("/playerStocks", null, {
      playerId: player.playerId,
      stockId: stock.id,
      quantity,
    });
  }

  // 사용자의 예수금 업데이트
  await apiCall.put(`/players/${currentPlayer.id}`, null, {
    ...currentPlayer,
    playerMoney: currentPlayer.playerMoney - totalPrice,
  });

  stockId.value = "";
  stockQuantity.value = "";
  getPlayerInfo();
};

// 주식 판매
const sellPlayerStock = async () => {
  const quantity = Number(stockQuantity.value);
  if (!stockId.value || quantity <= 0) return;

  // 사용자 보유 주식 목록에서 가져오기
  const psRes = await apiCall.get("/playerStocks", null, {
    playerId: player.playerId,
    stockId: Number(stockId.value),
  });
  if (!Array.isArray(psRes) || !psRes[0]) return;

  const ps = psRes[0];

  // 해당 주식의 개수가 입력한 개수보다 많으면 return
  if (ps.quantity < quantity) return;

  // stock에서 주식 정보 가져오기
  const stockRes = await apiCall.get("/stocks", null, {
    id: ps.stockId,
  });
  if (!Array.isArray(stockRes) || !stockRes[0]) return;

  const stock = stockRes[0];
  const sellAmount = stock.stockPrice * quantity;

  // 입력한 주식 수량과 보유한 수량이 같으면 삭제
  if (ps.quantity === quantity) {
    await apiCall.delete(`/playerStocks/${ps.id}`, null, null);
  } else {
    // 입력한 주식 수량이 보유한 수량보다 적으면 업데이트
    await apiCall.put(`/playerStocks/${ps.id}`, null, {
      ...ps,
      quantity: ps.quantity - quantity,
    });
  }

  // 예수금을 가져오기 위해 player 정보 가져오기
  const playerRes = await apiCall.get("/players", null, {
    playerId: player.playerId,
  });
  const currentPlayer = playerRes[0];

  // 예수금 업데이트
  await apiCall.put(`/players/${currentPlayer.id}`, null, {
    ...currentPlayer,
    playerMoney: currentPlayer.playerMoney + sellAmount,
  });

  stockId.value = "";
  stockQuantity.value = "";
  getPlayerInfo();
};

// 사용자가 입력한 주식 보기
const refreshSelectedInfo = async () => {
  if (!stockId.value) {
    selectedStockPrice.value = 0;
    selectedOwnedQuantity.value = 0;
    return;
  }

  const [stockRes, psRes] = await Promise.all([
    apiCall.get("/stocks", null, { id: Number(stockId.value) }),
    apiCall.get("/playerStocks", null, {
      playerId: player.playerId,
      stockId: Number(stockId.value),
    }),
  ]);

  if (Array.isArray(stockRes) && stockRes[0]) {
    selectedStockPrice.value = stockRes[0].stockPrice ?? 0;
  } else {
    selectedStockPrice.value = 0;
  }

  if (Array.isArray(psRes) && psRes[0]) {
    selectedOwnedQuantity.value = psRes[0].quantity ?? 0;
  } else {
    selectedOwnedQuantity.value = 0;
  }
};

// 페이지네이션 계산
const ownedPageGroup = computed(() => {
  if (ownedPage.total === 0) return [];
  const start =
    Math.floor((ownedPage.current - 1) / ownedPage.groupSize) *
      ownedPage.groupSize +
    1;
  const end = Math.min(start + ownedPage.groupSize - 1, ownedPage.total);
  return Array.from({ length: end - start + 1 }, (_, i) => start + i);
});

const goToOwnedPage = (p) => {
  if (ownedPage.total === 0) return;
  ownedPage.current = p;
};

const prevOwnedGroup = () => {
  if (ownedPageGroup.value.length === 0) return;
  const prev = ownedPageGroup.value[0] - 1;
  if (prev >= 1) ownedPage.current = prev;
};

const nextOwnedGroup = () => {
  if (ownedPageGroup.value.length === 0) return;
  const next = ownedPageGroup.value[ownedPageGroup.value.length - 1] + 1;
  if (next <= ownedPage.total) ownedPage.current = next;
};

// 구매 가능  최대 수량 계산
const maxBuyQuantity = computed(() => {
  if (!selectedStockPrice.value) return 0;
  return Math.floor(playerMoney.value / selectedStockPrice.value);
});

// 판매 가능 최대 수량 계산
const maxSellQuantity = computed(() => selectedOwnedQuantity.value || 0);

// 최대 수량 구매 설정
const setMaxBuy = () => {
  if (!stockId.value) return;
  stockQuantity.value = String(maxBuyQuantity.value || "");
};

// 최대 수량 판매 설정
const setMaxSell = () => {
  if (!stockId.value) return;
  stockQuantity.value = String(maxSellQuantity.value || "");
};

// 현재 페이지와 정렬 기준으로 페이지 감시
watch(
  () => [ownedPage.current, ownedSortKey.value],
  () => {
    getOwnedStockList();
  },
);

// 사용자가 입력한 주식 감시
watch(
  () => [stockId.value, player.playerId],
  () => {
    refreshSelectedInfo();
  },
);

onMounted(() => {
  getPlayerInfo();
});
</script>

<template>
  <div class="row mt-2">
    <span class="fs-4">
      <i class="bi bi-person m-2"></i>{{ player.playerId }} 플레이어
    </span>
  </div>

  <div class="row border-bottom">
    <div class="col d-flex justify-content-end">
      <button
        class="btn btn-sm btn-outline-secondary m-2"
        @click="getPlayerInfo"
      >
        갱신
      </button>
    </div>
  </div>

  <div class="col justify-content-center">
    <div class="row">
      <div class="col">
        <InlineInput
          class="m-2"
          label="플레이어ID"
          v-model="player.playerId"
          :disabled="true"
        />
        <InlineInput
          class="m-2"
          label="예수금"
          v-model="playerMoney"
          :disabled="true"
        />
        <InlineInput
          class="m-2"
          label="나의 주식 자산"
          v-model="playerAssetTotal"
          :disabled="true"
        />
      </div>
    </div>

    <div class="row g-2 align-items-center m-2 mt-0">
      <div class="col-2 d-flex justify-content-end">
        <label class="col-form-label form-control-sm p-1">주식선택</label>
      </div>
      <div class="col">
        <InlineInput v-model="stockId" placeholder="주식ID" />
      </div>
      <div class="col">
        <InlineInput v-model="stockQuantity" placeholder="주식수량" />
      </div>
      <div class="col d-flex flex-column gap-2">
        <!-- 구매 라인 -->
        <div class="d-flex align-items-center gap-2">
          <button
            class="btn btn-sm btn-outline-primary"
            @click="buyPlayerStock"
            :disabled="!stockId || maxBuyQuantity === 0"
          >
            구매
          </button>

          <button
            class="btn btn-sm btn-outline-primary"
            @click="setMaxBuy"
            :disabled="!stockId || maxBuyQuantity === 0"
          >
            최대구매
          </button>
        </div>

        <!-- 판매 라인 -->
        <div class="d-flex align-items-center gap-2">
          <button
            class="btn btn-sm btn-outline-danger"
            @click="sellPlayerStock"
            :disabled="!stockId || maxSellQuantity === 0"
          >
            판매
          </button>

          <button
            class="btn btn-sm btn-outline-danger"
            @click="setMaxSell"
            :disabled="!stockId || maxSellQuantity === 0"
          >
            최대판매
          </button>
        </div>

        <!-- 설명 -->
        <div class="small text-muted">
          최대구매 {{ maxBuyQuantity }}주 · 최대판매 {{ maxSellQuantity }}주
        </div>
      </div>
    </div>

    <div class="row g-2 align-items-center m-2 mt-0">
      <div class="col">
        <div class="d-flex justify-content-start mb-2">
          <div class="col-2 d-flex">
            <label class="col-form-label form-control-sm p-1"
              >보유주식목록</label
            >
          </div>
          <select
            class="form-select form-select-sm w-auto"
            v-model="ownedSortKey"
          >
            <option value="id_asc">주식ID</option>
            <option value="price_asc">주식 가격 낮은순</option>
            <option value="price_desc">주식 가격 높은순</option>
            <option value="qty_desc">보유 수량 많은 순</option>
          </select>
        </div>
        <div v-if="table.items.length === 0" class="text-muted small py-3">
          보유 주식이 없습니다.
        </div>

        <ItemsTable
          v-else
          :headers="table.headers"
          :items="table.items"
          :nosetting="true"
        />

        <div
          v-if="table.items.length > 0"
          class="d-flex justify-content-center align-items-center gap-1 mt-2"
        >
          <button
            class="btn btn-sm btn-outline-secondary"
            @click="prevOwnedGroup"
            :disabled="ownedPageGroup.length === 0 || ownedPageGroup[0] === 1"
          >
            &laquo;
          </button>

          <button
            v-for="p in ownedPageGroup"
            :key="p"
            class="btn btn-sm"
            :class="
              p === ownedPage.current ? 'btn-primary' : 'btn-outline-secondary'
            "
            @click="goToOwnedPage(p)"
          >
            {{ p }}
          </button>

          <button
            class="btn btn-sm btn-outline-secondary"
            @click="nextOwnedGroup"
            :disabled="
              ownedPageGroup.length === 0 ||
              ownedPageGroup[ownedPageGroup.length - 1] === ownedPage.total
            "
          >
            &raquo;
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
