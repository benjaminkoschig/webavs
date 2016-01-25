/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.lienAffiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;

/**
 * La classe définissant l'entité LienAffiliation avec jointure sur les 2 affiliations concernées (voir
 * AFLienAffiliationFullManager) A n'utiliser que pour des recherche (pas d'ajout possible avec cette classe, il faut
 * utiliser AFLienAffiliation pour cela.)
 * 
 * @author oca
 */
public class AFLienAffiliationJoin extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateFin = "";
    private String idAffiliationEnfant = "";
    private String idAffiliationParent = "";
    private String idLien = "";

    private String numeroAffilieEnfant = "";
    private String numeroAffilieParent = "";
    private String typeLien = "";

    @Override
    protected String _getTableName() {
        return "AFLIENP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLien = statement.dbReadNumeric("lMWILIE");
        idAffiliationParent = statement.dbReadNumeric("pMAIAFF");
        numeroAffilieParent = statement.dbReadString("pMALNAF");
        idAffiliationEnfant = statement.dbReadNumeric("eMAIAFF");
        numeroAffilieEnfant = statement.dbReadString("eMALNAF");
        typeLien = statement.dbReadNumeric("lMWTLIE");
        dateDebut = statement.dbReadDateAMJ("lMWDDEB");
        dateFin = statement.dbReadDateAMJ("lMWDFIN");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MWILIE", this._dbWriteNumeric(statement.getTransaction(), idLien, ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /*
     * Getter et Setter
     */

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdAffiliationEnfant() {
        return idAffiliationEnfant;
    }

    public String getIdAffiliationParent() {
        return idAffiliationParent;
    }

    public String getIdLien() {
        return idLien;
    }

    public String getNumeroAffilieEnfant() {
        return numeroAffilieEnfant;
    }

    public String getNumeroAffilieParent() {
        return numeroAffilieParent;
    }

    public String getTypeLien() {
        return typeLien;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdAffiliationEnfant(String idAffiliationEnfant) {
        this.idAffiliationEnfant = idAffiliationEnfant;
    }

    public void setIdAffiliationParent(String idAffiliationParent) {
        this.idAffiliationParent = idAffiliationParent;
    }

    public void setIdLien(String idLien) {
        this.idLien = idLien;
    }

    public void setNumeroAffilieEnfant(String numeroAffilieEnfant) {
        this.numeroAffilieEnfant = numeroAffilieEnfant;
    }

    public void setNumeroAffilieParent(String numeroAffilieParent) {
        this.numeroAffilieParent = numeroAffilieParent;
    }

    public void setTypeLien(String typeLien) {
        this.typeLien = typeLien;
    }

}
