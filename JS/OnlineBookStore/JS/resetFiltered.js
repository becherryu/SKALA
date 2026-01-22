import { BOOKS } from "../utils/constants.js";
import RenderTable from "../utils/renderTable.js";
const resetBtn = document.getElementById("reset");

resetBtn.addEventListener("click", () => {
  const stock = document.getElementById("checkStock");
  const category = document.getElementById("checkCategory");

  stock.value = "all";
  category.value = "";
  RenderTable(BOOKS);
});
