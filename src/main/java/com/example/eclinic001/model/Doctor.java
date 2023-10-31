package com.example.eclinic001.model;

import com.example.eclinic001.model.webApi.video_conferencing_api.MeetingDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Doctor extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    private SPECIALIZATION specialization;

    private String qualification;

    private int[] rating = new int[5];

    private boolean availability = true;

    @ManyToMany(mappedBy = "doctors")
    private Set<Patient> patientsSet = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @OneToMany (fetch = FetchType.EAGER)
    @JoinColumn(name = "meetingId")
    private List< MeetingDto> meetingDto;

    public int[] getRating() {
        return rating;
    }

    public void setRating(int[] rating) {
        this.rating = rating;
    }


}
