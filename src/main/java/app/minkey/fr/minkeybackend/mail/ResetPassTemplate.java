package app.minkey.fr.minkeybackend.mail;

import java.util.Map;

public class ResetPassTemplate extends BrevoTemplate {
        String firstname;
        String url;
        public Map<String, Object> params() {
            return Map.of("firstname", firstname, "url", url);
        }
        public int template() {
            return 1;
        }
}
