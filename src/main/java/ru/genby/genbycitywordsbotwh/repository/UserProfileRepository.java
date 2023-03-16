package ru.genby.genbycitywordsbotwh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.genby.genbycitywordsbotwh.model.City;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;
import ru.genby.genbycitywordsbotwh.repository.user.UserRepoNative;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileData, Integer>, UserRepoNative<UserProfileData> {
}
