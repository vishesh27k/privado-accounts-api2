package ai.privado.demo.accounts.service.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ai.privado.demo.accounts.service.dto.LoginD;
import ai.privado.demo.accounts.service.dto.SignupD;
import ai.privado.demo.accounts.service.dto.UserProfileD;
import ai.privado.demo.accounts.service.entity.SessionE;
import ai.privado.demo.accounts.service.entity.UserE;
import ai.privado.demo.accounts.service.repos.SessionsR;
import ai.privado.demo.accounts.service.repos.UsersR;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/user")
@RequiredArgsConstructor
public class AuthenticationService {

	private final UsersR userr;
	private final SessionsR sesr;
	private final ModelMapper mapper;

	@PostMapping("/signup")
	public UserProfileD signup(@RequestBody SignupD signup) {
		if (signup != null && signup.getEmail() != null && signup.getPassword() != null && !signup.getEmail().isEmpty()
				&& !signup.getPassword().isEmpty()) {
			UserE saved = userr.save(mapper.map(signup, UserE.class));
			return mapper.map(saved, UserProfileD.class);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/authenticate")
	public String authenticate(@RequestBody LoginD login) {
		if (login != null && login.getEmail() != null && login.getPassword() != null && !login.getEmail().isBlank()
				&& !login.getPassword().isBlank()) {
			Optional<UserE> resp = userr.findByEmail(login.getEmail());
			if (!resp.isEmpty() && login.getPassword().equals(resp.get().getPassword())) {
				SessionE ses = new SessionE();
				ses.setUserId(resp.get().getId());
				ses = sesr.save(ses);
				return ses.getId();
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}
}
