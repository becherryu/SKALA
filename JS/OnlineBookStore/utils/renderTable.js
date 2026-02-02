export default function RenderTable(array) {
  const tbody = document.getElementById("bookBody");
  tbody.innerHTML = "";

  if (array.length < 1) {
    const tr = document.createElement("tr");
    tr.innerHTML = `<td colspan='9' style='text-align: center;'> 해당하는 조건의 책이 존재하지 않습니다. </td>`;

    tbody.appendChild(tr);
    return;
  }

  for (let i = 0; i < array.length; i++) {
    const book = array[i];
    const isbn = array[i].isbn;

    const tr = document.createElement("tr");

    tr.innerHTML = `
  <td>${i + 1}</td>
  <td id=isbn-${isbn}>${book.isbn}</td>
  <td id=category-${isbn}>${book.category}</td>
  <td id=title-${isbn}>${book.title}</td>
  <td id=author-${isbn}>${book.author}</td>
  <td id=publisher-${isbn}>${book.publisher}</td>
  <td id=price-${isbn}>${book.price}</td>
  <td id=stock-${isbn}>${book.stock}</td>
  <td><input id=addCnt-${isbn} type="number" min="1" vaule="1"/><button class="addBtn" data-isbn="${isbn}">추가</button></td>
  `;

    tbody.appendChild(tr);
  }
}
