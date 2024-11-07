import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button
} from '@mui/material';
import CreditService from '../services/credit.service';

const UserCreditTable = () => {
  const [credits, setCredits] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCredits = async () => {
      try {
        const response = await CreditService.getUserCredits();
        setCredits(response.data);
      } catch (error) {
        console.error('Error fetching user credits:', error);
      }
    };

    fetchCredits();
  }, []);

  // Función para manejar la navegación al detalle de crédito
  const handleViewDetails = (creditId) => {
    navigate(`/credit-details/${creditId}`);
  };

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Crédito ID</TableCell>
            <TableCell>Monto</TableCell>
            <TableCell>Estado</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {credits.map((credit) => (
            <TableRow key={credit.id}>
              <TableCell>{credit.id}</TableCell>
              <TableCell>${credit.amount}</TableCell>
              <TableCell>{credit.state}</TableCell>
              <TableCell>
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => handleViewDetails(credit.id)}
                >
                  Ver Detalles
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default UserCreditTable;
