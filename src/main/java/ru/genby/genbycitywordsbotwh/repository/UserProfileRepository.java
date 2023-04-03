package ru.genby.genbycitywordsbotwh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.genby.genbycitywordsbotwh.model.UserProfileEntity;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {
}
