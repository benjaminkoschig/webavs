package globaz.osiris.db.ordres.sepa;

import globaz.osiris.db.ordres.sepa.exceptions.CACamt054UnsupportedVersionException;
import java.util.List;
import org.w3c.dom.Document;

public class CACamt054BVRVersionResolver {

    public enum Versions {
        // A D�finir ici les diff�rentes versions d'un fichier camt054 BVR
        NAMESPACE_CAMT054V0104("urn:iso:std:iso:20022:tech:xsd:camt.054.001.04", new CACamt054BVRV0104());

        private String namespace;
        private IntCamt054Version camtVersionClass;

        private Versions(final String namespace, final IntCamt054Version versionJava) {
            this.namespace = namespace;
            camtVersionClass = versionJava;
        }

        protected static boolean isSupportedVersion(final String namespace) {
            Versions version = getVersionFromNameSpace(namespace);

            return version != null;
        }

        protected static Versions getVersionFromNameSpace(final String namespace) {
            for (Versions version : Versions.values()) {
                if (version.getNamespace().equals(namespace)) {
                    return version;
                }
            }
            return null;
        }

        protected String getNamespace() {
            return namespace;
        }

        protected IntCamt054Version getCamtVersionClass() {
            return camtVersionClass;
        }
    }

    private CACamt054BVRVersionResolver() {
        super();
    }

    /**
     * R�soud le document donn� en param�tre afin de retourner une liste de notifications (selon la version du document
     * qu'il aura pu r�soudre). Le filename est juste l� pour �tre renseign� dans les pojos � des fins de visualisations
     * d'�crans.
     * 
     * @param doc Un document w3c.
     * @param fileName Un nom de fichier.
     * @return Une liste de notifications selon la version trouv�e.
     * @throws CACamt054UnsupportedVersionException Une exception disant que la version du document donn� n'est pas
     *             support� par l'application.
     */
    public static List<CACamt054Notification> resolveDocument(Document doc, final String fileName)
            throws CACamt054UnsupportedVersionException {
        final String namespaceURI = doc.getDocumentElement().getNamespaceURI();

        if (!Versions.isSupportedVersion(namespaceURI)) {
            throw new CACamt054UnsupportedVersionException("Unsupported version for this namespace : " + namespaceURI);
        }

        return Versions.getVersionFromNameSpace(namespaceURI).getCamtVersionClass().extractPojos(doc, fileName);
    }

    public static boolean isSupportedVersion(final String namespace) {
        return Versions.isSupportedVersion(namespace);
    }
}
