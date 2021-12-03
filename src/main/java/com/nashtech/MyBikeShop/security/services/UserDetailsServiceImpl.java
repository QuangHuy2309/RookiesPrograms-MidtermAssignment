package com.nashtech.MyBikeShop.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.MyBikeShop.entity.UserEntity;
import com.nashtech.MyBikeShop.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		UserEntity user = userRepository.findById(Integer.valueOf(userId)).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with -> id : " + userId));

		return UserDetailsImpl.build(user);
	}
}
