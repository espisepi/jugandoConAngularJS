
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from Chirp c join c.user.followers u where u.id=?1")
	Collection<Chirp> getChirpsOfMyFollowers(int userId);

	@Query("select c from Chirp c where c.user.id=?1")
	Collection<Chirp> findAllChirpsByUserId(int userId);

	//Me devuelve los chirp con alguna palabra en el título, cuerpo o resumen (para las palabras tabú)
	@Query("select c from Chirp c where c.title like %?1% or c.description like %?1%")
	Collection<Chirp> findChirpWithTabooWord(String tabooWord);

}
