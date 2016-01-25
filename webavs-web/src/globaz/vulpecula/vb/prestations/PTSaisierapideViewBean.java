package globaz.vulpecula.vb.prestations;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.users.UsersServiceImpl;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.web.views.prestations.PrestationsViewService;

/**
 * @author JPA
 * 
 */
public class PTSaisierapideViewBean extends BJadeSearchObjectELViewBean {
    private String idTravailleur;
    private String typePrestation;

    private String nomPrenomTravailleur;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void retrieve() throws Exception {
        if (!JadeStringUtil.isEmpty(idTravailleur)) {
            nomPrenomTravailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur)
                    .getNomPrenomTravailleur();
        }
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Retourne les codes systèmes relatifs au groupe de codes systèmes bénéficiaire.
     * 
     * @return Liste de codes systèmes
     */
    public List<CodeSystem> getBeneficiaires() {
        return PrestationsViewService.getBeneficiairesForAJ();
    }

    public Taux getTauxAvs() {
        return new Taux(VulpeculaServiceLocator.getCotisationService().findTauxParitaireAVS().getValeurEmployeur());
    }

    public Taux getTauxAc() {
        return new Taux(VulpeculaServiceLocator.getCotisationService().findTauxParitaireAC().getValeurEmployeur());
    }

    public String getPrestationsViewService() {
        return PrestationsViewService.class.getName();
    }

    public String getUsersService() {
        return UsersServiceImpl.class.getName();
    }

    public String getGenrePrestationAJ() {
        return TypePrestation.ABSENCES_JUSTIFIEES.getValue();
    }

    public String getGenrePrestationCP() {
        return TypePrestation.CONGES_PAYES.getValue();
    }

    public String getGenrePrestationSM() {
        return TypePrestation.SERVICES_MILITAIRE.getValue();
    }

    public String getAjouterSuccesLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("SAISIE_RAPIDE_AJOUT_SUCCES");
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public String getNomPrenomTravailleur() {
        return nomPrenomTravailleur;
    }

    public void setNomPrenomTravailleur(String nomPrenomTravailleur) {
        this.nomPrenomTravailleur = nomPrenomTravailleur;
    }

    public String getPasDroitsLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("PAS_DROIT_SAISIE_RAPIDE");
    }
}
