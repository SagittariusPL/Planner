package pl.straszewski.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.straszewski.model.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
