package me.zombie_striker.verticality;

import net.minestom.server.MinecraftServer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        VerticalityCore core = new VerticalityCore();
        core.init();
        core.start(server);
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String line = input.nextLine();
            // ... existing command handling ...
            if (line.equalsIgnoreCase("end") || line.equalsIgnoreCase("stop")) {
                core.stop(); // Call the stop method to flush logs
                MinecraftServer.stopCleanly();
                break;
            }
        }
        input.close();
    }
}