
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCard;
import domain.Underwrite;

@Repository
public interface UnderwriteRepository extends JpaRepository<Underwrite, Integer> {

	@Query("select u.creditCard from Customer c join c.underwrites u where u.volume.id=?1 and c.id=?2")
	CreditCard credictcardByVolumenAndCustomer(int volumeId, int customerId);

}
