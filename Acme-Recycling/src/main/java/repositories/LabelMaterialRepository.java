
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.LabelMaterial;

@Repository
public interface LabelMaterialRepository extends JpaRepository<LabelMaterial, Integer> {

	@Query("select distinct(m.labelMaterial) from Material m")
	Collection<LabelMaterial> labelMaterialsOfAllMaterials();
}
