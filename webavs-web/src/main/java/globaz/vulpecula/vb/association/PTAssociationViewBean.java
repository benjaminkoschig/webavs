package globaz.vulpecula.vb.association;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import ch.globaz.vulpecula.web.views.association.AssociationViewService;

public class PTAssociationViewBean extends BJadePersistentObjectViewBean {
    private Employeur employeur;
    private List<Administration> associationsProfessionnelles;

    private Map<AssociationGenre, List<AssociationCotisation>> associationsCotisations;
    private Map<AssociationGenre, Collection<CotisationAssociationProfessionnelle>> cotisationAssociationByAssociationGenre;

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        associationsCotisations = VulpeculaServiceLocator.getAssociationCotisationService().getCotisationByAssociation(
                employeur.getId());
        cotisationAssociationByAssociationGenre = VulpeculaServiceLocator
                .getCotisationAssociationProfessionnelleService().findAllCotisationsByAssociationGenre();
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getIdEmployeur() {
        return employeur.getId();
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public List<Administration> getAssociationsProfessionnelles() {
        return associationsProfessionnelles;
    }

    public void setAssociationsProfessionnelles(List<Administration> associationsProfessionnelles) {
        this.associationsProfessionnelles = associationsProfessionnelles;
    }

    public Map<AssociationGenre, List<AssociationCotisation>> getAssociationsCotisations() {
        return associationsCotisations;
    }

    public void setAssociationsCotisations(Map<AssociationGenre, List<AssociationCotisation>> associationsCotisations) {
        this.associationsCotisations = associationsCotisations;
    }

    public Map<AssociationGenre, Collection<CotisationAssociationProfessionnelle>> getCotisationAssociationByAssociationGenre() {
        return cotisationAssociationByAssociationGenre;
    }

    public String getCsMembre() {
        return GenreCotisationAssociationProfessionnelle.MEMBRE.getValue();
    }

    public String getCsNonTaxe() {
        return GenreCotisationAssociationProfessionnelle.NON_TAXE.getValue();
    }

    /**
     * Retourne le nombre de cotisations maximum pour une association non membre.
     * 
     * @return int représentant le nombre de cotisations maximum possible
     */
    public int getLimiteCotisationsNonMembre() {
        return AssociationCotisation.LIMITE_COTISATION_NON_MEMBRE;
    }

    /**
     * Retourne la réduction de facture par défaut.
     * 
     * @return double représentant la réduction de facture par défaut
     */
    public double getReductionFactureDefaut() {
        return AssociationCotisation.REDUCTION_FACTURE_DEFAUT;
    }

    /**
     * Retourne la masse salariale par défaut.
     * 
     * @return double représentant la masse salariale par défaut
     */
    public double getMasseSalarialeDefaut() {
        return AssociationCotisation.MASSE_SALARIALE_DEFAUT;
    }

    public List<CodeSystem> getCategoriesFactures() {
        return CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_CATEGORIE_COTISATION_AP);
    }

    /**
     * Retourne les categories de facturation utilisables.
     * Le type RABAIS SPECIAL ne peut pas être utilisé en tant que tel.
     * 
     * @return Liste de codes systèmes sélectionnable par l'utilisateur
     */
    public List<CodeSystem> getCategoriesFacturesUtilisables() {
        List<CodeSystem> codesUtilisables = new ArrayList<CodeSystem>();
        List<CodeSystem> codes = getCategoriesFactures();
        for (CodeSystem code : codes) {
            if (!CategorieFactureAssociationProfessionnelle.RABAIS_SPECIAL.getValue().equals(code.getId())) {
                codesUtilisables.add(code);
            }
        }
        return codesUtilisables;
    }

    /**
     * Retourne les categories de facturation utilisables.
     * Le type RABAIS SPECIAL ne peut pas être utilisé en tant que tel.
     * 
     * @return Liste de codes systèmes sélectionnable par l'utilisateur
     */
    public CodeSystem getCategorieFactureRabaisSpecial() {
        List<CodeSystem> codes = getCategoriesFactures();
        for (CodeSystem code : codes) {
            if (CategorieFactureAssociationProfessionnelle.RABAIS_SPECIAL.getValue().equals(code.getId())) {
                return code;
            }
        }
        return null;
    }

    public String getAssociationViewService() {
        return AssociationViewService.class.getName();
    }
}
