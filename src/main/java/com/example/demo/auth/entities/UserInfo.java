package com.example.demo.auth.entities;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String coId;
	private String usrId;
	private String usrNm;
	private String usrEml;
	private String useYnFlg;
	private String usrPwd;
	private long creDt;
	private long updDt;
	private Collection<? extends GrantedAuthority> authorities;
}
