
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select c from Customer c where c.userAccount.id=?1")
	Customer findByUserAccountId(int userAccountId);

	//customer que estan subcritos en el volumen 
	@Query("select u from Customer u join u.underwrites c where c.volume.id=?1")
	Collection<Customer> customerWithUnderwriteToVolumeId(int volumeId);

}
