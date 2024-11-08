import React, { useState, useEffect } from 'react';
import { Grid, Paper, Typography, Button, Box, Input } from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import DocumentService from '../services/document.service';
import CreditService from '../services/credit.service'; // Importa el servicio de crédito

const DocumentUpload = () => {
    const { creditId } = useParams();
    const navigate = useNavigate();
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [requiredDocuments, setRequiredDocuments] = useState([]); // Almacena los documentos requeridos

    // Obtener los documentos requeridos según el tipo de crédito
    const fetchCreditTypeDocuments = async () => {
        try {
            const creditResponse = await CreditService.get(creditId); // Obtiene el crédito por ID
            const creditType = creditResponse.data.typeLoan; // Asumiendo que 'type' es el tipo de crédito

            // Dependiendo del tipo de crédito, se definen los documentos requeridos
            switch (creditType) {
                case 1:
                    setRequiredDocuments([
                        "Comprobante de ingresos", 
                        "Certificado de avalúo", 
                        "Historial crediticio"
                    ]);
                    break;
                case 2:
                    setRequiredDocuments([
                        "Comprobante de ingresos", 
                        "Certificado de avalúo", 
                        "Escritura de la primera vivienda",
                        "Historial crediticio"
                    ]);
                    break;
                case 3:
                    setRequiredDocuments([
                        "Estado financiero del negocio",
                        "Comprobante de ingresos", 
                        "Certificado de avalúo", 
                        "Plan de negocios"
                    ]);
                    break;
                case 4:
                    setRequiredDocuments([
                        "Comprobante de ingresos", 
                        "Presupuesto de la remodelación", 
                        "Certificado de avalúo actualizado"
                    ]);
                    break;
                default:
                    setRequiredDocuments([]); // Si no se encuentra el tipo de crédito
            }
        } catch (err) {
            setError("Error al obtener el crédito: " + err.message);
            console.error('Error al obtener el crédito:', err);
        }
    };

    useEffect(() => {
        fetchCreditTypeDocuments(); // Al montar el componente, obtenemos los documentos requeridos
    }, [creditId]); // Re-ejecutamos cuando el creditId cambia

    const handleFileChange = (event, index) => {
        const newDocuments = [...documents];
        newDocuments[index] = event.target.files[0];
        setDocuments(newDocuments);
    };

    const handleSubmit = async () => {
        if (documents.length !== requiredDocuments.length) {
            setError(`Debe subir todos los documentos requeridos (${requiredDocuments.length} documentos).`);
            return;
        }

        try {
            setLoading(true);
            setError(null);

            // Llamar al método del servicio que maneja múltiples archivos
            const response = await DocumentService.uploadDocuments(documents, creditId);
            console.log('Respuesta al subir documentos:', response.data);
            alert("Documentos subidos correctamente.");

            // Redirigir a la página de inicio después de subir documentos
            navigate('/home');

        } catch (err) {
            setError("Error al subir documentos: " + err.message);
            console.error('Error en la subida de documentos:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Grid container justifyContent="center" alignItems="center" style={{ minHeight: '100vh' }}>
            <Grid item xs={12} md={8}>
                <Paper elevation={3} style={{ padding: '2rem' }}>
                    <Typography variant="h5" align="center" gutterBottom>
                        Subir Documentos
                    </Typography>

                    {requiredDocuments.length === 0 ? (
                        <Typography color="error" align="center">
                            No se pudieron cargar los documentos requeridos.
                        </Typography>
                    ) : (
                        requiredDocuments.map((doc, index) => (
                            <Box key={index} mt={2}>
                                <Typography>{doc}:</Typography>
                                <Input
                                    type="file"
                                    onChange={(event) => handleFileChange(event, index)}
                                    fullWidth
                                    required
                                />
                            </Box>
                        ))
                    )}

                    {error && (
                        <Typography color="error" mt={2}>
                            {error}
                        </Typography>
                    )}

                    <Box mt={4} textAlign="center">
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleSubmit}
                            disabled={loading}
                        >
                            {loading ? 'Subiendo...' : 'Subir Documentos'}
                        </Button>
                    </Box>
                </Paper>
            </Grid>
        </Grid>
    );
};

export default DocumentUpload;
