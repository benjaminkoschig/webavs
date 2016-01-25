package ch.globaz.corvus.process.echeances.depassees;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.process.echeances.REListeEcheanceDocumentGenerator;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory.TypeAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;

public class REListeEcheancesDepasseesDocumentGenerator extends REListeEcheanceDocumentGenerator {

    private static final long serialVersionUID = -8221839141633807717L;

    public REListeEcheancesDepasseesDocumentGenerator(BSession session, String moisTraitement) throws Exception {
        this(session, moisTraitement, "");
    }

    public REListeEcheancesDepasseesDocumentGenerator(BSession session, String moisTraitement, String idGroupeLocalite)
            throws Exception {
        super(session, session.getLabel("TITRE_DOCUMENT_LISTE_ECHEANCES_DEPASSEES").replace("{moisCourant}",
                moisTraitement), moisTraitement, idGroupeLocalite, TypeAnalyseurEcheances.Ajournement,
                TypeAnalyseurEcheances.Echeance18ans, TypeAnalyseurEcheances.Echeance25ans,
                TypeAnalyseurEcheances.EcheanceEtudes, TypeAnalyseurEcheances.FemmeArrivantAgeAvs,
                TypeAnalyseurEcheances.HommeArrivantAgeAvs, TypeAnalyseurEcheances.EcheancesForcees,
                TypeAnalyseurEcheances.RenteDeVeuf);
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    protected List<REReponseModuleAnalyseEcheance> validerAjouterTiers(IREEcheances echeance,
            List<REReponseModuleAnalyseEcheance> reponses) {
        List<REReponseModuleAnalyseEcheance> reponsesATraiter = new ArrayList<REReponseModuleAnalyseEcheance>();

        // on ne prend que les motifs qui nous intéresse pour cette liste
        for (REReponseModuleAnalyseEcheance uneReponse : reponses) {
            switch (uneReponse.getMotif()) {
                case AjournementDepasse: // OK
                    // Inforom 483 suppression du motif : CertificatDeVie
                    // case CertificatDeVie:
                    // Inforom 483 ajout du motif RevocationDemandee (Ajournement)
                case AjournementRevocationDemandeeDepassee: // OK
                    // Inforom 483 ajout du motif EnqueteIntermediaire (EcheanceEtudes)
                case EnqueteIntermediaire: // OK
                    // case ConjointAgeAvsDepasse: // OK
                case Echeance25ansDepassee: // OK
                case EcheanceFinEtudesDepassees:
                    // Inforom 483 ajout du motif : EcheanceEtudesAucunePeriode
                case EcheanceEtudesAucunePeriode: // OK
                case FemmeAgeAvsDepasse: // OK
                case HommeAgeAvsDepasse: // OK
                    // Inforom 483 ajout du motif : FemmeArrivantAgeAvsRenteAnticipee
                case FemmeArrivantAgeAvsRenteAnticipee: // OK
                    // Inforom 483 ajout du motif : HommeArrivantAgeAvsRenteAnticipee
                case HommeArrivantAgeAvsRenteAnticipee: // OK
                case HommeAgeAvsAnticipationDepassee:
                case FemmeAgeAvsAnticipationDepassee:
                case RenteDeVeuf: // OK
                case RenteDeVeufSansEnfant: // OK
                case EcheanceForcee:
                    reponsesATraiter.add(uneReponse);
                    break;
            }
        }
        return reponsesATraiter;
    }
}
