package ch.globaz.perseus.process.traitementAnnuel;

import globaz.jade.context.JadeThread;

public enum PFProcessTraitementAnnuelEnum {
    /*
     * ADRESSE_MAIL_CCVD, ADRESSE_MAIL_AGLAU, MOIS et TEXTE_DECISION no sont pas internationalisés dans perseusbusiness
     * Les suivants oui parce que, quand on clique sur l'entité, les informations concernant l'entitées sont
     * enregistrés.
     */
    ADRESSE_MAIL_CCVD("perseus.process.traitementAdaptation.adressMailCCVD"),
    ADRESSE_MAIL_AGLAU("perseus.traitementAdaptation.adressMailAGLAU"),
    MOIS("perseus.traitementAdaptation.mois"),
    TEXTE_DECISION("perseus.traitementAdaptation.decision"),
    ID_DECISION("perseus.traitementAdaptation.idNewDecision"),
    ID_DEMANDE_COPIE("perseus.traitementAdaptation.idDemande"),
    CS_TYPE_DECISION("perseus.traitementAdaptation.typeDecision"),
    DECISION_IMPRIMER("perseus.traitementAdaptation.imprimerDecision"),
    TYPE_POPULATION("perseus.traitementAdaptation.typePopulation"),
    DECISION_IMPRIMES("perseus.traitementAdaptation.decisionsImprimees"),
    DEMANDE_FERME("perseus.traitementAdaptation.demandesFermees"),
    CAS_AVEC_RETENU("perseus.traitementAdaptation.casAvecRetenu"),
    OCTROI("perseus.traitementAdaptation.octroi"),
    PARTIEL("perseus.traitementAdaptation.partiel"),
    CAS_NON_CALCULABLE("perseus.traitementAnnuel.casNonCalculable");

    private final String idLabel;

    PFProcessTraitementAnnuelEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}
