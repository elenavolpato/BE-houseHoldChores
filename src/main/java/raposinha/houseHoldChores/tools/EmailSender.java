package raposinha.houseHoldChores.tools;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import raposinha.houseHoldChores.DTO.SendEmailDTO;
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
    public SendEmailDTO sendRegistrationEmail(User recipient) {
//        String apiKey = System.getenv("API_KEY");
//        if (apiKey == null) {
//            apiKey = "API_KEY";
//        }
        HttpResponse<String> response = Unirest.post(
                        "https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", apiKey)
                .field("from", "Household Chores App <mail@" + domainName + ">")
                .field("to", recipient.getEmail())
                .field("subject", "Welcome to Household Chores APP")
                .field(
                        "text",
                        "Hello, " + recipient.getUsername()
                                + "! Welcome to the Household chores app!"
                )
                .asString();

        System.out.println(response.getStatus() + domainName);
        System.out.println(response.getBody());

        if (response.getStatus() != 200) {
            throw new RuntimeException(
                    "Failed to send email: " + response.getBody()
            );
        }

        return new SendEmailDTO(
                "Email sent successfully to " + recipient.getEmail(),
                LocalDateTime.now()
        );
    }


}
