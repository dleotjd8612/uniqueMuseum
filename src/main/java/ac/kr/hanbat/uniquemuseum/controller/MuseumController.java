package ac.kr.hanbat.uniquemuseum.controller;

import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageRequestDTO;
import ac.kr.hanbat.uniquemuseum.service.MuseumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // @Controller의 역할은 Model 객체를 만들어 데이터를 담고 View를 찾는 것
@RequestMapping("museum") // URL을 컨트롤러의 메서드와 매핑할 때 사용
@Log4j2
@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class MuseumController {
    private final MuseumService museumService;

    @GetMapping("register")
    public void register() { // 박물관 등록 페이지 호출

    }
    @PostMapping("register")
    public String register(MuseumDTO museumDTO, RedirectAttributes redirectAttributes) {
        log.info("museumDTO: " + museumDTO);
        // Museum 객체와 Museum 이미지 List<> 객체를 DB에 저장 후 박물관 번호만 가져옴
        Long mno = museumService.register(museumDTO);

        // addAttribute로 전달한 값은 url뒤에 붙으며, 리프레시(새로고침)해도 데이터가 유지
        // addFlashAttribute로 전달한 값은 url뒤에 붙지 않는다. 일회성이라 리프레시할 경우 데이터가 소멸한다.
        // 또한 2개이상 쓸 경우, 데이터는 소멸한다. 따라서 맵을 이용하여 한번에 값을 전달해야한다.
        redirectAttributes.addFlashAttribute("msg", mno);

        // Museum과 Museum 이미지들을 DB에 등록 후 목록 페이지로 이동
        return "redirect:/museum/list";
    }

    @GetMapping("list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("pageRequestDTO: " + pageRequestDTO);
        model.addAttribute("result", museumService.getList(pageRequestDTO));
    }

    // @ModelAttribute
    // 1) @ModelAttribute 어노테이션이 붙은 객체를 자동으로 생성한다.
    // 2) 생성된 오브젝트에(PageRequestDTO) HTTP로 넘어 온 값들을 자동으로 바인딩한다.
    // 3) 마지막으로 @ModelAttribute 어노테이션이 붙은 객체(PageRequestDTO 객체)가 자동으로 Model 객체에 추가되고 뷰단으로 전달된다.
    @GetMapping("read")
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("mno: " + mno);
        MuseumDTO museumDTO = museumService.getMuseum(mno);
        model.addAttribute("dto", museumDTO);
    }

    @GetMapping("modify")
    public void modify(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("mno: " + mno);
        MuseumDTO museumDTO = museumService.getMuseum(mno);
        model.addAttribute("dto", museumDTO);
    }

    @PostMapping("modify") // 박물관 수정
    public String modify(MuseumDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        log.info("post modify...............");
        log.info("dto:" + dto);

        museumService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());

        redirectAttributes.addAttribute("mno", dto.getMno());

        return "redirect:/museum/read";
    }
    
    @PostMapping("remove") // 박물관 삭제
    public String remove(long mno, RedirectAttributes redirectAttributes) {
        log.info("mno: " + mno);
        museumService.remove(mno);
        redirectAttributes.addFlashAttribute("msg", mno);
        return "redirect:/museum/list";
    }
}
