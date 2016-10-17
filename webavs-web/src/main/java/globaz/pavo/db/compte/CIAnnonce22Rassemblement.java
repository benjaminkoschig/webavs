package globaz.pavo.db.compte;

import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Wrapper de l'annonce 22. Date de création : (20.12.2002 09:51:15)
 * 
 * @author: Administrator
 */
public class CIAnnonce22Rassemblement extends CIAnnonceWrapper {
    // tiers de la caisse
    private String idCaisse;

    /**
     * Constructeur CIAnnonce22Rassemblement.
     * 
     * @param annonce
     *            l'annonce en suspens
     */
    public CIAnnonce22Rassemblement(CIAnnonceSuspens annonce) {
        super(annonce);
    }

    /**
     * Retourne le contenu de l'annonce. Date de création : (23.12.2002 07:51:18)
     * 
     * @return le contenu de l'annonce.
     */
    private String getContenuAnnonce() throws Exception {
        StringBuilder contenuAnnonce = new StringBuilder();
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
     * Traîte l'annonce 22 (rassemblement). Date de création : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param testFinal
     *            doit être à true pour signifier si les tests doivent être effectués.
     */
    @Override
    public void traitementAnnonce(BTransaction transaction, boolean testFinal) throws Exception {
        // recherche ci au ra avec no avs
        compte = CICompteIndividuel.loadCI(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), transaction);

        if (compte != null) {
            idCaisse = application.getAdministration(getSession(),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE),
                    new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();
            // modifier le ci
            checkAndUpdateCI(transaction);
            // 02
            remoteAnnonceCompl = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteAnnonceCompl.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable" });
            remoteAnnonceCompl.setIdAnnonce(CIUtil.inc(remoteAnnonce.getIdAnnonce()));
            remoteAnnonceCompl.retrieve(remoteTransaction);
            // caisse tenante
            String caisseTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI));
            String agenceTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI));
            // recherche si le rassemblement a déjà été effectué
            CIRassemblementOuvertureManager rasMgr = new CIRassemblementOuvertureManager();
            rasMgr.setSession(getSession());
            rasMgr.setForCompteIndividuelId(compte.getCompteIndividuelId());
            rasMgr.setForMotifArc(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            rasMgr.setForDateOrdre(getDateBidouillee(remoteAnnonceCompl.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA)));
            rasMgr.setForNotInTypeEnregistrement(Arrays.asList(CIRassemblementOuverture.CS_EXTRAIT,
                    CIRassemblementOuverture.CS_SAISIE_MANUELLE));
            rasMgr.find(transaction);
            ArrayList result = new ArrayList();

            boolean dejaTraitee = false;
            // modif jmc 13.07.2006, si caisse fusionnée, on va regarder si c'est la caisse principale. Comme ça, on
            // sait si on doit répondre vide ou pas
            if (!application.isCaisseFusion()) {
                if (rasMgr.size() != 0) {
                    for (int i = 0; i < rasMgr.size(); i++) {
                        CIRassemblementOuverture ras = (CIRassemblementOuverture) rasMgr.getEntity(i);
                        // test caisse commettante
                        if (ras.getCaisseCommettante().equals(idCaisse)) {
                            dejaTraitee = true;
                            break;
                        }
                    }
                }
            } else if (!isCaissePrincipale()) {
                // avec le flag déjà traité, on renvoie vide pour la caisse fusionnée
                dejaTraitee = true;
            }

            // ajouter l'annonce
            CIRassemblementOuverture rassembl = new CIRassemblementOuverture();
            rassembl.setSession(getSession());
            rassembl.setDateOrdre(getDateBidouillee(remoteAnnonceCompl.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA)));

            if (application.isAnnoncesWA() && application.getProperty(CIApplication.CODE_CAISSE).equals(caisseTCI)) {
                rassembl.setTypeEnregistrement(CIRassemblementOuverture.CS_RASSEMBLEMENT.substring(0, 3)
                        + agenceTCI.trim() + CIRassemblementOuverture.CS_RASSEMBLEMENT.substring(4));
            } else {
                rassembl.setTypeEnregistrement(CIRassemblementOuverture.CS_RASSEMBLEMENT);
                rassembl.setCaisseTenantCI(agenceTCI.trim());

                if (CIUtil.isCaisseDifferente(getSession())) {
                    rassembl.setRealCaisse(caisseTCI.trim());
                }
            }

            rassembl.setMotifArc(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            rassembl.setCaisseCommettante(idCaisse);
            rassembl.setReferenceInterne(remoteAnnonce
                    .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
            rassembl.setCompteIndividuelId(compte.getCompteIndividuelId());
            rassembl.add(transaction);

            if (!dejaTraitee) {
                // rassembler les écritures actives
                result = rassembl.rassemblerEcritures(transaction);
            }

            // annonce du ci (vide si déjà traitée pour une autre agence)
            annonceExtraitCI(transaction, result, false);

            // effacement de l'annonce
            annonceTraitee(transaction);
        } else {

            // n'existe pas non plus, envoi message d'erreur
            String message = java.text.MessageFormat.format(getSession().getLabel("MSG_ANNONCE_22R_EMAIL_MESSAGE"),
                    new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), getContenuAnnonce() });
            ArrayList<String> to = application.getEMailResponsableCI(transaction);
            envoiEmail(to, getSession().getLabel("MSG_ANNONCE_22R_EMAIL_SUJET"), message);

            // annule les modifications effectuées
            transaction.rollback();
            createLog(transaction, getSession().getLabel("MSG_ANNONCE_22R_EMAIL_SUJET"));

            // mettre l'annonce en suspens
            suspendreAnnonce(transaction);
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
        // Rien faire
    }
}
