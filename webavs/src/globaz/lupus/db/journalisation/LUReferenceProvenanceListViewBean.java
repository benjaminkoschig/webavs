/*
 * Créé le 6 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.journalisation.db.journalisation.access.JOReferenceProvenanceManager;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUReferenceProvenanceListViewBean extends JOReferenceProvenanceManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String PROVENANCE_VIEWBEAN = "ProvenanceViewBean";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LUReferenceProvenanceViewBean();
    }

    public void addReferenceProvenance(BSession session, String idJournalisation, String typeReferenceProvenance,
            String idCleReferenceProvenance) throws Exception {
        addReferenceProvenance(null, session, idJournalisation, idCleReferenceProvenance, typeReferenceProvenance);
    }

    public void addReferenceProvenance(BTransaction transaction, BSession session, String idJournalisation,
            String typeReferenceProvenance, String idCleReferenceProvenance) throws Exception {
        LUReferenceProvenanceViewBean reference = new LUReferenceProvenanceViewBean();
        reference.setSession(session);
        reference.setIdJournalisation(idJournalisation);
        reference.setTypeReferenceProvenance(typeReferenceProvenance);
        reference.setIdCleReferenceProvenance(idCleReferenceProvenance);
        reference.add(transaction);
        _getContainer().add(reference);
    }

    public LUReferenceProvenanceViewBean getReferenceProvenance(int i) {
        super.wantLoadPrimaryKeyOnly(false);
        return (LUReferenceProvenanceViewBean) super.getEntity(i);
    }

}
