package globaz.helios.db.print;

import globaz.helios.itext.list.CGProcessImpressionListeEcritures;

/**
 * Insérez la description du type ici. Date de création : (08.07.2003 15:58:16)
 * 
 * @author: Administrator
 */
public class CGImprimerListeEcrituresViewBean extends CGProcessImpressionListeEcritures {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGExerciceComptableImprimerPertesProfitsViewBean.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CGImprimerListeEcrituresViewBean() throws Exception {
        super(new globaz.globall.db.BSession(globaz.helios.application.CGApplication.DEFAULT_APPLICATION_HELIOS));
    }
}