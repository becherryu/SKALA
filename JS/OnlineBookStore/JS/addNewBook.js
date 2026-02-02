import { BOOKS } from "../utils/constants.js";
import RenderTable from "../utils/renderTable.js";
import RenderLibraryInfo from "./libraryInfo.js";

const addBtn = document.getElementById("addNewBook");

addBtn.addEventListener("click", () => {
  const rawISBN = document.getElementById("isbn").value.trim();
  console.log(rawISBN.length);

  const book = {
    isbn: formatISBN(rawISBN),
    category: document.getElementById("category").value,
    title: document.getElementById("title").value.trim(),
    author: document.getElementById("author").value.trim(),
    publisher: document.getElementById("publisher").value.trim(),
    price: Number(document.getElementById("price").value),
    stock: Number(document.getElementById("stock").value),
  };

  // 유효성 검사
  if (!validateBook(book)) return;

  BOOKS.push(book);
  RenderTable(BOOKS);
  RenderLibraryInfo();

  // 입력값 초기화
  document.getElementById("isbn").value = "";
  document.getElementById("category").value = "";
  document.getElementById("title").value = "";
  document.getElementById("author").value = "";
  document.getElementById("publisher").value = "";
  document.getElementById("price").value = "";
  document.getElementById("stock").value = "";

  alert("새로운 책이 등록되었습니다!");
});

// isbn 함수
function formatISBN(isbn) {
  const num = isbn.trim();

  if (num.length != 13) return num;

  return `${num.slice(0, 3)}-${num.slice(3, 5)}-${num.slice(5, 9)}-${num.slice(
    9,
    12
  )}-${num.slice(12)}`;
}

// 유효성 검사 함수
function validateBook(book) {
  if (!book.isbn || book.isbn.length !== 17) {
    alert("ISBN은 13자리 숫자로 입력해주세요.");
    return false;
  }

  if (!book.category || !book.title || !book.author || !book.publisher) {
    alert("모든 항목을 입력해주세요.");
    return false;
  }

  if (!Number.isFinite(book.price) || book.price < 0) {
    alert("가격을 올바르게 입력해주세요.");
    return false;
  }

  if (!Number.isFinite(book.stock) || book.stock < 0) {
    alert("수량을 올바르게 입력해주세요.");
    return false;
  }

  // isbn 중복 검사
  const isExist = BOOKS.some((b) => b.isbn === book.isbn);
  if (isExist) {
    alert("이미 존재하는 ISBN입니다.");
    return false;
  }

  return true;
}
