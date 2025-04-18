package org.dd_lgp.com.tutospring.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HealthRestController {
    @GetMapping("/ping")
    public String ping() {
        return "Pong";
    }
}
