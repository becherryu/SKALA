import { BOOKS } from "../utils/constants.js";
import RenderTable from "../utils/renderTable.js";

const stockSelect = document.getElementById("checkStock");
const categorySelect = document.getElementById("checkCategory");

RenderTable(BOOKS);

function filteredBooks() {
  const stockValue = stockSelect.value;
  const categoryValue = categorySelect.value;

  let stockFiltered = [];
  let finalFiltered = [];

  // 재고 필터링
  for (const book of BOOKS) {
    if (stockValue === "available") {
      if (book.stock > 0) stockFiltered.push(book);
    } else if (stockValue === "soldOut") {
      if (book.stock === 0) stockFiltered.push(book);
    } else {
      stockFiltered.push(book);
    }
  }

  // 카테고리 필터링
  stockFiltered.forEach((book) => {
    if (categoryValue === "programming") {
      if (book.category === "프로그래밍") finalFiltered.push(book);
    } else if (categoryValue === "economics") {
      if (book.category === "경제") finalFiltered.push(book);
    } else if (categoryValue === "novel") {
      if (book.category === "소설") finalFiltered.push(book);
    } else if (categoryValue === "essay") {
      if (book.category === "에세이") finalFiltered.push(book);
    } else {
      finalFiltered.push(book); // 전체
    }
  });

  RenderTable(finalFiltered);
}

stockSelect.addEventListener("change", filteredBooks);
categorySelect.addEventListener("change", filteredBooks);
