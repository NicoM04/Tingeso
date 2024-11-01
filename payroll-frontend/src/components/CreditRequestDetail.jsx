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
  const [user, setUser] = useState(null);  // Nuevo estado para el usuario
  const [documents, setDocuments] = useState([]);
  const [rules, setRules] = useState([]);
  const [ruleInputs, setRuleInputs] = useState({
    employmentYears: 0,
    isSelfEmployed: false,
    incomeYears: 0,
    totalDebt: 0,
    monthlyIncome: 0,
    applicantAge: 0,
    hasMinimumBalance: false,
    hasConsistentSavings: false,
    hasRegularDeposits: false,
    meetsBalanceYears: false,
    hasNoRecentWithdrawals: false,
  });

  useEffect(() => {
    const fetchCreditDetails = async () => {
      try {
        const creditResponse = await CreditService.get(creditId);
        setCredit(creditResponse.data);

        // Obtener el usuario por ID del crédito
        const userResponse = await CreditService.getUserByCreditId(creditId);
        setUser(userResponse.data); // Guardar el usuario en el estado

        const documentResponse = await DocumentService.getDocumentsByCreditId(creditId);
        setDocuments(documentResponse.data);
        
        setRules([
          { id: 1, description: "R1: Relación cuota/ingreso", requiresInput: true },
          { id: 2, description: "R2: Buen historial crediticio", requiresInput: false },
          { id: 3, description: "R3: Estabilidad laboral", requiresInput: true },
          { id: 4, description: "R4: Relación deuda/ingreso", requiresInput: true },
          { id: 5, description: "R5: Monto máximo financiable", requiresInput: true },
          { id: 6, description: "R6: Edad del solicitante", requiresInput: true },
          { id: 7, description: "R7: Capacidad de Ahorro", requiresInput: false },
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
      const response = await DocumentService.downloadDocument(documentId, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');

      const fileName = response.headers['content-disposition'] 
          ? response.headers['content-disposition'].match(/filename="(.+)"/)[1] 
          : `document_${documentId}.pdf`;

      link.href = url;
      link.setAttribute('download', fileName);
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

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setRuleInputs((prevInputs) => ({
      ...prevInputs,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const checkRule = async (rule) => {
    let result;
    switch (rule.id) {
      case 1:
        result = await CreditService.checkIncomeToPaymentRatio(credit, ruleInputs.monthlyIncome);
        alert(`R1 cumplida: ${result.data}`);
        break;

      case 2:
        result = await CreditService.checkCreditHistory(true);
        alert(`R2 cumplida: ${result.data}`);
        break;

      case 3:
        const incomeYears = ruleInputs.isSelfEmployed ? ruleInputs.incomeYears : 0;
        result = await CreditService.checkEmploymentStability(
          ruleInputs.employmentYears,
          ruleInputs.isSelfEmployed,
          incomeYears
        );
        alert(`R3 cumplida: ${result.data}`);
        break;

      case 4:
        result = await CreditService.checkDebtToIncomeRatio(
          credit,
          ruleInputs.totalDebt,
          ruleInputs.monthlyIncome
        );
        alert(`R4 cumplida: ${result.data}`);
        break;

      case 5:
        result = await CreditService.checkMaximumLoanAmount(credit, ruleInputs.propertyValue);
        alert(`R5 cumplida: ${result.data}`);
        break;

      case 6:
        result = await CreditService.checkApplicantAge(ruleInputs.applicantAge, credit);
        alert(`R6 cumplida: ${result.data}`);
        break;

      case 7:
        result = await CreditService.checkSavingsCapacity(
          ruleInputs.hasMinimumBalance,
          ruleInputs.hasConsistentSavings,
          ruleInputs.hasRegularDeposits,
          ruleInputs.meetsBalanceYears,
          ruleInputs.hasNoRecentWithdrawals
        );
        alert(`R7 cumplida: ${result.data}`);
        break;

      default:
        break;
    }
  };

  const ruleInputLabels = {
    1: "Ingresos Mensuales",
    3: "Años de Antigüedad Laboral",
    4: "Total de Deuda",
    5: "Valor de Propiedad",
    6: "Edad del Solicitante",
  };

  const savingsRuleInputs = [
    { id: 'hasMinimumBalance', label: 'Saldo Mínimo Requerido' },
    { id: 'hasConsistentSavings', label: 'Historial de Ahorro Consistente' },
    { id: 'hasRegularDeposits', label: 'Depósitos Periódicos' },
    { id: 'meetsBalanceYears', label: 'Relación Saldo/Años de Antigüedad' },
    { id: 'hasNoRecentWithdrawals', label: 'Sin Retiros Recientes' },
  ];

  return (
    <div style={{ padding: '20px' }}>
      <Typography variant="h4" gutterBottom>Detalle de la Solicitud de Crédito</Typography>
      {credit ? (
        <>
          <Paper elevation={3} style={{ padding: '20px', borderRadius: '8px' }}>
            <Typography variant="h6" gutterBottom>Información del Crédito</Typography>
            <Divider />
            <Grid container spacing={2} style={{ marginTop: '10px' }}>
              <Grid item xs={12} sm={6}><Typography variant="subtitle1"><strong>Nombre del Cliente:</strong> {user ? user.name : 'Cargando...'}</Typography></Grid> {/* Cambiado de ID a Nombre */}
              <Grid item xs={12} sm={6}><Typography variant="subtitle1"><strong>Tipo de Crédito:</strong> {credit.typeLoan ? credit.typeLoan : 'Cargando...'}</Typography></Grid>
              <Grid item xs={12} sm={6}><Typography variant="subtitle1"><strong>Estado:</strong> {credit.state ? credit.state : 'Cargando...'}</Typography></Grid>
              <Grid item xs={12} sm={6}><Typography variant="subtitle1"><strong>Monto Solicitado:</strong> ${credit.amount}</Typography></Grid>
              <Grid item xs={12} sm={6}><Typography variant="subtitle1"><strong>Fecha de Solicitud:</strong> {new Date(credit.requestDate).toLocaleDateString()}</Typography></Grid>
              <Grid item xs={12} sm={6}><Typography variant="subtitle1"><strong>Plazo:</strong> {credit.dueDate ? credit.dueDate : 'Cargando...'} años</Typography></Grid>
              <Grid item xs={12} sm={6}><Typography variant="subtitle1"><strong>Tasa de Interés:</strong> {credit.interestRate}%</Typography></Grid>
            </Grid>
          </Paper>

          <Paper elevation={3} style={{ padding: '20px', borderRadius: '8px', marginTop: '20px' }}>
            <Typography variant="h6" gutterBottom>Reglas del Crédito</Typography>
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
                          name={rule.id === 1 ? "monthlyIncome" : 
                                rule.id === 3 ? "employmentYears" : 
                                rule.id === 4 ? "totalDebt" : 
                                rule.id === 5 ? "propertyValue" : 
                                rule.id === 6 ? "applicantAge" : ""}
                          label={ruleInputLabels[rule.id] || "Ingrese valor"}
                          type="number"
                          onChange={handleInputChange}
                          style={{ marginRight: '10px' }}
                        />
                        <Button variant="contained" onClick={() => checkRule(rule)}>
                          Verificar
                        </Button>
                      </div>
                    )}
                    {rule.id === 7 && ( // Sección de la nueva regla de capacidad de ahorro
                      <div style={{ marginLeft: '20px' }}>
                        {savingsRuleInputs.map((input) => (
                          <div key={input.id}>
                            <Checkbox
                              checked={ruleInputs[input.id]}
                              onChange={handleInputChange}
                              name={input.id}
                            />
                            <ListItemText primary={input.label} />
                          </div>
                        ))}
                        <Button variant="contained" onClick={() => checkRule(rule)}>
                          Verificar Capacidad de Ahorro
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
