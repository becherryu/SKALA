import { ref, computed, onMounted } from "vue";
import { fetchProductsAPI } from "@/api/products";
import { useToast } from "./useToast";

export function useProductStats() {
  const products = ref([]);
  const loading = ref(false);
  const toast = useToast();

  async function loadAllProducts() {
    loading.value = true;
    try {
      const res = await fetchProductsAPI({
        sort: "latest",
      });
      products.value = res.data;
    } catch (e) {
      toast.error("상품 현황을 불러오지 못했습니다");
    } finally {
      loading.value = false;
    }
  }

  onMounted(loadAllProducts);

  // 전체 개수
  const totalCount = computed(() => products.value.length);

  // 전체 재고 금액
  const totalPrice = computed(() =>
    products.value.reduce((sum, p) => sum + p.price * p.stock, 0),
  );

  // 카테고리별 상품 수
  const categoryStats = computed(() => {
    const stats = {};
    for (const p of products.value) {
      stats[p.category] = (stats[p.category] || 0) + 1;
    }
    return stats;
  });

  // 재고 부족 상품 (기본 5개 이하)
  const lowStockProducts = computed(() =>
    [...products.value]
      .filter((p) => p.stock <= 5)
      .sort((a, b) => a.stock - b.stock),
  );

  return {
    products,
    loading,
    totalCount,
    totalPrice,
    categoryStats,
    lowStockProducts,
  };
}
