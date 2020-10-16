package main;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @RequestMapping("/")
    public String index(Model model) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
//        return LocalDateTime.now().format(formatter);
        return "index";
    }
}
