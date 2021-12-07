<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css"
          integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn"
          crossorigin="anonymous">
    <title>Mobile by Price</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ page isELIgnored="false"%>
</head>
<body>
<div class="text-right">
<a href="/Mobiles" class="btn btn-primary">Back to home</a>
</div>
<div class="container">
    <form method="POST" action="get-by-price">
        <br><br>
        <h1><b>Get mobiles details by price</b></h1>
        <br>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="min_price_input">Minimum Price</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="minPrice" class="form-control"
                       id="min_price_input" placeholder="Enter Minimum Price">
            </div>
        </div>
        <div class="form-row">
            <div class="form-group col-4">
                <label for="max_price_input">Minimum Price</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="maxPrice" class="form-control"
                       id="max_price_input" placeholder="Enter Maximum Price">
            </div>
        </div>
        <div class="form-row">
            <div class="col-4 ">
                <button type="submit" class="btn btn-outline-primary" style="width:200px">
                    Get
                </button>
            </div>
            <div class="col-8">
                <button type="reset" class="btn btn-outline-primary" style="width:200px">
                    Reset
                </button>
            </div>
        </div>
    </form>
</div>

<br><br>

<style>
th,td{padding-left:10px;}
</style>
<div class="container-fluid text-center">
    <table>
        <thead>
        <tr>
            <th>Brand Name</th>
            <th>Model Number</th>
            <th>Model Name</th>
            <th>Type</th>
            <th>Ram</th>
            <th>Rom</th>
            <th>Price</th>
            <th>Availability</th>
        </tr>
        </thead>
        <c:forEach var="i" items="${data}">
            <tr>
                <td>${i.brandName}</td>
                <td>${i.modelNumber}</td>
                <td>${i.modelName}</td>
                <td>${i.type}</td>
                <td>${i.ram}</td>
                <td>${i.rom}</td>
                <td>${i.price}</td>
                <td>${i.availability}</td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>