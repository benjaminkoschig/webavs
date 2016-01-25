package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class RenteAvsAiWithMembreFamille extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamille droitMembreFamille = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleRenteAvsAi simpleRenteAvsAi = null;

    public RenteAvsAiWithMembreFamille() {
        super();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        droitMembreFamille = new DroitMembreFamille();
        simpleRenteAvsAi = new SimpleRenteAvsAi();
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
