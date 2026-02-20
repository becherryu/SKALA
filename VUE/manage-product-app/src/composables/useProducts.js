import { ref, watch } from "vue";
import {
  createProductAPI,
  editProductAPI,
  deleteProductAPI,
  fetchProductsAPI,
} from "@/api/products";
import { useToast } from "./useToast";

export function useProducts(manager, filters) {
  const products = ref([]);
  const loading = ref(false);
  const totalCount = ref(0);
  const toast = useToast();

  // 상품 목록 불러오기
  async function loadProducts() {
    loading.value = true;

    try {
      const res = await fetchProductsAPI({
        search: filters.search,
        category: filters.category,
        sort: filters.sort,
        page: filters.page,
        limit: filters.limit,
      });

      products.value = res.data;
      totalCount.value = Number(res.headers["x-total-count"] || 0);
    } catch (error) {
      toast.error("상품 목록을 불러오지 못했습니다");
    } finally {
      loading.value = false;
    }
  }

  // 상품 등록하기
  async function addProduct(newProduct) {
    if (!manager.value) {
      toast.warning("관리자 로그인 후 이용해주세요");
      return;
    }

    try {
      await createProductAPI({
        name: newProduct.name,
        category: newProduct.category,
        price: Number(newProduct.price),
        stock: Number(newProduct.stock),
        describe: newProduct.describe,
        manager: manager.value.name,
        createdAt: Date.now(),
      });
      toast.success("상품이 등록되었습니다");

      filters.page = 1;
      loadProducts();
    } catch {
      toast.error("상품 등록 실패");
    }
  }

  // 상품 수정하기
  async function editProduct(edited) {
    try {
      await editProductAPI(edited.id, {
        ...edited,
        manager: manager.value?.name,
      });
      toast.info("상품이 수정되었습니다");
      loadProducts();
    } catch {
      toast.error("상품 수정 실패");
    }
  }

  // 상품 삭제하기
  async function deleteProduct(id) {
    try {
      await deleteProductAPI(id);
      toast.error("상품이 삭제되었습니다");

      // 마지막 페이지에서 다 지워졌으면 이전 페이지로
      if (products.value.length === 1 && filters.page > 1) {
        filters.page--;
      }

      loadProducts();
    } catch {
      toast.error("상품 삭제 실패");
    }
  }

  /* 필터 변경 */
  watch(
    () => [filters.search, filters.category, filters.sort],
    () => {
      filters.page = 1;
      loadProducts();
    },
  );

  /* 페이지 변경 */
  watch(
    () => filters.page,
    () => {
      loadProducts();
    },
  );

  // 최초 로딩
  loadProducts();

  return {
    products,
    loading,
    totalCount,
    loadProducts,
    addProduct,
    editProduct,
    deleteProduct,
  };
}
