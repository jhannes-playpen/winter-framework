package com.johannesbrodwall.winter.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PropertySourceList implements PropertySource {

    private final List<PropertySource> propertySources = new ArrayList<>();

    @Override
    public Optional<String> property(String key) {
        return propertySources.stream()
                .map(p -> p.property(key))
                .filter(prop -> prop.isPresent())
                .map(p -> p.get())
                .findFirst();
    }

    public void addPropertySource(PropertySource propertySource) {
        propertySources.add(propertySource);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + propertySources + "}";
    }
}
