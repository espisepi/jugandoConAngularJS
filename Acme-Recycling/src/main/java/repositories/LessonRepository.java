
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

	@Query("select c.lessons from Course c where c.id=?1")
	Collection<Lesson> findLessonsByCourseId(int courseId);
}
