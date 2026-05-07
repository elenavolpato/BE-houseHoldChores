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
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", "Epic Energy Services <epic.energy.services@administration.com>")
                .queryString("to", recipient.getEmail()) // <-- DEVE ESSERE IL DESTINATARIO VERIFICATO!
                .queryString("subject", "Welcome to Household Chores APP")
                .queryString("text", "Hello, " + recipient.getUsername() + " "  + "! Welcome to the Household chores app! Happy organization :D")
                .asJson();
        System.out.println(response.getBody());
        return new SendEmailDTO("Email send to " + recipient.getEmail() + " successfully!", LocalDateTime.now());
    }


}
