import { MEMBERSHIP_DC, PRICE_DC, SHIPPING_FEE } from "../utils/constants.js";

/* 상품 재고 확인 - 재고가 1개 이상일 때 주문 가능*/
export function itemStock(item) {
  return item >= 1;
}

/* 회원 등급에 따른 할인
 * VIP - 10%
 * Gold - 5%
 * other - 0
 */
export function membershipDiscount(membership, price) {
  switch (membership) {
    case "VIP":
      return price * MEMBERSHIP_DC.VIP;
    case "Gold":
      return price * MEMBERSHIP_DC.Gold;
    default:
      return price;
  }
}

/**
 * 주문 금액에 따른 추가 할인
 * 기본 할인(멤버십 할인) 후
 * 주문 금액이 100,000원 이상 - 5000원 추가 할인
 * 주문 금액이 200,000원 이상 - 15,000원 추가 할인
 */
export function orderAmountDiscount(price) {
  if (price >= 200000) {
    return price - PRICE_DC.over20;
  }
  if (price >= 100000) {
    return price - PRICE_DC.over10;
  }

  return price;
}

/**
 * 배송 옵션
 * fast - 배송비 3000원 추가
 * standard | none - 배송비 0원
 */
export function shippingOption(option) {
  return option === "fast" ? SHIPPING_FEE.fast : 0;
}
