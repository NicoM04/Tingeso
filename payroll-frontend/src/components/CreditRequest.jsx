import React, { useState, useEffect } from 'react';
import { Grid, Paper, Typography, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio, TextField, Button } from '@mui/material';
import CreditService from '../services/credit.service';
import { useNavigate } from 'react-router-dom';

const creditTypes = [
    { id: 1, name: "Primera Vivienda", maxTerm: 30, interestRate: "3.5%-5%" },
    { id: 2, name: "2do Vivienda", maxTerm: 20, interestRate: "4%-6%" },
    { id: 3, name: "Tercera Vivienda", maxTerm: 25, interestRate: "5%-7%" },
    { id: 4, name: "Cuarta Vivienda", maxTerm: 15, interestRate: "4.5%-6%" },
];

const CreditRequest = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [selectedCreditType, setSelectedCreditType] = useState(null);
    const [amount, setAmount] = useState(0); // Nuevo estado para el monto
    const [dueDate, setDueDate] = useState(0); // Nuevo estado para el plazo
    const navigate = useNavigate();
    const [idClient, setUserId] = useState(null);

    // Obtener el ID del usuario desde localStorage o un contexto de autenticación
    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.id) {
            setUserId(user.id);
        } else {
            // Redirigir o mostrar error si no hay usuario autenticado
            setError("Debe iniciar sesión para solicitar un crédito.");
        }
    }, []);

    // Manejar la selección del tipo de crédito
    const handleCreditTypeChange = (event) => {
        const creditTypeId = parseInt(event.target.value);
        const selectedType = creditTypes.find(type => type.id === creditTypeId);
        setSelectedCreditType(selectedType); // Establecer el tipo de crédito seleccionado
        setAmount(0); // Reiniciar monto
        setDueDate(0); // Reiniciar plazo
    };

    // Manejar el envío del formulario
    const handleSubmit = async (event) => {
        event.preventDefault();
        const creditRequest = {
            typeLoan: selectedCreditType.id,
            amount: amount, // Monto proporcionado por el usuario
            interestRate: Math.max(...selectedCreditType.interestRate.split('%')[0].split('-').map(parseFloat)),
            dueDate: dueDate, // Plazo proporcionado por el usuario
            idClient: idClient, // Asignar el ID del usuario autenticado
            state: 1
        };

        try {
            setLoading(true);
            setError(null);
            // Guardar el crédito en el backend
            const saveResponse = await CreditService.create(creditRequest);
            const savedCreditId = saveResponse.data.id;
            console.log('Crédito guardado con ID:', savedCreditId);

            // Redirigir a la página de carga de documentos con el ID del crédito
            navigate(`/upload-documents/${savedCreditId}`);
        } catch (err) {
            setError("Error al crear la solicitud de crédito: " + err.message);
            console.error('Error en la solicitud de crédito:', err);
        } finally {
            setLoading(false);
        }
    };

    if (!idClient) {
        return (
            <Typography color="error" align="center" mt={2}>
                {error}
            </Typography>
        );
    }

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
                        <>
                            <TextField
                                fullWidth
                                margin="normal"
                                label="Monto del Préstamo"
                                type="number"
                                value={amount}
                                onChange={(e) => setAmount(e.target.value)}
                                required
                            />
                            <TextField
                                fullWidth
                                margin="normal"
                                label="Plazo (en años)"
                                type="number"
                                value={dueDate}
                                onChange={(e) => setDueDate(e.target.value)}
                                required
                            />
                            <Button variant="contained" onClick={handleSubmit} fullWidth>
                                Solicitar Crédito
                            </Button>
                        </>
                    )}

                    {error && (
                        <Typography color="error" mt={2}>
                            {error}
                        </Typography>
                    )}

                    {loading && (
                        <Typography mt={2}>
                            Creando solicitud de crédito...
                        </Typography>
                    )}
                </Paper>
            </Grid>
        </Grid>
    );
};

export default CreditRequest;
