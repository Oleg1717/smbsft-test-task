package globalsqa.com.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/project.properties"
})
public interface ProjectConfig extends Config {

    @Config.Key("base.url")
    String baseUrl();

    @Config.Key("selenium.grid.url")
    String seleniumGridUrl();

    @Config.Key("browser.name")
    String browserName();
}