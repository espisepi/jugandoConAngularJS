
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Legislation;

@Repository
public interface LegislationRepository extends JpaRepository<Legislation, Integer> {

}
