/**
 * 
 */
package ch.globaz.al.utils;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstEditingDecision;
import ch.globaz.al.business.exceptions.utils.ALUtilsException;

/**
 * Classe utilitaire liés à l'édition de documents par editin
 * 
 * @author pta
 * 
 */
public abstract class ALEditingUtils {

    /**
     * Hahsmap contenant etat du dossier
     */
    private static HashMap<String, String> etatDossier = null;
    /**
     * HashMap contenant les genres d'affiliation (clé: cs genre Affiliation, valeur: genre affiliationpour editing)
     * TODO à mettre dans un truc plus générique peut être utile à d'autres application
     */
    private static HashMap<String, String> genreAffiliation = null;
    /**
     * HashMap retournant le code genre d'une personne
     */
    private static HashMap<String, String> genrePersonne = null;
    /**
     * Hashmap contenant motifFin (clé: cs motif fin, valeur: fin motif échéance droit
     */

    private static HashMap<String, String> motifFinEcheance = null;
    /**
     * HashMap contenant le sexe de la personne (clé: cs tiers, valeur: sexe editing)
     */
    private static HashMap<String, String> sexePersonne = null;
    /**
     * Hashmap contenant statut du dossier (clé: cs statut dossier, valeur, statut editing)
     */
    private static HashMap<String, String> statutDossier = null;
    /**
     * Hashmap contenant activité (clé: cs activite allocataire, valeur: activité editing)
     */
    private static HashMap<String, String> typeBenfecifiaireType = null;
    /**
     * Hasmap contenant type de droit (prestations)
     */
    private static HashMap<String, String> typeDroit = null;
    /**
     * Hashmap contenant le type de paiement
     */
    private static HashMap<String, String> typePaiement = null;
    /**
     * Hashmap contenant type de prestation naissance et accueil
     */
    private static HashMap<String, String> typePrestaDivers = null;

    /**
     * Hashmap contenant type de prestations
     */
    private static HashMap<String, String> typePrestation = null;

    /**
     * Retourne une HahMap contenant les valeurs des états d'un dossier
     * 
     * @return
     */
    public static HashMap<String, String> getEtatDossier() {
        if (ALEditingUtils.etatDossier == null) {
            ALEditingUtils.etatDossier = new HashMap<String, String>();
            ALEditingUtils.etatDossier.put(ALCSDossier.ETAT_ACTIF, ALConstEditingDecision.ETAT_DOSSIER_ACTIF);
            ALEditingUtils.etatDossier.put(ALCSDossier.ETAT_RADIE, ALConstEditingDecision.ETAT_DOSSIER_RAD);

        }

        return ALEditingUtils.etatDossier;

    }

    public static HashMap<String, String> getGenreAffiliation() {
        if (ALEditingUtils.genreAffiliation == null) {
            ALEditingUtils.genreAffiliation = new HashMap<String, String>();
            // TODO voir si rajouter GENRE affiliation Bénéficiaire AF
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_AFFILIATION_PROVISOIRE,
                    ALConstEditingDecision.GENRE_AFFILIATION_PROVISOIRE);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_ANOBAG,
                    ALConstEditingDecision.GENRE_ANOBAG);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_EMPL_D_F,
                    ALConstEditingDecision.GENRE_EMPL_D_F);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_EMPLOYEUR,
                    ALConstEditingDecision.GENRE_EMPLOYEUR);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_INDEPENDANT,
                    ALConstEditingDecision.GENRE_INDEPENDANT);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_INDEPENDANT_ET_EMPLOYEUR,
                    ALConstEditingDecision.GENRE_INDEPENDANT_ET_EMPLOYEUR);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_NA_A_L_ETRANGER_SELON_ART1_AL4,
                    ALConstEditingDecision.GENRE_NA_A_L_ETRANGER_SELON_ART1_AL4);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_NON_ACTIF,
                    ALConstEditingDecision.GENRE_NON_ACTIF);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_NON_SOUMIS,
                    ALConstEditingDecision.GENRE_NON_SOUMIS);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_TSE, ALConstEditingDecision.GENRE_TSE);
            ALEditingUtils.genreAffiliation.put(ALConstEditingDecision.CS_GENRE_TSE_VOLONTAIRE,
                    ALConstEditingDecision.GENRE_TSE_VOLONTAIRE);

        }

        return ALEditingUtils.genreAffiliation;
    }

    /**
     * Retourne une Hahmap contenant le genre pour une personne
     */

    public static HashMap<String, String> getGenrePersonne() {
        if (ALEditingUtils.genrePersonne == null) {
            ALEditingUtils.genrePersonne = new HashMap<String, String>();

            ALEditingUtils.genrePersonne.put(ALConstEditingDecision.PERS_CS_GENRE_MADAME,
                    ALConstEditingDecision.PERS_GENRE_MADAME);
            ALEditingUtils.genrePersonne.put(ALConstEditingDecision.PERS_CS_GENRE_MADEMOISELLE,
                    ALConstEditingDecision.PERS_GENRE_MADEMOISELLE);
            ALEditingUtils.genrePersonne.put(ALConstEditingDecision.PERS_CS_GENRE_MONSIEUR,
                    ALConstEditingDecision.PERS_GENRE_MONSIEUR);
        }
        return ALEditingUtils.genrePersonne;
    }

    /**
     * Retourne un hashmap contenant les motifs de fin échéance
     */

    public static HashMap<String, String> getMotifFinEcheance() {

        if (ALEditingUtils.motifFinEcheance == null) {
            ALEditingUtils.motifFinEcheance = new HashMap<String, String>();

            ALEditingUtils.motifFinEcheance.put(ALCSDroit.MOTIF_FIN_CTAR, ALConstEditingDecision.MOTIF_FIN_DROIT_CHGMT);
            ALEditingUtils.motifFinEcheance.put(ALCSDroit.MOTIF_FIN_ECH, ALConstEditingDecision.MOTIF_FIN_DROIT_ECHU);
            ALEditingUtils.motifFinEcheance.put(ALCSDroit.MOTIF_FIN_RAD, ALConstEditingDecision.MOTIF_FIN_DROIT_RADIE);
        }
        return ALEditingUtils.motifFinEcheance;

    }

    /**
     * Retourne une hasmap contenant la valeur du statut du dossier
     */

    public static HashMap<String, String> getStatutDossier() {

        if (ALEditingUtils.statutDossier == null) {
            ALEditingUtils.statutDossier = new HashMap<String, String>();

            ALEditingUtils.statutDossier.put(ALCSDossier.STATUT_CP, ALConstEditingDecision.STATUT_CANTONAL_PRIORITAIRE);
            ALEditingUtils.statutDossier.put(ALCSDossier.STATUT_CS, ALConstEditingDecision.STATUT_CANTONAL_SUPPLETIF);
            ALEditingUtils.statutDossier.put(ALCSDossier.STATUT_IP,
                    ALConstEditingDecision.STATUT_INTERNATIONAL_PRIORITAIRE);
            ALEditingUtils.statutDossier.put(ALCSDossier.STATUT_IS, ALConstEditingDecision.STATUT_INTERNATIONAL_SUPPL);
            ALEditingUtils.statutDossier.put(ALCSDossier.STATUT_N, ALConstEditingDecision.STATUT_NORMAL);
            ALEditingUtils.statutDossier.put(ALCSDossier.STATUT_NP, ALConstEditingDecision.STATUT_PRIORITAIRE);
        }

        return ALEditingUtils.statutDossier;
    }

    /**
     * retourne une hashmap contenant le typeBenenficaire du dossier
     * 
     * @return the statutDossier
     */
    public static HashMap<String, String> getTypeBenfecifiaireType() {

        if (ALEditingUtils.typeBenfecifiaireType == null) {
            ALEditingUtils.typeBenfecifiaireType = new HashMap<String, String>();

            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_AGRICULTEUR,
                    ALConstEditingDecision.ACTIVITE_AGRICULTEUR);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE,
                    ALConstEditingDecision.ACTIVITE_COLLABO_AGRI);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE,
                    ALConstEditingDecision.ACTIVITE_EXP_ALPAGE);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_INDEPENDANT,
                    ALConstEditingDecision.ACTIVITE_INDEP);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_NONACTIF,
                    ALConstEditingDecision.ACTIVITE_NON_ACTIF);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_PECHEUR,
                    ALConstEditingDecision.ACTIVITE_PECHEUR);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_SALARIE,
                    ALConstEditingDecision.ACTIVITE_SALARIE);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE,
                    ALConstEditingDecision.ACTIVITE_TRAV_AGRICOLE);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_TSE,
                    ALConstEditingDecision.ACTIVITE_TRAV_SANS_EMPL);
            ALEditingUtils.typeBenfecifiaireType.put(ALCSDossier.ACTIVITE_VIGNERON,
                    ALConstEditingDecision.ACTIVITE_VIGNERON);

        }
        return ALEditingUtils.typeBenfecifiaireType;

    }

    /**
     * retourne une hasmap contenant les type de droits (prestations) de base
     */
    public static HashMap<String, String> getTypeDroit() {
        if (ALEditingUtils.typeDroit == null) {
            ALEditingUtils.typeDroit = new HashMap<String, String>();
            ALEditingUtils.typeDroit.put(ALCSDroit.TYPE_ENF, ALConstEditingDecision.TYPE_DROIT_ENF);
            ALEditingUtils.typeDroit.put(ALCSDroit.TYPE_FORM, ALConstEditingDecision.TYPE_DROIT_FORM);
            ALEditingUtils.typeDroit.put(ALCSDroit.TYPE_MEN, ALConstEditingDecision.TYPE_DROIT_MEN);
        }
        return ALEditingUtils.typeDroit;
    }

    /**
     * Retouren une hashmap contenant le type de paiement
     */

    public static HashMap<String, String> getTypePaiement() {
        if (ALEditingUtils.typePaiement == null) {
            ALEditingUtils.typePaiement = new HashMap<String, String>();
            ALEditingUtils.typePaiement
                    .put(ALCSDossier.PAIEMENT_DIRECT, ALConstEditingDecision.TYPE_PAIEMENT_DIR_ALLOC);
            ALEditingUtils.typePaiement.put(ALCSDossier.PAIEMENT_INDIRECT, ALConstEditingDecision.TYPE_PAIEMENT_IND);
            ALEditingUtils.typePaiement.put(ALCSDossier.PAIEMENT_TIERS, ALConstEditingDecision.TYPE_PAIEMENT_DIR_TIERS);
        }
        return ALEditingUtils.typePaiement;
    }

    /**
     * retourne une hashmap contenant les types de prestat (autres que droit base)
     */

    public static HashMap<String, String> getTypePrestaDivers() {
        if (ALEditingUtils.typePrestaDivers == null) {
            ALEditingUtils.typePrestaDivers = new HashMap<String, String>();
            ALEditingUtils.getTypePrestaDivers().put(ALCSDroit.TYPE_NAIS, ALConstEditingDecision.TYPE_PRESTA_NAISS);
            ALEditingUtils.getTypePrestaDivers().put(ALCSDroit.TYPE_ACCE, ALConstEditingDecision.TYPE_PRESTA_ACCUEIL);

        }
        return ALEditingUtils.typePrestaDivers;
    }

    /**
     * retourne une hashmap contenant les types de prestat
     */
    public static HashMap<String, String> getTypePrestation() {
        if (ALEditingUtils.typePrestation == null) {
            ALEditingUtils.typePrestation = new HashMap<String, String>();
            ALEditingUtils.getTypePrestation().put(ALCSDroit.TYPE_NAIS, ALConstEditingDecision.TYPE_PRESTA_NAISS);
            ALEditingUtils.getTypePrestation().put(ALCSDroit.TYPE_ACCE, ALConstEditingDecision.TYPE_PRESTA_ACCUEIL);
            ALEditingUtils.getTypePrestation().put(ALCSDroit.TYPE_ENF, ALConstEditingDecision.TYPE_DROIT_ENF);
            ALEditingUtils.getTypePrestation().put(ALCSDroit.TYPE_FORM, ALConstEditingDecision.TYPE_DROIT_FORM);
            ALEditingUtils.getTypePrestation().put(ALCSDroit.TYPE_MEN, ALConstEditingDecision.TYPE_DROIT_MEN);

        }
        return ALEditingUtils.typePrestation;
    }

    /**
     * Méthode qui retourne la valeur de l'état du dossier pour l'éditing
     */
    public static String getValueEditingEtatDossier(String csEtatDossier) throws JadeApplicationException {
        String res = null;
        if (csEtatDossier == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingEtatDossier : csEtatDossier is null");
        } else {
            res = ALEditingUtils.getEtatDossier().get(csEtatDossier);
            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingEtatDossier : l'état du dossier  "
                        + csEtatDossier + " is not found");

            }
        }

        return res;
    }

    /**
     * Méthode qui retourne la valeur de le genre d'affiliation
     */
    public static String getValueEditingGenreAffiliation(String csGenreAffiliation) throws JadeApplicationException {
        String res = null;
        if (csGenreAffiliation == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingGenreAffiliation : csEtatDossier is null");
        } else {
            res = ALEditingUtils.getGenreAffiliation().get(csGenreAffiliation);
            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingGenreAffiliation : le genre d'affiliation  "
                        + csGenreAffiliation + " is not found");

            }
        }

        return res;
    }

    /**
     * Méthode qui retourne la valeur editing du genre pour la personne
     */

    public static String getValueEditingGenrePersonne(String csTiersTitre) throws JadeApplicationException {
        String res = null;

        if (csTiersTitre == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingGenrePersonne : csTiersTitre is null");
        } else {

            res = ALEditingUtils.getGenrePersonne().get(csTiersTitre);

            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingGenrePersonne : le genre " + csTiersTitre
                        + " is not found");
            }

        }
        return res;
    }

    /**
     * Méthode qui retourne la valeur editing d'un motif d'échéance
     */
    public static String getValueEditingMotifEcheance(String csMotifEcheance) throws JadeApplicationException {
        String res = null;

        if (csMotifEcheance == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingMotifEcheance : csMotifEcheance is null");

        } else {
            res = ALEditingUtils.getMotifFinEcheance().get(csMotifEcheance);

            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingMotifEcheance : le motif d'échéance "
                        + csMotifEcheance + " is not found");
            }
        }

        return res;

    }

    /**
     * Méthode qui retourne la valeur de l'état du dossier pour l'diting de décision af
     * 
     * @param csStatutDossier
     * @return
     * @throws JadeApplicationException
     */
    public static String getValueEditingStatutDossier(String csStatutDossier) throws JadeApplicationException {
        String res = null;

        if (csStatutDossier == null) {
            throw new ALUtilsException("ALEditingUtils@getValueStatutDossier : csStatutDossier");
        } else {
            res = ALEditingUtils.getStatutDossier().get(csStatutDossier);
            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueStatutDossier : le type de statut "
                        + csStatutDossier + " is not found");
            }
        }
        return res;
    }

    /**
     * Méthode qui retourne la valeur du type d'activité pour le bénéficiaire du dossier (allocataire)
     * 
     * @param csTypeActivite
     * @return
     * @throws JadeApplicationException
     */
    public static String getValueEditingTypeBeneficiaire(String csTypeActivite) throws JadeApplicationException {
        String res = null;

        if (csTypeActivite == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingTypeBeneficiaire : csTypeActivite is null");
        } else {
            res = ALEditingUtils.getTypeBenfecifiaireType().get(csTypeActivite);
            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingTypeBeneficiaire : le type de bénéficiaire "
                        + csTypeActivite + " is not found");
            }

        }

        return res;
    }

    /**
     * méthode qui retourne la valeur editing du code system du droit passé en paramètre
     * 
     * @throws JadeApplicationException
     */

    public static String getValueEditingTypeDroit(String csTypeDroit) throws JadeApplicationException {
        String res = null;

        if (csTypeDroit == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingTypeDroit : csTypeDroit is null");
        }

        else {
            res = ALEditingUtils.getTypeDroit().get(csTypeDroit);

            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingTypeDroit : le type de droit " + csTypeDroit
                        + " is not found");
            }
        }
        return res;

    }

    /**
     * Méthode qui retourne la valeur editing d'un type de paiement
     */
    public static String getValueEditingTypePaiement(String csTypePaiement) throws JadeApplicationException {
        String res = null;
        if (csTypePaiement == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingTypePaiement : csTypePaiement is null");
        } else {
            res = ALEditingUtils.getTypePaiement().get(csTypePaiement);

            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingTypePaiement : le type de paiement "
                        + csTypePaiement + " is not found");
            }
        }

        return res;
    }

    /**
     * Méthode qui retourne le valeur editing de prestations diverses (autres qu'enfant, fromation ou ménage)
     */

    public static String getValueEditingTypePrestaDivers(String csPrestaDivers) throws JadeApplicationException {
        String res = null;

        if (csPrestaDivers == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingTypePrestaDivers : csPrestaDivers is null");
        } else {

            res = ALEditingUtils.getTypePrestaDivers().get(csPrestaDivers);

            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingTypePrestaDivers : le type de prestation  "
                        + csPrestaDivers + " is not found");
            }

        }
        return res;
    }

    /**
     * Méthode qui retounre une hasMap avec clé type de prestation csal droit et valeur le type pour editing
     */

    /**
     * méthode qui retourne la valeur editing du code system du droit en paramètre
     * 
     * @throws JadeApplicationException
     */

    public static String getValueEditingTypePrestation(String csTypeDroit) throws JadeApplicationException {
        String res = null;

        if (csTypeDroit == null) {
            throw new ALUtilsException("ALEditingUtils@getValueEditingTypeDroit : csTypeDroit is null");
        }

        else {
            res = ALEditingUtils.getTypePrestation().get(csTypeDroit);

            if (JadeStringUtil.isEmpty(res)) {
                throw new ALUtilsException("ALEditingUtils@getValueEditingTypeDroit : le type de prestation "
                        + csTypeDroit + " is not found");
            }
        }
        return res;

    }
}
