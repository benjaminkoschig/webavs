/*
 * Créé le 15 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.envoi;

import globaz.globall.db.BTransaction;
import globaz.journalisation.db.common.access.JOCommonLoggedEntity;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.journalisation.LUJournalListViewBean;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEnvoiListViewBean extends LUJournalListViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static int NBRE_MAX_CRITERE_ENTREE = 3;
    private String forProvenanceModule = "NAOS";

    public LEEnvoiListViewBean() {
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super.setForTypeReferenceProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE);
        super.setForIdCleReferenceProvenance(getForProvenanceModule());
    }

    /**
     * @return
     */
    public String getForProvenanceModule() {
        return forProvenanceModule;
    }

    @Override
    protected JOCommonLoggedEntity newEntity() {
        return new LEEnvoiViewBean();
    }

    /**
     * @param string
     */
    public void setForProvenanceModule(String string) {
        forProvenanceModule = string;
    }
}
