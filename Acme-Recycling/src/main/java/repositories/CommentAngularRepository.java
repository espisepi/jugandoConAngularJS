
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.CommentAngular;

@Repository
public interface CommentAngularRepository extends JpaRepository<CommentAngular, Integer> {

}
