package me.sup2is.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.service.ConvertedUrlService;
import me.sup2is.web.dto.ConvertedUrlRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(ObjectMapper.class)
@SpringBootTest(classes = HomeController.class)
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
        ConvertedUrlRequestDto convertedUrlRequestDto = new ConvertedUrlRequestDto(orgUrl);

        when(convertedUrlService.findByOrgUrl(orgUrl))
                .thenReturn(convertedUrl);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/convert")
                .content(objectMapper.writeValueAsString(convertedUrlRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        //then
        System.out.println(mvcResult);
    }

}