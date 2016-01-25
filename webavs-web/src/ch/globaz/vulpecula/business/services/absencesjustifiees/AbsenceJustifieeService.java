package ch.globaz.vulpecula.business.services.absencesjustifiees;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;

public interface AbsenceJustifieeService {
    /**
     * Cr�ation d'une absence justifi�e en v�rifiant le respect des sp�cifications.
     * 
     * @param absenceJustifiee Absence justifi�e � cr�er
     * @return L'absence justifi�e cr�� avec la mise � jour de l'id et du spy
     * @throws UnsatisfiedSpecificationException Exception retourn�e en cas de non respect des sp�cifications
     */
    public AbsenceJustifiee create(AbsenceJustifiee absenceJustifiee) throws UnsatisfiedSpecificationException;
}
