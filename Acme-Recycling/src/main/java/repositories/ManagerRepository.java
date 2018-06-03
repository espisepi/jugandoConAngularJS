
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	Manager findByUserAccountId(int id);

	@Query("select m from Manager m join m.incidences i where i.id = ?1")
	Manager findManagerByIncidence(int incidenceId);

	@Query("select m from Manager m join m.requests r where r.id=?1")
	Manager findByRequestId(int requestId);
}
