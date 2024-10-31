import React, { useState, useEffect } from 'react';
import { Grid, Paper, Typography, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio } from '@mui/material';
import CreditService from '../services/credit.service';
import { useNavigate } from 'react-router-dom';

const creditTypes = [
    { id: 1, name: "Primera Vivienda", maxTerm: 30, interestRate: "3.5%-5%" },
    { id: 2, name: "Segunda Vivienda", maxTerm: 20, interestRate: "4%-6%" },
    { id: 3, name: "Tercera Vivienda", maxTerm: 25, interestRate: "5%-7%" },
    { id: 4, name: "Cuarta Vivienda", maxTerm: 15, interestRate: "4.5%-6%" },
];

const CreditRequest = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
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
    const handleCreditTypeChange = async (event) => {
        const creditTypeId = parseInt(event.target.value);
        const selectedCreditType = creditTypes.find(type => type.id === creditTypeId);

        const creditRequest = {
            typeLoan: selectedCreditType.id,
            amount: 100000, // Puedes personalizar este valor
            interestRate: Math.max(...selectedCreditType.interestRate.split('%')[0].split('-').map(parseFloat)),
            dueDate: selectedCreditType.maxTerm,
            idClient: idClient, // Asignar el ID del usuario autenticado
        };

        try {
            setLoading(true);
            setError(null);
            // Guardar el crédito en el backend
            const saveResponse = await CreditService.create(creditRequest);
            const savedCreditId = saveResponse.data.id;
            const savedCreditIduser = saveResponse.data.idClient;
            console.log('Crédito guardado con ID:', savedCreditId);
            console.log('Crédito guardado con ID usre:', savedCreditIduser);

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
