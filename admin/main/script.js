function loadPage(page) {
  fetch(page)
    .then(response => {
      if (!response.ok) throw new Error(`Failed to load ${page}`);
      return response.text();
    })
    .then(html => {
      document.getElementById("mainContent").innerHTML = html;
    })
    .catch(err => {
      document.getElementById("mainContent").innerHTML = `<p>Error loading page.</p>`;
      console.error(err);
    });
}

document.querySelectorAll(".nav-link").forEach(link => {
  link.addEventListener("click", (e) => {
    e.preventDefault();
    document.querySelectorAll(".nav-link").forEach(l => l.classList.remove("active"));
    link.classList.add("active");

    loadPage(link.dataset.page);
  });
});
loadPage("home/home.html");
