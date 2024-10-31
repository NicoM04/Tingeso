import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import CreditService from '../services/credit.service';
import DocumentService from '../services/document.service';
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
  Checkbox,
  TextField,
  Button,
} from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';

const CreditRequestDetail = () => {
  const { creditId } = useParams();
  const [credit, setCredit] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [rules, setRules] = useState([]); // Nuevo estado para las reglas
  const [ruleInputs, setRuleInputs] = useState({}); // Para almacenar entradas de usuario

  useEffect(() => {
    const fetchCreditDetails = async () => {
      try {
        const creditResponse = await CreditService.get(creditId);
        setCredit(creditResponse.data);

        const documentResponse = await DocumentService.getDocumentsByCreditId(creditId);
        setDocuments(documentResponse.data); 

        // Simulando la carga de reglas. Aquí deberías implementar la lógica real para obtener las reglas asociadas al crédito.
        setRules([
          { id: 1, description: "R1: Relación cuota/ingreso", requiresInput: true },
          { id: 2, description: "R2: Buen historial crediticio", requiresInput: false },
          { id: 3, description: "R3: Estabilidad laboral", requiresInput: true },
          { id: 4, description: "R4: Relación deuda/ingreso", requiresInput: true },
          { id: 5, description: "R5: Monto máximo financiable", requiresInput: true },
          { id: 6, description: "R6: Edad del solicitante", requiresInput: true },
        ]);
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
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `document_${documentId}`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      
      window.URL.revokeObjectURL(url);
      
      alert('Documento descargado exitosamente.');
    } catch (error) {
      console.error('Error al descargar el documento:', error);
      alert('Hubo un error al descargar el documento.');
    }
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setRuleInputs({
      ...ruleInputs,
      [name]: value,
    });
  };

  const checkRule = async (rule) => {
    let result;
    switch (rule.id) {
      case 1:
        // Relación cuota/ingreso
        result = await CreditService.checkIncomeToPaymentRatio(credit, ruleInputs.monthlyIncome);
        alert(`R1 cumplida: ${result.data}`);
        break;
      case 2:
        // Buen historial crediticio
        result = await CreditService.checkCreditHistory(ruleInputs.hasGoodCreditHistory);
        alert(`R2 cumplida: ${result.data}`);
        break;
      case 3:
        // Estabilidad laboral
        result = await CreditService.checkEmploymentStability(
          ruleInputs.employmentYears,
          ruleInputs.isSelfEmployed,
          ruleInputs.incomeYears
        );
        alert(`R3 cumplida: ${result.data}`);
        break;
      case 4:
        // Relación deuda/ingreso
        result = await CreditService.checkDebtToIncomeRatio(credit, ruleInputs.totalDebt, ruleInputs.monthlyIncome);
        alert(`R4 cumplida: ${result.data}`);
        break;
      case 5:
        // Monto máximo financiable
        result = await CreditService.checkMaximumLoanAmount(credit, ruleInputs.propertyValue);
        alert(`R5 cumplida: ${result.data}`);
        break;
      case 6:
        // Edad del solicitante
        result = await CreditService.checkApplicantAge(ruleInputs.applicantAge, credit.term);
        alert(`R6 cumplida: ${result.data}`);
        break;
      default:
        break;
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
                    <ListItemText primary={doc.fileName} />
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

          {/* Nuevo Bloque de Reglas */}
          <Paper elevation={3} style={{ padding: '20px', borderRadius: '8px', marginTop: '20px' }}>
            <Typography variant="h6" gutterBottom>
              Reglas del Crédito
            </Typography>
            <Divider />
            {rules.length > 0 ? (
              <List>
                {rules.map((rule) => (
                  <ListItem key={rule.id}>
                    <Checkbox
                      onChange={(event) => {
                        if (event.target.checked) {
                          checkRule(rule);
                        }
                      }}
                    />
                    <ListItemText primary={rule.description} />
                    {rule.requiresInput && (
                      <div style={{ marginLeft: '20px' }}>
                        <TextField
                          name="monthlyIncome"
                          label="Ingresos Mensuales"
                          type="number"
                          onChange={handleInputChange}
                          style={{ marginRight: '10px' }}
                        />
                        <Button variant="contained" onClick={() => checkRule(rule)}>
                          Verificar
                        </Button>
                      </div>
                    )}
                  </ListItem>
                ))}
              </List>
            ) : (
              <Typography variant="body1">No hay reglas definidas para esta solicitud de crédito.</Typography>
            )}
          </Paper>
        </>
      ) : (
        <Typography variant="body1">Cargando información del crédito...</Typography>
      )}
    </div>
  );
};

export default CreditRequestDetail;
