package globaz.corvus.annonce.controle;

import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle10EmeRevision;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;

/**
 * Cette classe est bas�e sur les directives l�gal 10�me r�vision qui d�finissent en fonction de la rente les champs qui
 * doivent
 * imp�rativement �tres remplis et ceux qui doivent �tre vide le tout en fonction du type de rente (AVS_RO, AVS_REO,
 * etc)</br>
 * Ces contr�les doivent �tres r�alis�s sur l'annonce de domaine avant la g�n�ration des annonces en DB (BEntity) et
 * donc avant le formatage des valeurs pour l'enregistrement en DB</br>
 * Actuellement, aucune validation n'est r�alis�e par cette classe car les contr�les d'annonces de rente sont r�alis�s
 * via Anakin</br>
 * Cette classe va donc : </br>
 * - mettre a null des champs qui seraient renseign�s et qui ne doivent pas l'�tre en fonction du type de rente</br>
 * - Mettre des valeurs � 0 dans le cas ou <strong>certains</strong> champs obligatoire seraient null (AVS_RO et AI_RO)
 * 
 * @author lga
 * 
 */
public class RECreationAnnonce10EmeRevisionControleLegaux {

    /**
     * Cette m�thode s'occupe de :</br>
     * - mettre a null des champs qui seraient renseign�s et qui ne doivent pas l'�tre en fonction du type de rente</br>
     * - Mettre des valeurs � 0 dans le cas ou <strong>certains</strong> champs obligatoire seraient null (AVS_RO et
     * AI_RO)</br>
     * 
     * @param annonce L'annonce � contr�ler
     * @throws RECreationAnnonceControleException si annonce null ou si le genre de rente n'est pas renseign�
     */
    public void controlAnnonce(REAnnoncePonctuelle10EmeRevision annonce) throws RECreationAnnonceControleException {
        if (annonce == null) {
            throw new RECreationAnnonceControleException("Annonce is null");
        }
        Integer genrePrestation = annonce.getGenrePrestation();
        if (genrePrestation == null || genrePrestation == 0) {
            throw new RECreationAnnonceControleException("genre prestation is null");
        }
        PRTypeCodePrestation typeCodePrestation = RECodePrestationResolver.getGenreDePrestation(String
                .valueOf(genrePrestation));

        IPRCodePrestationEnum codePrestation = null;
        switch (typeCodePrestation) {
            case VIEILLESSE:
                codePrestation = PRCodePrestationVieillesse.getCodePrestation(genrePrestation);
                if (codePrestation == null) {
                    throw new RECreationAnnonceControleException(
                            "Unable to retrieve the IPRCodePrestationEnum assicied with codePrestation  ["
                                    + genrePrestation + "]");
                }
                if (codePrestation.isPrestationOrdinaire()) {
                    controlAVS_RO(annonce);
                } else {
                    controlAVS_REO(annonce);
                }
                break;
            case SURVIVANT:
                codePrestation = PRCodePrestationSurvivant.getCodePrestation(genrePrestation);
                if (codePrestation == null) {
                    throw new RECreationAnnonceControleException(
                            "Unable to retrieve the IPRCodePrestationEnum associed with codePrestation  ["
                                    + genrePrestation + "]");
                }
                if (codePrestation.isPrestationOrdinaire()) {
                    controlAVS_RO(annonce);
                } else {
                    controlAVS_REO(annonce);
                }
                break;

            case INVALIDITE:
                codePrestation = PRCodePrestationInvalidite.getCodePrestation(genrePrestation);
                if (codePrestation == null) {
                    throw new RECreationAnnonceControleException(
                            "Unable to retrieve the IPRCodePrestationEnum associed with codePrestation  ["
                                    + genrePrestation + "]");
                }
                if (codePrestation.isPrestationOrdinaire()) {
                    controlAI_RO(annonce);
                } else {
                    controlAI_REO(annonce);
                }
                break;

            case API:
                codePrestation = PRCodePrestationAPI.getCodePrestation(genrePrestation);
                if (codePrestation == null) {
                    throw new RECreationAnnonceControleException(
                            "Unable to retrieve the IPRCodePrestationEnum associed with codePrestation  ["
                                    + genrePrestation + "]");
                }
                if (PRDomainDePrestation.AVS.equals(codePrestation.getDomainDePrestation())) {
                    controlAVS_API(annonce);
                } else {
                    controlAI_API(annonce);
                }
                break;
            default:
                throw new RECreationAnnonceControleException(
                        "Unable to retrieve the IPRCodePrestationEnum associed with codePrestation  ["
                                + genrePrestation + "]");
        }
    }

    /**
     * Contr�le les champs commun � tous les type d'annonces</br>
     * Pas de contr�le actuellement
     * 
     * @param annonce l'annonce � contr�ler
     */
    private void controlCommonField(REAnnoncePonctuelle10EmeRevision annonce) {

    }

    private void controlAI_RO(REAnnoncePonctuelle10EmeRevision annonce) {
        controlCommonField(annonce);

        if (annonce.getDureeCoEchelleRenteAv73_nombreAnnee() == null
                && annonce.getDureeCoEchelleRenteAv73_nombreMois() == null) {
            annonce.setDureeCoEchelleRenteAv73(0, 0);
        }
        if (annonce.getDureeCoEchelleRenteDes73_nombreAnnee() == null
                && annonce.getDureeCoEchelleRenteDes73_nombreMois() == null) {
            annonce.setDureeCoEchelleRenteDes73(0, 0);
        }
        if (annonce.getDureeCotManquante48_72() == null) {
            annonce.setDureeCotManquante48_72(0);
        }
        if (annonce.getDureeCotManquante73_78() == null) {
            annonce.setDureeCotManquante73_78(0);
        }

        annonce.setNouveauNoAssureAyantDroit(null);
        annonce.setGenreDroitAPI(null);
        annonce.setNbreAnneeAnticipation(null);
        annonce.setReductionAnticipation(null);
        annonce.setDateDebutAnticipation(null);
        annonce.setSurvivant(null);
    }

    private void controlAVS_RO(REAnnoncePonctuelle10EmeRevision annonce) {
        controlCommonField(annonce);
        if (annonce.getDureeCoEchelleRenteAv73_nombreAnnee() == null
                && annonce.getDureeCoEchelleRenteAv73_nombreMois() == null) {
            annonce.setDureeCoEchelleRenteAv73(0, 0);
        }
        if (annonce.getDureeCoEchelleRenteDes73_nombreAnnee() == null
                && annonce.getDureeCoEchelleRenteDes73_nombreMois() == null) {
            annonce.setDureeCoEchelleRenteDes73(0, 0);
        }
        if (annonce.getDureeCotManquante48_72() == null) {
            annonce.setDureeCotManquante48_72(0);
        }
        if (annonce.getDureeCotManquante73_78() == null) {
            annonce.setDureeCotManquante73_78(0);
        }

        annonce.setNouveauNoAssureAyantDroit(null);
        annonce.setOfficeAICompetent(null);
        annonce.setDegreInvalidite(null);
        annonce.setCodeInfirmite(null);
        annonce.setSurvenanceEvenAssure(null);
        annonce.setAgeDebutInvalidite(null);
        annonce.setGenreDroitAPI(null);
    }

    private void controlAI_REO(REAnnoncePonctuelle10EmeRevision annonce) {
        controlCommonField(annonce);
        annonce.setNouveauNoAssureAyantDroit(null);
        annonce.setEchelleRente(null);
        annonce.setDureeCoEchelleRenteAv73(null, null);
        annonce.setDureeCoEchelleRenteDes73(null, null);
        annonce.setDureeCotManquante48_72(null);
        annonce.setDureeCotManquante73_78(null);
        annonce.setAnneeCotClasseAge(null);
        annonce.setRamDeterminant(null);
        annonce.setCodeRevenuSplitte(null);
        annonce.setDureeCotPourDetRAM(null, null);
        annonce.setAnneeNiveau(null);
        annonce.setNombreAnneeBTE(null, null);
        annonce.setNbreAnneeBTA(null, null);
        annonce.setNbreAnneeBonifTrans(null, null);
        annonce.setGenreDroitAPI(null);
        annonce.setNbreAnneeAnticipation(null);
        annonce.setReductionAnticipation(null);
        annonce.setDateDebutAnticipation(null);
        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);
        annonce.setSurvivant(null);
    }

    private void controlAI_API(REAnnoncePonctuelle10EmeRevision annonce) {
        controlCommonField(annonce);
        annonce.setNouveauNoAssureAyantDroit(null);
        annonce.setEchelleRente(null);
        annonce.setDureeCoEchelleRenteAv73(null, null);
        annonce.setDureeCoEchelleRenteDes73(null, null);
        annonce.setDureeCotManquante48_72(null);
        annonce.setDureeCotManquante73_78(null);
        annonce.setAnneeCotClasseAge(null);
        annonce.setRamDeterminant(null);
        annonce.setCodeRevenuSplitte(null);
        annonce.setDureeCotPourDetRAM(null, null);
        annonce.setAnneeNiveau(null);
        annonce.setNombreAnneeBTE(null, null);
        annonce.setNbreAnneeBTA(null, null);
        annonce.setNbreAnneeBonifTrans(null, null);
        annonce.setDegreInvalidite(null);
        annonce.setAgeDebutInvalidite(null);
        annonce.setReduction(null);
        annonce.setNbreAnneeAnticipation(null);
        annonce.setReductionAnticipation(null);
        annonce.setDateDebutAnticipation(null);
        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);
        annonce.setSurvivant(null);
    }

    private void controlAVS_API(REAnnoncePonctuelle10EmeRevision annonce) {
        controlCommonField(annonce);
        annonce.setSecondNoAssComplementaire(null);
        annonce.setNouveauNoAssureAyantDroit(null);
        annonce.setEchelleRente(null);
        annonce.setDureeCoEchelleRenteAv73(null, null);
        annonce.setDureeCoEchelleRenteDes73(null, null);
        annonce.setDureeCotManquante48_72(null);
        annonce.setDureeCotManquante73_78(null);
        annonce.setAnneeCotClasseAge(null);
        annonce.setRamDeterminant(null);
        annonce.setCodeRevenuSplitte(null);
        annonce.setDureeCotPourDetRAM(null, null);
        annonce.setAnneeNiveau(null);
        annonce.setNombreAnneeBTE(null, null);
        annonce.setNbreAnneeBTA(null, null);
        annonce.setNbreAnneeBonifTrans(null, null);
        annonce.setDegreInvalidite(null);
        annonce.setAgeDebutInvalidite(null);
        annonce.setReduction(null);
        annonce.setNbreAnneeAnticipation(null);
        annonce.setReductionAnticipation(null);
        annonce.setDateDebutAnticipation(null);
        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);
        annonce.setSurvivant(null);
    }

    private void controlAVS_REO(REAnnoncePonctuelle10EmeRevision annonce) {
        controlCommonField(annonce);
        annonce.setNouveauNoAssureAyantDroit(null);
        annonce.setEchelleRente(null);
        annonce.setDureeCoEchelleRenteAv73(null, null);
        annonce.setDureeCoEchelleRenteDes73(null, null);
        annonce.setDureeCotManquante48_72(null);
        annonce.setDureeCotManquante73_78(null);
        annonce.setAnneeCotClasseAge(null);
        annonce.setRamDeterminant(null);
        annonce.setCodeRevenuSplitte(null);
        annonce.setDureeCotPourDetRAM(null, null);
        annonce.setAnneeNiveau(null);
        annonce.setNombreAnneeBTE(null, null);
        annonce.setNbreAnneeBTA(null, null);
        annonce.setNbreAnneeBonifTrans(null, null);
        annonce.setOfficeAICompetent(null);
        annonce.setDegreInvalidite(null);
        annonce.setCodeAtteinteFonctionnelle(null);
        annonce.setCodeInfirmite(null);
        annonce.setSurvenanceEvenAssure(null);
        annonce.setAgeDebutInvalidite(null);
        annonce.setGenreDroitAPI(null);
        annonce.setNbreAnneeAnticipation(null);
        annonce.setReductionAnticipation(null);
        annonce.setDateDebutAnticipation(null);
        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);
    }
}
