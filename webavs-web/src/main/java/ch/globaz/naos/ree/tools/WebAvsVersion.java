package ch.globaz.naos.ree.tools;

import globaz.webavs.common.WebavsDocumentionLocator;

public class WebAvsVersion {

    /**
     * La longueur max autoris� par les XSD pour le num�ro de version de l'application
     */
    private static final int VERSION_LENGTH = 10;
    private static String version;

    /**
     * Retourne la version de WebAvs, la longueur sera 10 caract�res max (contrainte XSD) -> la version peut donc �tre
     * tronqu�e
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
