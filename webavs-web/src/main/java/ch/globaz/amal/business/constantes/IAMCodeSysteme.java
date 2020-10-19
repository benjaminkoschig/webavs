/**
 *
 */
package ch.globaz.amal.business.constantes;

/**
 * @author DHI
 *
 */
public interface IAMCodeSysteme {

    /**
     * enum pour les codes de traitement dossier
     *
     * @author dhi
     *
     */
    public enum AMCodeTraitementDossier {
        ASSUREUR_INCONNU("42002106"),
        COMPLET("42002100"),
        INCOMPLET("42002101"),
        OPPOSITION("42002104"),
        RECOURS("42002103"),
        RETOUR_POSTE("42002105"),
        TRAITE("42002102");
        private final String value;

        private AMCodeTraitementDossier(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * enum for type code traitement in SimpleFamille ()
     *
     * @author cbu
     *
     */
    public enum AMCodeTraitementDossierFamille {
        ADRESSE_INCONNUE("42001309"),
        ATTRIBUTION_AUTRE_CANTON("42001314"),
        ATTRIBUTION_AUTRE_NO("42001317"),
        ATTRIBUTION_AUTRE_PARENT("42001310"),
        ATTRIBUTION_PROPRE_NO("42001315"),
        CHARGE_AUTRE_PARENT("42001316"),
        CHGT_ETAT_CIVIL("42001302"),
        CHGT_ETAT_CIVIL_MERE("42001313"),
        DECES("42001300"),
        DEPART("42001301"),
        DEPART_AUTRE_CANTON("42001312"),
        DEPART_ETRANGER("42001311"),
        DISPENSE("42001303"),
        EXEMPTION("42001305"),
        FIN_FORMATION("42001318"),
        MODIF_SELON_FISC("42001306"),
        PLUS_CHARGE_PARENTS("42001308"),
        RENONCEMENT("42001307"),
        SUSPENSION("42001304");

        private final String value;

        private AMCodeTraitementDossierFamille(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum AMDocumentModeles {
        ACREP10("42000085"),
        ACREP11("42000086"),
        ACREP12("42000087"),
        ACREP13("42000088"),
        ACREPC1("42000012"),
        ACREPC2("42000013"),
        ACREPC3("42000014"),
        ACREPC4("42000015"),
        ACREPC5("42000036"),
        ACREPC6("42000037"),
        ACREPC7("42000038"),
        ACREPC8("42000063"),
        ARCHIV("42000075"),
        ARCHIV0("42000074"),
        ARCHIVE("42000076"),
        ATAPP1("42000050"),
        ATENF1("42000053"),
        ATENF2("42000062"),
        ATENF8("42000070"),
        ATMIS1("42000052"),
        ATPAR1("42000049"),
        ATPAR2("42000064"),
        ATPRO1("42000047"),
        ATSUBS1("42000003"),
        ATSUBS2("42000004"),
        ATSUBS3("42000007"),
        ATSUBS4("42000029"),
        ATSUBS5("42000030"),
        ATSUBS6("42000031"),
        DECAPP1("42000051"),
        DECMAS1("42000002"),
        DECMAS2("42000024"),
        DECMAS3("42000025"),
        DECMAS5("42000060"),
        DECMAS6("42000061"),
        DECMAS7("42000069"),
        DECMASA("42000044"),
        DECMASB("42000045"),
        DECMASM("42000068"),
        DECMASV("42000059"),
        DECMIS1("42000032"),
        DECMIS2("42000033"),
        DECMISA("42000054"),
        DECMISB("42000055"),
        DECMISC("42000071"),
        DECMPC0("42000035"),
        DECMPC1("42000008"),
        DECMPC2("42000009"),
        DECMPC3("42000010"),
        DECMPC4("42000011"),
        DECMPC5("42000016"),
        DECMPC6("42000017"),
        DECMPC7("42000067"),
        DECMPC7M("42000026"),
        DECMPC8("42000028"),
        DECMPC9("42000034"),
        DECMPCA("42000039"),
        DECMPCB("42000040"),
        DECMPCC("42000041"),
        DECMPCD("42000042"),
        DECMPCE("42000043"),
        DECMPCF("42000056"),
        DECMPCM("42000066"),
        DECMPCV("42000058"),
        DECMST1("42000001"),
        DECMST10("42000089"),
        DECMST11("42000090"),
        DECMST12("42000091"),
        DECMST13("42000092"),
        DECMST14("42000093"),
        DECMST2("42000018"),
        DECMST3("42000019"),
        DECMST4("42000020"),
        DECMST5("42000021"),
        DECMST6("42000022"),
        DECMST7("42000023"),
        DECMST8("42000072"),
        DECMSTV("42000057"),
        DECOPP("42000094"),
        DECPAR1("42000048"),
        DECPAR2("42000065"),
        DECPRO1("42000046"),
        DUPLTCA("42000097"),
        DUPLTFA("42000101"),
        INFOCM1("42000005"),
        INFOCM2("42000006"),
        INFOCM3("42000027"),
        MEMREP("42000095"),
        MEMRETFA("42000099"),
        RECOTCA("42000098"),
        RENONCE("42000078"),
        REPLTCA("42000096"),
        REPLTFA("42000100"),
        SERVMIL("42000077"),
        SPECIA2("42000083"),
        SPECIA3("42000082"),
        SPECIA4("42000081"),
        SPECIA5("42000080"),
        SPECIA6("42000079"),
        SPECIA7("42000073"),
        SPECIAL("42000084");

        // Value storage
        private final String value;

        // Default private constructor
        private AMDocumentModeles(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * enum for document status code system (AMDOCSTS)
     *
     * @author DHI
     *
     */
    public enum AMDocumentStatus {
        AUTOGENERATED("42002400"),
        ERROR("42002405"),
        INPROGRESS("42002404"),
        MANUALGENERATED("42002401"),
        PRINTED("42002402"),
        SENT("42002403");
        // Value storage
        private final String value;

        // Default private constructor
        private AMDocumentStatus(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * enum for document type code system (AMDOCTYPE)
     *
     * @author DHI
     *
     */
    public enum AMDocumentType {
        ANNONCE("42002501"),
        ENVOI("42002500");
        // Value storage
        private final String value;

        // Default private constructor
        private AMDocumentType(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * Enum for job type code system (AMJOBTYPE)
     *
     * @author DHI
     *
     */
    public enum AMJobType {
        JOBANNONCE("42002303"),
        JOBMANUALEDITED("42002301"),
        JOBMANUALQUEUED("42002300"),
        JOBPROCESS("42002302");
        // Value storage
        private final String value;

        // Default private constructor
        private AMJobType(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * enum for parametre application type (AMPARAPP)
     *
     * @author DHI
     *
     */
    public enum AMParametreApplication {
        CHEMIN_EXPORT_WORD_CLIENT("42003101"),
        CHEMIN_EXPORT_WORD_SERVER_JOB("42003102"),
        CHEMIN_EXPORT_WORD_SERVER_WAS("42003100"),
        COSAMA_INCR("42003106"),
        FICHIER_XSD("42003105"),
        NAMESPACE_URL("42003104"),
        NAMESPACE_XSD("42003103"),
        USER_REPRISE("42003107");
        // Value storage
        private final String value;

        // Default private constructor
        private AMParametreApplication(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * enum for rubriques revenus
     *
     * @author CBU
     *
     */
    public enum AMRubriqueRevenu {
        ALLOCATION_FAMILIALE("190"),
        ALLOCATION_FAMILIALE_C("190c"),
        DEDUCTION_APPRENTIS_ETUDIANTS("660"),
        DEDUCTION_COUPLES_MARIES("680"),
        EXCEDENT_DEPENSE_PROP_IMMO_COMMERCIALE("330"),
        EXCEDENT_DEPENSE_PROP_IMMO_COMMERCIALE_C("330c"),
        EXCEDENT_DEPENSE_PROP_IMMOB_PRIVE("310"),
        EXCEDENT_DEPENSE_SUCCESSION_NON_PARTAGEE("390"),
        FORTUNE_IMPOSABLE("890"),
        FORTUNE_TAUX("895"),
        INDEMNITE_IMPOSABLE("230"),
        INDEMNITE_IMPOSABLE_C("230c"),
        INTERETS_PASSIFS_COMMERCIAUX("535"),
        INTERETS_PASSIFS_PRIVE("530"),
        PERSONNE_CHARGE_OU_ENFANTS("620"),
        PERTE_ACTIVITE_ACC_INDEP("170"),
        PERTE_ACTIVITE_ACC_INDEP_C("170c"),
        REVENU_ACTIVITE_AGRICOLE("150"),
        REVENU_ACTIVITE_AGRICOLE_C("150c"),
        REVENU_ACTIVITE_INDEP("140"),
        REVENU_ACTIVITE_INDEP_C("140c"),
        PERTE_EXERCICES_COMM("186"),
        PERTE_LIQUIDATION("188"),
        PERTE_LIQUIDATION_C("188c"),
        PERTE_REPORTEE_EXERCICES_COMM("180"),
        PERTE_REPORTEE_EXERCICES_COMM_C("180c"),
        PERTE_SOCIETE("160"),
        PERTE_SOCIETE_C("160c"),
        RENDEMENT_FORTUNE_IMMOB_COMMERCIALE("320"),
        RENDEMENT_FORTUNE_IMMOB_COMMERCIALE_C("320c"),
        RENDEMENT_FORTUNE_IMMOB_PRIVE("300"),
        REVENU_IMPOSABLE("690"),
        REVENU_NET_EMPLOI("100"),
        REVENU_NET_EPOUSE("100c"),
        REVENU_TAUX("695"),
        TOTAL_REVENUS_NETS("490"),
        TOTAUX_100_400("480"),
        TOTAUX_100C_400C("480c");
        // Value storage
        private final String value;

        // Default private constructor
        private AMRubriqueRevenu(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    public enum AMSousTypeMessageSedexLibellesSubside {
        CONFIRMATION_DECISION("42003601"),
        CONFIRMATION_INTERRUPTION("42003604"),
        DECOMPTE_ANNUEL("42003611"),
        DEMANDE_RAPPORT_ASSURANCE("42003607"),
        EFFECTIFS_ASSURES("42003610"),
        ETAT_DECISIONS("42003609"),
        INTERRUPTION("42003603"),
        MUTATION_RAPPORT_ASSURANCE("42003606"),
        NOUVELLE_DECISION("42003600"),
        REJET_DECISION("42003602"),
        REJET_INTERRUPTION("42003605"),
        REPONSE_RAPPORT_ASSURANCE("42003608"),
        DEMANDE_PRIME_TARIFAIRE("42003612"),
        REPONSE_PRIME_TARIFAIRE("42003613");

        private final String value;

        private AMSousTypeMessageSedexLibellesSubside(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum AMSousTypeMessageSedexCOLibellesSubside {
        LISTE_PERSONNE_NE_DEVANT_PAS_ETRE_POURSUIVIES("42003801"),
        CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE("42003802"),
        DECOMPTE_TRIMESTRIEL("42003803"),
        DECOMPTE_FINAL("42003804");

        private final String value;

        private AMSousTypeMessageSedexCOLibellesSubside(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Enum for annonces sedex statuts
     *
     * @author CBU
     *
     */
    public enum AMStatutAnnonceSedex {
        CREE("42003401"),
        ENVOYE("42003402"),
        ERREUR_RECU("42003406"),
        ERROR_CREE("42003403"),
        ERROR_ENVOYE("42003404"),
        INITIAL("42003400"),
        RECU("42003405"),
        RECU_SIMULE("42003407");

        // Value storage
        private final String value;

        // Default private constructor
        private AMStatutAnnonceSedex(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }

        public static AMStatutAnnonceSedex getAMStatutAnnonceSedex(String value) {
            if (CREE.getValue().equals(value)) {
                return CREE;
            } else if (ENVOYE.getValue().equals(value)) {
                return ENVOYE;
            } else if (ERREUR_RECU.getValue().equals(value)) {
                return ERREUR_RECU;
            } else if (ERROR_CREE.getValue().equals(value)) {
                return ERROR_CREE;
            } else if (ERROR_ENVOYE.getValue().equals(value)) {
                return ERROR_ENVOYE;
            } else if (RECU.getValue().equals(value)) {
                return RECU;
            } else if (RECU_SIMULE.getValue().equals(value)) {
                return RECU_SIMULE;
            } else {
                return INITIAL;
            }
        }

        /**
         * @return image name in accord to message status
         */
        public static String getStatusImageName(String value) {
            String imgName = "";
            AMStatutAnnonceSedex enumValue = AMStatutAnnonceSedex.getAMStatutAnnonceSedex(value);
            switch (enumValue) {
                case CREE:
                    imgName = "sedex_creation_ok.png";
                    break;
                case ERROR_CREE:
                    imgName = "sedex_creation_ko.png";
                    break;
                case ENVOYE:
                    imgName = "sedex_envoi_ok.png";
                    break;
                case ERROR_ENVOYE:
                    imgName = "sedex_envoi_ko.png";
                    break;
                case RECU:
                    imgName = "sedex_recu_ok.png";
                    break;
                case ERREUR_RECU:
                    imgName = "sedex_recu_ko.png";
                    break;
                default:
                    imgName = "";
                    break;
            }
            return imgName;
        }

        /**
         * @return image description in accord to message status
         */
        public static String getStatusImageLabel(String value) {
            String imgLabel = "";
            AMStatutAnnonceSedex enumValue = AMStatutAnnonceSedex.getAMStatutAnnonceSedex(value);
            switch (enumValue) {
                case CREE:
                    imgLabel = "Créé";
                    break;
                case ERROR_CREE:
                    imgLabel = "Erreur création";
                    break;
                case ENVOYE:
                    imgLabel = "Envoyé";
                    break;
                case ERROR_ENVOYE:
                    imgLabel = "Erreur envoyé";
                    break;
                case RECU:
                    imgLabel = "Reçu";
                    break;
                case ERREUR_RECU:
                    imgLabel = "Erreur reçu";
                    break;
                default:
                    imgLabel = "";
                    break;
            }
            return imgLabel;
        }
    }

    /**
     * enum for type taxation (AMTYTAX)
     *
     * @author DHI
     *
     */
    public enum AMTaxationType {
        DECISION_NVL("42001504"),
        DECISION_RCL("42001505"),
        DECISION_REC("42001509"),
        OFFICE_DETAIL("42001503"),
        OFFICE_TOTAUX("42001502"),
        ORDINAIRE("42001501"),
        PROVISOIRE("42001500"),
        RAPPEL_IMPOT("42001508"),
        RECTIFICATION("42001507"),
        REVISION_TAX("42001506");
        // Value storage
        private final String value;

        // Default private constructor
        private AMTaxationType(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * Enum for annonces sedex traitements
     *
     * @author CBU
     *
     */
    public enum AMTraitementsAnnonceSedex {
        A_TRAITER("42005702"),
        TRAITE_AUTO("42005700"),
        TRAITE_MANU("42005701");
        // Value storage
        private final String value;

        // Default private constructor
        private AMTraitementsAnnonceSedex(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * enum for document type code system (AMDOCTYPE)
     *
     * @author CBU
     *
     */
    public enum AMTypeDemandeSubside {
        ASSISTE("42002000"),
        DEMANDE("42002001"),
        PC("42002003"),
        REPRISE("42002004"),
        SOURCE("42002002");
        // Value storage
        private final String value;

        // Default private constructor
        private AMTypeDemandeSubside(String value) {
            this.value = value;
        }

        // value getter
        public String getValue() {
            return value;
        }
    }

    /**
     * enum for type source taxation (AMRYSRCTAX)
     *
     * @author dhi
     *
     */
    public enum AMTypeSourceTaxation {
        AUTO_FISC("42002900"),
        FUSION_AUTO("42002904"),
        FUSION_MANU("42002903"),
        MANUELLE("42002901"),
        REPRISE_AS400("42002902");
        private final String value;

        private AMTypeSourceTaxation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    // ---------------------------------------------------------------------------------
    // Codes professions
    // ---------------------------------------------------------------------------------
    public final static String CS_CODE_PROF_AGRICULTEUR = "42001703";
    public final static String CS_CODE_PROF_INCONNU = "42001704";
    public final static String CS_CODE_PROF_INDEPENDANT = "42001702";
    public final static String CS_CODE_PROF_RENTIER = "42001701";
    public final static String CS_CODE_PROF_SALARIE = "42001700";

    // ---------------------------------------------------------------------------------
    // Etats civils
    // ---------------------------------------------------------------------------------
    public final static String CS_ETAT_CIVIL_CELIBATAIRE = "42001600";
    public final static String CS_ETAT_CIVIL_CONCUBIN = "42001605";
    public final static String CS_ETAT_CIVIL_DIVORCE = "42001603";
    public final static String CS_ETAT_CIVIL_HOIRIE = "42001606";
    public final static String CS_ETAT_CIVIL_MARIED = "42001601";
    public final static String CS_ETAT_CIVIL_SEPARE = "42001604";
    public final static String CS_ETAT_CIVIL_VEUF = "42001602";

    // ---------------------------------------------------------------------------------
    // Genres taxations
    // ---------------------------------------------------------------------------------
    public final static String CS_GENRE_TAXATION_OFFICE = "42001502";
    public final static String CS_GENRE_TAXATION_ORDINAIRE = "42001501";
    public final static String CS_GENRE_TAXATION_PROVISOIRE = "42001500";

    public final static String CS_MOYEN_COMMUNICATION_SEDEX = "42003700";

    public final static String CS_REVENU_CODE_PROFESSION = "42001700";
    // ---------------------------------------------------------------------------------
    // Rubriques
    // ---------------------------------------------------------------------------------
    public final static String CS_RUBRIQUE_ALLOCATIONFAMILLE = "42002239";
    public final static String CS_RUBRIQUE_DEDUCAPPRENTIETUDIANT = "42002257";
    public final static String CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOCOMM = "42002249";
    public final static String CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOPRIVE = "42002248";
    public final static String CS_RUBRIQUE_EXCEDENTDEPSUCCNONPART = "42002255";
    public final static String CS_RUBRIQUE_FORTUNEIMPOSABLE = "42002260";
    public final static String CS_RUBRIQUE_FORTUNETAUX = "42002261";
    public final static String CS_RUBRIQUE_INDEMNITEIMPOSABLE = "42002242";
    public final static String CS_RUBRIQUE_INTERETSPASSIFSCOMM = "42002247";
    public final static String CS_RUBRIQUE_INTERETSPASSIFSPRIVE = "42002246";
    public final static String CS_RUBRIQUE_PERSONNECHARGEENFANT = "42002256";
    public final static String CS_RUBRIQUE_PERTEACTIVITEACCESSINDEP = "42002254";
    public final static String CS_RUBRIQUE_REVENUACTIVINDEP = "42002268";
    public final static String CS_RUBRIQUE_REVENUACTIVINDEPEPOUSE = "42002269";
    public final static String CS_RUBRIQUE_PERTEACTIVITEAGRIC = "42002252";
    public final static String CS_RUBRIQUE_REVENUACTIVITEAGRIC = "42002270";
    public final static String CS_RUBRIQUE_REVENUACTIVITEAGRICEPOUSE = "42002271";
    public final static String CS_RUBRIQUE_PERTEACTIVITEINDEP = "42002251";
    public final static String CS_RUBRIQUE_PERTELIQUIDATION = "42002263";
    public final static String CS_RUBRIQUE_PERTEREPEXCOMM = "42002262";
    public final static String CS_RUBRIQUE_PERTESEXERCICESCOMM = "42002250";
    public final static String CS_RUBRIQUE_PERTESOCIETE = "42002253";
    public final static String CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBCOMM = "42002244";
    public final static String CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBPRIVE = "42002243";
    public final static String CS_RUBRIQUE_REVENUIMPOSABLE = "42002258";
    public final static String CS_RUBRIQUE_REVENUNETEMPLOI = "42002240";
    public final static String CS_RUBRIQUE_REVENUNETEPOUSE = "42002241";
    public final static String CS_RUBRIQUE_REVENUTAUX = "42002259";
    public final static String CS_RUBRIQUE_DEDUCTIONCOUPLESMARIES = "42002265";

    public final static String CS_RUBRIQUE_TOTAUXREVENUSNETS = "42002245";
    public final static String CS_TYPE_CONTRIBUABLE = "42002600";
    public final static String CS_TYPE_ENFANT = "42001202";
    public final static String CS_TYPE_MERE = "42001201";
    public final static String CS_TYPE_PERE = "42001200";

    public final static String CS_TYPE_SOURCIER = "42002601";

}
