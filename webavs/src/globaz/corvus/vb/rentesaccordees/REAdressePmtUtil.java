package globaz.corvus.vb.rentesaccordees;

import globaz.jade.client.util.JadeStringUtil;

public class REAdressePmtUtil {

    private String adresseFormattee = "";
    private String ccpOuBanqueFormatte = "";
    private String csDomaine = "";
    private String idTiers = "";
    private String nom = "";
    private String prenom = "";

    public String getAdresseFormattee() {
        String r = JadeStringUtil.change(adresseFormattee, '"', '\'');
        r = JadeStringUtil.change(r, "\n", "<br/>");
        return r;
    }

    public String getCcpOuBanqueFormatte() {
        String r = JadeStringUtil.change(ccpOuBanqueFormatte, '"', '\'');
        r = JadeStringUtil.change(r, "\n", "<br/>");
        return r;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setAdresseFormattee(String adresseFormattee) {
        this.adresseFormattee = adresseFormattee;
    }

    public void setCcpOuBanqueFormatte(String ccpOuBanqueFormatte) {
        this.ccpOuBanqueFormatte = ccpOuBanqueFormatte;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
