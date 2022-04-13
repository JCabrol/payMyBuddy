package com.openclassrooms.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Message_To_Admin")
public class MessageToAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_Id")
    private int messageId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message")
    private String message;

    @Column(name = "email")
    private String email;

    @Column(name = "first_Name")
    private String firstName;

    @Column(name = "last_Name")
    private String lastName;

    @Column(name = "new_Message")
    private Boolean newMessage = true;

    @Column(name = "date_Time")
    private LocalDateTime dateTime = LocalDateTime.now();


    public MessageToAdmin(String subject, String message, String email, String firstName, String lastName) {
        this.subject = subject;
        this.message = message;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
