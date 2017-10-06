package an.function;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HrefExtractor {

	static final Pattern hrefPattern = Pattern.compile("href=\"(.*?)\"", Pattern.DOTALL);

	public Collection<String> extractAll(CharSequence input) {
		Collection<String> hrefs = new HashSet<String>();

		Matcher m = hrefPattern.matcher(input);

		while (m.find()) {
			hrefs.add(m.group(1));
		}

		return hrefs;
	}

}
