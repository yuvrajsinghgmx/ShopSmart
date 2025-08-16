function searchShop() {
  const input = document.getElementById("searchInput").value.trim().toLowerCase();
  const rows = document.querySelectorAll("#shopTable tr");

  rows.forEach(row => {
    const shopId = row.cells[0].textContent.trim().toLowerCase();
    if (input === "" || shopId.includes(input)) {
      row.style.display = "";
    } else {
      row.style.display = "none";
    }
  });
}

document.addEventListener("click", (e) => {
  if (e.target.classList.contains("approve")) {
    alert("Shop approved!");
  } else if (e.target.classList.contains("reject")) {
    alert("Shop rejected!");
  } else if (e.target.classList.contains("delete")) {
    if (confirm("Are you sure you want to delete this shop?")) {
      e.target.closest("tr").remove();
    }
  }
});
