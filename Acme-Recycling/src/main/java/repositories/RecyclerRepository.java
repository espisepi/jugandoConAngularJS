
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;
import domain.Recycler;

@Repository
public interface RecyclerRepository extends JpaRepository<Recycler, Integer> {

	@Query("select r from Recycler r where r.userAccount.id = ?1")
	Recycler findByUserAccountId(int id);

	//Query que me devuelve la puntuación dado un recycler
	@Query("select sum(i.value) from Recycler r join r.items i where r.id=?1")
	Double puntuationOfRecycler(int recyclerId);

	//Query que me devuelve todos los comentarios dado un reciclador
	@Query("select c from Recycler r join r.comments c where r.id=?1")
	Collection<Comment> findAllCommentsByRecycler(int recyclerId);

	//Me devuelve el reciclador que ha hecho un comentario concreto
	@Query("select r from Recycler r join r.comments c where c.id=?1")
	Recycler findRecyclerByComment(int commentId);

	//Me hace falta para el delete de course para el recycler
	@Query("select r from Recycler r join r.courses c where c.id=?1")
	Collection<Recycler> findRecyclerByCourse(int courseId);

	//Saco al recyclador que tiene esa solicitud
	@Query("select r from Recycler r join r.items i where i.request.id=?1")
	Recycler findRecyclerByRequest(int requestId);

}
