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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ai.privado.demo.accounts.apistubs.DataLoggerS;
import ai.privado.demo.accounts.async.EventJobRun;
import ai.privado.demo.accounts.service.dto.EventD;
import ai.privado.demo.accounts.service.dto.UserProfileD;
import ai.privado.demo.accounts.service.entity.SessionE;
import ai.privado.demo.accounts.service.entity.UserE;
import ai.privado.demo.accounts.service.repos.SessionsR;
import ai.privado.demo.accounts.service.repos.UsersR;

@RestController
@RequestMapping("/api/user")
public class ProfileService {
	private static Logger logger = LoggerFactory.getLogger(ProfileService.class);
	private final UsersR userr;
	private final SessionsR sesr;
	private final ModelMapper mapper;
	private ExecutorService apiExecutor;
	private DataLoggerS datalogger;
	private ObjectMapper objectMapper;

	@Autowired
	public ProfileService(UsersR userr, SessionsR sesr, ModelMapper mapper,
			@Qualifier("ApiCaller") ExecutorService apiExecutor, DataLoggerS datalogger, ObjectMapper objectMapper) {
		super();
		this.userr = userr;
		this.sesr = sesr;
		this.mapper = mapper;
		this.apiExecutor = apiExecutor;
		this.datalogger = datalogger;
		this.objectMapper = objectMapper;
	}

	@GetMapping("{sessionid}")
	public UserProfileD getProfile(@PathVariable(name = "sessionid") String sessionid) {
		if (sessionid != null && !sessionid.isBlank()) {
			Optional<SessionE> sese = sesr.findById(sessionid);
			if (!sese.isEmpty()) {
				Optional<UserE> usre = userr.findById(sese.get().getUserId());
				if (!usre.isEmpty()) {
					try {
						logger.info("Fetch Profile : - " + objectMapper.writeValueAsString(usre.get()));
						EventD event = new EventD();
						event.setId(UUID.randomUUID().toString());
						event.setEvent("GET_PROFILE");
						event.setData(objectMapper.writeValueAsString(usre.get()));
						EventJobRun ejr = new EventJobRun(datalogger, event);
						apiExecutor.execute(ejr);
					} catch (JsonProcessingException e) {
						logger.error("Error scheduling api call: ", e);
					}

					return mapper.map(usre.get(), UserProfileD.class);
				}
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}
}
