package com.bbraun.accountmanager.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.bbraun.accountmanager.dto.AccountDto;
import com.bbraun.accountmanager.dto.AddressDto;
import com.bbraun.accountmanager.entity.AccountEntity;
import com.bbraun.accountmanager.entity.AddressEntity;

public class AccountMapper {

	public static AccountEntity toEntity(AccountDto dto) {
		AccountEntity entity = new AccountEntity();

		entity.setId(dto.getId());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		entity.setAge(dto.getAge());

		List<AddressEntity> addressEntities = new ArrayList<>();

		Optional.ofNullable(dto.getAddresses()).orElseGet(Collections::emptyList).stream().filter(Objects::nonNull)
				.forEach(address -> {
					address.setAccount(entity);
					addressEntities.add(toEntity(address));
				});
		entity.setAddresses(addressEntities);

		return entity;

	}

	public static AccountDto toDto(AccountEntity entity) {
		AccountDto dto = new AccountDto();
		List<AddressDto> addressDtos = new ArrayList<>();

		Optional.ofNullable(entity.getAddresses()).orElseGet(Collections::emptyList).stream().filter(Objects::nonNull)
				.forEach(address -> addressDtos.add(toDto(address)));

		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setEmail(entity.getEmail());
		dto.setAge(entity.getAge());
		dto.setAddresses(addressDtos);

		return dto;
	}

	public static List<AccountDto> toListOfDtos(List<AccountEntity> page) {
		List<AccountDto> result = new ArrayList<>();

		page.forEach(entity -> result.add(toDto(entity)));

		return result;

	}

	public static Page<AccountDto> toPageOfDtos(Page<AccountEntity> page) {
		List<AccountDto> content = new ArrayList<>();

		page.forEach(entity -> content.add(toDto(entity)));

		return new PageImpl<>(content);

	}

	public static AddressEntity toEntity(AddressDto dto) {
		AddressEntity entity = new AddressEntity();

		entity.setId(dto.getId());
		entity.setCity(dto.getCity());
		entity.setStreet(dto.getStreet());
		entity.setPostCode(dto.getPostCode());
		entity.setAccount(dto.getAccount());

		return entity;

	}

	public static AddressDto toDto(AddressEntity entity) {
		AddressDto dto = new AddressDto();

		dto.setId(entity.getId());
		dto.setCity(entity.getCity());
		dto.setStreet(entity.getStreet());
		dto.setPostCode(entity.getPostCode());

		return dto;
	}
}
