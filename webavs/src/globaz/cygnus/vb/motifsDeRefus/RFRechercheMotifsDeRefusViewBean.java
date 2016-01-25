package globaz.cygnus.vb.motifsDeRefus;

import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * author fha
 */
public class RFRechercheMotifsDeRefusViewBean extends RFMotifsDeRefus implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeSousTypeDeSoin = "";

    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";

    private String codeTypeDeSoinList = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forIdMotifRefus = "";
    private Boolean hideHasMontant = Boolean.FALSE;

    private String idSousTypeDeSoin = "";
    // ~ Liste des id fournisseurs-type de soin à supprimer
    private List<String> idSuppressionMotifArray = new ArrayList<String>();

    private String idTypeDeSoin = "";
    private Boolean isFirstLoad = Boolean.FALSE;

    private Boolean isUpdate = Boolean.FALSE;
    // ~ Liste des objets fournisseurs-type de soin
    private List<RFSoinMotif> SoinMotifArray = new ArrayList<RFSoinMotif>();

    // gestion des types de soin
    private transient String sousTypeDeSoinParTypeInnerJavascript = "";

    private transient Vector<String[]> typeDeSoinsDemande = null;

    // ~ methods
    // ------------------------------------------------------------------------------------------------------

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeSousTypeDeSoinList() {
        return codeSousTypeDeSoinList;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCodeTypeDeSoinList() {
        return codeTypeDeSoinList;
    }

    public String getForIdMotifRefus() {
        return forIdMotifRefus;
    }

    public Boolean getHideHasMontant() {
        return hideHasMontant;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public List<String> getIdSuppressionMotifArray() {
        return idSuppressionMotifArray;
    }

    public String getIdTypeDeSoin() {
        return idTypeDeSoin;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    public Boolean getIsFirstLoad() {
        return isFirstLoad;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public List<RFSoinMotif> getSoinMotifArray() {
        return SoinMotifArray;
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        if (JadeStringUtil.isBlank(sousTypeDeSoinParTypeInnerJavascript)) {
            try {
                sousTypeDeSoinParTypeInnerJavascript = RFSoinsListsBuilder.getInstance(((BSession) getISession()))
                        .getSousTypeDeSoinParTypeInnerJavascript();
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(ERROR);
            }
        }

        return sousTypeDeSoinParTypeInnerJavascript;
    }

    public Vector<String[]> getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemande == null) {
                typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(((BSession) getISession()))
                        .getTypeDeSoinsDemande();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(ERROR);
        }

        return typeDeSoinsDemande;
    }

    public Vector<String[]> getTypeDeSoinsDemande() {
        return typeDeSoinsDemande;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeSousTypeDeSoinList(String codeSousTypeDeSoinList) {
        this.codeSousTypeDeSoinList = codeSousTypeDeSoinList;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCodeTypeDeSoinList(String codeTypeDeSoinList) {
        this.codeTypeDeSoinList = codeTypeDeSoinList;
    }

    public void setForIdMotifRefus(String forIdMotifRefus) {
        this.forIdMotifRefus = forIdMotifRefus;
    }

    public void setHideHasMontant(Boolean hideHasMontant) {
        this.hideHasMontant = hideHasMontant;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdSuppressionMotifArray(List<String> idSuppressionMotifArray) {
        this.idSuppressionMotifArray = idSuppressionMotifArray;
    }

    public void setIdTypeDeSoin(String idTypeDeSoin) {
        this.idTypeDeSoin = idTypeDeSoin;
    }

    public void setIsFirstLoad(Boolean isFirstLoad) {
        this.isFirstLoad = isFirstLoad;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void setSoinMotifArray(List<RFSoinMotif> soinMotifArray) {
        SoinMotifArray = soinMotifArray;
    }

    public void setSousTypeDeSoinParTypeInnerJavascript(String sousTypeDeSoinParTypeInnerJavascript) {
        this.sousTypeDeSoinParTypeInnerJavascript = sousTypeDeSoinParTypeInnerJavascript;
    }

    public void setTypeDeSoinsDemande(Vector<String[]> typeDeSoinsDemande) {
        this.typeDeSoinsDemande = typeDeSoinsDemande;
    }

}
