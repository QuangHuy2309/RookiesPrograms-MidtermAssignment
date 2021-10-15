package com.nashtech.MyBikeShop.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.payload.request.ChangePasswordRequest;
import com.nashtech.MyBikeShop.payload.request.LoginRequest;
import com.nashtech.MyBikeShop.payload.response.JwtResponse;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.security.JWT.JwtUtils;
import com.nashtech.MyBikeShop.security.services.UserDetailsImpl;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.security.JWT.JwtAuthTokenFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthController {

	final private AuthenticationManager authenticationManager;

	final private PersonRepository personRepository;

	final private PersonService personService;

	final private PasswordEncoder encoder;

	final private JwtUtils jwtUtils;

	public AuthController(AuthenticationManager authenticationManager, PersonRepository personRepository,
			PersonService personService, PasswordEncoder encoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.personRepository = personRepository;
		this.personService = personService;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	@Operation(summary = "Log in to get Authorize")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login Success!", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request : JSON missing elements", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Login Failed!", content = @Content),
			@ApiResponse(responseCode = "404", description = "Login not found", content = @Content),
			@ApiResponse(responseCode = "405", description = "Method not allow. Using POST to Login", content = @Content) })
	@PostMapping("/auth/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		PersonEntity person = personRepository.findByEmail(loginRequest.getEmail());
		if (person.isStatus()) {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			// if go there, the user/password is correct
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// generate jwt to return to client
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(
					new JwtResponse(jwt, userDetails.getId(), userDetails.getName(), userDetails.getEmail(), roles));
		} else
			return ResponseEntity.badRequest().body(new MessageResponse("Error: This user was disable"));
	}

	@Operation(summary = "Sign up to get Authorize")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registered successfully!", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "405", description = "Method not allow. Using POST to Login", content = @Content) })
	@PostMapping("/auth/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody PersonDTO signUpRequest) {

		if (personRepository.existsByEmail(signUpRequest.getEmail().toLowerCase())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));
		PersonEntity user = new PersonEntity(signUpRequest);
		personRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@GetMapping("/auth/checkEmail/{email}")
	public boolean checkExistEmailSignUp(@PathVariable(name = "email") String email) {
		return personRepository.existsByEmail(email.toLowerCase()) ? false : true;
	}

	@GetMapping("/auth/logout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jwt = JwtAuthTokenFilter.parseJwt(request);
		if (jwt != null && jwtUtils.addToBlackList(jwt)) {
			return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot add jwt token to blacklist!"));
	}

	@GetMapping("/auth/sendOTP/{email}")
	public ResponseEntity<?> sendOTP(@PathVariable(name = "email") String email) {
		try {
			personService.sendOTPEmail(email);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Cannot send OTP"));
		}
		return ResponseEntity.ok(new MessageResponse("Send OTP success!"));
	}
	
	@GetMapping("/auth/checkOTP")
	public boolean checkOTP(@RequestParam(name = "email") String email,
			@RequestParam(name = "otp") String otp) {
		return personService.checkOTP(email,otp);
	}
	
	@PutMapping("/auth/forgotPassword/{email}")
	public ResponseEntity<?> forgotPassword(@PathVariable String email,
			@RequestBody LoginRequest loginRequest) {
		try {
			if (!email.equalsIgnoreCase(loginRequest.getEmail())) throw new ObjectNotFoundException("Email not the same with body");
			PersonEntity updateAccount = personService.forgotPassword(email, loginRequest.getPassword());
			if (updateAccount == null)
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Change password failed."));
			return ResponseEntity.ok().body(new MessageResponse("Update password successfully."));
		} catch (NoSuchElementException ex) {
			throw new ObjectNotFoundException("Error: Not found account with email: " + email);
		}

	}
}
