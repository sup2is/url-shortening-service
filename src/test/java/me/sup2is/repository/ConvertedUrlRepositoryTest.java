package me.sup2is.repository;

import me.sup2is.domain.ConvertedUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ConvertedUrlRepositoryTest {

    @Autowired
    ConvertedUrlRepository convertedUrlRepository;

    @Test
    public void save_and_find_by_org_url() {
        //given
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl("http://localhost/test");

        //when
        convertedUrlRepository.save(convertedUrl);

        //then
        ConvertedUrl findOne = convertedUrlRepository.findByOrgUrl(convertedUrl.getOrgUrl()).get();
        assertEquals(convertedUrl, findOne);
    }

    @Test
    public void exist_by_org_url_when_first_request() {
        //given
        String url = "http://localhost/test";

        //when
        //then
        boolean result = convertedUrlRepository.existsByOrgUrl(url);
        assertFalse(result);
    }

    @Test
    public void exist_by_org_url_when_duplicate_request() {
        //given
        String url = "http://localhost/test";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl(url);
        convertedUrlRepository.save(convertedUrl);

        //when
        //then
        boolean result = convertedUrlRepository.existsByOrgUrl(url);
        assertTrue(result);
    }

    @Test
    public void find_by_shortening_url() {
        //given
        String shorteningUrl = "AABBCC";
        ConvertedUrl convertedUrl = ConvertedUrl.createConvertedUrl("http://localhost/test");
        convertedUrl.bindShorteningUrl(shorteningUrl);
        convertedUrlRepository.save(convertedUrl);

        //when
        ConvertedUrl findOne = convertedUrlRepository.findByShorteningUrl(shorteningUrl).get();

        //then
        assertEquals(convertedUrl, findOne);
    }

}