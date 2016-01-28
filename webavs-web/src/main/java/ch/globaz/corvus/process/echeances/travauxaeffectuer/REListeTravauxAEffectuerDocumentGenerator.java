package ch.globaz.corvus.process.echeances.travauxaeffectuer;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.process.echeances.REListeEcheanceDocumentGenerator;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory.TypeAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;

public class REListeTravauxAEffectuerDocumentGenerator extends REListeEcheanceDocumentGenerator {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean isAjournement;
    private boolean isAutresEcheance;
    private boolean isCertificatDeVie;
    private boolean isEcheanceEtude;
    private boolean isEnfantDe18ans;
    private boolean isEnfantDe25ans;
    private boolean isFemmeArrivantAgeVieillesse;
    private boolean isHommeArrivantAgeVieillesse;
    private boolean isRenteDeVeuf;

    private List<REReponseModuleAnalyseEcheance> reponsesATraiter;

    public REListeTravauxAEffectuerDocumentGenerator(BSession session, String moisTraitement) throws Exception {
        this(session, moisTraitement, "");
    }

    public REListeTravauxAEffectuerDocumentGenerator(BSession session, String moisTraitement, String idGroupeLocalite)
            throws Exception {
        super(session, session.getLabel("TITRE_DOCUMENT_LISTE_TRAVAUX_A_EFFECTUER").replace("{moisCourant}",
                moisTraitement), moisTraitement, idGroupeLocalite, TypeAnalyseurEcheances.Ajournement,
                TypeAnalyseurEcheances.Echeance18ans, TypeAnalyseurEcheances.Echeance25ans,
                TypeAnalyseurEcheances.EcheanceEtudes, TypeAnalyseurEcheances.FemmeArrivantAgeAvs,
                TypeAnalyseurEcheances.HommeArrivantAgeAvs, TypeAnalyseurEcheances.RenteDeVeuf,
                TypeAnalyseurEcheances.CertificatDeVie);
    }

    public final void setAjournement(boolean isAjournement) {
        this.isAjournement = isAjournement;
    }

    public final void setAutresEcheance(boolean isAutresEcheance) {
        this.isAutresEcheance = isAutresEcheance;
    }

    public final void setCertificatDeVie(boolean isCertificatDeVie) {
        this.isCertificatDeVie = isCertificatDeVie;
    }

    public final void setEcheanceEtude(boolean isEcheanceEtude) {
        this.isEcheanceEtude = isEcheanceEtude;
    }

    public final void setEnfantDe18ans(boolean isEnfantDe18ans) {
        this.isEnfantDe18ans = isEnfantDe18ans;
    }

    public final void setEnfantDe25ans(boolean isEnfantDe25ans) {
        this.isEnfantDe25ans = isEnfantDe25ans;
    }

    public final void setFemmeArrivantAgeVieillesse(boolean isFemmeArrivantAgeVieillesse) {
        this.isFemmeArrivantAgeVieillesse = isFemmeArrivantAgeVieillesse;
    }

    public final void setHommeArrivantAgeVieillesse(boolean isHommeArrivantAgeVieillesse) {
        this.isHommeArrivantAgeVieillesse = isHommeArrivantAgeVieillesse;
    }

    public final void setRenteDeVeuf(boolean isRenteDeVeuf) {
        this.isRenteDeVeuf = isRenteDeVeuf;
    }

    @Override
    protected List<REReponseModuleAnalyseEcheance> validerAjouterTiers(IREEcheances echeance,
            List<REReponseModuleAnalyseEcheance> reponses) {
        reponsesATraiter = new ArrayList<REReponseModuleAnalyseEcheance>();

        // on ne prend que les motifs qui nous intéresse pour cette liste
        for (REReponseModuleAnalyseEcheance uneReponse : reponses) {
            switch (uneReponse.getMotif()) {

                case Ajournement:
                case AjournementRevocationDemandee:
                    if (isAjournement) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;

                case CertificatDeVie:
                    if (isCertificatDeVie) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;

                case Echeance18ans:
                    if (isEnfantDe18ans) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;

                case Echeance25ans:
                    if (isEnfantDe25ans) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;

                case EcheanceFinEtudes:
                case EnqueteIntermediaire:
                    if (isEcheanceEtude) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;

                case FemmeArrivantAgeAvs:
                case FemmeArrivantAgeAvsAvecApiAi:
                case FemmeArrivantAgeAvsRenteAnticipee:
                    if (isFemmeArrivantAgeVieillesse) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;

                case HommeArrivantAgeAvs:
                case HommeArrivantAgeAvsAvecApiAi:
                case HommeArrivantAgeAvsRenteAnticipee:
                    if (isHommeArrivantAgeVieillesse) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;
                case ConjointArrivantAgeAvs:
                    if (isHommeArrivantAgeVieillesse || isFemmeArrivantAgeVieillesse) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;
                case RenteDeVeuf:
                case RenteDeVeufSansEnfant:
                    if (isRenteDeVeuf) {
                        reponsesATraiter.add(uneReponse);
                    }
                    break;

                default:
                    break;
            }
        }
        return reponsesATraiter;
    }
}
