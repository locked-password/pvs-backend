package pvs.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Springfox Configuration
 *
 * @author Joey
 */
@EnableOpenApi
@Configuration
public class SpringfoxConfig {
    @Value("${springfox.documentation.open-api.enabled}")
    Boolean openAPIEnabled;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(openAPIEnabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pvs.app"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Project Visualization System API")
                .description("臺北科技大學資訊工程學系軟體工程2020課堂專題")
                .version("1.0.0")
                .build();
    }
}
