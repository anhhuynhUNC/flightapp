$(document).ready(function () {
    //$("#p").append("PABLOOOOOOO");
    addCity();
    addRoute();
    loadValue();
    setUpbar();
    loadDisplay();
});

function setUpbar() {
    //for now preload 30 days 
    for (let i = 0; i < 30; i++) {
        $(".chart-container").append("<div class= 'chart-bar chart" + i + "'>");
        $(".chart" + i).append("<button class= 'bar bar" + i + "'>");
    }


}

function loadDisplay() {
    $("#button3").on('click', function (e) {

        $.ajax({
            type: "GET",
            url: "/flight/display?" + "depart=" + $("#departselect").val() + "&" + "arrive=" + $("#arriveselect").val(),
            contentType: "application/json",
            dataType: "json"
        }).then(function (data) {
            console.log(data);

            for (let j = 0; j < data[0].length; j++) {
                if (data[0][j] != null) {
                    console.log(".bar" + j)
                    $(".bar" + j).css("height", "" + data[0][j].price * 1 / 39)

                } else {
                    $(".bar" + j).css("height", "10px")
                }
            }

        })
    })
}


function loadValue() {
    $.ajax({
        type: "GET",
        url: "/flight/city/all",
        contentType: "application/json",
        dataType: "json"

    }).then(function (data) {
        console.log(data[0].name);
        for (let i = 0; i < data.length; i++) {
            let item = data[i];
            $('#departselect').append($('<option>',
                {
                    value: item.cityId,
                    text: item.name
                }))
            $('#arriveselect').append($('<option>',
                {
                    value: item.cityId,
                    text: item.name
                }))
        }
    })
}

function addRoute() {
    $("#button2").on('click', function (e) {
        let depart = $("#depart").val();
        let arrive = $("#arrive").val();
        object = {
            "depart": { "cityId": 6, "name": "London ", "airport": "London Airport" },
            "arrive": { "cityId": 5, "name": "Cai Dau ma", "airport": "CDG" },
            "name": "London to TP Ho Chi Minh"
        }
        console.log(object);
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: "/flight/route/add",
            data: JSON.stringify(object),
            dataType: "json"


        }).then(function (data) {
            console.log(data);
        })



    })
}


function addCity() {
    $("#button").on('click', function (e) {
        let name = $("#name").val();
        let airport = $("#airport").val();
        object = {
            "name": name,
            "airport": airport
        }
        console.log(object);
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: "/flight/city/add",
            data: JSON.stringify(object),
            dataType: "json"


        }).then(function (data) {
            console.log(data);
        })



    })
}