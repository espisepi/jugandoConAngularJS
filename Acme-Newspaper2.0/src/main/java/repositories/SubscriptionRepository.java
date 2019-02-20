
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

	@Query("select s from Subscription s where s.newspaper.id=?1")
	Collection<Subscription> findSubscriptionByNewspaper(int newspaperId);
	
}

