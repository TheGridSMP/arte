package the.grid.smp.arte.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import the.grid.smp.arte.common.Arte;
import the.grid.smp.arte.common.config.ArteConfig;
import the.grid.smp.arte.common.logger.ArteLogger;
import the.grid.smp.arte.fabric.command.ArteCommand;
import the.grid.smp.arte.fabric.config.FabricArteConfig;
import the.grid.smp.arte.fabric.listener.PlayerListener;
import the.grid.smp.arte.fabric.logger.FabricArteLogger;
import the.grid.smp.arte.fabric.pack.FabricPackManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ArteMod implements DedicatedServerModInitializer, Arte {

    private ArteLogger logger;
    private ArteConfig config;
    private FabricPackManager packManager;

    public void onInitializeServer() {
        this.logger = new FabricArteLogger("arte");
        this.config = new FabricArteConfig(this);

        this.packManager = new FabricPackManager(this);

        CommandRegistrationCallback.EVENT.register(new ArteCommand(this));
        ServerPlayConnectionEvents.JOIN.register(new PlayerListener(this));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> this.config.save());
    }

    @Override
    public ArteLogger logger() {
        return this.logger;
    }

    @Override
    public ArteConfig config() {
        return this.config;
    }

    @Override
    public FabricPackManager getPackManager() {
        return this.packManager;
    }

    @Override
    public File getDataFolder() {
        return FabricLoader.getInstance().getGameDir().resolve("arte").toFile();
    }

    @Override
    public File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("arte.json").toFile();
    }

    @Override
    public URL getResourceUrl(String path) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(path);

        if (url == null)
            throw new IOException("Couldn't find the default config! The build may be corrupt! Path: " + path);

        return url;
    }
}
