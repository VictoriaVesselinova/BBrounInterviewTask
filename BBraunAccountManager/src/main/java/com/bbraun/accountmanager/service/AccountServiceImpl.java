package com.bbraun.accountmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbraun.accountmanager.dto.AccountDto;
import com.bbraun.accountmanager.entity.AccountEntity;
import com.bbraun.accountmanager.exception.AccountErrors;
import com.bbraun.accountmanager.exception.AccountException;
import com.bbraun.accountmanager.repository.AccountRepository;
import com.bbraun.accountmanager.util.AccountMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Transactional
	@Override
	public AccountDto create(AccountDto account) {

		account.setId(null);

		account.getAddresses().stream().forEach(address -> address.setId(null));

		return AccountMapper.toDto(accountRepository.save(AccountMapper.toEntity(account)));

	}

	@Transactional
	@Override
	public AccountDto update(AccountDto account) throws AccountException {
		if (account.getId() == null) {
			log.error(AccountErrors.EMPTY_ID.name());
			throw new AccountException(AccountErrors.EMPTY_ID.name());
		}

		Optional<AccountEntity> existingAccount = accountRepository.findById(account.getId());

		if (!existingAccount.isPresent()) {
			log.error(AccountErrors.ACCOUNT_NOT_EXIST.name());
			throw new AccountException(AccountErrors.ACCOUNT_NOT_EXIST.name());
		}

		return AccountMapper.toDto(accountRepository.save(AccountMapper.toEntity(account)));
	}

	@Override
	public void delete(Long id) {
		accountRepository.deleteById(id);

	}

	@Override
	public Page<AccountDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		return AccountMapper.toPageOfDtos(accountRepository.findAll(paging));

	}

	@Override
	public List<AccountDto> getByFirstNameOrLastName(String firstName, String lastName) {

		return AccountMapper.toListOfDtos(accountRepository.findByFirstNameOrLastName(firstName, lastName));

	}

	@Override
	public List<AccountDto> getOlderThanThirtyAndFromCity(String city) {

		return AccountMapper.toListOfDtos(accountRepository.getOlderThanThirtyAndFromCity(city));
	}

}
