package me.sup2is.service;

import lombok.RequiredArgsConstructor;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.exception.ConvertedUrlNotFoundException;
import me.sup2is.repository.ConvertedUrlRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvertedUrlService {

    private final ConvertedUrlRepository convertedUrlRepository;
    private final StringGenerator<Long> stringGenerator;

    public ConvertedUrl findByOrgUrl(String orgUrl) {
        return convertedUrlRepository.findByOrgUrl(orgUrl)
                .orElseThrow(() -> new ConvertedUrlNotFoundException("Request url is not shortening"));
    }

    public void register(ConvertedUrl convertedUrl) {
        if(convertedUrlRepository.existsByOrgUrl(convertedUrl.getOrgUrl())) {
            ConvertedUrl findOne = findByOrgUrl(convertedUrl.getOrgUrl());
            convertedUrlRepository.updateRequestCnt(findOne.getId());
        } else {
            convertedUrlRepository.save(convertedUrl);
            convertedUrl.bindShorteningUrl(stringGenerator.generate(convertedUrl.getId()));
        }
    }

}
