package me.sup2is.web.api;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Import(ObjectMapper.class)
@SpringBootTest(classes = ConvertController.class)
@EnableWebMvc
class ConvertControllerTest {

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
        convertedUrl.bindShorteningUrl("AABBCC");
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
        convertedUrl.bindShorteningUrl("AABBCC");
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
    public void convert_internal_server_error() throws Exception {
        //given
        String orgUrl = "http://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(orgUrl);
        convertedUrl.bindShorteningUrl("AABBCC");
        Map<String, String> map = new HashMap<>();
        map.put("orgUrl", orgUrl);

        //unexpect error
        when(convertedUrlService.findByOrgUrl(orgUrl))
                .thenThrow(new ConvertedUrlNotFoundException());

        //when
        MvcResult mvcResult = mockMvc.perform(post("/convert")
                .content(objectMapper.writeValueAsString(map))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        //then
        JsonResult<FieldError> jsonResult =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JsonResult.class);
        assertEquals(JsonResult.Result.FAIL, jsonResult.getResult());
        assertEquals(ConvertedUrlNotFoundException.class.getName(), jsonResult.getError().getName());
    }


}