package globaz.hermes.process;

import globaz.framework.process.FWProcess;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HESupprimerCIProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String motif = null;
    private String nbreInscriptionCI = null;
    private String numeroAvs = null;
    private String numeroCaisse = null;
    private String referenceUnique = null;
    private String referenceUnique38 = null;
    private String statut = null;

    /**
	 *
	 */
    public HESupprimerCIProcess() {
        super();
    }

    /**
     * @param parent
     */
    public HESupprimerCIProcess(FWProcess parent) {
        super(parent);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        setSendCompletionMail(false);
        if (!JadeStringUtil.isEmpty(getReferenceUnique()) && !JadeStringUtil.isEmpty(getNumeroAvs())
                && !JadeStringUtil.isEmpty(getNumeroCaisse()) && !JadeStringUtil.isEmpty(getMotif())
                && !JadeStringUtil.isEmpty(getStatut())) {
            try {
                if (JadeStringUtil.isBlank(getReferenceUnique38()) && !JadeStringUtil.isBlank(getNbreInscriptionCI())
                        && Integer.parseInt(getNbreInscriptionCI()) > 0) {
                    throw new Exception("Echec de suppression, impossible de retrouver le 38 associé");
                }
                if (!IHEAnnoncesViewBean.CS_EN_ATTENTE.equals(getStatut())) {
                    throw new Exception("Impossible de supprimer un extrait qui n'est pas dans le statut en attente");
                }
                // reprendre les 3839 pour les supprimer
                HEOutputAnnonceListViewBean liste3839 = new HEOutputAnnonceListViewBean();
                liste3839.setSession(getSession());
                liste3839.setForCodeApplicationOR("38", "39");
                liste3839.setForRefUnique(getReferenceUnique());
                liste3839.setAndForRefUnique2(getReferenceUnique38());
                liste3839.setForNumeroAVS(getNumeroAvs());
                liste3839.setForNumCaisse(getNumeroCaisse());
                liste3839.setForMotif(getMotif());
                liste3839.wantCallMethodAfter(false);
                liste3839.wantCallMethodAfterFind(false);
                liste3839.wantCallMethodBefore(false);
                liste3839.wantCallMethodBeforeFind(false);
                liste3839.find(getTransaction(), BManager.SIZE_NOLIMIT);
                setProgressScaleValue(liste3839.size());
                setProgressCounter(0);
                for (int i = 0; i < liste3839.size(); i++) {
                    HEOutputAnnonceViewBean crt = (HEOutputAnnonceViewBean) liste3839.getEntity(i);
                    crt.delete(getTransaction());
                    setProgressCounter(i);
                }
                return true;
            } catch (Exception e) {
                getTransaction().addErrors(e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    public String getMotif() {
        return motif;
    }

    public String getNbreInscriptionCI() {
        return nbreInscriptionCI;
    }

    public String getNumeroAvs() {
        return numeroAvs;
    }

    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getReferenceUnique() {
        return referenceUnique;
    }

    public String getReferenceUnique38() {
        return referenceUnique38;
    }

    public String getStatut() {
        return statut;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNbreInscriptionCI(String nbreInscriptionCI) {
        this.nbreInscriptionCI = nbreInscriptionCI;
    }

    public void setNumeroAvs(String numeroAvs) {
        this.numeroAvs = numeroAvs;
    }

    public void setNumeroCaisse(String numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }

    public void setReferenceUnique(String referenceUnique) {
        this.referenceUnique = referenceUnique;
    }

    public void setReferenceUnique38(String referenceUnique38) {
        this.referenceUnique38 = referenceUnique38;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
