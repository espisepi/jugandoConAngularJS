
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Carrier;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Integer> {

	@Query("select c from Carrier c where c.userAccount.id = ?1")
	Carrier findByUserAccountId(int id);
}
