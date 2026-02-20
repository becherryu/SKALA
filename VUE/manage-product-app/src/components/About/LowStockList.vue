<script setup>
const props = defineProps({
  products: {
    type: Array,
    required: true,
  },
  threshold: {
    type: Number,
    default: 5, // 재고 부족 기준
  },
});

function getStatus(stock) {
  if (stock === 0) return "품절";
  if (stock <= props.threshold) return "부족";
  return "정상";
}
</script>

<template>
  <section class="low-stock">
    <h2>⚠️ 재고 부족 상품 현황</h2>

    <div v-if="products.length === 0" class="empty">
      재고 부족 상품이 없습니다 👍
    </div>

    <!-- 스크롤 영역 -->
    <div v-else class="table-wrapper">
      <table class="stock-table">
        <thead>
          <tr>
            <th>상품명</th>
            <th>카테고리</th>
            <th>재고</th>
            <th>가격</th>
            <th>재고 금액</th>
            <th>관리자</th>
            <th>상태</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="p in products"
            :key="p.id"
            :class="{ danger: p.stock === 0 }"
          >
            <td>{{ p.name }}</td>
            <td>{{ p.category }}</td>
            <td>{{ p.stock }}개</td>
            <td>{{ p.price.toLocaleString() }}원</td>
            <td>{{ (p.price * p.stock).toLocaleString() }}원</td>
            <td>{{ p.manager || "-" }}</td>
            <td>
              <span
                class="status"
                :class="{
                  out: p.stock === 0,
                  low: p.stock > 0 && p.stock <= threshold,
                }"
              >
                {{ getStatus(p.stock) }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<style scoped>
/* 카드 전체 */
.low-stock {
  padding: 20px;
  border-radius: 16px;
  background: #fff5f5;
  border: 1px solid #ffd6d6;
  max-height: 400px; /* 카드 전체 높이 */
  display: flex;
  flex-direction: column;
  width: 800px;
}

/* 제목 */
.low-stock h2 {
  margin-bottom: 12px;
  color: #d32f2f;
  font-size: 18px;
}

/* empty */
.empty {
  color: #888;
  font-size: 14px;
  text-align: center;
  margin-top: 40px;
}

/* ✅ 스크롤 영역 */
.table-wrapper {
  flex: 1;
  overflow-y: auto;
  border-radius: 12px;
}

/* 스크롤바 */
.table-wrapper::-webkit-scrollbar {
  width: 6px;
}

.table-wrapper::-webkit-scrollbar-thumb {
  background: #f2bcbc;
  border-radius: 4px;
}

/* table */
.stock-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

/* 헤더 고정 */
.stock-table thead th {
  position: sticky;
  top: 0;
  background: #ffecec;
  z-index: 1;
}

/* 셀 */
.stock-table th,
.stock-table td {
  padding: 10px 8px;
  border-bottom: 1px solid #f2caca;
  text-align: center;
}

.stock-table th {
  font-weight: 600;
}

.stock-table td.name {
  text-align: left;
  font-weight: 500;
}

.stock-table td.stock {
  font-weight: bold;
  color: #d32f2f;
}

/* 상태 뱃지 */
.status {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
}

.status.low {
  background: #fff3cd;
  color: #856404;
}

.status.out {
  background: #f8d7da;
  color: #721c24;
}

/* 품절 행 강조 */
tr.danger {
  background: #fff0f0;
}
</style>
