package nl.hva.hvacrawler.util.event.listener;

import jakarta.mail.MessagingException;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.business.service.RegistrationService;
import nl.hva.hvacrawler.util.event.RegistrationCompleteEvent;
import nl.hva.hvacrawler.exception.EmailSendingException;
import nl.hva.hvacrawler.util.security.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    private final Logger logger = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);
    private final RegistrationService registrationService;
    private final EmailSenderService emailSenderService;

    public RegistrationCompleteEventListener(RegistrationService registrationService, EmailSenderService emailSenderService) {
        this.registrationService = registrationService;
        this.emailSenderService = emailSenderService;
        logger.info("New RegistrationCompleteEventListener");
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        registrationService.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl()+"/verifyEmail?token="+verificationToken;
        try {
            emailSenderService.sendVerificationEmail(url, theUser);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailSendingException("Error sending verification email", e);
        }
    }


}
