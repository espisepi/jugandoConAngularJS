
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ConfigurationSystem;

@Repository
public interface ConfigurationSystemRepository extends JpaRepository<ConfigurationSystem, Integer> {
	
	@Query("select c from ConfigurationSystem c")
	ConfigurationSystem findConfigurationSystem();

}
