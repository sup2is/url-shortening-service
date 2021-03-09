package me.sup2is.exception;

import me.sup2is.domain.ConvertedUrl;
import me.sup2is.service.ConvertedUrlService;
import me.sup2is.web.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = HomeController.class)
class GlobalControllerAdviceTest {

    MockMvc mockMvc;

    @Autowired
    HomeController homeController;

    @MockBean
    ConvertedUrlService convertedUrlService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(homeController)
                .setControllerAdvice(new GlobalControllerAdvice())
                .build();
    }

    @Test
    public void redirect_not_exist_shortening_url() throws Exception{
        //given
        String orgUrl = "http://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(orgUrl);
        String shorteningUrl = "AABBCC";
        convertedUrl.bindShorteningUrl(shorteningUrl);

        when(convertedUrlService.findByShorteningUrl(shorteningUrl))
                .thenThrow(new ConvertedUrlNotFoundException(shorteningUrl + " is invalid."));

        //when
        MvcResult mvcResult = mockMvc.perform(get("/" + shorteningUrl)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals("index", mvcResult.getResponse().getForwardedUrl());
        List<String> errors = (List<String>) mvcResult.getModelAndView().getModelMap().get("errors");
        assertEquals(1, errors.size());
        assertEquals("AABBCC is invalid.", errors.get(0));
    }


}