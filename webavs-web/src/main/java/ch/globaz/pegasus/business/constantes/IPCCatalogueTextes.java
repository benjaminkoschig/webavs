package ch.globaz.pegasus.business.constantes;

public interface IPCCatalogueTextes {

   String BABEL_DOC_NAME_ADAPTATION_ANNUELLE = "Communication Adaptation";
   String BABEL_DOC_NAME_APRES_CALCUL = "apres calcul";
   String BABEL_DOC_NAME_COMMUNICATION_OCC = "communicationOCC";
   String BABEL_DOC_NAME_DEMANDE_AGENCE_COMMUNALE_AVS = "Agence communale AVS";
   String BABEL_DOC_NAME_DEMANDE_RENSEIGNEMENT = "Demande de renseignement";
   String BABEL_DOC_NAME_PAGE_GARDE_COPIE = "Page de garde PC";
   String BABEL_DOC_NAME_PLAN_CALCUL = "planCalcul";
   String BABEL_DOC_NAME_REFUS = "refus";
   String BABEL_DOC_NAME_TRANSFERT_DOSSIER = "Transfert de Dossier PC";
   String BABEL_DOC_NAME_TRANSFERT_DOSSIER_SUPPRESSION = "Transfert sur suppression PC";
   String BABEL_DOC_NAME_TRANSFERT_RENTE = "Transfert de dossier de rente";
    /* Domaine PC */
    String CS_PC = "64055001";
    String CS_TYPE_ADAPTATION_ANNUELLE = "64056007";
    /* TYpe de document */
    String CS_TYPE_COMMUNICATION_OCC = "64056003";

    String CS_TYPE_DECISION = "64056001";
    String CS_TYPE_DEMANDE_RENSEIGNEMENT = "64056006";
    String CS_TYPE_PAGE_GARDE_COPIE = "64056005";
    String CS_TYPE_TRANSFERT_DOSSIER = "64056004";
    String CS_TYPE_RAPPEL_E_BUISNESS = "64056013";
    /* nom de docuement */
   String NOM_OPEN_OFFICE = "oppenOffice";
   String PROCESS_COMMUNICATION_ADAPTATION = "PCCommunicationAdaptation";
    /** nom des proces **/
    String PROCESS_COMMUNICATION_OCC = "PCCommunicationOCC";
    String PROCESS_DECISION_APRESCALCUL = "PCDecisionApresCalcul";
    String PROCESS_DECISION_APRESCALCUL_REFORME = "PCDecisionApresCalculReforme";
    String PROCESS_DECISION_APRESCALCUL_COPIE = "PCDecisionApresCalculCopie";
    String PROCESS_DECISION_APRESCALCUL_COPIE_REFORME = "PCDecisionApresCalculCopieReforme";
    String PROCESS_DECISION_APRESCALCUL_PG = "PCDecisionApresCalculPg";
    String PROCESS_DECISION_BILLAG = "PCAttestationBillag";
    String PROCESS_DECISION_REDEVANCE = "PCAttestationRedevance";
    String PROCESS_DECISION_DECOMPTE = "PCRecapitulatif";
    String PROCESS_DECISION_REFUS = "PCDecisionRefus";
    String PROCESS_DEMANDE_RENSEIGNEMENT = "PCDemandeRenseignement";
    String PROCESS_FORMULAIRE_COMMUNICATION_OCC = "PCFormulaireCommunicationOCC";
    String PROCESS_PLAN_CALCUL = "PCPlanDeCalcul";
    String PROCESS_TRANSFERT_DOSSIER_SANS_PC = "PCDemandeTransfertDossierSansPC";
    String PROCESS_TRANSFERT_DOSSIER_SUPPRESSION_ANNEXE_PC = "PCDemandeTransfertDossierSuppressionAnnexePC";
    String PROCESS_TRANSFERT_DOSSIER_SUPPRESSION_FICHE_VD_LAUSANNE = "PCDemandeTransfertDossierSuppressionFicheVdLsne";
    String PROCESS_TRANSFERT_DOSSIER_SUPPRESSION_PC = "PCDemandeTransfertDossierSuppressionPC";
    String PROCESS_TRANSFERT_RENTE_PC = "PCDemandeTransfertRente";
    /* variables */
    String STR_ID_DEMANDE_RENSEIGNEMENT = "idDemandeRenseignement";
    String STR_ID_PROCESS = "idProcess";
}
