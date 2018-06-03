
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

	@Query("select c from Course c where c.draftMode=false")
	Collection<Course> findCoursesNoAuthenticate();

	@Query("select b.courses from Buyer b where b.id=?1")
	Collection<Course> findCoursesCreatedByBuyer(int buyerId);

	@Query("select r.courses from Recycler r join r.courses c where c.realisedMoment<current_date and r.id=?1")
	Collection<Course> coursesOfRecyclerFinished(int recyclerId);

	@Query("select c from Course c where c.realisedMoment>current_date and c.minimumScore<?1 and c.draftMode=false and c.lessons.size>0")
	Collection<Course> coursesAvailables(int puntuation);

	@Query("select c from Course c join c.materials m where m.id =?1")
	Collection<Course> findCoursesOfMaterial(int materialId);

	@Query("select c from Course c join c.opinions o where o.actor.id=?1")
	Collection<Course> findToOpineByActorId(int actorId);

	@Query("select c from Course c join c.lessons l where l.id=?1")
	Course findCourseByLessonId(int lessonId);

}
