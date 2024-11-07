import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CreditService from '../services/credit.service';
import {
  Paper,
  Typography,
  Grid,
  Divider
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
                <strong>Costo Total:</strong> ${credit.totalCost}
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
        </Paper>
      ) : (
        <Typography variant="body1">Cargando información del crédito...</Typography>
      )}
    </div>
  );
};

export default UserCreditDetails;