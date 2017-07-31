package pl.filmoteka.configuration;

import com.google.common.io.Resources;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.model.User;
import pl.filmoteka.service.UserService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

	// Logger
	final static Logger logger = Logger.getLogger(DataInitializer.class.getName());

	@Autowired
	private UserService userService;
	
	@Value("${spring.datasource.url}")
	private String dataSourceUrl;
	
	@Value("${spring.datasource.username}")
	private String dataSourceUsername;

	@Value("${spring.datasource.password}")
	private String dataSourcePassword;
	
	@Value(value = "classpath:db_initializer.sql")
	private Resource resource;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    	User adminUser = userService.findByUsername("admin");
    	
    	if (adminUser == null) {
    		try {
    			String fileContent = Resources.toString(resource.getURL(), Charsets.toCharset("UTF-8"));
    			
				Connection connection = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);
				ScriptUtils.executeSqlScript(connection, new ByteArrayResource(fileContent.getBytes()));
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.severe("A SQL Exception occurred while initializing database with artificial data");
				
			} catch (IOException e) {
				e.printStackTrace();
				logger.severe("The Application was unable to read prepared sql file");
			} 
    	}
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
