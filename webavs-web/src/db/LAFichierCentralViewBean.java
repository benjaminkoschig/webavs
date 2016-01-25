package db;

import globaz.pyxis.db.alternate.TIPersonneAvsAdresseViewBean;

public class LAFichierCentralViewBean extends TIPersonneAvsAdresseViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutAffiliation = "";
    private String dateDebutCaisse = "";
    private String dateFinAffiliation = "";
    private String dateFinCaisse = "";
    private String idAffiliation = "";
    private String numAffilieFC = "";
    private String numCaisse = "";
    private String typeAffiliation = "";
    private String typeCaisse = "";

    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new LAFichierCentralViewBean();
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        typeAffiliation = statement.dbReadString("MATTAF");
        dateDebutCaisse = statement.dbReadDateAMJ("MYDDEB");
        dateFinCaisse = statement.dbReadDateAMJ("MYDFIN");
        typeCaisse = statement.dbReadString("MYTGEN");
        numCaisse = statement.dbReadString("HBCADM");
        idAffiliation = statement.dbReadString("MAIAFF");
        numAffilieFC = statement.dbReadString("MYTNAF");
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutCaisse() {
        return dateDebutCaisse;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinCaisse() {
        return dateFinCaisse;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getNumAffilieFC() {
        return numAffilieFC;
    }

    public String getNumCaisse() {
        return numCaisse;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public String getTypeAffiliationLibelle() {
        return getSession().getCodeLibelle(getTypeAffiliation());
    }

    public String getTypeCaisse() {
        return typeCaisse;
    }

    public String getTypeCaisseLibelle() {
        return getSession().getCodeLibelle(getTypeCaisse());
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutCaisse(String dateDebutCaisse) {
        this.dateDebutCaisse = dateDebutCaisse;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinCaisse(String dateFinCaisse) {
        this.dateFinCaisse = dateFinCaisse;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setNumAffilieFC(String numAffilieFC) {
        this.numAffilieFC = numAffilieFC;
    }

    public void setNumCaisse(String numCaisse) {
        this.numCaisse = numCaisse;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public void setTypeCaisse(String typeCaisse) {
        this.typeCaisse = typeCaisse;
    }

}
