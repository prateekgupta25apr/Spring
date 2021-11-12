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
    <form method="POST" action="update-availability-by-model-name">
        <br><br>
        <h1><b>Update availability by model name</b></h1>
        <br>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="model_name_input">Model Name</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="modelName" class="form-control"
                       id="model_name_input" placeholder="Enter Model Name">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="availability_input">Updated Availability</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="availability" class="form-control"
                       id="availability_input" placeholder="Enter Availability">
            </div>
        </div>
        <div class="form-row">
            <div class="col-4 ">
                <button type="submit" class="btn btn-outline-primary" style="width:200px">
                    Update
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


</body>
</html>