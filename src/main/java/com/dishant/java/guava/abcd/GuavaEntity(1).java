package com.dishant.java.guava.abcd;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class GuavaEntity {

	@Id
	private int id;

	private String name;

	private String code;
}
