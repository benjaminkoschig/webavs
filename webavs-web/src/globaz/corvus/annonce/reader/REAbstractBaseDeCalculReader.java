package globaz.corvus.annonce.reader;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Contient des m�thodes de lecture des champs de la table REBACAL 'commun' � la 9�me et 10�me r�vision
 * 
 * @author lga
 * 
 */
public class REAbstractBaseDeCalculReader extends REAbstractBEntityValueReader {

    /**
     * REBACAL.YINDIN</br>
     * Lit le d�gr�e d'invalidit� et le convertis en nombre entier</br>
     * Format : nnn, c'est un % (n est un nombre, ex : 45, 100, 0)</br>
     * </br>
     * 
     * @param degreInvalidite Le degr� d'invalidit� contenu dans la base de calcul
     * @return d�gr�e d'invalidit� convertis en nombre entier ou null si le champs est vide ou n'a pas le bon format (2
     *         caract�res num�rique)
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
     * Lit le nombre d'ann�e de cotisation avant 1973 et le convertis en nombre entier
     * Format en DB : AAMM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 0406 = 4 ann�e et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 04.06 = 4 ann�e et 6 mois)
     * 
     * @param dureeCotiAvant73 la dur�e de cotisation avant 1973
     * @return nombre d'ann�e de cotisation avant 1973 OU null si null, vide ou n'a pas le bon format (4
     *         caract�res num�rique)
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
     * Format en DB : AAMM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 0406 = 4 ann�e et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 04.06 = 4 ann�e et 6 mois)
     * 
     * @param dureeCotiAvant73 la dur�e de cotisation avant 1973
     * @return nombre de mois de cotisation avant 1973 OU null si null, vide ou n'a pas le bon format (2
     *         caract�re num�rique)
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
     * Lit le nombre d'ann�e de cotisation d�s 1973 et le convertis en nombre entier
     * Format en DB : AAMM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 0406 = 4 ann�e et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 04.06 = 4 ann�e et 6 mois)
     * 
     * @param dureeCotiDes73 la dur�e de cotisation d�s 1973
     * @return nombre d'ann�e de cotisation d�s 1973 OU null si null, vide ou n'a pas le bon format (4
     *         caract�res num�rique)
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
     * Lit le nombre de mois de cotisation d�s 1973 et le convertis en nombre entier
     * Format en DB : AAMM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 0406 = 4 ann�e et 6 mois)
     * Format dans REBaseCalcul : AA.MM (AA -> nombre d'ann�e, MM -> nombre de mois, ex : 04.06 = 4 ann�e et 6 mois)
     * 
     * @param dureeCotiDes73 la dur�e de cotisation d�s 1973
     * @return nombre de mois de cotisation d�s 1973 OU null si null, cha�ne vide ou n'a pas le bon format (2
     *         caract�re num�rique)
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
     * Retourne le nombre d'ann�es de bonification transitoire et le convertis en nombre entier
     * Format DB : AM (A -> nombre d'ann�e (max 8), M -> nombre de mois (0 ou 5), ex : 0405 = 4 ann�e et demie)
     * Format BEntity : A.M
     * 
     * 
     * @param anneeBonifTransitoire le nombre d'ann�es de bonification transitoire
     * @return le nombre d'ann�es de bonification transitoire OU null si null, vide ou n'a pas le bon format (2
     *         caract�res num�rique)
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
     * Retourne le nombre d'ann�es de bonification transitoire
     * Format DB : AM (A -> nombre d'ann�e (max 8), M -> nombre de mois (0 ou 5), ex : 0405 = 4 ann�e et demie)
     * Format BEntity : A.M
     * 
     * @param anneeBonifTransitoire le nombre d'ann�es de bonification transitoire
     * @return le nombre d'ann�es de bonification transitoire OU null si null, vide ou n'a pas
     *         le bon format (2 caract�res num�rique)
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
     * Retourne le code infirmit� uniquement</br>
     * Dans la base de calcul, le champs codeInfirmit� est agr�g� avec le codeAtteinte comme suit :</br>
     * code infirmit� : 3 positions -> xxx</br>
     * code atteinte : 2 positions -> aa</br>
     * agr�g� comme suit : xxxaa</br>
     * 
     * Cette m�thode retourne la valeur xxx</br>
     * 
     * @param codeInfirmiteEtAtteinte Le code infirmit�EtAtteinte de la base de calcul
     * @return le code infirmit� ou null si <code>codeInfirmiteEtAtteinte</code> est null, vide ou n'as pas le bon
     *         format (5 caract�res num�rique)
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
     * Retourne le nombre de mois de cotisation manquante avant 1973 format�
     * Le format dans la base de calcul est MM (nombre de mois)
     * 
     * @param dureeCotManquante48_72 le nombre de mois de cotisation manquante
     * @return le nombre de mois de cotisation manquante avant 1973 format� ou null si
     *         <code>dureeCotManquante48_72</code> est null, vide ou n'a pas le bon format (2 caract�res num�rique)
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
     * Retourne le nombre de mois de cotisation manquante entre 1973 et 1978 format�
     * Le format dans la base de calcul est MM (nombre de mois)
     * 
     * @param dureeCotManquante73_78 le nombre de mois de cotisation manquante
     * @return le nombre de mois de cotisation manquante avant 1973 format� ou null si
     *         <code>dureeCotManquante73_78</code> est null, vide ou n'a pas le bon format (2 caract�res num�rique)
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
     * XX (x est un num�rique) repr�sente un nombre d'ann�es de cotisation
     * 
     * @param anneeCotiClasseAge
     * @return
     */
    public Integer readAnneeCotClasseAge(String anneeCotiClasseAge) {
        return convertToInteger(anneeCotiClasseAge);
    }

    /**
     * REBACAL.YIDANN
     * Ann�e encod�e sur 2 positions AA
     * 
     * @param anneeDeNiveau L'ann�e de niveau
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
     * Retourne la valeur enti�re du nombre d'ann�e de BTA
     * Format : AAMM (AA -> nombre d'ann�e, MM -> fraction d'ann�e, ex : 0466 = 4 ann�e et 2/3)
     * Retourne la valeur AA
     * 
     * @param bta le nombre d'ann�es de bonification pout t�che �ducative
     * @return la valeur enti�re du nombre d'ann�e de BTA ou null si <code>bta</code> est null, vide ou ne poss�de pas
     *         le bon format (4 caract�re num�rique)
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
     * Retourne la fraction d'ann�e de BTA
     * Format : AAMM (AA -> nombre d'ann�e, MM -> fraction d'ann�e, ex : 0466 = 4 ann�e et 2/3)
     * Retourne la valeur MM
     * 
     * @param bta la fraction d'ann�es de bta
     * @return Retourne la fraction d'ann�e de bonification pout t�che �ducative ou null si <code>bta</code> est
     *         null, vide ou n'a pas le bon format (4 caract�re num�rique)
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
     * Format en DB : AAMM (AA est un nombre d'ann�e, MM est un nombre de mois) ex : 0406
     * Format dans REBaseCalcul : AA.MM (AA est un nombre d'ann�e, MM est un nombre de mois) ex : 04.06
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
     * Format en DB : AAMM (AA est un nombre d'ann�e, MM est un nombre de mois) ex: 0406
     * Format dans REBaseCalcul : AA.MM (AA est un nombre d'ann�e, MM est un nombre de mois) ex : 04.06
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
