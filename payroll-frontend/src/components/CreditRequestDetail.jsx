import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CreditService from '../services/credit.service';
import DocumentService from '../services/document.service'; // Asegúrate de importar tu servicio de documentos
import {
  Paper,
  Typography,
  Grid,
  Divider,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
} from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download'; // Importa el icono para descargar

const CreditRequestDetail = () => {
  const { creditId } = useParams();
  const [credit, setCredit] = useState(null);
  const [documents, setDocuments] = useState([]);

  useEffect(() => {
    const fetchCreditDetails = async () => {
      try {
        const creditResponse = await CreditService.get(creditId);
        setCredit(creditResponse.data);
        // Asumiendo que tienes un endpoint para obtener documentos por ID de crédito
        const documentResponse = await DocumentService.getDocumentsByCreditId(creditId);
        setDocuments(documentResponse.data); 
      } catch (error) {
        console.error('Error al obtener los detalles del crédito:', error);
      }
    };

    if (creditId) {
      fetchCreditDetails();
    }
  }, [creditId]);

  const handleDownload = async (documentId) => {
    try {
        const response = await DocumentService.downloadDocument(documentId);
        
        // Crear un enlace para descargar el archivo
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `document_${documentId}`); // Puedes personalizar el nombre del archivo
        document.body.appendChild(link);
        link.click();
        link.remove();
        
        // Opcional: liberar el objeto URL
        window.URL.revokeObjectURL(url);
        
        alert('Documento descargado exitosamente.');
    } catch (error) {
        console.error('Error al descargar el documento:', error);
        alert('Hubo un error al descargar el documento.');
    }
};


  return (
    <div style={{ padding: '20px' }}>
      <Typography variant="h4" gutterBottom>
        Detalle de la Solicitud de Crédito
      </Typography>
      {credit ? (
        <>
          <Paper elevation={3} style={{ padding: '20px', borderRadius: '8px' }}>
            <Typography variant="h6" gutterBottom>
              Información del Crédito
            </Typography>
            <Divider />
            <Grid container spacing={2} style={{ marginTop: '10px' }}>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1"><strong>ID del Cliente:</strong> {credit.clientId}</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1"><strong>Tipo de Crédito:</strong> {credit.type}</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1"><strong>Estado:</strong> {credit.status}</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1"><strong>Monto Solicitado:</strong> ${credit.amount}</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1"><strong>Fecha de Solicitud:</strong> {new Date(credit.requestDate).toLocaleDateString()}</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1"><strong>Plazo:</strong> {credit.term} meses</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1"><strong>Tasa de Interés:</strong> {credit.interestRate}%</Typography>
              </Grid>
            </Grid>
          </Paper>

          {/* Bloque de Documentos Asociados */}
          <Paper elevation={3} style={{ padding: '20px', borderRadius: '8px', marginTop: '20px' }}>
            <Typography variant="h6" gutterBottom>
              Documentos Asociados
            </Typography>
            <Divider />
            {documents.length > 0 ? (
              <List>
                {documents.map((doc) => (
                  <ListItem key={doc.id}>
                    <ListItemText primary={doc.name} />
                    <ListItemSecondaryAction>
                      <IconButton edge="end" aria-label="download" onClick={() => handleDownload(doc.id)}>
                        <DownloadIcon />
                      </IconButton>
                    </ListItemSecondaryAction>
                  </ListItem>
                ))}
              </List>
            ) : (
              <Typography variant="body1">No hay documentos asociados a esta solicitud de crédito.</Typography>
            )}
          </Paper>
        </>
      ) : (
        <Typography variant="body1">Cargando detalles del crédito...</Typography>
      )}
    </div>
  );
};

export default CreditRequestDetail;
