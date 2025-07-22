package example.medicalmangement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedicalMangementApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalMangementApplication.class, args);
    }
}


// mvn clean install -DskipTests to avoid err
//mvnd clean install -DskipTests
//mvnd spring-boot:run -e -X
//mvnd spring-boot:run    
