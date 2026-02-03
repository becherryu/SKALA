<script setup>
defineProps({
  totalCount: Number,
  totalPrice: Number,
});
</script>

<template>
  <div>
    <h2>📊 상품 현황</h2>
    <h3>상품 항목 개수 : {{ totalCount }}</h3>
    <h3>상품 총 가격 : {{ totalPrice.toLocaleString() }} 원</h3>
  </div>
</template>
