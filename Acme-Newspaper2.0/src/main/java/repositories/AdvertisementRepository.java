
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	//Me devuelve los avisos con alguna palabra tabu en el título
	@Query("select a from Advertisement a where a.title like %?1%")
	Collection<Advertisement> findAdvertisementWithTabooWord(String tabooWord);

	//Me devuelve todos los avisos dado un periódico concreto
	@Query("select s from Newspaper n join n.advertisements s where n.id=?1")
	List<Advertisement> findAdvertisementsByNewspaper(int newspaperId);

}
