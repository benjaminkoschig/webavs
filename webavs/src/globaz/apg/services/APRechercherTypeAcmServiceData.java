package globaz.apg.services;

import globaz.apg.enums.APTypeDePrestation;

public class APRechercherTypeAcmServiceData {

    private String csAssociation;
    private String nomAssociation;
    private APTypeDePrestation typeDePrestation;

    public APRechercherTypeAcmServiceData(String csAssociation, String nomAssociation,
            APTypeDePrestation typeDePrestation) {
        super();
        this.csAssociation = csAssociation;
        this.nomAssociation = nomAssociation;
        this.typeDePrestation = typeDePrestation;
    }

    public String getCsAssociation() {
        return csAssociation;
    }

    public APTypeDePrestation getTypeDePrestation() {
        return typeDePrestation;
    }

    public void setAssociation(String csAssociation) {
        this.csAssociation = csAssociation;
    }

    public void setTypeDePrestation(APTypeDePrestation typeDePrestation) {
        this.typeDePrestation = typeDePrestation;
    }

    public final String getNomAssociation() {
        return nomAssociation;
    }

}
