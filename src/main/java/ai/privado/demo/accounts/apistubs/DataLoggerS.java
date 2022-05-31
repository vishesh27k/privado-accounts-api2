package ai.privado.demo.accounts.apistubs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import ai.privado.demo.accounts.service.dto.EventD;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class DataLoggerS {
	private static Logger logger = LoggerFactory.getLogger(DataLoggerS.class);
	@Autowired
	private ObjectMapper objectMapper;
	private String baseURL = "https://localhost/analytics";

	public void sendEvent(EventD event) {

		try {
			String payload = objectMapper.writeValueAsString(event);
			// TODO: pickup the base URL from application.properties
			HttpResponse<String> response = Unirest.post(baseURL + "/events").header("accept", "application/json")
					.header("Content-Type", "application/json").body(payload).asString();

			if (response.getStatus() != 200) {
				logger.error("Error in event logging..");
			} else {
				logger.info("Event logging successful");
			}
		} catch (UnirestException | IOException e) {
			logger.error("Event log error:", e);
		}
	}
}
