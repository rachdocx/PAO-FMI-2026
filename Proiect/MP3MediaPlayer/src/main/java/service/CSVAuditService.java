package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVAuditService {

    private static CSVAuditService instance;
    private String fisier = "audit_log.csv";

    private CSVAuditService() {
    }

    public static CSVAuditService getInstance() {
        if (instance == null) {
            instance = new CSVAuditService();
        }
        return instance;
    }

    public void logAction(String actiune) {
        try {
            FileWriter writer = new FileWriter(fisier, true);
            
            String timp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(actiune + "," + timp + "\n");
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Eroare la scriere: " + e.getMessage());
        }
    }
}
