package me.sup2is.web.api;

import lombok.RequiredArgsConstructor;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.exception.ConvertedUrlNotFoundException;
import me.sup2is.service.ConvertedUrlService;
import me.sup2is.web.dto.ConvertedUrlRequestDto;
import me.sup2is.web.dto.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ConvertController {

    private final ConvertedUrlService convertedUrlService;

    @PostMapping("/convert")
    public ResponseEntity<JsonResult<?>> convert(@RequestBody @Valid ConvertedUrlRequestDto convertedUrlRequestDto,
                                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(new JsonResult<>(bindingResult.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try {
            convertedUrlService.register(convertedUrlRequestDto.toConvertedUrl());
            ConvertedUrl findOne = convertedUrlService.findByOrgUrl(convertedUrlRequestDto.getOrgUrl());
            return ResponseEntity.ok(new JsonResult<>(findOne));
        } catch (Exception e) {
            return new ResponseEntity<>(new JsonResult<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
