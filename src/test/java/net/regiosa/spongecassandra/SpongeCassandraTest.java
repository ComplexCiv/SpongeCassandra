package net.regiosa.spongecassandra;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SpongeCassandraTest {

    // TODO: Test

    @Before
    public void before() throws ConfigurationException, IOException, TTransportException, InterruptedException {
        //EmbeddedCassandraServerHelper.startEmbeddedCassandra();
    }

    @Test
    public void successfulConnection() {
        //SpongeCassandra dummy = new SpongeCassandra();
        //dummy.connectToCassandra(Collections.singletonList("localhost"), 9042);
    }
}
