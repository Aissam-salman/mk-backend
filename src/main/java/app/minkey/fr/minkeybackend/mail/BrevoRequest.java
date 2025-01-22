package app.minkey.fr.minkeybackend.mail;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrevoRequest {
    private BrevoTemplate template;
    private List<String> receivers;
}


@JsonComponent
class BrevoRequestSerializer extends JsonSerializer<BrevoRequest> {

    @Override
    public void serialize(BrevoRequest request, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeStartObject();

        // Write receivers
        generator.writeArrayFieldStart("to");
        for (String receiver : request.getReceivers()) {
            generator.writeStartObject();
            generator.writeStringField("email", receiver);
            generator.writeEndObject();
        }
        generator.writeEndArray();

        // Write template ID
        generator.writeNumberField("templateId", request.getTemplate().getTemplateId());

        // Write template parameters if not empty
        Map<String, Object> params = request.getTemplate().getParams();
        if (params != null && !params.isEmpty()) {
            generator.writeObjectFieldStart("params");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                generator.writeStringField(entry.getKey(), entry.getValue().toString());
            }
            generator.writeEndObject();
        }

        generator.writeEndObject();
    }
}