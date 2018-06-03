package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.TabooWord;

@Repository
public interface TabooWordRepository extends JpaRepository<TabooWord, Integer>{
	
	@Query("select w.name from TabooWord w")
	Collection<String> findTabooWordByName();

}
