<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css"
          integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn"
          crossorigin="anonymous">

    <title>Registration</title>
</head>
<body>

<div class="container">
    <form method="POST" action="save">
        <br><br>
        <h1><b>Registration</b></h1>
        <br>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="brand_name_input">Brand Name</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="brandName" class="form-control"
                       id="brand_name_input" placeholder="Enter brand name">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="model_number_input">Model Number</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="modelNumber" class="form-control"
                       id="model_number_input" placeholder="Enter model number">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="model_name_input">Model Name</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="modelName" class="form-control"
                       id="model_name_input" placeholder="Enter model name">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="type_input">Type</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="type" class="form-control"
                       id="type_input" placeholder="Enter type of mobile">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="ram_input">Ram</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="ram" class="form-control"
                       id="ram_input" placeholder="Enter ram of mobile">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="rom_input">Rom</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="rom" class="form-control"
                       id="rom_input" placeholder="Enter rom of mobile">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="price_input">Price</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="price" class="form-control"
                       id="price_input" placeholder="Enter price of mobile">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-4">
                <label for="availability_input">Availability</label>
            </div>
            <div class="form-group col-8">
                <input type="text" name="availability" class="form-control"
                       id="availability_input" placeholder="Enter availability of mobile">
            </div>
        </div>

        <div class="form-row">
            <div class="col-4 ">
                <button type="submit" class="btn btn-outline-primary" style="width:200px">
                    Save
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


<!-- Option 1: jQuery and Bootstrap Bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-fQybjgWLrvvRgtW6bFlB7jaZrFsaBXjsOMm/tB9LTS58ONXgqbR9W8oWht/amnpF"
        crossorigin="anonymous"></script>


</body>
</html>