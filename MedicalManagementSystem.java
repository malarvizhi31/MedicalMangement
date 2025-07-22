package example.medicalmangement;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicalManagementSystem {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> patientCollection;
    private final MongoCollection<Document> appointmentCollection;
    private final MongoCollection<Document> counterCollection;

    public MedicalManagementSystem() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("medical_management");
        patientCollection = database.getCollection("patients");
        appointmentCollection = database.getCollection("appointments");
        counterCollection = database.getCollection("counters");
    }

    public void addPatient(String name, String gender, int age) {
        int patientId = getNextPatientId();
        Document doc = new Document("patient_id", patientId)
                .append("name", name)
                .append("gender", gender)
                .append("age", age);
        patientCollection.insertOne(doc);
    }

    public List<Document> getAllPatients() {
        return patientCollection.find().into(new ArrayList<>());
    }

    public boolean deletePatientById(int patientId) {
        Bson filter = Filters.eq("patient_id", patientId);
        if (patientCollection.find(filter).first() == null) return false;

        patientCollection.deleteOne(filter);
        appointmentCollection.deleteMany(Filters.eq("patient_id", patientId));
        return true;
    }

    public boolean addAppointment(int patientId, String doctorName, String appointmentDate) {
        Document patient = patientCollection.find(Filters.eq("patient_id", patientId)).first();
        if (patient == null) return false;

        Document doc = new Document("patient_id", patientId)
                .append("doctor_name", doctorName)
                .append("appointment_date", appointmentDate);
        appointmentCollection.insertOne(doc);
        return true;
    }

    public List<Document> getAllAppointments() {
        return appointmentCollection.find().into(new ArrayList<>());
    }

    private int getNextPatientId() {
        Document result = counterCollection.findOneAndUpdate(
                Filters.eq("_id", "patient_id"),
                Updates.inc("sequence_value", 1)
        );

        if (result == null) {
            counterCollection.insertOne(new Document("_id", "patient_id").append("sequence_value", 1));
            return 1;
        } else {
            return result.getInteger("sequence_value") + 1;
        }
    }
}
