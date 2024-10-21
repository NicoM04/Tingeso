package com.example.demo.Controller;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/credit")
@CrossOrigin("*")
public class CreditController {
    @Autowired
    CreditService creditService;


    @GetMapping("/")
    public ResponseEntity<List<CreditEntity>> getAllCredits() {
        List<CreditEntity> credit = creditService.getAllCredits();
        return ResponseEntity.ok(credit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditEntity> getCreditById(@PathVariable Long id) {
        CreditEntity credit = creditService.getCreditById(id);
        return ResponseEntity.ok(credit);
    }

    @PostMapping("/")
    public ResponseEntity<CreditEntity> saveCredit(@RequestBody CreditEntity credit) {
        CreditEntity creditNew = creditService.saveCredit(credit);
        return ResponseEntity.ok(creditNew);
    }

    @PutMapping("/")
    public ResponseEntity<CreditEntity> updateCredit(@RequestBody CreditEntity credit){
        CreditEntity creditUpdated = creditService.updateCredit(credit);
        return ResponseEntity.ok(creditUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCreditById(@PathVariable Long id) throws Exception {
        var isDeleted = creditService.deleteCredit(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/amount/{id}")
    public int getAmountById(@PathVariable Long id) {
        return creditService.getAmountById(id);
    }


    @PutMapping("/update-state")
    public ResponseEntity<CreditEntity> updateState(@RequestBody CreditEntity credit){
        CreditEntity creditUpdated = creditService.updateCredit(credit);
        return ResponseEntity.ok(creditUpdated);
    }
    //creo q esta mal esto


    // para simular un crédito
    @PostMapping("/simulate")
    public ResponseEntity<?> simulateCredit(@RequestBody CreditEntity credit) {
        // Validación simple
        if (credit.getAmount() <= 0 || credit.getInterestRate() <= 0 || credit.getDueDate() <= 0) {
            return ResponseEntity.badRequest().body("Valores no válidos.");
        }

        CreditEntity simulatedCredit = creditService.simulateCredit(credit);
        return ResponseEntity.ok(simulatedCredit);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCredit(@RequestBody CreditEntity credit,
                                               @RequestParam("files") MultipartFile[] files) {
        try {

            // 2. Llamar al servicio para procesar la creación del crédito y la subida de documentos
            creditService.createCreditWithDocuments(credit, files);

            return ResponseEntity.ok("Crédito creado y documentos subidos correctamente.");

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }


    /**
     * Endpoint para verificar la relación cuota/ingreso (R1)
     * @param credit Solicitud de crédito con el monto de la cuota mensual
     * @param monthlyIncome Ingresos mensuales del solicitante
     * @return true si la relación es menor o igual a 35%, false si no
     */
    @PostMapping("/check-income-to-payment-ratio")
    public ResponseEntity<Boolean> checkIncomeToPaymentRatio(@RequestBody CreditEntity credit,
                                                             @RequestParam double monthlyIncome) {
        boolean result = creditService.checkIncomeToPaymentRatio(credit, monthlyIncome);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para verificar el historial crediticio del cliente (R2)
     * @param hasGoodCreditHistory Indica si el cliente tiene un buen historial crediticio
     * @return true si el historial es bueno, false si no
     */
    @GetMapping("/check-credit-history")
    public ResponseEntity<Boolean> checkCreditHistory(@RequestParam boolean hasGoodCreditHistory) {
        boolean result = creditService.checkCreditHistory(hasGoodCreditHistory);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para verificar la estabilidad laboral del solicitante (R3)
     * @param employmentYears Años de antigüedad en el empleo actual
     * @param isSelfEmployed Indica si el cliente es trabajador independiente
     * @param incomeYears Años de ingresos estables si es trabajador independiente
     * @return true si cumple con la estabilidad laboral, false si no
     */
    @PostMapping("/check-employment-stability")
    public ResponseEntity<Boolean> checkEmploymentStability(@RequestParam int employmentYears,
                                                            @RequestParam boolean isSelfEmployed,
                                                            @RequestParam int incomeYears) {
        boolean result = creditService.checkEmploymentStability(employmentYears, isSelfEmployed, incomeYears);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para verificar la relación deuda/ingreso (R4)
     * @param credit Solicitud de crédito con la cuota mensual
     * @param totalDebt Deuda total actual del cliente
     * @param monthlyIncome Ingresos mensuales del cliente
     * @return true si la relación deuda/ingreso es menor o igual a 50%, false si es mayor
     */
    @PostMapping("/check-debt-to-income-ratio")
    public ResponseEntity<Boolean> checkDebtToIncomeRatio(@RequestBody CreditEntity credit,
                                                          @RequestParam double totalDebt,
                                                          @RequestParam double monthlyIncome) {
        boolean result = creditService.checkDebtToIncomeRatio(credit, totalDebt, monthlyIncome);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para verificar el monto máximo financiable (R5)
     * @param credit Solicitud de crédito con el tipo de préstamo
     * @param propertyValue Valor de la propiedad
     * @return true si el monto está dentro del límite financiable, false si es mayor
     */
    @PostMapping("/check-maximum-loan-amount")
    public ResponseEntity<Boolean> checkMaximumLoanAmount(@RequestBody CreditEntity credit,
                                                          @RequestParam double propertyValue) {
        boolean result = creditService.checkMaximumLoanAmount(credit, propertyValue);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para verificar si el préstamo puede finalizar antes de los 75 años (R6)
     * @param applicantAge Edad actual del solicitante
     * @param loanTerm Plazo del préstamo en años
     * @return true si puede finalizar antes de los 75 años, false si no
     */
    @GetMapping("/check-applicant-age")
    public ResponseEntity<Boolean> checkApplicantAge(@RequestParam int applicantAge,
                                                     @RequestParam int loanTerm) {
        boolean result = creditService.checkApplicantAge(applicantAge, loanTerm);
        return ResponseEntity.ok(result);
    }

    /*
     * Endpoint para verificar la capacidad de ahorro (R7)
     * @param savingsAccount Información de la cuenta de ahorros del cliente
     * @param requestedLoanAmount Monto del préstamo solicitado
     * @return true si cumple con las reglas de ahorro, false si no

    @PostMapping("/check-savings-capacity")
    public ResponseEntity<Boolean> checkSavingsCapacity(@RequestBody SavingsAccount savingsAccount,
                                                        @RequestParam double requestedLoanAmount) {
        boolean result = creditService.checkSavingsCapacity(savingsAccount, requestedLoanAmount);
        return ResponseEntity.ok(result);
    }
     */

}
