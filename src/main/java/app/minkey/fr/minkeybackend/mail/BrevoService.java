package app.minkey.fr.minkeybackend.mail;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BrevoService {
    private final RestClient restClient;
    private final Logger logger =  LoggerFactory.getLogger(BrevoService.class);
    @Value("${brevo.enabled}")
    private Boolean enabled;
    public void sendBrevoMail(BrevoTemplate template, String receiver) {
        if (enabled) {
            submit(template, receiver);
        } else {
            log(template, receiver);
        }
    }

    private void log(BrevoTemplate template, String receiver) {
        logger.info("Sending template {} to {}", template, receiver);
    }

    private void submit(BrevoTemplate template, String receiver) {
        restClient.post()
                .body(new BrevoRequest(template, Collections.singletonList(receiver)))
                .retrieve()
                .toBodilessEntity();
    }

}
