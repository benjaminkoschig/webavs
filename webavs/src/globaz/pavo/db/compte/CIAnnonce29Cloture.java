package globaz.pavo.db.compte;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import java.util.ArrayList;

/**
 * Wrapper de l'annonce 29. Date de création : (20.12.2002 09:51:15)
 * 
 * @author: Administrator
 */
public class CIAnnonce29Cloture extends CIAnnonceWrapper {
    /**
     * Constructeur CIAnnonce29Cloture.
     * 
     * @param annonce
     *            l'annonce en suspens
     */
    public CIAnnonce29Cloture(CIAnnonceSuspens annonce) {
        super(annonce);
    }

    /**
     * Retourne le contenu de l'annonce. Date de création : (23.12.2002 07:51:18)
     * 
     * @return le contenu de l'annonce.
     */
    private String getContenuAnnonce() throws Exception {
        StringBuffer contenuAnnonce = new StringBuffer();
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_AVS"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
        contenuAnnonce.append(" ou ");
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_NOM"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_MOTIF"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_DATE"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_CLOTURE_OU_ORDRE_SPLITTING_MMAA));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_CAISSE"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_AGENCE"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_REF"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
        return contenuAnnonce.toString();
    }

    /**
     * Traîte l'annonce 29 (clôture). Date de création : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param testFinal
     *            doit être à true pour signifier si les tests doivent être effectués.
     */
    @Override
    public void traitementAnnonce(BTransaction transaction, boolean testFinal) throws Exception {
        compte = CICompteIndividuel.loadCI(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), transaction);
        if (compte == null) {
            // n'existe pas. Recherche ci au ra avec no avs avant le 1.7.72
            // note -> envoi email erreur
            // compte =
            // CICompteIndividuel.loadCI(
            // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972),
            // transaction);
        }
        if (compte != null) {
            if (compte.isProvenanceFusion().booleanValue()) {
                String message = java.text.MessageFormat.format(
                        getSession().getLabel("MSG_ANNONCE_29_CL_EMAIL_MESSAGE_FUSION") + " " + compte.getNumeroAvs(),
                        new Object[] { compte.getNumeroAvs(), getContenuAnnonce() });
                ArrayList to = application.getEMailResponsableCI(transaction);
                this.envoiEmail(to, getSession().getLabel("MSG_CAISSE_FUS"), message);
                // log

            }
        }
        boolean found = false;
        if (compte != null) {
            // 07.01.04 possible aussi si le CI est ouvert (exemple 85) &&
            // !compte.isCiOuvert().booleanValue()) {
            // modifier le ci
            checkAndUpdateCI(transaction);
            // recherche du rassemblement
            CIRassemblementOuvertureManager clotures = new CIRassemblementOuvertureManager();
            clotures.setSession(getSession());
            clotures.setForCompteIndividuelId(compte.getCompteIndividuelId());
            clotures.setForDateCloture(getDateBidouillee(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA)));
            // clotures.setForMotifArc(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            clotures.find(transaction);
            for (int cl = 0; cl < clotures.size(); cl++) {
                // modification de la clôture et les éventuels ci additionnels
                CIRassemblementOuverture cloture = (CIRassemblementOuverture) clotures.getEntity(cl);
                if (CIRassemblementOuverture.CS_CI_ADDITIONNEL.equals(cloture.getTypeEnregistrementWA())
                        || CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.equals(cloture.getTypeEnregistrementWA())
                        || CIRassemblementOuverture.CS_CLOTURE.equals(cloture.getTypeEnregistrementWA())
                        || CIRassemblementOuverture.CS_CLOTURE_OUVERTURE.equals(cloture.getTypeEnregistrementWA())) {
                    //
                    if (JAUtil.isStringEmpty(cloture.getDateRevocation())) {
                        cloture.setDateRevocation(annonceSuspens.getDateReception());
                        cloture.wantCallMethodBefore(false);
                        cloture.update(transaction);
                        // modification des écritures
                        CIEcritureManager ecritures = new CIEcritureManager();
                        ecritures.setSession(getSession());
                        ecritures.setForCompteIndividuelId(compte.getCompteIndividuelId());
                        ecritures.setForRassemblementOuvertureId(cloture.getRassemblementOuvertureId());
                        // ecritures.setForIdTypeCompte(CIEcriture.CS_CI);
                        ecritures.find(transaction, BManager.SIZE_NOLIMIT);
                        for (int i = 0; i < ecritures.size(); i++) {
                            CIEcriture ecriture = (CIEcriture) ecritures.getEntity(i);
                            ecriture.setRassemblementOuvertureId("");
                            ecriture.setDateCiAdditionnel("");
                            ecriture.simpleUpdate(transaction);
                        }
                        found = true;
                    }
                }
            }
            CIEcritureManager ecrituresSuspens = new CIEcritureManager();
            ecrituresSuspens.setForCompteIndividuelId(compte.getCompteIndividuelId());
            ecrituresSuspens.setForIdTypeCompte(CIEcriture.CS_CI_SUSPENS);
            ecrituresSuspens.setSession(getSession());
            ecrituresSuspens.find(transaction);
            CIEcriture ecr = new CIEcriture();
            // mettre les supsens en active
            for (int i = 0; i < ecrituresSuspens.size(); i++) {
                ecr = (CIEcriture) ecrituresSuspens.getEntity(i);
                ecr.setIdTypeCompte(CIEcriture.CS_CI);
                ecr.simpleUpdate(transaction);
            }
            CIEcritureManager ecrituresTempSus = new CIEcritureManager();
            ecrituresTempSus.setSession(getSession());
            ecrituresTempSus.setForCompteIndividuelId(compte.getCompteIndividuelId());
            ecrituresTempSus.setForIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
            ecrituresTempSus.find(transaction);
            // mettre les temporaires suspens en temporaire
            for (int i = 0; i < ecrituresTempSus.size(); i++) {
                ecr = (CIEcriture) ecrituresTempSus.getEntity(i);
                ecr.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                ecr.simpleUpdate(transaction);
            }
            // effacement de l'annonce
            annonceTraitee(transaction);
        } else {
            if (testFinal) {
                // n'existe pas, envoi message d'erreur
                String message = java.text.MessageFormat
                        .format(getSession().getLabel("MSG_ANNONCE_29_CL_EMAIL_MESSAGE"),
                                new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE),
                                        getContenuAnnonce() });
                ArrayList to = application.getEMailResponsableCI(transaction);
                this.envoiEmail(to, getSession().getLabel("MSG_ANNONCE_29_CL_EMAIL_SUJET"), message);
                // annule les modifications effectuées
                transaction.rollback();
                // log
                createLog(transaction, getSession().getLabel("MSG_ANNONCE_29_CL_EMAIL_SUJET"));
                // modification annonce
                // annonceSuspens.setNumeroCaisse(
                // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE)
                // + "."
                // +
                // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
                // mettre l'annonce en suspens
                suspendreAnnonce(transaction);
            }
        }
    }

    /**
     * Modifie l'état du CI avec copie si nécessaire.<br>
     * Note: cette méthode est appelée par la super-classe.
     * 
     * @param transaction
     *            la transaction à utiliser. Date de création : (27.11.2002 14:24:57)
     */
    @Override
    public void updateCI(BTransaction transaction) throws Exception {
        // modification: pas de mise à jour de l'en-tête pour les 29
        /*
         * // nom compte.setNomPrenom(checkAndSet(compte.getNomPrenom(),
         * remoteAnnonce.getField(IHEOutputAnnonce.ETAT_NOMINATIF))); // no avs précédant String result =
         * remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972 ); if (!JAUtil.isStringEmpty(result))
         * { compte.setNumeroAvsPrecedant(result); } // référence interne result = remoteAnnonce
         * .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE); if (!JAUtil.isStringEmpty(result)) {
         * compte.setReferenceInterne(result); }
         */
        // caisse - mise à zéro
        compte.setDerniereCaisse("");
        compte.setDerniereCaisseAgence("");
        // clôture - mise à zéro
        compte.setDerniereCloture("");
        // motif clôture
        compte.setDernierMotifCloture("");
        // compte ouvert
        compte.setCiOuvert(new Boolean(true));
    }
}
