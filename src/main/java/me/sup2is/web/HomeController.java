package me.sup2is.web;

import lombok.RequiredArgsConstructor;
import me.sup2is.service.ConvertedUrlService;
import me.sup2is.web.dto.ConvertedUrlRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ConvertedUrlService convertedUrlService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/convert")
    public String convert(@RequestBody @Valid ConvertedUrlRequestDto convertedUrlRequestDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
        } else {
            convertedUrlService.register(convertedUrlRequestDto.toConvertedUrl());
            model.addAttribute("convertedUrl", convertedUrlService.findByOrgUrl(convertedUrlRequestDto.getOrgUrl()));
        }
        return "index";
    }

}
