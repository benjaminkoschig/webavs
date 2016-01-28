package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;

/**
 * Insérez la description du type ici. Date de création : (06.02.2002 14:08:00)
 * 
 * @author: Administrator
 */
public class CAEcritureManager extends CAOperationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCompteCourant = new String();
    private String forIdCompte = new String();
    private String forIdExterneRubrique = new String();
    private String forMontant = new String();
    private String forMontantABS = new String();
    // TODO Dal : A EFFACER des suppression ancien mode de calcul des intérêts
    // moratoires.
    private boolean groupByForCalculInteretMoratoire = false;
    private Boolean groupByRubrique = new Boolean(false);
    private String likeIdExterneRubrique = new String();

    private String likeIdExterneSection = new String();

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (isGroupByForCalculInteretMoratoire()) {
            return CAOperation.FIELD_IDSECTION + ", " + CAOperation.FIELD_IDJOURNAL;
        } else {
            return super._getFields(statement);
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {

        // Récupérer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement selon le numéro de journal
        if (getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        // traitement du positionnement selon le montant
        if (getForMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MONTANT=" + this._dbWriteNumeric(statement.getTransaction(), getForMontant());
        }

        // traitement du positionnement selon le montant absolu
        if (getForMontantABS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ABS(MONTANT)=" + "ABS(" + this._dbWriteNumeric(statement.getTransaction(), getForMontantABS())
                    + ")";
        }

        // S'il n'y a pas de sélection de type d'écriture, on force
        if ((getForIdTypeOperation().length() == 0) && (getLikeIdTypeOperation().length() == 0)
                && (getForIdTypeOperationLikeIn() == null)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION LIKE "
                    + this._dbWriteString(statement.getTransaction(), APIOperation.CAECRITURE + "%");
        }
        // traitement du positionnement selon l'idExterne de la rubrique par un
        // like
        if (getLikeIdExterneRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CARUBRP.IDEXTERNE LIKE "
                    + this._dbWriteString(statement.getTransaction(), getLikeIdExterneRubrique() + "%");
        }
        // traitement du positionnement selon l'idExterne de la rubrique par un
        // like
        if (getLikeIdExterneSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CASECTP.IDEXTERNE LIKE "
                    + this._dbWriteString(statement.getTransaction(), getLikeIdExterneSection() + "%");
        }
        // traitement du positionnement selon l'idExterne de la rubrique pour
        // autant qu'on accède à la vue
        if (getVueOperationCpteAnnexe().booleanValue() || getVueOperationCaCcSe().booleanValue()) {
            if (getForIdExterneRubrique().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CARUBRP.IDEXTERNE="
                        + this._dbWriteString(statement.getTransaction(), getForIdExterneRubrique());
            }
        }

        // Traitement par compte Courant
        if (getForCompteCourant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CACPTCP.IDEXTERNE="
                    + this._dbWriteString(statement.getTransaction(), getForCompteCourant());
        }

        return sqlWhere + getGroupBy();
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAEcriture();
    }

    /**
     * @return
     */
    public String getForCompteCourant() {
        return forCompteCourant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 14:08:15)
     * 
     * @return String
     */
    @Override
    public String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.02.2002 10:59:18)
     * 
     * @return String
     */
    public String getForIdExterneRubrique() {
        return forIdExterneRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 15:14:00)
     * 
     * @return String
     */
    public String getForMontant() {
        return globaz.globall.util.JANumberFormatter.deQuote(forMontant);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2002 11:39:28)
     * 
     * @return String
     */
    @Override
    public String getForMontantABS() {
        return forMontantABS;
    }

    private String getGroupBy() {
        if (isGroupByForCalculInteretMoratoire()) {
            return " group by " + CAOperation.FIELD_IDSECTION + ", " + CAOperation.FIELD_IDJOURNAL;
        }

        return "";
    }

    /**
     * @return the likeIdExterneRubrique
     */
    public String getLikeIdExterneRubrique() {
        return likeIdExterneRubrique;
    }

    /**
     * @return the likeIdExterneSection
     */
    public String getLikeIdExterneSection() {
        return likeIdExterneSection;
    }

    /**
     * @return
     */
    public boolean isGroupByForCalculInteretMoratoire() {
        return groupByForCalculInteretMoratoire;
    }

    /**
     * @return
     */
    public Boolean isGroupByRubrique() {
        return groupByRubrique;
    }

    /**
     * @param string
     */
    public void setForCompteCourant(String string) {
        forCompteCourant = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 14:08:15)
     * 
     * @param newForIdCompte
     *            String
     */
    @Override
    public void setForIdCompte(String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.02.2002 10:59:18)
     * 
     * @param newForIdExterneRubrique
     *            String
     */
    public void setForIdExterneRubrique(String newForIdExterneRubrique) {
        forIdExterneRubrique = newForIdExterneRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 15:14:00)
     * 
     * @param newForMontant
     *            String
     */
    public void setForMontant(String newForMontant) {
        forMontant = newForMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2002 11:39:28)
     * 
     * @param newForMontantABS
     *            String
     */
    @Override
    public void setForMontantABS(String newForMontantABS) {
        forMontantABS = newForMontantABS;
    }

    /**
     * @param b
     */
    public void setGroupByForCalculInteretMoratoire(boolean b) {
        groupByForCalculInteretMoratoire = b;
    }

    /**
     * @param boolean1
     */
    public void setGroupByRubrique(String boolean1) {
        try {
            groupByRubrique = Boolean.valueOf(boolean1);
        } catch (Exception ex) {
            groupByRubrique = new Boolean(false);
        }

    }

    /**
     * @param likeIdExterneRubrique
     *            the likeIdExterneRubrique to set
     */
    public void setLikeIdExterneRubrique(String likeIdExterneRubrique) {
        this.likeIdExterneRubrique = likeIdExterneRubrique;
    }

    /**
     * @param likeIdExterneSection
     *            the likeIdExterneSection to set
     */
    public void setLikeIdExterneSection(String likeIdExterneSection) {
        this.likeIdExterneSection = likeIdExterneSection;
    }

}
