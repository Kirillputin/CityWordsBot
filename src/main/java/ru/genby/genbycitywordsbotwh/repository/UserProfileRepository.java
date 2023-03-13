package ru.genby.genbycitywordsbotwh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;

public interface UserProfileRepository extends JpaRepository<UserProfileData, Integer> {
}
