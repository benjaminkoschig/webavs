package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.translation.FWTranslation;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.pyxis.constantes.IConstantes;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;

/**
 * Insérez la description du type ici. Date de création : (16.10.2002 08:25:57)
 * 
 * @author: Administrator
 */
public class CIDossierSplittingViewBean extends CIDossierSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private String idAnnonceRCI = null;
    private Boolean isArchivage = new Boolean(false);
    private java.lang.String message = null;
    private java.lang.String msgType = null;
    private ByteArrayOutputStream outImpression;
    private String refUniqueRCI = null;

    /**
     * Commentaire relatif au constructeur CIDossierSplittingViewBean.
     */
    public CIDossierSplittingViewBean() {
        super();

    }

    public java.lang.String getAction() {
        return action;
    }

    /**
     * Returns the idAnnonceRCI.
     * 
     * @return String
     */
    public String getIdAnnonceRCI() {
        return idAnnonceRCI;
    }

    /**
     * @return
     */
    @Override
    public Boolean getIsArchivage() {
        return isArchivage;
    }

    /**
     * Returns the outImpression.
     * 
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream getOutImpression() {
        return outImpression;
    }

    /**
     * Returns the refUniqueRCI.
     * 
     * @return String
     */
    public String getRefUniqueRCI() {
        return refUniqueRCI;
    }

    public HashSet<String> giveListLangueToExcept() {
        HashSet<String> theListLangueToExcept = new HashSet<String>();

        theListLangueToExcept.add("503003");
        theListLangueToExcept.add("503005");
        theListLangueToExcept.add("503006");
        theListLangueToExcept.add("503007");

        return theListLangueToExcept;
    }

    public HashSet<String> giveListTitreToExcept() {
        HashSet<String> theListTitreToExcept = new HashSet<String>();

        try {
            FWParametersSystemCodeManager theCodeSystemManager = FWTranslation.getSystemCodeList("PYTITRE",
                    getSession());

            for (Object record : theCodeSystemManager) {
                FWParametersSystemCode theCodeSystem = (FWParametersSystemCode) record;
                if (!IConstantes.CS_TIERS_TITRE_MADAME.equalsIgnoreCase(theCodeSystem.getIdCode())
                        && !IConstantes.CS_TIERS_TITRE_MONSIEUR.equalsIgnoreCase(theCodeSystem.getIdCode())) {
                    theListTitreToExcept.add(theCodeSystem.getIdCode());
                }
            }
        } catch (Exception e) {
            // Nothing to do : return an empty list
        }

        return theListTitreToExcept;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    /**
     * Sets the idAnnonceRCI.
     * 
     * @param idAnnonceRCI
     *            The idAnnonceRCI to set
     */
    public void setIdAnnonceRCI(String idAnnonceRCI) {
        this.idAnnonceRCI = idAnnonceRCI;
    }

    /**
     * @param boolean1
     */
    @Override
    public void setIsArchivage(Boolean boolean1) {
        isArchivage = boolean1;
    }

    /**
     * Sets the outImpression.
     * 
     * @param outImpression
     *            The outImpression to set
     */
    public void setOutImpression(ByteArrayOutputStream outImpression) {
        this.outImpression = outImpression;
    }

    /**
     * Sets the refUniqueRCI.
     * 
     * @param refUniqueRCI
     *            The refUniqueRCI to set
     */
    public void setRefUniqueRCI(String refUniqueRCI) {
        this.refUniqueRCI = refUniqueRCI;
    }

}
