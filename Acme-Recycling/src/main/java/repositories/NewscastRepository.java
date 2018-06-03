
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;
import domain.Newscast;

@Repository
public interface NewscastRepository extends JpaRepository<Newscast, Integer> {

	//Me devuelve todos los comentarios que tiene una noticia
	@Query("select c from Newscast n join n.comments c where n.id=?1 and c.commentTo=null")
	Collection<Comment> findCommentsByNewscast(int newscastId);

	//Me devuelve todas las noticias en order descendiente
	@Query("select n from Newscast n order by creationDate desc")
	Page<Newscast> findAllNewscastsInDescOrder(Pageable pageable);

	//Me devuelve las noticias con alguna palabra en el título, 
	//o content(para las palabras tabú)
	@Query("select c from Newscast c where c.title like %?1% or c.content like %?1%")
	Collection<Newscast> findNewscastsWithTabooWord(String tabooWord);

	//Dado un comentario devuelveme su noticia
	@Query("select n from Newscast n join n.comments c where c.id=?1")
	Newscast findNewscastByComment(int commentId);
}
