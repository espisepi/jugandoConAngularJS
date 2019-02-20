
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.Volume;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Integer> {

	@Query("select u.volumes from User u where u.id=?1")
	Collection<Volume> myVolumes(int idUser);
	//seleccionar las newspapers de un  volumen
	@Query("select v.newspapers from Volume v where v.id=?1")
	Collection<Newspaper> volumesNewspaper(int volumenid);

	@Query("select u.volume from Customer c join c.underwrites u where c.id=?1")
	Collection<Volume> volumesWithUnderwriteOneCustomer(int customerId);

	@Query("select v from Volume v join v.newspapers n where n.id=?1")
	Collection<Volume> findByNewspaperId(int newspaperId);

}
