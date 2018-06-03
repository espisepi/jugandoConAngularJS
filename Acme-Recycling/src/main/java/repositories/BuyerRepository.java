
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Buy;
import domain.Buyer;
import domain.Material;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

	@Query("select b from Buyer b where b.userAccount.id = ?1")
	Buyer findByBuyerAccountId(int id);

	//Me hace falta para el delete de course para el buyer
	@Query("select b from Buyer b join b.courses c where c.id=?1")
	Buyer findBuyerByCourse(int buyerId);

	@Query("select b from Buyer b join b.buys bs where bs.id=?1")
	Buyer findBuyerOfBuy(int buyId);

	@Query("select b from Buyer b where b.finder.id=?1")
	Buyer findBuyerOfFinder(int finderId);

	//Me devuelve todos los materiales que ha comprado un buyer 
	@Query("select c.material from Buyer b join b.buys c where b.id=?1")
	Collection<Material> findAllMaterialsBuyByABuyer(int buyerId);

	//Me devuelve todas las compras que ha comprado un buyer 
	@Query("select c from Buyer b join b.buys c where b.id=?1")
	Collection<Buy> findAllBuysByABuyer(int buyerId);

}
