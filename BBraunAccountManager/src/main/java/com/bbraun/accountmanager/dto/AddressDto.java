package com.bbraun.accountmanager.dto;

import com.bbraun.accountmanager.entity.AccountEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

	private Long id;

	@ApiModelProperty(notes = "City")
	private String city;

	@ApiModelProperty(notes = "Street")
	private String street;

	@ApiModelProperty(notes = "City")
	@JsonProperty(value = "post_code")
	private int postCode;

	@JsonIgnore
	private AccountEntity account;

}
