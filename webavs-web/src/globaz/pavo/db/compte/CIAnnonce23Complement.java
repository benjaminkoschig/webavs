package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;

/**
 * Wrapper de l'annonce 23. Date de cr�ation : (20.12.2002 09:51:15)
 * 
 * @author: Administrator
 */
public class CIAnnonce23Complement extends CIAnnonceWrapper {
    /**
     * Constructeur CIAnnonce23Complement.
     * 
     * @param annonce
     *            l'annonce en suspens
     */
    public CIAnnonce23Complement(CIAnnonceSuspens annonce) {
        super(annonce);
    }

    /**
     * Retourne le contenu de l'annonce. Date de cr�ation : (23.12.2002 07:51:18)
     * 
     * @return le contenu de l'annonce.
     */
    private String getContenuAnnonce() throws Exception {
        StringBuffer contenuAnnonce = new StringBuffer();
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_AVS"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE));
        contenuAnnonce.append(" ou ");
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_A_COMPLETER));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_NOM"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
        contenuAnnonce.append("\n");
        return contenuAnnonce.toString();
    }

    @Override
    public boolean isCaissePrincipale() throws Exception {
        if (remoteAnnonce == null) {
            remoteAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteAnnonce.setIdAnnonce(annonceSuspens.getIdAnnonce());
            remoteAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable", "getUtilisateur" });
            remoteAnnonce.retrieve(remoteTransaction);
        }

        if (CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE)).equals(
                application.getProperty(CIApplication.CODE_AGENCE))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tra�te l'annonce 23. Date de cr�ation : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction � utiliser
     * @param testFinal
     *            doit �tre � true pour signifier si les tests doivent �tre effectu�s.
     */
    @Override
    public void traitementAnnonce(BTransaction transaction, boolean testFinal) throws Exception {
        if (application.isCaisseFusion() && !isCaissePrincipale()) {
            annonceTraitee(transaction);
            return;
        }
        // recherche ci au ra avec no avs compl�t�
        compte = CICompteIndividuel.loadCI(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE),
                transaction);
        // recherche si CI existe avec ancien num�ros
        CICompteIndividuel ancienCompteACompl = null;
        if (!JAUtil.isStringEmpty(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_A_COMPLETER))) {
            ancienCompteACompl = CICompteIndividuel.loadCI(
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_A_COMPLETER), transaction);
        }
        CICompteIndividuel ancienCompteAnt = null;
        if (!JAUtil.isStringEmpty(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_ANTERIEUR))) {
            ancienCompteAnt = CICompteIndividuel.loadCI(
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_ANTERIEUR), transaction);
        }
        if (compte == null) {
            // n'existe pas, no avs � compl�ter
            compte = ancienCompteACompl;
            if (compte == null) {
                // n'existe pas, no avs ant�rieur
                compte = ancienCompteAnt;
            } else {
                // liaison si no ant�rieur existe
                if (ancienCompteAnt != null) {
                    // Modif. bug centrale
                    if (!NSUtil.unFormatAVS(ancienCompteAnt.getNumeroAvs()).equals(
                            NSUtil.unFormatAVS(compte.getNumeroAvs()))) {
                        compte.addLiaison(transaction, ancienCompteAnt, false);
                    }
                }
            }
        } else {
            // liaison si no ant�rieur et/ou no � compl�ter existent
            if (ancienCompteAnt != null) {
                if (!NSUtil.unFormatAVS(ancienCompteAnt.getNumeroAvs()).equals(
                        NSUtil.unFormatAVS(compte.getNumeroAvs()))) {
                    compte.addLiaison(transaction, ancienCompteAnt, false);
                }
            }
            if (ancienCompteACompl != null) {
                if (!NSUtil.unFormatAVS(ancienCompteACompl.getNumeroAvs()).equals(
                        NSUtil.unFormatAVS(compte.getNumeroAvs()))) {
                    compte.addLiaison(transaction, ancienCompteACompl, false);
                }
            }
        }
        if (compte != null) {
            // ci trouv�, mise � jour
            checkAndUpdateCI(transaction);
            // effacement de l'annonce
            annonceTraitee(transaction);
        } else {
            // n'existe pas non plus, envoi message d'erreur
            String message = java.text.MessageFormat.format(getSession().getLabel("MSG_ANNONCE_23_EMAIL_MESSAGE"),
                    new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE).trim(),
                            remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_A_COMPLETER).trim(),
                            getContenuAnnonce() });
            ArrayList to = application.getEMailResponsableCI(transaction);
            envoiEmail(to, getSession().getLabel("MSG_ANNONCE_23_EMAIL_SUJET"), message);
            // annule les modifications effectu�es
            transaction.rollback();
            // log
            createLog(transaction, getSession().getLabel("MSG_ANNONCE_23_EMAIL_SUJET"));
            // mettre l'annonce en suspens
            suspendreAnnonce(transaction);
        }
    }

    /**
     * Modifie l'�tat du CI avec copie si n�cessaire.<br>
     * Note: cette m�thode est appel�e par la super-classe.
     * 
     * @param transaction
     *            la transaction � utiliser. Date de cr�ation : (27.11.2002 14:24:57)
     */
    @Override
    public void updateCI(BTransaction transaction) throws Exception {
        // nom
        compte.setNomPrenom(checkAndSet(compte.getNomPrenom(),
                remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF)));
        // pays
        compte.setPaysOrigineId(checkAndSet(compte.getPaysOrigineId(),
                "315" + remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_ORIGINE)));
        // no avs
        compte.setNumeroAvs(checkAndSet(compte.getNumeroAvs(),
                remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE)));
        // no avs pr�c�dant
        String result = remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_ANTERIEUR);
        if (!JAUtil.isStringEmpty(result)) {
            compte.setNumeroAvsPrecedant(result);
        }
    }
}
