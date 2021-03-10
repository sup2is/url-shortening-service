package me.sup2is.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.service.ConvertedUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(ObjectMapper.class)
@SpringBootTest(classes = HomeController.class)
@EnableWebMvc
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ConvertedUrlService convertedUrlService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void common_index() throws Exception{
        //given
        //when
        MvcResult mvcResult = mockMvc.perform(get("/")).andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        //then
        assertEquals("index", mvcResult.getResponse().getForwardedUrl());
    }

    @Test
    public void redirect() throws Exception{
        //given
        String orgUrl = "http://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(orgUrl);
        String shorteningUrl = "AABBCC";
        convertedUrl.bindShorteningUrl(shorteningUrl);

        when(convertedUrlService.findByShorteningUrl(shorteningUrl))
            .thenReturn(convertedUrl);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/" + shorteningUrl)).andDo(print())
            .andExpect(status().is3xxRedirection())
            .andReturn();


        //then
        assertEquals(convertedUrl.getOrgUrl(), mvcResult.getResponse().getRedirectedUrl());
    }

}