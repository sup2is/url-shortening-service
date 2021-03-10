package me.sup2is.web;

import lombok.RequiredArgsConstructor;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.service.ConvertedUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ConvertedUrlService convertedUrlService;

    @GetMapping("/")
    public String index() {
        return "index";
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
