import httpClient from "../http-common";

// Subir múltiples archivos
const uploadDocuments = (files, creditId) => {
    const formData = new FormData();
    files.forEach(file => {
        formData.append("files", file); // 'files' para manejar múltiples archivos
    });
    if (creditId) {
        formData.append("creditId", creditId);
    }
    return httpClient.post("/api/documents/upload", formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
};


// Descargar un archivo por su ID
const downloadDocument = documentId => {
    return httpClient.get(`/api/documents/download/${documentId}`, {
        responseType: "blob", // Para descargar archivos en formato binario
    });
};

// Obtener todos los documentos
const getAllDocuments = () => {
    return httpClient.get("/api/documents/all");
};

const getDocumentsByCreditId = (creditId) => {
    return httpClient.get(`/api/documents/byCredit/${creditId}`);
};

export default {
    uploadDocuments,
    downloadDocument,
    getAllDocuments,
    getDocumentsByCreditId
};
