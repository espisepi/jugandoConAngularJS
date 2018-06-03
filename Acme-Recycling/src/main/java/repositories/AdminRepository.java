
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;
import domain.Editor;
import domain.Item;
import domain.LabelProduct;
import domain.Material;
import domain.Newscast;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	@Query("select a from Admin a where a.userAccount.id = ?1")
	Admin findByUserAccountId(int id);

	//QUERY I Las noticias que contengan más comentarios.
	@Query("select c from Newscast c where c.comments.size=(select max(t.comments.size) from Newscast t)")
	Collection<Newscast> findNewWithMoreComments();

	//QUERY II Los redactores con mayor número de noticias redactadas.
	@Query("select e from Editor e where e.news.size=(select max(n.news.size) from Editor n)")
	Collection<Editor> findEditorsWithMoreNewsRedacted();

	//QUERY III Las 5 categorías de productos con más productos asociados
	@Query("select l from LabelProduct l where l.items.size!=0 order by l.items.size desc")
	Page<LabelProduct> findTop5LabelProducts(Pageable pageable);

	//QUERY IV La media, el mínimo, el máximo y la desviación típica de productos manejados por los manager.
	@Query("select avg(b.courses.size),min(b.courses.size),max(b.courses.size),stddev(b.courses.size) from Buyer b")
	Double[] avgMinMaxAndStddevOfCoursesByBuyer();

	//QUERY V La media de la solicitudes con el estado Finalizadas
	@Query("select count(i)*1.0/(select count(st) from Incidence st) from Incidence i where i.resolved=true")
	Double avgOfIncidencesResolved();

	//QUERY VI La media de recicladores que han reciclado al menos un producto.
	@Query("select avg(r.items.size) from Recycler r where r.items.size>0")
	Double avgOfRecyclerWithAtLeastOneProduct();

	//QUERY VII La media de usuarios baneados en el sistema.
	//TODO: POR HACER A FALTA DE LEU
	@Query("select count(a)*1.0/(select count(ta) from Actor ta) from Actor a where a.userAccount.activated=false")
	Double avgOfUsersBanned();

	//QUERY VIII La media, el mínimo, el máximo y la desviación típica de comentarios por noticias.
	@Query(" select avg(n.comments.size),min(n.comments.size),max(n.comments.size),stddev(n.comments.size) from Newscast n")
	Double[] avgMinMaxAndStddevOfCommentsByNews();

	//QUERY IX Items que se han subido al sistema en el último mes.
	@Query("select i from Item i where i.publicationMoment >= ?1 order by i.publicationMoment desc")
	Collection<Item> findLatestItems(Date since);

	//QUERY X Nombre del reciclador y título del Item que más valor tiene del sistema.
	@Query("select r.name,i.title from Recycler r join r.items i where i.value=(select max(it.value) from Item it)group by r.id")
	String[] nameTitleRecyclerWithItemMostValue();

	//QUERY XI La media, el mínimo, el máximo y la desviación típica peticiones por manager.
	@Query("select avg(m.requests.size),min(m.requests.size),max(m.requests.size),stddev(m.requests.size) from Manager m")
	Double[] avgMinMaxAndStddevOfRequestByManager();

	//QUERY XII Los 3 materiales más demandaos
	@Query("select m from Material m where m.buys.size!=0 order by m.buys.size desc")
	Page<Material> findTop3Materials(Pageable pageable);

	//QUERY XIII La media de transportistas que han tenido al menos una solicitud frente a los que no han tenido ninguna.
	@Query("select (select count(c) from Carrier c where c.requests.size>=1)*1.0/count(ce) from Carrier ce where ce.requests.size=0")
	Double ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest();
}
