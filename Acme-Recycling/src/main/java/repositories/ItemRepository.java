
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	@Query("select i from Item i where i.recycler.id=?1")
	Collection<Item> findItemsByRecycler(int recyclerId);

	//Item con request FINISHED de un recycler
	@Query("select i from Item i where i.recycler.id = ?1 and i.request.status = 'FINISHED'")
	Collection<Item> findItemsWithFinishedRequest(int recyclerId);

	@Query("select i from Item i join i.opinions o where o.actor.id=?1")
	Collection<Item> findToOpineByActorId(int actorId);

}
