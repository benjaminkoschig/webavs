package ch.globaz.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;

/**
 * Le but de cette class est de donner plus d'information sur l'environnement quand on fait du débug et de
 * fournir des fonctions pour faciliter le debug.
 */
@Slf4j
public class Debug {

    private static final boolean IN_DEBUG_MODE;

    static {
        if (isInDebugMode()) {
            LOG.info("The application is running in debug mode");
            ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(LOG::info);
            IN_DEBUG_MODE = true;
        } else {
            IN_DEBUG_MODE = false;
        }
    }

    private Debug() {}

    private static boolean isInDebugMode() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }

    /**
     * Permet me de savoir si l'environnement a démarré en débug.
     *
     * @return Si l'environnement et mode débug ou pas
     */
    public static boolean isEnvironnementInDebug() {
        return IN_DEBUG_MODE;
    }
}
