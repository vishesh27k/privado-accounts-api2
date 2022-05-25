package ai.privado.demo.accounts.service.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ai.privado.demo.accounts.service.dto.UserProfileD;
import ai.privado.demo.accounts.service.entity.SessionE;
import ai.privado.demo.accounts.service.entity.UserE;
import ai.privado.demo.accounts.service.repos.SessionsR;
import ai.privado.demo.accounts.service.repos.UsersR;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ProfileService {

	private final UsersR userr;
	private final SessionsR sesr;
	private final ModelMapper mapper;

	@GetMapping("{sessionid}")
	public UserProfileD getProfile(@PathVariable(name = "sessionid") String sessionid) {
		if (sessionid != null && !sessionid.isBlank()) {
			Optional<SessionE> sese = sesr.findById(sessionid);
			if (!sese.isEmpty()) {
				Optional<UserE> usre = userr.findById(sese.get().getUserId());
				if (!usre.isEmpty()) {
					return mapper.map(usre.get(), UserProfileD.class);
				}
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}
}
