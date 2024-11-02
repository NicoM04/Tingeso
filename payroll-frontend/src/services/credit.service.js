import httpClient from "../http-common";

// Obtener todos los créditos
const getAll = () => {
    return httpClient.get('/api/credit/');
};

// Obtener un crédito por ID
const get = id => {
    return httpClient.get(`/api/credit/${id}`);
};

// Crear un nuevo crédito
const create = data => {
    return httpClient.post("/api/credit/", data);
};

// Actualizar un crédito existente
const update = data => {
    return httpClient.put('/api/credit/', data);
};


const updateCreditStatus = (creditId, newStatus) => {
    return httpClient.patch(`/api/credit/${creditId}/status?newStatus=${newStatus}`);
};

// Eliminar un crédito por ID
const remove = id => {
    return httpClient.delete(`/api/credit/${id}`);
};

// Simular un crédito
const simulateCredit = data => {
    return httpClient.post("/api/credit/simulate", data);
};


// Verificar la relación cuota/ingreso
const checkIncomeToPaymentRatio = (data, monthlyIncome) => {
    return httpClient.post(`/api/credit/check-income-to-payment-ratio?monthlyIncome=${monthlyIncome}`, data);
};

const checkCreditHistory = (hasGoodCreditHistory) => {
    return httpClient.post(`/api/credit/check-credit-history?hasGoodCreditHistory=${hasGoodCreditHistory}`);
};



// Verificar estabilidad laboral (R3)
const checkEmploymentStability = (employmentYears, isSelfEmployed, incomeYears) => {
    return httpClient.post(`/api/credit/check-employment-stability?employmentYears=${employmentYears}&isSelfEmployed=${isSelfEmployed}&incomeYears=${incomeYears}`);
};


// Verificar relación deuda/ingreso
const checkDebtToIncomeRatio = (data, totalDebt, monthlyIncome) => {
    return httpClient.post(`/api/credit/check-debt-to-income-ratio?totalDebt=${totalDebt}&monthlyIncome=${monthlyIncome}`, data);
};

// Verificar monto máximo financiable
const checkMaximumLoanAmount = (data, propertyValue) => {
    return httpClient.post(`/api/credit/check-maximum-loan-amount?propertyValue=${propertyValue}`, data);
};


// Verificar si el préstamo puede finalizar antes de los 75 años
const checkApplicantAge = (applicantAge, credit) => {
    return httpClient.post(`/api/credit/check-applicant-age?applicantAge=${applicantAge}`, credit);
};

// Verificar la capacidad de ahorro (R7)
const checkSavingsCapacity = (hasMinimumBalance, hasConsistentSavings, hasRegularDeposits, meetsBalanceYears, hasNoRecentWithdrawals) => {
    return httpClient.post(`/api/credit/evaluate-savings-capacity?hasMinimumBalance=${hasMinimumBalance}&hasConsistentSavings=${hasConsistentSavings}&hasRegularDeposits=${hasRegularDeposits}&meetsBalanceYears=${meetsBalanceYears}&hasNoRecentWithdrawals=${hasNoRecentWithdrawals}`);
};

// Obtener el usuario por ID del crédito
const getUserByCreditId = (creditId) => {
    return httpClient.get(`/api/credit/${creditId}/user`);
};
// Obtener todos los créditos de un cliente por ID
const getCreditsByClientId = (clientId) => {
    return httpClient.get(`/api/credit/client/${clientId}`);
};


export default {
    getAll,
    get,
    create,
    update,
    remove,
    simulateCredit,
    checkIncomeToPaymentRatio,
    checkCreditHistory,
    checkEmploymentStability,
    checkDebtToIncomeRatio,
    checkMaximumLoanAmount,
    checkApplicantAge,
    checkSavingsCapacity,
    getUserByCreditId,
    updateCreditStatus,
    getCreditsByClientId
};
