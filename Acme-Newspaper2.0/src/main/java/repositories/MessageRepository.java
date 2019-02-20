
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m where messageFolder.id=?1")
	Collection<Message> findMessagesByMessageFolder(int messageFolderId);

	//Me devuelve si el mensaje tiene palabras taboo para ser considerado mesaje spam)
	@Query("select m from Message m where (m.subject like %?1% or m.body like %?1%) and m.id=?2")
	Message findMessageWithTabooWord(String tabooWord, int messageId);

}
