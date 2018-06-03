
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Opinable;

@Repository
public interface OpinableRepository extends JpaRepository<Opinable, Integer> {

	@Query("select o from Opinable o join o.opinions opinion where opinion.id=?1")
	Opinable findByOpinionId(int opinionId);

	@Query("select o from Opinable o where o.id=?1")
	Opinable findOneManual(Integer opinableId);

	@Query("select o from Opinable o join o.opinions opinion where opinion.actor.id=?1")
	Collection<Opinable> findByActorId(int actorId);

}
