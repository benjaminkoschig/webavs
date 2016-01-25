package ch.globaz.vulpecula.business.services.absencesjustifiees;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;

public interface AbsenceJustifieeService {
    /**
     * Création d'une absence justifiée en vérifiant le respect des spécifications.
     * 
     * @param absenceJustifiee Absence justifiée à créer
     * @return L'absence justifiée créé avec la mise à jour de l'id et du spy
     * @throws UnsatisfiedSpecificationException Exception retournée en cas de non respect des spécifications
     */
    public AbsenceJustifiee create(AbsenceJustifiee absenceJustifiee) throws UnsatisfiedSpecificationException;
}
