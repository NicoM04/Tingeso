import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CreditService from '../services/credit.service';
import {
  Paper,
  Typography,
  Grid,
  Divider,
  Button,
  Box
} from '@mui/material';

const CreditDetails = () => {
  const { creditId } = useParams();
  const [credit, setCredit] = useState(null);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchCreditDetails = async () => {
      try {
        const creditResponse = await CreditService.get(creditId);
        setCredit(creditResponse.data);

        const userResponse = await CreditService.getUserByCreditId(creditId);
        setUser(userResponse.data);
      } catch (error) {
        console.error('Error fetching credit details:', error);
      }
    };

    if (creditId) {
      fetchCreditDetails();
    }
  }, [creditId]);

  // Función para cancelar el crédito
  const handleCancelCredit = async () => {
    try {
      await CreditService.updateCreditStatus(creditId, 8); // 8 representa "Cancelada por el Cliente"
      setCredit({ ...credit, state: 8 }); // Actualiza el estado localmente
    } catch (error) {
      console.error('Error canceling credit:', error);
    }
  };

  // Función para aprobar el crédito
  const handleApproveCredit = async () => {
    try {
      await CreditService.updateCreditStatus(creditId, 5); // 5 representa "En Aprobación Final"
      setCredit({ ...credit, state: 5 }); // Actualiza el estado localmente
    } catch (error) {
      console.error('Error approving credit:', error);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <Typography variant="h4" gutterBottom>
        Detalles del Crédito
      </Typography>
      {credit ? (
        <Paper elevation={3} style={{ padding: '20px', borderRadius: '8px' }}>
          <Typography variant="h6" gutterBottom>
            Información del Crédito
          </Typography>
          <Divider />
          <Grid container spacing={2} style={{ marginTop: '10px' }}>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Nombre del Cliente:</strong> {user ? user.name : 'Cargando...'}
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Tipo de Crédito:</strong> {credit.typeLoan ? credit.typeLoan : 'Cargando...'}
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Estado:</strong> {credit.state}
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Monto Solicitado:</strong> ${credit.amount}
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Costo Total:</strong> ${Number(credit.totalCost).toFixed(0)}
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Fecha de Solicitud:</strong> {new Date(credit.requestDate).toLocaleDateString()}
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Plazo:</strong> {credit.dueDate ? credit.dueDate : 'Cargando...'} años
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle1">
                <strong>Tasa de Interés:</strong> {credit.interestRate}%
              </Typography>
            </Grid>
          </Grid>
          <Box display="flex" justifyContent="space-between" mt={3}>
            {/* Botón para cancelar crédito */}
            <Button 
              variant="contained" 
              color="secondary" 
              onClick={handleCancelCredit}
            >
              Cancelar Crédito
            </Button>
            {/* Botón para aprobar crédito solo si el estado es "Pre-Aprobada" */}
            {credit.state === 4 && (
              <Button 
                variant="contained" 
                color="primary" 
                onClick={handleApproveCredit}
              >
                Aprobar Crédito
              </Button>
            )}
          </Box>
        </Paper>
      ) : (
        <Typography variant="body1">Cargando información del crédito...</Typography>
      )}
    </div>
  );
};

export default CreditDetails;
