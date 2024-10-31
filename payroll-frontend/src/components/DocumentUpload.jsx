import React, { useState } from 'react';
import { Grid, Paper, Typography, Button, Box, Input } from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom'; // Importa useNavigate
import DocumentService from '../services/document.service';

const DocumentUpload = () => {
    const { creditId } = useParams();
    const navigate = useNavigate(); // Crea una instancia de navigate
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const requiredDocuments = [
        "Comprobante de ingresos", 
        "Certificado de avalúo", 
        "Historial crediticio"
    ];

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
            navigate('/home'); // Cambia esta línea para redirigir a la página de inicio

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

                    {requiredDocuments.map((doc, index) => (
                        <Box key={index} mt={2}>
                            <Typography>{doc}:</Typography>
                            <Input
                                type="file"
                                onChange={(event) => handleFileChange(event, index)}
                                fullWidth
                                required
                            />
                        </Box>
                    ))}

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
