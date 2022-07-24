function setGlobalRoute(depart, arrive, fn1, fn2) {
    fn1(depart);
    fn2(arrive);
}

function toLocaleDate(date) {
    let options = { month: '2-digit', day: '2-digit', year: 'numeric' };

    let timeStamp = Date.parse(date);
    let display = new Date(timeStamp);
    return new Intl.DateTimeFormat('vi', options).format(display);
}


function getTime(val) {
    let timeStamp = Date.parse(val);
    let date = new Date(timeStamp);

    let hours = date.getHours();
    let minutes = date.getMinutes();

    if (hours < 10) hours = "0" + hours;
    if (minutes < 10) minutes = "0" + minutes;

    return hours + ":" + minutes;

}


function sortByPrice(data) {
    return [...data].sort(function compareFn(a, b) {
        return a.price - b.price
    })
}

function sortByDate(data) {
    return [...data].sort(function compareFn(a, b) {
        let timeStamp = a.outFlightTime;
        let date_a = new Date(timeStamp);
        timeStamp = b.outFlightTime;
        let date_b = new Date(timeStamp);
        return date_a.getTime() - date_b.getTime();
    })
}

export { setGlobalRoute, toLocaleDate, getTime, sortByDate, sortByPrice };