package com.mukku.shrinkurl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {

    @NotEmpty
    @Email
    private String user;

    @NotEmpty
    private String fullUrl;

    @NotEmpty
    private String shortUrl;

    @NotEmpty
    private int usage;
    
}
