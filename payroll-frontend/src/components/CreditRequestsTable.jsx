// CreditRequestsTable.jsx
import React, { useEffect, useState } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import CreditService from '../services/credit.service';

const CreditRequestsTable = () => {
  const [credits, setCredits] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCredits = async () => {
      try {
        const response = await CreditService.getAll(); // Usa el método getAll del servicio
        setCredits(response.data);
      } catch (error) {
        console.error('Error al obtener solicitudes de crédito:', error);
      }
    };

    fetchCredits();
  }, []);

  const handleViewDetails = (creditId) => {
    navigate(`/creditRequestDetail/${creditId}`); // Redirige al detalle de la solicitud
    // falla pq no tiene relacionado la url con q reciba el id del credito
  };

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID de Cliente</TableCell>
            <TableCell>Tipo de Crédito</TableCell>
            <TableCell>Estado</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {credits.map((credit) => (
            <TableRow key={credit.id}>
              <TableCell>{credit.idClient}</TableCell>
              <TableCell>{credit.typeLoan}</TableCell>
              <TableCell>{credit.state}</TableCell>
              <TableCell>
                <Button onClick={() => handleViewDetails(credit.id)} variant="contained" color="primary">
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

export default CreditRequestsTable;
