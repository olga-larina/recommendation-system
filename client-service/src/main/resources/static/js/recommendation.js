window.onload = function () {

    function resetAll() {
        resetRecommendationsTable();
    }

    function resetRecommendationsTable() {
        var old_tbody = document.getElementById('tab-recommendations-data').getElementsByTagName('tbody')[0];
        old_tbody.parentNode.replaceChild(document.createElement('tbody'), old_tbody);
        document.getElementById('product-code-for-recommendations-input').value = ""
        document.getElementById('client-id-for-recommendations-input').value = ""
    }

    document.getElementById('btn-get-all-products').onclick = function () {
        getAllProducts()
            .then((response) => resetAll());
    };

    async function getAllProducts() {
        return await fetch('/product', {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json;charset=utf-8'
              }
            })
            .then((response) => {
                response.json().then(
                    products => {
                        console.log(products);
                        var new_tbody = document.createElement('tbody');
                        if (products.length > 0) {
                          products.forEach((product) => {
                            var row = new_tbody.insertRow();
                            cell0 = row.insertCell(0);
                            cell0.innerHTML = product.code;
                            cell1 = row.insertCell(1);
                            cell1.innerHTML = product.title;
                            cell2 = row.insertCell(2);
                            cell2.innerHTML = product.manufacturer.title;
                            cell3 = row.insertCell(3);
                            temp = "<ul>";
                            product.categories.forEach((category) => {
                                temp += "<li>" + category.title + "</li>";
                            });
                            temp += "</ul>";
                            cell3.innerHTML = temp;
                            cell4 = row.insertCell(4);
                            cell4.innerHTML = `<button type="btn-view-product">View</button><br /><input type="checkbox" name="cbx-view-product" value="Viewed" disabled readonly/>`;
                            cell5 = row.insertCell(5);
                            cell5.innerHTML = `<button type="btn-fav-product">Favorites</button><br /><input type="checkbox" name="cbx-fav-product" value="In favorites" disabled readonly/>`;
                            cell6 = row.insertCell(6);
                            cell6.innerHTML = `<button type="btn-cart-product">Cart</button><br /><input type="checkbox" name="cbx-cart-product" value="In cart" disabled readonly/>`;
                            cell7 = row.insertCell(7);
                            cell7.innerHTML = `<button type="btn-get-recommendations">Recommendations</button>`;
                          });
                        }
                        var old_tbody = document.getElementById('tab-products-data').getElementsByTagName('tbody')[0];
                        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
                        document.querySelectorAll('[type=btn-view-product]').forEach((btn) => {
                            btn.addEventListener('click', function() {
                              onProductView(this);
                            });
                        });
                        document.querySelectorAll('[type=btn-fav-product]').forEach((btn) => {
                            btn.addEventListener('click', function() {
                              onProductFavorites(this);
                            });
                        });
                        document.querySelectorAll('[type=btn-cart-product]').forEach((btn) => {
                            btn.addEventListener('click', function() {
                              onProductCart(this);
                            });
                        });
                        document.querySelectorAll('[type=btn-get-recommendations]').forEach((btn) => {
                            btn.addEventListener('click', function() {
                              onProductGetRecommendations(this);
                            });
                        });
                    }
                )
            });
    }

    function onProductView(btn) {
        row = btn.parentElement.parentElement;
        checkbox = row.cells[4].getElementsByTagName('input')[0]
        productView(row.cells[0].innerHTML)
            .then((response) => checkbox.checked = response)
            .then((response) => resetAll());
    }

    function onProductFavorites(btn) {
        row = btn.parentElement.parentElement;
        checkbox = row.cells[5].getElementsByTagName('input')[0]
        productFavorites(row.cells[0].innerHTML)
            .then((response) => checkbox.checked = response)
            .then((response) => resetAll());
    }

    function onProductCart(btn) {
        row = btn.parentElement.parentElement;
        checkbox = row.cells[6].getElementsByTagName('input')[0]
        productCart(row.cells[0].innerHTML)
            .then((response) => checkbox.checked = response)
            .then((response) => resetAll());
    }

    function onProductGetRecommendations(btn) {
        row = btn.parentElement.parentElement;
        var productCode = row.cells[0].innerHTML;
        document.getElementById('product-code-for-recommendations-input').value = productCode
        getRecommendationsByProductCode(productCode);
    }

    async function productView(code) {
        return await fetch('/view/' + code, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json;charset=utf-8'
            }
        });
    }

    async function productFavorites(code) {
        return await fetch('/favorites/' + code, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json;charset=utf-8'
            }
        });
    }

    async function productCart(code) {
        return await fetch('/cart/' + code, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json;charset=utf-8'
            }
        });
    }

    async function getRecommendationsByProductCode(productCode) {
        return await fetch('/recommendation/' + productCode, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json;charset=utf-8'
              }
            })
            .then((response) => {
                response.json().then(
                    recommendation => {
                        console.log(recommendation);
                        document.getElementById('client-id-for-recommendations-input').value = recommendation.clientId
                        var products = recommendation.product
                        var new_tbody = document.createElement('tbody');
                        if (products.length > 0) {
                          products.forEach((product) => {
                            var row = new_tbody.insertRow();
                            cell0 = row.insertCell(0);
                            cell0.innerHTML = product.code;
                            cell1 = row.insertCell(1);
                            cell1.innerHTML = product.title;
                            cell2 = row.insertCell(2);
                            cell2.innerHTML = product.manufacturer.title;
                            cell3 = row.insertCell(3);
                            temp = "<ul>";
                            product.categories.forEach((category) => {
                                temp += "<li>" + category.title + "</li>";
                            });
                            temp += "</ul>";
                            cell3.innerHTML = temp;
                          });
                        }
                        var old_tbody = document.getElementById('tab-recommendations-data').getElementsByTagName('tbody')[0];
                        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
                    }
                )
            });
    }
}