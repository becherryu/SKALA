import { BOOKS } from "../utils/constants.js";
import RenderTable from "../utils/renderTable.js";
import RenderLibraryInfo from "./libraryInfo.js";

const tbody = document.getElementById("bookBody");

tbody.addEventListener("click", (e) => {
  const btn = e.target.closest(".addBtn");
  if (!btn) return;

  const isbn = btn.dataset.isbn;
  const input = document.getElementById(`addCnt-${isbn}`);
  const addCnt = Number(input.value);

  if (!Number.isFinite(addCnt) || addCnt <= 0) {
    alert("추가할 수량을 1 이상으로 입력해주세요.");
    return;
  }

  const targetBook = BOOKS.find((book) => String(book.isbn) === String(isbn));
  if (!targetBook) return;

  targetBook.stock += addCnt;

  RenderTable(BOOKS);
  RenderLibraryInfo();

  alert(`${targetBook.title}에 총 ${addCnt} 권 추가되었습니다!`);
});
