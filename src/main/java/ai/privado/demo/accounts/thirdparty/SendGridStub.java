package ai.privado.demo.accounts.thirdparty;

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

public class SendGridStub {
	private static Logger logger = LoggerFactory.getLogger(SendGridStub.class);

	public void sendEmail(SGEMailD mail) {
		Email from = new Email("test@privado.ai");
		Email to = new Email(mail.getEmailid());
		Content content = new Content("text/plain", mail.getMsgBody());
		Mail mails = new Mail(from, mail.getSubject(), to, content);

		SendGrid sg = new SendGrid("Dummy-api-key");
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mails.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			logger.error("Error sending email:", ex);
		}
	}
}
