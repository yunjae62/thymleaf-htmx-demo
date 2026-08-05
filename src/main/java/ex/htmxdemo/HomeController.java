package ex.htmxdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("now", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return "index :: result";
    }
}
