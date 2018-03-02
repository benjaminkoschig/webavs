package globaz.corvus.process;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.topaz.REDemandeCalculProvisoireOO;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.itext.PRLettreEnTete;
import globaz.prestation.topaz.PRLettreEnTeteOO;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import java.util.ArrayList;
import java.util.List;

public class REGenererDemandeCalculProvisoireProcess extends REAbstractInfoComplPrintProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PRInfoCompl info = null;
    private String motif = "";
    private String nomAssure = "";

    private REDemandeCalculProvisoireOO createDocumentCopie() throws Exception {

        REDemandeCalculProvisoireOO demandeCopie = new REDemandeCalculProvisoireOO();
        demandeCopie.setSession(getSession());
        demandeCopie.setIsCopie(true);
        demandeCopie.setMotif(getMotif());
        demandeCopie.setNomAssure(getNomAssure());
        demandeCopie.setInfo(getInfo());

        if (isAgenceTrouve()) {
            demandeCopie.setCopieAgenceCommu(true);
            demandeCopie.setIdAgenceCommu(getIdAgenceCommunale());
        }

        demandeCopie.setDateDocument(getDateDocument());
        demandeCopie.setIdDemandeRente(getIdDemandeRente());
        demandeCopie.setIdTiers(getIdTiersDemande());

        demandeCopie.generationLettre();

        return demandeCopie;
    }

    private REDemandeCalculProvisoireOO createDocumentOriginale() throws Exception {

        REDemandeCalculProvisoireOO demandeOriginale = new REDemandeCalculProvisoireOO();
        demandeOriginale.setSession(getSession());
        demandeOriginale.setIsCopie(false);
        demandeOriginale.setMotif(getMotif());
        demandeOriginale.setNomAssure(getNomAssure());
        demandeOriginale.setInfo(getInfo());

        if (isAgenceTrouve()) {
            demandeOriginale.setCopieAgenceCommu(true);
            demandeOriginale.setIdAgenceCommu(getIdAgenceCommunale());
        }

        demandeOriginale.setDateDocument(getDateDocument());
        demandeOriginale.setIdDemandeRente(getIdDemandeRente());
        demandeOriginale.setIdTiers(getIdTiersDemande());

        demandeOriginale.generationLettre();

        return demandeOriginale;
    }

    private PRLettreEnTeteOO createLettreEnTete(String idTiers) throws Exception {

        PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(getSession());

        // retrieve du tiers
        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);

        if (null == tier) {
            tier = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tier) {
            lettreEnTete.setTierAdresse(tier);
            // lettreEnTete.setDateDocument(JACalendar.format(this.getInfo().getDateInfoCompl(),
            // PRUtil.getISOLangueTiers(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));
            lettreEnTete.setDateDocument(getInfo().getDateInfoCompl());
            lettreEnTete.setSession(getSession());
            lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_CORVUS);
            lettreEnTete.generationLettre();
        }

        return lettreEnTete;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("DEMANDE_CAL_PROV_DOCUMENT");
    }

    private PRInfoCompl getInfo() {
        return info;
    }

    public String getMotif() {
        return motif;
    }

    @Override
    public String getName() {
        return getSession().getLabel("DEMANDE_CAL_PROV_DOCUMENT");
    }

    public String getNomAssure() {
        return nomAssure;
    }

    @Override
    public void run() {
        try {

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(getSession());
            demandeRente.setIdDemandeRente(getIdDemandeRente());
            demandeRente.retrieve();

            RERenteAccJoinTblTiersJoinDemRenteManager rdm = new RERenteAccJoinTblTiersJoinDemRenteManager();
            rdm.setSession(getSession());
            rdm.setForNoDemandeRente(demandeRente.getIdDemandeRente());
            rdm.find();

            if (!rdm.isEmpty()) {
                RERenteAccJoinTblTiersJoinDemandeRente ra = (RERenteAccJoinTblTiersJoinDemandeRente) rdm
                        .getFirstEntity();

                for (int i = 0; i < rdm.size(); i++) {
                    ra = (RERenteAccJoinTblTiersJoinDemandeRente) rdm.getEntity(i);
                    REBasesCalcul bc = new REBasesCalcul();
                    bc.setSession(getSession());
                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                    bc.retrieve();
                    REDeleteCascadeDemandeAPrestationsDues.supprimerBaseCalculCascade_noCommit(getSession(),
                            getSession().getCurrentThreadTransaction(), bc);
                }

            }

            PRInfoCompl info = new PRInfoCompl();
            info.setSession(getSession());
            info.setIdInfoCompl(demandeRente.getIdInfoComplementaire());
            info.retrieve();

            setInfo(info);

            PRDemande demandePrestation = new PRDemande();
            demandePrestation.setSession(getSession());
            demandePrestation.setIdDemande(demandeRente.getIdDemandePrestation());
            demandePrestation.retrieve();

            setIdTiersDemande(demandePrestation.getIdTiers());

            JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
            allDoc.setMergedDocDestination(generateDestinationDocInfo(getSession()
                    .getLabel("DEMANDE_CAL_PROV_DOCUMENT")));

            JadePublishDocumentInfo docInfoTransfertCalculPrevisionnel = this.generateAndFillDocInfo(getSession()
                    .getLabel("DEMANDE_CAL_PROV_DOCUMENT"),
                    IRENoDocumentInfoRom.TRANSFERT_A_CAISSE_COMPETENTE_POUR_CALCUL_PREVISIONNEL, isSendToGed());

            JadePublishDocumentInfo docInfoCopieTransfertCalculPrevisionnel = this.generateAndFillDocInfo(getSession()
                    .getLabel("DEMANDE_CAL_PROV_DOCUMENT"),
                    IRENoDocumentInfoRom.TRANSFERT_A_CAISSE_COMPETENTE_POUR_CALCUL_PREVISIONNEL);

            JadePublishDocumentInfo docInfoLettreAccompagnement = this.generateAndFillDocInfo(
                    getSession().getLabel("DEMANDE_CAL_PROV_DOCUMENT"),
                    IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);

            setIsCopieAgenceCommunale(Boolean.parseBoolean(getSession().getApplication().getProperty(
                    "isCopieAgenceCommunale")));

            // Test pour savoir si une copie doit être faite à l'agence
            // communale AVS et si le tiers en a une renseignée
            TICompositionTiersManager compTiersMgr = new TICompositionTiersManager();

            if (isCopieAgenceCommunale()) {
                compTiersMgr.setSession(getSession());
                compTiersMgr.setForIdTiersParent(getIdTiersDemande());
                compTiersMgr.setForTypeLien("507007");
                compTiersMgr.find();

                if (compTiersMgr.size() == 0) {
                    PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiersDemande());
                    getLogSession().addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                    REGenererDemandeCalculProvisoireProcess.class.getName(), getSession().getLabel(
                                            "ERREUR_AGENCE_COMMUNALE")
                                            + " "
                                            + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                                            + " "
                                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + "."));
                } else {
                    setIsAgenceTrouve(Boolean.TRUE);
                }
            }

            if (isAgenceTrouve()) {
                TICompositionTiers tiers = (TICompositionTiers) compTiersMgr.getFirstEntity();
                setIdAgenceCommunale(tiers.getIdTiersEnfant());

                REDemandeCalculProvisoireOO demandeCalProvOO;

                // 1. Création du document de calcul provisoire original
                demandeCalProvOO = createDocumentOriginale();
                allDoc.addDocument(demandeCalProvOO.getDocumentData(), docInfoTransfertCalculPrevisionnel);

                // 2. Création de l'entête pour l'assuré
                PRLettreEnTeteOO lettreEnTete = createLettreEnTete(getIdTiersDemande());
                allDoc.addDocument(lettreEnTete.getDocumentData(), docInfoLettreAccompagnement);

                // 3. Création de la copie assuré
                demandeCalProvOO = createDocumentCopie();
                allDoc.addDocument(demandeCalProvOO.getDocumentData(), docInfoCopieTransfertCalculPrevisionnel);

                // 4. Si nécessaire, création de l'entete et de la copie de l'agence communale AVS
                lettreEnTete = createLettreEnTete(tiers.getIdTiersEnfant());
                allDoc.addDocument(lettreEnTete.getDocumentData(), docInfoLettreAccompagnement);
                demandeCalProvOO = createDocumentCopie();
                allDoc.addDocument(demandeCalProvOO.getDocumentData(), docInfoCopieTransfertCalculPrevisionnel);
            } else {
                REDemandeCalculProvisoireOO demandeCalProvOO;

                // 1. Création du document de transfert original
                demandeCalProvOO = createDocumentOriginale();
                allDoc.addDocument(demandeCalProvOO.getDocumentData(), docInfoTransfertCalculPrevisionnel);

                // 2. Création de l'entête pour l'assuré
                PRLettreEnTeteOO lettreEnTete = createLettreEnTete(getIdTiersDemande());
                allDoc.addDocument(lettreEnTete.getDocumentData(), docInfoLettreAccompagnement);

                // 3. Création de la copie assuré
                demandeCalProvOO = createDocumentCopie();
                allDoc.addDocument(demandeCalProvOO.getDocumentData(), docInfoCopieTransfertCalculPrevisionnel);
            }

            this.createDocuments(allDoc);

            demandeRente.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
            demandeRente.update();

        } catch (Exception e) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                            REGenererDemandeCalculProvisoireProcess.class.getName(), e.getMessage()));
        } finally {
            try {
                if (getLogSession().hasMessages()) {
                    List<String> emails = new ArrayList<String>();
                    emails.add(getEMailAddress());
                    sendCompletionMail(emails);
                }
            } catch (Exception e) {
                getLogSession().addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                REGenererDemandeCalculProvisoireProcess.class.getName(), getSession().getLabel(
                                        "ERREUR_ENVOI_MAIL_COMPLETION")));
            }
        }
    }

    private void setInfo(PRInfoCompl info) {
        this.info = info;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }
}