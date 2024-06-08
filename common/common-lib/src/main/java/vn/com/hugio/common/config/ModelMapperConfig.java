package vn.com.hugio.common.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper() {
            {
                getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
            }
        };
    }

}
