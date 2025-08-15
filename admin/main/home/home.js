document.addEventListener("DOMContentLoaded", () => {
  console.log("Admin Panel Loaded - Fetching stats...");

  setTimeout(() => {
    document.getElementById("totalShops").textContent = "142";
    document.getElementById("activeUsers").textContent = "2,560";
    document.getElementById("categories").textContent = "17";
    document.getElementById("pendingShops").textContent = "4";
  }, 1000);
});
