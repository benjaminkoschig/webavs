package globaz.osiris.db.ordres.sepa;

import globaz.osiris.db.ordres.sepa.exceptions.CACamt054UnsupportedVersionException;
import java.util.List;
import org.w3c.dom.Document;

/**
 * Classe permettant de lister toutes les versions et type support� par l'application et qui permet de r�soudre un
 * fichier en entr�e afin de savoir si il est support� et aussi permettant de retourner un ensemble d'objet m�tier selon
 * la version du fichier et du type.
 * 
 * @author dcl
 * 
 */
public class CACamt054VersionResolver {

    public enum Versions {
        NAMESPACE_BVRV0104("urn:iso:std:iso:20022:tech:xsd:camt.054.001.04", new CACamt054BVRV0104()),
        NAMESPACE_LSVV0104("urn:iso:std:iso:20022:tech:xsd:camt.054.001.04", new CACamt054LSVV0104());

        private String namespace;
        private CACamt054VersionInterface camtVersionClass;

        private Versions(final String namespace, final CACamt054VersionInterface camtVersionClass) {
            this.namespace = namespace;
            this.camtVersionClass = camtVersionClass;
        }

        protected static boolean isSupportedVersion(final String namespace, CACamt054DefinitionType type) {
            Versions version = getVersionFromNameSpace(namespace, type);

            return version != null;
        }

        protected static Versions getVersionFromNameSpace(final String namespace, CACamt054DefinitionType type) {
            for (Versions version : Versions.values()) {
                if (version.getNamespace().equals(namespace) && version.getCamtVersionClass().getType().equals(type)) {
                    return version;
                }
            }
            return null;
        }

        protected String getNamespace() {
            return namespace;
        }

        protected CACamt054VersionInterface getCamtVersionClass() {
            return camtVersionClass;
        }
    }

    private CACamt054VersionResolver() {
        super();
    }

    /**
     * R�sout le document donn� en param�tre afin de retourner une liste de notifications (selon la version du document
     * qu'il aura pu r�soudre). Le filename est juste l� pour �tre renseign� dans les pojos � des fins de visualisations
     * d'�crans.
     * 
     * @param doc Un document w3c.
     * @param fileName Un nom de fichier.
     * @param type Le type que l'on souhaite identifi�.
     * @return Une liste de notifications selon la version trouv�e.
     * @throws CACamt054UnsupportedVersionException Une exception disant que la version du document donn� n'est pas
     *             support� par l'application.
     */
    public static List<CACamt054Notification> resolveDocument(Document doc, final String fileName,
            CACamt054DefinitionType type) throws CACamt054UnsupportedVersionException {
        final String namespaceURI = doc.getDocumentElement().getNamespaceURI();

        if (!Versions.isSupportedVersion(namespaceURI, type)) {
            throw new CACamt054UnsupportedVersionException("Unsupported version for this namespace : " + namespaceURI);
        }

        return Versions.getVersionFromNameSpace(namespaceURI, type).getCamtVersionClass().extractPojos(doc, fileName);
    }

    public static boolean isSupportedVersion(final String namespace, CACamt054DefinitionType type) {
        return Versions.isSupportedVersion(namespace, type);
    }
}
