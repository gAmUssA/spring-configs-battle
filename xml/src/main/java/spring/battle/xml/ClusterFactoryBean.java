package spring.battle.xml;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import org.springframework.beans.factory.FactoryBean;

import static spring.battle.xml.Cluster.Builder.PoolingOptions.Options.LOCAL;
import static spring.battle.xml.Cluster.DowngradingConsistencyRetryPolicy.INSTANCE;

/**
 * @author jbaruch
 * @since 10/9/14
 */
public class ClusterFactoryBean implements FactoryBean<Cluster> {

    private String contactPoint;
    private int connectionsPerHost;
    private long reconnectionPolicy;
    private Config hazelcastConfig;

    @Override
    public Cluster getObject() throws Exception {
        return Cluster.builder().addContactPoint(contactPoint)
                .poolingOptions().setCoreConnectionsPerHost(LOCAL, connectionsPerHost).withRetryPolicy(INSTANCE)
                .withReconnectionPolicy(new Cluster.Builder.ConstantReconnectionPolicy(reconnectionPolicy))
                .withHazelcastInstance(Hazelcast.newHazelcastInstance(hazelcastConfig))
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return Cluster.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setContactPoint(String contactPoint) {
        this.contactPoint = contactPoint;
    }

    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public void setReconnectionPolicy(long reconnectionPolicy) {
        this.reconnectionPolicy = reconnectionPolicy;
    }

    public void setHazelcastConfig(Config hazelcastConfig) {
        this.hazelcastConfig = hazelcastConfig;
    }
}
