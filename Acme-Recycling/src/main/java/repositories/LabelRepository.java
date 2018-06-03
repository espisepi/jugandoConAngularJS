
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {

}
