
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewscastRepository;
import domain.Comment;
import domain.Editor;
import domain.Newscast;

@Service
@Transactional
public class NewscastService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private NewscastRepository	newscastRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private EditorService		editorService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private TabooWordService	tabooWordService;

	@Autowired
	private AdminService		adminService;

	//Importar la que pertenece a Spring
	@Autowired
	private Validator			validator;


	// Constructors -----------------------------------------------------------
	public NewscastService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Newscast create() {
		Newscast result;
		Collection<Comment> comments;
		Date creationDate;

		comments = new ArrayList<Comment>();
		result = new Newscast();
		creationDate = new Date();

		result.setComments(comments);
		result.setCreationDate(creationDate);

		return result;
	}

	public Newscast save(final Newscast newscast) {
		Newscast result;
		Assert.notNull(newscast);
		Date createdMoment;
		Editor principal;

		principal = this.editorService.findByPrincipal();

		createdMoment = new Date(System.currentTimeMillis() - 1000);
		newscast.setCreationDate(createdMoment);

		result = this.newscastRepository.save(newscast);

		if (newscast.getId() == 0)
			principal.getNews().add(result);

		return result;
	}

	public Newscast saveA(final Newscast newscast) {
		Newscast result;
		Assert.notNull(newscast);
		Date createdMoment;

		createdMoment = new Date(System.currentTimeMillis() - 1000);
		newscast.setCreationDate(createdMoment);

		result = this.newscastRepository.save(newscast);

		return result;
	}

	public Collection<Newscast> findAll() {
		Collection<Newscast> result;

		result = this.newscastRepository.findAll();
		return result;
	}

	public Newscast findOne(final int newId) {
		Assert.isTrue(newId != 0);

		Newscast result;

		result = this.newscastRepository.findOne(newId);
		return result;
	}

	public void delete(Newscast newscast) {
		Assert.notNull(newscast);
		Editor editor;

		editor = this.editorService.findByPrincipal();
		Assert.isTrue(editor.getNews().contains(newscast), "This new is  not yours");

		if (newscast.getComments().size() != 0)
			for (final Comment c : newscast.getComments())
				if (c.getCommentTo() == null)
					this.commentService.delete(c);

		editor.getNews().remove(newscast);
		newscast = this.findOne(newscast.getId());
		this.newscastRepository.delete(newscast);

	}
	public void deleteAdmin(Newscast newscast) {
		Assert.notNull(newscast);
		this.adminService.checkPrincipal();

		Editor editor;

		editor = this.editorService.findEditorByNew(newscast.getId());

		editor.getNews().remove(newscast);

		if (newscast.getComments().size() != 0)
			for (final Comment c : newscast.getComments())
				if (c.getCommentTo() == null)
					this.commentService.delete(c);

		editor.getNews().remove(newscast);
		newscast = this.findOne(newscast.getId());
		this.newscastRepository.delete(newscast);
	}
	//Other business methods---------------------------------------------------

	//	RECONSTRUCTOR

	public Newscast reconstruct(final Newscast newscast, final BindingResult bindingResult) {
		Newscast result;
		Newscast newBD;
		if (newscast.getId() == 0) {

			Collection<Comment> comments;
			comments = new ArrayList<Comment>();
			newscast.setComments(comments);
			result = newscast;
		} else {
			newBD = this.newscastRepository.findOne(newscast.getId());
			newscast.setId(newBD.getId());
			newscast.setVersion(newBD.getVersion());
			newscast.setCreationDate(newBD.getCreationDate());

			if (newscast.getComments() == null)
				newscast.setComments(new ArrayList<Comment>());
			else
				newscast.setComments(newBD.getComments());

			result = newscast;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Collection<Comment> findCommentsByNew(final int newId) {
		Collection<Comment> result;

		result = this.newscastRepository.findCommentsByNewscast(newId);

		return result;
	}

	//Para que los no autenticados solo vieran las 5 últimas noticias siempre.
	public Collection<Newscast> findAllNewsInDescOrder() {
		Collection<Newscast> result;
		final Page<Newscast> resPage;
		final Pageable pageable;

		pageable = new PageRequest(0, 5);
		resPage = this.newscastRepository.findAllNewscastsInDescOrder(pageable);
		result = resPage.getContent();
		return result;
	}

	//Para las palabrá tabú
	public Collection<Newscast> findNewsWithTabooWord(final String tabooWord) {
		Collection<Newscast> result;

		result = this.newscastRepository.findNewscastsWithTabooWord(tabooWord);

		return result;
	}

	//	//Para listar todas las noticias con palabras tabú
	public Set<Newscast> newWithTabooWord() {

		Set<Newscast> result;
		Collection<String> tabooWords;
		Iterator<String> it;

		result = new HashSet<>();
		tabooWords = this.tabooWordService.findTabooWordByName();
		it = tabooWords.iterator();
		while (it.hasNext())
			result.addAll(this.findNewsWithTabooWord(it.next()));

		return result;

	}

	public Newscast findNewByComment(final int commentId) {
		Newscast result;

		result = this.newscastRepository.findNewscastByComment(commentId);

		return result;
	}
	public void flush() {
		this.newscastRepository.flush();
	}

}
