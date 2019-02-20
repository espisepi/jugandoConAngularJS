
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageFolder;

@Repository
public interface MessageFolderRepository extends JpaRepository<MessageFolder, Integer> {
	
	@Query("select m from MessageFolder m where m.actor.id = ?1")
	Collection<MessageFolder> findMessageFolderByActor(int actorId);
	
	@Query("select m from MessageFolder m where m.name=?1 and m.actor.id = ?2")
	MessageFolder findMessageFolderByNameAndActor(String name, int actorId);
	
	@Query("select m.name from MessageFolder m where m.actor.id = ?1")
	Collection<String> findMessageFolderNameByActor(int actorId);
	
	
	

}
