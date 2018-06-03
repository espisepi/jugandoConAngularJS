
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Item;
import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select i from Item i where i.request=null")
	Collection<Item> findItemsNonRequest();

	@Query("select m.requests from Manager m where m.id=?1")
	Collection<Request> findByActorId(int id);

	@Query("select c.requests from Carrier c where c.id=?1")
	Collection<Request> findByCarrierId(int id);

	@Query("select i from Item i where i.request.id=?1")
	Item findItemByRequestId(int requestId);

	@Query("select r from Request r where r.assesment.id=?1")
	Request findByAssessmentId(int assessmentId);

}
