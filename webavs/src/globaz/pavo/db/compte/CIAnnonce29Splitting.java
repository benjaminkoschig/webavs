package globaz.pavo.db.compte;

import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.pavo.db.splitting.CIMandatSplitting;
import java.util.ArrayList;

/**
 * Wrapper de l'annonce 29. Date de création : (20.12.2002 09:51:15)
 * 
 * @author: Administrator
 */
public class CIAnnonce29Splitting extends CIAnnonceWrapper {
    /**
     * Constructeur CIAnnonce29Splitting.
     * 
     * @param annonce
     *            l'annonce en suspens
     */
    public CIAnnonce29Splitting(CIAnnonceSuspens annonce) {
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
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_PARTENAIRE"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_PARTENAIRE));
        return contenuAnnonce.toString();
    }

    /**
     * Traîte l'annonce 29 (splitting). Date de création : (20.12.2002 09:48:45)
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
            compte = CICompteIndividuel.loadCI(
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972), transaction);
        }
        boolean found = false;
        if (compte != null) {
            // modifier le ci
            checkAndUpdateCI(transaction);
            // recherche des périodes de splitting
            CIPeriodeSplittingManager periodes = new CIPeriodeSplittingManager();
            periodes.setSession(getSession());
            periodes.setForCompteIndividuelId(compte.getCompteIndividuelId());
            periodes.setForPartenaireNumeroAvs(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_PARTENAIRE));
            periodes.find(transaction);
            // On révoque et on avertit
            if (compte.isProvenanceFusion().booleanValue()) {
                String message = java.text.MessageFormat
                        .format(getSession().getLabel("MSG_ANNONCE_29_SP_EMAIL_MESSAGE_FUSION"), new Object[] {
                                remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), getContenuAnnonce() });
                ArrayList to = application.getEMailResponsableCI(transaction);
                envoiEmail(to, getSession().getLabel("MSG_CAISSE_FUS"), message);
                // log

            }
            for (int j = 0; j < periodes.size(); j++) {
                // modification des périodes
                CIPeriodeSplitting periode = (CIPeriodeSplitting) periodes.getEntity(j);
                // Ne pas prendre en compte les mandats manuels
                if (JAUtil.isStringEmpty(periode.getDateRevocation())) {
                    if (CIMandatSplitting.CS_MANDAT_NORMAL.equals(periode.getParticulier())
                            || CIMandatSplitting.CS_ANNEE_JEUNESSE.equals(periode.getParticulier())
                            || CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE.equals(periode.getParticulier())
                            || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT.equals(periode.getParticulier())) {
                        periode.setDateRevocation(annonceSuspens.getDateReception());
                        periode.revoquerEcrituresSplitting(transaction);
                        periode.wantCallMethodBefore(false);
                        periode.update(transaction);
                        found = true;
                    }
                }
            }
            if (found) {
                // recherche du 95 -> impossible pour l'instant
                // todo lien entre CIRAOUP et CISPLIP
            }
            // annonce ok, même si aucune période trouvée
            annonceTraitee(transaction);
        } else {
            if (testFinal) {
                // n'existe pas, envoi message d'erreur
                String message = java.text.MessageFormat
                        .format(getSession().getLabel("MSG_ANNONCE_29_SP_EMAIL_MESSAGE"),
                                new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE),
                                        getContenuAnnonce() });
                ArrayList to = application.getEMailResponsableCI(transaction);
                envoiEmail(to, getSession().getLabel("MSG_ANNONCE_29_SP_EMAIL_SUJET"), message);
                // annule les modifications effectuées
                transaction.rollback();
                // log
                createLog(transaction, getSession().getLabel("MSG_ANNONCE_29_SP_EMAIL_SUJET"));
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
    }
}
