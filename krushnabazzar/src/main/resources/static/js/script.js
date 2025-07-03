// Inside script.js
$(document).ready(function () {
  console.log("script.js loaded");

  window.togglesidebar = function () {
    if ($(".sidebar").is(":visible")) {
      $(".sidebar").css("display", "none");
      $(".content").css("margin-left", "0%");
    } else {
      $(".sidebar").css("display", "block");
      $(".content").css("margin-left", "20%");
    }
  };
});

const search = () => {
  let query = $("#search-input").val().trim();
  let category = $("meta[name='category']").attr("content"); // <-- get current category

  if (query === "") {
    $(".search-result").hide();
  } else {
    let url = `http://localhost:8080/product/search/${encodeURIComponent(query)}`;

    // append category if it's present
    if (category) {
      url += `?category=${encodeURIComponent(category)}`;
    }

    fetch(url)
      .then((response) => {
        if (!response.ok) throw new Error("Network error");
        return response.json();
      })
      .then((data) => {
        let text = `<div class='list-group'>`;

        if (data.length === 0) {
          text += `<div class='list-group-item'>No products found</div>`;
        }

        data.forEach((product) => {
          if (product.name) {
            text += `<a href='/product/${product.id}' class='list-group-item list-group-item-action'>
                        ${product.name} <small class="text-muted">(${product.category})</small>
                    </a>`;
          }
        });

        text += `</div>`;

        $(".search-result").html(text).show();
      })
      .catch((err) => {
        console.error("Fetch failed:", err);
      });
  }
};

//for normal user
const Search = () => {
    let query = $("#search-input").val().trim();
    let category = $("meta[name='category']").attr("content"); // <-- get current category

    if (query === "") {
      $(".search-result").hide();
    } else {
      let url = `http://localhost:8080/user/products/search/${encodeURIComponent(query)}`;

      // append category if it's present
      if (category) {
        url += `?category=${encodeURIComponent(category)}`;
      }

      fetch(url)
        .then((response) => {
          if (!response.ok) throw new Error("Network error");
          return response.json();
        })
        .then((data) => {
          let text = `<div class='list-group'>`;

          if (data.length === 0) {
            text += `<div class='list-group-item'>No products found</div>`;
          }

          data.forEach((product) => {
            if (product.name) {
              text += `<a href='/user/products/${product.id}' class='list-group-item list-group-item-action'>
                          ${product.name} <small class="text-muted">(${product.category})</small>
                      </a>`;
            }
          });

          text += `</div>`;

          $(".search-result").html(text).show();
        })
        .catch((err) => {
          console.error("Fetch failed:", err);
        });
    }
  };



