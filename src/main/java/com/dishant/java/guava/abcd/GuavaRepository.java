package com.dishant.java.guava.abcd;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface GuavaRepository extends CrudRepository<GuavaEntity, Integer> {

	@Query(value = "select * from guava_entity where code in :codes", nativeQuery = true)
	List<GuavaEntity> method1(Set<String> codes);
}
