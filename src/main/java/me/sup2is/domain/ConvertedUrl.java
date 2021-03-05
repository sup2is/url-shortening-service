package me.sup2is.domain;


import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertedUrl {

    @Id @GeneratedValue
    private long id;

    private String orgUrl;
    private String shorteningUrl;
    private int duplicateConversionRequestCnt;

    public static ConvertedUrl createConvertedUrl(String orgUrl) {
        ConvertedUrl convertedUrl = new ConvertedUrl();
        convertedUrl.orgUrl = orgUrl;
        return convertedUrl;
    }
}
