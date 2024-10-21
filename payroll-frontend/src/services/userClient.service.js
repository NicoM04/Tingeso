import httpClient from "../http-common";

// Obtener todos los clientes
const getAll = () => {
    return httpClient.get('/api/user/');
};

// Obtener un cliente por ID
const get = id => {
    return httpClient.get(`/api/user/${id}`);
};

// Crear un nuevo cliente
const create = data => {
    return httpClient.post("/api/user/", data);
};

// Actualizar un cliente existente
const update = data => {
    return httpClient.put('/api/user/', data);
};

// Eliminar un cliente por ID
const remove = id => {
    return httpClient.delete(`/api/user/${id}`);
};

export default {
    getAll,
    get,
    create,
    update,
    remove
};
