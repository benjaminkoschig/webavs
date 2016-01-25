/*
 * Créé le 06 oct. 05
 */
package globaz.ij.helpers.lots;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.db.comptes.CASection;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRSession;
import java.util.Collection;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFactureACompenserHelper extends PRAbstractHelper {

    /** DOCUMENT ME! */
    public static final String ATTR_ID_FACTURE = "idFacture";

    /** DOCUMENT ME! */
    public static final String ATTR_NO_FACTURE = "noFacture";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private static String appendOptionList(String noFacture, String idFacture, String soldeSection) {

        StringBuffer options = new StringBuffer();
        options.append("<option value='");
        options.append(noFacture);

        /* ID_FACTURE */
        options.append("' " + ATTR_ID_FACTURE + "=\"");
        options.append(idFacture);
        options.append("\"");

        /* NO_FACTURE */
        options.append(" " + ATTR_NO_FACTURE + "=\"");
        options.append(noFacture);
        options.append("\"");

        options.append(">");

        /* la partie visible a l'ecran */
        options.append(noFacture + " - " + soldeSection);

        options.append("</option>\n");

        return options.toString();
    }

    /**
     * @param idTiers
     *            l'idTiers de la personne susceptible de devoir compenser des trucs
     * @param montant
     *            montant qu'on peut compenser
     * 
     * @return une Collection de CAPropositionCompensation
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static String getCollectionSectionsACompenser(BSession session, String idTiers, String montantReel)
            throws Exception {
        BISession sessionOsiris = PRSession.connectSession(session, "OSIRIS");

        Collection compensations;
        FWCurrency montant = new FWCurrency(montantReel);

        if (montant.isZero()) {

            return "";

        } else {

            APIPropositionCompensation propositionCompensation = (APIPropositionCompensation) sessionOsiris
                    .getAPIFor(APIPropositionCompensation.class);

            // on utilise le montant de la prestation APG uniquement pour
            // retrouver le signe
            FWCurrency montantTotalNegate = new FWCurrency(montant.toString());
            montantTotalNegate.negate();

            // on construit une valeur de meme signe la plus grande possible
            // pour retrouver
            // toutes les sections
            if (montantTotalNegate.isPositive()) {
                montantTotalNegate = new FWCurrency("9999999.99");
            } else {
                montantTotalNegate = new FWCurrency("-9999999.99");
            }

            compensations = propositionCompensation.propositionCompensation(Integer.parseInt(idTiers),
                    montantTotalNegate, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

            // on construit le string option pour toutes les compensations
            // retrouvee
            Iterator iter = compensations.iterator();
            StringBuffer options = new StringBuffer();

            while (iter.hasNext()) {
                CASection section = (CASection) iter.next();

                options.append(appendOptionList(section.getIdExterne(), section.getIdSection(),
                        section.getSoldeFormate()));
            }

            return options.toString();
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }
}
