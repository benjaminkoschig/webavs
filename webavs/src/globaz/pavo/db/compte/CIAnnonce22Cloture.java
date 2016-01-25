package globaz.pavo.db.compte;

import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;

/**
 * Wrapper de l'annonce 22. Date de création : (20.12.2002 09:51:15)
 * 
 * @author: Administrator
 */
public class CIAnnonce22Cloture extends CIAnnonceWrapper {
    // tiers de la caisse
    private String idCaisse;

    /**
     * Constructeur CIAnnonce22Cloture.
     * 
     * @param annonce
     *            l'annonce en suspens
     */
    public CIAnnonce22Cloture(CIAnnonceSuspens annonce) {
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
        contenuAnnonce.append(" / ");
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
     * Permet de savoir si le CI a été réouvert par un 67 ou un 81, dans ce cas là, on ne ferme pas le CI
     * 
     * @param transaction
     * @return
     * @throws Exception
     */
    public boolean isCiFermable(BTransaction transaction) throws Exception {
        CIRassemblementOuvertureManager rassMgr = new CIRassemblementOuvertureManager();
        rassMgr.setSession(getSession());
        rassMgr.setForCompteIndividuelId(compte.getCompteIndividuelId());
        rassMgr.setForTypeEnregistrement(CIRassemblementOuverture.CS_OUVERTURE);
        rassMgr.find(transaction);
        for (int i = 0; i < rassMgr.size(); i++) {
            CIRassemblementOuverture rass = (CIRassemblementOuverture) rassMgr.get(i);
            if ("67".equals(rass.getMotifArc().trim()) || "81".equals(rass.getMotifArc().trim())) {
                return false;
            }
        }
        return true;

    }

    /**
     * Traîte l'annonce 22 (clôture). Date de création : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param testFinal
     *            doit être à true pour signifier si les tests doivent être effectués.
     */
    @Override
    public void traitementAnnonce(BTransaction transaction, boolean testFinal) throws Exception {
        // recherche ci au ra avec no avs / le Ci doit être ouvert
        compte = CICompteIndividuel.loadCI(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), transaction);
        if (compte == null || !compte.isCiOuvert().booleanValue()) {
            // n'existe pas. Recherche ci au ra avec no avs avant le 1.7.72
            // note -> envoi email erreur
            // compte =
            // CICompteIndividuel.loadCI(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972),
            // transaction);
        }
        boolean erreurCloture = false;
        if (compte != null) {
            if (!compte.isCiOuvert().booleanValue()) {
                // CI déjà clôturé -> erreur
                erreurCloture = true;
                // peut être quand-même ok pour la même caisse et une agence
                // différente
            }
            idCaisse = application.getAdministration(getSession(),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE),
                    new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();
            // modifier le ci
            checkAndUpdateCI(transaction);
            // caisse tenant CI
            String caisseTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI));
            String agenceTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI));
            // ci trouvé, recherche d'une éventuelle clôture
            String dateCloture = getDateBidouillee(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA));
            CIRassemblementOuvertureManager cloture = new CIRassemblementOuvertureManager();
            cloture.setSession(getSession());
            cloture.setForCompteIndividuelId(compte.getCompteIndividuelId());
            cloture.setForDateCloture(dateCloture);
            cloture.find(transaction);
            if (cloture.size() != 0) {
                for (int i = 0; i < cloture.size(); i++) {
                    // tester date révocation
                    CIRassemblementOuverture clo = (CIRassemblementOuverture) cloture.getEntity(i);
                    // Ignorer les clôtures communiquées lors de l'ouverture
                    // d'un CI
                    if (!clo.getTypeEnregistrementWA().equals(CIRassemblementOuverture.CS_CLOTURE_OUVERTURE)) {
                        if (clo.isCloture() && JAUtil.isStringEmpty(clo.getDateRevocation())) {
                            boolean clotureExistante = false;
                            // test caisse commettante
                            if (!clo.getCaisseCommettante().equals(idCaisse)) {
                                clotureExistante = true;
                            } else {
                                // test de la caisse tenant le CI
                                if (application.isAnnoncesWA()) {
                                    if (application.getProperty(CIApplication.CODE_CAISSE).equals(caisseTCI)) {
                                        if (clo.getTypeEnregistrement().length() > 3
                                                && clo.getTypeEnregistrement().substring(3, 4).equals(agenceTCI)) {
                                            // même agence
                                            clotureExistante = true;
                                        } else {
                                            // ok, autre agence. Ignorer le fait
                                            // que le CI est déjà clôturé
                                            erreurCloture = false;
                                        }
                                    } else {
                                        // autre que 26? ne devrait pas arriver
                                        clotureExistante = true;
                                    }
                                } else {
                                    if (application.getProperty(CIApplication.CODE_CAISSE).equals(caisseTCI)) {
                                        if (clo.getTypeEnregistrement().length() > 3
                                                && clo.getCaisseTenantCI().trim().equals(agenceTCI)) {
                                            // même agence
                                            clotureExistante = true;
                                        } else {
                                            // ok, autre agence. Ignorer le fait
                                            // que le CI est déjà clôturé
                                            erreurCloture = false;
                                        }
                                    } else {
                                        if (CIUtil.isCaisseDifferente(getSession())) {
                                            if (clo.getTypeEnregistrement().length() > 3
                                                    && clo.getRealCaisse().trim().equals(caisseTCI)) {
                                                if (clo.getTypeEnregistrement().length() > 3
                                                        && clo.getCaisseTenantCI().trim().equals(agenceTCI)) {
                                                    // même agence
                                                    clotureExistante = true;
                                                } else {
                                                    // ok, autre agence. Ignorer
                                                    // le fait que le CI est
                                                    // déjà clôturé
                                                    erreurCloture = false;
                                                }
                                            } else {
                                                // ok, autre agence. Ignorer le
                                                // fait que le CI est déjà
                                                // clôturé
                                                erreurCloture = false;
                                            }
                                        }
                                    }
                                }
                            }
                            if (clotureExistante) {
                                // 07.01.04 ne rien faire: une clôture à la même
                                // date est possible!
                                /*
                                 * // déjà une clôture présente -> envoi email au responsable ci // envoi email String
                                 * message = java.text.MessageFormat.format( getSession
                                 * ().getLabel("MSG_ANNONCE_22_EMAIL_MESSAGE"), new Object[] {
                                 * remoteAnnonce.getField(IHEAnnoncesViewBean .NUMERO_ASSURE), getContenuAnnonce()});
                                 * String to = application.getEMailResponsableCI( transaction); envoiEmail(to,
                                 * getSession().getLabel ("MSG_ANNONCE_22_EMAIL_SUJET"), message); // log
                                 * createLog(transaction, getSession().getLabel ("MSG_ANNONCE_22_EMAIL_SUJET")); //
                                 * modification annonce //annonceSuspens.setNumeroCaisse( //
                                 * remoteAnnonce.getField(IHEAnnoncesViewBean. NUMERO_CAISSE_COMMETTANTE) // + "." // +
                                 * remoteAnnonce.getField(IHEAnnoncesViewBean. NUMERO_AGENCE_COMMETTANTE)); // mettre
                                 * l'annonce en suspens suspendreAnnonce(transaction); // valider la suspension
                                 * transaction.commit(); return;
                                 */
                            }
                        }
                    }
                }
            }
            if (!isAnnonceSuspens() && !erreurCloture) {
                remoteAnnonceCompl = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
                remoteAnnonceCompl.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable" });
                remoteAnnonceCompl.setIdAnnonce(CIUtil.inc(remoteAnnonce.getIdAnnonce()));
                remoteAnnonceCompl.retrieve(remoteTransaction);
                // créer clôture
                CIRassemblementOuverture rCloture = new CIRassemblementOuverture();
                rCloture.setSession(getSession());
                rCloture.setDateOrdre(getDateBidouillee(remoteAnnonceCompl
                        .getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA)));
                // application.getCalendar().todayjjMMMMaaaa());
                if (application.isAnnoncesWA() && application.getProperty(CIApplication.CODE_CAISSE).equals(caisseTCI)) {
                    rCloture.setTypeEnregistrement(CIRassemblementOuverture.CS_CLOTURE.substring(0, 3)
                            + agenceTCI.trim() + CIRassemblementOuverture.CS_CLOTURE.substring(4));
                } else {
                    rCloture.setTypeEnregistrement(CIRassemblementOuverture.CS_CLOTURE);
                    // todo: ajout caisse tenant ci
                    rCloture.setCaisseTenantCI(agenceTCI.trim());
                    if (CIUtil.isCaisseDifferente(getSession())) {
                        rCloture.setRealCaisse(caisseTCI.trim());
                    }
                }
                rCloture.setDateCloture(dateCloture);
                rCloture.setMotifArc(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
                rCloture.setCaisseCommettante(idCaisse);
                rCloture.setReferenceInterne(remoteAnnonce
                        .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
                rCloture.setCompteIndividuelId(compte.getCompteIndividuelId());
                rCloture.add(transaction);
                // clôturer les écritures concernée
                // Si caisse fusionée, on ne cloture pas, donc on annonce vide
                ArrayList result = null;
                if (application.isCaisseFusion() && !isCaissePrincipale()) {
                    result = new ArrayList();
                } else {
                    result = rCloture.cloturerEcritures(transaction);
                }
                /*
                 * if(transaction.hasErrors()){ String mess = java.text.MessageFormat.format(
                 * getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL")+ " "+ compte.getNumeroAvs() ,new Object[]
                 * {transaction.getErrors().toString() + rCloture.getNumeroAvs()}); ArrayList tos =
                 * application.getEMailResponsableCI(transaction); envoiEmail(tos,
                 * getSession().getLabel("MSG_ANNONCE_EMAIL_SUJET"), mess);
                 * 
                 * }
                 */
                // annonces 38/39
                annonceExtraitCI(transaction, result, false);
                // effacement de l'annonce
                annonceTraitee(transaction);
            }
        }
        if (compte == null || erreurCloture) {
            // n'existe pas ou CI fermé, envoi message d'erreur
            String message = java.text.MessageFormat.format(getSession().getLabel("MSG_ANNONCE_22B_EMAIL_MESSAGE"),
                    new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), getContenuAnnonce() });
            ArrayList to = application.getEMailResponsableCI(transaction);
            envoiEmail(to, getSession().getLabel("MSG_ANNONCE_22B_EMAIL_SUJET"), message);
            // annule les modifications effectuées
            transaction.rollback();
            // log
            createLog(transaction, getSession().getLabel("MSG_ANNONCE_22B_EMAIL_SUJET"));
            // modification annonce
            // annonceSuspens.setNumeroCaisse(
            // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE)
            // + "."
            // +
            // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
            // mettre l'annonce en suspens
            suspendreAnnonce(transaction);
            transaction.commit();
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
        // modification: pas de mise à jour de l'en-tête pour les 22
        /*
         * // nomcompte.setNomPrenom(remoteAnnonce.getField(IHEOutputAnnonce. ETAT_NOMINATIF)); // no avs précédant
         * String result = remoteAnnonce.getField (IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972); if
         * (!JAUtil.isStringEmpty(result)) { compte.setNumeroAvsPrecedant(result); } // référence interne result =
         * remoteAnnonce .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE); if
         * (!JAUtil.isStringEmpty(result)) { compte.setReferenceInterne(result); }
         */
        // motif
        compte.setDernierMotifCloture(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
        // caisse
        if (!JAUtil.isStringEmpty(idCaisse)) {
            compte.setDerniereCaisseAgence("");
            compte.setDerniereCaisse(idCaisse);
        }
        // clôture
        String result = getDateBidouillee(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA));
        if (!JAUtil.isStringEmpty(result)) {
            compte.setDerniereCloture(result);
        }
        // compte clos
        // Modif 19.04.2006 suite comparaison, il ne faut pas clôturé si 67 ou
        // 81 comme motif d'ouverture
        if (isCiFermable(transaction)) {
            compte.setCiOuvert(new Boolean(false));
        } else {
            compte.setCiOuvert(new Boolean(true));
        }
    }
}
