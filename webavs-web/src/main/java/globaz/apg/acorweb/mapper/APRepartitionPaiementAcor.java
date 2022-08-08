package globaz.apg.acorweb.mapper;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.tools.PRSession;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public class APRepartitionPaiementAcor {
    final static String CONST_TYPE_AFFILI_EMPLOY = "[1]";
    final static String CONST_TYPE_AFFILI_EMPLOY_D_F = "[4]";
    final static String CONST_TYPE_AFFILI_INDEP = "[3]";
    final static String CONST_TYPE_AFFILI_INDEP_EMPLOY = "[2]";
    final static String CONST_TYPE_AFFILI_LTN = "[5]";
    final static String CONST_TYPE_AFFILI_TSE = "[6]";
    final static String CONST_TYPE_AFFILI_TSE_VOLONTAIRE = "[7]";

    @Getter
    String idTiers;
    @Getter
    String idAffilie;
    @Getter
    String numeroAffilieEmployeur;
    @Getter
    String nomEmployeur;
    @Getter
    String cantonImposition;
    @Getter
    @Setter
    FWCurrency montantNet;
    @Getter
    @Setter
    FWCurrency salaireJournalier;
    @Getter
    @Setter
    boolean isVersementEmployeur;
    @Getter
    @Setter
    boolean isIndependant;
    @Getter
    @Setter
    boolean isTravailleurSansEmployeur;
    @Getter
    @Setter
    boolean isCollaborateurAgricole;
    @Getter
    @Setter
    boolean isTravailleurAgricole;
    @Getter
    @Setter
    boolean isSoumisCotisation;
    @Getter
    @Setter
    String idSituationProfessionnelle;

    public APRepartitionPaiementAcor(BSession session, String noAffilie, String nom, String cantonImpot) throws PRACORException {
        this.numeroAffilieEmployeur = noAffilie;
        this.nomEmployeur = nom;
        this.cantonImposition = cantonImpot;

        // stocker les id pour les taux journaliers
        if (PRAbstractEmployeur.isNumeroBidon(noAffilie)) {
            idAffilie = "0"; // sauve dans la base puis recharge, donc 0
            idTiers = PRAbstractEmployeur.extractIdTiers(noAffilie);
        } else {
            try {
                IPRAffilie affilie = getIprAffilie(session, noAffilie, nom);

                idAffilie = affilie.getIdAffilie();
                idTiers = affilie.getIdTiers();
            } catch (Exception e) {
                throw new PRACORException("Impossible de trouver l'affilie", e);
            }
        }
    }

    public static IPRAffilie getIprAffilie(BSession session, String noAffilie, String nom) throws Exception {
        AFAffiliationManager mgr = new AFAffiliationManager();
        mgr.setSession((BSession) PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
        mgr.setForAffilieNumero(noAffilie);
        IPRAffilie affilie;

        if (nom.startsWith(CONST_TYPE_AFFILI_INDEP)) {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    IAFAffiliation.TYPE_AFFILI_INDEP);
        } else if (nom.startsWith(CONST_TYPE_AFFILI_INDEP_EMPLOY)) {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY);
        } else if (nom.startsWith(CONST_TYPE_AFFILI_EMPLOY)) {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    IAFAffiliation.TYPE_AFFILI_EMPLOY);
        } else if (nom.startsWith(CONST_TYPE_AFFILI_EMPLOY_D_F)) {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    IAFAffiliation.TYPE_AFFILI_EMPLOY_D_F);
        } else if (nom.startsWith(CONST_TYPE_AFFILI_LTN)) {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    IAFAffiliation.TYPE_AFFILI_LTN);
        } else if (nom.startsWith(CONST_TYPE_AFFILI_TSE)) {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    IAFAffiliation.TYPE_AFFILI_TSE);
        } else if (nom.startsWith(CONST_TYPE_AFFILI_TSE_VOLONTAIRE)) {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    IAFAffiliation.TYPE_AFFILI_TSE_VOLONTAIRE);
        } else {
            affilie = PRAffiliationHelper.getEmployeurParNumAffilie(session, noAffilie);
        }
        return affilie;
    }

}
