package globaz.musca.db.facturation;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.musca.api.IFAPrintManageDoc;
import java.io.Serializable;

public class FAEnteteFactureAPGManager extends BManager implements Serializable, IFAPrintManageDoc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean _useManagerForCompensation = false;
    private boolean _useManagerForCompensationAnnexe = false;
    private boolean _useManagerForLSV = false; // Le manager sur le LSV
    // recherche une adresse de
    // paiment avec le un LSV active
    private String forIdEntete = new String();
    private String forIdExterneFacture = new String();
    private String forIdExterneRole = new String();
    private String forIdPassage = new String();
    private String forIdRemarque = new String();
    private String forIdRole = new String();
    private String forIdSousType = new String();
    private String forIdTiers = new String();
    private String forIdTypeFacture = new String();
    private String forIdTypeSection = new String();
    private String forTotalFacture = new String();
    private String groupBy = "";
    private String orderBy = new String();
    private boolean wantModeRecouvrementAutomatiqueOuDirect = false;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAEnteteFactureAPG.APG_TABLE_FIELDS;

    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = new String();
        from = _getCollection()
                + "FAENTFP AS FAENTFP "
                + "INNER JOIN "
                + _getCollection()
                + "CACPTAP AS CACPTAP ON (FAENTFP.IDROLE = CACPTAP.IDROLE AND FAENTFP.IDEXTERNEROLE = CACPTAP.IDEXTERNEROLE AND FAENTFP.IDTIERS = CACPTAP.IDTIERS) "
                + "INNER JOIN " + _getCollection()
                + "CASECTP AS CASECTP ON (CACPTAP.IDCOMPTEANNEXE = CASECTP.IDCOMPTEANNEXE) ";
        return from;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 09:21:13)
     * 
     * @return String
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        return groupBy;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return " FAENTFP.IDEXTERNEROLE ASC, CASECTP.IDEXTERNE ASC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "(CASECTP.CATEGORIESECTION=227016 OR CASECTP.CATEGORIESECTION=227021 OR CASECTP.CATEGORIESECTION=227028 OR CASECTP.CATEGORIESECTION=227029 OR CASECTP.CATEGORIESECTION=227015) AND CASECTP.SOLDE<>0 ";

        if (getForIdEntete().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDENTETEFACTURE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdEntete());
        }

        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDPASSAGE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDTIERS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        if (getForIdRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDROLE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRole());
        }

        if (getForIdExterneRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEROLE="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneRole());
        }

        if (getForIdTypeFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDTYPEFACTURE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeFacture());
        }

        if (getForIdSousType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDSOUSTYPE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSousType());
        }

        if (getForIdExterneFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEFACTURE="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneFacture());
        }

        if (getForTotalFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.TOTALFACTURE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForTotalFacture());
        }

        if (getForIdTypeSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CASECTP.IDTYPESECTION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection());
        }

        return sqlWhere;
    }

    /**
     * Insert the method's description here. Creation date: (17.06.2003 08:41:01)
     * 
     * @return boolean
     */
    public boolean _isUseManagerForCompensation() {
        return _useManagerForCompensation;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 08:23:12)
     * 
     * @return boolean
     */
    public boolean _isUseManagerForCompensationAnnexe() {
        return _useManagerForCompensationAnnexe;
    }

    public boolean _isUseManagerForLSV() {
        return _useManagerForLSV;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        if (_isUseManagerForCompensationAnnexe()) {
            return new FAEnteteCompteAnnexe();
        } else if (_isUseManagerForLSV()) {
            FAEnteteFactureAPG entete = new FAEnteteFactureAPG();
            entete.setUseEntityForLSV(true);
            return entete;
        } else {
            return new FAEnteteFactureAPG();
        }
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 09:18:31)
     * 
     * @param newGroupBy
     *            String
     */
    public void _setGroupBy(String newGroupBy) {
        groupBy = newGroupBy;
    }

    /**
     * Insert the method's description here. Creation date: (17.06.2003 08:41:01)
     * 
     * @param new_useManagerForCompensation
     *            boolean
     */
    public void _setUseManagerForCompensation(boolean new_useManagerForCompensation) {
        _useManagerForCompensation = new_useManagerForCompensation;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 08:23:12)
     * 
     * @param new_useManagerForCompensationAnnexe
     *            boolean
     */
    public void _setUseManagerForCompensationAnnexe(boolean new_useManagerForCompensationAnnexe) {
        _useManagerForCompensationAnnexe = new_useManagerForCompensationAnnexe;
    }

    /**
     * Getter
     */
    public String getForIdEntete() {
        return forIdEntete;
    }

    public String getForIdExterneFacture() {
        return forIdExterneFacture;
    }

    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getForIdRemarque() {
        return forIdRemarque;
    }

    public String getForIdRole() {
        return forIdRole;
    }

    public String getForIdSousType() {
        return forIdSousType;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTypeFacture() {
        return forIdTypeFacture;
    }

    /**
     * @return
     */
    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    public String getForTotalFacture() {
        return forTotalFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 14:46:28)
     * 
     * @return String
     */
    public String getOrderBy() {
        if (orderBy == null) {
            return "";
        } else {
            return orderBy;
        }
    }

    public boolean isWantModeRecouvrementAutomatiqueOuDirect() {
        return wantModeRecouvrementAutomatiqueOuDirect;
    }

    /**
     * Setter
     */
    public void setForIdEntete(String newForIdEntete) {
        forIdEntete = newForIdEntete;
    }

    public void setForIdExterneFacture(String newForIdExterneFacture) {
        forIdExterneFacture = newForIdExterneFacture;
    }

    public void setForIdExterneRole(String newForIdExterneRole) {
        forIdExterneRole = newForIdExterneRole;
    }

    public void setForIdPassage(String newForIdPassage) {
        forIdPassage = newForIdPassage;
    }

    public void setForIdRemarque(String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    public void setForIdRole(String newForIdRole) {
        forIdRole = newForIdRole;
    }

    public void setForIdSousType(String newForIdSousType) {
        forIdSousType = newForIdSousType;
    }

    public void setForIdTiers(String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForIdTypeFacture(String newForIdTypeFacture) {
        forIdTypeFacture = newForIdTypeFacture;
    }

    /**
     * @param string
     */
    public void setForIdTypeSection(String string) {
        forIdTypeSection = string;
    }

    /**
     * Insert the method's description here. Creation date: (16.06.2003 08:48:08)
     * 
     * @param newForTillTotalFacture
     *            String
     */
    public void setForTotalFacture(String newForTotalFacture) {
        forTotalFacture = newForTotalFacture;
    }

    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

    public void setUseManagerForLSV(boolean b) {
        _useManagerForLSV = b;
    }

    /**
     * Setter pour ne selectionner que les entetes de factures avec comme mode de recouvrement directe et automatique
     */
    public void setWantModeRecouvrementAutomatiqueOuDirect(boolean b) {
        wantModeRecouvrementAutomatiqueOuDirect = b;
    }

}
