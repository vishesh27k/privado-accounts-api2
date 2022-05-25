package ai.privado.demo.accounts.async;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import ai.privado.demo.accounts.service.dto.SGEMailD;

public class SGSendMailJobRun implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SGSendMailJobRun.class);
	private SGEMailD mail;

	public SGSendMailJobRun(SGEMailD mail) {
		super();
		this.mail = mail;
	}

	@Override
	public void run() {
		Email from = new Email("test@privado.ai");
		Email to = new Email(this.mail.getEmailid());
		Content content = new Content("text/plain", this.mail.getMsgBody());
		Mail mail = new Mail(from, this.mail.getSubject(), to, content);

		SendGrid sg = new SendGrid("Dummy-api-key");
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			logger.error("Error sending email:", ex);
		}
	}

}
