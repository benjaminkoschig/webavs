package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.bouclement.access.TUNoPassage;
import globaz.tucana.exception.fw.TUDeleteException;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed May 03 13:47:49 CEST 2006
 */
public class TUBouclementViewBean extends TUBouclement implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Table : TUBPBOU */
    private String forCanton = new String();
    private String forCsApplication = new String();
    private String forCsRubrique = new String();
    private String forCsTypeRubrique = new String();
    private String forLibelleRubrique = new String();
    private TUNoPassageViewBean passageACM = null;
    private TUNoPassageViewBean passageAF = null;
    private TUNoPassageViewBean passageCA = null;
    private TUNoPassageViewBean passageCG = null;

    /**
     * Constructeur de la classe TUBouclementViewBean
     */
    public TUBouclementViewBean() {
        super();
        passageCA = new TUNoPassageViewBean();
        passageCG = new TUNoPassageViewBean();
        passageAF = new TUNoPassageViewBean();
        passageACM = new TUNoPassageViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);
        // Supprime tous les num�ros de passages
        suppressionNoPassage(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        // Chargement compta auxiliaire
        passageCA.setIdBouclement(getIdBouclement());
        passageCA.setCsApplication(ITUCSConstantes.CS_APPLICATION_CA);
        passageCA.setAlternateKey(TUNoPassage.KEY_ALTERNATE_BOUCLEMENT);
        passageCA.retrieve();
        // Chargement compta g�n�rale
        passageCG.setIdBouclement(getIdBouclement());
        passageCG.setCsApplication(ITUCSConstantes.CS_APPLICATION_CG);
        passageCG.setAlternateKey(TUNoPassage.KEY_ALTERNATE_BOUCLEMENT);
        passageCG.retrieve();
        // Chargement ACM
        passageACM.setIdBouclement(getIdBouclement());
        passageACM.setCsApplication(ITUCSConstantes.CS_APPLICATION_ACM);
        passageACM.setAlternateKey(TUNoPassage.KEY_ALTERNATE_BOUCLEMENT);
        passageACM.retrieve();
        // Chargement AF
        passageAF.setIdBouclement(getIdBouclement());
        passageAF.setCsApplication(ITUCSConstantes.CS_APPLICATION_AF);
        passageAF.setAlternateKey(TUNoPassage.KEY_ALTERNATE_BOUCLEMENT);
        passageAF.retrieve();
    }

    /**
     * R�cup�re la r�f�rence du canton
     * 
     * @return
     */
    public String getForCanton() {
        return forCanton;
    }

    /**
     * R�cup�re la r�f�rence du code syst�me application
     * 
     * @return
     */
    public String getForCsApplication() {
        return forCsApplication;
    }

    /**
     * R�cup�re la r�f�rence du code syst�me rubrique
     * 
     * @return
     */
    public String getForCsRubrique() {
        return forCsRubrique;
    }

    /**
     * R�cup�re la r�f�rence du code syst�me type de rubrique
     * 
     * @return
     */
    public String getForCsTypeRubrique() {
        return forCsTypeRubrique;
    }

    /**
     * R�cup�re la r�f�rence du libell� rubrique
     * 
     * @return
     */
    public String getForLibelleRubrique() {
        return forLibelleRubrique;
    }

    /**
     * @return l'entit� de passage ACM
     */
    public TUNoPassageViewBean getPassageACM() {
        return passageACM;
    }

    /**
     * @return l'entit� de passage AF
     */
    public TUNoPassageViewBean getPassageAF() {
        return passageAF;
    }

    /**
     * @return l'entit� de passage CA
     */
    public TUNoPassageViewBean getPassageCA() {
        return passageCA;
    }

    /**
     * @return l'entit� de passage CG
     */
    public TUNoPassageViewBean getPassageCG() {
        return passageCG;
    }

    /**
     * Modifie la r�f�rence du canton
     * 
     * @param string
     */
    public void setForCanton(String string) {
        forCanton = string;
    }

    /**
     * Modifie la r�f�rence du code syst�me canton
     * 
     * @param string
     */
    public void setForCsApplication(String string) {
        forCsApplication = string;
    }

    /**
     * Modifie la r�f�rence du code syst�me rubrique
     * 
     * @param string
     */
    public void setForCsRubrique(String string) {
        forCsRubrique = string;
    }

    /**
     * Modifie la r�f�rence du code syst�me type rubrique
     * 
     * @param string
     */
    public void setForCsTypeRubrique(String string) {
        forCsTypeRubrique = string;
    }

    /**
     * Modifie la r�f�rence du libelle rubrique
     * 
     * @param string
     */
    public void setForLibelleRubrique(String string) {
        forLibelleRubrique = string;
    }

    /**
     * @param bean
     *            d'une entit� de passage ACM
     */
    public void setPassageACM(TUNoPassageViewBean bean) {
        passageACM = bean;
    }

    /**
     * @param bean
     *            d'une entit� de passage AF
     */
    public void setPassageAF(TUNoPassageViewBean bean) {
        passageAF = bean;
    }

    /**
     * @param bean
     *            d'une entit� de passage CA
     */
    public void setPassageCA(TUNoPassageViewBean bean) {
        passageCA = bean;
    }

    /**
     * @param bean
     *            d'une entit� de passage CG
     */
    public void setPassageCG(TUNoPassageViewBean bean) {
        passageCG = bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BAccessBean#setSession(globaz.globall.db.BSession)
     */
    @Override
    public void setSession(BSession newSession) {
        super.setSession(newSession);
        passageCA.setSession(getSession());
        passageCG.setSession(getSession());
        passageAF.setSession(getSession());
        passageACM.setSession(getSession());
    }

    /**
     * Supprime les num�ros de passage pour le bouclement
     */
    private void suppressionNoPassage(BTransaction transaction) throws TUDeleteException {
        // Suppression du passage CA (Osiris) si existant lors de la suppression
        // du bouclement
        if (passageCA != null && !passageCA.isNew()) {
            try {
                passageCA.delete(transaction);
            } catch (Exception e) {
                throw new TUDeleteException("TUBouclementViewBean.suppressionNoPassage() : "
                        + getSession().getLabel("SUPPR_CA") + " " + passageCA.getIdNoPassage(), e.getMessage());
            }
        }
        // Suppression du passage CG (Helios) si existant lors de la suppression
        // du bouclement
        if (passageCG != null && !passageCG.isNew()) {
            try {
                passageCG.delete(transaction);
            } catch (Exception e) {
                throw new TUDeleteException("TUBouclementViewBean.suppressionNoPassage() : "
                        + getSession().getLabel("SUPPR_CG") + " " + passageCG.getIdNoPassage(), e.getMessage());
            }
        }
        // Suppression du passage ACM si existant lors de la suppression du
        // bouclement
        if (passageACM != null && !passageACM.isNew()) {
            try {
                passageACM.delete(transaction);
            } catch (Exception e) {
                throw new TUDeleteException("TUBouclementViewBean.suppressionNoPassage() : "
                        + getSession().getLabel("SUPPR_ACM") + " " + passageACM.getIdNoPassage(), e.getMessage());
            }
        }
        // Suppression du passage AF (Helios) si existant lors de la suppression
        // du bouclement
        if (passageAF != null && !passageAF.isNew()) {
            try {
                passageAF.delete(transaction);
            } catch (Exception e) {
                throw new TUDeleteException("TUBouclementViewBean.suppressionNoPassage() : "
                        + getSession().getLabel("SUPPR_AF") + " " + passageAF.getIdNoPassage(), e.getMessage());
            }
        }

    }

}
