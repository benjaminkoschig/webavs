package ch.globaz.orion.business.provider;

import globaz.globall.db.GlobazServer;

/**
 * Permet de savoir a qui appartient l'instance de Orion et donc permet de définir lors des accès WS si l'instance
 * ebusiness cible est la bonne
 * 
 * @author BJO
 */
public final class OwnerProvider {

    public static String getOwner() {
        try {
            return GlobazServer.getCurrentSystem().getApplication("ORION").getProperty("ebusiness.owner");
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private OwnerProvider() {

    }

}
