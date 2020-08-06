package com.bbraun.accountmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbraun.accountmanager.dto.AccountDto;
import com.bbraun.accountmanager.exception.AccountException;
import com.bbraun.accountmanager.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.POST)
	public AccountDto create(@RequestBody AccountDto account) {
		return accountService.create(account);

	}

	@RequestMapping(method = RequestMethod.PATCH)
	public ResponseEntity<AccountDto> update(@RequestBody AccountDto account) throws AccountException {
		try {
			return new ResponseEntity<AccountDto>(accountService.update(account), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<AccountDto>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable(name = "id") Long id) {
		accountService.delete(id);

	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<AccountDto> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy) {

		return accountService.getAll(pageNo, pageSize, sortBy);

	}

	@RequestMapping(value = "/getByFirstNameAndLastName", method = RequestMethod.GET)
	public List<AccountDto> getByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName) {

		return accountService.getByFirstNameOrLastName(firstName, lastName);

	}

	@RequestMapping(value = "/getByCityAndOlderThan30", method = RequestMethod.GET)
	public List<AccountDto> getByCityAndOlderThan30(@RequestParam String city) {

		return accountService.getOlderThanThirtyAndFromCity(city);

	}
}
