/**
 * 
 */
package globaz.vulpecula.vb.postetravailvueglobale;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.musca.db.facturation.FAModuleFacturation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.comparators.PosteTravailActifsInactifsComparator;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.services.musca.PassageSearchException;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import ch.globaz.vulpecula.web.views.postetravail.TravailleurViewService;

/**
 * @author JPA
 * 
 */
public class PTTravailleurvueglobaleViewBean extends BJadeSearchObjectELViewBean {
    private Travailleur travailleur = new Travailleur();

    private String passageAJErreur = null;
    private String passageCPErreur = null;
    private String passageSMErreur = null;

    private List<CodeSystem> qualifications;
    private List<CodeSystem> genresSalaires;
    private List<Convention> conventions;
    private List<CodeSystem> typesDecompte;

    /** Définit l'id du tab actuellement actif */
    private String tab;

    public PTTravailleurvueglobaleViewBean() {
        super();
        travailleur = new Travailleur();
    }

    public List<String> getAdresseTiersDetail() {
        return travailleur.getAdressePrincipale().getAdresseAsStringLines();
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public List<CodeSystem> getQualifications() {
        return qualifications;
    }

    public List<CodeSystem> getGenresSalaires() {
        return genresSalaires;
    }

    public List<CodeSystem> getTypesDecompte() {
        return typesDecompte;
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    @Override
    public void retrieve() throws Exception {
        travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(getId());
        List<PosteTravail> postesDeTravail = new ArrayList<PosteTravail>();
        postesDeTravail.addAll(VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdTravailleur(getId()));
        for (PosteTravail poste : postesDeTravail) {
            poste.setOccupations(VulpeculaRepositoryLocator.getOccupationRepository().findOccupationsByIdPosteTravail(
                    poste.getId()));
            poste.setAdhesionsCotisations(VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository()
                    .findByIdPosteTravail(poste.getId()));

        }
        travailleur.setPostesTravail(postesDeTravail);

        Collections.sort(travailleur.getPostesTravail(), new PosteTravailActifsInactifsComparator());

        // On tente de charger le passage de facturation auquel sera affecté les absences justifiées. Si celui-ci ne
        // peut être chargé, on affecte un message d'erreur.
        try {
            VulpeculaServiceLocator.getPassageService().findPassageActif(
                    FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES);
        } catch (PassageSearchException ex) {
            passageAJErreur = ex.getMessage();
        }

        // On tente de charger le passage de facturation auquel sera affecté les congés congés. Si celui-ci ne peut
        // être chargé, on affecte un message d'erreur.
        try {
            VulpeculaServiceLocator.getPassageService().findPassageActif(FAModuleFacturation.CS_MODULE_CONGE_PAYE);
        } catch (PassageSearchException ex) {
            passageCPErreur = ex.getMessage();
        }

        // On tente de charger le passage de facturation auquel sera affecté les services militaires. Si celui-ci ne
        // peut
        // être chargé, on affecte un message d'erreur.
        try {
            VulpeculaServiceLocator.getPassageService().findPassageActif(
                    FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE);
        } catch (PassageSearchException ex) {
            passageSMErreur = ex.getMessage();
        }

        qualifications = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_QUALIFICATION);
        genresSalaires = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_GENRE_SALAIRE);
        typesDecompte = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_TYPE_DECOMPTES);
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    @Override
    public String getId() {
        return travailleur.getId();
    }

    @Override
    public void setId(final String newId) {
        travailleur.setId(newId);

    }

    @Override
    public BSpy getSpy() {
        return new BSpy(travailleur.getSpy());
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    private TypeAssurance[] getTypesAssurancesArray(PosteTravail posteTravail) {
        List<TypeAssurance> typeAssurance = new ArrayList<TypeAssurance>();
        for (AdhesionCotisationPosteTravail cotisation : posteTravail.getAdhesionsCotisations()) {
            typeAssurance.add(cotisation.getTypeAssurance());
        }

        return typeAssurance.toArray(new TypeAssurance[typeAssurance.size()]);
    }

    /**
     * Contrôle si un des postes de travail liés au travailleur à les droits pour les AJ
     * 
     * @return true si le travailleur a droit aux AJ sur l'un de ses postes
     */
    public boolean isHasDroitAJ() {
        for (PosteTravail posteTravail : travailleur.getPostesTravail()) {
            int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(
                    posteTravail.getId());
            if (TableParametrage.getInstance().hasDroitAJ(idCaisseMetier, getTypesAssurancesArray(posteTravail))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contrôle si un des postes de travail liés au travailleur à les droits pour les CP
     * 
     * @return true si le travailleur a droit aux CP sur l'un de ses postes
     */
    public boolean isHasDroitCP() {
        for (PosteTravail posteTravail : travailleur.getPostesTravail()) {
            int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(
                    posteTravail.getId());
            if (TableParametrage.getInstance().hasDroitCP(idCaisseMetier, getTypesAssurancesArray(posteTravail))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contrôle si un des postes de travail liés au travailleur à les droits pour les SM
     * 
     * @return true si le travailleur a droit aux SM sur l'un de ses postes
     */
    public boolean isHasDroitSM() {
        for (PosteTravail posteTravail : travailleur.getPostesTravail()) {
            int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(
                    posteTravail.getId());
            if (TableParametrage.getInstance().hasDroitSM(idCaisseMetier, getTypesAssurancesArray(posteTravail))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne le message d'erreur lors de la tentative du chargement du passage de facturation des absences
     * justifiées.
     * Celui-ci est null lorsqu'il existe un et un seul passage de facturation de ce type.
     * 
     * @return String représentant le message d'erreur ou null si tout s'est bien passé
     */
    public String getPassageAJErreur() {
        return passageAJErreur;
    }

    /**
     * Retourne le message d'erreur lors de la tentative de chargement du passage de facturation des CP.
     * Le message d'erreur contiendra un avertissement si il n'y a pas de passage de facturation pour les CP ou si
     * plusieurs sont ouverts en même temps.
     * 
     * @return String représentant le message d'erreur ou null si tout s'est bien passé
     */
    public String getPassageCPErreur() {
        return passageCPErreur;
    }

    /**
     * Retourne le message d'erreur lors de la tentative de chargement du passage de facturation des SM.
     * Le message d'erreur contiendra un avertissement si il n'y a pas de passage de facturation pour les SM ou si
     * plusieurs sont ouverts en même temps.
     * 
     * @return String représentant le message d'erreur ou null si tout s'est bien passé
     */
    public String getPassageSMErreur() {
        return passageSMErreur;
    }

    public String getTravailleurViewService() {
        return TravailleurViewService.class.getName();
    }

    public String getMessageEnfantsAnnonces() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ENFANTS_ANNONCES");
    }
}
