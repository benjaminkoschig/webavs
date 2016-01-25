/*
 * Créé le 12 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.process;

import globaz.framework.process.FWProcess;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.jade.log.JadeLogger;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEAnnulerAnnonceProcess extends FWProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String motif;
    String referenceUnique;

    /**
	 *
	 */
    public HEAnnulerAnnonceProcess() {
        super();
    }

    /**
     * @param session
     */
    public HEAnnulerAnnonceProcess(BSession session) {
        super(session);
    }

    /**
     * @param parent
     */
    public HEAnnulerAnnonceProcess(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() {
        setSendCompletionMail(false);
        if (!JAUtil.isStringEmpty(getReferenceUnique())) {
            // idAnnonce n'est pas vide --> donc on peut annuler l'arc
            // correspondant
            HEOutputAnnonceListViewBean arcs = new HEOutputAnnonceListViewBean();
            arcs.setSession(getSession());
            arcs.setForRefUnique(getReferenceUnique());
            arcs.setForMotif(getMotif());
            HEOutputAnnonceViewBean crtArc;
            try {
                // on met à jour toutes les arcs portant cette reférence unique
                // et ce motif
                arcs.find(getTransaction());
                for (int i = 0; i < arcs.size(); i++) {
                    crtArc = (HEOutputAnnonceViewBean) arcs.getEntity(i);
                    crtArc.setStatut(IHEAnnoncesViewBean.CS_PROBLEME);
                    crtArc.wantCallMethodAfter(false);
                    crtArc.wantCallMethodBefore(false);
                    crtArc.wantCallValidate(false);
                    crtArc.update(getTransaction());
                }
                // on recherche encore les attentes qui non pas de référence sur
                // un arc
                HEAttenteRetourListViewBean list = new HEAttenteRetourListViewBean();
                list.setSession(getSession());
                list.setForReferenceUnique(getReferenceUnique());
                list.setForMotif(getMotif());
                list.setForIdAnnonceRetour("0");
                list.wantCallMethodAfter(false);
                list.wantCallMethodAfterFind(false);
                list.wantCallMethodBefore(false);
                list.wantCallMethodBeforeFind(false);
                list.find(getTransaction(), BManager.SIZE_NOLIMIT);
                for (int i = 0; i < list.size(); i++) {
                    HEAttenteRetourViewBean entity = (HEAttenteRetourViewBean) list.getEntity(i);
                    entity.wantCallMethodAfter(false);
                    entity.wantCallMethodBefore(false);
                    entity.wantCallValidate(false);
                    entity.delete(getTransaction());
                }
            } catch (Exception e) {
                JadeLogger.info("HEAnnulerAnnonceProcess : ref unique=" + getReferenceUnique() + " motif :"
                        + getMotif(), e);
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * @return
     */
    public String getMotif() {
        return motif;
    }

    /**
     * @return
     */
    public String getReferenceUnique() {
        return referenceUnique;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param string
     */
    public void setMotif(String mot) {
        motif = mot;
    }

    /**
     * @param string
     */
    public void setReferenceUnique(String ref) {
        referenceUnique = ref;
    }

}
