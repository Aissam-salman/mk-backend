package app.minkey.fr.minkeybackend.mail;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrevoTemplate {
        private int templateId;
        private Map<String, Object> params;
}
