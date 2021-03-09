package me.sup2is.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.sup2is.domain.ConvertedUrl;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ConvertedUrlRequestDto {

    @NotBlank
    @URL(message = "Unable to shorten that link. It is not a valid url.")
    private String orgUrl;

    public ConvertedUrl toConvertedUrl() {
        return ConvertedUrl.createConvertedUrl(orgUrl);
    }

}
