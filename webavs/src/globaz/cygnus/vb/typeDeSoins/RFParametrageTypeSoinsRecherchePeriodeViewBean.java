package globaz.cygnus.vb.typeDeSoins;

import globaz.cygnus.db.typeDeSoins.RFPotAssure;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinJointAssPeriodeJointPotAssure;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Vector;

/**
 * author fha
 */
public class RFParametrageTypeSoinsRecherchePeriodeViewBean extends RFSousTypeDeSoinJointAssPeriodeJointPotAssure
        implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeSousTypeDeSoin = "";
    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";

    /************ attributes ****************************/
    private String forDateDebut = "";
    private String forDateFin = "";

    private transient String forIdPotAssure = "";
    private String forIdSoinPot = "";

    private String forIdSousTypeSoin = "";

    private Boolean isUpdate = Boolean.FALSE;

    private String sousTypeDeSoinParTypeInnerJavascript = "";

    private transient Vector<String[]> typeDeSoinsDemande = null;

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
    public BSpy getCreationSpy() {

        RFPotAssure pot = new RFPotAssure();

        try {
            // on ne charge les spys que si on est en update
            if (getIsUpdate()) {
                pot = RFPotAssure.loadPotAssure(getSession(), getSession().getCurrentThreadTransaction(),
                        getIdPotAssure());
            }
        } catch (Exception e) {
        }
        return pot.getCreationSpy();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdPotAssure() {
        return forIdPotAssure;
    }

    public String getForIdSoinPot() {
        return forIdSoinPot;
    }

    public String getForIdSousTypeSoin() {
        return forIdSousTypeSoin;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        if (JadeStringUtil.isBlank(sousTypeDeSoinParTypeInnerJavascript)) {
            try {
                sousTypeDeSoinParTypeInnerJavascript = RFSoinsListsBuilder.getInstance(getSession())
                        .getSousTypeDeSoinParTypeInnerJavascript();
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return sousTypeDeSoinParTypeInnerJavascript;

    }

    @Override
    public BSpy getSpy() {

        RFPotAssure pot = new RFPotAssure();

        try {
            // on ne charge les spys que si on est en update
            if (getIsUpdate()) {
                pot = RFPotAssure.loadPotAssure(getSession(), getSession().getCurrentThreadTransaction(),
                        getIdPotAssure());
            }
        } catch (Exception e) {
        }
        return pot.getSpy();
    }

    public Vector<String[]> getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemande == null) {
                typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(getSession()).getTypeDeSoinsDemande();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return typeDeSoinsDemande;
    }

    /************ methods ****************************/

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

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdPotAssure(String forIdPotAssure) {
        this.forIdPotAssure = forIdPotAssure;
    }

    public void setForIdSoinPot(String forIdSoinPot) {
        this.forIdSoinPot = forIdSoinPot;
    }

    public void setForIdSousTypeSoin(String forIdSousTypeSoin) {
        this.forIdSousTypeSoin = forIdSousTypeSoin;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void setTypeDeSoinsDemande(Vector<String[]> typeDeSoinsDemande) {
        this.typeDeSoinsDemande = typeDeSoinsDemande;
    }
}
