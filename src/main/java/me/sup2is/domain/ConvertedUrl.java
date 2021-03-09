package me.sup2is.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertedUrl {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String orgUrl;
    private String shorteningUrl;
    private int requestCnt;

    @Version
    private int version;

    public static ConvertedUrl createConvertedUrl(String orgUrl) {
        ConvertedUrl convertedUrl = new ConvertedUrl();
        convertedUrl.orgUrl = orgUrl;
        convertedUrl.requestCnt = 1;
        return convertedUrl;
    }

    public void bindShorteningUrl(String shorteningUrl){
        this.shorteningUrl = shorteningUrl;
    }

    public void increaseRequestCnt() {
        this.requestCnt ++;
    }

}
