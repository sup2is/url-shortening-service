package me.sup2is.web;

import lombok.RequiredArgsConstructor;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.service.ConvertedUrlService;
import me.sup2is.web.dto.ConvertedUrlRequestDto;
import me.sup2is.web.dto.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public ResponseEntity<JsonResult<?>> convert(@RequestBody @Valid ConvertedUrlRequestDto convertedUrlRequestDto,
                                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(new JsonResult<>(bindingResult.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }
        convertedUrlService.register(convertedUrlRequestDto.toConvertedUrl());
        ConvertedUrl findOne = convertedUrlService.findByOrgUrl(convertedUrlRequestDto.getOrgUrl());
        return ResponseEntity.ok(new JsonResult<>(findOne));
    }

    @GetMapping("/{shorteningUrl}")
    public String redirect(@PathVariable("shorteningUrl") String shorteningUrl) {
        ConvertedUrl convertedUrl = convertedUrlService.findByShorteningUrl(shorteningUrl);
        return "redirect:" + convertedUrl.getOrgUrl();
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void disableFavicon() {
        //not working. The favicon is handled by the web server
    }

}
