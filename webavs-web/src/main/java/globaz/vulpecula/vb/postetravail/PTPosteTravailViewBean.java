/**
 * 
 */
package globaz.vulpecula.vb.postetravail;

import globaz.globall.db.BSessionUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurServiceCRUD;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.services.CotisationServiceImpl;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.web.gson.AdhesionCotisationPosteTravailGSON;
import ch.globaz.vulpecula.web.gson.OccupationGSON;
import ch.globaz.vulpecula.web.views.postetravail.PosteTravailAjaxService;
import ch.globaz.vulpecula.web.views.postetravail.PosteTravailViewService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author JPA
 */
public class PTPosteTravailViewBean extends DomainPersistentObjectViewBean<PosteTravail> {
    private PosteTravail posteTravail;
    private AffiliationCaisseMaladie affiliationCourante;

    private List<Administration> listCaisseMaladie = new ArrayList<Administration>();
    private static Gson gson = new Gson();
    private String caisseMaladie = "";

    // Variables temporaires utilisés pour stocker les valeurs de champs qui ne
    // peuvent être mises directement dans l'objet métier
    private String dateDebut = "";
    private String dateFin = "";
    private String affiliationCaisseMaladie = "";

    private boolean addWithEmployeur = false;
    private boolean addWithTravailleur = false;

    private boolean isNouveau = false;
    private String idTravailleur = "";

    private PosteTravailService posteTravailService = VulpeculaServiceLocator.getPosteTravailService();

    public PTPosteTravailViewBean() {
        super();
        posteTravail = new PosteTravail();
    }

    @Override
    public void add() throws ViewException {
        try {
            VulpeculaServiceLocator.getPosteTravailService().create(posteTravail);
        } catch (UnsatisfiedSpecificationException ex) {
            throw new ViewException(ex);
        }
    }

    @Override
    public void update() throws Exception {
        VulpeculaServiceLocator.getPosteTravailService().update(posteTravail);
    }

    @Override
    public void delete() throws Exception {
        VulpeculaServiceLocator.getPosteTravailService().delete(posteTravail);
    }

    @Override
    public void retrieve() throws Exception {
        posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(getEntityId());
        affiliationCourante = VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository()
                .findActifByIdPosteTravail(getId());
    }

    /**
     * Retourne la date de début d'activité au format dd.mm.YYYY
     * 
     * @return String représentant la date de début d'activité ou null si non
     *         présente
     */
    public String getDebutActivite() {
        return posteTravail.getDebutActiviteAsSwissValue();
    }

    /**
     * Retourne la date de début d'activité au format dd.mm.YYYY
     * 
     * @return String représentant la date début d'activité ou null si non
     *         présente
     */
    public String getFinActivite() {
        return posteTravail.getFinActiviteAsSwissValue();
    }

    public String getQualification() {
        if (posteTravail.getQualification() != null) {
            return posteTravail.getQualification().getValue();
        }
        return null;
    }

    public void setQualification(final String qualification) {
        if (Qualification.isValid(qualification)) {
            posteTravail.setQualification(Qualification.fromValue(qualification));
        }
    }

    public List<CodeSystem> getListQualification() {
        return posteTravailService.getQualificationForConvention(posteTravail.getIdConvention());
    }

    public String getConventionId() {
        if (posteTravail.getEmployeur() != null && posteTravail.getEmployeur().getConvention() != null) {
            return posteTravail.getEmployeur().getConvention().getId();
        }
        return null;
    }

    public String getConventionNo() {
        return posteTravail.getConventionNo();
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public String getConventionDesignation() {
        return posteTravail.getDesignationConvention();
    }

    public String getDescriptionTravailleur() {
        if (posteTravail.getTravailleur() != null) {
            return posteTravail.getDescriptionTravailleur();
        }
        return null;
    }

    public String getDescriptionEmployeur() {
        if (posteTravail.getEmployeur() != null) {
            return posteTravail.getEmployeur().getRaisonSociale();
        }
        return null;
    }

    public String getGenreSalaire() {
        if (posteTravail.getTypeSalaire() != null) {
            return posteTravail.getTypeSalaire().getValue();
        }
        return null;
    }

    public void setGenreSalaire(final String typeSalaire) {
        if (TypeSalaire.isValid(typeSalaire)) {
            posteTravail.setTypeSalaire(TypeSalaire.fromValue(typeSalaire));
        }
    }

    /**
	 * 
	 */
    public void setposteFranchiseAvs(final boolean franchiseAVS) {
        posteTravail.setFranchiseAVS(franchiseAVS);
    }

    public boolean isPosteFranchiseAVS(final boolean franchiseAVS) {
        return posteTravail.isFranchiseAVS();
    }

    /**
     * Appelé automatiquement par le framework Jade, cette méthode effectue une
     * recherche avec l'id passé en paramètre en base de données.
     * 
     * @param idTravailleur
     *            id du travailleur à rechercher
     */
    public void setIdTravailleur(final String idTravailleur) {
        if (idTravailleur != null) {
            this.idTravailleur = idTravailleur;
            setTravailleurWithId(idTravailleur);
        }
    }

    /**
     * Appelé automatiquement par Jade, cette méthode stocke la date de début
     * d'activité et tente de mettre à jour la période dans l'objet métier
     * 
     * @param date
     *            String représentant une date
     */
    public void setDebutActivite(final String date) {
        dateDebut = date;
        setPeriode();
    }

    /**
     * Appelé automatiquement par Jade, cette méthode stocke la date de fin
     * d'activité et tente de mettre à jour la période dans l'objet métier
     * 
     * @param date
     *            String représentant une date
     */
    public void setFinActivite(final String date) {
        dateFin = date;
        setPeriode();
    }

    /**
     * Appelé automatiquement par Jade Construit à partir d'une liste serialisée
     * en JSON les cotisations auxquels un poste de travail est rattaché en les
     * settant dans l'objet métier
     * 
     * @param cotisations
     */
    public void setCotisations(final String cotisations) {
        List<AdhesionCotisationPosteTravailGSON> adhesionCotisationPosteTravailGSONs = PTPosteTravailViewBean.gson
                .fromJson(cotisations, new TypeToken<List<AdhesionCotisationPosteTravailGSON>>() {
                }.getType());
        List<AdhesionCotisationPosteTravail> adhesionsCotisations = new ArrayList<AdhesionCotisationPosteTravail>();

        for (AdhesionCotisationPosteTravailGSON adhesionsCaisseGSON : adhesionCotisationPosteTravailGSONs) {
            adhesionsCotisations.add(adhesionsCaisseGSON.convertToDomain());
        }

        posteTravail.setAdhesionsCotisations(adhesionsCotisations);
    }

    /**
     * Tente de mettre à jour la période dans l'objet métier
     */
    public void setPeriode() {
        Date dateDebut = null;
        Date dateFin = null;

        if (this.dateDebut != null && this.dateDebut.length() > 0) {
            if (Date.isValid(this.dateDebut)) {
                dateDebut = new Date(this.dateDebut);
            }
        }

        if (this.dateFin != null && this.dateFin.length() > 0) {
            if (Date.isValid(this.dateFin)) {
                dateFin = new Date(this.dateFin);
            }
        }

        posteTravail.setPeriodeActivite(new Periode(dateDebut, dateFin));
    }

    /**
     * Appel au travailleur repository afin de rechercher le travailleur
     * associé à l'id passé en paramètre.
     * 
     * @param idTravailleur
     *            id du travailleur à rechercher
     */
    private void setTravailleurWithId(final String idTravailleur) {
        posteTravail.setTravailleur(VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur));
    }

    public void setIdAffiliation(final String idAffiliation) {
        if (posteTravail.getEmployeur() == null) {
            posteTravail.setEmployeur(new Employeur());
        }
        posteTravail.getEmployeur().setId(idAffiliation);
    }

    public void setIdConvention(final String idConvention) {
        posteTravail.getEmployeur().getConvention().setId(idConvention);
    }

    /**
     * Appelé automatiquement par Jade Construit à partir d'une liste serialisée
     * en JSON les occupations d'un poste de travail en le settant dans l'objet
     * métier.
     */
    public void setOccupations(final String occupations) {
        List<OccupationGSON> occupationsGSON = PTPosteTravailViewBean.gson.fromJson(occupations,
                new TypeToken<List<OccupationGSON>>() {
                }.getType());
        List<Occupation> occupationsDomain = new ArrayList<Occupation>();

        for (OccupationGSON occupationGSON : occupationsGSON) {
            occupationsDomain.add(occupationGSON.convertToDomain());
        }

        posteTravail.setOccupations(occupationsDomain);
    }

    public boolean isPosteFranchiseAvs() {
        return posteTravail.isFranchiseAVS();
    }

    public boolean isAddWithEmployeur() {
        return addWithEmployeur;
    }

    public void setAddWithEmployeur(final boolean addWithEmployeur) {
        this.addWithEmployeur = addWithEmployeur;
    }

    public boolean isAddWithTravailleur() {
        return addWithTravailleur;
    }

    public void setAddWithTravailleur(final boolean addWithTravailleur) {
        this.addWithTravailleur = addWithTravailleur;
    }

    public void setEmployeur(final Employeur employeur) {
        posteTravail.setEmployeur(employeur);
    }

    public void setTravailleur(final Travailleur travailleur) {
        posteTravail.setTravailleur(travailleur);
    }

    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(final PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    public String getPosteTravailAjaxService() {
        return PosteTravailAjaxService.class.getName();
    }

    @Override
    public PosteTravail getEntity() {
        return posteTravail;
    }

    @Override
    public String getId() {
        return posteTravail.getId();
    }

    public String getTravailleurServiceCRUD() {
        return TravailleurServiceCRUD.class.getName();
    }

    public String getMessageSuppression() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_MESSAGE_SUPPRESSION");
    }

    public String getPosteTravailViewService() {
        return PosteTravailViewService.class.getName();
    }

    public String getCotisationService() {
        return CotisationServiceImpl.class.getName();
    }

    public String getEmployeurService() {
        return EmployeurServiceCRUD.class.getName();
    }

    public boolean isNouveau() {
        return isNouveau;
    }

    public void setIsNouveau(boolean isNouveau) {
        this.isNouveau = isNouveau;
    }

    public List<Administration> getListCaisseMaladie() {
        return listCaisseMaladie;
    }

    public void setListCaisseMaladie(List<Administration> listCaisseMaladie) {
        this.listCaisseMaladie = listCaisseMaladie;
    }

    public String getCaisseMaladie() {
        return caisseMaladie;
    }

    public void setCaisseMaladie(String caisseMaladie) {
        this.caisseMaladie = caisseMaladie;
    }

    /**
     * Retourne l'affiliation courante à une caisse maladie
     * 
     * @return Affiliation à une caisse maladie
     */
    public AffiliationCaisseMaladie getAffiliationCourante() {
        return affiliationCourante;
    }

    public String getAffiliationCaisseMaladie() {
        return affiliationCaisseMaladie;
    }

    public void setAffiliationCaisseMaladie(String affiliationCaisseMaladie) {
        this.affiliationCaisseMaladie = affiliationCaisseMaladie;
    }

}
