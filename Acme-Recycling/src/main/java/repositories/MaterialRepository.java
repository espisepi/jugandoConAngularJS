
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

	@Query("select m from Material m where m.quantity=0")
	Collection<Material> allMaterialWithoutQuantity();

}
