package ai.privado.demo.accounts.service.controller;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ai.privado.demo.accounts.apistubs.DataLoggerS;
import ai.privado.demo.accounts.async.EventJobRun;
import ai.privado.demo.accounts.async.SlackSendJobRun;
import ai.privado.demo.accounts.service.dto.EventD;
import ai.privado.demo.accounts.service.dto.LoginD;
import ai.privado.demo.accounts.service.dto.SignupD;
import ai.privado.demo.accounts.service.dto.UserProfileD;
import ai.privado.demo.accounts.service.entity.SessionE;
import ai.privado.demo.accounts.service.entity.UserE;
import ai.privado.demo.accounts.service.repos.SessionsR;
import ai.privado.demo.accounts.service.repos.UsersR;

@RestController
@RequestMapping("/api/public/user")
public class AuthenticationService {
	private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	private final UsersR userr;
	private final SessionsR sesr;
	private final ModelMapper mapper;
	private ExecutorService apiExecutor;
	private DataLoggerS datalogger;
	private ObjectMapper objectMapper;

	@Autowired
	public AuthenticationService(UsersR userr, SessionsR sesr, ModelMapper mapper, DataLoggerS datalogger,
			ObjectMapper objectMapper, @Qualifier("ApiCaller") ExecutorService apiExecutor) {
		super();
		this.userr = userr;
		this.sesr = sesr;
		this.mapper = mapper;
		this.datalogger = datalogger;
		this.objectMapper = objectMapper;
		this.apiExecutor = apiExecutor;
	}

	@PostMapping("/signup")
	public UserProfileD signup(@RequestBody SignupD signup) {
		if (signup != null && signup.getEmail() != null && signup.getPassword() != null && !signup.getEmail().isEmpty()
				&& !signup.getPassword().isEmpty()) {
			UserE saved = userr.save(mapper.map(signup, UserE.class));
			try {
				logger.info("New Signup : - " + objectMapper.writeValueAsString(signup));
				EventD event = new EventD();
				event.setId(UUID.randomUUID().toString());
				event.setEvent("SIGNUP");
				event.setData(objectMapper.writeValueAsString(signup));
				EventJobRun ejr = new EventJobRun(datalogger, event);
				apiExecutor.execute(ejr);
				apiExecutor.execute(new SlackSendJobRun("someid", "New user Signup - " + signup.getEmail() + ", Name - "
						+ signup.getFirstName() + " " + signup.getLastName()));

			} catch (JsonProcessingException e) {
				logger.error("Error scheduling api call: ", e);
			}
			return mapper.map(saved, UserProfileD.class);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/authenticate")
	public String authenticate(@RequestBody LoginD login) {
		if (login != null && login.getEmail() != null && login.getPassword() != null && !login.getEmail().isBlank()
				&& !login.getPassword().isBlank()) {
			Optional<UserE> resp = userr.findByEmail(login.getEmail());
			try {
				logger.info("Login request : - " + objectMapper.writeValueAsString(login));
				EventD event = new EventD();
				event.setId(UUID.randomUUID().toString());
				event.setEvent("LOGIN");
				event.setData(objectMapper.writeValueAsString(login));
				EventJobRun ejr = new EventJobRun(datalogger, event);
				apiExecutor.execute(ejr);
			} catch (JsonProcessingException e) {
				logger.error("Error scheduling api call: ", e);
			}
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
