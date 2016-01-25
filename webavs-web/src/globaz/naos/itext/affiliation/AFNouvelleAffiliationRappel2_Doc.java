/*
 * Créé le 15.10.07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.affiliation;

import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author jpa
 */
public class AFNouvelleAffiliationRappel2_Doc extends AFNouvelleAffiliation_Doc {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String NUMERO_INFOROM = "0258CAF";

    public AFNouvelleAffiliationRappel2_Doc() throws Exception {
        super();
    }

    public AFNouvelleAffiliationRappel2_Doc(BSession session) throws Exception {
        super(session);
    }

    @Override
    public String getNumeroRappel() {
        return getSession().getLabel("NAOS_RAPPEL_2");
    }

    @Override
    protected String giveNumeroInforom() throws Exception {
        return AFNouvelleAffiliationRappel2_Doc.NUMERO_INFOROM;
    }

}
