package me.sup2is.service;

import me.sup2is.domain.ConvertedUrl;
import me.sup2is.exception.ConvertedUrlNotFoundException;
import me.sup2is.repository.ConvertedUrlRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ConvertedUrlService.class)
class ConvertedUrlServiceTest {

    @Autowired
    ConvertedUrlService convertedUrlService;

    @MockBean
    ConvertedUrlRepository convertedUrlRepository;

    @MockBean
    StringGenerator<Long> stringGenerator;

    @Test
    public void find_by_org_url() {
        //given
        String orgUrl = "http://localhost/test";
        ConvertedUrl expect = ConvertedUrl.createConvertedUrl(orgUrl);
        when(convertedUrlRepository.findByOrgUrl(orgUrl))
            .thenReturn(Optional.of(expect));

        //when
        ConvertedUrl byOrgUrl = convertedUrlService.findByOrgUrl(orgUrl);

        //then
        assertEquals(expect, byOrgUrl);
    }

    @Test
    public void find_by_org_url_when_not_exist() {
        //given
        //when
        //then
        assertThrows(ConvertedUrlNotFoundException.class, () -> convertedUrlService.findByOrgUrl("http://localhost/test"));
    }

    @Test
    public void register() {
        //given
        String orgUrl = "http://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(orgUrl);

        when(convertedUrlRepository.existsByOrgUrl(orgUrl))
                .thenReturn(false);

        //when
        convertedUrlService.register(convertedUrl);

        //then
        verify(convertedUrlRepository, times(1)).save(convertedUrl);
        verify(stringGenerator, times(1)).generate(any());

    }

    @Test
    public void register_when_already_exist_url() {
        //given
        String orgUrl = "http://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(orgUrl);

        when(convertedUrlRepository.existsByOrgUrl(orgUrl))
                .thenReturn(true);

        when(convertedUrlRepository.findByOrgUrl(orgUrl))
                .thenReturn(Optional.of(convertedUrl));

        //when
        convertedUrlService.register(convertedUrl);

        //then
        verify(convertedUrlRepository, times(1)).updateRequestCnt(any());

    }



}