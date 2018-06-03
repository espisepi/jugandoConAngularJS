
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Editor;
import domain.Newscast;
import domain.Opinion;
import forms.EditorForm;

@Service
@Transactional
public class EditorService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private EditorRepository		editorRepository;

	@Autowired
	private Validator				validator;

	@Autowired
	private MessageFolderService	messageFolderService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public EditorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Editor create() {
		Editor result;
		UserAccount userAccount;
		Authority authority;
		final Collection<Newscast> news;
		final Collection<Opinion> opinions;

		result = new Editor();
		userAccount = new UserAccount();
		authority = new Authority();
		news = new ArrayList<>();
		opinions = new ArrayList<>();

		authority.setAuthority(Authority.EDITOR);
		userAccount.addAuthority(authority);
		userAccount.setActivated(true);
		result.setUserAccount(userAccount);
		result.setNews(news);
		result.setOpinions(opinions);

		return result;
	}

	public Collection<Editor> findAll() {
		Collection<Editor> result;
		result = this.editorRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Editor findOne(final int editorId) {
		Assert.isTrue(editorId != 0);
		Editor result;
		result = this.editorRepository.findOne(editorId);
		return result;
	}

	public Editor save(final Editor editor) {
		Assert.notNull(editor);
		Editor result;
		Md5PasswordEncoder encoder;
		String passwordHash;

		if (editor.getId() == 0) {
			encoder = new Md5PasswordEncoder();
			passwordHash = encoder.encodePassword(editor.getUserAccount().getPassword(), null);
			editor.getUserAccount().setPassword(passwordHash);
		}
		result = this.editorRepository.save(editor);
		Assert.notNull(result);

		if (editor.getId() == 0)
			this.messageFolderService.createDefaultMessageFolder(result);

		return result;
	}

	//Delete	------------------------------------------------------------
	public void delete(final Editor editor) {
		Assert.notNull(editor);
		Assert.isTrue(editor.getId() != 0);
		this.editorRepository.delete(editor);
	}

	// Other business methods -------------------------------------------------
	public Editor findByPrincipal() {
		Editor result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.editorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public void checkPrincipal() {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.EDITOR);

		Assert.isTrue(authorities.contains(auth));
	}

	public boolean checkPrincipalBoolean() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.EDITOR);

		return (authorities.contains(auth));
	}

	public EditorForm reconstruct(final EditorForm editorForm, final BindingResult binding) {

		EditorForm result = null;
		Editor editorBD;
		editorBD = editorForm.getEditor();

		if (editorBD.getId() == 0) {
			UserAccount userAccount;
			Authority authority;
			final Collection<Newscast> news;
			final Collection<Opinion> opinions;

			userAccount = editorForm.getEditor().getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.EDITOR);
			userAccount.addAuthority(authority);
			editorForm.getEditor().setUserAccount(userAccount);
			userAccount.setActivated(true);
			news = new ArrayList<>();
			opinions = new ArrayList<>();
			editorForm.getEditor().setNews(news);
			editorForm.getEditor().setOpinions(opinions);
			result = editorForm;

		} else {

			editorBD = this.editorRepository.findOne(editorForm.getEditor().getId());
			editorForm.getEditor().setId(editorBD.getId());
			editorForm.getEditor().setVersion(editorBD.getVersion());
			editorForm.getEditor().setUserAccount(editorBD.getUserAccount());
			editorForm.getEditor().getUserAccount().setActivated(editorBD.getUserAccount().isActivated());
			editorForm.getEditor().setNews(editorBD.getNews());
			editorForm.getEditor().setOpinions(editorBD.getOpinions());

			result = editorForm;

		}

		this.validator.validate(result, binding);

		return result;

	}

	public void flush() {
		this.editorRepository.flush();
	}

	public Collection<Newscast> findAllNewByEditor() {
		Collection<Newscast> result;
		Editor principal;

		principal = this.findByPrincipal();
		result = this.editorRepository.findAllNewByEditor(principal.getId());

		return result;
	}

	public Editor findEditorByNew(final int newId) {
		Editor editor;

		editor = this.editorRepository.findEditorByNew(newId);

		return editor;
	}
}
