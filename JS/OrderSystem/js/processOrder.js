import {
  itemStock,
  membershipDiscount,
  orderAmountDiscount,
  shippingOption,
} from "./order.js";

const button = document.getElementById("orderBtn");

button.addEventListener("click", () => {
  const amounts = Number(document.getElementById("amounts").value);
  const price = Number(document.getElementById("price").value);
  const membership = document.getElementById("membership").value;
  const ship = document.getElementById("ship").value;

  const result = processOrder(amounts, price, membership, ship);
  if (result !== null) {
    alert(`주문이 완료되었습니다!\n 총 결제 금액 ${result.toLocaleString()}원`);
  }
});

export default function processOrder(amounts, price, membership, ship) {
  if (!itemStock(amounts)) {
    alert("재고가 부족합니다!");
    return null;
  }

  let totalPrice = price;

  // 회원등급 할인
  totalPrice = membershipDiscount(membership, totalPrice);

  // 주문 금액 추가 할인
  totalPrice = orderAmountDiscount(totalPrice);

  // 배송비
  totalPrice += shippingOption(ship);

  return totalPrice;
}
