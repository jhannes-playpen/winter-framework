package com.johannesbrodwall.winter.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public interface PropertySource {

	Optional<String> property(String key);

	default String required(String key) {
		return property(key).orElseThrow(() -> new RuntimeException("Missing required property " + key));
	}

	default String propertyChoice(String key, String[] options, String defaultOption) {
		String result = property(key).orElse(defaultOption);
		if (!Arrays.asList(options).contains(result)) {
			throw new IllegalArgumentException("Illegal value for " + key + ": " + result);
		}
		return result;
	}

	default String requiredUrl(String key) {
		required(key);
		return url(key, null);
	}

	default String url(String key, String defaultValue) {
		try {
			URL url = new URL(property(key).orElse(defaultValue));
			return url.toString();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed URL for propery " + key + ": " + required(key));
		}
	}


	static PropertySource create(String activeProfiles) {
		return create(new File("."), activeProfiles);
	}

	static PropertySource create(File configurationDirectory, String activeProfiles) {
		PropertySourceList chain = new PropertySourceList();

		if (activeProfiles != null) {
			for (String profile : activeProfiles.split(",\\s*")) {
				chain.addPropertySource(new FilePropertySource(new File(configurationDirectory, "application-" + profile + ".properties")));
				chain.addPropertySource(new ClasspathPropertySource("/application-" + profile + ".properties"));
			}
		}
		chain.addPropertySource(new FilePropertySource(new File(configurationDirectory, "application.properties")));
		chain.addPropertySource(new ClasspathPropertySource("/application.properties"));

		return chain;
	}

}
