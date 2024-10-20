import httpClient from "../http-common";

// Subir un archivo
const uploadDocument = (file, creditId) => {
    const formData = new FormData();
    formData.append("file", file);
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

export default {
    uploadDocument,
    downloadDocument,
    getAllDocuments
};
