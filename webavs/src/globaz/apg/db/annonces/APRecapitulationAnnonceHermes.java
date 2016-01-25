package globaz.apg.db.annonces;

import globaz.globall.db.BStatement;

/**
 * Entité base de donnée encapsulant une partie des données nécessaire à la génération de la liste de récapitulation des
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
