package an.function;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;

public final class ResourceReader {

	public static byte[] readAll(InputStream is) throws MessagingException, IOException {
		MimeMessage message = new MimeMessage(null, is);
		InputStream msgSource = ((Multipart) message.getContent()).getBodyPart(1).getInputStream();

		return IOUtils.toByteArray(msgSource);
	}

	public static InputStream read(String fileName) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		return classloader.getResourceAsStream(fileName);
	}

}
