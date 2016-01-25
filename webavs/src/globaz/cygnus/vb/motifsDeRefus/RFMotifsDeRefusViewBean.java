package globaz.cygnus.vb.motifsDeRefus;

import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Vector;

/**
 * author fha
 */
public class RFMotifsDeRefusViewBean extends RFMotifsDeRefus implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeSousTypeDeSoin = "";

    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";

    private String descriptionDE = "";
    private String descriptionFR = "";

    private String descriptionIT = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forIdMotifRefus = "";

    private String idSousTypeDeSoin = "";
    // ~ Liste des ìd fournisseurs-type de soin à supprimer
    private ArrayList idSuppressionMotifArray = new ArrayList();

    private String idTypeDeSoin = "";
    // ~ Liste des objets fournisseurs-type de soin
    private ArrayList SoinMotifArray = new ArrayList();

    // gestion des types de soin
    private transient String sousTypeDeSoinParTypeInnerJavascript = "";

    private transient Vector typeDeSoinsDemande = null;

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

    @Override
    public String getDescriptionDE() {
        return descriptionDE;
    }

    @Override
    public String getDescriptionFR() {
        return descriptionFR;
    }

    @Override
    public String getDescriptionIT() {
        return descriptionIT;
    }

    public String getForIdMotifRefus() {
        return forIdMotifRefus;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public ArrayList getIdSuppressionMotifArray() {
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

    public ArrayList getSoinMotifArray() {
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

    public Vector getTypeDeSoinData() {
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

    public Vector getTypeDeSoinsDemande() {
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

    @Override
    public void setDescriptionDE(String descriptionDE) {
        this.descriptionDE = descriptionDE;
    }

    @Override
    public void setDescriptionFR(String descriptionFR) {
        this.descriptionFR = descriptionFR;
    }

    @Override
    public void setDescriptionIT(String descriptionIT) {
        this.descriptionIT = descriptionIT;
    }

    public void setForIdMotifRefus(String forIdMotifRefus) {
        this.forIdMotifRefus = forIdMotifRefus;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdSuppressionMotifArray(ArrayList idSuppressionMotifArray) {
        this.idSuppressionMotifArray = idSuppressionMotifArray;
    }

    public void setIdTypeDeSoin(String idTypeDeSoin) {
        this.idTypeDeSoin = idTypeDeSoin;
    }

    public void setSoinMotifArray(ArrayList soinMotifArray) {
        SoinMotifArray = soinMotifArray;
    }

    public void setSousTypeDeSoinParTypeInnerJavascript(String sousTypeDeSoinParTypeInnerJavascript) {
        this.sousTypeDeSoinParTypeInnerJavascript = sousTypeDeSoinParTypeInnerJavascript;
    }

    public void setTypeDeSoinsDemande(Vector typeDeSoinsDemande) {
        this.typeDeSoinsDemande = typeDeSoinsDemande;
    }

}
