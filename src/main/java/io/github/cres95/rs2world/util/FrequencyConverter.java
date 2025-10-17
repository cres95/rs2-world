package io.github.cres95.rs2world.util;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class FrequencyConverter implements Converter<String, Frequency> {
    @Override
    public Frequency convert(String source) {
        return Frequency.of(source);
    }
}
