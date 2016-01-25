package globaz.draco.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;

public class DSInscriptionsCalculMontantCI extends BManager {

    private static final long serialVersionUID = -8974425047433406006L;
    private String forAnnee = "";
    private String forCategoriePersonnel = "";
    private String forCodeCanton = "";
    private String forCompteIndividuelId = "";
    private String forIdDeclaration = "";
    private String fromDateSexeFemme = "";
    private String fromDateSexeHomme = "";
    private String notForInCatPerso = "";
    private String untilDateSexeFemme = "";
    private String untilDateSexeHomme = "";

    public DSInscriptionsCalculMontantCI() {
        super();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = new String();
        String firstUnion = " (select " + _getCollection() + "CIECRIP.KBIECR,KBMMON from " + _getCollection()
                + _getTableName();
        String secondUnion = " AND KBTEXT IN (0,311006,311002,311008) union select " + _getCollection()
                + "CIECRIP.KBIECR,-KBMMON AS KBMMON from " + _getCollection() + _getTableName();
        String endUnion = " AND KBTEXT IN (311001,311003,311004,311005,311007,311009)) as RESULT";
        String whereUnion = "";
        // Récupération du CI, du tiers/partenaire
        // Obligation de renommée la table CIINDIP pour requête sur le
        // partenaire
        joinStr += " left outer join " + _getCollection() + "CIECRIP on " + _getCollection() + _getTableName()
                + ".KBIECR=" + _getCollection() + "CIECRIP.KBIECR" + " left outer join " + _getCollection()
                + "CIINDIP on " + _getCollection() + _getTableName() + ".KAIIND=" + _getCollection() + "CIINDIP.KAIIND"
                + " inner join " + _getCollection() + "DSDECLP on " + _getCollection() + _getTableName() + ".TAIDDE="
                + _getCollection() + "DSDECLP.TAIDDE";
        if ((forCompteIndividuelId != null) && (forCompteIndividuelId.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KAIIND=" + forCompteIndividuelId;
        }
        if ((forCodeCanton != null) && (forCodeCanton.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "TETCAN=" + forCodeCanton;
        }
        if ((forIdDeclaration != null) && (forIdDeclaration.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += _getCollection() + "DSINDP.TAIDDE=" + forIdDeclaration;
        }
        if (getNotForInCatPerso().length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            }
            whereUnion += _getCollection() + "DSINDP.TETCPE NOT IN (" + getNotForInCatPerso() + ")";
        }
        if ((forCategoriePersonnel != null) && (forCategoriePersonnel.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "TETCPE=" + forCategoriePersonnel;
        }
        if ((forAnnee != null) && (forAnnee.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KBNANN=" + forAnnee;
        }
        // until date différenciée homme / femme
        if (!JadeStringUtil.isBlankOrZero(untilDateSexeFemme) && !JadeStringUtil.isBlankOrZero(untilDateSexeHomme)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            }
            whereUnion += " ((KADNAI >="
                    + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + untilDateSexeFemme)
                    + " AND KATSEX = " + CICompteIndividuel.CS_FEMME + ") " + "OR (KADNAI >= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + untilDateSexeHomme)
                    + " AND KATSEX = " + CICompteIndividuel.CS_HOMME + ")) ";
        }
        if (!JadeStringUtil.isBlankOrZero(fromDateSexeFemme) && !JadeStringUtil.isBlankOrZero(fromDateSexeFemme)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            }
            whereUnion += " ((KADNAI <="
                    + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + fromDateSexeFemme) + " AND KATSEX = "
                    + CICompteIndividuel.CS_FEMME + ") " + "OR (KADNAI <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + fromDateSexeHomme) + " AND KATSEX = "
                    + CICompteIndividuel.CS_HOMME + ")) ";
        }
        return firstUnion + joinStr + whereUnion + secondUnion + joinStr + whereUnion + endUnion;
    }

    protected String _getTableName() {
        return "DSINDP";
    }

    @Override
    protected BEntity _newEntity() throws Exception {

        return null;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCategoriePersonnel() {
        return forCategoriePersonnel;
    }

    /**
     * @return
     */
    public String getForCodeCanton() {
        return forCodeCanton;
    }

    /**
     * @return
     */
    public String getForCompteIndividuelId() {
        return forCompteIndividuelId;
    }

    /**
     * @return
     */
    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    public String getFromDateSexeFemme() {
        return fromDateSexeFemme;
    }

    public String getFromDateSexeHomme() {
        return fromDateSexeHomme;
    }

    public String getNotForInCatPerso() {
        return notForInCatPerso;
    }

    public String getUntilDateSexeFemme() {
        return untilDateSexeFemme;
    }

    public String getUntilDateSexeHomme() {
        return untilDateSexeHomme;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCategoriePersonnel(String forCategoriePersonnel) {
        this.forCategoriePersonnel = forCategoriePersonnel;
    }

    /**
     * @param string
     */
    public void setForCodeCanton(String string) {
        forCodeCanton = string;
    }

    /**
     * @param string
     */
    public void setForCompteIndividuelId(String string) {
        forCompteIndividuelId = string;
    }

    /**
     * @param string
     */
    public void setForIdDeclaration(String string) {
        forIdDeclaration = string;
    }

    public void setFromDateSexeFemme(String fromDateSexeFemme) {
        this.fromDateSexeFemme = fromDateSexeFemme;
    }

    public void setFromDateSexeHomme(String fromDateSexeHomme) {
        this.fromDateSexeHomme = fromDateSexeHomme;
    }

    public void setNotForInCatPerso(String notForInCatPerso) {
        this.notForInCatPerso = notForInCatPerso;
    }

    public void setUntilDateSexeFemme(String untilDateSexeFemme) {
        this.untilDateSexeFemme = untilDateSexeFemme;
    }

    public void setUntilDateSexeHomme(String untilDateSexeHomme) {
        this.untilDateSexeHomme = untilDateSexeHomme;
    }

}
