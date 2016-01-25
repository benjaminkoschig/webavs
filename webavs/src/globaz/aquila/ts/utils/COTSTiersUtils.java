package globaz.aquila.ts.utils;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.ts.opge.COTSOPGEExecutor;
import globaz.aquila.ts.rules.utils.COOPGESpellChecker;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

public class COTSTiersUtils {

    private static final int NPA_ETRANGER_MAX_LENGTH = 5;

    /**
     * Convertit les caractères spéciaux et transforme les äöü en aeoeue
     * 
     * @param text
     * @return
     * @throws Exception
     */
    public static String convertSpecialChars(String text) throws Exception {
        text = JadeStringUtil.change(text, "ä", "ae");
        text = JadeStringUtil.change(text, "ö", "oe");
        text = JadeStringUtil.change(text, "ü", "ue");
        text = JadeStringUtil.change(text, "Ä", "AE");
        text = JadeStringUtil.change(text, "Ö", "OE");
        text = JadeStringUtil.change(text, "Ü", "UE");

        text = JadeStringUtil.convertSpecialChars(text);

        return text;
    }

    /**
     * Retourne la ligne d'adresse trimé à maxlength si nécessaire. Remplace le caractère " " par separator si passé en
     * paramètre. <br/>
     * Renvois un String en UpperCase.
     * 
     * @param ligne
     * @param maxLength
     * @param separator
     * @param allowTrim
     * @param nomTiers
     * @return
     * @throws Exception
     */
    private static String getLigne(String ligne, int maxLength, String separator, boolean allowTrim, String nomTiers)
            throws Exception {
        if (JadeStringUtil.isBlank(ligne)) {
            return "";
        } else {
            ligne = COTSTiersUtils.convertSpecialChars(ligne);
            ligne = ligne.toUpperCase();

            String result;
            if (JadeStringUtil.isBlank(separator) || separator.equals(" ")) {
                result = ligne;
            } else {
                nomTiers = COTSTiersUtils.convertSpecialChars(nomTiers);

                if (nomTiers != null) {
                    nomTiers = nomTiers.toUpperCase();
                }

                if ((nomTiers != null) && (ligne.indexOf(nomTiers) > -1)) {
                    result = nomTiers.trim() + separator
                            + JadeStringUtil.remove(ligne, ligne.indexOf(nomTiers), nomTiers.length() + 1).trim();
                } else {
                    result = JadeStringUtil.change(ligne.trim(), " ", separator);
                }
            }

            if ((result.length() > maxLength) && allowTrim) {
                return result.substring(0, maxLength);
            } else {
                return result;
            }
        }
    }

    /**
     * Return la première ligne de l'adresse.
     * 
     * @param session
     * @param contentieux
     * @param adresseDataSource
     * @param allowTrim
     * @return la première ligne de l'adresse
     * @throws Exception
     */
    public static String getLigne1(BSession session, COContentieux contentieux, TIAdresseDataSource adresseDataSource,
            boolean allowTrim) throws Exception {
        if (COServiceLocator.getTiersService().isPersonnePhysique(session, contentieux.getCompteAnnexe().getTiers())) {
            return COTSTiersUtils.getLigne(adresseDataSource.fullLigne1, COTSOPGEExecutor.LENGTH_NOM, "#", allowTrim,
                    adresseDataSource.tiersLigne1);
        } else {
            return COTSTiersUtils.getLigne(adresseDataSource.fullLigne1, COTSOPGEExecutor.LENGTH_NOM, " ", allowTrim,
                    null);
        }
    }

    /**
     * Return la 2ème ligne de l'adresse. En premier la ligne 2 du tiers, si vide ligne 3, si vide ligne 4, si vide
     * return vide.
     * 
     * @param adresseDataSource
     * @param allowTrim
     * @return
     * @throws Exception
     */
    public static String getLigne2(TIAdresseDataSource adresseDataSource, boolean allowTrim) throws Exception {
        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne2)) {
            return COTSTiersUtils.getLigne(adresseDataSource.fullLigne2, COTSOPGEExecutor.LENGTH_SUITE_NOM, null,
                    allowTrim, null);
        } else if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne3)) {
            return COTSTiersUtils.getLigne(adresseDataSource.fullLigne3, COTSOPGEExecutor.LENGTH_SUITE_NOM, null,
                    allowTrim, null);
        } else if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne4)) {
            return COTSTiersUtils.getLigne(adresseDataSource.fullLigne4, COTSOPGEExecutor.LENGTH_SUITE_NOM, null,
                    allowTrim, null);
        } else {
            return "";
        }
    }

    /**
     * Return un cumul des lignes pour le datasource passé en paramètre.
     * 
     * @param adresseDataSource
     * @return
     */
    public static String getLigneCumule(TIAdresseDataSource adresseDataSource, String separator) {
        String result = adresseDataSource.fullLigne1;

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne2)) {
            result += separator + adresseDataSource.fullLigne2;
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne3)) {
            result += separator + adresseDataSource.fullLigne3;
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne4)) {
            result += separator + adresseDataSource.fullLigne4;
        }

        return result;
    }

    /**
     * Return le nom ou la raison sociale du débiteur (zone 10)
     * 
     * @param session
     * @param contentieux
     * @param adresseDataSource
     * @return le nom ou la raison sociale du débiteur (zone 10)
     * @throws Exception
     */
    public static String getNomOuRaisonSociale(BSession session, COContentieux contentieux,
            TIAdresseDataSource adresseDataSource) throws Exception {
        if (COTSTiersUtils.getLigne1(session, contentieux, adresseDataSource, false).length() <= COTSOPGEExecutor.LENGTH_NOM) {
            return COTSTiersUtils.getLigne1(session, contentieux, adresseDataSource, false);
        } else {
            return COTSTiersUtils.getLigne1(session, contentieux, adresseDataSource, true);
        }
    }

    /**
     * Return la suite du nom ou de la raison sociale du débiteur (zone 10)
     * 
     * @param session
     * @param contentieux
     * @param adresseDataSource
     * @return la suite du nom ou de la raison sociale du débiteur (zone 10)
     * @throws Exception
     */
    public static String getSuiteNomOuRaisonSociale(BSession session, COContentieux contentieux,
            TIAdresseDataSource adresseDataSource) throws Exception {
        if (COTSTiersUtils.getLigne1(session, contentieux, adresseDataSource, false).length() <= COTSOPGEExecutor.LENGTH_NOM) {
            return COTSTiersUtils.getLigne2(adresseDataSource, true);
        } else {
            String suiteNom = COTSTiersUtils.getLigne1(session, contentieux, adresseDataSource, false);

            suiteNom = suiteNom.substring(COTSOPGEExecutor.LENGTH_NOM);
            if (suiteNom.length() > COTSOPGEExecutor.LENGTH_SUITE_NOM) {
                return suiteNom.substring(0, COTSOPGEExecutor.LENGTH_SUITE_NOM);
            }

            String ligne2 = COTSTiersUtils.getLigne2(adresseDataSource, false);
            if (JadeStringUtil.isBlank(ligne2)) {
                return suiteNom;
            } else if (ligne2.length() + 1 <= (COTSOPGEExecutor.LENGTH_SUITE_NOM - suiteNom.length())) {
                return suiteNom + " " + ligne2;
            } else {
                return suiteNom + " "
                        + ligne2.substring(0, (COTSOPGEExecutor.LENGTH_SUITE_NOM - suiteNom.length() - 1));
            }
        }
    }

    /**
     * Limite la longueur d'un npa étranger. Excemple : 7400001 => 74000
     * 
     * @param npa
     * @return
     * @throws Exception
     */
    public static String limitNpaEtranger(String npa) throws Exception {
        if (JadeStringUtil.isBlank(npa)) {
            return npa;
        } else {
            if (npa.length() > COTSTiersUtils.NPA_ETRANGER_MAX_LENGTH) {
                return npa.substring(0, COTSTiersUtils.NPA_ETRANGER_MAX_LENGTH);
            } else {
                return npa;
            }
        }
    }

    /**
     * Doit-on créer une zone de texte débiteur (11) automatiquement ? <br/>
     * Oui si ligne1 > 24 ou ligne2 > 24 ou une 3ème ligne d'adresse est présente.
     * 
     * @param session
     * @param contentieux
     * @param adresseDataSourceDebiteur
     * @return
     * @throws Exception
     */
    public static boolean needTexteDuDebiteurAutomatique(BSession session, COContentieux contentieux,
            TIAdresseDataSource adresseDataSourceDebiteur) throws Exception {
        if (!COOPGESpellChecker.isLengthNomAllowed(COTSTiersUtils.getLigne1(session, contentieux,
                adresseDataSourceDebiteur, false))) {
            return true;
        } else if (!COOPGESpellChecker.isLengthSuiteNomAllowed(COTSTiersUtils.getLigne2(adresseDataSourceDebiteur,
                false))) {
            return true;
        } else {
            int count = 0;
            if (!JadeStringUtil.isBlank(adresseDataSourceDebiteur.fullLigne1)) {
                count++;
            }
            if (!JadeStringUtil.isBlank(adresseDataSourceDebiteur.fullLigne2)) {
                count++;
            }
            if (!JadeStringUtil.isBlank(adresseDataSourceDebiteur.fullLigne3)) {
                count++;
            }
            if (!JadeStringUtil.isBlank(adresseDataSourceDebiteur.fullLigne4)) {
                count++;
            }

            return count > 2;
        }
    }
}
