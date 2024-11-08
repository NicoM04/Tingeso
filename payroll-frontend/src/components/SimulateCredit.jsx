import React, { useState } from 'react';
import CreditService from '../services/credit.service';
import { 
    TextField, 
    Button, 
    Grid, 
    Paper, 
    Typography, 
    Box, 
    FormHelperText 
} from '@mui/material';
import axios from 'axios';

const SimulateCredit = () => {
    const [creditData, setCreditData] = useState({
        amount: '',
        interestRate: '',
        dueDate: ''
    });
    const [error, setError] = useState({});
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);

    // Manejar el cambio de valores en los inputs
    const handleChange = (e) => {
        setCreditData({
            ...creditData,
            [e.target.name]: e.target.value
        });
    };

    // Validar los campos antes de enviar
    const validate = () => {
        let tempErrors = {};
        if (!creditData.amount || creditData.amount <= 0) {
            tempErrors.amount = "Amount must be greater than 0";
        }
        if (!creditData.interestRate || creditData.interestRate <= 0) {
            tempErrors.interestRate = "Interest rate must be greater than 0";
        }
        if (!creditData.dueDate || creditData.dueDate <= 0) {
            tempErrors.dueDate = "Loan term must be greater than 0";
        }
        setError(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

// Manejar el envío del formulario
const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) {
        return;
    }
    setLoading(true);
    setResult(null);

    try {
        // Logs para depurar los valores de entrada
        console.log("Simulating credit with the following data:");
        console.log("Amount:", creditData.amount);
        console.log("Interest Rate:", creditData.interestRate);
        console.log("Due Date:", creditData.dueDate);

        const response = await CreditService.simulateCredit(creditData);
        setResult(response.data);
    } catch (error) {
        console.error("Error in simulateCredit:", error);
        setResult({ error: "Failed to simulate the credit" });
    } finally {
        setLoading(false);
    }
};


    return (
        <Grid container justifyContent="center" alignItems="center" style={{ minHeight: '100vh' }}>
            <Grid item xs={10} md={6} lg={4}>
                <Paper elevation={3} style={{ padding: '2rem' }}>
                    <Typography variant="h5" align="center" gutterBottom>
                        Credit Simulation
                    </Typography>
                    <form onSubmit={handleSubmit}>
                        <TextField
                            label="Monto del Prestamo"
                            name="amount"
                            type="number"
                            fullWidth
                            margin="normal"
                            variant="outlined"
                            value={creditData.amount}
                            onChange={handleChange}
                            error={!!error.amount}
                            helperText={error.amount}
                        />
                        <TextField
                            label="Tasa de interes anual(%)"
                            name="interestRate"
                            type="number"
                            fullWidth
                            margin="normal"
                            variant="outlined"
                            value={creditData.interestRate}
                            onChange={handleChange}
                            error={!!error.interestRate}
                            helperText={error.interestRate}
                        />
                        <TextField
                            label="Plazo (años)"
                            name="dueDate"
                            type="number"
                            fullWidth
                            margin="normal"
                            variant="outlined"
                            value={creditData.dueDate}
                            onChange={handleChange}
                            error={!!error.dueDate}
                            helperText={error.dueDate}
                        />
                        <Box textAlign="center" mt={2}>
                            <Button 
                                type="submit" 
                                variant="contained" 
                                color="primary" 
                                disabled={loading}
                            >
                                {loading ? 'Simulating...' : 'Simulate Credit'}
                            </Button>
                        </Box>
                    </form>

                    {result && (
                        <Box mt={4}>
                            {result.error ? (
                                <Typography color="error" align="center">
                                    {result.error}
                                </Typography>
                            ) : (
                                <Box>
                                    <Typography variant="h6" align="center">
                                        Resultado de la simulacion
                                    </Typography>
                                    <Typography align="center">
                                        Pago mensual: ${Math.round(result.monthlyPayment)}
                                    </Typography>
                                </Box>
                            )}
                        </Box>
                    )}
                </Paper>
            </Grid>
        </Grid>
    );
};

export default SimulateCredit;
