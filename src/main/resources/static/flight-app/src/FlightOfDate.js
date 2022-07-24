import { useState } from "react";
import axios from "axios";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSort, faSquareXmark } from '@fortawesome/free-solid-svg-icons'
import { toLocaleDate, sortByDate, sortByPrice } from './Utility.js'

function FlightOfDate(props) {
    let [data, setData] = useState(props.data);

    function daySort() {
        setData(sortByDate(data));
    }

    function priceSort() {
        setData(sortByPrice(data));
    }

    return (
        <div className="flights-table-container">
            {!props.dayLoading ?
                <div className="flights-on-day-content">
                    <div>
                        <h1>Các chuyến bay trong ngày {toLocaleDate(data[0].date)}</h1>
                        <button className='mode-button' onClick={() => { props.modedisplay(0) }}><FontAwesomeIcon className="test" icon={faSquareXmark} /></button>
                    </div>
                    <div className="flights-on-day-content-main">
                        <table className="flights-on-day-table">
                            <tbody>
                                <tr className="th">
                                    <th><span onClick={daySort} className="table-head">Thời gian <FontAwesomeIcon className="test" icon={faSort} /></span></th>
                                    <th>Từ</th>
                                    <th>Đến</th>
                                    <th>Loại vé</th>
                                    <th><span onClick={priceSort} className="table-head">Giá  <FontAwesomeIcon className="test" icon={faSort} /></span></th>
                                </tr>
                                {data.map(function (val, i) {
                                    return (
                                        <tr className="flights-on-day-table-value-row" key={val.orderId + ""}>
                                            <td><p style={{ margin: 0 }}>Từ: {toLocaleDate(val.outFlightTime) + ", " + getTime(val.outFlightTime)}</p><br></br>
                                                <p style={{ margin: 0 }}>Đến: {toLocaleDate(val.arriveFlightTime) + ", " + getTime(val.arriveFlightTime)}</p></td>
                                            <td ><p>{val.outDepartCity} </p></td>
                                            <td><p>{val.outArriveCity}</p> </td>
                                            <td><p>{val.ticketId !== undefined ? (val.ticketSeat + " | " + val.ticketType) : "Loading"}</p></td>
                                            <td><p>{val.price}</p></td>
                                        </tr>
                                    )

                                })}
                            </tbody>
                        </table>
                    </div>
                </div> : <div className="flights-on-day-content">Loading......</div>}
        </div>
    )
}

function getTime(val) {
    console.log(val);
    let timeStamp = Date.parse(val);
    let date = new Date(timeStamp);

    let hours = date.getHours();
    let minutes = date.getMinutes();

    if (hours < 10) hours = "0" + hours;
    if (minutes < 10) minutes = "0" + minutes;

    return hours + ":" + minutes;

}

export default FlightOfDate;