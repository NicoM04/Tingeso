// UserCreditTable.jsx
import React, { useState, useEffect } from 'react';
import CreditService from '../services/credit.service';
import {
    Container,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
    CircularProgress
} from '@mui/material';

const UserCreditTable = () => {
    const [credits, setCredits] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    // Recupera el usuario del almacenamiento local
    const user = JSON.parse(localStorage.getItem("user"));
    const userId = user ? user.id : null; // Obtén el ID del usuario

    // Mapeo de estados de crédito a texto
    const creditStatusMap = {
        1: 'En Revisión Inicial',
        2: 'Pendiente de Documentación',
        3: 'En Evaluación',
        4: 'Pre-Aprobada',
        5: 'En Aprobación Final',
        6: 'Aprobada',
        7: 'Rechazada',
        8: 'Cancelada por el Cliente',
        9: 'En Desembolso'
    };

    useEffect(() => {
        const fetchUserCredits = async () => {
            try {
                if (!userId) {
                    setError("ID de usuario no válido.");
                    return;
                }
                setLoading(true);
                const response = await CreditService.getCreditsByClientId(userId); // Llama al servicio para obtener créditos del cliente
                setCredits(response.data);
            } catch (err) {
                console.error("Error al obtener los créditos del usuario:", err);
                setError("Error al cargar los créditos.");
            } finally {
                setLoading(false);
            }
        };

        fetchUserCredits();
    }, [userId]); // Dependencia de userId

    return (
        <Container>
            <Typography variant="h4" gutterBottom align="center">Mis Solicitudes de Crédito</Typography>
            {loading ? (
                <CircularProgress />
            ) : error ? (
                <Typography color="error" align="center">{error}</Typography>
            ) : (
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Monto</TableCell>
                                <TableCell>Estado</TableCell>
                                <TableCell>Tipo de Préstamo</TableCell>
                                <TableCell>Fecha de Solicitud</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {credits.length === 0 ? (
                                <TableRow>
                                    <TableCell colSpan={6} align="center">No tienes solicitudes de crédito.</TableCell>
                                </TableRow>
                            ) : (
                                credits.map((credit) => (
                                    <TableRow key={credit.id}>
                                        <TableCell>{credit.id}</TableCell>
                                        <TableCell>{credit.amount}</TableCell>
                                        <TableCell>{creditStatusMap[credit.state] || 'Estado Desconocido'}</TableCell>
                                        <TableCell>{credit.typeLoan}</TableCell>
                                        <TableCell>{new Date(credit.requestDate).toLocaleDateString()}</TableCell>
                                    </TableRow>
                                ))
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </Container>
    );
};

export default UserCreditTable;
