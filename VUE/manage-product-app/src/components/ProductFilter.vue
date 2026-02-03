<script setup>
defineProps({
  search: String,
  category: String,
  sort: String,
});

const emit = defineEmits(["update:search", "update:category", "update:sort"]);
</script>

<template>
  <div class="inPfilter">
    <!-- 검색 -->
    <section>
      <input
        class="searchInput"
        type="text"
        :value="search"
        placeholder="상품명 또는 설명으로 검색하세요."
        @input="$emit('update:search', $event.target.value)"
      />
    </section>

    <!-- 카테고리 -->
    <section class="categoryFilter">
      <button
        :class="{ active: category === 'all' }"
        @click="$emit('update:category', 'all')"
      >
        전체
      </button>
      <button
        :class="{ active: category === '전자기기' }"
        @click="$emit('update:category', '전자기기')"
      >
        전자기기
      </button>
      <button
        :class="{ active: category === '의류' }"
        @click="$emit('update:category', '의류')"
      >
        의류
      </button>
      <button
        :class="{ active: category === '식품' }"
        @click="$emit('update:category', '식품')"
      >
        식품
      </button>
      <button
        :class="{ active: category === '도서' }"
        @click="$emit('update:category', '도서')"
      >
        도서
      </button>
      <button
        :class="{ active: category === '기타' }"
        @click="$emit('update:category', '기타')"
      >
        기타
      </button>
    </section>

    <hr />

    <!-- 정렬 -->
    <section class="sortFilter">
      <button
        :class="{ active: sort === 'latest' }"
        @click="$emit('update:sort', 'latest')"
      >
        최신순
      </button>
      <button
        :class="{ active: sort === 'name' }"
        @click="$emit('update:sort', 'name')"
      >
        이름순
      </button>
      <button
        :class="{ active: sort === 'price' }"
        @click="$emit('update:sort', 'price')"
      >
        가격순
      </button>
      <button
        :class="{ active: sort === 'stock' }"
        @click="$emit('update:sort', 'stock')"
      >
        재고순
      </button>
      <button
        :class="{ active: sort === 'oldest' }"
        @click="$emit('update:sort', 'oldest')"
      >
        오래된순
      </button>
    </section>
  </div>
</template>

<style>
.inPfilter {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.searchInput {
  width: 30.5vw;
  padding: 8px 12px;
  border-radius: 6px;
  border: 1px solid #d9d9d9;
}

.searchInput:focus {
  outline: none;
  border-color: #555;
}

/* 공통 버튼 스타일 */
.categoryFilter button,
.sortFilter button {
  padding: 6px 14px;
  border-radius: 20px;
  border: 1px solid #d9d9d9;
  background-color: #f5f5f5;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
}

/* 선택된 버튼 */
.categoryFilter button.active,
.sortFilter button.active {
  background-color: #555; /* 진한 회색 */
  color: #fff; /* 흰색 글자 */
  border-color: #555;
}

/* hover */
.categoryFilter button:hover,
.sortFilter button:hover {
  background-color: #e0e0e0;
}

.categoryFilter {
  display: flex;
  gap: 10px;
}

.sortFilter {
  display: flex;
  gap: 16px;
}

hr {
  border: 0.5px solid #d9d9d9;
  width: 35vw;
  margin: 0;
}
</style>
