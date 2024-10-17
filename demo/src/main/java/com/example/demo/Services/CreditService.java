package com.example.demo.Services;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Entities.UserClientEntity;
import com.example.demo.Repository.CreditRepository;
import com.example.demo.Repository.DocumentRepository;
import jakarta.persistence.Access;
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

    public int getAmountById(Long id) {
        return creditRepository.findById(id).get().getAmount();
    }

    public CreditEntity saveCredit(CreditEntity credit) {
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

    // Calcula la cuota mensual según la fórmula proporcionada
    public double calculateMonthlyPayment(double loanAmount, double annualInterestRate, int termInYears) {
        double monthlyInterestRate = annualInterestRate / 12 / 100;  // Convertir la tasa anual a tasa mensual
        int numberOfPayments = termInYears * 12;  // Número total de pagos (meses)

        // Fórmula para calcular la cuota mensual
        return (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));
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
     * Crear una solicitud de crédito y asociar los documentos requeridos según el tipo de préstamo.
     * @param credit solicitud de crédito
     * @param files archivos subidos por el cliente
     * @return El crédito creado con los documentos asociados
     * @throws Exception si no se reciben los archivos correctos
     */
    public CreditEntity createCreditWithDocuments(CreditEntity credit, MultipartFile[] files) throws Exception {
        // 1. Determinar cuántos documentos se necesitan según el tipo de préstamo
        int requiredDocuments = getRequiredDocumentsCount(credit.getTypeLoan());

        // 2. Validar que se haya recibido la cantidad correcta de archivos
        if (files.length != requiredDocuments) {
            throw new Exception("Debe subir " + requiredDocuments + " documentos para este tipo de préstamo.");
        }

        // 3. Guardar el crédito en la base de datos
        CreditEntity savedCredit = creditRepository.save(credit);

        // 4. Guardar los archivos en el servidor y crear los DocumentEntity relacionados
        List<DocumentEntity> documents = new ArrayList<>();
        for (MultipartFile file : files) {
            // Guardar el archivo en el sistema de archivos
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), filePath);

            // Crear la entidad DocumentEntity con la ruta del archivo
            DocumentEntity document = new DocumentEntity();
            document.setFileName(fileName);
            document.setFilePath(filePath.toString());
            document.setCreditId(credit.getId());  // Relacionar el documento con el crédito
            documents.add(document);
        }

        // 5. Guardar los documentos en la base de datos
        documentRepository.saveAll(documents);

        return savedCredit;  // Devolver el crédito con los documentos guardados
    }

    /**
     * Obtener la cantidad de documentos requeridos según el tipo de préstamo.
     * @param typeLoan tipo de préstamo (1, 2, 3, 4)
     * @return cantidad de documentos requeridos
     */
    private int getRequiredDocumentsCount(int typeLoan) {
        switch (typeLoan) {
            case 1: // Primera Vivienda
                return 3;  // Ejemplo: 2 documentos requeridos
            case 2: // Segunda Vivienda
                return 4;  // Ejemplo: 3 documentos requeridos
            case 3: // Propiedades Comerciales
                return 4;  // Ejemplo: 4 documentos requeridos
            case 4: // Remodelación
                return 3;  // Ejemplo: 2 documentos requeridos
            default:
                throw new IllegalArgumentException("Tipo de préstamo inválido.");
        }
    }





}
