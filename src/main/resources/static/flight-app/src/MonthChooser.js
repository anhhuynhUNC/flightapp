import { useEffect, useState } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faChevronDown } from '@fortawesome/free-solid-svg-icons'

function MonthChooser(props) {
    let [fullMonth, setFull] = useState([[]]);
    let [tinyIndex, setTiny] = useState(0);

    function loadMonths() {
        let temp = []
        for (let i = 0; i < props.monthChooser.length; i += 3) {
            let sub = [];
            for (let j = 0; j < 3; j++) {
                if (i + j >= props.monthChooser.length) break;
                else sub.push(i + j);
            }
            temp.push(sub);
        }
        return temp;
    }

    useEffect(() => {
        setFull(loadMonths());
    }, [props.monthChooser])

    return (
        <div className='month-container'>
            {tinyIndex != 0 ? <button className='switch-month-button-left' onClick={() => {
                setTiny(tinyIndex -= 1);
                props.setIndex(fullMonth[tinyIndex][0]);
            }}></button> : <div></div>}<br></br>
            {/*  0 */}
            <div className="month-button-container">
                {fullMonth[tinyIndex].map(function (val, i) {
                    return <button className={`month-button ${val === props.index ? 'active' : 'non-active'}`} id={'month-id-' + val} key={val} onClick={() => {
                        if (hasFlights(props.data[val])) {
                            props.setIndex(val);
                            console.log(props.data[val]);
                        }
                    }}>
                        {hasFlights(props.data[val]) ?
                            <div className="month-button-sub">
                                <h3>{"Tháng " + props.data[val][0].month}</h3><br></br>
                                <p>Bắt đầu từ</p><br></br><p>{props.min[val][0] + " USD$"}</p>
                                <br></br>
                                <div className='month-button-show-list'>
                                    <p className={props.loading ? 'active' : 'non-active'} onClick={() => { props.monthcallback(props.data[val][0].monthId); props.modedisplay(1); props.switchcallback(false) }}>{!props.loading ? 'Xem các chặng bay' : 'Đang tải'} <FontAwesomeIcon className="test" icon={faChevronDown} /> </p>
                                </div>
                            </div> :
                            <div>
                                <h3>{"Tháng " + props.data[val][0].month}</h3><br></br>
                                <p>Không có kết quả</p><br></br><p>{props.route}</p>
                            </div>
                        }
                    </button>

                })}
            </div>
            {tinyIndex != fullMonth.length - 1 ? <button className='switch-month-button-right' onClick={() => {
                setTiny(tinyIndex += 1); props.setIndex(fullMonth[tinyIndex][0]);
            }}></button> : <div></div>}
        </div>
    )

}

function hasFlights(array) {
    if (array === undefined) return "data not loaded";
    for (let i = 0; i < array.length; i++) {
        if (array[i].price != '0') return true
    }
    return false;
}


export default MonthChooser;