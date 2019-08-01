package ru.maratk.undertow.file.server;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;
import ru.maratk.undertow.file.server.handlers.CustomHttpHandler;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class App {

    public final static void main(final String[] args){
        // disable file watcher for path resource manager
        System.setProperty("io.undertow.disable-file-system-watcher", "true");
        // resource manager
        final ResourceManager rm = new PathResourceManager(generateRootResourcesPath());
        // byul undertow server
        final Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new CustomHttpHandler(rm))
                .build();
        // start undertow server
        server.start();
    }

    private final static Path generateRootResourcesPath(){
        final String resourcesRoot = System.getenv("resources.root");
        if(resourcesRoot != null && (!resourcesRoot.isEmpty())){
            return Paths.get(resourcesRoot);
        } else {
            return Paths.get(System.getProperty("user.home"));
        }
    }
}