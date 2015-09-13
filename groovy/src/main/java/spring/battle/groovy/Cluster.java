package spring.battle.groovy;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Jeka on 13/10/2014.
 */
public class Cluster {

    private static Logger LOG = LoggerFactory.getLogger(Cluster.class);
    private final IMap<String, String> storage;

    public Cluster(IMap storage) {
        this.storage = storage;
    }

    public String save(Map<String, String> map) {
        LOG.info("saving " + map);

        map.forEach(storage::put);

        return "Saved " + (map.size() - 1) + " entries, which were parsed by " + map.get(Parser.PARSER_NAME_KEY);
    }

    public static enum DowngradingConsistencyRetryPolicy {INSTANCE}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private IMap storage;

        public Builder addContactPoint(String contactPoint) {
            return this;
        }

        public Builder withRetryPolicy(DowngradingConsistencyRetryPolicy policy) {
            return this;
        }

        public Builder withReconnectionPolicy(ConstantReconnectionPolicy policy) {
            return this;
        }

        public Builder withHazelcastInstance(HazelcastInstance hazelcastInstance) {
            this.storage = hazelcastInstance.getMap("storage");
            return this;
        }

        public Cluster build() {
            return new Cluster(storage);
        }

        public Builder.PoolingOptions poolingOptions() {
            return new PoolingOptions(this);
        }

        public static class PoolingOptions {

            private Builder builder;

            public PoolingOptions(Builder builder) {
                this.builder = builder;
            }

            public static enum Options {LOCAL}

            public Builder setCoreConnectionsPerHost(Options option, int value) {
                return builder;
            }
        }


        public static class ConstantReconnectionPolicy {
            public ConstantReconnectionPolicy(long timeout) {
            }
        }
    }
}
