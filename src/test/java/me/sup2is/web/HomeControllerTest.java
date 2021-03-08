package me.sup2is.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.exception.ConvertedUrlNotFoundException;
import me.sup2is.service.ConvertedUrlService;
import me.sup2is.web.dto.JsonResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void convert() throws Exception {
        //given
        String orgUrl = "http://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(orgUrl);
        convertedUrl.bindShorteningUrl("http://localhost/converted");
        Map<String, String> map = new HashMap<>();
        map.put("orgUrl", orgUrl);

        when(convertedUrlService.findByOrgUrl(orgUrl))
                .thenReturn(convertedUrl);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/convert")
                    .content(objectMapper.writeValueAsString(map))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

        //then
        JsonResult<ConvertedUrl> jsonResult =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JsonResult.class);
        ConvertedUrl responseObj = objectMapper.convertValue(jsonResult.getData(), ConvertedUrl.class);

        assertEquals(JsonResult.Result.SUCCESS, jsonResult.getResult());
        assertEquals(convertedUrl.getRequestCnt(), responseObj.getRequestCnt());
        assertEquals(convertedUrl.getShorteningUrl(), responseObj.getShorteningUrl());
        assertEquals(convertedUrl.getOrgUrl(), responseObj.getOrgUrl());
        assertEquals(convertedUrl.getId(), responseObj.getId());
    }

    @Test
    public void convert_request_invalid_param() throws Exception {
        //given
        String orgUrl = "htts://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(orgUrl);
        convertedUrl.bindShorteningUrl("http://localhost/converted");
        Map<String, String> map = new HashMap<>();
        map.put("orgUrl", orgUrl);

        when(convertedUrlService.findByOrgUrl(orgUrl))
                .thenReturn(convertedUrl);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/convert")
                    .content(objectMapper.writeValueAsString(map))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

        //then
        JsonResult<FieldError> jsonResult =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JsonResult.class);
        assertEquals(JsonResult.Result.FAIL, jsonResult.getResult());
        assertEquals(1, jsonResult.getFieldErrors().size());
        assertEquals("Unable to shorten that link. It is not a valid url.",
            jsonResult.getFieldErrors().get(0).getMessage());

    }

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
        assertEquals(convertedUrl.getOrgUrl(), mvcResult.getResponse().getRedirectedUrl());
    }

}