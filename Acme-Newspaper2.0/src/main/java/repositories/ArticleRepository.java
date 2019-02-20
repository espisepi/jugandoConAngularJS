
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.publishedMoment!=null and a.newspaper.open=true and a.newspaper.publicationDate!=null")
	Collection<Article> findArticlesByKeyword(String keyWord);

	@Query("select distinct a from Subscription s join s.newspaper.articles a where s.customer.id=?1 and (a.title like %?2% or a.summary like %?2% or a.body like %?2%)")
	Collection<Article> findArticlesPrivateBySubcriptionCustomer(int customerId, String keyWord);

	@Query("select a from Article a where a.newspaper.id=?1")
	Collection<Article> findArticlesByNewspaperId(int newspaperId);

	@Query("select a from Newspaper n join n.articles a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%)and (n.publisher.id=?2 or (n.open=true and n.publicationDate!=null))")
	Collection<Article> findArticlesForUser(String keyWord, int userId);

	@Query("select a from Article a where a.writer.id=?1 and a.publishedMoment!=null")
	Collection<Article> findArticlesPublishedByUserId(int newspaperId);

	@Query("select a from Article a where a.writer.id=?1 and a.publishedMoment!=null and a.newspaper.open=true")
	Collection<Article> findArticlesPublishedByUserIdAndNotPrivate(int newspaperId);

	@Query("select a from Article a where a.writer.id=?1")
	Collection<Article> findArticlesByUserId(int userId);

	@Query("select a.summary from Article a where a.id=?1")
	String findSummaryByArticleId(int articleId);

	//Me devuelve los articulos con alguna palabra en el título, cuerpo o resumen (para las palabras tabú)
	@Query("select a from Article a where a.title like %?1% or a.body like %?1% or a.summary like %?1%")
	Collection<Article> findArticleWithTabooWord(String tabooWord);

	@Query("select a from Article a where a.writer.id=?1 and a.publishedMoment!=null and a.newspaper.publicationDate!=null and a.newspaper.open=true")
	Collection<Article> findArticlesIfNewspaperPublishedByUserId(int userId);

	//Query para los customer cuando muestren los artículos de un periódico concreto
	@Query("select a from Article a where a.newspaper.id=?1 and a.publishedMoment!=null and a.draftMode=false and a.newspaper.publicationDate!=null")
	Collection<Article> findAllArticlesByCustomerNewspaperList(int newspaperId);
	//buscador para los articulos que esten en modo final
	@Query("select a from Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%)and a.draftMode=false")
	Collection<Article> findAllArticlesByAdmin(String keyWord);

	@Query("select c from Article c where c.writer.id = ?1 and c.newspaper.open =true and c.newspaper.publicationDate != null")
	Collection<Article> findArticlesOfUserWhatIsOpen(int userId);

	@Query("select c from Article c where c.writer.id = ?1 and c.draftMode = false")
	Collection<Article> findArticlesOfUserWhatNotIsDraftMode(int userId);

	@Query("select a from Subscription s join s.newspaper.articles a where a.writer.id=?1 and s.customer.id =?2")
	Collection<Article> findArticlesByUserSuscribed(int userId, int customerId);

	@Query("select a from Article a where a.newspaper.publicationDate!=null and a.writer.id=?1 and a.newspaper.open=true")
	Collection<Article> findArticlesByUserOpenAndFinalMode(int userId);

	@Query("select a from Article a where a.draftMode = false")
	Collection<Article> findArticlesFinalMode();

	//Encontrar los artículos en modo final dado un periódico
	@Query("select a from Article a where a.newspaper.id=?1 and a.draftMode = false")
	Collection<Article> findArticlesFinalModeByNewspaper(int newspaperId);

	//Encontrar los artículos en modo final dado un writer
	@Query("select a from Article a where a.writer.id=?1 and a.draftMode=false and a.newspaper.open=true")
	Collection<Article> findArticlesFinalModeByWriter(int writerId);

}
