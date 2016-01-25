package globaz.vulpecula.vb.absencesjustifiees;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.businessimpl.services.prestations.PrestationStatus;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * ViewBean Ajax utilisée dans afin d'afficher les absences justifiées d'un travailleur.
 * 
 */
public class PTAbsencesjustifieesAjaxViewBean extends JadeAbstractAjaxFindForDomain<AbsenceJustifiee> {
    private static final long serialVersionUID = 1L;

    private String idTravailleur;

    private PrestationStatus prestationStatus = new PrestationStatus();

    /**
     * Retourne les absences justifiées pour le travailleur sélectionné.
     * 
     * @return Liste représentant les absences du travailleur
     */
    public List<AbsenceJustifiee> getAbsencesJustifiees() {
        return getList();
    }

    /**
     * Retourne l'id du travailleur sur lequel la recherche est effectuée.
     * 
     * @return String représentant l'id du travailleur
     */
    public String getIdTravailleur() {
        return idTravailleur;
    }

    /**
     * Set l'id du travailleur sur lequel sera recherché les absences justifiées.
     * 
     * @param idTravailleur String représentant l'id du travailleur
     */
    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    @Override
    public AbsenceJustifiee getEntity() {
        return new AbsenceJustifiee();
    }

    @Override
    public Repository<AbsenceJustifiee> getRepository() {
        return VulpeculaRepositoryLocator.getAbsenceJustifieeRepository();
    }

    @Override
    public List<AbsenceJustifiee> findByRepository() {
        return VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findByIdTravailleurOrderByIdpassage(
                idTravailleur);
    }

    public boolean isModifiable(AbsenceJustifiee absenceJustifiee) {
        return prestationStatus.isModifiable(absenceJustifiee);
    }

    /**
     * Nécessaire pour WS
     */
    public boolean isModifiable(Object object) {
        return isModifiable((AbsenceJustifiee) object);
    }
}
