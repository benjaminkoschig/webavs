package ch.globaz.orion.business.models.dan;

/**
 * Correspond a une déclaration de salaire dans eBusiness
 * 
 * @author sco
 * @since 12 avr. 2011
 */
public class Dan {

    private String annee = null;
    private String canton = null;
    private String idAffilie = null;
    private String idInstitutionLAA = null;
    private String idInstitutionLPP = null;
    private String numeroAffilie = null;
    private Salaire salaires[] = null;

    public void addSalaire(int i, Salaire salaire) {
        if (salaires != null) {
            salaires[i] = salaire;
        }
    }

    public void createTabSalaires(int lengh) {
        salaires = new Salaire[lengh];
    }

    public String getAnnee() {
        return annee;
    }

    public String getCanton() {
        return canton;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdInstitutionLAA() {
        return idInstitutionLAA;
    }

    public String getIdInstitutionLPP() {
        return idInstitutionLPP;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public Salaire getSalaire(int i) {
        return salaires[i];
    }

    public Salaire[] getSalaires() {
        return salaires;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdInstitutionLAA(String idInstitutionLAA) {
        this.idInstitutionLAA = idInstitutionLAA;
    }

    public void setIdInstitutionLPP(String idInstitutionLPP) {
        this.idInstitutionLPP = idInstitutionLPP;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setSalaires(Salaire[] salaires) {
        this.salaires = salaires;
    }
}
