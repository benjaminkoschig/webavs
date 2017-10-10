package globaz.prestation.utils.compta;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.enums.codeprestation.soustype.PRSousTypeCodePrestationPC;
import globaz.prestation.enums.codeprestation.soustype.PRSousTypeCodePrestationRFM;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.properties.CommonProperties;

public class PRRubriqueComptableResolver {

    /**
     * Map de mapping entre les prestations PC et les rubriques comptables relatives
     */
    private static Map<String, String> mapRubriquePC = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            // PC AVS
            put(PRSousTypeCodePrestationPC.PC_AVS_100.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_A_DOMICILE_ORDINAIRES_ESPECES);

            put(PRSousTypeCodePrestationPC.PC_AVS_101.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_EN_HOME_SASH);

            put(PRSousTypeCodePrestationPC.PC_AVS_102.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_EN_HOME_HORS_CANTON_SASH);

            put(PRSousTypeCodePrestationPC.PC_AVS_103.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_EN_HOME_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AVS_104.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_EN_HOME_HORS_CANTON_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AVS_114.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_EN_HOME);

            put(PRSousTypeCodePrestationPC.PC_AVS_115.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_EN_HOME_EPS);

            put(PRSousTypeCodePrestationPC.PC_AVS_116.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_EN_HOME_HORS_CANTON_EPS);
            // PC AI
            put(PRSousTypeCodePrestationPC.PC_AI_106.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_A_DOMICILE_ORDINAIRES_ESPECES);

            put(PRSousTypeCodePrestationPC.PC_AI_107.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_EN_HOME_SASH);

            put(PRSousTypeCodePrestationPC.PC_AI_108.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_EN_HOME_HORS_CANTON_SASH);

            put(PRSousTypeCodePrestationPC.PC_AI_109.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_EN_HOME_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AI_110.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_EN_HOME_HORS_CANTON_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AI_112.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_EN_HOME);

            put(PRSousTypeCodePrestationPC.PC_AI_117.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_EN_HOME_EPS);

            put(PRSousTypeCodePrestationPC.PC_AI_118.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_EN_HOME_HORS_CANTON_EPS);

        }
    };

    private static Map<String, String> mapRubriqueRestitutionPC = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            // PC AVS
            put(PRSousTypeCodePrestationPC.PC_AVS_100.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_A_RESTITUER);

            put(PRSousTypeCodePrestationPC.PC_AVS_101.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_RESTITUTION_EN_HOME_SASH);

            put(PRSousTypeCodePrestationPC.PC_AVS_102.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_SASH);

            put(PRSousTypeCodePrestationPC.PC_AVS_103.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_RESTITUTION_EN_HOME_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AVS_104.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AVS_114.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_RESTITUTION_EN_HOME);

            put(PRSousTypeCodePrestationPC.PC_AVS_115.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_RESTITUTION_EN_HOME_EPS);

            put(PRSousTypeCodePrestationPC.PC_AVS_116.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_EPS);

            // PC AI
            put(PRSousTypeCodePrestationPC.PC_AI_106.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_A_RESTITUER);

            put(PRSousTypeCodePrestationPC.PC_AI_107.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_RESTITUTION_EN_HOME_SASH);

            put(PRSousTypeCodePrestationPC.PC_AI_108.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_SASH);

            put(PRSousTypeCodePrestationPC.PC_AI_109.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_RESTITUTION_EN_HOME_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AI_110.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_SPAS);

            put(PRSousTypeCodePrestationPC.PC_AI_112.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_RESTITUTION_EN_HOME);

            put(PRSousTypeCodePrestationPC.PC_AI_117.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_RESTITUTION_EN_HOME_EPS);

            put(PRSousTypeCodePrestationPC.PC_AI_118.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_EPS);
        }
    };

    /**
     * Map de mapping entre les prestations RFM et les rubriques comptables relatives
     */
    private static Map<String, String> mapRubriqueRestiutionRFM = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            // TODO
        }
    };

    /**
     * Map de mapping entre les prestations RFM et les rubriques comptables relatives
     */
    private static Map<String, String> mapRubriqueRFM = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            // RFM AVS
            put(PRSousTypeCodePrestationRFM.RFM_AVS_201.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AVS_FRQP); // FRANCHISE_ET_PARTICIPATION

            put(PRSousTypeCodePrestationRFM.RFM_AVS_213.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AVS_REGIME); // AVS_REGIME_ALIMENTAIRE

            // RFM AI
            put(PRSousTypeCodePrestationRFM.RFM_AI_215.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AI_FRQP); // FRANCHISE_ET_PARTICIPATION

            put(PRSousTypeCodePrestationRFM.RFM_AI_227.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AI_REGIME); // REGIME_ALIMENTAIRE

            // CCVS Specifique
            // AI home et dmo
            put(PRSousTypeCodePrestationRFM.RFM_AI_DOMICILE_206.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AI_DOMICILE);

            put(PRSousTypeCodePrestationRFM.RFM_AI_HOME_212.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AI_HOME);
            // AV home et dom
            put(PRSousTypeCodePrestationRFM.RFM_AVS_DOMICILE_200.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AVS_DOMICILE);

            put(PRSousTypeCodePrestationRFM.RFM_AVS_HOME_214.getSousTypeCodePrestationAsString(),
                    APIReferenceRubrique.RFM_AVS_HOME);

        }
    };

    private static String getCSRubrique(String codePrestation, String sousTypeCodePrestation, boolean isPartCantonale,
            Map<String, String> mapRupriquePC, Map<String, String> mapRupriqueRFM, boolean forRestitution)
            throws Exception {

        // La caisse ne travaille pas avec des sous-types pour les genre de prestation PC et RFM
        if (!CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue()) {
            if (!JadeStringUtil.isBlankOrZero(sousTypeCodePrestation)) {
                throw new Exception("Error : sousTypeCodePrestation [" + sousTypeCodePrestation
                        + "] is not empty and the properties ("
                        + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getPropertyName()
                        + ") is defined as he should be empty. Can not resolve the code system of rubrique comptable");
            }
            return PRRubriqueComptableResolver.getCsRubriqueSousTypeCodeDesactive(codePrestation,
                    sousTypeCodePrestation, isPartCantonale, forRestitution);
        }

        // La caisse travaille avec des sous-types pour les genre de prestation PC et RFM
        return PRRubriqueComptableResolver.getCsRubriqueSousTypeCodeActive(codePrestation, sousTypeCodePrestation,
                mapRupriquePC, mapRupriqueRFM);

    }

    /**
     * Retourne la rubrique comptable de type restitution correspondante au genre de prestation et au sous-type de
     * prestation.</br> <strong>Le genre de prestation et le sous-type de prestation doivent être renseigné sinon une
     * exception sera lancée</strong>
     * 
     * @param genrePrestation
     *            Le genre de prestation, doit être renseigné
     * @param sousTypeCodePrestation
     *            Le sous-type du genre de prestation, doit être renseigné
     * @return Le code system de la rubrique comptable sous forme de string
     * @throws Exception
     *             Si le genre de prestation ou le sous-type genre prestation sont null ou invalides
     */
    public static String getCSRubriqueComptablePCRFMrestitution(String codePrestation, String sousTypeCodePrestation,
            boolean isPartCantonale) throws Exception {

        return PRRubriqueComptableResolver.getCSRubrique(codePrestation, sousTypeCodePrestation, isPartCantonale,
                PRRubriqueComptableResolver.mapRubriqueRestitutionPC,
                PRRubriqueComptableResolver.mapRubriqueRestiutionRFM, true);
    }

    /**
     * Retourne la rubrique comptable correspondante au genre de prestation et au sous-type de prestation.</br>
     * <strong>Le genre de prestation et le sous-type de prestation doivent être renseigné sinon une exception sera
     * lancée</strong>
     * 
     * @param genrePrestation
     *            Le genre de prestation, doit être renseigné
     * @param sousTypeCodePrestation
     *            Le sous-type du genre de prestation, doit être renseigné
     * @return Le code system de la rubrique comptable sous forme de string
     * @throws Exception
     *             Si le genre de prestation ou le sous-type genre prestation sont null ou invalides
     */
    public static String getCSRubriqueComptablePCRFMStandard(String codePrestation, String sousTypeCodePrestation,
            boolean isPartCantonale) throws Exception {

        return PRRubriqueComptableResolver.getCSRubrique(codePrestation, sousTypeCodePrestation, isPartCantonale,
                PRRubriqueComptableResolver.mapRubriquePC, PRRubriqueComptableResolver.mapRubriqueRFM, false);

    }

    /**
     * Retourne la rubrique comptable correspondante au genre de prestation et au sous-type de prestation. Le boolean
     * permet de prendre en compte le split rubrique comptable pour les PC avec part cantonale </br>
     * <strong>Le genre de prestation et le sous-type de prestation doivent être renseigné sinon une exception sera
     * lancée</strong>
     * 
     * @param codePrestation
     * @param sousTypeCodePrestation
     * @param isPartCantonale
     * @return
     * @throws Exception
     */
    public static String getCSRubriqueComptablePCRFMStandardWithPartCantonale(String codePrestation,
            String sousTypeCodePrestation, boolean isPartCantonale) throws Exception {

        return PRRubriqueComptableResolver.getCSRubrique(codePrestation, sousTypeCodePrestation, isPartCantonale,
                PRRubriqueComptableResolver.mapRubriquePC, PRRubriqueComptableResolver.mapRubriqueRFM, false);
    }

    private static String getCsRubriqueSousTypeCodeActive(String codePrestation, String sousTypeCodePrestation,
            Map<String, String> mapRupriquePC, Map<String, String> mapRupriqueRFM) throws Exception {
        if (JadeStringUtil.isEmpty(codePrestation)) {
            throw new Exception("Error : codePrestation [" + codePrestation
                    + "] is empty. Can not resolve the code system of rubrique comptable");
        }
        if (JadeStringUtil.isEmpty(sousTypeCodePrestation)) {
            throw new Exception("Error : sousTypeCodePrestation [" + sousTypeCodePrestation
                    + "] is empty. Can not resolve the code system of rubrique comptable");
        }

        if (PRCodePrestationPC.isCodePrestationPC(codePrestation)) {
            if (mapRupriquePC.containsKey(sousTypeCodePrestation)) {
                return mapRupriquePC.get(sousTypeCodePrestation);
            } else {
                throw new Exception("Error : codePrestation [" + codePrestation + "], sousTypeCodePrestation ["
                        + sousTypeCodePrestation + "] can not resolve the CS rubrique compta in domain PC STANDARD");
            }
        } else if (PRCodePrestationRFM.isCodePrestationRFM(codePrestation)) {
            if (mapRupriqueRFM.containsKey(sousTypeCodePrestation)) {
                return mapRupriqueRFM.get(sousTypeCodePrestation);
            } else {
                throw new Exception("Error : codePrestation [" + codePrestation + "], sousTypeCodePrestation ["
                        + sousTypeCodePrestation + "] can not resolve the CS rubrique compta in domain RFM STANDARD");
            }
        } else {
            throw new Exception("Error : sousTypeCodePrestation [" + sousTypeCodePrestation
                    + "]is empty AND can not resolve codePrestation [" + codePrestation + "] in domain PC or RFM");
        }

    }

    private static String getCsRubriqueSousTypeCodeDesactive(String codePrestation, String sousTypeCodePrestation,
            boolean isPartCantonale, boolean forRestitution) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(sousTypeCodePrestation)) {
            throw new Exception("Error : sousTypeCodePrestation [" + sousTypeCodePrestation
                    + "] is not empty and the properties ("
                    + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getPropertyName()
                    + ") is defined as he should be empty. Can not resolve the code system of rubrique comptable");
        }

        if (forRestitution) {
            if (PRCodePrestationPC.isCodePrestationAI(codePrestation)) {
                return APIReferenceRubrique.PC_AI_A_RESTITUER;
            } else if (PRCodePrestationPC.isCodePrestationAVS(codePrestation)) {
                return APIReferenceRubrique.PC_AVS_A_RESTITUER;
            } else if (PRCodePrestationRFM.isCodePrestationAI(codePrestation)) {
                return APIReferenceRubrique.RFM_AI_A_RESTITUER;
            } else if (PRCodePrestationRFM.isCodePrestationAVS(codePrestation)) {
                return APIReferenceRubrique.RFM_AVS_A_RESTITUER;
            } else {
                throw new Exception("Rubrique restitution: not find with this genre of prestation: " + codePrestation);
            }
        } else {
            if (PRCodePrestationPC.isCodePrestationAI(codePrestation)) {
                if (isPartCantonale) {
                    return APIReferenceRubrique.PC_AI_PART_CANTONALE;
                } else {
                    return APIReferenceRubrique.PC_AI;
                }
            } else if (PRCodePrestationPC.isCodePrestationAVS(codePrestation)) {
                if (isPartCantonale) {
                    return APIReferenceRubrique.PC_AVS_PART_CANTONALE;
                } else {
                    return APIReferenceRubrique.PC_AVS;
                }
            } else if (PRCodePrestationRFM.isCodePrestationAI(codePrestation)) {
                return APIReferenceRubrique.RFM_AI;
            } else if (PRCodePrestationRFM.isCodePrestationAVS(codePrestation)) {
                return APIReferenceRubrique.RFM_AVS;
            } else {
                throw new Exception("Rubrique: not find with this genre of prestation: " + codePrestation);
            }
        }

    }

    private PRRubriqueComptableResolver() {

    }
}
