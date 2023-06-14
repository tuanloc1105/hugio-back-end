package vn.com.hugio.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Component;

import java.io.Serial;

@Component
public class YamlMapperConfig {

    public YAMLMapper yamlMapperConfig() {
        return new YAMLMapper() {
            @Serial
            private static final long serialVersionUID = -4564435301260766046L;

            {
                findAndRegisterModules();
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                setSerializationInclusion(JsonInclude.Include.ALWAYS);
            }
        };
    }

}
