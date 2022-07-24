import axios from "axios";

let port = "localhost"

async function RetrieveVnaAirport() {
    let data = await GetCities();
    let response = []
    let full = [];
    let obj = getAvailableRoute(data.Groups);
    for (let i = 0; i < data.Airport.length; i++) {
        if (obj[data.Airport[i].Code].length != 0) {
            response.push({
                cityId: data.Airport[i].Id,
                name: data.Airport[i].DisplayName,
                airport: data.Airport[i].Code,
                countryId: data.Airport[i].Country
            })
        } else continue;
    }


    full.push(sortByAlphabet(response));
    full.push(data.Groups);
    full.push(obj);
    return full;
}

function sortByAlphabet(data) {
    return data.sort((a, b) => a.name.localeCompare(b.name))
}

function getAvailableRoute(data) {
    let obj = {};
    for (let i = 0; i < data.length; i++) {
        let airport = data[i].Airports;
        for (let j = 0; j < airport.length; j++) {
            let code = airport[j].Code;
            let DlGroup = airport[j].DLGroups;
            let airportArray = [];
            DlGroup.forEach(element => {
                airportArray = [...airportArray, ...element.Airports];
            });
            obj[code] = airportArray
        }
    }
    return obj;
}

async function cityMapping() {
    let data = await GetCities();
    let response = {};
    for (let i = 0; i < data.Airport.length; i++) {
        let obj = {
            cityId: data.Airport[i].Id,
            name: data.Airport[i].DisplayName,
            airport: data.Airport[i].Code,
            countryId: data.Airport[i].Country
        }
        response[data.Airport[i].Code] = obj
    }

    return response;

}


async function GetCities() {
    const option = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/vna/airports`,
        contentType: "application/json",
        dataType: "json",
        // headers: {        'Access-Control-Allow-Origin' : '*', 
        //         'Access-Control-Allow-Methods' :    'GET'
        // },
    })

    return option.data;
}

async function getTickets(){
    const option = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/ticket/all`,
        contentType: "application/json",
        dataType: "json",
    })

    return option.data;
}

export { RetrieveVnaAirport, cityMapping ,sortByAlphabet, getTickets};