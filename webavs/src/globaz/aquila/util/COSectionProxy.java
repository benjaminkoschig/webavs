/*
 * Créé le 23 janv. 06
 */
package globaz.aquila.util;

import globaz.globall.db.BSession;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>Description</h1>
 * <p>
 * Un proxy pour ne charger les informations les plus importantes concernant une section qu'une seule fois.
 * </p>
 * 
 * @author vre
 */
public class COSectionProxy {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private CAEcritureManager ecritures;
    private HashMap idComptCourToComptCour = new HashMap();

    private String idSection;
    private CAJoinCompteCourantOperationManager operations;
    private BSession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COSectionProxy.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idSection
     *            DOCUMENT ME!
     */
    public COSectionProxy(BSession session, String idSection) {
        this.session = session;
        this.idSection = idSection;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Charge le compte courant et le garde en cache pour la suite.
     * 
     * @param idCompteCourant
     *            l'identifiant du compte courant à charger
     * @return un compte courant
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CACompteCourant compteCourant(String idCompteCourant) throws Exception {
        CACompteCourant compteCourant = (CACompteCourant) idComptCourToComptCour.get(idCompteCourant);

        if (compteCourant == null) {
            compteCourant = new CACompteCourant();
            compteCourant.setSession(session);
            compteCourant.setIdCompteCourant(idCompteCourant);
            compteCourant.retrieve();
            idComptCourToComptCour.put(idCompteCourant, compteCourant);
        }

        return compteCourant;
    }

    /**
     * Charge les écritures pour la section donnée.
     * 
     * @return La liste des écritures.
     * @throws Exception
     *             DOCUMENT ME!
     */
    public List ecritures() throws Exception {
        ArrayList liste = new ArrayList();
        liste.add(APIOperation.ETAT_COMPTABILISE);
        liste.add(APIOperation.ETAT_PROVISOIRE);
        if (ecritures == null) {
            ecritures = new CAEcritureManager();
            ecritures.setSession(session);
            ecritures.setForIdSection(idSection);
            ecritures.setForEtatIn(liste);
            ecritures.find();
        }

        return ecritures.getContainer();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CAJoinCompteCourantOperationManager operations() throws Exception {
        if (operations == null) {
            operations = new CAJoinCompteCourantOperationManager();
            operations.setSession(session);
            operations.setForIdSection(idSection);
            operations.find();
        }

        return operations;
    }
}
