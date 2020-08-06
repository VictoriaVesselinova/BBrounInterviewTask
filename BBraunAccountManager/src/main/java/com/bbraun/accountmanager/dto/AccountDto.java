package com.bbraun.accountmanager.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

	@ApiModelProperty(notes = "Id (Not needed for create)")
	private Long id;

	@ApiModelProperty(notes = "First Name")
	@JsonProperty(value = "first_name")
	private String firstName;

	@ApiModelProperty(notes = "Last Name")
	@JsonProperty(value = "last_name")
	private String lastName;

	@ApiModelProperty(notes = "Age")
	private int age;

	@ApiModelProperty(notes = "Email")
	private String email;

	@ApiModelProperty(notes = "Addresses")
	private List<AddressDto> addresses;

}
