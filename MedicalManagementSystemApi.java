package example.medicalmangement;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.Document;

import java.util.List;
@CrossOrigin(origins = "http://localhost:*") // 🔥 Important for Flutter Web (Chrome)
@RestController
@RequestMapping("/api")
public class MedicalManagementSystemApi {

    @Autowired
    private MedicalManagementSystem system;

    // -------------------- PATIENTS --------------------

    @PostMapping("/patients")
    public String addPatient(@RequestParam String name, @RequestParam String gender, @RequestParam int age) {
        system.addPatient(name, gender, age);
        return "Patient added using service.";
    }

    @GetMapping("/patients")
    public String viewPatients() {
        StringBuilder sb = new StringBuilder();
        List<Document> patients = system.getAllPatients();
        if (patients.isEmpty()) return "No patients found.";

        for (Document doc : patients) {
            sb.append("Patient ID: ").append(doc.getInteger("patient_id"))
              .append(", Name: ").append(doc.getString("name"))
              .append(", Gender: ").append(doc.getString("gender"))
              .append(", Age: ").append(doc.getInteger("age"))
              .append("\n");
        }
        return sb.toString();
    }

    @DeleteMapping("/patients/{pid}")
    public String deletePatient(@PathVariable int pid) {
        boolean deleted = system.deletePatientById(pid);
        return deleted ? "Patient and appointments deleted." : "Patient not found.";
    }

    // -------------------- APPOINTMENTS --------------------

    @PostMapping("/appointments")
    public String addAppointment(@RequestParam(required = false) Integer patientid,
                                 @RequestParam(required = false) String doctorname,
                                 @RequestParam(required = false) String date) {
        if (patientid == null || doctorname == null || date == null) {
            return "Missing required fields. Please provide patientId, doctorName, and date.";
        }
        try {
            boolean result = system.addAppointment(patientid, doctorname, date);
            return result ? "Appointment added." : "Patient not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding appointment: " + e.getMessage();
        }
    }

    @GetMapping("/appointments")
    public String viewAppointments() {
        StringBuilder sb = new StringBuilder();
        List<Document> appointments = system.getAllAppointments();
        if (appointments.isEmpty()) return "No appointments found.";

        for (Document doc : appointments) {
            sb.append("Appointment ID: ").append(doc.getObjectId("_id"))
              .append(", Patient ID: ").append(doc.getInteger("patient_id"))
              .append(", Doctor: ").append(doc.getString("doctor_name"))
              .append(", Date: ").append(doc.getString("appointment_date"))
              .append("\n");
        }
        return sb.toString();
    }
}
