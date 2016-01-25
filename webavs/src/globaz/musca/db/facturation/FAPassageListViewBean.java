package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import java.util.HashMap;
import java.util.Map;

public class FAPassageListViewBean extends FAPassageManager implements globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private Map<String, String> nbDecomptesParPassage = new HashMap<String, String>();
    private java.lang.String reqLibelle = "";
    private java.lang.String reqTri = "";
    private String typeSummary = "";

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        String _fields = "FAPASSP.DATEFACTURATION, ";
        if (getForIdPassage().length() != 0) {
            _fields += this._dbWriteNumeric(statement.getTransaction(), getForIdPassage()) + " AS IDPASSAGE";
        } else {
            _fields += "FAPASSP.IDPASSAGE";
        }
        _fields += ", FAPASSP.LIBELLEPASSAGE, FAPASSP.IDTYPEFACTURATION, FAPASSP.STATUS, FAPASSP.ESTVERROUILLE, FAPASSP.AUTO, FAPASSP.DELAI, FAPASSP.PROP ";
        return _fields;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String _from = _getCollection() + "FAPASSP AS FAPASSP ";
        return _from;

    }

    public java.lang.String getAction() {
        return action;
    }

    public String getDateFacturation(int pos) {
        return ((FAPassage) getEntity(pos)).getDateFacturation();

    }

    public String getIdPassage(int pos) {
        return ((FAPassage) getEntity(pos)).getIdPassage();

    }

    public boolean getIsAuto(int pos) {
        return ((FAPassage) getEntity(pos)).getIsAuto().booleanValue();
    }

    public String getLibelle(int pos) {
        return ((FAPassage) getEntity(pos)).getLibelle();

    }

    public String getLibelleEtat(int pos) {
        try {
            return globaz.musca.translation.CodeSystem.getLibelle(getSession(),
                    ((FAPassage) getEntity(pos)).getStatus());
        } catch (Exception e) {
            return "";
        }
    }

    public String getLibelleType(int pos) {
        try {
            return globaz.musca.translation.CodeSystem.getLibelle(getSession(),
                    ((FAPassage) getEntity(pos)).getIdTypeFacturation());
        } catch (Exception e) {
            return "";
        }

    }

    public Map<String, String> getNbDecomptesParPassage() {
        return nbDecomptesParPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.06.2002 14:03:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqLibelle() {
        return reqLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.06.2002 14:01:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqTri() {
        return reqTri;
    }

    public String getStatus(int pos) {
        return ((FAPassage) getEntity(pos)).getStatus();

    }

    public String getTypeSummary() {
        return typeSummary;
    }

    public Boolean isEstVerrouille(int pos) {
        return ((FAPassage) getEntity(pos)).isEstVerrouille();

    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    public void setNbDecomptesParPassage(Map<String, String> nbDecomptesParPassage) {
        this.nbDecomptesParPassage = nbDecomptesParPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.06.2002 14:03:49)
     * 
     * @param newReqLibelle
     *            java.lang.String
     */
    public void setReqLibelle(java.lang.String newReqLibelle) {
        reqLibelle = newReqLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.06.2002 14:01:23)
     * 
     * @param newReqCritere
     *            java.lang.String
     */
    public void setReqTri(java.lang.String newReqTri) {
        reqTri = newReqTri;
    }

    public void setTypeSummary(String typeSummary) {
        this.typeSummary = typeSummary;
    }
}
