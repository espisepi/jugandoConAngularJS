
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Opinion;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Integer> {

	//Las queries no se utilizan de momento
	@Query("select o from Item i join i.opinions o where o.actor.id=?1")
	Collection<Opinion> findOpinableItemByActor(int actorId);

	//Las queries no se utilizan de momento
	@Query("select o from Course c join c.opinions o where o.actor.id=?1")
	Collection<Opinion> findOpinableCourseByActor(int actorId);

}
