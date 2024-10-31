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

// Eliminar un crédito por ID
const remove = id => {
    return httpClient.delete(`/api/credit/${id}`);
};

// Obtener el monto de un crédito por ID
const getAmountById = id => {
    return httpClient.get(`/api/credit/amount/${id}`);
};

// Simular un crédito
const simulateCredit = data => {
    return httpClient.post("/api/credit/simulate", data);
};

// Crear un crédito con documentos
const createCreditWithDocuments = (data, files) => {
    const formData = new FormData();
    formData.append("credit", new Blob([JSON.stringify(data)], { type: 'application/json' }));
    Array.from(files).forEach((file, i) => {
        formData.append("files", file);
    });
    return httpClient.post("/api/credit/create", formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};

// Verificar la relación cuota/ingreso
const checkIncomeToPaymentRatio = (data, monthlyIncome) => {
    return httpClient.post(`/api/credit/check-income-to-payment-ratio?monthlyIncome=${monthlyIncome}`, data);
};

// Verificar historial crediticio
const checkCreditHistory = hasGoodCreditHistory => {
    return httpClient.get(`/api/credit/check-credit-history?hasGoodCreditHistory=${hasGoodCreditHistory}`);
};

// Verificar estabilidad laboral
const checkEmploymentStability = (employmentYears, isSelfEmployed, incomeYears) => {
    return axios.post(`/api/credit/check-employment-stability`, null, {
      params: {
        employmentYears: employmentYears,
        isSelfEmployed: isSelfEmployed,
        incomeYears: incomeYears,
      },
    });
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
const checkApplicantAge = (applicantAge, loanTerm) => {
    return httpClient.get(`/api/credit/check-applicant-age?applicantAge=${applicantAge}&loanTerm=${loanTerm}`);
};

export default {
    getAll,
    get,
    create,
    update,
    remove,
    getAmountById,
    simulateCredit,
    createCreditWithDocuments,
    checkIncomeToPaymentRatio,
    checkCreditHistory,
    checkEmploymentStability,
    checkDebtToIncomeRatio,
    checkMaximumLoanAmount,
    checkApplicantAge
};
