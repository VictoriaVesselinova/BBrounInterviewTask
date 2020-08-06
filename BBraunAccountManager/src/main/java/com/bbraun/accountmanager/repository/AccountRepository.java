package com.bbraun.accountmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bbraun.accountmanager.entity.AccountEntity;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<AccountEntity, Long> {

	@Query
	public List<AccountEntity> findByFirstNameOrLastName(String firstName, String lastName);

	@Query(value = "SELECT * FROM account acc JOIN address add ON acc.id = add.account_id WHERE add.city = :city "
			+ "AND acc.age > 30", nativeQuery = true)
	public List<AccountEntity> getOlderThanThirtyAndFromCity(@Param("city") String city);
}
