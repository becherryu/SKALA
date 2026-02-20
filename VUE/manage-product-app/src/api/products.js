import { http } from "./http";

// 상품 등록
export function createProductAPI(data) {
  return http.post("/products", data);
}

// 상품 수정
export function editProductAPI(id, data) {
  return http.patch(`/products/${id}`, data);
}

// 상품 삭제
export function deleteProductAPI(id) {
  return http.delete(`/products/${id}`);
}

// 상품 목록 조회 (필터/검색/정렬)
export function fetchProductsAPI({ search, category, sort, page, limit }) {
  const params = {};

  // 검색
  if (search?.trim()) {
    params.q = search;
  }

  // 카테고리
  if (category && category !== "all") {
    params.category = category;
  }

  // 정렬
  switch (sort) {
    case "name":
      params._sort = "name";
      params._order = "asc";
      break;

    case "price":
      params._sort = "price";
      params._order = "desc";
      break;

    case "stock":
      params._sort = "stock";
      params._order = "desc";
      break;

    case "oldest":
      params._sort = "createdAt";
      params._order = "asc";
      break;

    case "latest":
    default:
      params._sort = "createdAt";
      params._order = "desc";
      break;
  }

  if (page != null && limit != null) {
    params._page = page;
    params._limit = limit;
  }

  return http.get("/products", { params });
}
