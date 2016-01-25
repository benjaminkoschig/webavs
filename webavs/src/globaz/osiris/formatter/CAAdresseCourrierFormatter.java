package globaz.osiris.formatter;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;

/**
 * Date de création : (11.02.2002 09:21:51)
 * 
 * @author: Administrator
 */
public class CAAdresseCourrierFormatter {
    private static final String CASE_POSTALE_DE = "Postfach ";

    private static final String CASE_POSTALE_FR = "Case Postale ";
    private static final String CASE_POSTALE_IT = "Scatola postale ";
    private static final String CODE_ISO_LIST_INVERSE_POSTALCODE_AND_LOCALITY = "AM/AU/BD/BN/CA/GB/IN/ID/JO/JP/KR/KZ/LV/MN/MT/NP/PG/PK/PY/SA/SG/SV/TH/US";
    private IntAdresseCourrier adresse;
    private String nom = "";
    private boolean nomFirst = true; // Affiche le tiers avec le prénom puis le
    private IntTiers tiers = null;
    private String titre = "";
    private boolean useCountry = false;

    private boolean useTitle = true;

    // nom

    /**
     * Classe d'aide au formattage d'une adresse de courrier
     */
    public CAAdresseCourrierFormatter() {
        super();
    }

    /**
     * Date de création : (11.02.2002 09:23:02)
     * 
     * @param adresse
     *            globaz.osiris.interfaceext.tiers.IntAdresseCourrier l'adresse de courrier à formatter
     */
    public CAAdresseCourrierFormatter(IntTiers tiers, IntAdresseCourrier adresse) {
        this.adresse = adresse;
        this.tiers = tiers;
    }

    /**
     * Date de création : (11.02.2002 09:49:39)
     */
    private String _formatLieu() {

        // Initialser
        String buf = "";

        // Code ISO du pays si nécessaire
        if (!JadeStringUtil.isBlank(adresse.getPaysISO()) && useCountry) {
            buf += adresse.getPaysISO() + "-";
        }

        if (!JadeStringUtil.isBlank(adresse.getPaysISO())
                && (CAAdresseCourrierFormatter.CODE_ISO_LIST_INVERSE_POSTALCODE_AND_LOCALITY.indexOf(adresse
                        .getPaysISO()) > -1)) {
            if (!JadeStringUtil.isBlank(adresse.getLocalite())) {
                buf += adresse.getLocalite() + " ";
            }

            if (!JadeStringUtil.isBlank(adresse.getNumPostal())) {
                buf += adresse.getNumPostal();
            }
        } else {
            if (!JadeStringUtil.isBlank(adresse.getNumPostal())) {
                buf += adresse.getNumPostal() + " ";
            }

            if (!JadeStringUtil.isBlank(adresse.getLocalite())) {
                buf += adresse.getLocalite();
            }

        }

        return buf;
    }

    /**
     * Date de création : (11.02.2002 09:24:51)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    public IntAdresseCourrier getAdresseCourrier() {
        return adresse;
    }

    /**
     * Retourne l'adresse en maximisant le nombre de lignes désirées 1 ligne : - le nom 2 lignes : - le nom - le lieu 3
     * lignes : - le nom - la rue - le lieu 4 ligne : - le titre (si use title = true) - le nom - la rue - le lieu dès 5
     * lignes - le titre (si use title = true) - le nom - le complément (de 1 à x lignes) - A l'attention de - la rue -
     * le lieu dès 6 lignes - le titre (si use title = true) - le nom - le complément (de 1 à x lignes) - la rue - la
     * case postale - le lieu Date de création : (11.02.2002 09:58:27)
     * 
     * @return java.lang.String[] une tableau de chaînes de caractères contenant l'adresse
     * @param maxLines
     *            int nombre maximum de lignes désirées
     */
    public String[] getAdresseLines(int maxLines) throws Exception {
        return this.getAdresseLines(maxLines, tiers.getLangueISO());
    }

    /**
     * @param maxLines
     * @param printLanguage
     * @return
     */
    public String[] getAdresseLines(int maxLines, String printLanguage) throws Exception {
        maxLines = validateAdresseLinesMaxLines(maxLines);

        // Initialiser
        String[] adr = new String[maxLines];
        int i = 0;

        // Titre à partir de 4 lignes si désiré
        if ((maxLines >= 4) && useTitle) {
            if (!JadeStringUtil.isBlank(this.getTitre(printLanguage))) {
                adr[i++] = this.getTitre(printLanguage);
            }
        }

        // Nom (toujours)
        adr[i++] = getNom();

        String casePostale = getAdresseLinesCasePostale(printLanguage);

        if (JadeStringUtil.isBlank(casePostale)) {
            // Adresse complémentaire SANS case postale(dès 5 lignes ou dès 4
            // sans titre)
            if ((maxLines >= 5) || ((maxLines >= 4) && !useTitle)) {
                String[] adrCompl = getComplement();
                int nbr = 4;
                if (!useTitle) {
                    if (JadeStringUtil.isBlank(getAttention())) {
                        nbr = 3;
                    } else {
                        nbr = 4;
                    }
                }

                for (int j = 0; j < Math.min(maxLines - nbr, adrCompl.length); j++) {
                    if (!JadeStringUtil.isBlank(adrCompl[j])) {
                        adr[i++] = adrCompl[j];
                    }
                }
            }
        } else {
            // Adresse complémentaire AVEC case postale(dès 6 lignes ou dès 5
            // sans titre)
            if ((maxLines >= 6) || ((maxLines >= 5) && !useTitle)) {
                String[] adrCompl = getComplement();
                int nbr = 5;
                if (!useTitle) {
                    if (JadeStringUtil.isBlank(getAttention())) {
                        nbr = 4;
                    } else {
                        nbr = 5;
                    }
                }

                for (int j = 0; j < Math.min(maxLines - nbr, adrCompl.length); j++) {
                    if (!JadeStringUtil.isBlank(adrCompl[j])) {
                        adr[i++] = adrCompl[j];
                    }
                }
            }
        }
        // A l'attention de
        if ((maxLines >= 5) || ((maxLines >= 4) && !useTitle)) {
            if (!JadeStringUtil.isBlank(getAttention())) {
                adr[i++] = getAttention();
            }
        }

        // Rue à partir de 3 lignes
        if (maxLines >= 3) {
            if (!JadeStringUtil.isBlank(adresse.getRue())) {
                adr[i++] = adresse.getRue();
            }
        }

        if ((!JadeStringUtil.isBlank(casePostale)) && ((maxLines >= 6) || ((maxLines >= 5) && !useTitle))) {
            adr[i++] = casePostale;
        }

        // Lieu à partir de 2 lignes
        if (maxLines >= 2) {
            if (i == maxLines) {
                throw new Exception(((BSession) adresse.getISession()).getLabel("TROP_DE_LIGNES_A_IMPRIMER") + " ("
                        + getNom() + ")");
            }

            adr[i++] = _formatLieu();
        }

        // Vider les lignes restantes
        for (int j = i; j < adr.length; j++) {
            adr[j] = "";
        }

        return adr;
    }

    /**
     * Retourne la case postale en fonction de la langue.
     * 
     * @param language
     * @return String case postale (libellé et numéro)
     */
    private String getAdresseLinesCasePostale(String language) {
        String casePostale = adresse.getCasePostale();
        // Si la case postale est zéro, on n'affiche pas le numéro de la case
        if (!JadeStringUtil.isBlank(casePostale) && "0".equalsIgnoreCase(casePostale)) {
            if ("FR".equalsIgnoreCase(language)) {
                casePostale = CAAdresseCourrierFormatter.CASE_POSTALE_FR;
            } else if ("DE".equalsIgnoreCase(language)) {
                casePostale = CAAdresseCourrierFormatter.CASE_POSTALE_DE;
            } else {
                casePostale = CAAdresseCourrierFormatter.CASE_POSTALE_IT;
            }
            // Si la case postale n'est pas vide on affiche la case postale
        } else if (!JadeStringUtil.isBlank(casePostale)) {
            if ("FR".equalsIgnoreCase(language)) {
                casePostale = CAAdresseCourrierFormatter.CASE_POSTALE_FR + casePostale;
            } else if ("DE".equalsIgnoreCase(language)) {
                casePostale = CAAdresseCourrierFormatter.CASE_POSTALE_DE + casePostale;
            } else {
                casePostale = CAAdresseCourrierFormatter.CASE_POSTALE_IT + casePostale;
            }
        }
        return casePostale;
    }

    /**
     * Cette méthode permet de récupérer la ligne "à l'attention de"
     * 
     * @return String "à l'attention de "
     */
    public String getAttention() {
        return adresse.getAttention();
    }

    public String[] getComplement() {
        String[] complement = getAdresseCourrier().getAdresse();
        if ("".equalsIgnoreCase(getAdresseCourrier().getAutreNom()) && (complement.length > 0)
                && "".equalsIgnoreCase(complement[0])) {
            complement = tiers.getComplementNom();
        }
        return complement;
    }

    /**
     * Récupère l'adresse complète sans ligne vide entre les éléments Date de création : (11.02.2002 09:27:31)
     * 
     * @return java.lang.String[] une tableau de chaînes de caractères représentant l'adresse
     */
    public String[] getFullAdresse() {

        // Vérifier l'adresse
        if (adresse == null) {
            return null;
        }

        // Initialiser un tableau de chaînes
        String[] adr = new String[13];

        // Compteur de ligne
        int i = 0;

        // Insérer le titre
        if (!JadeStringUtil.isBlank(adresse.getTitre()) && useTitle) {
            adr[i++] = adresse.getTitre();
        }

        // Insérer le nom
        if (!JadeStringUtil.isBlank(tiers.getNom())) {
            adr[i++] = tiers.getNom();
        }

        // Insérer l'adresse complémentaire
        String[] adrCompl = adresse.getAdresse();
        if (adrCompl != null) {
            for (int j = 0; j < adrCompl.length; j++) {
                if (!JadeStringUtil.isBlank(adrCompl[j])) {
                    adr[i++] = adrCompl[j];
                }
            }
        }

        // Insérer la rue
        if (!JadeStringUtil.isBlank(adresse.getRue())) {
            adr[i++] = adresse.getRue();
        }

        if (!JadeStringUtil.isBlank(adresse.getCasePostale())) {
            String cp = adresse.getCasePostale();

            if ("FR".equalsIgnoreCase(tiers.getLangueISO())) {
                cp = CAAdresseCourrierFormatter.CASE_POSTALE_FR + cp;
            } else if ("DE".equalsIgnoreCase(tiers.getLangueISO())) {
                cp = CAAdresseCourrierFormatter.CASE_POSTALE_DE + cp;
            } else {
                cp = CAAdresseCourrierFormatter.CASE_POSTALE_IT + cp;
            }

            adr[i++] = cp;
        }

        // Insérer le lieu
        adr[i++] = _formatLieu();

        // Retourner un tableau correspondant au nombre de lignes
        String[] finalAdr = new String[i];
        for (int k = 0; k < i; k++) {
            finalAdr[k] = adr[k];
        }

        return finalAdr;
    }

    /**
     * Returns the nom.
     * 
     * @return String
     */
    public String getNom() {
        if ("".equalsIgnoreCase(getAdresseCourrier().getAutreNom())) {
            if ((!JadeStringUtil.isBlank(tiers.getNom())) && (nomFirst)) {
                nom = tiers.getNom();
            } else if ((!JadeStringUtil.isBlank(tiers.getPrenomNom())) && (!nomFirst)) {
                nom = tiers.getPrenomNom();
            }
        } else {
            nom = getAdresseCourrier().getAutreNom();
        }
        return nom;
    }

    /**
     * Returns the titre.
     * 
     * @return String
     */
    public String getTitre() {
        if (JadeStringUtil.isBlank(getAdresseCourrier().getAutreNom())) {
            titre = tiers.getTitre();
        } else {
            titre = getAdresseCourrier().getTitre();
        }

        return titre;
    }

    /**
     * Retourne le Titre en fonction de la langue sélectionné.
     * 
     * @param language
     * @return
     */
    public String getTitre(String language) {
        if (JadeStringUtil.isBlank(getAdresseCourrier().getAutreNom())) {
            titre = tiers.getTitre(language);
        } else {
            titre = getAdresseCourrier().getTitre();
        }

        return titre;
    }

    /**
     * Date de création : (11.02.2002 10:27:23)
     * 
     * @return boolean
     */
    public boolean getUseTitle() {
        return useTitle;
    }

    /**
     * @return
     */
    public boolean isNomFirst() {
        return nomFirst;
    }

    /**
     * Date de création : (11.02.2002 15:14:09)
     * 
     * @return boolean
     */
    public boolean isUseCountry() {
        return useCountry;
    }

    /**
     * Date de création : (11.02.2002 09:25:17)
     * 
     * @param newAdresseCourrier
     *            globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    public void setAdresseCourrier(IntTiers newTiers, IntAdresseCourrier newAdresseCourrier) {
        adresse = newAdresseCourrier;
        tiers = newTiers;
    }

    /**
     * Sets the nom.
     * 
     * @param nom
     *            The nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param b
     */
    public void setNomFirst(boolean b) {
        nomFirst = b;
    }

    /**
     * Sets the titre.
     * 
     * @param titre
     *            The titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Date de création : (11.02.2002 15:14:09)
     * 
     * @param newUseCountry
     *            boolean
     */
    public void setUseCountry(boolean newUseCountry) {
        useCountry = newUseCountry;
    }

    /**
     * Date de création : (11.02.2002 10:26:56)
     * 
     * @param newUseTitle
     *            boolean
     */
    public void setUseTitle(boolean newUseTitle) {
        useTitle = newUseTitle;
    }

    /**
     * Vérifier le paramètre maxLines.
     * 
     * @param maxLines
     * @return
     */
    private int validateAdresseLinesMaxLines(int maxLines) {
        maxLines = Math.max(1, maxLines); // au moins une ligne
        maxLines = Math.min(maxLines, 12); // maximum douze lignes
        return maxLines;
    }

}
