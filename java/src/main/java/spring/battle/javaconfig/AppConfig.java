package spring.battle.javaconfig;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static spring.battle.javaconfig.Cluster.Builder.PoolingOptions.Options.LOCAL;
import static spring.battle.javaconfig.Cluster.DowngradingConsistencyRetryPolicy.INSTANCE;

/**
 * Created by Jeka on 13/10/2014.
 */
@Configuration
@ComponentScan
@EnableWebMvc
@PropertySource("file:hazelcast.properties")
public class AppConfig {

    @Value("${contactPoint}") String contactPoint;
    @Value("${connectionsPerHost}") int connectionsPerHost;
    @Value("${reconnectionPolicy}") long reconnectionPolicy;

    @Bean
    public Cluster cluster() {
        return Cluster.builder().addContactPoint(contactPoint)
            .poolingOptions().setCoreConnectionsPerHost(LOCAL, connectionsPerHost)
            .withRetryPolicy(INSTANCE)
            .withReconnectionPolicy(
                new Cluster.Builder.ConstantReconnectionPolicy(reconnectionPolicy))
            .withHazelcastInstance(hazelcast())
            .build();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public HazelcastInstance hazelcast() {
        // to test in-process 2 nodes cluster uncomment following line
        // Hazelcast.newHazelcastInstance(config);
        return Hazelcast.newHazelcastInstance(hzConfig());
    }

    @Bean
    public Config hzConfig() {
        final InputStream inputStream =
            AppConfig.class.getClassLoader().getResourceAsStream("hazelcast.xml");
        final XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(inputStream);
        xmlConfigBuilder.setProperties(hzProperties());
        return xmlConfigBuilder.build();
    }

    @Bean
    public Properties hzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new FileSystemResource("hazelcast.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();

        } catch (IOException e) {
            // not cool !!!
        }
        return properties;
    }
}
