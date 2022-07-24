import logo from './logo.svg';
import './App.css';
import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Display from './Display';
import List from './List';
import Search from './Search';
import FlightOfDate from './FlightOfDate';

import { setGlobalRoute } from './Utility';

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import {
  Bar, Line, getDatasetAtEvent,
  getElementAtEvent,
  getElementsAtEvent,
} from 'react-chartjs-2';
import MonthList from './MonthList';



ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

function App() {
  let [data, setContent] = useState([[]]);
  let [secondData, setSecondData] = useState([[]]);
  let [label, setLable] = useState([[]]);
  let [globalDepart, setGlobalDepart] = useState('default'); //default depart
  let [globalArrive, setGlobalArrive] = useState('default'); //default arrive
  let [globalTicket, setGlobalTicket] = useState(1); // default ticket
  let [dayData, setDayData] = useState([{}]);
  let [loading, setloading] = useState(false);
  let [annotation, setAnnotation] = useState([[]]);
  let [displayMode, setDisplayMode] = useState(false); //display of the graph
  let [globalMonthId, setMonthId] = useState(-1);
  let [modeDisplay, setModeDisplay] = useState(0); //display between month, day, or none table
  let [switchingRoute, setSwitch] = useState(true); // if only switching route, do not immediately query month

  useEffect(() => {

  }, []);

  /**
   * accepts the value of all flights in the future and set up the neccessary arrays
   * @param {*} val the value to populate display arrays
   * @param {*} fn useState function to update based on val
   * 
   */
  async function setUp(val, fn) {
    if (val == [] || val == undefined || val == null) {
      setDisplayMode(false);
      return;
    }

    let displayArray = [];
    let label = [];
    let monthSeperator = [];

    let firstMonth = val[0].month;
    let nextMonth = firstMonth + 1;
    let current = 0;
    let nextMonthIndex = 0;
    let passed = false;
    let tempMonth = 0;

    // TODO: Update values : now using 4 months => could be any
    let timestamp = Date.parse(val[val.length - 1].date);
    let finalDate = new Date(timestamp);
    let firstDate = new Date(Date.parse(val[0].date))
    for (let index = 0; index < monthDiff(firstDate, finalDate) + 1; index += 1) {  //firstMonth
      let monthArray = [];
      let monthSeperatorSub = [];
      for (let i = current; i < 37 + current; i++) {
        if (i >= val.length || i < 0) break;
        let obj;
        let current2 = new Date(val[i].date);
        //if first instance of next month then set index
        if (current2.getMonth() == nextMonth - 1 && !passed) {
          nextMonthIndex = i;
          passed = true;
        }

        if (current2.getMonth() + 1 !== val[current].month && current2.getDate() == 1) {
          let annote = {
            id: 'abtr' + i,
            type: 'line',
            xMin: monthArray.length - 0.5,
            xMax: monthArray.length - 0.5,

            borderDash: [8, 5],
            borderDashOffset: 0,
            borderWidth: 2,
            backgroundColor: 'rgba(255, 99, 132, 0.5)'
          }
          monthSeperatorSub.push(annote);
          tempMonth = i;
        }
        obj = { dayId: val[i].dayOfFlightId, day: current2.getDate(), price: val[i].price, dayOfWeek: val[i].dayOfWeek.substring(0, 2), month: val[i].month, route: val[i].routeName, fulldate: val[i].date, ticketSeat: val[i].ticketSeat, ticketType: val[i].ticketType, monthId: val[i].monthId }
        monthArray.push(obj);
        // if is final date of not the first month, then set next starting index
        if (index != 0 && i == 37 + current - 1) {
          nextMonthIndex = tempMonth;
        }
      }
      displayArray.push(monthArray);
      monthSeperator.push(monthSeperatorSub);
      current = nextMonthIndex;
    }


    //set labels by going through all of display array and collect info
    for (let index = 0; index < displayArray.length; index++) {
      let subLabel = [];
      for (let i = 0; i < displayArray[index].length; i++) {
        subLabel.push(displayArray[index][i].day + ";" + displayArray[index][i].dayOfWeek + ";" + displayArray[index][i].month);
      }
      label.push(subLabel)
    }
    if (fn) {
      setContent(displayArray);
    } else {
      setSecondData(displayArray);
    }
    setAnnotation(monthSeperator);
    setLable(label);

    //TODO : HANDLE CHANGE
  }

  const modeFunc = () => {
    switch (modeDisplay) {
      case 0:
        return (<div></div>);
      case 1:
        return (<MonthList data={null} depart={globalDepart} arrive={globalArrive} monthId={globalMonthId} ticket={globalTicket} modedisplay={setModeDisplay} daycallback={setDayData} loading={loading} loadingCallback={setloading} switchingRoute={switchingRoute} switchcallback={setSwitch} />)
      case 2:
        return (<FlightOfDate data={dayData} dayLoading={loading} modedisplay={setModeDisplay} />)
    }
  }

  return (
    <div className="App">
      <div className='display-container'>
        {displayMode ? <Display data={data} data2={secondData} annotation={annotation} label={label} depart={globalDepart} arrive={globalArrive} ticket={globalTicket} daycallback={setDayData} loading={loading} daydisplaycb={setloading} monthcallback={setMonthId} modedisplay={setModeDisplay} switchcallback={setSwitch} /> : <div></div>}
      </div>
      <br></br>
      <Search callback={setContent} method={setUp} callback2={setSecondData} depart={globalDepart} arrive={globalArrive} ticket={globalTicket} updateDepart={setGlobalDepart} updateArrive={setGlobalArrive} updateTicket={setGlobalTicket} loading={loading} loadingCallback={setloading} modedisplay={setModeDisplay} />
      {modeFunc()}
      <p>Kiểm tra font chữ.</p>
      <List depart={globalDepart} arrive={globalArrive} ticket={globalTicket} displayCallback={setDisplayMode} />
    </div >
  );
}

function monthDiff(dateFrom, dateTo) {
  return dateTo.getMonth() - dateFrom.getMonth() +
    (12 * (dateTo.getFullYear() - dateFrom.getFullYear()))
}


export default App;
