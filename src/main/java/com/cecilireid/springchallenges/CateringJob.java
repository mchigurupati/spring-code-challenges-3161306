package com.cecilireid.springchallenges;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class CateringJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String phoneNumber;
    private String email;
    private String menu;
    private int noOfGuests;
    @Enumerated(EnumType.STRING)
    private Status status;
}
