package com.wakuza.springboot.realProjects.infra.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;

@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String host;
}
