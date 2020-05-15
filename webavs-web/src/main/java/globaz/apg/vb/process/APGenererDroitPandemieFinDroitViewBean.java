package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 *
 */
public class APGenererDroitPandemieFinDroitViewBean extends PRAbstractViewBeanSupport  {

    private String eMailAddress = "";
    private String dateFin  = "";
    private String genreService;
    private String idDroit;


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

    public String getGenreService() {
        return genreService;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }
}
