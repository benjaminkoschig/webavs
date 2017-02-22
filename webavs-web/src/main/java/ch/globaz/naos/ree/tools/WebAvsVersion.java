package ch.globaz.naos.ree.tools;

import globaz.webavs.common.WebavsDocumentionLocator;

public class WebAvsVersion {

    /**
     * La longueur max autorisé par les XSD pour le numéro de version de l'application
     */
    private static final int VERSION_LENGTH = 10;
    private static String version;

    /**
     * Retourne la version de WebAvs, la longueur sera 10 caractères max (contrainte XSD) -> la version peut donc être
     * tronquée
     * 
     * @return
     */
    public static String getVersion() {
        if (version == null) {
            version = WebavsDocumentionLocator.getVersion();
            if (version.length() > VERSION_LENGTH) {
                version = version.substring(0, 9);
            }
        }
        return version;
    }
}
