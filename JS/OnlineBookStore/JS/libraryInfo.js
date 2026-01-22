// for...of를 사용해서 도서 전체 가격의 평균 구하기
/** 현재 도서관 현황 띄우기
 * 전체 도서 권수
 * 가장 많은 책의 카테고리
 * 평균 도서 가격
 * 재고 0 개인 도서 권수
 */

import { BOOKS } from "../utils/constants.js";

RenderLibraryInfo();

export default function RenderLibraryInfo() {
  const libraryInfo = document.getElementById("libraryInfo");

  let totalPrice = 0;
  let zeroStock = 0;
  let categoryCount = {};

  for (const book of BOOKS) {
    totalPrice += book.price;

    if (book.stock === 0) zeroStock++;

    categoryCount[book.category] = (categoryCount[book.category] || 0) + 1;
  }

  let maxCategory = "";
  let maxCount = 0;

  for (const key in categoryCount) {
    if (categoryCount[key] > maxCount) {
      maxCount = categoryCount[key];
      maxCategory = key;
    }
  }

  libraryInfo.innerHTML = `<div class='infoTable'>
  <h3>SKALA 도서관 현황</h3>
  <p>전체 도서 권수 : <span>${BOOKS.length}</span> 권</p>
  <p>가장 많은 책의 카테고리 : <span>${maxCategory}</span> </p>
  <p>평균 도서 가격 : <span>${Math.floor(
    totalPrice / BOOKS.length
  )}</span> 원</p>
  <p>품절인 도서 권수: <span>${zeroStock}</span> 권</p>
</div>`;
}
