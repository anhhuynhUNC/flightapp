import axios from "axios";
import { useEffect, useState } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSort,faSquareXmark } from '@fortawesome/free-solid-svg-icons'
import { toLocaleDate, getTime, sortByDate, sortByPrice} from './Utility'

let port = "localhost";

function MonthList(props) {
    let [data, setData] = useState([]);
    useEffect(() => {
        console.log(props.switchingRoute);
        if (props.monthId == -1) return
        else if(props.loading) return // restrict user from interacting when retrieval has not been returned
        else if(!props.switchingRoute){
            props.loadingCallback(true);
            console.log(props.loading)
            getMonthList(props.monthId, props.depart, props.arrive, false, props.ticket).then(val => { setData(val); props.loadingCallback(false); props.switchcallback(true); console.log(val) })
        } 
    }, [props.depart, props.arrive, props.monthId])


    function switchMode(){
        props.modedisplay(0)
    }

    function daySort(){
        setData(sortByDate(data));
    }

    function priceSort(){
        setData(sortByPrice(data));
    }

    return (
        <div className="flights-table-container">
            {props.loading? <div>Loading ..................</div> :
            <div className='flights-month-content-main'>
                <div className = 'flights-month-title'>
                    <h1>Các chuyến bay của tháng {data[0] !== undefined ? data[0].month : ''}</h1>
                    <button className ='mode-button' onClick={switchMode}><FontAwesomeIcon className="test" icon={faSquareXmark} /></button>
                </div>
                <table className="flights-month-table">
                    <tbody>
                        <tr className="th">
                            <th>Từ</th>
                            <th>Đến</th>
                            <th><span onClick={daySort} className="table-head">Ngày <FontAwesomeIcon className="test" icon={faSort} /></span></th>
                            <th>Loại vé</th>
                            <th><span onClick={priceSort} className="table-head">Giá  <FontAwesomeIcon className="test" icon={faSort} /></span></th>
                        </tr>
                        {data.map(function (val, i) {
                            return (
                                <tr className="flights-month-table-value-row" key={val.orderId + ""}>
                                    <td ><p>{val.outDepartCity} </p></td>
                                    <td><p>{val.outArriveCity}</p> </td>
                                    <td><p style={{ margin: 0 }}>Khởi hành: {toLocaleDate(val.date)}</p><br></br>
                                        <p style={{ margin: 0 }}>Lúc: {getTime(val.outFlightTime)}</p></td>
                                    <td><p>{val.ticketId !== undefined ? (val.ticketSeat + " | " + val.ticketType) : "Loading"}</p></td>
                                    <td><p>{val.price}</p></td>
                                </tr>
                            )

                        })}
                    </tbody>
                </table>
            </div>}
        </div>
    )
}

async function getMonthList(id, depart, arrive, sort, ticket) {
    if (depart == 'default' || arrive == 'default' || ticket =='default') return null;
    const option = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/order/byMonth?id=${id}&depart=${depart}&arrive=${arrive}&sort=${sort}&ticket=${ticket}`,
        contentType: "application/json",
        dataType: "json"
    })
    return option.data;
}

export default MonthList;