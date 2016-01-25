package globaz.prestation.acor.xml;

import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORAdapter;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Document;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe pour anticiper une eventuelle migration des fichiers de configuration d'acor depuis des fichiers plat vers
 * des fichiers xml.
 * </p>
 * 
 * <p>
 * Le principe est de deleguer la creation d'une instance de org.w3c.dom.Document aux implementations de cette classe.
 * Le document est ensuite envloppe dans une instance de {@link globaz.prestation.acor.xml.PRDocumentAdapter
 * PRDocumentAdapter} qui pourra être inscrit sans problemes dans un fichier de commande ACOR au moyen du mécanisme
 * habituel.
 * </p>
 * 
 * @author vre
 */
public abstract class PRAbstractXMLAdapter implements PRACORAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // TODO: nom du fichier de configuration de ACOR
    private static final String NOM_FICHIER_CONFIG = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List fichiers;
    private BSession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe PRAbstractXMLAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    protected PRAbstractXMLAdapter(BSession session) {
        this.session = session;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * cree le document (xml) de configuration de l'interface ACOR.
     * 
     * @return DOCUMENT ME!
     */
    protected abstract Document buildDocument();

    /**
     * getter pour l'attribut fichiers ACOR.
     * 
     * @return la valeur courante de l'attribut fichiers ACOR
     */
    @Override
    public List getFichiersACOR() {
        if (fichiers == null) {
            fichiers = new LinkedList();
            fichiers.add(new PRDocumentAdapter(this, NOM_FICHIER_CONFIG, true));
        }

        return fichiers;
    }

    /**
     * getter pour l'attribut session.
     * 
     * @return la valeur courante de l'attribut session
     */
    @Override
    public BSession getSession() {
        return session;
    }
}
