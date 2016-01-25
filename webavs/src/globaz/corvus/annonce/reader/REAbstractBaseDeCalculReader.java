package globaz.corvus.annonce.reader;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Contient des méthodes de lecture des champs de la table REBACAL 'commun' à la 9ème et 10ème révision
 * 
 * @author lga
 * 
 */
public class REAbstractBaseDeCalculReader extends REAbstractBEntityValueReader {

    /**
     * REBACAL.YINDIN</br>
     * Lit le dégrée d'invalidité et le convertis en nombre entier</br>
     * Format : nnn, c'est un % (n est un nombre, ex : 45, 100, 0)</br>
     * </br>
     * 
     * @param degreInvalidite Le degré d'invalidité contenu dans la base de calcul
     * @return dégrée d'invalidité convertis en nombre entier ou null si le champs est vide ou n'a pas le bon format (2
     *         caractères numérique)
     */
    public Integer readDegreInvalidite(String degreInvalidite) {
        Integer value = null;
        if (JadeNumericUtil.isInteger(degreInvalidite) && degreInvalidite.length() <= 3) {
            value = Integer.valueOf(degreInvalidite);
        }
        return value;
    }

    /**
     * REBACAL.YIDDCA
     * Lit le nombre d'année de cotisation avant 1973 et le convertis en nombre entier
     * Format en DB : AAMM (AA -> nombre d'année, MM -> nombre de mois, ex : 0406 = 4 année et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'année, MM -> nombre de mois, ex : 04.06 = 4 année et 6 mois)
     * 
     * @param dureeCotiAvant73 la durée de cotisation avant 1973
     * @return nombre d'année de cotisation avant 1973 OU null si null, vide ou n'a pas le bon format (4
     *         caractères numérique)
     */
    public Integer readDureeCoEchelleRenteAv73_nombreAnnee(String dureeCotiAvant73) {
        Integer value = null;
        if (!JadeStringUtil.isBlank(dureeCotiAvant73) && dureeCotiAvant73.length() == 5) {
            if (dureeCotiAvant73.contains(".")) {
                String[] values = dureeCotiAvant73.split("\\.");
                if (JadeNumericUtil.isInteger(values[0])) {
                    value = Integer.valueOf(values[0]);
                }
            }
        }
        return value;
    }

    /**
     * REBACAL.YIDDCA
     * Lit le nombre de mois de cotisation avant 1973 et le convertis en nombre entier
     * Format en DB : AAMM (AA -> nombre d'année, MM -> nombre de mois, ex : 0406 = 4 année et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'année, MM -> nombre de mois, ex : 04.06 = 4 année et 6 mois)
     * 
     * @param dureeCotiAvant73 la durée de cotisation avant 1973
     * @return nombre de mois de cotisation avant 1973 OU null si null, vide ou n'a pas le bon format (2
     *         caractère numérique)
     */
    public Integer readDureeCoEchelleRenteAv73_nombreMois(String dureeCotiAvant73) {
        Integer value = null;
        if (!JadeStringUtil.isBlank(dureeCotiAvant73) && dureeCotiAvant73.length() == 5) {
            if (dureeCotiAvant73.contains(".")) {
                String[] values = dureeCotiAvant73.split("\\.");
                if (JadeNumericUtil.isInteger(values[1])) {
                    value = Integer.valueOf(values[1]);
                }
            }
        }
        return value;
    }

    /**
     * REBACAL.YIDDCD
     * Lit le nombre d'année de cotisation dès 1973 et le convertis en nombre entier
     * Format en DB : AAMM (AA -> nombre d'année, MM -> nombre de mois, ex : 0406 = 4 année et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'année, MM -> nombre de mois, ex : 04.06 = 4 année et 6 mois)
     * 
     * @param dureeCotiDes73 la durée de cotisation dès 1973
     * @return nombre d'année de cotisation dès 1973 OU null si null, vide ou n'a pas le bon format (4
     *         caractères numérique)
     */
    public Integer readDureeCoEchelleRenteDes73_nombreAnnee(String dureeCotiDes73) {
        Integer value = null;
        if (!JadeStringUtil.isBlank(dureeCotiDes73) && dureeCotiDes73.length() == 5) {
            if (dureeCotiDes73.contains(".")) {
                String[] values = dureeCotiDes73.split("\\.");
                if (JadeNumericUtil.isInteger(values[0])) {
                    value = Integer.valueOf(values[0]);
                }
            }
        }
        return value;
    }

    /**
     * REBACAL.YIDDCD
     * Lit le nombre de mois de cotisation dès 1973 et le convertis en nombre entier
     * Format en DB : AAMM (AA -> nombre d'année, MM -> nombre de mois, ex : 0406 = 4 année et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'année, MM -> nombre de mois, ex : 04.06 = 4 année et 6 mois)
     * 
     * @param dureeCotiDes73 la durée de cotisation dès 1973
     * @return nombre de mois de cotisation dès 1973 OU null si null, chaîne vide ou n'a pas le bon format (2
     *         caractère numérique)
     */
    public Integer readDureeCoEchelleRenteDes73_nombreMois(String dureeCotiDes73) {
        Integer value = null;
        if (!JadeStringUtil.isBlank(dureeCotiDes73) && dureeCotiDes73.length() == 5) {
            if (dureeCotiDes73.contains(".")) {
                String[] values = dureeCotiDes73.split("\\.");
                if (JadeNumericUtil.isInteger(values[1])) {
                    value = Integer.valueOf(values[1]);
                }
            }
        }
        return value;
    }

    /**
     * REBACAL.YIDABT
     * Retourne le nombre d'années de bonification transitoire et le convertis en nombre entier
     * Format DB : AM (A -> nombre d'année (max 8), M -> nombre de mois (0 ou 5), ex : 0405 = 4 année et demie)
     * Format BEntity : A.M
     * 
     * 
     * @param anneeBonifTransitoire le nombre d'années de bonification transitoire
     * @return le nombre d'années de bonification transitoire OU null si null, vide ou n'a pas le bon format (2
     *         caractères numérique)
     */
    public Integer readNbreAnneeBonifTrans_nombreAnnee(String anneeBonifTransitoire) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(anneeBonifTransitoire)) {
            String[] valeurs = anneeBonifTransitoire.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[0])) {
                result = convertToInteger(valeurs[0]);
            }
        }
        return result;
    }

    /**
     * REBACAL.YIDABT
     * Retourne le nombre d'années de bonification transitoire
     * Format DB : AM (A -> nombre d'année (max 8), M -> nombre de mois (0 ou 5), ex : 0405 = 4 année et demie)
     * Format BEntity : A.M
     * 
     * @param anneeBonifTransitoire le nombre d'années de bonification transitoire
     * @return le nombre d'années de bonification transitoire OU null si null, vide ou n'a pas
     *         le bon format (2 caractères numérique)
     */
    public Boolean readNbreAnneeBonifTrans_isDemiAnnee(String anneeBonifTransitoire) {
        Boolean value = null;
        if (!JadeStringUtil.isBlank(anneeBonifTransitoire)) {
            String[] valeurs = anneeBonifTransitoire.split("\\.");
            if (valeurs.length == 2) {
                if (!JadeStringUtil.isBlank(valeurs[1])) {
                    if ("5".equals(valeurs[1])) {
                        value = Boolean.TRUE;
                    } else {
                        value = Boolean.FALSE;
                    }
                }
            }
        }
        return value;
    }

    /**
     * Retourne le code infirmité uniquement</br>
     * Dans la base de calcul, le champs codeInfirmité est agrégé avec le codeAtteinte comme suit :</br>
     * code infirmité : 3 positions -> xxx</br>
     * code atteinte : 2 positions -> aa</br>
     * agrégé comme suit : xxxaa</br>
     * 
     * Cette méthode retourne la valeur xxx</br>
     * 
     * @param codeInfirmiteEtAtteinte Le code infirmitéEtAtteinte de la base de calcul
     * @return le code infirmité ou null si <code>codeInfirmiteEtAtteinte</code> est null, vide ou n'as pas le bon
     *         format (5 caractères numérique)
     */
    public Integer readCodeInfirmite(String codeInfirmiteEtAtteinte) {
        Integer result = null;
        if (JadeNumericUtil.isInteger(codeInfirmiteEtAtteinte)) {
            if (codeInfirmiteEtAtteinte.length() == 5) {
                result = convertToInteger(codeInfirmiteEtAtteinte.substring(0, 3));
            }
        }
        return result;
    }

    public Integer readEchelleRente(String echelleRente) {
        return convertToInteger(echelleRente);
    }

    /**
     * Retourne le nombre de mois de cotisation manquante avant 1973 formaté
     * Le format dans la base de calcul est MM (nombre de mois)
     * 
     * @param dureeCotManquante48_72 le nombre de mois de cotisation manquante
     * @return le nombre de mois de cotisation manquante avant 1973 formaté ou null si
     *         <code>dureeCotManquante48_72</code> est null, vide ou n'a pas le bon format (2 caractères numérique)
     */
    public Integer readDureeCotManquante48_72(String dureeCotManquante48_72) {
        Integer result = null;
        if (JadeNumericUtil.isInteger(dureeCotManquante48_72)) {
            if (dureeCotManquante48_72.length() == 2) {
                result = convertToInteger(dureeCotManquante48_72);
            }
        }
        return result;
    }

    /**
     * Retourne le nombre de mois de cotisation manquante entre 1973 et 1978 formaté
     * Le format dans la base de calcul est MM (nombre de mois)
     * 
     * @param dureeCotManquante73_78 le nombre de mois de cotisation manquante
     * @return le nombre de mois de cotisation manquante avant 1973 formaté ou null si
     *         <code>dureeCotManquante73_78</code> est null, vide ou n'a pas le bon format (2 caractères numérique)
     */
    public Integer readDureeCotManquante73_78(String dureeCotManquante73_78) {
        Integer result = null;
        if (JadeNumericUtil.isInteger(dureeCotManquante73_78)) {
            if (dureeCotManquante73_78.length() == 2) {
                result = convertToInteger(dureeCotManquante73_78);
            }
        }
        return result;
    }

    /**
     * Montant simple entier
     * 
     * @param revenuAnnuelMoyen
     * @return
     */
    public Integer readRamDeterminant(String revenuAnnuelMoyen) {
        return convertToInteger(revenuAnnuelMoyen);
    }

    /**
     * XX (x est un numérique) représente un nombre d'années de cotisation
     * 
     * @param anneeCotiClasseAge
     * @return
     */
    public Integer readAnneeCotClasseAge(String anneeCotiClasseAge) {
        return convertToInteger(anneeCotiClasseAge);
    }

    /**
     * REBACAL.YIDANN
     * Année encodée sur 2 positions AA
     * 
     * @param anneeDeNiveau L'année de niveau
     * @return
     */
    public Integer readAnneeNiveau(String anneeDeNiveau) {
        Integer value = null;
        if (JadeNumericUtil.isInteger(anneeDeNiveau) && anneeDeNiveau.length() == 2) {
            value = Integer.valueOf(anneeDeNiveau);
            JADate today = JACalendar.today();
            int year = today.getYear();
            year = year - 2000;
            if (value <= year) {
                value = 2000 + value;
            } else {
                value = 1900 + value;
            }

        }
        return value;
    }

    /**
     * REBACAL.
     * Format en DB : AAAAMM
     * Format REBaseCalcul : MM.AAAA
     * Format attendu : MM.AAAA
     * 
     * @param survenanceEvtAssAyantDroit
     * @return
     * @throws IllegalArgumentException
     */
    public JADate readSurvenanceEvenAssure(String survenanceEvtAssAyantDroit) throws IllegalArgumentException {
        JADate result = null;
        if (survenanceEvtAssAyantDroit.contains(".") && survenanceEvtAssAyantDroit.length() == 7) {
            try {
                String[] values = survenanceEvtAssAyantDroit.split("\\.");
                if (values.length == 2) {
                    if (!JadeStringUtil.isBlankOrZero(values[0]) && !JadeStringUtil.isBlankOrZero(values[1])) {
                        result = new JADate(survenanceEvtAssAyantDroit);
                    }
                }
            } catch (JAException e) {
                throw new IllegalArgumentException(
                        "readSurvenanceEvenAssure(String survenanceEvtAssAyantDroit) : wrong date format ["
                                + survenanceEvtAssAyantDroit + "]. Expected format is MM.AAAA", e);
            }
        }
        return result;
    }

    public Integer readCodeAtteinteFonctionnelle(String codeInfirmiteEtAtteinte) {
        Integer result = null;
        if (JadeNumericUtil.isInteger(codeInfirmiteEtAtteinte)) {
            if (codeInfirmiteEtAtteinte.length() == 5) {
                result = convertToInteger(codeInfirmiteEtAtteinte.substring(3, 5));
            }
        }
        return result;
    }

    /**
     * REBACAL.YIDABA
     * Retourne la valeur entière du nombre d'année de BTA
     * Format : AAMM (AA -> nombre d'année, MM -> fraction d'année, ex : 0466 = 4 année et 2/3)
     * Retourne la valeur AA
     * 
     * @param bta le nombre d'années de bonification pout tâche éducative
     * @return la valeur entière du nombre d'année de BTA ou null si <code>bta</code> est null, vide ou ne possède pas
     *         le bon format (4 caractère numérique)
     */
    public Integer readNbreAnneeBTA_valeurEntiere(String bta) {
        Integer result = null;
        if (!JadeStringUtil.isBlank(bta)) {
            String[] valeurs = bta.split("\\.");
            if (valeurs.length == 2 && JadeNumericUtil.isNumeric(valeurs[0])) {
                result = convertToInteger(valeurs[0]);
            }
        }
        return result;
    }

    /**
     * REBACAL.YIDABA
     * Retourne la fraction d'année de BTA
     * Format : AAMM (AA -> nombre d'année, MM -> fraction d'année, ex : 0466 = 4 année et 2/3)
     * Retourne la valeur MM
     * 
     * @param bta la fraction d'années de bta
     * @return Retourne la fraction d'année de bonification pout tâche éducative ou null si <code>bta</code> est
     *         null, vide ou n'a pas le bon format (4 caractère numérique)
     */
    public Integer readNbreAnneeBTA_valeurDecimal(String bta) {
        Integer value = null;
        if (JadeNumericUtil.isInteger(bta) && bta.length() == 4) {
            value = Integer.valueOf(bta.substring(2, 4));
        }
        return value;
    }

    /**
     * REBACAL.YYNDCO
     * Format en DB : AAMM (AA est un nombre d'année, MM est un nombre de mois) ex : 0406
     * Format dans REBaseCalcul : AA.MM (AA est un nombre d'année, MM est un nombre de mois) ex : 04.06
     * Retourne la valeur AA
     * 
     * @param dureeCotPourDetRAM
     * @return
     */
    public Integer readDureeCotPourDetRAM_nombreAnnee(String dureeCotPourDetRAM) {
        Integer value = null;
        if (!JadeStringUtil.isBlank(dureeCotPourDetRAM) && dureeCotPourDetRAM.length() == 5) {
            if (dureeCotPourDetRAM.contains(".")) {
                String[] values = dureeCotPourDetRAM.split("\\.");
                if (JadeNumericUtil.isInteger(values[0])) {
                    value = Integer.valueOf(values[0]);
                }
            }
        }
        return value;
    }

    /**
     * REBACAL.YYNDCO
     * Format en DB : AAMM (AA est un nombre d'année, MM est un nombre de mois) ex: 0406
     * Format dans REBaseCalcul : AA.MM (AA est un nombre d'année, MM est un nombre de mois) ex : 04.06
     * Retourne la valeur AA
     * 
     * @param dureeCotPourDetRAM
     * @return
     */
    public Integer readDureeCotPourDetRAM_nombreMois(String dureeCotPourDetRAM) {
        Integer value = null;
        if (!JadeStringUtil.isBlank(dureeCotPourDetRAM) && dureeCotPourDetRAM.length() == 5) {
            if (dureeCotPourDetRAM.contains(".")) {
                String[] values = dureeCotPourDetRAM.split("\\.");
                if (JadeNumericUtil.isInteger(values[1])) {
                    value = Integer.valueOf(values[1]);
                }
            }
        }
        return value;
    }
}
