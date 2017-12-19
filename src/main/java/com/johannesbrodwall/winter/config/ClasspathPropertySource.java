package com.johannesbrodwall.winter.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

import com.johannesbrodwall.winter.ExceptionUtil;

public class ClasspathPropertySource implements PropertySource {

    private final Properties properties = new Properties();
    private String name;

    public ClasspathPropertySource(String resource) {
        URL url = getClass().getResource(resource);
        this.name = url != null ? url.toString() : (resource + " (not found)");
        try (InputStream inputStream = getClass().getResourceAsStream(resource)) {
            if (inputStream == null) {
                return;
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw ExceptionUtil.soften(e);
        }
    }

    @Override
    public Optional<String> property(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + name + "}";
    }

}
