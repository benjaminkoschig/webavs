/*
 * Cr�� le 15.10.07
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.affiliation;

import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author jpa
 */
public class AFNouvelleAffiliationRappel_Doc extends AFNouvelleAffiliation_Doc {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String NUMERO_INFOROM = "0257CAF";

    public AFNouvelleAffiliationRappel_Doc() throws Exception {
        super();
    }

    public AFNouvelleAffiliationRappel_Doc(BSession session) throws Exception {
        super(session);
    }

    @Override
    public String getNumeroRappel() {
        return getSession().getLabel("NAOS_RAPPEL_1");
    }

    @Override
    protected String giveNumeroInforom() throws Exception {
        return AFNouvelleAffiliationRappel_Doc.NUMERO_INFOROM;
    }

}
