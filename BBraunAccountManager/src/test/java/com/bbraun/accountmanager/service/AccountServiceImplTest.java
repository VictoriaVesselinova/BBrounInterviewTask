package com.bbraun.accountmanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.bbraun.accountmanager.Application;
import com.bbraun.accountmanager.H2JpaConfig;
import com.bbraun.accountmanager.dto.AccountDto;
import com.bbraun.accountmanager.dto.AddressDto;
import com.bbraun.accountmanager.exception.AccountException;
import com.bbraun.accountmanager.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@ContextConfiguration(classes = H2JpaConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountServiceImplTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	private static final String FIRST_NAME = "Ivan";
	private static final String LAST_NAME = "Ivanov";
	private static final String EMAIL = "ivan.ivanov@mail.com";
	private static final int AGE = 28;

	private static final String CITY = "Sofia";
	private static final String STREET = "Bul. Vasil Levski ";
	private static final int POST_CODE = 1000;

	private static final String FIRST_NAME_FOR_UPDATE = "NewFirstName";
	private static final String EMAIL_FOR_UPDATE = "new.email@mail.com";
	private static final int POST_CODE_FOR_UPDATE = 2000;
	private static final String CITY_FOR_UPDATE = "Sofia";

	private static final String NOT_EXISTING_CITY = "Varna";

	@After
	public void after() {
		accountRepository.deleteAll();
	}

	@Test
	public void testCreateAccount_whithRightData_shouldSuceed() {
		AccountDto savedAccount = accountService.create(prepareAccount(1));

		assertNotNull(savedAccount);

		assertEquals(savedAccount.getFirstName(), FIRST_NAME + 1);
		assertEquals(savedAccount.getLastName(), LAST_NAME + 1);
		assertEquals(savedAccount.getAge(), AGE + 1);
		assertEquals(savedAccount.getEmail(), 1 + EMAIL);
		assertEquals(savedAccount.getAddresses().get(0).getCity(), CITY);
		assertEquals(savedAccount.getAddresses().get(0).getStreet(), STREET + 1);
		assertEquals(savedAccount.getAddresses().get(0).getPostCode(), POST_CODE);

	}

	@Test
	public void testCreateAccount_whithSomeMissingProperties_shouldSuceed() {
		AccountDto savedAccount = accountService.create(prepareAccountWithSomeMissingProperties(1));

		assertNotNull(savedAccount);

		assertEquals(savedAccount.getFirstName(), FIRST_NAME + 1);
		assertEquals(savedAccount.getLastName(), null);
		assertEquals(savedAccount.getAge(), 0);
		assertEquals(savedAccount.getEmail(), 1 + EMAIL);
		assertEquals(savedAccount.getAddresses().get(0).getCity(), CITY);
		assertEquals(savedAccount.getAddresses().get(0).getStreet(), null);
		assertEquals(savedAccount.getAddresses().get(0).getPostCode(), POST_CODE);

	}

	@Test
	public void testUpdateAccountData_whithRightData_shouldSuceed() throws AccountException {
		AccountDto savedAccount = accountService.create(prepareAccount(1));

		savedAccount.setFirstName(FIRST_NAME_FOR_UPDATE);
		savedAccount.setEmail(EMAIL_FOR_UPDATE);
		savedAccount.getAddresses().get(0).setPostCode(POST_CODE_FOR_UPDATE);
		savedAccount.getAddresses().get(0).setCity(CITY_FOR_UPDATE);

		AccountDto updatedAccount = accountService.update(savedAccount);

		assertNotNull(updatedAccount);

		assertEquals(updatedAccount.getFirstName(), FIRST_NAME_FOR_UPDATE);
		assertEquals(updatedAccount.getLastName(), LAST_NAME + 1);
		assertEquals(updatedAccount.getAge(), AGE + 1);
		assertEquals(updatedAccount.getEmail(), EMAIL_FOR_UPDATE);
		assertEquals(updatedAccount.getAddresses().get(0).getCity(), CITY_FOR_UPDATE);
		assertEquals(updatedAccount.getAddresses().get(0).getStreet(), STREET + 1);
		assertEquals(updatedAccount.getAddresses().get(0).getPostCode(), POST_CODE_FOR_UPDATE);

	}

	@Test(expected = AccountException.class)
	public void testUpdateAccountData_whithIdNull_shouldThrowException() throws AccountException {
		AccountDto savedAccount = accountService.create(prepareAccount(1));

		savedAccount.setId(null);

		accountService.update(savedAccount);

	}

	@Test(expected = AccountException.class)
	public void testUpdateAccountData_whithNotExistingId_shouldThrowException() throws AccountException {
		AccountDto savedAccount = accountService.create(prepareAccount(1));

		savedAccount.setId(5l);

		accountService.update(savedAccount);

	}

	@Test
	public void testGetAllAccounts_whithValidData_shouldSucceed() {
		AccountDto account1 = accountService.create(prepareAccount(1));
		AccountDto account2 = accountService.create(prepareAccount(2));
		accountService.create(prepareAccount(3));
		accountService.create(prepareAccount(4));

		Page<AccountDto> accounts = accountService.getAll(0, 2, "firstName");

		assertEquals(2, accounts.getSize());
		assertNotNull(accounts.getContent());
		assertEquals(account1, accounts.getContent().get(0));
		assertEquals(account2, accounts.getContent().get(1));

	}

	@Test
	public void testDeleteAccount_whithValidData_shouldSucceed() {
		AccountDto account = accountService.create(prepareAccount(1));

		accountService.delete(account.getId());

		assertFalse(accountRepository.findById(account.getId()).isPresent());

	}

	@Test
	public void testGetByFirstNameOrLastName_whithValidData_shouldSucceed() {
		accountService.create(prepareAccount(1));
		accountService.create(prepareAccount(2));
		accountService.create(prepareAccount(3));
		accountService.create(prepareAccount(4));

		List<AccountDto> accounts = accountService.getByFirstNameOrLastName(FIRST_NAME + 2, LAST_NAME + 4);

		assertEquals(accounts.size(), 2);
		assertEquals(accounts.get(0).getFirstName(), FIRST_NAME + 2);
		assertEquals(accounts.get(1).getLastName(), LAST_NAME + 4);

	}

	@Test
	public void testGetByFirstNameOrLastName_OnlyWithFirstName_shouldSucceed() {
		accountService.create(prepareAccount(1));
		accountService.create(prepareAccount(2));
		accountService.create(prepareAccount(3));
		accountService.create(prepareAccount(4));

		List<AccountDto> accounts = accountService.getByFirstNameOrLastName(null, LAST_NAME + 4);

		assertEquals(accounts.size(), 1);
		assertEquals(accounts.get(0).getLastName(), LAST_NAME + 4);

	}

	@Test
	public void testGetByFirstNameOrLastName_OnlyWithLasttName_shouldSucceed() {
		accountService.create(prepareAccount(1));
		accountService.create(prepareAccount(2));
		accountService.create(prepareAccount(3));
		accountService.create(prepareAccount(4));

		List<AccountDto> accounts = accountService.getByFirstNameOrLastName(FIRST_NAME + 2, null);

		assertEquals(accounts.size(), 1);
		assertEquals(accounts.get(0).getFirstName(), FIRST_NAME + 2);

	}

	@Test
	public void testGetByFirstNameOrLastName_WhithoutFirstNameAndLastName_shouldSucceed() {
		accountService.create(prepareAccount(1));
		accountService.create(prepareAccount(2));
		accountService.create(prepareAccount(3));
		accountService.create(prepareAccount(4));

		List<AccountDto> accounts = accountService.getByFirstNameOrLastName(null, null);

		assertEquals(accounts.size(), 0);

	}

	@Test
	public void testGetOlderThanThirtyAndFromCity_whithValidData_shouldSucceed() {
		accountService.create(prepareAccount(1));
		accountService.create(prepareAccount(2));
		AccountDto account3 = accountService.create(prepareAccount(3));
		AccountDto account4 = accountService.create(prepareAccount(4));

		List<AccountDto> accounts = accountService.getOlderThanThirtyAndFromCity(CITY);

		assertEquals(2, accounts.size());
		assertTrue(accounts.contains(account3));
		assertTrue(accounts.contains(account4));

	}

	@Test
	public void testGetOlderThanThirtyAndFromCity_whithNotExistiongCity_shouldSucceed() {
		accountService.create(prepareAccount(1));
		accountService.create(prepareAccount(2));
		accountService.create(prepareAccount(3));
		accountService.create(prepareAccount(4));

		List<AccountDto> accounts = accountService.getOlderThanThirtyAndFromCity(NOT_EXISTING_CITY);

		assertEquals(0, accounts.size());

	}

	@Test
	public void testGetOlderThanThirtyAndFromCity_whithNotExistiongCityAndWithoutPeopleUnder30_shouldSucceed() {
		accountService.create(prepareAccount(-1));
		accountService.create(prepareAccount(-2));
		accountService.create(prepareAccount(-3));
		accountService.create(prepareAccount(-4));

		List<AccountDto> accounts = accountService.getOlderThanThirtyAndFromCity(NOT_EXISTING_CITY);

		assertEquals(0, accounts.size());

	}

	@Test
	public void testGetOlderThanThirtyAndFromCity_whithExistiongCityButWithoutPeopleUnder30_shouldSucceed() {
		accountService.create(prepareAccount(-1));
		accountService.create(prepareAccount(-2));
		accountService.create(prepareAccount(-3));
		accountService.create(prepareAccount(-4));

		List<AccountDto> accounts = accountService.getOlderThanThirtyAndFromCity(CITY);

		assertEquals(0, accounts.size());

	}

	private AccountDto prepareAccount(int number) {

		AddressDto address = new AddressDto();

		address.setCity(CITY);
		address.setStreet(STREET + number);
		address.setPostCode(POST_CODE);

		AccountDto account = new AccountDto();

		account.setFirstName(FIRST_NAME + number);
		account.setLastName(LAST_NAME + number);
		account.setEmail(number + EMAIL);
		account.setAge(AGE + number);
		account.setAddresses(Stream.of(address).collect(Collectors.toList()));

		return account;

	}

	private AccountDto prepareAccountWithSomeMissingProperties(int number) {

		AddressDto address = new AddressDto();

		address.setCity(CITY);
		address.setPostCode(POST_CODE);

		AccountDto account = new AccountDto();

		account.setFirstName(FIRST_NAME + number);
		account.setEmail(number + EMAIL);
		account.setAddresses(Stream.of(address).collect(Collectors.toList()));

		return account;

	}

}
