package globaz.osiris.formatter;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;

/**
 * Insérez la description du type ici. Date de création : (11.02.2002 09:21:51)
 * 
 * @author: Administrator
 */
public class CAAdresseCourrierFormatterFacture {
    private IntAdresseCourrier _adresse;
    private IntTiers _tiers = null;

    /**
     * Classe d'aide au formattage d'une adresse de courrier
     */
    public CAAdresseCourrierFormatterFacture() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.02.2002 09:23:02)
     * 
     * @param adresse
     *            globaz.osiris.interfaceext.tiers.IntAdresseCourrier l'adresse de courrier à formatter
     */
    public CAAdresseCourrierFormatterFacture(IntTiers tiers, IntAdresseCourrier adresse) {
        _adresse = adresse;
        _tiers = tiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.02.2002 09:49:39)
     */
    private String _formatLieu() {

        // Initialser
        String buf = "";

        if (!JadeStringUtil.isBlank(_adresse.getNumPostal())) {
            buf += _adresse.getNumPostal() + " ";
        }

        if (!JadeStringUtil.isBlank(_adresse.getLocalite())) {
            buf += _adresse.getLocalite();
        }

        return buf;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.02.2002 09:24:51)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    public IntAdresseCourrier getAdresseCourrier() {
        return _adresse;
    }

    /**
     * Retourne l'adresse sur 6 lignes Date de création : (16.07.2002 09:58:27)
     * 
     * @return java.lang.String[] une tableau de chaînes de caractères contenant l'adresse
     */
    public String[] getAdresseLines() {

        // Vérifier le paramètre
        // On aura 6 lignes au maximum dans noter adresse
        int nbrLigAdresse = 6;

        // Initialiser
        String[] adr = new String[nbrLigAdresse];
        int i = 0;

        // Nom (toujours)
        if (!JadeStringUtil.isBlank(_tiers.getNom())) {
            adr[i++] = _tiers.getNom();
        }

        // Adresse complémentaire (on prend que les 2 lignes du début ligne 1 et
        // Ligne 2 (pas ligne 3 et ligne 4)
        String[] adrCompl = _adresse.getAdresse();
        int nbr;
        if (adrCompl.length >= 2) {
            nbr = 2;
        } else {
            nbr = adrCompl.length;
        }
        for (int j = 0; j < nbr; j++) {
            if (!JadeStringUtil.isBlank(adrCompl[j])) {
                adr[i++] = adrCompl[j];
            }
        }

        // Ajout de la rue si nécessaire
        if (!JadeStringUtil.isBlank(_adresse.getRue())) {
            adr[i++] = _adresse.getRue();
        }
        // Ajout de la case postale
        if (!JadeStringUtil.isBlank(_adresse.getCasePostale())) {
            adr[i++] = _adresse.getCasePostale();
        }

        // Ajout de la localite
        adr[i++] = _formatLieu();

        // Vider les lignes restantes
        for (int j = i; j < adr.length; j++) {
            adr[j] = "";
        }

        return adr;
    }

    /**
     * Retourne l'adresse depuis le tiers de Pyxis (getAdresseAsString(CS_courrier)
     * 
     * @return java.lang.String[] une tableau de chaînes de caractères contenant l'adresse
     */
    public String[] getAdresseLinesTiers() {

        // Vérifier le paramètre
        // On aura 6 lignes au maximum dans noter adresse
        int nbrLigAdresse = 6;
        int i = 0;

        // Initialiser
        String[] adr = new String[nbrLigAdresse];
        if (_adresse != null) {
            String[] adrCompl = _adresse.getAdresse();
            for (int j = 0; j < adrCompl.length; j++) {
                if (!JadeStringUtil.isBlank(adrCompl[j])) {
                    adr[i++] = adrCompl[j];
                }
            }
        }
        // Vider les lignes restantes
        for (int j = i; j < adr.length; j++) {
            adr[j] = "";
        }

        return adr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.02.2002 09:25:17)
     * 
     * @param newAdresseCourrier
     *            globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    public void setAdresseCourrier(IntTiers newTiers, IntAdresseCourrier newAdresseCourrier) {
        _adresse = newAdresseCourrier;
        _tiers = newTiers;
    }
}
