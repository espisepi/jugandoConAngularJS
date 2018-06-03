
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Buy;

@Repository
public interface BuyRepository extends JpaRepository<Buy, Integer> {

	@Query("select b from Buy b where b.material.id = ?1")
	Collection<Buy> findBuysOfMaterial(int materialId);
}
