package com.example.demo.Controller;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.UserEntity;
import com.example.demo.Services.CreditService;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<CreditEntity>> getAllCredits() {
        List<CreditEntity> credit = creditService.getAllCredits();
        return ResponseEntity.ok(credit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditEntity> getCreditById(@PathVariable("id") Long id) {
        System.out.println("Request received for Credit ID: " + id); // Imprime el ID recibido
        try {
            CreditEntity credit = creditService.getCreditById(id);
            System.out.println("Credit found: " + credit); // Imprime el objeto CreditEntity encontrado
            return ResponseEntity.ok(credit);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching Credit: " + e.getMessage()); // Imprime el error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    // Obtener créditos por ID de cliente
    @GetMapping("/client/{idClient}") // Cambiado a idClient
    public ResponseEntity<List<CreditEntity>> getCreditsByClientId(@PathVariable Long idClient) {
        List<CreditEntity> credits = creditService.getCreditsByClientId(idClient);
        return ResponseEntity.ok(credits);
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

    //creo q esta mal esto

    @PatchMapping("/{id}/status")
    public ResponseEntity<CreditEntity> updateCreditStatus(@PathVariable Long id, @RequestParam int newStatus) {
        CreditEntity creditUpdated = creditService.updateCreditStatus(id, newStatus);
        return ResponseEntity.ok(creditUpdated);
    }


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



    @GetMapping("/{id}/user")
    public ResponseEntity<UserEntity> getUserByCreditId(@PathVariable("id") Long id) {
        CreditEntity credit = creditService.getCreditById(id); // Busca el crédito por ID
        if (credit == null) {
            return ResponseEntity.notFound().build();
        }

        Long userId = credit.getIdClient(); // Obtén el ID del cliente desde el objeto de crédito
        UserEntity user = userService.getUserById(userId); // Busca el cliente por su ID

        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
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
    @PostMapping("/check-credit-history")
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
     * @param credit Plazo del préstamo en años
     * @return true si puede finalizar antes de los 75 años, false si no
     */
    @PostMapping("/check-applicant-age")
    public ResponseEntity<Boolean> checkApplicantAge(@RequestParam int applicantAge,
                                                     @RequestBody CreditEntity credit) {
        boolean result = creditService.checkApplicantAge(applicantAge, credit);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/evaluate-savings-capacity")
    public ResponseEntity<String> evaluateSavingsCapacity(
            @RequestParam boolean hasMinimumBalance,
            @RequestParam boolean hasConsistentSavings,
            @RequestParam boolean hasRegularDeposits,
            @RequestParam boolean meetsBalanceYears,
            @RequestParam boolean hasNoRecentWithdrawals) {

        String result = creditService.evaluateSavingsCapacity(
                hasMinimumBalance,
                hasConsistentSavings,
                hasRegularDeposits,
                meetsBalanceYears,
                hasNoRecentWithdrawals
        );

        return ResponseEntity.ok(result);
    }

}