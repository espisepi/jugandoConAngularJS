
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.LabelProduct;

@Repository
public interface LabelProductRepository extends JpaRepository<LabelProduct, Integer> {

	@Query("select distinct(i.labelProduct) from Item i")
	Collection<LabelProduct> labelProductsOfAllProducts();
}
