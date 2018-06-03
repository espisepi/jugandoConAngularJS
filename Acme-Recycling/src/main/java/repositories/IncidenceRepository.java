
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Incidence;

@Repository
public interface IncidenceRepository extends JpaRepository<Incidence, Integer> {

	//Incidencias de un reciclador
	@Query("select i from Incidence i where i.recycler.id=?1")
	Collection<Incidence> findIncidencesByRecycler(int recyclerId);

	//Buscador por palabras tabú
	@Query("select c from Incidence c where c.title like %?1% or c.reasonWhy like %?1% or c.comment like %?1%")
	Collection<Incidence> findIncidencesWithTabooWord(String tabooWord);
	
	//Incidencias no resultas
	@Query("select i from Incidence i where i.resolved = false")
	Collection<Incidence> findIncidenceNoResolved();
	
	//Incidencias resueltas
	@Query("select i from Incidence i where i.resolved = true")
	Collection<Incidence> findIncidenceResolved();
	
	

}
