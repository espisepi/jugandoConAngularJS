
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Assesment;

@Repository
public interface AssesmentRepository extends JpaRepository<Assesment, Integer> {

	@Query("select r.assesment from Carrier c join c.requests r where c.id=?1 and r.assesment!=null")
	Collection<Assesment> findByCarrierId(int id);

}
