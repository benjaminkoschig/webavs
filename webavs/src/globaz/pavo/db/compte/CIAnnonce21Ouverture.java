package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;

/**
 * Wrapper de l'annonce 21. Date de création : (20.12.2002 09:51:15)
 * 
 * @author: Administrator
 */
public class CIAnnonce21Ouverture extends CIAnnonceWrapper {
    // tiers de la caisse
    private String idCaisse;
    // motif RCI
    private String motifRCI = null;

    /**
     * Constructeur CIAnnonce21Ouverture.
     * 
     * @param annonce
     *            l'annonce en suspens
     */
    public CIAnnonce21Ouverture(CIAnnonceSuspens annonce) {
        super(annonce);
    }

    private boolean checkIsParitaire(String numeroAffilie, BTransaction transaction) throws Exception {

        boolean IsParitaire = false;

        if (numeroAffilie == null) {
            transaction.addErrors("Affiliation number is null");
        } else {
            // On récupère l'affiliation en tant qu'objet grace au numéro d'affiliation
            AFAffiliation affiliation = retrieveAffiliation(numeroAffilie, transaction);

            // récupérer NSS du tiers affilié
            String numeroNSSAffiliation = NSUtil.unFormatAVS(affiliation.getTiers().getNumAvsActuel());
            String numeroNSSAnnonce = NSUtil.unFormatAVS(compte.getNumeroAvs());

            if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(affiliation.getTypeAffiliation())) {
                IsParitaire = true;
            }

            else if (CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(affiliation.getTypeAffiliation())) {
                if (!numeroNSSAffiliation.equals(numeroNSSAnnonce)) {
                    IsParitaire = true;
                }
            }
        }
        return IsParitaire;
    }

    @Override
    public String getAgenceCommise() throws Exception {
        return CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE));

    }

    @Override
    public boolean isCaissePrincipale() throws Exception {
        if (remoteAnnonce == null) {
            remoteAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteAnnonce.setIdAnnonce(annonceSuspens.getIdAnnonce());
            remoteAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable", "getUtilisateur" });
            remoteAnnonce.retrieve(remoteTransaction);
        }
        // Modif 1-5-8 => Modif pour FPV, pas seulement agence fusionnée mais
        // caisse fusionnée
        if (CIUtil.isCaisseDifferente(getSession())) {
            if (CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE)).equals(
                    application.getProperty(CIApplication.CODE_CAISSE))) {
                return true;
            } else {
                return false;
            }

        }

        if (CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE)).equals(
                application.getProperty(CIApplication.CODE_AGENCE))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param numeroAffilie
     * @return
     * @throws Exception
     */
    private AFAffiliation retrieveAffiliation(String numeroAffilie, BTransaction transaction) throws Exception {
        AFAffiliationManager affiliationManager = new AFAffiliationManager();

        affiliationManager.setSession(getSession());
        affiliationManager.setForAffilieNumero(numeroAffilie);

        affiliationManager.find();

        AFAffiliation affiliation = ((AFAffiliation) affiliationManager.getFirstEntity());

        if (affiliation == null) {
            transaction.addErrors("Can't find affiliation for affiliation number : " + numeroAffilie);
        }
        return affiliation;
    }

    /**
     * Traîte l'annonce 21. Date de création : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param testFinal
     *            doit être à true pour signifier si les tests doivent être effectués.
     */
    @Override
    public void traitementAnnonce(BTransaction transaction, boolean testFinal) throws Exception {
        // Si caisse fusionnée et pas principale => erreur
        if (application.isCaisseFusion() && !isCaissePrincipale()) {
            createLog(transaction, "L'ouverture doit se faire sur la caisse principale");
            suspendreAnnonce(transaction);
            return;
        }
        motifRCI = remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_RCI_OU_ORDRE_SPLITTING);
        boolean tenaitCI = false;
        compte = CICompteIndividuel.loadCITemporaire(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE),
                transaction);
        if (compte == null) {
            // compte inexistant, en créer un nouveau
            compte = new CICompteIndividuel();
            compte.setSession(getSession());
            compte.findAndSetId(transaction);
        }
        // liaison?
        CICompteIndividuel compteLie = CICompteIndividuel.loadCI(
                remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_1), transaction);
        if ((compte != null) && (compteLie != null)) {
            // ancien CI trouvé -> liaison
            compte.addLiaison(transaction, compteLie, false);
            // Pour le NNSS, si on a pas le dernier ouvert on check dans la
            // table de concordance
        }
        // caisse
        String idCaisseLocale = application.getAdministrationLocale(getSession()).getIdTiersAdministration();
        idCaisse = application.getAdministration(getSession(),
                remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_FIXANT_RENTE),
                remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_FIXANT_RENTE),
                new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();
        // si caisse fixant la rente non spécifiée, prendre caisse locale
        if ((idCaisse == null) || JAUtil.isIntegerEmpty(idCaisse)) {
            idCaisse = idCaisseLocale;
        }
        try {
            // créer/modifier le ci
            checkAndUpdateCI(transaction);
        } catch (Exception e) {
            // BZ 8443 - 18-12-2013
            // une exception est catchée dans cette partie car une mauvaise liaison entre compte individuel est
            // présent (donc pas de liens dans les deux sens).
            createLog(transaction, getSession().getLabel("MSG_21_MAUVAISE_LIAISON_ERREUR"));
            // on suspend l'annonce (inscription)
            suspendreAnnonce(transaction);
            return;
        }

        envoiEmailErreurTr(transaction, "CI [" + compte.getNumeroAvs() + "]");
        // ordre d'ouverture
        CIRassemblementOuverture ouverture = new CIRassemblementOuverture();
        ouverture.setSession(getSession());
        ouverture.setCompteIndividuelId(compte.getCompteIndividuelId());
        ouverture.setDateOrdre(JACalendar.todayJJsMMsAAAA());
        ouverture.setReferenceInterne(compte.getReferenceInterne());
        ouverture.setTypeEnregistrement(CIRassemblementOuverture.CS_OUVERTURE);
        ouverture.setMotifArc(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
        ouverture.setCaisseCommettante(idCaisseLocale);
        // ajout
        ouverture.add(transaction);
        // chercher les suspens
        CIEcritureManager ecritures = new CIEcritureManager();
        ecritures.setSession(getSession());
        ecritures.setForCompteIndividuelId(compte.getCompteIndividuelId());
        ecritures.setForIdTypeCompte(CIEcriture.CS_CI_SUSPENS);
        ecritures.setAdjustType(false);
        if (!JadeStringUtil.isIntegerEmpty(compte.getCompteIndividuelId())) {
            ecritures.find(transaction);
        }
        for (int i = 0; i < ecritures.size(); i++) {
            CIEcriture ecr = (CIEcriture) ecritures.getEntity(i);
            ecr.setIdTypeCompte(CIEcriture.CS_CI);
            ecr.simpleUpdate(transaction);
            envoiEmailErreurTr(transaction, "Ecr suspens [" + ecr.getNoNomEmployeur() + "/" + ecr.getAnnee() + "/"
                    + ecr.getMontantSigne() + "]");
        }
        ecritures.setForIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
        if (!JadeStringUtil.isIntegerEmpty(compte.getCompteIndividuelId())) {
            ecritures.find(transaction);
        }
        for (int i = 0; i < ecritures.size(); i++) {
            CIEcriture ecr = (CIEcriture) ecritures.getEntity(i);
            ecr.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
            ecr.simpleUpdate(transaction);
            envoiEmailErreurTr(transaction,
                    "Ecr temp [" + ecr.getNoNomEmployeur() + "/" + ecr.getAnnee() + "/" + ecr.getMontantSigne() + "]");
        }
        // test dernier motif du RCI
        // motifRCI =
        // remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_RCI_OU_ORDRE_SPLITTING);
        if (!JAUtil.isIntegerEmpty(motifRCI) && !transaction.hasErrors()) {
            if (!"95".equals(motifRCI)) {
                // Recherche de la clôtrue (si existante)
                CIRassemblementOuverture cloture = compte.getDerniereBCloture(transaction);
                String derClo = getDateBidouillee(remoteAnnonce
                        .getField(IHEAnnoncesViewBean.DATE_CLOTURE_OU_ORDRE_SPLITTING_MMAA));
                // compte.getDerniereCloture(true);
                if ((cloture != null) && BSessionUtil.compareDateEqual(getSession(), cloture.getDateCloture(), derClo)) {
                    tenaitCI = true;
                } else {
                    // créer clôture
                    cloture = new CIRassemblementOuverture();
                    cloture.setSession(getSession());
                    cloture.setCompteIndividuelId(compte.getCompteIndividuelId());
                    cloture.setDateOrdre(JACalendar.todayJJsMMsAAAA());
                    cloture.setReferenceInterne(compte.getReferenceInterne());
                    cloture.setTypeEnregistrement(CIRassemblementOuverture.CS_CLOTURE_OUVERTURE);
                    cloture.setCaisseTenantCI(application.getProperty(CIApplication.CODE_AGENCE));
                    cloture.setDateCloture(derClo);
                    // Modif, si motif 2 => on passe 85 ou 81, car ces motifs 2 ne sont pas gérés par ACCOR
                    cloture.setMotifArc(motifRCI);

                    cloture.setCaisseCommettante(idCaisse);
                    // ajout
                    cloture.add(transaction);
                }
                // test de clôture sur les écritures
                ArrayList result = cloture.addCIAdditionnel(transaction, null, true);
                if (result.size() != 0) {
                    // ci additionnel ou second RCI?
                    // Envoi de CI additionnel si forcé par les propriétés du
                    // module ou si
                    // le CI est tenu par la caisse et le motif est 73, 75, 77,
                    // 83, 84, 85 ou 86
                    if (application.isCIAddAutomatique()
                            || (tenaitCI && ("73".equals(motifRCI) || "75".equals(motifRCI) || "77".equals(motifRCI)
                                    || "83".equals(motifRCI) || "84".equals(motifRCI) || "85".equals(motifRCI) || "86"
                                        .equals(motifRCI)))) {

                        compte.annonceCIAdditionnel(transaction, result);
                    } else {
                        // envoi second RCI -> email
                        String message = java.text.MessageFormat.format(
                                getSession().getLabel("MSG_ANNONCE_21_RCI_EMAIL_MESSAGE"),
                                new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE) });
                        ArrayList to = application.getEMailResponsableCI(transaction);
                        this.envoiEmail(to, getSession().getLabel("MSG_ANNONCE_21_RCI_EMAIL_SUJET"), message);
                        // modification de la clôture (date second RCI)
                        cloture.setDateSecondRci(JACalendar.todayJJsMMsAAAA());
                        cloture.wantCallMethodBefore(false);
                        cloture.update(transaction);
                    }
                }
            } else {
                // ordre de splitting
                CIPeriodeSplitting splitting = new CIPeriodeSplitting();
                splitting.setSession(getSession());
                splitting.setCompteIndividuelId(compte.getCompteIndividuelId());
                splitting.setCaisseCommettante(idCaisse);
                splitting.setTypeEnregistrement(CIPeriodeSplitting.CS_SPLITTING_OUVERTURE);
                // ajout de la période de splitting
                splitting.wantCallValidate(false);
                splitting.add();
                // envoi email
                String message = java.text.MessageFormat.format(getSession().getLabel("MSG_ANNONCE_21_EMAIL_MESSAGE"),
                        new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE) });
                ArrayList to = application.getEMailResponsableCI(transaction);
                this.envoiEmail(to, getSession().getLabel("MSG_ANNONCE_21_EMAIL_SUJET"), message);
            }
        }

        if (!transaction.hasErrors()) {
            String numeroAffillie = "";
            try {
                numeroAffillie = remoteAnnonce.getNumeroAffilie();
            } catch (Exception e) {
                transaction.addErrors(getSession().getLabel("MSG_NUMERO_AFFILLIE_NOT_FOUND"));
            }

            if (!JadeStringUtil.isBlankOrZero(numeroAffillie)
                    && !"68".equals(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                    && !"46".equals(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {

                // méthode qui contrôle si l'affilé est paritaire.
                boolean isParitaire = checkIsParitaire(numeroAffillie, transaction);
                // on créer une exception que si l'affilé est paritaire
                if (isParitaire == true) {
                    CIExceptions exceptionCI = new CIExceptions();
                    exceptionCI.setSession(getSession());
                    exceptionCI.setNumeroAvs(compte.getNumeroAvs());
                    exceptionCI.setAffilie(numeroAffillie);
                    exceptionCI.setDateEngagement(remoteAnnonce.getDateEngagement());
                    exceptionCI.add(transaction);
                }
            }
        }
        // effacement de l'annonce
        annonceTraitee(transaction);
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
        // nom
        compte.setNomPrenom(this.checkAndSet(compte.getNomPrenom(),
                remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF).trim()));
        // pays
        compte.setPaysOrigineId(this.checkAndSet(compte.getPaysOrigineId(),
                "315" + remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_ORIGINE)));
        // motif
        compte.setDernierMotifOuverture(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
        // no avs
        compte.setNumeroAvs(this.checkAndSet(compte.getNumeroAvs(),
                remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE)));
        /*
         * **
         * Modif NNSS => je regarde si le numéro AVS donné à la centrale
         */
        if (CIUtil.isNNSSlengthOrNegate(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {
            compte.setNnss(new Boolean(true));
            compte.setNumeroAvsNNSS("true");

            // modif NNSS
            // Si le sexe ou la date de naissance ne sont pas renseigné =>
            // erreur, le 20.01 dans hermes n'est pas associé
            if (JadeStringUtil.isIntegerEmpty(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA))
                    || JadeStringUtil.isIntegerEmpty(remoteAnnonce.getField(IHEAnnoncesViewBean.SEXE))) {
                transaction.addErrors(getSession().getLabel("SEXE_OU_DATE_NAISSANCE_NNSS_NON_TROUVE")
                        + remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
                return;
            } else {
                compte.setDateNaissance(this.checkAndSet(compte.getDateNaissance(),
                        getDateBidouilleeForNaissance(remoteAnnonce
                                .getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA))));
                if ("2".equals(remoteAnnonce.getField(IHEAnnoncesViewBean.SEXE))) {
                    compte.setSexe(this.checkAndSet(compte.getSexe(), CICompteIndividuel.CS_FEMME));
                } else {
                    compte.setSexe(this.checkAndSet(compte.getSexe(), CICompteIndividuel.CS_HOMME));
                }
            }

        } else {
            compte.setNnss(new Boolean(false));
        }

        // ancien no avs
        String result = remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_1);
        if (!JAUtil.isStringEmpty(result)) {
            compte.setNumeroAvsPrecedant(result);
        }
        // date naissance
        // date non trouvée dans l'annonce
        // compte.setDateNaissance(checkAndSet(compte.getDateNaissance(),
        // remoteAnnonce.getField(IHEOutputAnnonce.DATE_NAISSANCE_1_JJMMAA)));

        // annee ouverture
        result = remoteAnnonce.getField(IHEAnnoncesViewBean.ANNEE_OUVERTURE_CI);
        if (!JAUtil.isStringEmpty(result)) {
            compte.setAnneeOuverture(getAnneeBidouillee(result));
        }
        // référence interne
        result = remoteAnnonce.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE);
        if (!JAUtil.isStringEmpty(result)) {
            compte.setReferenceInterne(result);
        }
        if ((!JAUtil.isIntegerEmpty(motifRCI)) && (!"95".equals(motifRCI))) {
            // dernière caisse
            if (!JAUtil.isStringEmpty(idCaisse)) {
                compte.setDerniereCaisse(this.checkAndSet(compte.getDerniereCaisse(), idCaisse));
            }
            // date clôture
            result = getDateBidouillee(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_CLOTURE_OU_ORDRE_SPLITTING_MMAA));
            if (!JAUtil.isStringEmpty(result)) {
                compte.setDerniereCloture(result);
            }
        }
        // dernier employeur, test si indiqué dans référence interne
        String idEmployeur = getEmployeurDeReference(transaction,
                remoteAnnonce.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE));
        if (idEmployeur != null) {
            compte.setDernierEmployeur(this.checkAndSet(compte.getDernierEmployeur(), idEmployeur));
        }
        // utilisateur
        if (!CICompteIndividuel.CS_REGISTRE_ASSURES.equals(compte.getRegistre())) {
            compte.setUtilisateurCreation(remoteAnnonce.getUtilisateur());
            compte.setDateCreation(JACalendar.todayJJsMMsAAAA());
        }
        // registre (pas de copie nécessaire pour l'état du registre)
        compte.setRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        // securité
        compte.setAccesSecurite(CICompteIndividuel.CS_ACCESS_0);
        // compte ouvert
        compte.setCiOuvert(new Boolean(true));

    }
}
