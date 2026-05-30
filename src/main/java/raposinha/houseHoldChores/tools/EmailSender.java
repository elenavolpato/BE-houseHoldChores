package raposinha.houseHoldChores.tools;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import raposinha.houseHoldChores.DTO.SendRegistrationEmailDTO;
import raposinha.houseHoldChores.entities.User;

import java.time.LocalDateTime;
@Component
public class EmailSender {
    private final String domainName;
    private final String apiKey;
    public EmailSender(@Value("${mailgun.domainName}") String domainName, @Value("${mailgun.apiKey}") String apiKey) {
        this.domainName = domainName;
        this.apiKey = apiKey;
    }

    // registration email
    public void sendRegistrationEmail(User recipient) {
        String htmlContent = """
            <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h1 style="color: #4A90E2;">Welcome %s!</h1>
                    <p>We are thrilled to have you on board.</p>
                    <p>Start organizing your home by checking your assigned tasks.</p>
                    <br>
                    <button>
                        <a href="https://raposinha.dev" 
                        style=" text-decoration: none;">
                        Open Dashboard
                        </a>
                    </button>
                </body>
            </html>
            """.formatted(recipient.getUsername());
        HttpResponse<String> response = Unirest.post(
                        "https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", apiKey)
                .field("from", "Household Chores App <welcome@" + domainName + ">")
                .field("to", recipient.getEmail())
                .field("subject", "Welcome to Household Chores APP")
                .field("text", "Welcome " + recipient.getUsername() + "!")
                .field("html", htmlContent)
                .asString();

        System.out.println(response.getStatus() + domainName);
        System.out.println(response.getBody());

        if (response.getStatus() != 200) {
            throw new RuntimeException(
                    "Failed to send email: " + response.getBody()
            );
        }

        new SendRegistrationEmailDTO(
                "Email sent successfully to " + recipient.getEmail(),
                LocalDateTime.now()
        );
    }


    //invite someone to app email
    public void sendInvitationEmail(User inviter, String recipientEmail, String recipientName, String url){
        String htmlContent = """
       <html>
        <body style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
            <h1 style="color: #4A90E2;">You've been invited to the HouseHold Chores app!</h1>
            <p>Hello <strong>%s</strong>,</p>
            <p><strong>%s</strong> has invited you to join their household. Start organizing your home together by checking your assigned tasks.</p>
            <br>
            <a href="%s" 
               style="background-color: #4A90E2; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;">
               Join the Household Group
            </a>
        </body>
    </html>
    """.formatted(recipientName, inviter.getUsername(), url); // 🚀 Added 'url' variable tracking here

        HttpResponse<String> response = Unirest.post(
                        "https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", apiKey)
                .field("from", "Household Chores App <invite@" + domainName + ">")
                .field("to", recipientEmail)
                .field("subject", "Invitation to Household Chores APP")
                .field("text", "Welcome " + recipientName + "! Join here: " + url)
                .field("html", htmlContent)
                .asString();

        System.out.println(response.getStatus() + domainName);
        System.out.println(response.getBody());

        if (response.getStatus() != 200) {
            throw new RuntimeException(
                    "Failed to send email: " + response.getBody()
            );
        }

        new SendRegistrationEmailDTO(
                "Email sent successfully to " + recipientEmail,
                LocalDateTime.now()
        );

    }
}
