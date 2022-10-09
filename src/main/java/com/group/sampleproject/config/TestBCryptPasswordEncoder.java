package com.group.sampleproject.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCryptPasswordEncoder extends BCryptPasswordEncoder {
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return true;
	}    
}
