package example.medicalmangement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
public class StaffAuthController {

    @Autowired
    private StaffRepository staffRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final String ADMIN_SECRET_KEY = "admin123";

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String secretKey) {
        if (!ADMIN_SECRET_KEY.equals(secretKey)) {
            return "Unauthorized: Invalid admin key.";
        }

        if (staffRepo.findByUsername(username) != null) {
            return "Staff already exists.";
        }

        String hashed = encoder.encode(password);
        staffRepo.save(new Staff(username, hashed));
        return "Staff registered successfully.";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        Staff staff = staffRepo.findByUsername(username);
        if (staff == null) return "Staff not registered.";
        if (encoder.matches(password, staff.getPassword())) {
            return "Login successful. Welcome, " + username + "!";
        } else {
            return "Incorrect password.";
        }
    }
}
