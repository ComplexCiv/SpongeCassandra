package net.regiosa.spongecassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public class SpongeCassandra {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private ConfigurationNode config = null;

    @Inject
    private Game game;

    private Session session;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) throws ObjectMappingException {
        initConfig();
        session = createCassandraSession(
                config.getNode("cassandra", "contact-points").getList(new TypeToken<String>() {}),
                config.getNode("cassandra", "port").getInt()
        );
    }

    @Listener
    public void disable(GameStoppingServerEvent event) {
        session.close();
    }

    Session createCassandraSession(List<String> contactPoints, int port) {
        Cluster.Builder clusterBuilder = Cluster.builder().withPort(port);
        contactPoints.forEach(clusterBuilder::addContactPoint);
        return clusterBuilder.build().connect();
    }

    private void initConfig() {
        try {
            config = configLoader.load();

            if (!defaultConfig.exists()) {
                config.getNode("cassandra", "contact-points").setValue(Collections.singletonList("localhost"));
                config.getNode("cassandra", "port").setValue(9042);
                configLoader.save(config);
            }
        } catch (IOException e) {
            logger.warn("Main config could not be loaded/created/changed!");
        }
    }

    public Session getSession() {
        return session;
    }
}
