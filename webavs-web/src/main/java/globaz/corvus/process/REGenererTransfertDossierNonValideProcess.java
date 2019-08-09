package globaz.corvus.process;

import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.topaz.RETransfertNonValideOO;
import globaz.corvus.utils.REGedUtils;
import globaz.hera.utils.SFFamilleUtils;
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
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class REGenererTransfertDossierNonValideProcess extends REAbstractInfoComplPrintProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idOfficeAi = "";
    private PRInfoCompl info = null;
    private boolean isCopieOfAi = false;
    private String remarque = "";
    private TIAdministrationViewBean tiAdministration;
    private TICompositionTiers tiers;

    private RETransfertNonValideOO createDocumentCopie() throws Exception {

        RETransfertNonValideOO transfertCopie = new RETransfertNonValideOO();
        transfertCopie.setSession(getSession());
        transfertCopie.setIsCopie(true);
        transfertCopie.setInfo(getInfo());
        transfertCopie.setRemarque(getRemarque());

        if (isAgenceTrouve()) {
            transfertCopie.setCopieAgenceCommu(true);
            transfertCopie.setIdAgenceCommu(getIdAgenceCommunale());
        }

        if (isCopieOfAi) {
            transfertCopie.setCopieAgenceAI(true);
            transfertCopie.setOfficeAi(tiAdministration);
        }

        transfertCopie.setDateDocument(getDateDocument());
        transfertCopie.setIdDemandeRente(getIdDemandeRente());
        transfertCopie.setIdTiers(getIdTiersDemande());

        transfertCopie.generationLettre();

        return transfertCopie;
    }

    private RETransfertNonValideOO createDocumentOriginale() throws Exception {

        RETransfertNonValideOO transfertOriginale = new RETransfertNonValideOO();
        transfertOriginale.setSession(getSession());
        transfertOriginale.setIsCopie(false);
        transfertOriginale.setInfo(getInfo());
        transfertOriginale.setRemarque(getRemarque());

        if (isAgenceTrouve()) {
            transfertOriginale.setCopieAgenceCommu(true);
            transfertOriginale.setIdAgenceCommu(getIdAgenceCommunale());
        }

        if (isCopieOfAi) {
            transfertOriginale.setCopieAgenceAI(true);
            transfertOriginale.setOfficeAi(tiAdministration);
        }

        transfertOriginale.setDateDocument(getDateDocument());
        transfertOriginale.setIdDemandeRente(getIdDemandeRente());
        transfertOriginale.setIdTiers(getIdTiersDemande());

        transfertOriginale.generationLettre();

        return transfertOriginale;
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

            // lettreEnTete.setDateDocument(JACalendar.format(this.getInfo().getDateInfoCompl(),
            // PRUtil.getISOLangueTiers(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));
            lettreEnTete.setDateDocument(getInfo().getDateInfoCompl());
            lettreEnTete.setTierAdresse(tier);
            lettreEnTete.setSession(getSession());
            lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_CORVUS);
            lettreEnTete.generationLettre();
        }

        return lettreEnTete;
    }

    private PRLettreEnTeteOO createLettreEnTeteOfficeAI(String codeOfficeAI) throws Exception {

        PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(getSession());

        if (null != tiAdministration) {
            // retrieve de l'office AI
            PRTiersWrapper tiersOfficeAi = PRTiersHelper.getAdministrationParId(getSession(), tiAdministration.getId());

            if (null != tiersOfficeAi) {
                lettreEnTete.setTierAdresse(tiersOfficeAi);
                // lettreEnTete.setDateDocument(JACalendar.format(this.getInfo().getDateInfoCompl(),
                // PRUtil.getISOLangueTiers(tiersOfficeAi.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));
                lettreEnTete.setDateDocument(getInfo().getDateInfoCompl());
                lettreEnTete.setSession(getSession());
                lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_CORVUS);
                lettreEnTete.generationLettre();
            }
        }

        return lettreEnTete;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("TRANSFERT_DOCUMENT");
    }

    private PRInfoCompl getInfo() {
        return info;
    }

    @Override
    public String getName() {
        return getSession().getLabel("TRANSFERT_DOCUMENT");
    }

    public String getRemarque() {
        return remarque;
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

                REBasesCalcul bc = new REBasesCalcul();
                bc.setSession(getSession());
                bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                bc.retrieve();
                idOfficeAi = bc.getCodeOfficeAi();
                REDeleteCascadeDemandeAPrestationsDues.supprimerBaseCalculCascade_noCommit(getSession(), getSession()
                        .getCurrentThreadTransaction(), bc);
                for (int i = 1; i < rdm.size(); i++) {
                    ra = (RERenteAccJoinTblTiersJoinDemandeRente) rdm.getEntity(i);
                    bc = new REBasesCalcul();
                    bc.setSession(getSession());
                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                    bc.retrieve();
                    if (!bc.isNew()) {
                        REDeleteCascadeDemandeAPrestationsDues.supprimerBaseCalculCascade_noCommit(getSession(),
                                getSession().getCurrentThreadTransaction(), bc);
                    }
                }

            }

            // Utiliser pour la date du document car c'est celle de l'info
            // compl. qu'il faut afficher
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

            if (StringUtils.equals(TypeDemandeRente.DEMANDE_SURVIVANT.getCodeSysteme().toString(),demandeRente.getCsTypeDemandeRente())){
                PRTiersWrapper tiersCourant = PRTiersHelper.getTiersById(getSession(), demandePrestation.getIdTiers());
                if (StringUtils.isNotEmpty(tiersCourant.getDateDeces())) {
                    PRTiersWrapper tiersTransfert = SFFamilleUtils.getTiersTransfert(getSession(),tiersCourant.getIdTiers());
                    if (Objects.nonNull(tiersTransfert )){
                        setIdTiersDemande(tiersTransfert .getIdTiers());
                    }
                }
            }

            String transfertDocument = getSession().getLabel("TRANSFERT_DOCUMENT");

            JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
            allDoc.setMergedDocDestination(generateDestinationDocInfo(transfertDocument));

            JadePublishDocumentInfo docInfoTransfertDossier = this.generateAndFillDocInfo(transfertDocument,
                    IRENoDocumentInfoRom.TRANSFERT_A_CAISSE_COMPETENTE, isSendToGed());
            docInfoTransfertDossier.setDocumentProperty(
                    REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(),
                            REGedUtils.getTypeRentePourCetteDemandeRente(getSession(), demandeRente)));

            JadePublishDocumentInfo docInfoCopieTransfertDossier = this.generateAndFillDocInfo(transfertDocument,
                    IRENoDocumentInfoRom.TRANSFERT_A_CAISSE_COMPETENTE);

            JadePublishDocumentInfo docInfoLettreAccompagnement = this.generateAndFillDocInfo(transfertDocument,
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
                                    REGenererTransfertDossierNonValideProcess.class.getName(), getSession().getLabel(
                                            "ERREUR_AGENCE_COMMUNALE")
                                            + " "
                                            + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                                            + " "
                                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + "."));
                } else {
                    setIsAgenceTrouve(Boolean.TRUE);
                }
            }

            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente())
                    || IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(demandeRente.getCsTypeDemandeRente())) {

                isCopieOfAi = true;

                if (rdm.isEmpty()) {
                    if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente())) {
                        REDemandeRenteInvalidite demInv = new REDemandeRenteInvalidite();
                        demInv.setSession(getSession());
                        demInv.setIdDemandeRente(demandeRente.getIdDemandeRente());
                        demInv.retrieve();
                        idOfficeAi = demInv.getCodeOfficeAI();
                    } else {
                        REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
                        demAPI.setSession(getSession());
                        demAPI.setIdDemandeRente(demandeRente.getIdDemandeRente());
                        demAPI.retrieve();
                        idOfficeAi = demAPI.getCodeOfficeAI();
                    }
                }

                TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
                tiAdministrationMgr.setSession(getSession());
                tiAdministrationMgr.setForCodeAdministration(idOfficeAi);
                tiAdministrationMgr.setForGenreAdministration("509004");
                tiAdministrationMgr.find();

                tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();
            }
            if (isAgenceTrouve()) {
                tiers = (TICompositionTiers) compTiersMgr.getFirstEntity();
                setIdAgenceCommunale(tiers.getIdTiersEnfant());
            }

            RETransfertNonValideOO TransfertOO = null;

            // 1. Création du document de transfert original
            TransfertOO = createDocumentOriginale();
            allDoc.addDocument(TransfertOO.getDocumentData(), docInfoTransfertDossier);

            // 2. Création de l'entête pour l'assuré
            PRLettreEnTeteOO lettreEnTete = createLettreEnTete(getIdTiersDemande());
            allDoc.addDocument(lettreEnTete.getDocumentData(), docInfoLettreAccompagnement);

            // 3. Création de la copie assuré
            TransfertOO = createDocumentCopie();
            allDoc.addDocument(TransfertOO.getDocumentData(), docInfoCopieTransfertDossier);

            if (isAgenceTrouve()) {
                // 4. Si nécessaire, création de l'entete et de la copie de l'agence communale AVS
                lettreEnTete = createLettreEnTete(tiers.getIdTiersEnfant());
                allDoc.addDocument(lettreEnTete.getDocumentData(), docInfoLettreAccompagnement);

                // 5. Création de la copie à l'agence communale AVS
                TransfertOO.setIdAgenceCommu(tiers.getId());
                TransfertOO = createDocumentCopie();
                allDoc.addDocument(TransfertOO.getDocumentData(), docInfoCopieTransfertDossier);
            }

            if (isCopieOfAi) {
                // 6. Création de l'entete office AI
                lettreEnTete = createLettreEnTeteOfficeAI(idOfficeAi);
                allDoc.addDocument(lettreEnTete.getDocumentData(), docInfoLettreAccompagnement);

                // 7. Création de la copie à l'office AI
                TransfertOO = createDocumentCopie();
                allDoc.addDocument(TransfertOO.getDocumentData(), docInfoCopieTransfertDossier);
            }

            this.createDocuments(allDoc);

            demandeRente.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
            demandeRente.update();

        } catch (Exception e) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                            REGenererTransfertDossierNonValideProcess.class.getName(), e.toString()));
        } finally {
            try {
                if (getLogSession().hasMessages()) {
                    List<String> emails = new ArrayList<String>();
                    emails.add(getEMailAddress());
                    sendCompletionMail(emails);
                }
            } catch (Exception e) {
                new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                        REGenererTransfertDossierNonValideProcess.class.getName(), getSession().getLabel(
                                "ERREUR_ENVOI_MAIL_COMPLETION"));
            }
        }
    }

    private void setInfo(PRInfoCompl info) {
        this.info = info;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
}
