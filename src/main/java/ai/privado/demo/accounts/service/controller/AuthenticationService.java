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

import com.fasterxml.jackson.databind.ObjectMapper;

import ai.privado.demo.accounts.apistubs.DataLoggerS;
import ai.privado.demo.accounts.service.dto.EventD;
import ai.privado.demo.accounts.service.dto.LoginD;
import ai.privado.demo.accounts.service.dto.SGEMailD;
import ai.privado.demo.accounts.service.dto.SignupD;
import ai.privado.demo.accounts.service.dto.UserProfileD;
import ai.privado.demo.accounts.service.entity.SessionE;
import ai.privado.demo.accounts.service.entity.UserE;
import ai.privado.demo.accounts.service.repos.SessionsR;
import ai.privado.demo.accounts.service.repos.UsersR;
import ai.privado.demo.accounts.thirdparty.SendGridStub;
import ai.privado.demo.accounts.thirdparty.SlackStub;

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
	private SlackStub slackStub;
	private SendGridStub sgStub;

	@Autowired
	public AuthenticationService(UsersR userr, SessionsR sesr, ModelMapper mapper, DataLoggerS datalogger,
			ObjectMapper objectMapper, @Qualifier("ApiCaller") ExecutorService apiExecutor, SlackStub slackStub,
			SendGridStub sgStub) {
		super();
		this.userr = userr;
		this.sesr = sesr;
		this.mapper = mapper;
		this.datalogger = datalogger;
		this.objectMapper = objectMapper;
		this.apiExecutor = apiExecutor;
		this.slackStub = slackStub;
		this.sgStub = sgStub;
	}

	@PostMapping("/signup")
	public UserProfileD signup(@RequestBody SignupD signup) {
		if (signup != null && signup.getEmail() != null && signup.getPassword() != null && !signup.getEmail().isEmpty()
				&& !signup.getPassword().isEmpty()) {
			UserE saved = userr.save(mapper.map(signup, UserE.class));
			String email = signup.getEmail();
			String phone = signup.getPhone();
			String firstName = signup.getFirstName();
			String lastName = signup.getLastName();
			logger.info("New Signup : - " + email + phone);
			EventD event = new EventD();
			event.setId(UUID.randomUUID().toString());
			event.setEvent("SIGNUP");
			event.setData(email + phone);
			datalogger.sendEvent(event);
			slackStub.sendMessage("someid", "New user Signup - " + email + ", Name - " + firstName + " " + lastName);
			SGEMailD sgemail = new SGEMailD();
			sgemail.setEmailid(signup.getEmail());
			sgemail.setSubject("Welcome");
			sgemail.setMsgBody("Hi " + firstName + " " + lastName + " Some welcome message");
			sgStub.sendEmail(sgemail);
			return mapper.map(saved, UserProfileD.class);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/authenticate")
	public String authenticate(@RequestBody LoginD login) {
		if (login != null && login.getEmail() != null && login.getPassword() != null && !login.getEmail().isBlank()
				&& !login.getPassword().isBlank()) {
			Optional<UserE> resp = userr.findByEmail(login.getEmail());

			String email = login.getEmail();
			String password = login.getPassword();
			logger.info("Login request : - " + email + "-" + password);
			EventD event = new EventD();
			event.setId(UUID.randomUUID().toString());
			event.setEvent("LOGIN");
			event.setData("Login request : - " + email + "-" + password);
			datalogger.sendEvent(event);
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
