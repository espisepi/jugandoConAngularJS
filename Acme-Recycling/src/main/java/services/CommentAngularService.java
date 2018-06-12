
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentAngularRepository;
import domain.CommentAngular;

@Service
@Transactional
public class CommentAngularService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private CommentAngularRepository	commentAngularRepository;


	// Supporting services ----------------------------------------------------

	//Constructor
	public CommentAngularService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public CommentAngular create() {
		CommentAngular result;

		result = new CommentAngular();

		return result;
	}

	public CommentAngular save(CommentAngular commentAngular) {
		Assert.notNull(commentAngular);

		commentAngular = this.commentAngularRepository.save(commentAngular);

		return commentAngular;
	}

	public CommentAngular findOne(final int commentAngularId) {
		CommentAngular result;

		result = this.commentAngularRepository.findOne(commentAngularId);

		return result;
	}

	public Collection<CommentAngular> findAll() {
		Collection<CommentAngular> result;

		result = this.commentAngularRepository.findAll();

		return result;
	}

	public void delete(final CommentAngular commentAngular) {
		Assert.notNull(commentAngular);

		this.commentAngularRepository.delete(commentAngular);
	}
}
