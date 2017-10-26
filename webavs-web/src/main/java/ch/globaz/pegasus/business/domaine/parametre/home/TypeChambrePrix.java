package ch.globaz.pegasus.business.domaine.parametre.home;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.parametre.Parametre;

public class TypeChambrePrix implements Parametre<String> {
    private String id;
    private String idHome;
    private ServiceEtat serviceEtat;
    private HomeCategorie categorie;
    private HomeCategoriArgentPoche categorieArgentPoche;
    private boolean isApiFacturee;
    private Montant prix;
    private Date dateDebut;
    private Date dateFin;

    @Override
    public Date getDateDebut() {
        return dateDebut;
    }

    @Override
    public Date getDateFin() {
        return dateFin;
    }

    @Override
    public String getType() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdHome() {
        return idHome;
    }

    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    public boolean isApiFacturee() {
        return isApiFacturee;
    }

    public void setApiFacturee(boolean isApiFacturee) {
        this.isApiFacturee = isApiFacturee;
    }

    public Montant getPrix() {
        return prix;
    }

    public void setPrix(Montant prix) {
        this.prix = prix;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public ServiceEtat getServiceEtat() {
        return serviceEtat;
    }

    public void setServiceEtat(ServiceEtat serviceEtat) {
        this.serviceEtat = serviceEtat;
    }

    public HomeCategoriArgentPoche getCategorieArgentPoche() {
        return categorieArgentPoche;
    }

    public void setCategorieArgentPoche(HomeCategoriArgentPoche categorieArgentPoche) {
        this.categorieArgentPoche = categorieArgentPoche;
    }

    public HomeCategorie getCategorie() {
        return categorie;
    }

    public void setCategorie(HomeCategorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "TypeChambrePrix [id=" + id + ", idHome=" + idHome + ", serviceEtat=" + serviceEtat + ", categorie="
                + categorie + ", categorieArgentPoche=" + categorieArgentPoche + ", isApiFacturee=" + isApiFacturee
                + ", prix=" + prix + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + "]";
    }

}
