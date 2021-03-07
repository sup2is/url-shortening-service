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
    private int requestCnt;

    public static ConvertedUrl createConvertedUrl(String orgUrl) {
        ConvertedUrl convertedUrl = new ConvertedUrl();
        convertedUrl.orgUrl = orgUrl;
        convertedUrl.requestCnt = 1;
        return convertedUrl;
    }

    public void bindShorteningUrl(String shorteningUrl){
        this.shorteningUrl = shorteningUrl;
    }

}
