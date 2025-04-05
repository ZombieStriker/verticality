package me.zombie_striker.verticality;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Verticality {

    private static final Verticality INSTANCE = new Verticality();
    private static final Logger LOGGER = LoggerFactory.getLogger("Verticality");
    private final VerticalityCore core;

    private Verticality() {
        core = new VerticalityCore();
    }

    public static VerticalityCore getVerticalityCore() {
        return INSTANCE.core;
    }

    public static void log(String message) {
        LOGGER.info(message);
    }

    public static void log(String provider, String message) {
        LOGGER.info("[" + provider + "]: " + message);
    }
}
