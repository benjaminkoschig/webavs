/*
 * Créé le 3 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.envoi;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.IJOCommonComplementJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonGroupeJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceDestinationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceProvenanceDefTable;
import globaz.journalisation.db.journalisation.access.IJOComplementJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOGroupeJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOJournalisationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceDestinationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceProvenanceDefTable;
import globaz.leo.constantes.ILEConstantes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.common.base.Joiner;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEtapesSuivantesListViewBean extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String datePriseEnCompte;
    // private ArrayList orderByProvenance;
    private String forCategorie;
    private List forCsFormule;
    private String forIdSuivant;
    private String forStatut;
    private String orderBy1;
    private String orderBy2;
    private boolean wantOrderBy = true;
    private Collection<String> forNumerosAffilie = new ArrayList<String>();

    public Collection<String> getForNumerosAffilie() {
        return forNumerosAffilie;
    }

    public void setForNumerosAffilie(Collection<String> forNumerosAffilie) {
        this.forNumerosAffilie = forNumerosAffilie;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        // paramétre la sélection au niveau de l'utilisateur
        StringBuffer sqlFrom = new StringBuffer("");
        // ajout du groupe journal
        sqlFrom.append(_getCollection() + IJOJournalisationDefTable.TABLE_NAME + " JOURNALISATION " + "INNER JOIN "
                + _getCollection() + IJOGroupeJournalDefTable.TABLE_NAME + " GROUPEJOURNAL " + "ON GROUPEJOURNAL."
                + IJOCommonGroupeJournalDefTable.IDGROUPEJOURNAL + " = JOURNALISATION."
                + IJOCommonGroupeJournalDefTable.IDGROUPEJOURNAL);
        // ajout complement pour etape suivante
        sqlFrom.append(" INNER JOIN ( SELECT * FROM " + _getCollection() + IJOComplementJournalDefTable.TABLE_NAME
                + "  WHERE " + IJOCommonComplementJournalDefTable.CSTYPECODESYSTEME + " = "
                + ILEConstantes.CS_ETAPE_SUIVANTE_GROUPE);
        if (getForCsFormule() != null && getForCsFormule().size() > 0
                && getForCsFormule().get(0).toString().length() > 0) {
            sqlFrom.append("  AND " + IJOCommonComplementJournalDefTable.VALEURCODESYSTEME + " = "
                    + getForCsFormule().get(0).toString());
        }
        sqlFrom.append(") AS Cpl " + "ON Cpl." + IJOCommonComplementJournalDefTable.IDJOURNALISATION
                + " = JOURNALISATION." + IJOCommonJournalisationDefTable.IDJOURNALISATION);
        // order by provenance

        if (!JAUtil.isStringEmpty(getOrderBy1())) {
            if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(getOrderBy1())) {
                sqlFrom.append(" INNER JOIN ( SELECT * FROM " + _getCollection()
                        + IJOReferenceDestinationDefTable.TABLE_NAME + " WHERE  "
                        + IJOCommonReferenceDestinationDefTable.TYPEREFERENCEDESTINATION + " = '"
                        + JOConstantes.CS_JO_LIEN_TIERS_DESTINATAIRE + "') AS T0  ON T0."
                        + IJOCommonReferenceDestinationDefTable.IDJOURNALISATION + " = JOURNALISATION."
                        + IJOCommonJournalisationDefTable.IDJOURNALISATION);
            } else {
                sqlFrom.append(" INNER JOIN ( SELECT * FROM " + _getCollection()
                        + IJOReferenceProvenanceDefTable.TABLE_NAME + " WHERE  "
                        + IJOCommonReferenceProvenanceDefTable.TYPEREFERENCEPROVENANCE + " = '" + getOrderBy1()
                        + "') AS T0  ON T0." + IJOCommonReferenceProvenanceDefTable.IDJOURNALISATION
                        + " = JOURNALISATION." + IJOCommonJournalisationDefTable.IDJOURNALISATION);
            }
        }
        if (!JAUtil.isStringEmpty(getOrderBy2())) {
            if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(getOrderBy2())) {
                sqlFrom.append(" INNER JOIN ( SELECT * FROM " + _getCollection()
                        + IJOReferenceDestinationDefTable.TABLE_NAME + " WHERE  "
                        + IJOCommonReferenceDestinationDefTable.TYPEREFERENCEDESTINATION + " = '"
                        + JOConstantes.CS_JO_LIEN_TIERS_DESTINATAIRE + "') AS T1  ON T1."
                        + IJOCommonReferenceDestinationDefTable.IDJOURNALISATION + " = JOURNALISATION."
                        + IJOCommonJournalisationDefTable.IDJOURNALISATION);
            } else {
                sqlFrom.append(" INNER JOIN ( SELECT * FROM " + _getCollection()
                        + IJOReferenceProvenanceDefTable.TABLE_NAME + " WHERE  "
                        + IJOCommonReferenceProvenanceDefTable.TYPEREFERENCEPROVENANCE + " = '" + getOrderBy2()
                        + "') AS T1  ON T1." + IJOCommonReferenceProvenanceDefTable.IDJOURNALISATION
                        + " = JOURNALISATION." + IJOCommonJournalisationDefTable.IDJOURNALISATION);
            }
        }

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer("");
        if (!JAUtil.isStringEmpty(getOrderBy1())) {
            if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(getOrderBy1())) {
                sqlOrder.append("T0." + IJOCommonReferenceDestinationDefTable.IDCLEREFERENCEDESTINATION);
            } else {
                sqlOrder.append("T0." + IJOCommonReferenceProvenanceDefTable.IDCLEREFERENCEPROVENANCE);
            }
        }

        if (!JAUtil.isStringEmpty(getOrderBy2())) {
            if (!JAUtil.isStringEmpty(sqlOrder.toString())) {
                sqlOrder.append(" , ");
            }
            if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(getOrderBy2())) {
                sqlOrder.append("T1." + IJOCommonReferenceDestinationDefTable.IDCLEREFERENCEDESTINATION);
            } else {
                sqlOrder.append("T1." + IJOCommonReferenceProvenanceDefTable.IDCLEREFERENCEPROVENANCE);
            }
        }
        if (!JAUtil.isStringEmpty(sqlOrder.toString())) {
            return sqlOrder.toString();
        } else {
            return super._getOrder(statement);
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        // recherche d'après les catégories
        if (getForCategories() != null && getForCategories().length() > 0) {
            if (!JAUtil.isStringEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" JOURNALISATION." + IJOCommonJournalisationDefTable.IDJOURNALISATION + " IN (SELECT "
                    + IJOCommonJournalisationDefTable.IDJOURNALISATION + " FROM " + _getCollection()
                    + IJOComplementJournalDefTable.TABLE_NAME + " WHERE "
                    + IJOCommonComplementJournalDefTable.CSTYPECODESYSTEME + "=" + ILEConstantes.CS_CATEGORIE_GROUPE
                    + " AND ( ");
            sqlWhere.append(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME + " = " + getForCategories());
            sqlWhere.append(" ) )");
        }

        if (!forNumerosAffilie.isEmpty()) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonJournalisationDefTable.LIBELLE + " in (" + "\'"
                    + Joiner.on("\',\'").skipNulls().join(forNumerosAffilie) + "\' ) ");
        }

        // recherche d'après les formules

        /*
         * if (getForCsFormule()!=null&&getForCsFormule().get(0).toString().length () > 0) { if
         * (!JAUtil.isStringEmpty(sqlWhere.toString())) { sqlWhere.append(" AND "); } //
         * if(!(getForCsFormule().get(1).toString().length() > 0)){ // ArrayList listDefault = new ArrayList(); //
         * listDefault.add(ILEConstantes.CS_CATEGORIE_SUIVI_LAA); //
         * listDefault.add(ILEConstantes.CS_CATEGORIE_SUIVI_LPP); // setForCsFormule(listDefault); // }
         * sqlWhere.append(" JOURNALISATION."+ IJOJournalisationDefTable.IDJOURNALISATION
         * +" IN (SELECT "+IJOJournalisationDefTable.IDJOURNALISATION+" FROM " +
         * _getCollection()+IJOComplementJournalDefTable.TABLE_NAME+" WHERE "+ IJOComplementJournalDefTable
         * .CSTYPECODESYSTEME+"="+ILEConstantes.CS_DEF_FORMULE_GROUPE +" AND ( "); //for (int i = 0; i <
         * getForCategories().size(); i++) { sqlWhere.append(IJOComplementJournalDefTable.VALEURCODESYSTEME+" = " +
         * getForCsFormule().get(0).toString()); // if (i != getForCsFormule().size() - 2) { // sqlWhere.append(" OR ");
         * // } // } sqlWhere.append(" ) )"); }
         */
        // idSuivant=0
        if (!JAUtil.isStringEmpty(sqlWhere.toString())) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(IJOCommonJournalisationDefTable.IDSUIVANT + "= 0");
        // traitement du until date
        if (!JAUtil.isStringEmpty(getDatePriseEnCompte())) {
            if (!JAUtil.isStringEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(" + IJOCommonGroupeJournalDefTable.DATE_RAPPEL).append("<=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getDatePriseEnCompte()) + "  )");
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEEtapesSuivantesViewBean();
    }

    /**
     * @return
     */
    public String getDatePriseEnCompte() {
        // datePriseEnCompte="10.01.2005";
        return datePriseEnCompte;
    }

    /**
     * @return
     */
    // public ArrayList getOrderByProvenance() {
    /*
     * orderByProvenance = new ArrayList(5); orderByProvenance.add(0,"6600001"); orderByProvenance.add(1,"6600002");
     * orderByProvenance.add(2,"6600003"); orderByProvenance.add(3,"6600004");
     */
    // return orderByProvenance;
    // }

    /**
     * @return
     */
    public String getForCategories() {
        return forCategorie;
    }

    /**
     * @return
     */
    public List getForCsFormule() {
        return forCsFormule;
    }

    /**
     * @param list
     */
    // public void setOrderByProvenance(ArrayList list) {
    // orderByProvenance = list;
    // }
    /**
     * @return
     */
    public String getForIdSuivant() {
        return forIdSuivant;
    }

    /**
     * @return
     */
    public String getForStatut() {
        return forStatut;
    }

    /**
     * @return
     */
    public String getOrderBy1() {
        return orderBy1;
    }

    /**
     * @return
     */

    /**
     * @return
     */
    public String getOrderBy2() {
        return orderBy2;
    }

    /**
     * @return
     */
    public boolean isWantOrderBy() {
        return wantOrderBy;
    }

    /**
     * @param string
     */
    public void setDatePriseEnCompte(String string) {
        datePriseEnCompte = string;
    }

    /**
     * @param list
     */
    public void setForCategories(String cat) {
        forCategorie = cat;
    }

    /**
     * @param list
     */
    public void setForCsFormule(List list) {
        forCsFormule = list;
    }

    /**
     * @param string
     */
    public void setForIdSuivant(String string) {
        forIdSuivant = string;
    }

    /**
     * @param string
     */
    public void setForStatut(String string) {
        forStatut = string;
    }

    /**
     * @param string
     */
    public void setOrderBy1(String string) {
        orderBy1 = string;
    }

    /**
     * @param string
     */
    public void setOrderBy2(String string) {
        orderBy2 = string;
    }

    /**
     * @param b
     */
    public void setWantOrderBy(boolean b) {
        wantOrderBy = b;
    }

    public String getForCategorie() {
        return forCategorie;
    }

    public void setForCategorie(String forCategorie) {
        this.forCategorie = forCategorie;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
