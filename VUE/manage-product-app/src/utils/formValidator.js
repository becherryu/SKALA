/**
 * 상품 등록 폼 유효성 검사 함수
 * @param {Object} ProductForm
 * @returns {Boolean}
 */
export function validateProductForm(product) {
  // 카테고리
  if (!product.category) {
    alert("카테고리를 선택해주세요");
    return false;
  }

  // 가격
  if (
    product.price === "" ||
    isNaN(product.price) ||
    Number(product.price) < 0
  ) {
    alert("가격은 0 이상의 양수를 입력해주세요.");
    return false;
  }

  // 재고
  if (
    product.stock === "" ||
    isNaN(product.stock) ||
    Number(product.stock) < 0
  ) {
    alert("재고는 0 이상의 양수를 입력해주세요.");
    return false;
  }

  // 설명
  if (product.describe && product.describe.length > 200) {
    alert("설명은 200자 이내로 작성해주세요.");
    return false;
  }

  return true;
}
