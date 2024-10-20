import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/v1/paycheck/');
}

const calculate = (year,month) => {
    return httpClient.get("/api/v1/paycheck/calculate",{params:{year,month}});
}

export default { getAll, calculate };