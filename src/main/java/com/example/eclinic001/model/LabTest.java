package com.example.eclinic001.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LabTest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String testType;
    private String result;
    private String description;
    private String labName;
    private String scientistName;
//    @ManyToOne
//    private Appointments appointments;
}
