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
public class AFRadiationRappel_Doc extends AFRadiation_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFRadiationRappel_Doc() throws Exception {
        super();
    }

    public AFRadiationRappel_Doc(BSession session) throws Exception {
        super(session);
    }

    @Override
    public String getNumeroRappel() {
        return getSession().getLabel("NAOS_RAPPEL_1");
    }

}
