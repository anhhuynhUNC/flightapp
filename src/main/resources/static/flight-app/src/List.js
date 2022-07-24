import { useEffect, useState } from "react";
import axios from "axios";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSort } from '@fortawesome/free-solid-svg-icons'
import { faRegistered as faSortLight } from '@fortawesome/free-regular-svg-icons'
import {toLocaleDate, getTime} from './Utility'

let port = "localhost";

function List(props) {
    let [data, setData] = useState([[]]);
    let [loadDisplay, setDisplay] = useState([]);
    let [loadIndex, setLoadIndex] = useState(0);
    let [buttonToggle, setButton] = useState(false);
    let state = { depart: props.depart, arrive: props.arrive, ticket: props.ticket } // using state
    useEffect(() => {
        retreiveAll(state.depart, state.arrive, state.ticket).then(val => {
            setData(restrictLoad(val));
            if (val.length !== 0) props.displayCallback(true);
            else props.displayCallback(false);
        });
    }, [state.depart, state.arrive, state.ticket])

    // load once when data is updated to actual display
    useEffect(() => {
        console.log(data);
        setDisplay(data[0]);
        setLoadIndex(0)
        if (data[0].length !== 0) setButton(true)
        else setButton(false);
    }, [data])

    function sortByDay() {
        retreiveAllByDate(state.depart, state.arrive, state.ticket).then(val => { setData(restrictLoad(val)); })
    }

    function sortByPrice() {
        retreiveAll(state.depart, state.arrive, state.ticket).then(val => { setData(restrictLoad(val)) })
    }

    return (
        <div className="flights-table-container main-list">
            <div>
                <h1>Các chuyến bay của chặng</h1>
            </div>
            <table className="flights-table">
                <tbody>
                    <tr className="th">
                        <th>Từ</th>
                        <th>Đến</th>
                        <th onClick={sortByDay}><span className="table-head">Ngày <FontAwesomeIcon className="test" icon={faSort} /></span></th>
                        <th>Loại vé</th>
                        <th onClick={sortByPrice} className="table-head">Giá  <FontAwesomeIcon className="test" icon={faSort} /></th>
                    </tr>
                    {loadDisplay.map(function (val, i) {
                        return (
                            <tr className="flights-table-value-row" key={val.orderId + ""}>
                                <td ><p>{val.outDepartCity} </p></td>
                                <td><p>{val.outArriveCity}</p> </td>
                                <td><p style ={{margin: 0}}>Khởi hành: {toLocaleDate(val.date)}</p><br></br>
                                <p style ={{margin: 0}}>Lúc: {getTime(val.outFlightTime)}</p></td>
                                <td><p>{val.ticketId !== undefined ? (val.ticketSeat + " | " + val.ticketType) : "Loading"}</p></td>
                                <td><p>{val.price}</p></td>
                            </tr>
                        )

                    })}
                </tbody>
            </table>
            <div> {/*TODO : fix when no routes*/}
                {buttonToggle ? <button className='list-load-button' onClick={() => { handleClick(loadDisplay, data, setButton, loadIndex, setDisplay, setLoadIndex) }}>Load More</button> : <span></span>}
            </div>
           {(data[0].length == 0 && state.depart !== 'default' && state.arrive !=='default') ? <div>Không có kết quả</div> : <div></div>}

        </div>


    )
}

function handleClick(val, data, setButton, loadIndex, setDisplay, setLoadIndex) {
    if (loadIndex >= data.length - 2) {
        setButton(false);
    }

    setDisplay([...val, ...data[loadIndex + 1]]);
    setLoadIndex(loadIndex + 1);
}



function restrictLoad(val) {
    let result = [];
    let blank = [[]];
    for (let i = 0; i < val.length; i += 10) {
        let sub = [];
        for (let j = 0; j < 10; j++) {
            if (i + j >= val.length) break;
            sub.push(val[i + j]);
        }
        result.push(sub);
    }
    if (result.length == 0) return blank; //return blank template for pre loading
    return result;
}

async function retreiveAll(depart, arrive, ticket) {
    if (depart == 'default' || arrive == 'default' || ticket =='default') return [];
    const option = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/order/all/sorted/p?depart=` + depart + '&arrive=' + arrive + '&ticket=' + ticket,
        contentType: "application/json",
        dataType: "json"
    })

    return option.data;
}

async function retreiveAllByDate(depart, arrive, ticket) {
    if (depart == 'default' || arrive == 'default' || ticket =='default') return [];
    const option = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/order/all/sorted/d?depart=` + depart + '&arrive=' + arrive  + '&ticket=' + ticket,
        contentType: "application/json",
        dataType: "json"
    })

    return option.data;
}


export default List;