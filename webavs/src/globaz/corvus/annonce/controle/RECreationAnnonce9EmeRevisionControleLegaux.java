package globaz.corvus.annonce.controle;

import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle9EmeRevision;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;

/**
 * Cette classe est basée sur les directives légal qui définissent en fonction de la rente les champs qui doivent
 * impérativement êtres remplis et ceux qui doivent être vide le tout en fonction du type de rente (AVS_RO, AVS_REO,
 * etc)</br>
 * Ces contrôles doivent êtres réalisés sur l'annonce de domaine avant la génération des annonces en DB (BEntity) et
 * donc avant le formatage des valeurs pour l'enregistrement en DB</br>
 * Actuellement, aucune validation n'est réalisée par cette classe car les contrôles d'annonces de rente sont réalisés
 * via Anakin</br>
 * Cette classe va donc : </br>
 * - mettre a null des champs qui seraient renseignés et qui ne doivent pas l'être en fonction du type de rente</br>
 * - Mettre des valeurs à 0 dans le cas ou <strong>certains</strong> champs obligatoire seraient null (AVS_RO et AI_RO)
 * 
 * @author lga
 * 
 */
public class RECreationAnnonce9EmeRevisionControleLegaux {

    /**
     * Cette méthode s'occupe de :</br>
     * - mettre a null des champs qui seraient renseignés et qui ne doivent pas l'être en fonction du type de rente</br>
     * - Mettre des valeurs à 0 dans le cas ou <strong>certains</strong> champs obligatoire seraient null (AVS_RO et
     * AI_RO)</br>
     * 
     * @param annonce L'annonce à contrôler
     * @throws RECreationAnnonceControleException si annonce null ou si le genre de rente n'est pas renseigné
     * 
     * @param annonce
     * @throws RECreationAnnonceControleException
     */
    public void controlAnnonce(REAnnoncePonctuelle9EmeRevision annonce) throws RECreationAnnonceControleException {
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
     * Contrôle les champs commun à tous les type d'annonces</br>
     * Pas de contrôle actuellement
     * 
     * @param annonce l'annonce à contrôler
     */
    private void controlCommonField(REAnnoncePonctuelle9EmeRevision annonce) {

    }

    private void controlAI_API(REAnnoncePonctuelle9EmeRevision annonce) {
        controlCommonField(annonce);

        annonce.setSecondNoAssComplementaire(null);
        annonce.setNouveauNoAssureAyantDroit(null);

        annonce.setRamDeterminant(null);
        annonce.setDureeCotPourDetRAM(null, null);
        annonce.setAnneeNiveau(null);
        annonce.setRevenuPrisEnCompte(null);
        annonce.setEchelleRente(null);
        annonce.setDureeCoEchelleRenteAv73(null, null);
        annonce.setDureeCoEchelleRenteDes73(null, null);
        annonce.setDureeCotManquante48_72(null);
        annonce.setDureeCotManquante73_78(null);
        annonce.setAnneeCotClasseAge(null);
        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);
        annonce.setIsLimiteRevenu(null);
        annonce.setIsMinimumGaranti(null);
        annonce.setDegreInvalidite(null);
        annonce.setAgeDebutInvalidite(null);

    }

    private void controlAVS_API(REAnnoncePonctuelle9EmeRevision annonce) {
        controlCommonField(annonce);

        annonce.setSecondNoAssComplementaire(null);
        annonce.setNouveauNoAssureAyantDroit(null);

        annonce.setRamDeterminant(null);
        annonce.setDureeCotPourDetRAM(null, null);
        annonce.setAnneeNiveau(null);
        annonce.setRevenuPrisEnCompte(null);
        annonce.setEchelleRente(null);
        annonce.setDureeCoEchelleRenteAv73(null, null);
        annonce.setDureeCoEchelleRenteDes73(null, null);
        annonce.setDureeCotManquante48_72(null);
        annonce.setDureeCotManquante73_78(null);
        annonce.setAnneeCotClasseAge(null);
        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);
        annonce.setIsLimiteRevenu(null);
        annonce.setIsMinimumGaranti(null);
        annonce.setDegreInvalidite(null);
        annonce.setAgeDebutInvalidite(null);
    }

    private void controlAI_REO(REAnnoncePonctuelle9EmeRevision annonce) {
        controlCommonField(annonce);

        annonce.setNouveauNoAssureAyantDroit(null);

        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);

        // Si minimum garantis, les champs suivants doivent être remplis (au moins avec des 0 si vide)
        if (annonce.getIsMinimumGaranti() != null && annonce.getIsMinimumGaranti()) {
            if (annonce.getRamDeterminant() == null) {
                annonce.setRamDeterminant(0);
            }
            if (annonce.getDureeCotPourDetRAM_nombreAnnee() == null
                    && annonce.getDureeCotPourDetRAM_nombreMois() == null) {
                annonce.setDureeCotPourDetRAM(0, 0);
            }
            if (annonce.getAnneeNiveau() == null) {
                annonce.setAnneeNiveau(0);
            }
            if (annonce.getRevenuPrisEnCompte() == null) {
                annonce.setRevenuPrisEnCompte(0);
            }
            if (annonce.getEchelleRente() == null) {
                annonce.setEchelleRente(0);
            }
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
            if (annonce.getAnneeCotClasseAge() == null) {
                annonce.setAnneeCotClasseAge(0);
            }
        }
        // Sinon tous ces champs à blanc
        else {
            annonce.setRamDeterminant(null);
            annonce.setDureeCotPourDetRAM(null, null);
            annonce.setAnneeNiveau(null);
            annonce.setRevenuPrisEnCompte(null);
            annonce.setEchelleRente(null);
            annonce.setDureeCoEchelleRenteAv73(null, null);
            annonce.setDureeCoEchelleRenteDes73(null, null);
            annonce.setDureeCotManquante48_72(null);
            annonce.setDureeCotManquante73_78(null);
            annonce.setAnneeCotClasseAge(null);
        }
    }

    private void controlAI_RO(REAnnoncePonctuelle9EmeRevision annonce) {
        controlCommonField(annonce);

        if (annonce.getDureeCotManquante48_72() == null) {
            annonce.setDureeCotManquante48_72(0);
        }
        if (annonce.getDureeCotManquante73_78() == null) {
            annonce.setDureeCotManquante73_78(0);
        }

        annonce.setNouveauNoAssureAyantDroit(null);

        annonce.setIsLimiteRevenu(null);
        annonce.setIsMinimumGaranti(null);
    }

    private void controlAVS_REO(REAnnoncePonctuelle9EmeRevision annonce) {
        controlCommonField(annonce);

        annonce.setNouveauNoAssureAyantDroit(null);
        annonce.setDureeAjournement(null, null);
        annonce.setSupplementAjournement(null);
        annonce.setDateRevocationAjournement(null);
        annonce.setOfficeAICompetent(null);
        annonce.setDegreInvalidite(null);
        annonce.setCodeAtteinteFonctionnelle(null);
        annonce.setCodeInfirmite(null);
        annonce.setSurvenanceEvenAssure(null);
        annonce.setAgeDebutInvalidite(null);

        // Si minimum garantis, les champs suivants doivent être remplis (au moins avec des 0 si vide)
        if (annonce.getIsMinimumGaranti() != null && annonce.getIsMinimumGaranti()) {
            if (annonce.getRamDeterminant() == null) {
                annonce.setRamDeterminant(0);
            }
            if (annonce.getDureeCotPourDetRAM_nombreAnnee() == null
                    && annonce.getDureeCotPourDetRAM_nombreMois() == null) {
                annonce.setDureeCotPourDetRAM(0, 0);
            }
            if (annonce.getAnneeNiveau() == null) {
                annonce.setAnneeNiveau(0);
            }
            if (annonce.getRevenuPrisEnCompte() == null) {
                annonce.setRevenuPrisEnCompte(0);
            }
            if (annonce.getEchelleRente() == null) {
                annonce.setEchelleRente(0);
            }
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
            if (annonce.getAnneeCotClasseAge() == null) {
                annonce.setAnneeCotClasseAge(0);
            }
        }
        // Sinon tous ces champs à blanc
        else {
            annonce.setRamDeterminant(null);
            annonce.setDureeCotPourDetRAM(null, null);
            annonce.setAnneeNiveau(null);
            annonce.setRevenuPrisEnCompte(null);
            annonce.setEchelleRente(null);
            annonce.setDureeCoEchelleRenteAv73(null, null);
            annonce.setDureeCoEchelleRenteDes73(null, null);
            annonce.setDureeCotManquante48_72(null);
            annonce.setDureeCotManquante73_78(null);
            annonce.setAnneeCotClasseAge(null);
        }
    }

    private void controlAVS_RO(REAnnoncePonctuelle9EmeRevision annonce) {
        controlCommonField(annonce);

        if (annonce.getDureeCotManquante48_72() == null) {
            annonce.setDureeCotManquante48_72(0);
        }
        if (annonce.getDureeCotManquante73_78() == null) {
            annonce.setDureeCotManquante73_78(0);
        }

        annonce.setNouveauNoAssureAyantDroit(null);

        annonce.setIsLimiteRevenu(null);
        annonce.setIsMinimumGaranti(null);
        annonce.setOfficeAICompetent(null);
        annonce.setDegreInvalidite(null);
        annonce.setCodeAtteinteFonctionnelle(null);
        annonce.setCodeInfirmite(null);
        annonce.setSurvenanceEvenAssure(null);
        annonce.setAgeDebutInvalidite(null);
    }

}
