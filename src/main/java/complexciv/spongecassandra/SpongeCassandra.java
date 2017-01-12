package complexciv.spongecassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
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
    public void onPreInit(GamePreInitializationEvent event) {
        initConfig();

        Cluster.Builder clusterBuilder = Cluster.builder();//.addContactPoint("localhost").withPort(9042).build();
        for (ConfigurationNode node : config.getNode("cassandra", "contact-points").getChildrenList())
            clusterBuilder.addContactPoint(node.getString());
        clusterBuilder.withPort(config.getNode("cassandra", "port").getInt());

        session = clusterBuilder.build().connect();
    }

    @Listener
    public void disable(GameStoppingServerEvent event) {
        session.close();
    }

    public Session getSession() {
        return session;
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
}
