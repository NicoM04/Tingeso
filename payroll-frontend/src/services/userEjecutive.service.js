import httpClient from "../http-common";

// Obtener todos los ejecutivos
const getAll = () => {
    return httpClient.get('/api/userEjecutive/');
};

// Obtener un ejecutivo por ID
const get = id => {
    return httpClient.get(`/api/userEjecutive/${id}`);
};

// Crear un nuevo ejecutivo
const create = data => {
    return httpClient.post("/api/userEjecutive/", data);
};

// Actualizar un ejecutivo existente
const update = data => {
    return httpClient.put('/api/userEjecutive/', data);
};

// Eliminar un ejecutivo por ID
const remove = id => {
    return httpClient.delete(`/api/userEjecutive/${id}`);
};

export default {
    getAll,
    get,
    create,
    update,
    remove
};
