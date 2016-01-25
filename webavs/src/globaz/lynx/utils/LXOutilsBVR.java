package globaz.lynx.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

public class LXOutilsBVR {

    private static final char SIGNE_AUXILIAIRE_1 = '>';
    private static final char SIGNE_AUXILIAIRE_2 = '+';

    /**
     * @param zone
     * @return
     */
    public static String getCcpFormated(String zone) {
        StringBuffer strBuff = new StringBuffer();

        // Code BVR
        strBuff.append(zone.substring(0, 2));
        // - : Trait de liaison
        strBuff.append("-");

        if (zone.length() <= 5) {
            String s = zone.substring(2, zone.length());
            strBuff.append(JadeStringUtil.leftJustifyInteger(s, 6));
            // - : Trait de liaison
            strBuff.append("-");
            strBuff.append("0");
        } else {
            // Numéro d'ordre
            strBuff.append(zone.substring(2, zone.length() - 1));
            // - : Trait de liaison
            strBuff.append("-");
            // Chiffre clé
            strBuff.append(zone.substring(zone.length() - 1, zone.length()));
        }

        return strBuff.toString();
    }

    private String codeGenre;
    private String montant;
    private String numeroClient;
    private String numeroReference;

    private String referenceBVR;

    /**
     * Cosntructeur
     */
    public LXOutilsBVR() {
        super();
    }

    /**
     * Cosntructeur
     */
    public LXOutilsBVR(String referenceBVR) {
        super();
        setReferenceBVR(referenceBVR);
    }

    /**
     * Decoupage du numéro de référence
     * 
     * @return True si le decoupage est ok, false sinon
     */
    public boolean decouper() {

        StringBuffer zone = new StringBuffer();
        boolean resultat = true;
        int traitementZone = 1;

        // Si le numero de référence est vide, on retourne faux
        if (!JadeStringUtil.isBlank(referenceBVR)) {

            for (int i = 0; i < referenceBVR.length(); i++) {

                if ((LXOutilsBVR.SIGNE_AUXILIAIRE_1 != referenceBVR.charAt(i))
                        && (LXOutilsBVR.SIGNE_AUXILIAIRE_2 != referenceBVR.charAt(i))) {
                    zone.append(referenceBVR.charAt(i));
                } else {

                    switch (traitementZone) {
                        case 1:
                            resultat = traitePartie1(zone.toString());
                            break;
                        case 2:
                            resultat = traitePartie2(zone.toString());
                            break;
                        case 3:
                            resultat = traitePartie3(zone.toString());
                            break;
                        default:
                            return false;
                    }
                    traitementZone++;
                    zone = new StringBuffer();
                }
            }

        } else {
            resultat = false;
        }

        return resultat;
    }

    /**
     * Decoupage du numéro de référence
     * 
     * @param referenceBVR
     *            Une référence de bvr
     * @return True si le decoupage est ok, false sinon
     */
    public boolean decouper(String referenceBVR) {
        setReferenceBVR(referenceBVR);
        return this.decouper();
    }

    public String getCodeGenre() {
        return codeGenre;
    }

    public String getMontant() {
        if (!JadeStringUtil.isBlank(montant) && (montant.length() > 3)) {
            String montantFormated = montant.substring(0, montant.length() - 2) + "."
                    + montant.substring(montant.length() - 2);
            return new FWCurrency(montantFormated).toString();
        } else {
            return montant;
        }
    }

    public String getMontantFormatted() {
        if (!JadeStringUtil.isBlank(montant) && (montant.length() > 3)) {
            return JANumberFormatter.fmt(getMontant(), true, true, false, 2);
        } else {
            return getMontant();
        }

    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getNumeroClient() {
        return numeroClient;
    }

    public String getNumeroReference() {
        return numeroReference;
    }

    public String getReferenceBVR() {
        return referenceBVR;
    }

    public void setCodeGenre(String codeGenre) {
        this.codeGenre = codeGenre;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNumeroClient(String compteClient) {
        numeroClient = compteClient;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setNumeroReference(String numeroReference) {
        this.numeroReference = numeroReference;
    }

    public void setReferenceBVR(String referenceBVR) {
        this.referenceBVR = referenceBVR;
    }

    /**
     * Traite la premiere partie de la référence BVR. Cette zone contient : - le code du genre - un montant (dans le cas
     * d'un montant préimprimé) - le chiffre clé (modulo)
     * 
     * @param zone
     *            La premiere zone de la référence BVR
     * @return true si le traitement est ok, false sinon
     */
    protected boolean traitePartie1(String zone) {

        if (!JadeStringUtil.isBlank(zone)) {

            // Recuperation du code genre
            String codeGenre;
            if (zone.startsWith("<")) {
                codeGenre = zone.substring(1, 3);
            } else {
                codeGenre = zone.substring(0, 2);
            }

            if (codeGenre.length() != 2) {
                return false;
            }
            setCodeGenre(codeGenre);

            if (codeGenre.equals("01") || codeGenre.equals("03") || codeGenre.equals("11") || codeGenre.equals("21")
                    || codeGenre.equals("23")) {
                // Traitement du cas avec montant, code : 01, 03, 11, 21, 23
                setMontant(zone.substring(2, zone.length() - 1));
            } else if (codeGenre.equals("08")) {
                setMontant(zone.substring(7, zone.length()));
            } else if (!codeGenre.equals("04") && !codeGenre.equals("14") && !codeGenre.equals("31")
                    && !codeGenre.equals("33")) {
                // Si le code n'est pas : 04, 14, 31, 33 (cas d'un bvr sans
                // montant)
                // Le code n'existe pas.
                return false;
            }

        } else { // La zone est vide, on retourne faux
            return false;
        }

        return true;
    }

    /**
     * Traite la deuxieme partie de la référence BVR <BR>
     * Cette zone contient : <br>
     * - le numéro de référence <br>
     * - le chiffre clé (modulo)
     * 
     * @param zone
     *            La premiere zone de la référence BVR
     * @return true si le traitement est ok, false sinon
     */
    protected boolean traitePartie2(String zone) {
        if (!JadeStringUtil.isBlank(zone)) {

            setNumeroReference(zone.trim());

        } else { // La zone est vide, on retourne faux
            return false;
        }

        return true;
    }

    /**
     * Traite la troisieme partie de la référence BVR Cette zone contient : - le numéro de client - le chiffre clé
     * (modulo)
     * 
     * @param zone
     *            La premiere zone de la référence BVR
     * @return true si le traitement est ok, false sinon
     */
    protected boolean traitePartie3(String zone) {
        if (!JadeStringUtil.isBlank(zone)) {
            zone = zone.trim();

            setNumeroClient(LXOutilsBVR.getCcpFormated(zone));

        } else { // La zone est vide, on retourne faux
            return false;
        }

        return true;
    }

}
