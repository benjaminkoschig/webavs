package ch.globaz.pegasus.business.models.process.adaptation;

import ch.globaz.pegasus.businessimpl.utils.PegasusSelectField;

public class TypeChambreHome extends PegasusSelectField {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String idTypeChambre = null;

    @Override
    public String getId() {
        return idTypeChambre;
    }

    public String getIdTypeChambre() {
        return idTypeChambre;
    }

    @Override
    public void setId(String id) {
        idTypeChambre = id;
    }

    public void setIdTypeChambre(String idTypeChambre) {
        this.idTypeChambre = idTypeChambre;
    }

}
