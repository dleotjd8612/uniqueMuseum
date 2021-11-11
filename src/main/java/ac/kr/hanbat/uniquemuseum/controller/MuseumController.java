package ac.kr.hanbat.uniquemuseum.controller;

import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.service.MuseumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("museum")
@Log4j2
@RequiredArgsConstructor
public class MuseumController {
    private final MuseumService museumService;

    @GetMapping("register")
    public void register() { // 박물관 등록 페이지 호출

    }
    @PostMapping("register")
    public String register(MuseumDTO museumDTO, RedirectAttributes redirectAttributes) {
        log.info("museumDTO: " + museumDTO);

        Long mno = museumService.register(museumDTO);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/museum/list";
    }
}
