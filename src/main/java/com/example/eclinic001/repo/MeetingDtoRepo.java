package com.example.eclinic001.repo;

import com.example.eclinic001.model.webApi.video_conferencing_api.MeetingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingDtoRepo extends JpaRepository<MeetingDto, Long> {
}
