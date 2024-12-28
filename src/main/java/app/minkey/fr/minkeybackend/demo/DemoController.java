package app.minkey.fr.minkeybackend.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {
    @GetMapping()
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> secured() {
        return ResponseEntity.ok("Hello from Secured API");
    }

}
