package globaz.pavo.print.itext;

import java.util.ArrayList;

public class CIConcordanceEmployeurStruc {

    private String adresse;
    private String affilieNumero;
    private String documentTitle;
    private String idTiers;
    private ArrayList listEmploye;

    /**
     * Structure de données de l'employeur pour l'impression CSV/PDF
     * 
     * @param listEmploye
     *            contient la liste des employés affiliés à l'employeur
     */

    public CIConcordanceEmployeurStruc(ArrayList listEmploye) {
        this.listEmploye = listEmploye;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getAffilieNumero() {
        return affilieNumero;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public ArrayList getListEmploye() {
        return listEmploye;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAffilieNumero(String affilieNumero) {
        this.affilieNumero = affilieNumero;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
