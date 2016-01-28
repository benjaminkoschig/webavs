package globaz.apg.db.annonces;

import globaz.globall.db.BStatement;

/**
 * Entit� base de donn�e encapsulant une partie des donn�es n�cessaire � la g�n�ration de la liste de r�capitulation des
 * annonces APG (avant septembre 2012).
 * 
 * @author VRE
 * @author PBA
 * @author LGA
 */
public class APRecapitulationAnnonceHermes extends APAbstractRecapitulationAnnonce {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String contenuAnnonce;

    public APRecapitulationAnnonceHermes() {
        super();
        contenuAnnonce = "";
    }

    @Override
    public String getCodeUtilisateurTypeMontant() {
        return contenuAnnonce;
    }

    public String getContenuAnnonce() {
        return contenuAnnonce;
    }

    @Override
    protected String getSpecificFields() {
        return APAnnonceAPG.FIELDNAME_CONTENUANNONCE;
    }

    @Override
    protected void readSpecificFields(BStatement statement) throws Exception {
        contenuAnnonce = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_CONTENUANNONCE);
    }

    public void setContenuAnnonce(String contenuAnnonce) {
        this.contenuAnnonce = contenuAnnonce;
    }
}
