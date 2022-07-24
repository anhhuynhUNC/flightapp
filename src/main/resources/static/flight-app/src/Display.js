import { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import { defaults, registerables } from 'chart.js';
import './App.css';
import annotationPlugin from "chartjs-plugin-annotation";
import MonthChooser from './MonthChooser';
import { toLocaleDate } from './Utility';

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
    Filler,
    ArcElement

} from 'chart.js';
import {
    Bar, Line, getDatasetAtEvent,
    getElementAtEvent,
    getElementsAtEvent,
    Chart, Pie, Bubble,

} from 'react-chartjs-2';
import { faIls } from '@fortawesome/free-solid-svg-icons';

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    PointElement,
    LineElement,
    ArcElement,
    Title,
    Tooltip,
    Legend,
    Filler,
    annotationPlugin
);

let port = "localhost"

function Display(props) {
    let [index, setIndex] = useState(0);
    let [test, setTest] = useState(0);
    let [cheapestFlights, setCheapestFlights] = useState([[]]);
    let [cheastFlightsVal, setCheapestFlightsVal] = useState([[]]);

    //add cheapest flights
    useEffect(() => {
        setCheapestFlights(props.data.map(getMins));
        setCheapestFlightsVal(props.data.map(getMinsVal));
    }, [props.data])


    //set defautl font
    defaults.font.size = 12;
    defaults.font.family = 'Open Sans'

    //options config
    const options = {
        maintainAspectRatio: false,
        devicePixelRatio: 2.5,
        animation: false,
        interaction: {
            mode: 'index',
        },
        plugins: {
            chartAreaBorder: {
                borderColor: 'red',
                borderWidth: 2,
            },
            annotation: {
                annotations: props.annotation[index],
                animation: {
                    duration: 0
                },
            },
            tooltip: {
                callbacks: {
                    title: function (context) {
                        if (context[0].raw.price !== 0) return [context[0].raw.route, toLocaleDate(context[0].raw.fulldate)]
                        else return "Không có kết quả"
                    },
                    label: function (context) {
                        if (context.raw.price == 0) return;
                        else return context.dataset.label + ": " + context.raw.price;
                    }
                }
            },
            legend: {
                onHover: function (event) {
                    event.native.target.style.cursor = 'pointer';
                },
                onLeave: function (event) {
                    event.native.target.style.cursor = '';
                }
            },
        },
        onHover: function (event, chartElement) {
            if (props.loading) { // if still loading other functions then disable interaction
                event.native.target.style.cursor = '';
                return;
            }

            if (chartElement[0]) {
                event.native.target.style.cursor = chartElement[0].element.$context.raw.price !== 0 ? 'pointer' : '';
            } else {
                event.native.target.style.cursor = '';
            }
        },
        scales: {
            y: {
                stacked: false,
                beginAtZero: true,
                ticks: {
                    //display: false
                },
                grid: {
                    display: false,
                    drawBorder: false
                }
            },
            x: {
                stacked: true,
                grid: {
                    display: false
                },
                ticks: {
                    maxRotation: 0,
                    minRotation: 0,
                    callback: function (label) {
                        let dayLabel = this.getLabelForValue(label) + "";
                        let array = dayLabel.split(";");
                        let day = array[0];
                        let name = array[1];
                        let month = array[2];
                        return [day, name];
                    },
                    autoSkip: false
                }
            },
            xAxis2: {
                //type: "Day_of_week",
                grid: {
                    drawOnChartArea: false,
                    //Uncomment if want to disable border
                    /* display: false,            
                    drawBorder:false, */
                },
                ticks: {
                    maxRotation: 0,
                    minRotation: 0,
                    autoSkip: false,
                    callback: function (label) {
                        let dayLabel = this.getLabelForValue(label);
                        let array = dayLabel.split(";");
                        let day = array[0];
                        let month = array[2];
                        if (day == 1) {
                            return "THG" + month
                        } else {
                            return '';
                        };
                    }
                }
            }
        }
    };

    const backgroundColor = ['rgb(5, 140, 179)', '#DBA510']
    // data config
    const data = {
        labels: props.label[index],   //: ['Jan', 'Feb'],
        datasets: [
            {
                label: 'Giá thấp nhất',
                data: props.data[index],
                minBarLength: 5,
                backgroundColor: function (c) {  // oringinal color "#058CB3",
                    if (cheapestFlights[index].includes(c.dataIndex)) return backgroundColor[1]
                    else return backgroundColor[0];
                },
                borderColor: 'rgb(5, 140, 179)',
                borderWidth: 0,
                borderRadius: 3,
                //borderSkipped: false,

                parsing: {
                    yAxisKey: 'price',
                    xAxisKey: 'day'
                },
            },
            {
                label: 'Giá cao nhất',
                data: props.data2[index],
                minBarLength: 0,
                backgroundColor: "rgb(184, 220, 242,0.5)", //rgb(75, 169, 196,0.5)
                borderColor: 'rgb(75, 169, 196,0.5)',
                borderWidth: 2,
                borderRadius: 3,

                parsing: {
                    yAxisKey: 'price',
                    xAxisKey: 'day'
                },
                fill: true,
                hidden: true,
            }]
    };

    const processElementAtEvent = (element) => {
        if (!element.length) return;

        const { datasetIndex, index } = element[0];

        return data.datasets[datasetIndex].data[index];
    };


    const chartRef = useRef(null);

    async function onClick(event) {
        const { current: chart } = chartRef;
        if (props.loading) { // restrict user from interacting when retrieval has not been returned
            return;
        } else {
            props.daydisplaycb(true);
        }

        if (!chart) {
            return;
        }
        //console.log(processElementAtEvent(getElementAtEvent(chart, event)));

        let elementToProcess = getElementAtEvent(chart, event);
        let element = processElementAtEvent(elementToProcess);

        if (element == undefined) {
            props.daydisplaycb(false); // if not a bar or legend then just return
            return;
        }

        if (element != null) {

            if (element.price !== 0) {
                getFlightOnDay(element.dayId, props.depart, props.arrive, props.ticket).then(val => {
                    //console.log(val);
                    if (val.length != 0) {
                        props.daycallback(val);
                        props.daydisplaycb(false)
                        props.modedisplay(2);
                    }
                });
            } else {
                props.daydisplaycb(false);
            }
        }
    };

    let monthChooser = props.data.map(function (val, i) { return i });

    return (

        <div className="chart-container">
            <Bar ref={chartRef} options={options} data={data} onClick={onClick} />
            <MonthChooser monthChooser={monthChooser} data={props.data} test={test} setIndex={setIndex} index={index} min={cheastFlightsVal} route={props.depart + " - " + props.arrive} monthcallback={props.monthcallback} modedisplay={props.modedisplay} loading={props.loading} switchcallback={props.switchcallback} />
            <br></br>
        </div>
    );
}

function hasFlights(array) {
    //console.log(array);
    if (array === undefined) return "data not loaded";
    for (let i = 0; i < array.length; i++) {
        if (array[i].price != '0') return true
    }
    return false;
}

//TO DO ENGLISH MONTH NAME
/* function toMonth(val){
    let months = [""];

} */

async function getFlightOnDay(id, depart, arrive, ticket) {
    const options = await axios({
        method: 'GET',
        url: `http://${port}:8080/flight/order/byDay/s?id=${id}&depart=${depart}&arrive=${arrive}&ticket=${ticket}`,
        contentType: "application/json",
        dataType: "json"
    })

    return options.data;
}

function getMins(val) {
    let min = Infinity;
    let index;
    let result_1 = [];
    let result_2 = [];
    let month = val[0].month
    for (let i = 0; i < val.length; i++) {
        if (val[i].day == 1 && val[i].month != month) {
            index = i;
            break;
        }
        if (val[i].price < min && val[i].price !== 0) {
            result_1 = [];
            result_1.push(i);
            min = val[i].price
        } else if (val[i] === min && val[i].price !== 0) result_1.push(i);
    }

    for (let i = index; i < val.length; i++) {
        if (val[i].price < min && val[i].price !== 0) {
            result_2 = [];
            result_2.push(i);
            min = val[i].price
        } else if (val[i] === min && val[i].price !== 0) result_2.push(i);
    }

    return [...result_1, ...result_2];
}

function getMinsVal(val) {
    let min = Infinity;
    let index;
    let result_1 = [];
    let result_2 = [];
    let month = val[0].month
    for (let i = 0; i < val.length; i++) {
        if (val[i].day == 1 && val[i].month != month) {
            index = i;
            break;
        }
        if (val[i].price < min && val[i].price !== 0) {
            result_1 = [];
            result_1.push(val[i].price);
            min = val[i].price
        } else if (val[i] === min && val[i].price !== 0) result_1.push(min);
    }

    for (let i = index; i < val.length; i++) {
        if (val[i].price < min && val[i].price !== 0) {
            result_2 = [];
            result_2.push(val[i].price);
            min = val[i].price
        } else if (val[i] === min && val[i].price !== 0) result_2.push(min);
    }

    return [...result_1, ...result_2];
}

export default Display;