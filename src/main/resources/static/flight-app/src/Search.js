import axios from "axios";
import { useEffect, useState } from "react";
import { RetrieveVnaAirport, cityMapping, sortByAlphabet, getTickets } from "./RetreiveVnaAirport";

let port = "localhost";

function Search(props) {
    let [inputs, setInput] = useState({ departInput: 'default', arriveInput: 'default', ticketInput: 1 }); //default
    let [cities, setCity] = useState([]);
    let [hasChange, setState] = useState(false);
    let [data, setData] = useState([]);
    let [tickets, setTicket] = useState([]);

    let [arrive, setArrive] = useState([]);
    let [mapping, setMapping] = useState({});

    // run once for retreiving cities and ticket
    useEffect(() => {
        RetrieveVnaAirport().then(val => { setData(val); setCity(val[0]); setArrive(val[0]) })
        getTickets().then(val => { setTicket(val) })
        cityMapping().then(val => { setMapping(val) })
    }, []);

    //update data everytime inputs change
    useEffect(() => {
        if (hasChange) {
            getFlightsFrom(inputs.departInput, inputs.arriveInput, inputs.ticketInput).then(val => {
                props.method(val, true);
                // console.log(val);
            });
            getMaxFrom(inputs.departInput, inputs.arriveInput, inputs.ticketInput).then(val => {
                props.method(val, false)
            })
            props.loadingCallback(false);
            props.updateDepart(inputs.departInput);
            props.updateArrive(inputs.arriveInput);
            props.updateTicket(inputs.ticketInput);
            props.modedisplay(0)
        }
    }, [inputs.arriveInput, inputs.departInput, inputs.ticketInput])    //remove depart input later

    useEffect(() => {
        if (hasChange) {
            if (inputs.departInput != 'default') {
                let list = data[2][inputs.departInput].map((val, i) => { return mapping[val.Code] });
                setArrive(sortByAlphabet(list));
            }
            else setArrive(data[0])
            props.updateDepart(inputs.departInput);
            props.updateArrive(inputs.arriveInput);
            props.updateTicket(inputs.ticketInput);
        }
    }, [inputs.departInput]);

    const handleChange = e => {
        setInput(state => ({ ...state, [e.target.name]: e.target.value })); // convert to integer and set new state of inputs
        props.loadingCallback(true);
        setState(true);
    }

    return (
        <div className="selection-div" style={props.loading ? { pointerEvents: "none", opacity: "0.4" } : {}}>
            <label>Cities</label><br></br>
            Điểm Đi <select className="city-select" options={cities} type="number" id="departInput" name="departInput" value={inputs.departInput} onChange={handleChange}>
                <option key={"none"} value={'default'}>Chọn Điểm Đi</option>
                {cities.map((val, i) => {
                    return (<option key={"depart" + val.cityId} value={val.airport}>{val.name}</option>)
                })}
            </select>

            Điểm Đến < select className="city-select" type="number" id="arriveInput" name="arriveInput" value={inputs.arriveInput} onChange={handleChange} >
                <option key={"none"} value={'default'}>Chọn Điểm Đến</option>
                {arrive.map((val, i) => {
                    return (<option key={"arrive" + val.cityId} value={val.airport}>{val.name}</option>)
                })}
            </select>

            Loại Vé < select className="ticket-select" type="number" id="ticketInput" name="ticketInput" value={inputs.ticketInput} onChange={handleChange} >
                <option key={"none"} value={'default'}>Chọn vé</option>
                {tickets.map((val, i) => {
                    return (<option key={"ticket" + val.ticketId} value={val.ticketId}>{val.type + " | " + val.seat}</option>)
                })}
            </select>
        </div>
    )
}

async function getFlightsFrom(depart, arrive, ticket) {
    if (depart == 'default' || arrive == 'default' || ticket == 'default') return null
    const option = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/display?depart=${depart}&arrive=${arrive}&ticket=${ticket}`,
        contentType: "application/json",
        dataType: "json"
    })
    return option.data;
}

async function getMaxFrom(depart, arrive, ticket) {
    if (depart == 'default' || arrive == 'default' || ticket == 'default') return null
    const option = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/maxDisplay?depart=${depart}&arrive=${arrive}&ticket=${ticket}`,
        contentType: "application/json",
        dataType: "json"
    })
    return option.data;
}

export default Search;