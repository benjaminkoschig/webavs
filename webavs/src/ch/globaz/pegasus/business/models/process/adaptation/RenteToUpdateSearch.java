package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class RenteToUpdateSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<String> inIdDonneeFinanciere = null;

    public Collection<String> getInIdDonneeFinanciere() {
        return inIdDonneeFinanciere;
    }

    public void setInIdDonneeFinanciere(Collection<String> inIdDonneeFinanciere) {
        this.inIdDonneeFinanciere = inIdDonneeFinanciere;
    }

    @Override
    public Class<RenteToUpdate> whichModelClass() {
        return RenteToUpdate.class;
    }

}
