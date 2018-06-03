
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.CleanPoint;

@Repository
public interface CleanPointRepository extends JpaRepository<CleanPoint, Integer> {

}
