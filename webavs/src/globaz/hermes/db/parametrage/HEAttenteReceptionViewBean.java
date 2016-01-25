package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;

/**
 * Insérez la description du type ici. Date de création : (25.03.2003 11:15:17)
 * 
 * @author: Administrator
 */
public class HEAttenteReceptionViewBean extends HEAttenteEnvoiViewBean implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected HEAttenteReceptionChampsListViewBean champs = new HEAttenteReceptionChampsListViewBean();

    public HEAttenteReceptionChampsViewBean getChampsReceptionAt(int index) {
        return (HEAttenteReceptionChampsViewBean) champs.getEntity(index);
    }

    /**
     * @see globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean#getChampsSize()
     */
    @Override
    public int getChampsSize() {
        return champs.size();
    }

    /**
     * @see globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean#getTypeEnvoi()
     */
    @Override
    public String getTypeEnvoi() {
        try {
            FWParametersSystemCodeManager code = ((HEApplication) getSession().getApplication())
                    .getCsCodeApplicationListe(getSession());
            int codeApp = Integer.parseInt(getEnregistrement().substring(0, 2));
            switch (codeApp) {
                case 11:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_11_ANNONCE_ARC).getCurrentCodeUtilisateur()
                            .getLibelle();
                case 20:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_20_ACCUSE_RECEPTION_ARC)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 21:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_21_AUTORISATION_OUVERTURE_CI)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 22:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_22_RCI_OU_ORDRE_SPLITTING)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 23:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_23_CI_COMPLEMENT).getCurrentCodeUtilisateur()
                            .getLibelle();
                case 24:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_24_COMMUNICATION_ETAT_REGISTRE_ASSURES_CENTRALE)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 25:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_25_CONFIRMATION_RCI_OU_ORDRE_SPLITTING)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 29:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_29_REVOCATION_RCI_OU_ORDRE_SPLITTING)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 38:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_38_EXTRAIT_CI_DONNEES)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 39:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_39_EXTRAIT_CI_CONTROLE)
                            .getCurrentCodeUtilisateur().getLibelle()
                            + " (" + getCaisse() + ")";
                case 52:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_52_CAS_DECES_ANNONCES_CENTRALE_AUX_CAISSES)
                            .getCurrentCodeUtilisateur().getLibelle();
                case 50:
                    return code.getCodeSysteme(IHEAnnoncesViewBean.CS_50_REPONSE_CENTRALE_AUX_CAISSES)
                            .getCurrentCodeUtilisateur().getLibelle();
                default:
                    return "?";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "XXX";
        }
    }

    /**
     * @see globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean#loadChamps()
     */
    @Override
    public void loadChamps() {
        champs.setSession(getSession());
        // champs.setForIdAttenteRetour(idAttenteRetour);
        champs.setForRefUnique(getRefUnique());
        champs.setLikeCodeApplication(getEnregistrement().substring(0, 2));
        champs.setIsArchivage(isArchivage());
        try {
            champs.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            e.printStackTrace();
            champs.setMsgType(FWViewBeanInterface.ERROR);
            champs.setMessage(e.getMessage());
        }
    }
}
