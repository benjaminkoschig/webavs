package globaz.apg.acor.adapter.plat;

import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.tools.PRFilterIterator;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Sous-class permettant la création des fichier ENFANTS pour les prestations maternite
 * </p>
 * 
 * @author vre
 */
public class APFichierEnfantsMatPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private int compteEnfant;
    private Iterator enfants = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe FichierEnfantsMatPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    /**
     * Crée une nouvelle instance de la classe APAbstractFichierDroitPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    protected APFichierEnfantsMatPrinter(APACORDroitMatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected APACORDroitMatAdapter adapter() {
        return (APACORDroitMatAdapter) parent;
    }

    private Iterator enfants() throws PRACORException {
        if (enfants == null) {
            enfants = new PRFilterIterator(adapter().situationsFamiliales().iterator(),
                    APACORDroitMatAdapter.ENFANTS_PREDICATE);
        }

        return enfants;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        return enfants().hasNext();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        APSituationFamilialeMat enfant = (APSituationFamilialeMat) enfants().next(); // il
        // y
        // a
        // forcément
        // au
        // moins
        // un enfant
        // 1. le no avs de l'enfant
        String noAVS = enfant.getNoAVS();

        if (JadeStringUtil.isIntegerEmpty(noAVS)) {
            noAVS = adapter().nssBidon("", PRACORConst.CS_HOMME, "" + compteEnfant, false);
        }

        writeAVS(writer, noAVS);

        // 2. le no avs du pere
        writeAVS(writer, adapter().noAVSPere());

        // 3. le no avs de la mere
        writeAVS(writer, adapter().numeroAVSAssure());

        // 4. enfant receuilli
        writeBoolean(writer, false);

        // 5. date d'adoption
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 7. no AVS de la personne pour l'enfant est à charge
        writeAVSSansFinDeChamp(writer, PRACORConst.CA_NSS_VIDE);

        ++compteEnfant;
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {
        APSituationFamilialeMat enfant = (APSituationFamilialeMat) enfants().next(); // il
        // y
        // a
        // forcément
        // au
        // moins
        // un enfant
        // 1. le no avs de l'enfant
        String noAVS = enfant.getNoAVS();

        if (JadeStringUtil.isIntegerEmpty(noAVS)) {
            noAVS = adapter().nssBidon("", PRACORConst.CS_HOMME, "" + compteEnfant, false);
        }

        writeAVS(writer, noAVS);

        // 2. le no avs du pere
        writeAVS(writer, adapter().noAVSPere());

        // 3. le no avs de la mere
        writeAVS(writer, adapter().numeroAVSAssure());

        // 4. enfant receuilli
        writeBoolean(writer, false);

        // 5. date d'adoption
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 7. no AVS de la personne pour l'enfant est à charge
        writeAVSSansFinDeChamp(writer, PRACORConst.CA_NSS_VIDE);

        ++compteEnfant;
    }
}
