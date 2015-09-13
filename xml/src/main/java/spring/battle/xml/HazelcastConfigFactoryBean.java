package spring.battle.xml;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import org.springframework.beans.factory.FactoryBean;

/**
 * TODO
 *
 * @author Viktor Gamov on 9/13/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class HazelcastConfigFactoryBean implements FactoryBean<Config> {
    private XmlConfigBuilder xmlConfigBuilder;

    @Override public Config getObject() throws Exception {
        return xmlConfigBuilder.build();
    }

    @Override public Class<?> getObjectType() {
        return Config.class;
    }

    @Override public boolean isSingleton() {
        return true;
    }

    public void setXmlConfigBuilder(XmlConfigBuilder xmlConfigBuilder) {
        this.xmlConfigBuilder = xmlConfigBuilder;
    }
}
