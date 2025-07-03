package com.Krushna.krushnabazzar.controller;

import com.Krushna.krushnabazzar.EmailService;
import com.Krushna.krushnabazzar.Helper.Message;
import com.Krushna.krushnabazzar.entity.User;
import com.Krushna.krushnabazzar.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;

@Controller
public class ForgotController {
    Random random = new Random();

    @Autowired
    private EmailService emailservice;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    //	email id form
    @GetMapping("/forgot")
    public String openEmailForm() {
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        System.out.println("Email: " + email);

        User user = userrepo.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", new Message("User with this email does not exist!", "alert-danger"));
            return "redirect:/forgot";  // redirect ensures flash attributes work properly
        }

        // Generate OTP and send email
        int otp = random.nextInt(999999);
        System.out.println("otp: " + otp);

        String subject = "Krushna E-SuperBazzar – One-Time Password (OTP)";
        String message = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ccc;'>"
                + "<h2 style='color: #2c3e50;'>Hello,</h2>"
                + "<p>Thank you for using <strong>Krushna E-SuperBazzar</strong>.</p>"
                + "<p>Your One-Time Password (OTP) for verification is:</p>"
                + "<h1 style='color: #e74c3c; font-size: 36px;'>" + otp + "</h1>"
                + "<p>This OTP is valid for 10 minutes. Please do not share it with anyone.</p>"
                + "<br><p>Regards,<br><strong>Krushna E-SuperBazzar Team</strong></p>"
                + "</div>";

        boolean flag = this.emailservice.sendEmail(subject, message, email);

        if (flag) {
            session.setAttribute("myotp", otp);
            session.setAttribute("email", email);
            return "verify_otp"; // return view (no redirect)
        } else {
            redirectAttributes.addFlashAttribute("message", new Message("Check your email ID !!", "alert-danger"));
            return "redirect:/forgot";  // redirect to show flash message
        }
    }


    //	verify-otp
    @PostMapping("/verify-otp")
    public String verifyotp(@RequestParam("otp") int otp , HttpSession session) {
        int myotp = (int) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");

        if(myotp == otp) {
            User user = this.userrepo.findByEmail(email);

            if(user == null) {
//				error message
                session.setAttribute("message", new Message("User Does Not Exist With This Email Id..!", "alert-danger"));
                return "forgot_email_form";
            }else {
//				send change password form
                return "change_password_form";
            }
        }else {
            session.setAttribute("message", "You have entered wrong otp..!");
            return "verify_otp";
        }
//		return null;
    }

    //	change-password
    @PostMapping("/change-password")
    public String ChangePass(@RequestParam("newpassword") String newpassword, HttpSession session) {
        String email = (String) session.getAttribute("email");
        User user = this.userrepo.findByEmail(email);
        user.setPassword(this.bcrypt.encode(newpassword));
        this.userrepo.save(user);
        return "redirect:login?change=password changed successfully :) ";
    }
}
