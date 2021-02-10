package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    @JsonProperty("uid")
    private String uid;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;
}
