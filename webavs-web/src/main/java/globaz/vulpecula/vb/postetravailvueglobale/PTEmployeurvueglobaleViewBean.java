/**
 * 
 */
package globaz.vulpecula.vb.postetravailvueglobale;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.naos.api.IAFAdhesion;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.association.ModeleEntete;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import ch.globaz.vulpecula.web.views.association.FacturationAPViewService;
import ch.globaz.vulpecula.web.views.postetravail.EmployeurViewService;

/**
 * @author JPA
 * 
 */
public class PTEmployeurvueglobaleViewBean extends BJadeSearchObjectELViewBean {
    private Employeur employeur;
    private Adhesion caisseMetier;

    private String tab;
    private List<CodeSystem> genresSalaires;
    private List<CodeSystem> qualifications;
    private List<CodeSystem> typesFacturation;
    private List<CodeSystem> typesDecomptes;

    public PTEmployeurvueglobaleViewBean() {
        super();
        employeur = new Employeur();
    }

    @Override
    public void retrieve() throws Exception {
        String id = getId();

        employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(id);
        employeur.setAdhesionsCaisses(VulpeculaRepositoryLocator.getAdhesionRepository()
                .findAdhesionsActivesByIdAffilie(id));
        employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdressePrioriteCourrierByIdTiers(employeur.getIdTiers()));
        employeur.setCotisations(VulpeculaRepositoryLocator.getCotisationsRepository().findByIdAffilie(id));

        caisseMetier = VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(employeur.getId());

        genresSalaires = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_GENRE_SALAIRE);
        qualifications = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_QUALIFICATION);
        typesFacturation = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_TYPE_FACTURATION);
        typesDecomptes = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_TYPE_DECOMPTES);
    }

    public boolean getEmployeurIsEbu() {
        return employeur.isEBusiness();
    }

    public List<CodeSystem> getGenresSalaires() {
        return genresSalaires;
    }

    public List<CodeSystem> getQualifications() {
        return qualifications;
    }

    public List<CodeSystem> getTypesFacturation() {
        return typesFacturation;
    }

    public List<CodeSystem> getTypesDecomptes() {
        return typesDecomptes;
    }

    /***
     * Retourne l'objet métier {@link Employeur} servant à l'affichage
     * 
     * @return Employeur utilisé pour l'affichage
     */
    public Employeur getEmployeur() {
        return employeur;
    }

    /**
     * @return la liste d'adhésion sans la caisse principale
     */
    public List<Adhesion> getEmployeurAdhesionsCaissesSansCaissePrincipale() {
        List<Adhesion> adhesionsCaisses = new ArrayList<Adhesion>();
        for (Adhesion ad : employeur.getAdhesionsCaisses()) {
            if (!IAFAdhesion.ADHESION_CAISSE_PRINCIPALE.equals(ad.getTypeAdhesion())) {
                adhesionsCaisses.add(ad);
            }
        }
        return adhesionsCaisses;
    }

    /**
     * @return la liste des cotisations actives
     */
    public List<Cotisation> getEmployeurCotisationsActives() {
        return employeur.getCotisations();
    }

    /**
     * Retourne l'adresse principale du tier sous forme de List
     * 
     * @return List<String> représentant l'adresse du tiers
     */
    public List<String> getAdresseTiersDetail() {
        return employeur.getAdressePrincipale().getAdresseAsStringLines();
    }

    /**
     * Retourne la caisse métier de l'employeur.
     * 
     * @return Adhesion
     */
    public Adhesion getCaisseMetier() {
        return caisseMetier;
    }

    @Override
    public String getId() {
        return employeur.getId();
    }

    @Override
    public void setId(final String newId) {
        employeur.setId(newId);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(employeur.getSpy());
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getLabelOui() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_OUI");
    }

    public String getLabelNon() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_NON");
    }

    public String getLabelDeleteFA() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DELETE_FACTURATION_ASSOCIATION");
    }

    public String getLabelRefuseFA() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_REFUSE_FACTURATION_ASSOCIATION");
    }

    public String getEmployeurViewService() {
        return EmployeurViewService.class.getName();
    }

    public String getFacturationAPViewService() {
        return FacturationAPViewService.class.getName();
    }

    public List<ModeleEntete> getModeles() {
        return VulpeculaServiceLocator.getParametrageAPService().getModeles();
    }

    public boolean hasParticularite() {
        return VulpeculaServiceLocator.getEmployeurService().hasParticulariteSansPersonnel(employeur, Date.now());
    }
}
