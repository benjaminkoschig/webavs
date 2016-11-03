package globaz.apg.calculateur.maternite.acm2;

import java.util.List;

public class APCalculateurACM2BusinessData {

    private String idDroit;
    private List<ACM2BusinessDataParEmployeur> donneesParEmployeur;

    /**
     * @param idDroit
     * @param donneesParEmployeur
     */
    public APCalculateurACM2BusinessData(String idDroit, List<ACM2BusinessDataParEmployeur> donneesParEmployeur) {
        super();
        this.idDroit = idDroit;
        this.donneesParEmployeur = donneesParEmployeur;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public List<ACM2BusinessDataParEmployeur> getDonneesParEmployeur() {
        return donneesParEmployeur;
    }

}
