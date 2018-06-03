
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Comment;
import domain.Newscast;
import domain.Recycler;

@Service
@Transactional
public class CommentService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private CommentRepository	commentRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private RecyclerService		recyclerService;
	@Autowired
	private NewscastService		newService;
	//Importar la que pertenece a Spring
	@Autowired
	private Validator			validator;


	// Constructors -----------------------------------------------------------
	public CommentService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Comment create() {
		Comment result;
		Collection<Comment> replys;

		Date createdMoment;

		result = new Comment();
		replys = new ArrayList<Comment>();
		createdMoment = new Date();

		result.setCreatedMoment(createdMoment);
		result.setReplys(replys);

		return result;
	}

	public Comment create(final Comment comment) {
		Comment result;
		Collection<Comment> replys;

		Date createdMoment;

		result = new Comment();
		replys = new ArrayList<Comment>();
		createdMoment = new Date();

		result.setCreatedMoment(createdMoment);
		result.setReplys(replys);
		result.setCommentTo(comment);

		return result;
	}

	public Comment save(final Comment comment) {
		Comment result;
		Assert.notNull(comment);
		Date createdMoment;
		Recycler principal;
		Collection<Comment> mycomments;

		principal = this.recyclerService.findByPrincipal();

		createdMoment = new Date(System.currentTimeMillis() - 1000);
		comment.setCreatedMoment(createdMoment);

		if (comment.getId() != 0) {
			mycomments = principal.getComments();
			Assert.isTrue(mycomments.contains(comment));
		}

		result = this.commentRepository.save(comment);
		//Le a“ado al reciclador que est∑ logueado el comentario
		if (comment.getId() == 0)
			principal.getComments().add(result);

		if (comment.getCommentTo() != null) {
			Newscast new_;

			new_ = this.newService.findNewByComment(result.getCommentTo().getId());

			new_.getComments().add(result);
		}

		return result;
	}

	public Collection<Comment> findAll() {
		Collection<Comment> result;

		result = this.commentRepository.findAll();
		return result;
	}

	public Comment findOne(final int commentId) {
		Assert.isTrue(commentId != 0);

		Comment result;

		result = this.commentRepository.findOne(commentId);
		return result;
	}

	public void delete(final Comment comment) {
		Assert.notNull(comment);
		Assert.isTrue(comment.getId() != 0);
		Newscast new_;
		final Recycler recycler;

		if (comment.getReplys() != null)
			for (final Comment c : comment.getReplys())
				this.delete(c);
		new_ = this.findNewbyComment(comment);
		recycler = this.recyclerService.findRecyclerByComment(comment.getId());
		new_.getComments().remove(comment);
		recycler.getComments().remove(comment);

		Comment commentBd;
		commentBd = this.commentRepository.findOne(comment.getId());
		this.commentRepository.delete(commentBd);
	}
	//Other business methods---------------------------------------------------

	//	RECONSTRUCTOR
	public Comment reconstruct(final Comment comment, final BindingResult bindingResult) {
		Comment result;
		Comment CommentBd;

		if (comment.getId() == 0) {
			Collection<Comment> replys;

			Date moment;

			result = comment;
			moment = new Date(System.currentTimeMillis() - 1000);
			replys = new ArrayList<Comment>();
			result.setReplys(replys);
			result.setCreatedMoment(moment);
		}

		else {
			CommentBd = this.commentRepository.findOne(comment.getId());
			comment.setId(CommentBd.getId());
			comment.setVersion(CommentBd.getVersion());
			comment.setCreatedMoment(CommentBd.getCreatedMoment());
			comment.setReplys(CommentBd.getReplys());

			result = comment;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Newscast findNewbyComment(final Comment comment) {
		Newscast new_;
		new_ = this.commentRepository.findNewbyComment(comment.getId());
		return new_;
	}
	public void flush() {
		this.commentRepository.flush();
	}
	//	public Comment reconstruct(final Comment comment, final BindingResult binding) {
	//		Comment result;
	//		Comment commentBD;
	//		User userPrincipal;
	//		if (comment.getId() == 0) {
	//			Collection<Comment> replys;
	//			Date moment;
	//
	//			result = comment;
	//			moment = new Date(System.currentTimeMillis() - 1000);
	//			replys = new ArrayList<Comment>();
	//			userPrincipal = this.userService.findByPrincipal();
	//			result.setUser(userPrincipal);
	//			result.setReplys(replys);
	//			result.setWrittenMoment(moment);
	//		} else {
	//			commentBD = this.commentRepository.findOne(comment.getId());
	//			comment.setId(commentBD.getId());
	//			comment.setVersion(commentBD.getVersion());
	//			comment.setUser(commentBD.getUser());
	//			comment.setReplys(commentBD.getReplys());
	//			comment.setWrittenMoment(commentBD.getWrittenMoment());
	//			result = comment;
	//		}
	//		this.validator.validate(result, binding);
	//		return result;
	//	}

}
