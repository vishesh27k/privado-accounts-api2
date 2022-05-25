package ai.privado.demo.accounts.async;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;

public class SlackSendJobRun implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SlackSendJobRun.class);
	private String id;
	private String message;

	@Override
	public void run() {
		// you can get this instance via ctx.client() in a Bolt app
		var client = Slack.getInstance().methods();
		try {
			// Call the chat.postMessage method using the built-in WebClient
			var result = client.chatPostMessage(r -> r
					// The token you used to initialize your app
					.token("xoxb-your-token").channel(id).text(message)
			// You could also use a blocks[] array to send richer content
			);
			// Print result, which includes information about the message (like TS)
			logger.info("result {}", result);
		} catch (IOException | SlackApiException e) {
			logger.error("error: {}", e.getMessage(), e);
		}
	}

	public SlackSendJobRun(String id, String message) {
		super();
		this.id = id;
		this.message = message;
	}

}
