package globaz.vulpecula.vb.postetravail;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import java.util.Map;
import ch.globaz.vulpecula.web.views.postetravail.AdhesionCotisationViewContainer;
import ch.globaz.vulpecula.web.views.postetravail.PosteTravailViewService;

public class PTCotisationsAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = -4929106786491751552L;

    private Map<String, AdhesionCotisationViewContainer> adhesionsActivesEtPossibles;

    private String idPosteTravail;
    private String idEmployeur;
    private String dateDebut;
    private String dateFin;
    private String dateNaissanceTravailleur;
    private String sexeTravailleur;

    @Override
    public void retrieve() throws Exception {
        adhesionsActivesEtPossibles = PosteTravailViewService
                .getAdhesionsCotisationsActivesEtPossiblesGroupByPlanCaisse(idPosteTravail, idEmployeur, dateDebut,
                        dateFin, dateNaissanceTravailleur, sexeTravailleur);
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @return the dateAnniversaireTravailleur
     */
    public String getDateNaissanceTravailleur() {
        return dateNaissanceTravailleur;
    }

    /**
     * @param dateAnniversaireTravailleur the dateAnniversaireTravailleur to set
     */
    public void setDateNaissanceTravailleur(String dateAnniversaireTravailleur) {
        dateNaissanceTravailleur = dateAnniversaireTravailleur;
    }

    /**
     * Retourne la liste des adhésions aux cotisations actives et possibles
     * regroupées par plan de caisse.
     * 
     * @return
     */
    public Map<String, AdhesionCotisationViewContainer> getAdhesionsCotisationsActivesEtPossiblesGroupByPlanCaisse() {
        return adhesionsActivesEtPossibles;
    }

    public String getSexeTravailleur() {
        return sexeTravailleur;
    }

    public void setSexeTravailleur(String sexeTravailleur) {
        this.sexeTravailleur = sexeTravailleur;
    }

}
