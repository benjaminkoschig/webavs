package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

/**
 * V�rifie que le nombre d'heure renseign� soit positif
 * 
 * @since WebBMS 0.01.03
 */
public class DecompteSalaireHeurePositiveSpecification extends AbstractSpecification<DecompteSalaire> {

    @Override
    public boolean isValid(final DecompteSalaire t) {
        if (t == null) {
            throw new NullPointerException("Le d�compte salaire est null !");
        }

        if (t.getHeures() < 0) {
            addMessage(SpecificationMessage.DECOMPTE_NOMBRE_HEURES_POSITIF);
        }
        return true;
    }

}
