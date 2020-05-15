package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class APGenererDroitPandemieMensuelViewBean extends PRAbstractViewBeanSupport {

    private String eMailAddress = "";
    private String dateFin  = "";
    private String dateDepart = "";
    private String dateArrivee = "";

    private String genreService;

    private String categorieEntreprise = "";

    private Boolean isDefinitif = null;



    /**
     * getter pour l'attribut EMail address
     *
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * setter pour l'attribut EMail address
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }


    /**
     */
    @Override
    public boolean validate() {
        // TODO
        return true;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getGenreService() {
        return genreService;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public String getCategorieEntreprise() {
        return categorieEntreprise;
    }

    public void setCategorieEntreprise(String categorieEntreprise) {
        this.categorieEntreprise = categorieEntreprise;
    }

    public Boolean getIsDefinitif() {
        return isDefinitif;
    }

    public void setIsDefinitif(Boolean isDefinitif) {
        this.isDefinitif = isDefinitif;
    }
}
