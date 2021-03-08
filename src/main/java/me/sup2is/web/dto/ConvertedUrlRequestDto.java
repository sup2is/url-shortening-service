package me.sup2is.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.sup2is.domain.ConvertedUrl;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertedUrlRequestDto {

    @NotNull @URL(message = "Unable to shorten that link. It is not a valid url.")
    private String orgUrl;

    public ConvertedUrl toConvertedUrl() {
        return ConvertedUrl.createConvertedUrl(orgUrl);
    }

}
