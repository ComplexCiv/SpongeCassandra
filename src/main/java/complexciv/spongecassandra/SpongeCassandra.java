package complexciv.spongecassandra;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;

@Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public class SpongeCassandra {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Inject
    private Game game;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        // Perform initialization tasks here
    }

    @Listener
    public void disable(GameStoppingServerEvent event) {
        // Perform shutdown tasks here
    }
}
