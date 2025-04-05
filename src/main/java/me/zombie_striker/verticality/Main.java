package me.zombie_striker.verticality;

import net.minestom.server.MinecraftServer;

public class Main {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        VerticalityCore core = Verticality.getVerticalityCore();
        core.init();
        core.start(server);
    }
}