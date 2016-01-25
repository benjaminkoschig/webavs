package globaz.apg.db.annonces;

import globaz.globall.db.BStatement;

/**
 * Entit� base de donn�e encapsulant une partie des donn�es n�cessaire � la g�n�ration de la liste de r�capitulation des
 * annonces APG (apr�s septembre 2012).
 * 
 * @author VRE
 * @author PBA
 */
public class APRecapitulationAnnonce extends APAbstractRecapitulationAnnonce {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String typeAnnonce;

    public APRecapitulationAnnonce() {
        super();

        typeAnnonce = "";
    }

    @Override
    public String getCodeUtilisateurTypeMontant() {
        return typeAnnonce;
    }

    @Override
    protected String getSpecificFields() {
        return APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE;
    }

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    @Override
    protected void readSpecificFields(BStatement statement) throws Exception {
        typeAnnonce = statement.dbReadString(APAnnonceAPG.FIELDNAME_SUB_MESSAGE_TYPE);
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }
}
