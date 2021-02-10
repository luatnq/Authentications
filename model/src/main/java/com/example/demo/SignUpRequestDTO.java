package com.example.demo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDTO {
    @NotNull
    @Size(min = 6, max = 20, message = "Username is from 6 to 20 characters!")
//    @JsonDeserialize(using = ToLowerCaseDeserializer.class)
    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    @Size(min = 6, max = 30, message = "Wrong format email!")
    private String email;

    @NotBlank
    @Size(min = 6,max = 50, message = "Password is from 6 to 50 characters!")
    private String password;
}
