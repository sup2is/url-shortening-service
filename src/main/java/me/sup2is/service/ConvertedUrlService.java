package me.sup2is.service;

import lombok.RequiredArgsConstructor;
import me.sup2is.domain.ConvertedUrl;
import me.sup2is.exception.ConvertedUrlNotFoundException;
import me.sup2is.repository.ConvertedUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConvertedUrlService {

    private final ConvertedUrlRepository convertedUrlRepository;
    private final StringGenerator<Long> stringGenerator;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public ConvertedUrl findByShorteningUrl(String shorteningUrl) {
        return convertedUrlRepository.findByShorteningUrl(shorteningUrl)
            .orElseThrow(() -> new ConvertedUrlNotFoundException(shorteningUrl + " is invalid."));
    }

}
