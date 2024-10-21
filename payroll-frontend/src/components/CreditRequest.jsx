import React, { useState } from 'react';
import { 
    Grid, 
    Paper, 
    Typography, 
    Button, 
    Box, 
    FormControl, 
    FormLabel, 
    RadioGroup, 
    FormControlLabel, 
    Radio, 
    Input 
} from '@mui/material';
import CreditService from '../services/credit.service'; // Asegúrate de que la ruta sea correcta
import DocumentService from '../services/document.service'; // Asegúrate de que la ruta sea correcta

const creditTypes = [
    { id: 1, name: "Primera Vivienda", maxTerm: 30, interestRate: "3.5%-5%", requiredDocuments: ["Comprobante de ingresos", "Certificado de avalúo", "Historial crediticio"] },
    { id: 2, name: "Segunda Vivienda", maxTerm: 20, interestRate: "4%-6%", requiredDocuments: ["Comprobante de ingresos", "Certificado de avalúo", "Escritura de la primera vivienda", "Historial crediticio"] },
    { id: 3, name: "Tercera Vivienda", maxTerm: 25, interestRate: "5%-7%", requiredDocuments: ["Estado financiero del negocio", "Comprobante de ingresos", "Certificado de avalúo", "Plan de negocios"] },
    { id: 4, name: "Cuarta Vivienda", maxTerm: 15, interestRate: "4.5%-6%", requiredDocuments: ["Comprobante de ingresos", "Presupuesto de la remodelación", "Certificado de avalúo actualizado"] },
];

const CreditRequest = () => {
    const [selectedCreditType, setSelectedCreditType] = useState(null);
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Manejar la selección del tipo de crédito
    const handleCreditTypeChange = (event) => {
        const creditTypeId = parseInt(event.target.value);
        setSelectedCreditType(creditTypes.find(type => type.id === creditTypeId));
        setDocuments([]);  // Limpiar documentos anteriores
        setError(null);

        console.log("Tipo de crédito seleccionado:", creditTypeId);
    };

    // Manejar la carga de archivos
    const handleFileChange = (event, index) => {
        const newDocuments = [...documents];
        newDocuments[index] = event.target.files[0];
        setDocuments(newDocuments);

        console.log(`Documento ${index + 1} seleccionado:`, event.target.files[0].name);
    };

    // Manejar el envío del formulario
    const handleSubmit = async () => {
        console.log("Enviando la solicitud de crédito...");

        if (!selectedCreditType || documents.length !== selectedCreditType.requiredDocuments.length) {
            setError(`Debe subir todos los documentos requeridos (${selectedCreditType.requiredDocuments.length} documentos).`);
            console.log("Error: No se subieron todos los documentos requeridos.");
            return;
        }

        const formData = new FormData();
        documents.forEach((doc) => {
            formData.append('file', doc);
        });

        // Logs para ver los datos enviados
        console.log('Tipo de crédito seleccionado:', selectedCreditType);
        console.log('Documentos cargados:', documents);

        try {
            setLoading(true);
            setError(null);

            // Crear el objeto de solicitud de crédito
            const creditRequest = {
                typeLoan: selectedCreditType.id,
                amount: 100000, // Ejemplo de monto, puedes cambiarlo
                interestRate: Math.max(...selectedCreditType.interestRate.split('%')[0].split('-').map(parseFloat)),
                term: selectedCreditType.maxTerm,
            };

            // Logs antes de enviar la solicitud del crédito
            console.log('Solicitud de crédito a guardar:', creditRequest);

            // Hacer una petición POST para guardar el crédito
            const saveResponse = await CreditService.create(creditRequest);
            const savedCreditId = saveResponse.data.id;

            // Log del ID del crédito guardado
            console.log('Crédito guardado con ID:', savedCreditId);

            // Subir los documentos asociados al crédito
            formData.append('creditId', savedCreditId);

            // Logs antes de enviar los documentos
            console.log('Subiendo archivos con creditId:', savedCreditId);

            const documentResponse = await DocumentService.uploadDocument(formData, savedCreditId);

            // Log de respuesta del servidor al subir documentos
            console.log('Respuesta al subir documentos:', documentResponse.data);

            // Finalmente, crear el crédito después de guardar y subir documentos
            const createCreditRequest = {
                ...creditRequest,
                id: savedCreditId,
            };

            // Log antes de crear el crédito final
            console.log('Creando crédito final:', createCreditRequest);

            const createCreditResponse = await CreditService.createCreditWithDocuments(createCreditRequest, documents);

            // Log de la respuesta al crear el crédito
            console.log('Respuesta al crear crédito:', createCreditResponse.data);

            alert("Crédito creado correctamente y documentos subidos.");
        } catch (err) {
            setError("Error al crear la solicitud de crédito: " + err.message);
            console.error('Error en la solicitud de crédito:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Grid container justifyContent="center" alignItems="center" style={{ minHeight: '100vh' }}>
            <Grid item xs={12} md={8}>
                <Paper elevation={3} style={{ padding: '2rem' }}>
                    <Typography variant="h5" align="center" gutterBottom>
                        Solicitar Crédito
                    </Typography>

                    <FormControl component="fieldset" fullWidth margin="normal">
                        <FormLabel component="legend">Selecciona el tipo de crédito</FormLabel>
                        <RadioGroup row onChange={handleCreditTypeChange}>
                            {creditTypes.map((type) => (
                                <FormControlLabel 
                                    key={type.id} 
                                    value={type.id} 
                                    control={<Radio />} 
                                    label={`${type.name} - Plazo máximo: ${type.maxTerm} años, Tasa de interés: ${type.interestRate}`} 
                                />
                            ))}
                        </RadioGroup>
                    </FormControl>

                    {selectedCreditType && (
                        <Box mt={3}>
                            <Typography variant="h6">Documentos requeridos:</Typography>
                            {selectedCreditType.requiredDocuments.map((doc, index) => (
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
                                    {loading ? 'Enviando...' : 'Enviar Solicitud'}
                                </Button>
                            </Box>
                        </Box>
                    )}
                </Paper>
            </Grid>
        </Grid>
    );
};

export default CreditRequest;
