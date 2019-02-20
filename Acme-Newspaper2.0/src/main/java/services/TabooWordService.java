package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.TabooWord;

import repositories.TabooWordRepository;

@Service
@Transactional
public class TabooWordService {
	
	// Managed repository -----------------------------------------------------
	@Autowired
	private TabooWordRepository	tabooWordRepository;
	
		
	// Supporting services ----------------------------------------------------
		
	@Autowired
	private AdminService	adminService;
	
	@Autowired
	private Validator	validator;
	
	// Constructors -----------------------------------------------------------
		
	public TabooWordService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	public TabooWord create(){
		this.adminService.checkPrincipal();
		TabooWord result;
		
		result = new TabooWord();
		result.setDefault_word(false);
		
		return result;
		
	}
	
	public TabooWord findOne(int tabooWordId){
		this.adminService.checkPrincipal();
		TabooWord result;
		
		Assert.notNull(tabooWordId);
		Assert.isTrue(tabooWordId != 0);
		
		
		result = this.tabooWordRepository.findOne(tabooWordId);
		Assert.isTrue(result.isDefault_word() == false);
		
		return result;
		
	}
	
	public Collection<TabooWord> findAll(){
		this.adminService.checkPrincipal();
		Collection<TabooWord> result;
		
		result = this.tabooWordRepository.findAll();
		
		Assert.notNull(result);
		
		return result;
	}
	
	public TabooWord save(TabooWord tabooWord){
		
		Assert.notNull(tabooWord);
		
		this.adminService.checkPrincipal();
		
		TabooWord result;
		
		result = this.tabooWordRepository.save(tabooWord);
		
		return result;
	}
	
	public void delete(TabooWord tabooWord){
		this.adminService.checkPrincipal();
		Assert.notNull(tabooWord);
		Assert.isTrue(tabooWord.getId() != 0);
		
		this.tabooWordRepository.delete(tabooWord);
		
	}

	//Other 
	
	public Collection<String> findTabooWordByName(){
		
		Collection<String> result;
		
		result = this.tabooWordRepository.findTabooWordByName();
		
		return result;
	}
	
	public TabooWord reconstruct(final TabooWord tabooWord, final BindingResult bindingResult) {
		TabooWord result;
		TabooWord tabooWordBD;
		if (tabooWord.getId() == 0) {
			tabooWord.setDefault_word(false);
			
			result = tabooWord;
		} else {
			tabooWordBD = this.tabooWordRepository.findOne(tabooWord.getId());
			tabooWord.setId(tabooWordBD.getId());
			tabooWord.setVersion(tabooWordBD.getVersion());
			tabooWord.setDefault_word(tabooWordBD.isDefault_word());
		
			result = tabooWord;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}
	
	public void flush(){
		this.tabooWordRepository.flush();
	}
}
