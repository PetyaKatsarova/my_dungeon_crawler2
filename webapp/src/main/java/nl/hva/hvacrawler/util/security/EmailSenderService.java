package nl.hva.hvacrawler.util.security;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nl.hva.hvacrawler.business.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    private static final String SENDER_EMAIL = "hvacrawler2023@gmail.com";
    private static final String SENDER_NAME = "HVA Crawler Team";

    public void sendVerificationEmail(String url, User theUser)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String mailContent = "<p> Hi new Crawler, </p>" +
                "<p> Thank you for registering with us, " +
                "Please, follow the link below to complete your registration. </p>" +
                "<a href=\"" + url+ "\">Verify your email to activate your account</a>" +
                "<p> Thank you, <br> HVA Crawler Team </p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(SENDER_EMAIL, SENDER_NAME);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sendResetTokenEmail(String email, String token)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(SENDER_EMAIL, SENDER_NAME);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Password Reset Token");

        String htmlContent = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Login Verification</title>
            </head>
            <body>
            <h1>Reset Token</h1>
            <p>Your reset code:</p>
            <h2 style="background-color: #f2f2f2; padding: 10px;">%s</h2>
            <p>The verification code will be valid for 30 minutes. Please do not share this code with anyone.</p>
            <p>Don’t recognize this activity? Please <a href=""http://localhost:8080/verify-account?email=%s&token=%s">reset your password</a> and contact customer support immediately.</p>
            <p>This is an automated message, please do not reply.</p>
            </body>
            </html>
            """.formatted(token, email, true);

        mimeMessageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}
