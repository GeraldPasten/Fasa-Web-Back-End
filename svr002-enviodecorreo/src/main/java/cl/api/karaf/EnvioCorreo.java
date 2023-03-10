package cl.api.karaf;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Date;

@Service
@Path("/services/")
public class EnvioCorreo {

	private static final Logger LOG = LoggerFactory.getLogger(EnvioCorreo.class);

	private final String fromEmail = "comunicacionesinternas";
	private final String password = "F4rm4c142017";


	@POST
	@Path("/enviar")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendMail(String bodyIn) {

		LOG.info("Inicia envio de correo.....");

		JSONObject js = new JSONObject(bodyIn);
		String toEmailUser = js.getString("toEmailUser");
		String subject = js.getString("subject");
		String bodyMail = js.getString("bodyMail");
		String smtpFriendlyDisplayName = js.getString("smtpFriendlyDisplayName");
		String smtpFriendlyMailName = js.getString("smtpFriendlyMailName");
		String smtpReplyDisplayName = js.getString("smtpReplyDisplayName");

		Properties props = new Properties();
		props.put("mail.smtp.host", "150.10.11.109");
		props.put("mail.smtp.port", "25");
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.ehlo", "false");
		props.put("mail.debug", "true");

		Authenticator auth = new Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		try {
			MimeMessage msg = new MimeMessage(session);

			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
//			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(smtpFriendlyMailName, smtpFriendlyDisplayName));
			msg.setReplyTo(InternetAddress.parse(smtpReplyDisplayName, false));
			msg.setSubject(subject, "UTF-8");
			msg.setContent(bodyMail, "text/html; charset=utf-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailUser, false));

			LOG.info("mensaje listo para enviar...!!!");
			Transport.send(msg);

			LOG.info("email enviado correctamente...!!!");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "envio de correo";
	}


}