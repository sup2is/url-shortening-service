package me.sup2is.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.sup2is.domain.ConvertedUrl;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ConvertedUrlRequestDto {

    @NotNull @URL
    private String orgUrl;

    public ConvertedUrl toConvertedUrl() {
        return ConvertedUrl.createConvertedUrl(orgUrl);
    }

}
