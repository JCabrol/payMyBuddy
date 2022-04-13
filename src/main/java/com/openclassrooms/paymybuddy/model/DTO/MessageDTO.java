package com.openclassrooms.paymybuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    @NotBlank(message = "Please indicate what is your message about")
    @Size(max = 80, message = "The subject can't be over 80 characters")
    private String subject;
    @NotBlank(message = "Please write a message before sending")
    @Size(max = 500, message = "Your message can't be over 500 characters")
    private String message;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Please indicate your email")
    @Email(message = "Your email is not valid")
    private String email;
    private String date;
    private boolean newMessage;
    private int id;
}
