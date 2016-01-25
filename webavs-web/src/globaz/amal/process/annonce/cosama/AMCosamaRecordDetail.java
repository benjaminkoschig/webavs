/**
 * 
 */
package globaz.amal.process.annonce.cosama;

import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dhi
 * 
 */
public class AMCosamaRecordDetail extends AMCosamaRecord {

    private String ancienNoAssure = "";
    private String baseReductionPourcent = "";
    private String beneficiaireAssiste = "";
    private String beneficiairePC = "";
    private String caisseInterne = "";
    private String classeRevenu = "";
    private String codeAssuresCollectifs = "";
    private String codeChefFamille = "";
    private String codeExtinctionDroit = "";
    private String codePayeurPrimes = "";
    private String codePostal = "";
    private String commune = "";
    private String complementAdresse = "";
    private String couvertureAccident = "";
    private String dateDebutSubside = "";
    private String dateDecision = "";
    private String dateDemandeRevision = "";
    private String dateExtinctionDroit = "";
    private String dateFinSubside = "";
    private String dateNaissance = "";
    private String dateSuspension = "";
    private String debutValiditeAffiliation = "";
    private String debutValiditePrime = "";
    private String echelonBonus = "";
    private String etatCivil = "";
    private String finValiditeAffiliation = "";
    private String groupeAge = "";
    private String hmoReseauBonus = "";
    private String localite = "";
    private String modePaiementPrimes = "";
    private String montantDecompte = "";
    private String montantEffectifSubside = "";
    private String montantFranchise = "";
    private String montantMaximumReduction = "";
    private String montantPartPatronale = "";
    private String montantPartPatronalePourcent = "";
    private String montantPrime = "";
    private String noAncienneCM = "";
    private String noArticle = "";
    private String noAssure = "";
    private String noAVS = "";
    private String noFamille = "";
    private String nomOfficielRue = "";
    private String nomPrenomUsuel = "";
    private String nomUsuel = "";
    private String noNouvelleCM = "";
    private String noPersonnelCantonal = "";
    private String numeroMaison = "";
    private String partMensuelleChargeAffilie = "";
    private String pourcentReduction = "";
    private String prenomUsuel = "";
    private String primesArrierees = "";
    private String revenuDeterminant = "";
    private String sectionCM = "";
    private String sexe = "";
    private String typeDecision = "";
    private String typeReductionPrime = "";
    private String zoneReservee = "";
    private String zoneTarifaire = "";

    /**
     * Default constructor (empêche la création)
     */
    private AMCosamaRecordDetail() {

    }

    /**
     * @param _typeEnregistrement
     */
    protected AMCosamaRecordDetail(String _typeEnregistrement) {
        setTypeEnregistrement(_typeEnregistrement);
    }

    @Override
    public int compareTo(AMCosamaRecord o) {
        if (o instanceof AMCosamaRecordDetail) {
            if (getNomPrenomUsuel().compareTo(((AMCosamaRecordDetail) o).getNomPrenomUsuel()) == 0) {
                return getDateDebutSubside().compareTo(((AMCosamaRecordDetail) o).getDateDebutSubside());
            } else {
                return getNomPrenomUsuel().compareTo(((AMCosamaRecordDetail) o).getNomPrenomUsuel());
            }
        } else {
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.AMCosamaRecord#formatFields()
     */
    @Override
    public void formatFields() {
        setTypeEnregistrement(fillField(getTypeEnregistrement(), '0', 1, true));
        noArticle = fillField(noArticle, '0', 8, true);
        noAssure = fillField(noAssure, ' ', 20, false);
        ancienNoAssure = fillField(ancienNoAssure, ' ', 20, false);
        caisseInterne = fillField(caisseInterne, ' ', 1, true);
        noPersonnelCantonal = fillField(JadeStringUtil.removeChar(noPersonnelCantonal, '.'), ' ', 20, false);
        noAVS = fillField(JadeStringUtil.removeChar(noAVS, '.'), ' ', 20, false);
        nomUsuel = fillField(nomUsuel, ' ', 50, false);
        prenomUsuel = fillField(prenomUsuel, ' ', 30, false);
        nomPrenomUsuel = fillField(nomPrenomUsuel, ' ', 80, false);
        etatCivil = fillField(etatCivil, ' ', 1, false);
        complementAdresse = fillField(complementAdresse, ' ', 30, false);
        nomOfficielRue = fillField(nomOfficielRue, ' ', 30, false);
        numeroMaison = fillField(numeroMaison, ' ', 10, false);
        codePostal = fillField(codePostal, ' ', 8, false);
        localite = fillField(localite, ' ', 30, false);
        commune = fillField(commune, '0', 4, true);
        dateNaissance = fillField(dateNaissance, '0', 8, false);
        sexe = fillField(sexe, ' ', 1, false);
        beneficiairePC = fillField(beneficiairePC, ' ', 1, false);
        beneficiaireAssiste = fillField(beneficiaireAssiste, ' ', 1, false);
        noFamille = fillField(noFamille, ' ', 20, false);
        codeChefFamille = fillField(codeChefFamille, ' ', 1, false);
        codePayeurPrimes = fillField(codePayeurPrimes, ' ', 1, false);
        groupeAge = fillField(groupeAge, ' ', 1, false);
        montantFranchise = fillField(montantFranchise, '0', 6, true);
        couvertureAccident = fillField(couvertureAccident, ' ', 1, false);
        hmoReseauBonus = fillField(hmoReseauBonus, ' ', 1, false);
        echelonBonus = fillField(echelonBonus, ' ', 1, false);
        zoneTarifaire = fillField(zoneTarifaire, ' ', 1, false);
        montantPrime = fillField(montantPrime, '0', 6, true);
        modePaiementPrimes = fillField(modePaiementPrimes, '0', 2, true);
        codeAssuresCollectifs = fillField(codeAssuresCollectifs, '0', 1, true);
        debutValiditeAffiliation = fillField(debutValiditeAffiliation, '0', 8, true);
        finValiditeAffiliation = fillField(finValiditeAffiliation, '0', 8, true);
        debutValiditePrime = fillField(debutValiditePrime, '0', 8, true);
        primesArrierees = fillField(primesArrierees, ' ', 1, false);
        noNouvelleCM = fillField(noNouvelleCM, '0', 4, true);
        noAncienneCM = fillField(noAncienneCM, '0', 4, true);
        dateDemandeRevision = fillField(dateDemandeRevision, '0', 8, false);
        typeDecision = fillField(typeDecision, ' ', 1, false);
        dateDecision = fillField(dateDecision, '0', 8, false);
        dateSuspension = fillField(dateSuspension, '0', 8, false);
        sectionCM = fillField(sectionCM, ' ', 5, false);
        montantEffectifSubside = fillField(montantEffectifSubside, '0', 6, true);
        montantDecompte = fillField(montantDecompte, '0', 6, true);
        montantPartPatronale = fillField(montantPartPatronale, '0', 6, true);
        montantPartPatronalePourcent = fillField(montantPartPatronalePourcent, '0', 3, true);
        typeReductionPrime = fillField(typeReductionPrime, '0', 1, true);
        pourcentReduction = fillField(pourcentReduction, '0', 5, true);
        montantMaximumReduction = fillField(montantMaximumReduction, '0', 6, true);
        partMensuelleChargeAffilie = fillField(partMensuelleChargeAffilie, '0', 6, true);
        baseReductionPourcent = fillField(baseReductionPourcent, '0', 2, true);
        dateDebutSubside = fillField(dateDebutSubside, '0', 6, false);
        dateFinSubside = fillField(dateFinSubside, '0', 6, false);
        dateExtinctionDroit = fillField(dateExtinctionDroit, '0', 8, false);
        codeExtinctionDroit = fillField(codeExtinctionDroit, '0', 1, false);
        revenuDeterminant = fillField(revenuDeterminant, '0', 9, true);
        classeRevenu = fillField(classeRevenu, ' ', 2, false);
        zoneReservee = fillField(zoneReservee, ' ', 48, false);
    }

    /**
     * @return the ancienNoAssure
     */
    public String getAncienNoAssure() {
        return ancienNoAssure;
    }

    /**
     * @return the baseReductionPourcent
     */
    public String getBaseReductionPourcent() {
        return baseReductionPourcent;
    }

    /**
     * @return the beneficiaireAssiste
     */
    public String getBeneficiaireAssiste() {
        return beneficiaireAssiste;
    }

    /**
     * @return the beneficiairePC
     */
    public String getBeneficiairePC() {
        return beneficiairePC;
    }

    /**
     * @return the caisseInterne
     */
    public String getCaisseInterne() {
        return caisseInterne;
    }

    /**
     * @return the classeRevenu
     */
    public String getClasseRevenu() {
        return classeRevenu;
    }

    /**
     * @return the codeAssuresCollectifs
     */
    public String getCodeAssuresCollectifs() {
        return codeAssuresCollectifs;
    }

    /**
     * @return the codeChefFamille
     */
    public String getCodeChefFamille() {
        return codeChefFamille;
    }

    /**
     * @return the codeExtinctionDroit
     */
    public String getCodeExtinctionDroit() {
        return codeExtinctionDroit;
    }

    /**
     * @return the codePayeurPrimes
     */
    public String getCodePayeurPrimes() {
        return codePayeurPrimes;
    }

    /**
     * @return the codePostal
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * @return the commune
     */
    public String getCommune() {
        return commune;
    }

    /**
     * @return the complementAdresse
     */
    public String getComplementAdresse() {
        return complementAdresse;
    }

    /**
     * @return the couvertureAccident
     */
    public String getCouvertureAccident() {
        return couvertureAccident;
    }

    /**
     * @return the dateDebutSubside
     */
    public String getDateDebutSubside() {
        return dateDebutSubside;
    }

    /**
     * @return the dateDecision
     */
    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return the dateDemandeRevision
     */
    public String getDateDemandeRevision() {
        return dateDemandeRevision;
    }

    /**
     * @return the dateExtinctionDroit
     */
    public String getDateExtinctionDroit() {
        return dateExtinctionDroit;
    }

    /**
     * @return the dateFinSubside
     */
    public String getDateFinSubside() {
        return dateFinSubside;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the dateSuspension
     */
    public String getDateSuspension() {
        return dateSuspension;
    }

    /**
     * @return the debutValiditeAffiliation
     */
    public String getDebutValiditeAffiliation() {
        return debutValiditeAffiliation;
    }

    /**
     * @return the debutValiditePrime
     */
    public String getDebutValiditePrime() {
        return debutValiditePrime;
    }

    /**
     * @return the echelonBonus
     */
    public String getEchelonBonus() {
        return echelonBonus;
    }

    /**
     * @return the etatCivil
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    /**
     * @return the finValiditeAffiliation
     */
    public String getFinValiditeAffiliation() {
        return finValiditeAffiliation;
    }

    /**
     * @return the groupeAge
     */
    public String getGroupeAge() {
        return groupeAge;
    }

    /**
     * @return the hmoReseauBonus
     */
    public String getHmoReseauBonus() {
        return hmoReseauBonus;
    }

    /**
     * @return the localite
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * @return the modePaiementPrimes
     */
    public String getModePaiementPrimes() {
        return modePaiementPrimes;
    }

    /**
     * @return the montantDecompte
     */
    public String getMontantDecompte() {
        return montantDecompte;
    }

    /**
     * @return the montantEffectifSubside
     */
    public String getMontantEffectifSubside() {
        return montantEffectifSubside;
    }

    /**
     * @return the montantFranchise
     */
    public String getMontantFranchise() {
        return montantFranchise;
    }

    /**
     * @return the montantMaximumReduction
     */
    public String getMontantMaximumReduction() {
        return montantMaximumReduction;
    }

    /**
     * @return the montantPartPatronale
     */
    public String getMontantPartPatronale() {
        return montantPartPatronale;
    }

    /**
     * @return the montantPartPatronalePourcent
     */
    public String getMontantPartPatronalePourcent() {
        return montantPartPatronalePourcent;
    }

    /**
     * @return the montantPrime
     */
    public String getMontantPrime() {
        return montantPrime;
    }

    /**
     * @return the noAncienneCM
     */
    public String getNoAncienneCM() {
        return noAncienneCM;
    }

    /**
     * @return the noArticle
     */
    public String getNoArticle() {
        return noArticle;
    }

    /**
     * @return the noAssure
     */
    public String getNoAssure() {
        return noAssure;
    }

    /**
     * @return the noAVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * @return the noFamille
     */
    public String getNoFamille() {
        return noFamille;
    }

    /**
     * @return the nomOfficielRue
     */
    public String getNomOfficielRue() {
        return nomOfficielRue;
    }

    /**
     * @return the nomPrenomUsuel
     */
    public String getNomPrenomUsuel() {
        return nomPrenomUsuel;
    }

    /**
     * @return the nomUsuel
     */
    public String getNomUsuel() {
        return nomUsuel;
    }

    /**
     * @return the noNouvelleCM
     */
    public String getNoNouvelleCM() {
        return noNouvelleCM;
    }

    /**
     * @return the noPersonnelCantonal
     */
    public String getNoPersonnelCantonal() {
        return noPersonnelCantonal;
    }

    /**
     * @return the numeroMaison
     */
    public String getNumeroMaison() {
        return numeroMaison;
    }

    /**
     * @return the partMensuelleChargeAffilie
     */
    public String getPartMensuelleChargeAffilie() {
        return partMensuelleChargeAffilie;
    }

    /**
     * @return the pourcentReduction
     */
    public String getPourcentReduction() {
        return pourcentReduction;
    }

    /**
     * @return the prenomUsuel
     */
    public String getPrenomUsuel() {
        return prenomUsuel;
    }

    /**
     * @return the primesArrierees
     */
    public String getPrimesArrierees() {
        return primesArrierees;
    }

    /**
     * @return the revenuDeterminant
     */
    public String getRevenuDeterminant() {
        return revenuDeterminant;
    }

    /**
     * @return the sectionCM
     */
    public String getSectionCM() {
        return sectionCM;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @return the typeDecision
     */
    public String getTypeDecision() {
        return typeDecision;
    }

    /**
     * @return the typeReductionPrime
     */
    public String getTypeReductionPrime() {
        return typeReductionPrime;
    }

    /**
     * @return the zoneReservee
     */
    public String getZoneReservee() {
        return zoneReservee;
    }

    /**
     * @return the zoneTarifaire
     */
    public String getZoneTarifaire() {
        return zoneTarifaire;
    }

    @Override
    public void parseLigne(String currentLigne) {
        // Numéro de l'article
        if (currentLigne.length() > 8) {
            noArticle = currentLigne.substring(1, 9);
        }
        // Nouveau numéro de l'affilié dans la CM
        if (currentLigne.length() > 28) {
            noAssure = currentLigne.substring(9, 29);
        }
        // Ancien numéro de l'affilié dans la CM
        if (currentLigne.length() > 48) {
            ancienNoAssure = currentLigne.substring(29, 49);
        }
        // Caisse interne
        if (currentLigne.length() > 49) {
            caisseInterne = currentLigne.substring(49, 50);
        }
        // Numéro personnel de l'affilié dans le canton
        if (currentLigne.length() > 69) {
            noPersonnelCantonal = currentLigne.substring(50, 70);
        }
        // Numéro AVS
        if (currentLigne.length() > 89) {
            noAVS = currentLigne.substring(70, 90);
        }
        // Nom usuel
        if (currentLigne.length() > 139) {
            nomUsuel = currentLigne.substring(90, 140);
        }
        // Prénom usuel
        if (currentLigne.length() > 169) {
            prenomUsuel = currentLigne.substring(140, 170);
        }
        // Nom-Prénom usuel
        if (currentLigne.length() > 249) {
            nomPrenomUsuel = currentLigne.substring(170, 250);
        }
        // Etat Civil
        if (currentLigne.length() > 250) {
            etatCivil = currentLigne.substring(250, 251);
        }
        // Complément d'adresse
        if (currentLigne.length() > 280) {
            complementAdresse = currentLigne.substring(251, 281);
        }
        // Nom officiel de la rue
        if (currentLigne.length() > 310) {
            nomOfficielRue = currentLigne.substring(281, 311);
        }
        // Numéro de maison
        if (currentLigne.length() > 320) {
            numeroMaison = currentLigne.substring(311, 321);
        }
        // Numéro postal
        if (currentLigne.length() > 328) {
            codePostal = currentLigne.substring(321, 329);
        }
        // Localité
        if (currentLigne.length() > 358) {
            localite = currentLigne.substring(329, 359);
        }
        // Commune
        if (currentLigne.length() > 362) {
            commune = currentLigne.substring(359, 363);
        }
        // Date de naissance
        if (currentLigne.length() > 370) {
            dateNaissance = currentLigne.substring(363, 371);
        }
        // Sexe
        if (currentLigne.length() > 371) {
            sexe = currentLigne.substring(371, 372);
        }
        // Bénéficiaire PC
        if (currentLigne.length() > 372) {
            beneficiairePC = currentLigne.substring(372, 373);
        }
        // Assisté
        if (currentLigne.length() > 373) {
            beneficiaireAssiste = currentLigne.substring(373, 374);
        }
        // Numéro de famille
        if (currentLigne.length() > 393) {
            noFamille = currentLigne.substring(374, 394);
        }
        // Code chef de famille
        if (currentLigne.length() > 394) {
            codeChefFamille = currentLigne.substring(394, 395);
        }
        // Code payeur des primes
        if (currentLigne.length() > 395) {
            codePayeurPrimes = currentLigne.substring(395, 396);
        }
        // Groupe d'âge
        if (currentLigne.length() > 396) {
            groupeAge = currentLigne.substring(396, 397);
        }
        // Montant de la franchise
        if (currentLigne.length() > 402) {
            montantFranchise = currentLigne.substring(397, 401);
            montantFranchise += currentLigne.substring(401, 403);
        }
        // Couverture accident
        if (currentLigne.length() > 403) {
            couvertureAccident = currentLigne.substring(403, 404);
        }
        // HMO/réseau/bonus
        if (currentLigne.length() > 404) {
            hmoReseauBonus = currentLigne.substring(404, 405);
        }
        // Echelon bonus
        if (currentLigne.length() > 405) {
            echelonBonus = currentLigne.substring(405, 406);
        }
        // Zone tarifaire
        if (currentLigne.length() > 406) {
            zoneTarifaire = currentLigne.substring(406, 407);
        }
        // Montant de la prime
        if (currentLigne.length() > 412) {
            montantPrime = currentLigne.substring(407, 411);
            montantPrime += currentLigne.substring(411, 413);
        }
        // Mode de paiement des primes
        if (currentLigne.length() > 414) {
            modePaiementPrimes = currentLigne.substring(413, 415);
        }
        // Code assurés collectifs
        if (currentLigne.length() > 415) {
            codeAssuresCollectifs = currentLigne.substring(415, 416);
        }
        // Date début validité affiliation dans la CM
        if (currentLigne.length() > 423) {
            debutValiditeAffiliation = currentLigne.substring(416, 424);
        }
        // Date fin validité affiliation dans la CM
        if (currentLigne.length() > 431) {
            finValiditeAffiliation = currentLigne.substring(424, 432);
        }
        // Date début de validité de la prime
        if (currentLigne.length() > 439) {
            debutValiditePrime = currentLigne.substring(432, 440);
        }
        // Primes arriérées
        if (currentLigne.length() > 440) {
            primesArrierees = currentLigne.substring(440, 441);
        }
        // Numéro OFAS nouvelle CM
        if (currentLigne.length() > 444) {
            noNouvelleCM = currentLigne.substring(441, 445);
        }
        // Numéro OFAS ancienne CM
        if (currentLigne.length() > 448) {
            noAncienneCM = currentLigne.substring(445, 449);
        }
        // Date de demande révision
        if (currentLigne.length() > 456) {
            dateDemandeRevision = currentLigne.substring(449, 457);
        }
        // Type de décision
        if (currentLigne.length() > 457) {
            typeDecision = currentLigne.substring(457, 458);
        }
        // Date de décision
        if (currentLigne.length() > 465) {
            dateDecision = currentLigne.substring(458, 466);
        }
        // Date de la suspension
        if (currentLigne.length() > 473) {
            dateSuspension = currentLigne.substring(466, 474);
        }
        // Section de la CM
        if (currentLigne.length() > 478) {
            sectionCM = currentLigne.substring(474, 479);
        }
        // Montant effectif du subside
        if (currentLigne.length() > 484) {
            montantEffectifSubside = currentLigne.substring(479, 483);
            montantEffectifSubside += currentLigne.substring(483, 485);
        }
        // Montant du décompte
        if (currentLigne.length() > 490) {
            montantDecompte = currentLigne.substring(485, 489);
            montantDecompte += currentLigne.substring(489, 491);
        }
        // Montant de la part patronale
        if (currentLigne.length() > 496) {
            montantPartPatronale = currentLigne.substring(491, 495);
            montantPartPatronale += currentLigne.substring(495, 497);
        }
        // Part patronale en %
        if (currentLigne.length() > 499) {
            montantPartPatronalePourcent = currentLigne.substring(497, 500);
        }
        // Type de réduction de prime
        if (currentLigne.length() > 500) {
            typeReductionPrime = currentLigne.substring(500, 501);
        }
        // % de réduction
        if (currentLigne.length() > 505) {
            pourcentReduction = currentLigne.substring(501, 504);
            pourcentReduction += currentLigne.substring(504, 506);
        }
        // Montant maximum de la réduction mensuelle
        if (currentLigne.length() > 511) {
            montantMaximumReduction = currentLigne.substring(506, 510);
            montantMaximumReduction += currentLigne.substring(510, 512);
        }
        // Part mensuelle à charge de l'affilié
        if (currentLigne.length() > 517) {
            partMensuelleChargeAffilie = currentLigne.substring(512, 516);
            partMensuelleChargeAffilie += currentLigne.substring(516, 518);
        }
        // Base de la réduction en %
        if (currentLigne.length() > 519) {
            baseReductionPourcent = currentLigne.substring(518, 520);
        }
        // Date début du subside
        if (currentLigne.length() > 525) {
            dateDebutSubside = currentLigne.substring(520, 526);
        }
        // Date fin du subside
        if (currentLigne.length() > 531) {
            dateFinSubside = currentLigne.substring(526, 532);
        }
        // Date d'extinction du droit
        if (currentLigne.length() > 539) {
            dateExtinctionDroit = currentLigne.substring(532, 540);
        }
        // Code d'extinction du droit
        if (currentLigne.length() > 540) {
            codeExtinctionDroit = currentLigne.substring(540, 541);
        }
        // Revenu déterminant
        if (currentLigne.length() > 549) {
            revenuDeterminant = currentLigne.substring(541, 550);
        }
        // Classe de revenu
        if (currentLigne.length() > 551) {
            classeRevenu = currentLigne.substring(550, 552);
            // Zone réservée
            zoneReservee = currentLigne.substring(552);
        }
    }

    /**
     * @param ancienNoAssure
     *            the ancienNoAssure to set
     */
    public void setAncienNoAssure(String ancienNoAssure) {
        this.ancienNoAssure = ancienNoAssure;
    }

    /**
     * @param baseReductionPourcent
     *            the baseReductionPourcent to set
     */
    public void setBaseReductionPourcent(String baseReductionPourcent) {
        this.baseReductionPourcent = baseReductionPourcent;
    }

    /**
     * @param beneficiaireAssiste
     *            the beneficiaireAssiste to set
     */
    public void setBeneficiaireAssiste(String beneficiaireAssiste) {
        this.beneficiaireAssiste = beneficiaireAssiste;
    }

    /**
     * @param beneficiairePC
     *            the beneficiairePC to set
     */
    public void setBeneficiairePC(String beneficiairePC) {
        this.beneficiairePC = beneficiairePC;
    }

    /**
     * @param caisseInterne
     *            the caisseInterne to set
     */
    public void setCaisseInterne(String caisseInterne) {
        this.caisseInterne = caisseInterne;
    }

    /**
     * @param classeRevenu
     *            the classeRevenu to set
     */
    public void setClasseRevenu(String classeRevenu) {
        this.classeRevenu = classeRevenu;
    }

    /**
     * @param codeAssuresCollectifs
     *            the codeAssuresCollectifs to set
     */
    public void setCodeAssuresCollectifs(String codeAssuresCollectifs) {
        this.codeAssuresCollectifs = codeAssuresCollectifs;
    }

    /**
     * @param codeChefFamille
     *            the codeChefFamille to set
     */
    public void setCodeChefFamille(String codeChefFamille) {
        this.codeChefFamille = codeChefFamille;
    }

    /**
     * @param codeExtinctionDroit
     *            the codeExtinctionDroit to set
     */
    public void setCodeExtinctionDroit(String codeExtinctionDroit) {
        this.codeExtinctionDroit = codeExtinctionDroit;
    }

    /**
     * @param codePayeurPrimes
     *            the codePayeurPrimes to set
     */
    public void setCodePayeurPrimes(String codePayeurPrimes) {
        this.codePayeurPrimes = codePayeurPrimes;
    }

    /**
     * @param codePostal
     *            the codePostal to set
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    /**
     * @param commune
     *            the commune to set
     */
    public void setCommune(String commune) {
        this.commune = commune;
    }

    /**
     * @param complementAdresse
     *            the complementAdresse to set
     */
    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    /**
     * @param couvertureAccident
     *            the couvertureAccident to set
     */
    public void setCouvertureAccident(String couvertureAccident) {
        this.couvertureAccident = couvertureAccident;
    }

    /**
     * @param dateDebutSubside
     *            the dateDebutSubside to set
     */
    public void setDateDebutSubside(String dateDebutSubside) {
        this.dateDebutSubside = dateDebutSubside;
    }

    /**
     * @param dateDecision
     *            the dateDecision to set
     */
    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    /**
     * @param dateDemandeRevision
     *            the dateDemandeRevision to set
     */
    public void setDateDemandeRevision(String dateDemandeRevision) {
        this.dateDemandeRevision = dateDemandeRevision;
    }

    /**
     * @param dateExtinctionDroit
     *            the dateExtinctionDroit to set
     */
    public void setDateExtinctionDroit(String dateExtinctionDroit) {
        this.dateExtinctionDroit = dateExtinctionDroit;
    }

    /**
     * @param dateFinSubside
     *            the dateFinSubside to set
     */
    public void setDateFinSubside(String dateFinSubside) {
        this.dateFinSubside = dateFinSubside;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param dateSuspension
     *            the dateSuspension to set
     */
    public void setDateSuspension(String dateSuspension) {
        this.dateSuspension = dateSuspension;
    }

    /**
     * @param debutValiditeAffiliation
     *            the debutValiditeAffiliation to set
     */
    public void setDebutValiditeAffiliation(String debutValiditeAffiliation) {
        this.debutValiditeAffiliation = debutValiditeAffiliation;
    }

    /**
     * @param debutValiditePrime
     *            the debutValiditePrime to set
     */
    public void setDebutValiditePrime(String debutValiditePrime) {
        this.debutValiditePrime = debutValiditePrime;
    }

    /**
     * @param echelonBonus
     *            the echelonBonus to set
     */
    public void setEchelonBonus(String echelonBonus) {
        this.echelonBonus = echelonBonus;
    }

    /**
     * @param etatCivil
     *            the etatCivil to set
     */
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    /**
     * @param finValiditeAffiliation
     *            the finValiditeAffiliation to set
     */
    public void setFinValiditeAffiliation(String finValiditeAffiliation) {
        this.finValiditeAffiliation = finValiditeAffiliation;
    }

    /**
     * @param groupeAge
     *            the groupeAge to set
     */
    public void setGroupeAge(String groupeAge) {
        this.groupeAge = groupeAge;
    }

    /**
     * @param hmoReseauBonus
     *            the hmoReseauBonus to set
     */
    public void setHmoReseauBonus(String hmoReseauBonus) {
        this.hmoReseauBonus = hmoReseauBonus;
    }

    /**
     * @param localite
     *            the localite to set
     */
    public void setLocalite(String localite) {
        this.localite = localite;
    }

    /**
     * @param modePaiementPrimes
     *            the modePaiementPrimes to set
     */
    public void setModePaiementPrimes(String modePaiementPrimes) {
        this.modePaiementPrimes = modePaiementPrimes;
    }

    /**
     * @param montantDecompte
     *            the montantDecompte to set
     */
    public void setMontantDecompte(String montantDecompte) {
        this.montantDecompte = montantDecompte;
    }

    /**
     * @param montantEffectifSubside
     *            the montantEffectifSubside to set
     */
    public void setMontantEffectifSubside(String montantEffectifSubside) {
        this.montantEffectifSubside = montantEffectifSubside;
    }

    /**
     * @param montantFranchise
     *            the montantFranchise to set
     */
    public void setMontantFranchise(String montantFranchise) {
        this.montantFranchise = montantFranchise;
    }

    /**
     * @param montantMaximumReduction
     *            the montantMaximumReduction to set
     */
    public void setMontantMaximumReduction(String montantMaximumReduction) {
        this.montantMaximumReduction = montantMaximumReduction;
    }

    /**
     * @param montantPartPatronale
     *            the montantPartPatronale to set
     */
    public void setMontantPartPatronale(String montantPartPatronale) {
        this.montantPartPatronale = montantPartPatronale;
    }

    /**
     * @param montantPartPatronalePourcent
     *            the montantPartPatronalePourcent to set
     */
    public void setMontantPartPatronalePourcent(String montantPartPatronalePourcent) {
        this.montantPartPatronalePourcent = montantPartPatronalePourcent;
    }

    /**
     * @param montantPrime
     *            the montantPrime to set
     */
    public void setMontantPrime(String montantPrime) {
        this.montantPrime = montantPrime;
    }

    /**
     * @param noAncienneCM
     *            the noAncienneCM to set
     */
    public void setNoAncienneCM(String noAncienneCM) {
        this.noAncienneCM = noAncienneCM;
    }

    /**
     * @param noArticle
     *            the noArticle to set
     */
    public void setNoArticle(String noArticle) {
        this.noArticle = noArticle;
    }

    /**
     * @param noAssure
     *            the noAssure to set
     */
    public void setNoAssure(String noAssure) {
        this.noAssure = noAssure;
    }

    /**
     * @param noAVS
     *            the noAVS to set
     */
    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    /**
     * @param noFamille
     *            the noFamille to set
     */
    public void setNoFamille(String noFamille) {
        this.noFamille = noFamille;
    }

    /**
     * @param nomOfficielRue
     *            the nomOfficielRue to set
     */
    public void setNomOfficielRue(String nomOfficielRue) {
        this.nomOfficielRue = nomOfficielRue;
    }

    /**
     * @param nomPrenomUsuel
     *            the nomPrenomUsuel to set
     */
    public void setNomPrenomUsuel(String nomPrenomUsuel) {
        this.nomPrenomUsuel = nomPrenomUsuel;
    }

    /**
     * @param nomUsuel
     *            the nomUsuel to set
     */
    public void setNomUsuel(String nomUsuel) {
        this.nomUsuel = nomUsuel;
    }

    /**
     * @param noNouvelleCM
     *            the noNouvelleCM to set
     */
    public void setNoNouvelleCM(String noNouvelleCM) {
        this.noNouvelleCM = noNouvelleCM;
    }

    /**
     * @param noPersonnelCantonal
     *            the noPersonnelCantonal to set
     */
    public void setNoPersonnelCantonal(String noPersonnelCantonal) {
        this.noPersonnelCantonal = noPersonnelCantonal;
    }

    /**
     * @param numeroMaison
     *            the numeroMaison to set
     */
    public void setNumeroMaison(String numeroMaison) {
        this.numeroMaison = numeroMaison;
    }

    /**
     * @param partMensuelleChargeAffilie
     *            the partMensuelleChargeAffilie to set
     */
    public void setPartMensuelleChargeAffilie(String partMensuelleChargeAffilie) {
        this.partMensuelleChargeAffilie = partMensuelleChargeAffilie;
    }

    /**
     * @param pourcentReduction
     *            the pourcentReduction to set
     */
    public void setPourcentReduction(String pourcentReduction) {
        this.pourcentReduction = pourcentReduction;
    }

    /**
     * @param prenomUsuel
     *            the prenomUsuel to set
     */
    public void setPrenomUsuel(String prenomUsuel) {
        this.prenomUsuel = prenomUsuel;
    }

    /**
     * @param primesArrierees
     *            the primesArrierees to set
     */
    public void setPrimesArrierees(String primesArrierees) {
        this.primesArrierees = primesArrierees;
    }

    /**
     * @param revenuDeterminant
     *            the revenuDeterminant to set
     */
    public void setRevenuDeterminant(String revenuDeterminant) {
        this.revenuDeterminant = revenuDeterminant;
    }

    /**
     * @param sectionCM
     *            the sectionCM to set
     */
    public void setSectionCM(String sectionCM) {
        this.sectionCM = sectionCM;
    }

    /**
     * @param sexe
     *            the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * @param typeDecision
     *            the typeDecision to set
     */
    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    /**
     * @param typeReductionPrime
     *            the typeReductionPrime to set
     */
    public void setTypeReductionPrime(String typeReductionPrime) {
        this.typeReductionPrime = typeReductionPrime;
    }

    /**
     * @param zoneReservee
     *            the zoneReservee to set
     */
    public void setZoneReservee(String zoneReservee) {
        this.zoneReservee = zoneReservee;
    }

    /**
     * @param zoneTarifaire
     *            the zoneTarifaire to set
     */
    public void setZoneTarifaire(String zoneTarifaire) {
        this.zoneTarifaire = zoneTarifaire;
    }

    @Override
    public String writeLigne() {
        return this.writeLigne(false);
    }

    @Override
    public String writeLigne(boolean bWithSeparator) {
        String fieldSeparator = "";
        String currencySeparator = "";
        if (bWithSeparator) {
            fieldSeparator = IAMCosamaRecord._FieldSeparator;
            currencySeparator = IAMCosamaRecord._CurrencySeparator;
        }
        String finalLine = "";

        finalLine += getTypeEnregistrement();
        finalLine += fieldSeparator;

        finalLine += getNoArticle();
        finalLine += fieldSeparator;

        finalLine += getNoAssure();
        finalLine += fieldSeparator;

        finalLine += getAncienNoAssure();
        finalLine += fieldSeparator;

        finalLine += getCaisseInterne();
        finalLine += fieldSeparator;

        finalLine += getNoPersonnelCantonal();
        finalLine += fieldSeparator;

        finalLine += getNoAVS();
        finalLine += fieldSeparator;

        finalLine += getNomUsuel();
        finalLine += fieldSeparator;

        finalLine += getPrenomUsuel();
        finalLine += fieldSeparator;

        finalLine += getNomPrenomUsuel();
        finalLine += fieldSeparator;

        finalLine += getEtatCivil();
        finalLine += fieldSeparator;

        finalLine += getComplementAdresse();
        finalLine += fieldSeparator;

        finalLine += getNomOfficielRue();
        finalLine += fieldSeparator;

        finalLine += getNumeroMaison();
        finalLine += fieldSeparator;

        finalLine += getCodePostal();
        finalLine += fieldSeparator;

        finalLine += getLocalite();
        finalLine += fieldSeparator;

        finalLine += getCommune();
        finalLine += fieldSeparator;

        finalLine += getDateNaissance();
        finalLine += fieldSeparator;

        finalLine += getSexe();
        finalLine += fieldSeparator;

        finalLine += getBeneficiairePC();
        finalLine += fieldSeparator;

        finalLine += getBeneficiaireAssiste();
        finalLine += fieldSeparator;

        finalLine += getNoFamille();
        finalLine += fieldSeparator;

        finalLine += getCodeChefFamille();
        finalLine += fieldSeparator;

        finalLine += getCodePayeurPrimes();
        finalLine += fieldSeparator;

        finalLine += getGroupeAge();
        finalLine += fieldSeparator;

        if (bWithSeparator && (getMontantFranchise().length() > 2)) {
            finalLine += getMontantFranchise().substring(0, getMontantFranchise().length() - 2);
            finalLine += currencySeparator;
            finalLine += getMontantFranchise().substring(getMontantFranchise().length() - 2);
        } else {
            finalLine += getMontantFranchise();
        }
        finalLine += fieldSeparator;

        finalLine += getCouvertureAccident();
        finalLine += fieldSeparator;

        finalLine += getHmoReseauBonus();
        finalLine += fieldSeparator;

        finalLine += getEchelonBonus();
        finalLine += fieldSeparator;

        finalLine += getZoneTarifaire();
        finalLine += fieldSeparator;

        if (bWithSeparator && (getMontantPrime().length() > 2)) {
            finalLine += getMontantPrime().substring(0, getMontantPrime().length() - 2);
            finalLine += currencySeparator;
            finalLine += getMontantPrime().substring(getMontantPrime().length() - 2);
        } else {
            finalLine += getMontantPrime();
        }
        finalLine += fieldSeparator;

        finalLine += getModePaiementPrimes();
        finalLine += fieldSeparator;

        finalLine += getCodeAssuresCollectifs();
        finalLine += fieldSeparator;

        finalLine += getDebutValiditeAffiliation();
        finalLine += fieldSeparator;

        finalLine += getFinValiditeAffiliation();
        finalLine += fieldSeparator;

        finalLine += getDebutValiditePrime();
        finalLine += fieldSeparator;

        finalLine += getPrimesArrierees();
        finalLine += fieldSeparator;

        finalLine += getNoNouvelleCM();
        finalLine += fieldSeparator;

        finalLine += getNoAncienneCM();
        finalLine += fieldSeparator;

        finalLine += getDateDemandeRevision();
        finalLine += fieldSeparator;

        finalLine += getTypeDecision();
        finalLine += fieldSeparator;

        finalLine += getDateDecision();
        finalLine += fieldSeparator;

        finalLine += getDateSuspension();
        finalLine += fieldSeparator;

        finalLine += getSectionCM();
        finalLine += fieldSeparator;

        if (bWithSeparator && (getMontantEffectifSubside().length() > 2)) {
            finalLine += getMontantEffectifSubside().substring(0, getMontantEffectifSubside().length() - 2);
            finalLine += currencySeparator;
            finalLine += getMontantEffectifSubside().substring(getMontantEffectifSubside().length() - 2);
        } else {
            finalLine += getMontantEffectifSubside();
        }
        finalLine += fieldSeparator;

        if (bWithSeparator && (getMontantDecompte().length() > 2)) {
            finalLine += getMontantDecompte().substring(0, getMontantDecompte().length() - 2);
            finalLine += currencySeparator;
            finalLine += getMontantDecompte().substring(getMontantDecompte().length() - 2);
        } else {
            finalLine += getMontantDecompte();
        }
        finalLine += fieldSeparator;

        if (bWithSeparator && (getMontantPartPatronale().length() > 2)) {
            finalLine += getMontantPartPatronale().substring(0, getMontantPartPatronale().length() - 2);
            finalLine += currencySeparator;
            finalLine += getMontantPartPatronale().substring(getMontantPartPatronale().length() - 2);
        } else {
            finalLine += getMontantPartPatronale();
        }
        finalLine += fieldSeparator;

        finalLine += getMontantPartPatronalePourcent();
        finalLine += fieldSeparator;

        finalLine += getTypeReductionPrime();
        finalLine += fieldSeparator;

        if (bWithSeparator && (getPourcentReduction().length() > 2)) {
            finalLine += getPourcentReduction().substring(0, getPourcentReduction().length() - 2);
            finalLine += currencySeparator;
            finalLine += getPourcentReduction().substring(getPourcentReduction().length() - 2);
        } else {
            finalLine += getPourcentReduction();
        }
        finalLine += fieldSeparator;

        if (bWithSeparator && (getMontantMaximumReduction().length() > 2)) {
            finalLine += getMontantMaximumReduction().substring(0, getMontantMaximumReduction().length() - 2);
            finalLine += currencySeparator;
            finalLine += getMontantMaximumReduction().substring(getMontantMaximumReduction().length() - 2);
        } else {
            finalLine += getMontantMaximumReduction();
        }
        finalLine += fieldSeparator;

        if (bWithSeparator && (getPartMensuelleChargeAffilie().length() > 2)) {
            finalLine += getPartMensuelleChargeAffilie().substring(0, getPartMensuelleChargeAffilie().length() - 2);
            finalLine += currencySeparator;
            finalLine += getPartMensuelleChargeAffilie().substring(getPartMensuelleChargeAffilie().length() - 2);
        } else {
            finalLine += getPartMensuelleChargeAffilie();
        }
        finalLine += fieldSeparator;

        finalLine += getBaseReductionPourcent();
        finalLine += fieldSeparator;

        finalLine += getDateDebutSubside();
        finalLine += fieldSeparator;

        finalLine += getDateFinSubside();
        finalLine += fieldSeparator;

        finalLine += getDateExtinctionDroit();
        finalLine += fieldSeparator;

        finalLine += getCodeExtinctionDroit();
        finalLine += fieldSeparator;

        finalLine += getRevenuDeterminant();
        finalLine += fieldSeparator;

        // Présent dans définition mais pas implémentation AS/400
        //
        // finalLine += this.getClasseRevenu();
        // finalLine += fieldSeparator;
        //
        // finalLine += this.getZoneReservee();
        // finalLine += fieldSeparator;

        return finalLine;
    }

}
