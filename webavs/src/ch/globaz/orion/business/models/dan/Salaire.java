package ch.globaz.orion.business.models.dan;

/**
 * Correspond a un salaire d'un employe dans eBusiness
 * 
 * @author sco
 * @since 12 avr. 2011
 */
public class Salaire {

    private String categoriePersonnel = null;
    private String dateNaissance = null;
    private String idCanton = null;
    private String nom = null;
    private String nss = null;
    private String periodeDebut = null;
    private String periodeFin = null;
    private String sexe = null;

    @Override
    public Salaire clone() throws CloneNotSupportedException {
        Salaire salaire = new Salaire();

        salaire.setCategoriePersonnel(categoriePersonnel);
        salaire.setIdCanton(idCanton);
        salaire.setDateNaissance(dateNaissance);
        salaire.setNom(nom);
        salaire.setNss(nss);
        salaire.setSexe(sexe);

        return salaire;
    }

    public String getCategoriePersonnel() {
        return categoriePersonnel;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdCanton() {
        return idCanton;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public String getSexe() {
        return sexe;
    }

    public void setCategoriePersonnel(String categoriePersonnel) {
        this.categoriePersonnel = categoriePersonnel;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdCanton(String idCanton) {
        this.idCanton = idCanton;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
