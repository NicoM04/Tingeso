package com.example.demo.Services;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Repository.CreditRepository;
import com.example.demo.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditService {

    private static final String UPLOAD_DIR = "C:/uploads/";
    @Autowired
    CreditRepository creditRepository;
    @Autowired
    private DocumentRepository documentRepository;


    public CreditEntity getCreditById(Long id) {
        return creditRepository.findById(id).get();
    }

    // Obtener créditos por ID de cliente
    public List<CreditEntity> getCreditsByClientId(Long idClient) {
        return creditRepository.findByIdClient(idClient); // Cambiado a idClient
    }


    public CreditEntity saveCredit(CreditEntity credit) {
        // Calcular el pago mensual
        double monthlyPayment = calculateMonthlyPayment(credit.getAmount(), credit.getInterestRate(), credit.getDueDate());
        credit.setMonthlyPayment(monthlyPayment);

        // Calcular el costo mensual
        double monthlyCost = calculateMonthlyCost(monthlyPayment, credit.getAmount());

        // Calcular el costo total
        double totalCost = calculateTotalCost(monthlyCost, credit.getDueDate(), credit.getAmount());
        credit.setTotalCost(totalCost);

        return creditRepository.save(credit);
    }
    public ArrayList<CreditEntity> getAllCredits() {
        return (ArrayList<CreditEntity>) creditRepository.findAll();
    }

    public ArrayList<CreditEntity> getCreditByState(int state){
        return (ArrayList<CreditEntity>) creditRepository.findByState(state);
    }
    public ArrayList<CreditEntity> getCreditByTypeLoan(int type){
        return (ArrayList<CreditEntity>) creditRepository.findByTypeLoan(type);
    }
    public CreditEntity updateCredit(CreditEntity credit) {
        return creditRepository.save(credit);
    }
    public boolean deleteCredit(Long id) throws Exception {
        try{
            creditRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    // Nuevo método para actualizar el estado del crédito
    public CreditEntity updateCreditStatus(Long creditId, int newStatus) {
        CreditEntity credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Crédito no encontrado con ID: " + creditId));
        credit.setState(newStatus);// Actualizar el estado
        return creditRepository.save(credit);// Guardar y devolver el crédito actualizado
    }

    // Calcula la cuota mensual según la fórmula proporcionada
    public double calculateMonthlyPayment(double loanAmount, double annualInterestRate, int termInYears) {
        double monthlyInterestRate = annualInterestRate / 12 / 100;  // Convertir la tasa anual a tasa mensual
        int numberOfPayments = termInYears * 12;  // Número total de pagos (meses)

        // Fórmula para calcular la cuota mensual
        return (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));
    }
    // Nuevo método para calcular el costo mensual según las reglas especificadas
    public double calculateMonthlyCost(double monthlyPayment, int amount) {
        double additionalCost = (0.03 / 100) * amount + 20000;  // 0.03% de amount + 20000
        return monthlyPayment + additionalCost;
    }

    // Nuevo método para calcular el costo total del crédito según las reglas especificadas
    public double calculateTotalCost(double monthlyCost, int termInYears, int amount) {
        int numberOfPayments = termInYears * 12;
        double baseTotalCost = monthlyCost * numberOfPayments;
        double additionalTotalCost = (1 / 100.0) * amount;  // 1% de amount
        return baseTotalCost + additionalTotalCost;
    }

    // Realizar la simulación del crédito
    public CreditEntity simulateCredit(CreditEntity credit) {
        double monthlyPayment = calculateMonthlyPayment(
                credit.getAmount(),
                credit.getInterestRate(),
                credit.getDueDate()
        );
        credit.setMonthlyPayment(monthlyPayment);
        return credit;
    }



    /**
     * R1: Relación Cuota/Ingreso.
     * La relación cuota/ingreso no debe ser mayor a un 35%. Si es mayor, la solicitud se rechaza.
     *
     * @param credit El crédito con la cuota mensual calculada.
     * @param monthlyIncome Ingresos mensuales del solicitante.
     * @return true si la relación está dentro del límite, false si es mayor a 35%.
     */
    public boolean checkIncomeToPaymentRatio(CreditEntity credit, double monthlyIncome) {
        double monthlyPayment = credit.getMonthlyPayment(); // Cuota mensual del préstamo
        double ratio = (monthlyPayment / monthlyIncome) * 100; // Cálculo del porcentaje cuota/ingreso

        return ratio < 35; // True si el porcentaje es menor o igual a 35%
    }

    /**
     * R2: Historial Crediticio del Cliente.
     * Revisa el historial crediticio (DICOM) para ver si el cliente tiene morosidad o deudas impagas recientes.
     *
     * @param hasGoodCreditHistory Booleano que indica si el cliente tiene un buen historial crediticio.
     * @return true si el historial es bueno, false si tiene morosidades graves o muchas deudas.
     */
    public boolean checkCreditHistory(boolean hasGoodCreditHistory) {
        // Aquí se podría extender la lógica para incluir consultas adicionales,
        // como revisar una base de datos o implementar reglas adicionales para evaluar el historial.
        if (!hasGoodCreditHistory) {
            return false; // Si el cliente no tiene un buen historial, retorna false
        }
        return true; // Retorna true si el historial es bueno
    }

    public boolean checkEmploymentStability(int employmentYears, boolean isSelfEmployed, int incomeYears) {
        if (isSelfEmployed) {
            // Si es independiente, revisar los últimos 2 años de ingresos estables
            return incomeYears >= 2;
        } else {
            // Si es empleado, requiere al menos 1-2 años de antigüedad
            return employmentYears >= 1;
        }
    }

    /**
     * R4: Relación Deuda/Ingreso.
     * La suma de todas las deudas no debe superar el 50% de los ingresos mensuales del cliente.
     *
     * @param credit El crédito actual, que tiene la nueva cuota mensual.
     * @param totalDebt Todas las deudas actuales del cliente.
     * @param monthlyIncome Ingresos mensuales del cliente.
     * @return true si la relación deuda/ingreso es menor al 50%, false si es mayor.
     */
    public boolean checkDebtToIncomeRatio(CreditEntity credit, double totalDebt, double monthlyIncome) {
        double projectedTotalDebt = totalDebt + credit.getMonthlyPayment();  // Deuda actual + nueva cuota
        double maxDebt = monthlyIncome * 0.50;  // No debe superar el 50% de los ingresos

        return projectedTotalDebt <= maxDebt;
    }

    /**
     * R5: Monto Máximo de Financiamiento.
     * El banco financia un porcentaje máximo del valor de la propiedad, dependiendo del tipo de préstamo.
     *
     * @param credit El crédito que incluye el tipo de préstamo.
     * @param propertyValue El valor de la propiedad.
     * @return true si el financiamiento está dentro de los límites, false si es mayor al permitido.
     */
    public boolean checkMaximumLoanAmount(CreditEntity credit, double propertyValue) {
        double maxLoanPercentage;

        // Determinar el porcentaje máximo financiable según el tipo de préstamo
        switch (credit.getTypeLoan()) {
            case 1:  // Primera vivienda
                maxLoanPercentage = 0.80;
                break;
            case 2:  // Segunda vivienda
                maxLoanPercentage = 0.70;
                break;
            case 3:  // Propiedades comerciales
                maxLoanPercentage = 0.60;
                break;
            case 4:  // Propiedades comerciales (parece un duplicado, ¿es correcto?)
                maxLoanPercentage = 0.50;
                break;
            default:
                throw new IllegalArgumentException("Tipo de préstamo no válido: " + credit.getTypeLoan());
        }

        double maxFinanciamiento = propertyValue * maxLoanPercentage;

        // Validar que el monto del crédito no sea negativo
        if (credit.getAmount() < 0) {
            throw new IllegalArgumentException("El monto del crédito no puede ser negativo.");
        }

        return credit.getAmount() <= maxFinanciamiento;  // El monto solicitado debe estar dentro del límite
    }

    /**
     * R6: Edad del Solicitante.
     * El préstamo debe terminar antes de que el solicitante tenga 75 años.
     * Si al final del plazo está muy cerca de 75 años (a menos de 5 años), el préstamo se rechaza.
     *
     * @param applicantAge Edad actual del solicitante.
     * @param credit Años del plazo del préstamo.
     * @return true si el préstamo puede terminar antes de los 75 años, false si no.
     */
    public boolean checkApplicantAge(int applicantAge, CreditEntity credit) {
        int ageAtLoanEnd = applicantAge + credit.getDueDate();
        return ageAtLoanEnd <= 75 ;  // El solicitante debe tener margen antes de los 75 años
    }




    public String evaluateSavingsCapacity(boolean hasMinimumBalance, boolean hasConsistentSavings,
                                          boolean hasRegularDeposits, boolean meetsBalanceYears,
                                          boolean hasNoRecentWithdrawals) {
        int criteriaMet = 0;

        // Evaluar subreglas
        if (hasMinimumBalance) criteriaMet++;
        if (hasConsistentSavings) criteriaMet++;
        if (hasRegularDeposits) criteriaMet++;
        if (meetsBalanceYears) criteriaMet++;
        if (hasNoRecentWithdrawals) criteriaMet++;

        // Evaluación final
        if (criteriaMet >= 5) {
            return "Sólida"; // Aprobación
        } else if (criteriaMet >= 3) {
            return "Moderada"; // Revisión adicional
        } else {
            return "Insuficiente"; // Rechazo
        }
    }







}