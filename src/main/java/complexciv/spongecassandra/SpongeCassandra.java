package complexciv.spongecassandra;

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
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private ConfigurationNode config = null;

    @Inject
    private Game game;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        initConfig();
    }

    @Listener
    public void disable(GameStoppingServerEvent event) {
        // Perform shutdown tasks here
    }

    private void initConfig() {
        try {
            config = configLoader.load();

            if (!defaultConfig.exists()) {
                config.getNode("cassandra", "contact-points").setValue(Collections.singletonList("0.0.0.0"));
                config.getNode("cassandra", "port").setValue(9160);
                configLoader.save(config);
            }
        } catch (IOException e) {
            logger.warn("Main config could not be loaded/created/changed!");
        }
    }
}
