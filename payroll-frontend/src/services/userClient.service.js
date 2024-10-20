import httpClient from "../http-common";

// Obtener todos los clientes
const getAll = () => {
    return httpClient.get('/api/userClient/');
};

// Obtener un cliente por ID
const get = id => {
    return httpClient.get(`/api/userClient/${id}`);
};

// Crear un nuevo cliente
const create = data => {
    return httpClient.post("/api/userClient/", data);
};

// Actualizar un cliente existente
const update = data => {
    return httpClient.put('/api/userClient/', data);
};

// Eliminar un cliente por ID
const remove = id => {
    return httpClient.delete(`/api/userClient/${id}`);
};

export default {
    getAll,
    get,
    create,
    update,
    remove
};
