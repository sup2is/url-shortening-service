package me.sup2is.web;

import lombok.RequiredArgsConstructor;
import me.sup2is.service.ConvertedUrlService;
import me.sup2is.web.dto.ConvertedUrlRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ConvertedUrlService convertedUrlService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/convert")
    public String convert(@RequestBody @Valid ConvertedUrlRequestDto convertedUrlRequestDto,
                          BindingResult bindingResult,
                          Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(toList()));
        } else {
            convertedUrlService.register(convertedUrlRequestDto.toConvertedUrl());
            model.addAttribute("convertedUrl", convertedUrlService.findByOrgUrl(convertedUrlRequestDto.getOrgUrl()));
        }
        return "index";
    }

}
