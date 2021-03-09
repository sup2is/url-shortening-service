package me.sup2is.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ConvertedUrlNotFoundException.class)
    public ModelAndView handleConvertedUrlNotFoundException(ConvertedUrlNotFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("errors", Arrays.asList(e.getMessage()));
        return mav;
    }


}
