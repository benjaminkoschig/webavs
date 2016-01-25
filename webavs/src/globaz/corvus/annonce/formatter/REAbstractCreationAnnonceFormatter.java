package globaz.corvus.annonce.formatter;

import globaz.corvus.annonce.RENSS;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisseProvider;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRStringFormatter;
import java.math.BigDecimal;

/**
 * Formateur abstrait qui contient les m�thodes communes pour le formatage des valeurs 9�mre et 10�me r�vision
 * 
 * @author lga
 * 
 */
public abstract class REAbstractCreationAnnonceFormatter {

    public static final int MIN_YEAR_FOR_VALIDATION = 1880;

    // -----------------------------------------------------------------------
    // REANHEA

    /**
     * REANHEA.ZAACOA
     * 
     * Retourne la valeur <code>codeApplication</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>codeApplication</code> est null
     * -> Sinon retourne la valeur <code>codeApplication</code>
     * 
     * @param codeApplication Le code application
     * @return la valeur <code>codeApplication</code> format�e
     */
    public String formatCodeApplication(int codeApplication) {
        return String.valueOf(codeApplication);
    }

    /**
     * REANHEA.ZAANOA
     * 
     * Retourne la valeur <code>numeroAgence</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>numeroAgence</code> est null
     * -> Sinon indente la valeur de <code>numeroAgence</code> sur 3 positions avec des 0 � gauche
     * 
     * @param numeroAgence Le num�ro d'agence
     * @return la valeur <code>numeroAgence</code> format�e
     */
    public String formatNumeroAgence(Integer numeroAgence) {
        String result = "";
        if (numeroAgence != null) {
            result = indentLeftWithZero(numeroAgence, 3);
        }
        return result;
    }

    /**
     * REANHEA.ZAANOC
     * 
     * Retourne la valeur <code>numeroCaisse</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>v</code> est null
     * -> Sinon indente la valeur de <code>numeroCaisse</code> sur 3 positions avec des 0 � gauche
     * 
     * @param numeroCaisse Le num�ro de caisse
     * @return la valeur <code>numeroCaisse</code> format�e
     */
    public String formatNumeroCaisse(Integer numeroCaisse) {
        String result = "";
        if (numeroCaisse != null) {
            result = indentLeftWithZero(numeroCaisse, 3);
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // REAAL1A

    /**
     * REAAL1A.YXDMRA
     * Formate la date en AAAAMM
     * 
     * @param moisRapport
     * @return
     */
    public String formatMoisRapport(JADate moisRapport) {
        return formatDateToMMAA(moisRapport);
    }

    /**
     * REAAL1A.YXLCAN
     * 
     * Retourne la valeur <code>cantonEtatDomicile</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>cantonEtatDomicile</code> est null
     * -> Sinon indente la valeur de <code>cantonEtatDomicile</code> sur 3 positions avec des 0 � gauche
     * 
     * @param cantonEtatDomicile Le canton de domicile
     * @return la valeur <code>cantonEtatDomicile</code> format�e
     */
    public String formatCantonEtatDomicile(Integer cantonEtatDomicile) {
        String result = "";
        if (cantonEtatDomicile != null) {
            result = indentLeftWithZero(cantonEtatDomicile, 3);
        }
        return result;
    }

    /**
     * REAAL1A.YXLCOM
     * 
     * Retourne la valeur <code>codeMutation</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>codeMutation</code> est null
     * -> Sinon indente la valeur de <code>codeMutation</code> sur 2 positions avec des 0 � gauche
     * 
     * @param codeMutation le code de mutation
     * @return la valeur <code>codeMutation</code> format�e
     */
    public String formatCodeMutation(Integer codeMutation) {
        String result = "";
        if (codeMutation != null) {
            result = indentLeftWithZero(codeMutation, 2);
        }
        return result;
    }

    /**
     * REAAL1A.YXDDEB
     * 
     * Retourne la valeur <code>dateDebutDroit</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>dateDebutDroit</code> est null
     * -> Sinon retourne la valeur de <code>dateDebutDroit</code> au format MMAA
     * 
     * @param dateDebutDroit La date de d�but du droit
     * @return la valeur <code>dateDebutDroit</code> format�e
     */
    public String formatDebutDroit(JADate dateDebutDroit) {
        return formatDateToMMAA(dateDebutDroit);
    }

    /**
     * REAAL1A.YXLETC
     * 
     * Retourne la valeur <code>etatCivil</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>etatCivil</code> est null
     * -> Sinon retourne la valeur de <code>etatCivil</code> (1 position, non contr�l�)
     * 
     * @param etatCivil Le code d'�tat civil
     * @return la valeur <code>etatCivil</code> format�e
     */
    public String formatEtatCivil(Integer etatCivil) {
        String result = "";
        if (etatCivil != null) {
            result = String.valueOf(etatCivil);
        }
        return result;
    }

    /**
     * REAAL1A.YXDFIN
     * 
     * Retourne la valeur <code>dateFinDroit</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>dateFinDroit</code> est null
     * -> Sinon retourne la valeur de <code>dateFinDroit</code> au format MMAA
     * 
     * @param dateFinDroit La date de fin de droit
     * @return la valeur <code>dateFinDroit</code> format�e
     */
    public String formatFinDroit(JADate dateFinDroit) {
        return formatDateToMMAA(dateFinDroit);
    }

    /**
     * REAAL1A.YXLGEN
     * 
     * Retourne la valeur <code>genrePrestation</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>genrePrestation</code> est null
     * -> Sinon retourne la valeur de <code>genrePrestation</code> sans formatage
     * 
     * @param genrePrestation Le code du genre de prestation
     * @return la valeur <code>genrePrestation</code> format�e
     */
    public String formatGenrePrestation(Integer genrePrestation) {
        String result = "";
        if (genrePrestation != null) {
            result = String.valueOf(genrePrestation);
        }
        return result;
    }

    /**
     * REAAL1A.??????
     * 
     * /**
     * Retourne la valeur <code>idRenteAccordee</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>idRenteAccordee</code> est null
     * -> Sinon retourne la valeur de <code>idRenteAccordee</code> sans formatage
     * 
     * @param idRenteAccordee L'id tiers � formater
     * @return la valeur <code>idRenteAccordee</code> format�e
     */
    public String formatIdRenteAccordee(Long idRenteAccordee) {
        return formatId(idRenteAccordee);
    }

    /**
     * REAAL1A.YXITIE
     * 
     * /**
     * Retourne la valeur <code>idTiers</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>idTiers</code> est null
     * -> Sinon retourne la valeur de <code>idTiers</code> sans formatage
     * 
     * @param idTiers L'id tiers � formater
     * @return la valeur <code>idTiers</code> format�e
     */
    public String formatIdTiers(Long idTiers) {
        return formatId(idTiers);
    }

    /**
     * REAAL1A.YXNNOA
     * 
     * /**
     * Retourne la valeur <code>numeroAnnonce</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>numeroAnnonce</code> est null
     * -> Sinon retourne la valeur de <code>numeroAnnonce</code> sans formatage
     * 
     * @param numeroAnnonce L'id tiers � formater
     * @return la valeur <code>numeroAnnonce</code> format�e
     */
    public String formatNumeroAnnonce(Long numeroAnnonce) {
        return formatId(numeroAnnonce);
    }

    /**
     * REAAL1A.YXBREF
     * 
     * Retourne la valeur <code>refugie</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>refugie</code> est null
     * -> -> Sinon retourne '1' si <code>refugie</code> = true et '0' si false
     * 
     * @param refugie Si c'est un r�fugi�
     * @return la valeur <code>refugie</code> format�e
     */
    public String formatIsRefugie(Boolean refugie) {
        return formatBoolean(refugie);
    }

    /**
     * REAAL1A.YXMMEN
     * 
     * Retourne la valeur <code>mensualitePrestationsFrancs</code> format�e
     * 
     * -> <strong>Retourne '00000' (5 z�ros) si <code>mensualitePrestationsFrancs</code> est null ou vide</strong>
     * -> Sinon indente la valeur sur 5 positions avec des 0 � gauche
     * 
     * @param mensualitePrestationsFrancs La valeur des mensualit�s en SFr.
     * @return la valeur <code>mensualitePrestationsFrancs</code> format�e
     */
    public String formatMensualitePrestationsFrancs(Integer mensualitePrestationsFrancs) {
        String result = "00000";
        if (mensualitePrestationsFrancs != null) {
            result = indentLeftWithZero(mensualitePrestationsFrancs, 5);
        }
        return result;
    }

    /**
     * REAAL1A.YXNASS
     * 
     * Retourne la valeur <code>prefixReferenceInterne, noAssAyantDroit</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>noAssAyantDroit</code> est null
     * -> Sinon retourne le NSS format� sans les point (13 positions)
     * 
     * @param noAssAyantDroit Le num�ro NSS de l'ayant droit
     * @return la valeur <code>prefixReferenceInterne, noAssAyantDroit</code> format�e
     */
    public String formatNoAssAyantDroit(RENSS noAssAyantDroit) {
        return formatNSS(noAssAyantDroit);
    }

    /**
     * REAAL1A.YXLREI
     * 
     * Retourne la valeur <code>prefixReferenceInterne, userPourReferenceCaisseInterne</code> format�e en majuscule. La
     * longueur max est 20 caract�res
     * 
     * -> <strong>Retourne une cha�ne vide si <code>prefixReferenceInterne</code> OU
     * <code>userPourReferenceCaisseInterne</code> sont null ou vide</strong>
     * -> Sinon concat�ne le prefix et le nom de l'utilisateur et met le r�sultat en majuscule
     * 
     * @param prefixReferenceInterne Le prefix � utiliser pour la r�f�rence caisse interne
     * @param userPourReferenceCaisseInterne Le nom de l'utilisateur � utiliser
     * @return la valeur <code>prefixReferenceInterne, userPourReferenceCaisseInterne</code> format�e en majuscule. La
     *         longueur max est 20 caract�res
     */
    public String formatReferenceCaisseInterne(REPrefixPourReferenceInterneCaisseProvider prefixReferenceInterne,
            String userPourReferenceCaisseInterne) {
        String result = "";
        if (prefixReferenceInterne != null && !JadeStringUtil.isEmpty(userPourReferenceCaisseInterne)) {
            result = prefixReferenceInterne.getPrefixPourReferenceInterneCaisse();
            result += userPourReferenceCaisseInterne;
            result = result.toUpperCase();
        }
        if (result.length() > 20) {
            result = result.substring(0, 20);
        }
        return result;
    }

    /**
     * REAAL1A.YXNPNA (NSS)
     * 
     * Retourne la valeur <code>premierNoAssComplementaire</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>premierNoAssComplementaire</code> est null
     * -> Sinon retourne le NSS format� sans les point (13 positions)
     * 
     * @param secondNoAssComplementaire Le 1er num�ro NSS compl�mentaire
     * @return la valeur <code>premierNoAssComplementaire</code> format�e
     */
    public String formatPremierNoAssComplementaire(RENSS premierNoAssComplementaire) {
        return formatNSS(premierNoAssComplementaire);
    }

    /**
     * REAAL1A.YXNDNA (NSS)
     * 
     * Retourne la valeur <code>secondNoAssComplementaire</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>secondNoAssComplementaire</code> est null
     * -> Sinon retourne le NSS format� sans les point (13 positions)
     * 
     * @param secondNoAssComplementaire Le 2�me num�ro NSS compl�mentaire
     * @return la valeur <code>secondNoAssComplementaire</code> format�e
     */
    public String formatSecondNoAssComplementaire(RENSS secondNoAssComplementaire) {
        return formatNSS(secondNoAssComplementaire);
    }

    // -------------------------------------------------------------------------
    // REAAL2A

    /**
     * REAAL2A.YYNAGE
     * 
     * Retourne la valeur <code>ageDebutInvalidite</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>ageDebutInvalidite</code> est null
     * -> Sinon retourne '1' si <code>ageDebutInvalidite</code> = true et '0' si false
     * 
     * @param ageDebutInvalidite Si invalide avant 25 ans 1 sinon
     * @return la valeur <code>ageDebutInvalidite</code> format�e
     */
    public String formatAgeDebutInvalidite(Boolean ageDebutInvalidite) {
        return formatBoolean(ageDebutInvalidite);
    }

    /**
     * REAAL2A.YYDACC
     * 
     * Retourne la valeur <code>anneeCotClasseAge</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>anneeCotClasseAge</code> est null
     * -> Sinon indent la valeur sur 2 positions avec des 0 � gauche
     * 
     * @param anneeCotClasseAge l'ann�e de cotisation
     * @return la valeur <code>anneeCotClasseAge</code> format�e
     */
    public String formatAnneeCotClasseAge(Integer anneeCotClasseAge) {
        String result = "";
        if (anneeCotClasseAge != null) {
            result = indentLeftWithZero(anneeCotClasseAge, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYDANI
     * 
     * Retourne la valeur <code>anneeNiveau</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>anneeNiveau</code> est null
     * -> Sinon retourne la date au format AA (ex : 2012 -> 12, 2000 -> 00)
     * 
     * @param anneeNiveau L'ann�e de niveau
     * @return la valeur <code>anneeNiveau</code> format�e
     */
    public String formatAnneeNiveau(Integer anneeNiveau) {
        String result = "";
        if (anneeNiveau != null) {
            validateYearValue(anneeNiveau);
            int value = anneeNiveau % 100;
            result = String.valueOf(value);
            result = indentLeftWithZero(result, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYLCS? [? va de 1 � 5]
     * 
     * Retourne la valeur <code>codeCasSpecial</code> format�e
     * -> Retourne une cha�ne vide si <code>codeCasSpecial</code> est null
     * -> Sinon indente la valeur sur 2 positions avec des 0 � gauche
     * LEs valeurs possibles sont de 1 � 99
     * 
     * @param codeCasSpecial le code cas sp�cial � formater
     * @return la valeur <code>codeCasSpecial</code> format�e
     */
    public String formatCodeCasSpecial(Integer codeCasSpecial) {
        String result = "";
        if (codeCasSpecial != null && codeCasSpecial > 0 && codeCasSpecial < 100) {
            result = indentLeftWithZero(codeCasSpecial, 2);
            if (result.length() > 2) {
                result = result.substring(0, 2);
            }
        }
        return result;
    }

    /**
     * REAAL2A.YYNCOI
     * 
     * Retourne la valeur <code>codeInfirmite, codeAtteinteFonctionnel</code> format�e
     * Le code infirmit� enregistr� en DB est l'agregation du 'codeInfirmit�' et du 'codeAtteinteFonctionnel'
     * 
     * -><strong> Retourne une cha�ne vide si <code>codeInfirmite</code> OU <code>codeAtteinteFonctionnel</code> son
     * null </strong>
     * -> Sinon :
     * --> indente la valeur <code>codeInfirmite</code> sur 3 positions avec des 0 � gauche
     * --> indente la valeur <code>codeAtteinteFonctionnel</code> sur 2 positions avec des 0 � gauche
     * --> concat�ne les deux r�sultat codeInfirmit�Indent� + codeAtteinteIndent�
     * 
     * @param codeInfirmite Le code infirmit�
     * @param codeAtteinteFonctionnel Le code atteinte fonctionnel
     * @return la valeur <code>codeInfirmite, codeAtteinteFonctionnel</code> format�e
     */
    public String formatCodeInfirmite(Integer codeInfirmite, Integer codeAtteinteFonctionnel) {
        String result = "";
        if (codeInfirmite != null && codeAtteinteFonctionnel != null) {
            String codeInf = indentLeftWithZero(codeInfirmite, 3);
            String codeAtteinte = indentLeftWithZero(codeAtteinteFonctionnel, 2);
            result = codeInf + codeAtteinte;
        }
        return result;
    }

    /**
     * REAAL2A.YYDREV
     * 
     * Retourne la valeur <code>dateRevocationAjournement</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>dateRevocationAjournement</code> est null
     * -> Sinon formate la date au format MMAA (ex : 2014.06 -> 1406)
     * 
     * @param dateRevocationAjournement la date de r�vocation de l'ajournement
     * @return la valeur <code>dateRevocationAjournement</code> format�e
     */
    public String formatDateRevocationAjournement(JADate dateRevocationAjournement) {
        return formatDateToMMAA(dateRevocationAjournement);
    }

    /**
     * REAAL2A.YYNDIN (Integer)
     * 
     * Retourne la valeur <code>degreInvalidite</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>degreInvalidite</code> est null
     * -> Sinon indente la valeur sur 3 positions avec des 0 � gauche
     * 
     * @param degreInvalidite Le degr� d'invalidit�
     * @return la valeur <code>degreInvalidite</code> format�e
     */
    public String formatDegreInvalidite(Integer degreInvalidite) {
        String result = "";
        if (degreInvalidite != null) {
            result = indentLeftWithZero(degreInvalidite, 3);
        }
        return result;
    }

    /**
     * REAAL2A.YYNDUR (Integer, Integer)
     * 
     * Retourne la valeur <code>dureeAjournement</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>dureeCoEchelleRenteAv73</code> est null
     * -> <strong> Retourne une cha�ne vide si <code>dureeAjournement</code> OU <code>dureeAjournementDecimal</code> son
     * null </strong>
     * -> Sinon : valeur a.b -> valeur enti�re de a + (si b < 10 = 0+b sinon b)
     * Exemple : 1.10 -> 110, 1.1 -> 101, 2.0 -> 200
     * 
     * 
     * @param dureeAjournementEntier La valeur enti�re de la dur�e de l'ajournement
     * @param dureeAjournementDecimal La valeur d�cimal de la dur�e de l'ajournement
     * @return la valeur <code>dureeAjournement</code> format�e
     */
    public String formatDureeAjournement(Integer dureeAjournementEntier, Integer dureeAjournementDecimal) {
        String result = "";
        if (dureeAjournementEntier != null && dureeAjournementDecimal != null) {
            result += String.valueOf(dureeAjournementEntier);
            if (dureeAjournementDecimal < 10) {
                result += "0";
            }
            result += String.valueOf(dureeAjournementDecimal);
        }
        return result;
    }

    /**
     * REAAL2A.YYDCEC (Integer)
     * 
     * Retourne la valeur <code>dureeCoEchelleRenteAv73</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>dureeCoEchelleRenteAv73</code> est null
     * -> Sinon indente la valeur sur 4 positions (AAMM) avec des 0
     * 
     * @param nombreAnnee le nombre d'ann�e de cotisation avant 1973
     * @param nombreMois le nombre de mois de cotisation avant 1973
     * @return la valeur <code>dureeCoEchelleRenteAv73</code> format�e
     */
    public String formatDureeCoEchelleRenteAv73(Integer nombreAnnee, Integer nombreMois) {
        String result = "";
        if (nombreAnnee != null && nombreMois != null) {
            result = indentLeftWithZero(nombreAnnee, 2);
            result += indentLeftWithZero(nombreMois, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYDECH (Integer)
     * 
     * Retourne la valeur <code>dureeCoEchelleRenteDes73</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>dureeCoEchelleRenteDes73</code> est null
     * -> Sinon indente la valeur sur 4 positions (AAMM) avec des 0
     * 
     * @param nombreAnnee le nombre d'ann�e de cotisation � partir de 1973
     * @param nombreMois le nombre de mois de cotisation � partir de 1973
     * @return la valeur <code>dureeCoEchelleRenteDes73</code> format�e
     */
    public String formatDureeCoEchelleRenteDes73(Integer nombreAnnee, Integer nombreMois) {
        String result = "";
        if (nombreAnnee != null && nombreMois != null) {
            result = indentLeftWithZero(nombreAnnee, 2);
            result += indentLeftWithZero(nombreMois, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYDCM1 (Integer)
     * 
     * Retourne la valeur <code>dureeCotManquante48_72</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>dureeCotManquante48_72</code> est null
     * -> Sinon indente la valeur sur 2 positions avec des 0 � gauche
     * 
     * @param dureeCotManquante48_72 la dur�e de cotisation manquante 48_72
     * @return la valeur <code>dureeCotManquante48_72</code> format�e
     */
    public String formatDureeCotManquante48_72(Integer dureeCotManquante48_72) {
        String result = "";
        if (dureeCotManquante48_72 != null) {
            result = indentLeftWithZero(dureeCotManquante48_72, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYDCM2 (Integer)
     * 
     * Retourne la valeur <code>dureeCotManquante73_78</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>dureeCotManquante73_78</code> est null
     * -> Sinon indente la valeur sur 2 positions avec des 0 � gauche
     * 
     * @param dureeCotManquante73_78 la dur�e de cotisation manquante 73_78
     * @return la valeur <code>dureeCotManquante73_78</code> format�e
     */
    public String formatDureeCotManquante73_78(Integer dureeCotManquante73_78) {
        String result = "";
        if (dureeCotManquante73_78 != null) {
            result = indentLeftWithZero(dureeCotManquante73_78, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYNDCO (Integer)
     * 
     * Retourne la valeur <code>dureeCotPourDetRAM</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>dureeCotPourDetRAM</code> est null
     * -> Sinon indente la valeur sur 4 positions avec des 0
     * 
     * @param dureeCotPourDetRAM La dur�e de cotisation pour le RAM (revenu annuel moyen)
     * @return la valeur <code>dureeCotPourDetRAM</code> format�e
     */
    public String formatDureeCotPourDetRAM(Integer nombreAnnee, Integer nombreMois) {
        String result = "";
        if (nombreAnnee != null && nombreMois != null) {
            result = indentLeftWithZero(nombreAnnee, 2);
            result += indentLeftWithZero(nombreMois, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYLECR (_Integer)
     * 
     * Retourne la valeur <code>echelleRente</code> format�e
     * 
     * ->Retourne une cha�ne vide si est null
     * -> Sinon indente la valeur sur 2 positions avec des 0
     * 
     * @param echelleRente La valeur de l'�chelle de rente
     * @return la valeur <code>echelleRente</code> format�e
     */
    public String formatEchelleRente(Integer echelleRente) {
        String result = "";
        if (echelleRente != null) {
            result = indentLeftWithZero(echelleRente, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYTGEN
     * 
     * Retourne la valeur <code>genreDroitApi</code> format�e
     * 
     * ->Retourne une cha�ne vide si <code>genreDroitApi</code> est null
     * -> Sinon retourne la valeur de <code>genreDroitApi</code>.
     * 
     * @param genreDroitApi La valeur du genre de droit API
     * @return la valeur <code>genreDroitApi</code> format�e
     */
    public String formatGenreDroitAPI(Integer genreDroitApi) {
        String result = "";
        if (genreDroitApi != null) {
            result = String.valueOf(genreDroitApi);
        }
        return result;
    }

    /**
     * REAAL2A.YYNANN (Integer, Boolean)
     * 
     * Retourne la valeur <code>nombreAnneeBTE</code> format�e
     * 
     * ->Retourne une cha�ne vide si <code>nombreAnneeBTE</code> est null
     * -> Sinon multiplie la valeur par 100 et l'indent sur 4 position avec des 0 � gauche.
     * 
     * @param nombreAnneeBTE Le nombre d'ann�e de bonification pour t�che �ducative
     * @param fractionAnneeBTE la faction d'ann�e de BTE
     * @return la valeur <code>nombreAnneeBTE</code> format�e
     */
    public String formatNombreAnneeBTE(Integer nombreAnnee, Integer fractionAnneeBTE) {
        String result = "";
        String annee = "00";
        String mois = "00";
        boolean valid = false;
        if ((nombreAnnee != null && nombreAnnee > 0)) {
            valid = true;
            annee = indentLeftWithZero(nombreAnnee, 2);
        }
        if (fractionAnneeBTE != null && fractionAnneeBTE > 0) {
            valid = true;
            mois = indentLeftWithZero(fractionAnneeBTE, 2);
        }
        if (valid) {
            result = annee + mois;
        }
        return result;
    }

    /**
     * REAAL2A.YYLOAI (Integer)
     * 
     * Retourne la valeur <code>officeAICompetent</code> format�e sur 2 position
     * 
     * -> Retourne une cha�ne vide si <code>officeAICompetent</code> est null
     * -> Sinon retourne la valeur brut sans formatage
     * 
     * @param officeAICompetent Le num�ro de l'office AI comp�tent
     * @return la valeur <code>officeAICompetent</code> format�e
     */
    // TODO est-ce qu'il faut indenter avec des 0 � gauche ?
    public String formatOfficeAICompetent(Integer officeAICompetent) {
        String result = "";
        if (officeAICompetent != null) {
            result = String.valueOf(officeAICompetent);
        }
        return result;
    }

    /**
     * REAAL2A.YYMRAM (Integer)
     * 
     * Retourne la valeur <code>ramDeterminant</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>ramDeterminant</code> est null
     * -> Sinon retourne la valeur sur 8 positions indent�e avec des 0 � gauche
     * 
     * @param ramDeterminant Le revenu annuel moyen d�terminant
     * @return la valeur <code>ramDeterminant</code> format�e
     */
    public String formatRamDeterminant(Integer ramDeterminant) {
        String result = "";
        if (ramDeterminant != null) {
            result = indentLeftWithZero(ramDeterminant, 8);
        }
        return result;
    }

    /**
     * REAAL2A.YYLRED (Integer)
     * 
     * Retourne la valeur <code>reduction</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>reduction</code> est null
     * -> Sinon retourne la valeur format�e sur 2 positions avec des 0 � gauche
     * 
     * @param reduction Le montant de la r�duction
     * @return la valeur <code>reduction</code> format�e
     */
    public String formatReduction(Integer reduction) {
        String result = "";
        if (reduction != null) {
            result = indentLeftWithZero(reduction, 2);
        }
        return result;
    }

    /**
     * REAAL2A.YYNSUP (Integer)
     * 
     * Retourne la valeur <code>supplementAjournement</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>supplementAjournement</code> est null
     * -> Sinon retourne la valeur sur 5 position indent�e avec des 0 � gauche
     * 
     * @param supplementAjournement La valeur du suppl�ment d'ajournement
     * @return la valeur <code>supplementAjournement</code> format�e
     */
    public String formatSupplementAjournement(Integer supplementAjournement) {
        String result = "";
        if (supplementAjournement != null && supplementAjournement > 0) {
            result = indentLeftWithZero(supplementAjournement, 5);
        }
        return result;
    }

    /**
     * REAAL2A.YYNSUR (Date)
     * 
     * Retourne la valeur <code>survenanceEvenAssure</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>survenanceEvenAssure</code> est null
     * -> Sinon retourne la date au format MMAA
     * 
     * @param survenanceEvenAssure La date de survenance de l'�v�nement assur�
     * @return la valeur <code>survenanceEvenAssure</code> format�e
     */
    public String formatSurvenanceEvenAssure(JADate survenanceEvenAssure) throws IllegalArgumentException {
        return formatDateToMMAA(survenanceEvenAssure);
    }

    // ----------------------------------------------------------------------------------
    // PRIVATE METHODS

    /**
     * Retourne la valeur <code>id</code> format�e
     * 
     * -> Retourne un cha�ne vide si <code>id</code> est null
     * -> Sinon retourne la valeur de l'id sans formatage
     * 
     * @param id L'id � formater
     * @return la valeur <code>id</code> format�e
     */
    protected String formatId(Long id) {
        String result = "";
        if (id != null) {
            result = String.valueOf(id);
        }
        return result;
    }

    /**
     * Retourne la valeur <code>nss</code> format�e
     * 
     * -> Retourne une cha�ne vide si <code>secondNoAssComplementaire</code> est null
     * -> Sinon retourne le NSS format� sans les point (13 positions)
     * 
     * @param nss Le num�ro nss � formater
     * @return la valeur <code>nss</code> format�e
     */
    protected String formatNSS(RENSS nss) {
        String result = "";
        if (nss != null) {
            result = nss.getUnformatedNSS();
        }
        return result;
    }

    /**
     * Retourne la valeur <code>date</code> format�e
     * -> Retourne une cha�ne vide si <code>date</code> est null
     * -> Sinon formate la date au format MMAA (ex 2012.09 -> 0912)
     * 
     * @param date La date � formater
     * @return la date au format MMAA ou une cha�ne vide si <code>date</code> est null
     */
    protected String formatDateToMMAA(JADate date) throws IllegalArgumentException {
        String result = "";
        if (date != null) {
            int mois = date.getMonth();
            int annee = date.getYear();

            validateMonthValue(mois);
            validateYearValue(annee);

            StringBuilder sb = new StringBuilder();
            sb.append(indentLeftWithZero(mois, 2));
            sb.append(String.valueOf(annee).substring(2, 4));
            result = sb.toString();
        }
        return result;
    }

    /**
     * Retourne la valeur <code>date</code> format�e
     * -> Retourne une cha�ne vide si <code>date</code> est null
     * -> Sinon formate la date au format AAAAMM (ex 2012.09 -> 201209)
     * 
     * @param date La date � formater
     * @return la date au format AAAAMM ou une cha�ne vide si <code>date</code> est null
     */
    protected String formatDateToAAAAMM(JADate date) throws IllegalArgumentException {
        String result = "";
        if (date != null) {
            int mois = date.getMonth();
            int annee = date.getYear();

            validateMonthValue(mois);
            validateYearValue(annee);

            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(annee));
            sb.append(indentLeftWithZero(mois, 2));
            result = sb.toString();
        }
        return result;
    }

    /**
     * @param annee
     */
    protected void validateYearValue(int annee) throws IllegalArgumentException {
        if (annee < MIN_YEAR_FOR_VALIDATION) {
            throw new IllegalArgumentException("A year can not be smaller as 1960");
        }
    }

    /**
     * @param mois
     */
    protected void validateMonthValue(int mois) throws IllegalArgumentException {
        if (mois <= 0) {
            throw new IllegalArgumentException("A month can not be smaller as 1");
        }
        if (mois > 12) {
            throw new IllegalArgumentException("A month can not be biger as 12");
        }
    }

    /**
     * Format un bool�en selon les r�gles suivantes :
     * -> value == null -> cha�ne vide
     * -> value == true -> 1
     * -> value == false -> 0
     * 
     * @param value Le bool�en � formater
     * @return un bool�en format� [true="0", false="1"] ou une cha�ne vide si <code>value</code> est null
     */
    protected String formatBoolean(Boolean value) {
        String result = "";
        if (value != null) {
            if (value) {
                result = "1";
            } else {
                result = "0";
            }
        }
        return result;
    }

    /**
     * Format une cha�ne de caract�re en lui ajoutant des z�ro � gauche</br> Example : value = '218', indentValue = '5'
     * ==> 00218
     * 
     * @param value
     *            La cha�ne de caract�re � formater
     * @param indentValue
     *            Le nombre de caract�re de la cha�ne final
     * @return Une cha�ne de caract�re indent�e avec des z�ros � gauche
     */
    protected String indentLeftWithZero(String value, int indentValue) {
        return PRStringFormatter.indentLeft(value, indentValue, "0");
    }

    /**
     * Format une cha�ne de caract�re en lui ajoutant des z�ro � gauche</br> Example : value = '218', indentValue = '5'
     * ==> 00218
     * 
     * @param value
     *            La cha�ne de caract�re � formater
     * @param indentValue
     *            Le nombre de caract�re de la cha�ne final
     * @return Une cha�ne de caract�re indent�e avec des z�ros � gauche
     */
    protected String indentLeftWithZero(Integer value, int indentValue) {
        return PRStringFormatter.indentLeft(String.valueOf(value), indentValue, "0");
    }

    /**
     * une nouvelle instance de BigDecimal avec la valeur 100
     * 
     * @return une nouvelle instance de BigDecimal avec la valeur 100
     */
    protected final BigDecimal getMultiplicateurPar100() {
        return new BigDecimal(100);
    }

}
