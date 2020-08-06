package com.bbraun.accountmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bbraun.accountmanager.dto.AccountDto;
import com.bbraun.accountmanager.exception.AccountException;

public interface AccountService {

	AccountDto create(AccountDto account);

	AccountDto update(AccountDto account) throws AccountException;

	void delete(Long id);

	Page<AccountDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

	List<AccountDto> getByFirstNameOrLastName(String firstName, String lastName);

	List<AccountDto> getOlderThanThirtyAndFromCity(String city);

}
