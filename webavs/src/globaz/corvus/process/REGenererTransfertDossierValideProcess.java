package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IRERenteAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamille;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamilleManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.topaz.REChangementCaisseOO;
import globaz.corvus.topaz.RETransfertValideOO;
import globaz.corvus.utils.REGedUtils;
import globaz.globall.db.BManager;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
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
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.prestation.domaine.CodePrestation;

public class REGenererTransfertDossierValideProcess extends REAbstractInfoComplPrintProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String CessationPaiement = "";
    private String DateEnvoi = "";
    private String idOfficeAi = "";
    private PRInfoCompl info = null;
    private boolean isCopieOfAi = false;
    private boolean isRaUnique;
    private List<RERenteAccordeeFamille> listeRenteAccordee;
    private final StringBuilder messageErreur = new StringBuilder();
    private String motifTransmission = "";
    private String NumAgence = "";
    private String NumCaisse = "";
    private String remarque = "";
    private TIAdministrationViewBean tiAdministration = null;
    private TICompositionTiers tiers = null;

    /**
     * BZ 5358, récupération des ID Tiers des ex-conjoints du tiers afin d'exclure les données des ex-conjoints du
     * transfert
     * 
     * @param idTiers
     *            l'ID tiers du dossier à transférer
     * @return un {@link Set}&lt;{@link String}&gt; contenant les ID Tiers des ex-conjoints, est vide si aucun
     *         ex-conjoint
     * @throws Exception
     *             Si un erreur survient lors de la recherche des ex-conjoints dans la base de données
     */
    private Set<String> chargerExConjoints(final String idTiers) throws Exception {
        Set<String> idTiersExConjoints = new HashSet<String>();

        List<PRTiersWrapper> exConjoints = SFFamilleUtils.getExConjointsTiers(getSession(),
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);

        RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager renteAccManager = new RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager();
        renteAccManager.setSession(getSession());

        for (PRTiersWrapper unExConjoint : exConjoints) {
            idTiersExConjoints.add(unExConjoint.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        return idTiersExConjoints;
    }

    private REChangementCaisseOO createDocumentChangementCopie() throws Exception {

        REChangementCaisseOO changementOriginale = new REChangementCaisseOO();

        changementOriginale.setSession(getSession());
        changementOriginale.setIdTiers(getIdTiersDemande());
        changementOriginale.setNumCaisse(getNumCaisse());
        changementOriginale.setNumAgence(getNumAgence());
        changementOriginale.setDateEnvoi(getDateEnvoi());
        changementOriginale.setCopie(true);
        changementOriginale.setDateDocument(getDateDocument());
        changementOriginale.setIdDemandeRente(getIdDemandeRente());

        changementOriginale.generationLettre();

        return changementOriginale;

    }

    private REChangementCaisseOO createDocumentChangementOriginale() throws Exception {

        REChangementCaisseOO changementOriginale = new REChangementCaisseOO();

        changementOriginale.setSession(getSession());
        changementOriginale.setIdTiers(getIdTiersDemande());
        changementOriginale.setNumCaisse(getNumCaisse());
        changementOriginale.setNumAgence(getNumAgence());
        changementOriginale.setDateEnvoi(getDateEnvoi());
        changementOriginale.setCopie(false);
        changementOriginale.setDateDocument(getDateDocument());
        changementOriginale.setIdDemandeRente(getIdDemandeRente());

        changementOriginale.generationLettre();

        return changementOriginale;

    }

    private RETransfertValideOO createDocumentCopie() throws Exception {

        RETransfertValideOO transfertCopie = new RETransfertValideOO();

        transfertCopie.setSession(getSession());
        transfertCopie.setNumCaisse(getNumCaisse());
        transfertCopie.setNumAgence(getNumAgence());
        transfertCopie.setIdTiers(getIdTiersDemande());
        transfertCopie.setMotifTransmission(getMotifTransmission());
        transfertCopie.setRemarque(getRemarque());
        transfertCopie.setCessationPaiement(getCessationPaiement());
        transfertCopie.setDateEnvoi(getDateEnvoi());
        transfertCopie.setList(getListeRenteAccordee());
        transfertCopie.setIsCopie(true);
        transfertCopie.setRaUnique(isRaUnique);

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

        transfertCopie.generationLettre();

        return transfertCopie;
    }

    private RETransfertValideOO createDocumentOriginale() throws Exception {

        RETransfertValideOO transfertOriginale = new RETransfertValideOO();

        transfertOriginale.setSession(getSession());
        transfertOriginale.setNumCaisse(getNumCaisse());
        transfertOriginale.setNumAgence(getNumAgence());
        transfertOriginale.setIdTiers(getIdTiersDemande());
        transfertOriginale.setMotifTransmission(getMotifTransmission());
        transfertOriginale.setRemarque(getRemarque());
        transfertOriginale.setCessationPaiement(getCessationPaiement());
        transfertOriginale.setDateEnvoi(getDateEnvoi());
        transfertOriginale.setList(getListeRenteAccordee());
        transfertOriginale.setIsCopie(false);
        transfertOriginale.setRaUnique(isRaUnique);

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

        transfertOriginale.generationLettre();

        return transfertOriginale;
    }

    private PRLettreEnTeteOO createLettreEnTete(final String idTiers) throws Exception {

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

    private PRLettreEnTeteOO createLettreEnTeteOfficeAi(final String codeOfficeAi) throws Exception {

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

    public String getCessationPaiement() {
        return CessationPaiement;
    }

    public String getDateEnvoi() {
        return DateEnvoi;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("TRANSFERT_DOSSIER");
    }

    private PRInfoCompl getInfo() {
        return info;
    }

    public List<RERenteAccordeeFamille> getListeRenteAccordee() {
        return listeRenteAccordee;
    }

    public String getMotifTransmission() {
        return motifTransmission;
    }

    @Override
    public String getName() {
        return getSession().getLabel("TRANSFERT_DOSSIER");
    }

    public String getNumAgence() {
        return NumAgence;
    }

    public String getNumCaisse() {
        return NumCaisse;
    }

    public String getRemarque() {
        return remarque;
    }

    private boolean isRenteAPI(final RERenteAccordeeFamille rente) {
        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(rente.getCodePrestation()));
        return codePrestation.isAPI();
    }

    private boolean isRenteInvalidite(final RERenteAccordeeFamille rente) {
        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(rente.getCodePrestation()));
        return codePrestation.isAI();
    }

    private boolean isRentePrincipale(final RERenteAccordeeFamille rente) {
        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(rente.getCodePrestation()));
        return codePrestation.isRentePrincipale();
    }

    @Override
    public void run() {
        try {

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(getSession());
            demandeRente.setIdDemandeRente(getIdDemandeRente());
            demandeRente.retrieve();

            PRDemande demandePrestation = new PRDemande();
            demandePrestation.setSession(getSession());
            demandePrestation.setIdDemande(demandeRente.getIdDemandePrestation());
            demandePrestation.retrieve();

            setIdTiersDemande(demandePrestation.getIdTiers());

            String titreTransfertDossier = getSession().getLabel("TRANSFERT_DOSSIER");
            String titreChangementCaisse = getSession().getLabel("CHANGEMENT_CAISSE");

            JadePrintDocumentContainer mergedDoc = new JadePrintDocumentContainer();
            mergedDoc.setMergedDocDestination(generateDestinationDocInfo(titreTransfertDossier));

            PRInfoCompl info = new PRInfoCompl();
            info.setSession(getSession());
            info.setIdInfoCompl(demandeRente.getIdInfoComplementaire());
            info.retrieve();

            setInfo(info);

            setNumCaisse(info.getNoCaisse());
            setNumAgence(info.getNoAgence());

            setIsCopieAgenceCommunale(Boolean.parseBoolean(getSession().getApplication().getProperty(
                    "isCopieAgenceCommunale")));

            // Le but est d'obtenir deux listes en fonction des adresses de
            // paiement des bénéficiaires des rentes accordées.

            // Recherche les rentes accordés
            RERenteAccordeeFamilleManager renteAccManager = new RERenteAccordeeFamilleManager();
            renteAccManager.setSession(getSession());

            renteAccManager.setForIdTiersLiant(demandePrestation.getIdTiers());
            renteAccManager.setForDateFinDroitPlusGrandeOuEgale(getCessationPaiement());
            if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {
                renteAccManager.setExclureRentesAutreQueSurvivant(true);
            } else if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)
                    || demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {
                renteAccManager.setExclureRentesSurvivant(true);
            }

            // BZ 5420
            renteAccManager.setOrderByCodePrestation();
            renteAccManager.find();

            Map<String, List<RERenteAccordeeFamille>> map = new HashMap<String, List<RERenteAccordeeFamille>>();

            if (renteAccManager.size() == 0) {
                // Tiers pour le nom et prénom du tiers qui sera indiqué dans le mail
                TITiers t = new TITiers();
                t.setSession(getSession());
                t.setIdTiers(getIdTiersDemande());
                t.retrieve();

                getLogSession().addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "REGenererTransfertDossierValide",
                                getSession().getLabel("ERREUR_AUCUNE_RA_ENCOURS")));

                demandeRente.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
                demandeRente.update();
            } else {
                Set<String> idTiersExConjoints = chargerExConjoints(demandePrestation.getIdTiers());
                Set<PRTiersWrapper> enfants = SFFamilleUtils.getEnfantsDuTiers(getSession(), getIdTiersDemande());
                for (int i = 0; i < renteAccManager.size(); i++) {
                    RERenteAccordeeFamille ra = (RERenteAccordeeFamille) renteAccManager.get(i);

                    // BZ 5358 si l'id tiers de la base de calcul est dans le Set des id tiers des ex conjoints
                    // on ignore cette rente
                    // BZ 5621 : Si la rente à une date de fin, on l'ignore
                    // BZ 9633 : si la rente est au bénéfice d'un ex-conjoint, on ignore cette rente
                    if (idTiersExConjoints.contains(ra.getIdTiersBaseCalcul())
                            || idTiersExConjoints.contains(ra.getIdTiersBeneficiaire())
                            || !JadeStringUtil.isBlank(ra.getDateFinDroit())) {
                        continue;
                    }

                    // BZ 7370 Si c'est une rente principale et qu'elle appartient à un enfant du tiers de la demande
                    // on ne la considère pas
                    // BZ 9633 Si c'est une API au nom de l'enfant, on ignore aussi cette rente
                    if (isRentePrincipale(ra) || isRenteAPI(ra)) {
                        boolean flag = false;
                        for (PRTiersWrapper enfant : enfants) {
                            if (ra.getIdTiersBeneficiaire().equals(enfant.getIdTiers())) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            continue;
                        }
                    }

                    // Les rentes n'étant pas dans l'état courant validé ou validé sont ignorées
                    if (!IREPrestationAccordee.CS_ETAT_PARTIEL.equals(ra.getCsEtatRenteAccordee())
                            && !IREPrestationAccordee.CS_ETAT_VALIDE.equals(ra.getCsEtatRenteAccordee())) {
                        continue;
                    }

                    if (map.containsKey(ra.getIdTiersAdressePaiement() + ra.getIdDomaineApplicationAdressePaiement())) {
                        List<RERenteAccordeeFamille> list = map.get(ra.getIdTiersAdressePaiement()
                                + ra.getIdDomaineApplicationAdressePaiement());
                        list.add(ra);
                    } else {
                        List<RERenteAccordeeFamille> list = new ArrayList<RERenteAccordeeFamille>();
                        list.add(ra);
                        map.put(ra.getIdTiersAdressePaiement() + ra.getIdDomaineApplicationAdressePaiement(), list);
                    }
                }

                for (List<RERenteAccordeeFamille> list : map.values()) {
                    isCopieOfAi = false;
                    idOfficeAi = "";
                    boolean isOnlyRaCompl = false;
                    int nbRaCompl = 0;

                    if (list.size() == 1) {
                        isRaUnique = true;
                    } else {
                        isRaUnique = false;
                    }

                    for (RERenteAccordeeFamille raPourOfficeAi : list) {
                        if (isRenteAPI(raPourOfficeAi) || (isRenteInvalidite(raPourOfficeAi) && !isCopieOfAi)) {
                            isCopieOfAi = true;

                            // Recherche du code de l'office AI
                            String codeOfficeAi = rechercherOfficeAI(raPourOfficeAi);
                            if (JadeStringUtil.isBlank(codeOfficeAi)) {
                                String message = getSession().getLabel(
                                        "ERREUR_IMPOSSIBLE_REROUVER_OFFICE_AI_RENTE_ACCORDEE");
                                messageErreur.append(message.replace("{0}", raPourOfficeAi.getIdRenteAccordee()));
                                throw new Exception();
                            } else {
                                TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
                                tiAdministrationMgr.setSession(getSession());
                                tiAdministrationMgr.setForCodeAdministration(codeOfficeAi);
                                tiAdministrationMgr.setForGenreAdministration("509004");
                                tiAdministrationMgr.find();
                                tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();
                            }

                            if (null == tiAdministration) {
                                PRTiersWrapper tiers2 = PRTiersHelper.getTiersParId(getSession(),
                                        demandePrestation.getIdTiers());

                                messageErreur.append(getSession().getLabel("ERREUR_PAS_OFFICE_AI_RENSEIGNEE"));
                                messageErreur.append(" ");
                                messageErreur.append(tiers2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                                messageErreur.append(" ");
                                messageErreur.append(tiers2.getProperty(PRTiersWrapper.PROPERTY_NOM));
                                messageErreur.append(" ");
                                messageErreur.append(tiers2.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                                messageErreur.append(".");
                                throw new Exception();
                            }

                        }
                        // Si ra non principale, j'incrémente le nombre de rente
                        // complémentaire
                        if (!"10".equals(raPourOfficeAi.getCodePrestation())
                                && !"20".equals(raPourOfficeAi.getCodePrestation())
                                && !"13".equals(raPourOfficeAi.getCodePrestation())
                                && !"23".equals(raPourOfficeAi.getCodePrestation())
                                && !"50".equals(raPourOfficeAi.getCodePrestation())
                                && !"70".equals(raPourOfficeAi.getCodePrestation())
                                && !"72".equals(raPourOfficeAi.getCodePrestation())) {
                            nbRaCompl++;
                        }
                    }

                    if (nbRaCompl == list.size()) {
                        isOnlyRaCompl = true;
                    }

                    setListeRenteAccordee(list);

                    RERenteAccordeeFamille ra = list.get(0);

                    setIdTiersDemande(ra.getIdTiersBeneficiaire());

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
                                            REGenererTransfertDossierValideProcess.class.getName(), getSession()
                                                    .getLabel("ERREUR_AGENCE_COMMUNALE")
                                                    + " "
                                                    + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                                                    + " "
                                                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + "."));
                        } else {
                            setIsAgenceTrouve(Boolean.TRUE);
                        }
                    }

                    RETransfertValideOO transfertValide;
                    REChangementCaisseOO changementCaisse;

                    if (isAgenceTrouve()) {
                        tiers = (TICompositionTiers) compTiersMgr.getFirstEntity();
                        setIdAgenceCommunale(tiers.getIdTiersEnfant());
                    }

                    // BZ 7062, les JadePublishDocumentInfo était instancié avant la boucle de génération des documents,
                    // c'était donc
                    // toujours les mêmes JadePublishDocumentInfo qui étaient utilisés pour tous les documents ce qui
                    // posait problème pour
                    // les paramètres de mise en GED.

                    JadePublishDocumentInfo docInfoTransfertDossier = this.generateAndFillDocInfo(
                            titreTransfertDossier, IRENoDocumentInfoRom.TRANSFERT_DOSSIER_EN_COURS_A_UNE_AUTRE_CAISSE,
                            isSendToGed());
                    if (isRaUnique) {
                        docInfoTransfertDossier.setDocumentProperty(
                                REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                                REGedUtils.getCleGedPourTypeRente(getSession(),
                                        REGedUtils.getTypeRentePourCetteDemandeRente(getSession(), demandeRente)));
                    } else {
                        Set<CodePrestation> codesPrestationDesRentes = new HashSet<CodePrestation>();

                        for (RERenteAccordeeFamille uneRente : list) {
                            if (!JadeStringUtil.isBlankOrZero(uneRente.getCodePrestation())
                                    && JadeNumericUtil.isInteger(uneRente.getCodePrestation())) {
                                codesPrestationDesRentes.add(CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                                        .getCodePrestation())));
                            }
                        }

                        docInfoTransfertDossier.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                                REGedUtils.getCleGedPourTypeRente(getSession(), REGedUtils
                                        .getTypeRentePourListeCodesPrestation(getSession(), codesPrestationDesRentes,
                                                true)));
                    }

                    JadePublishDocumentInfo docInfoCopieTransfertDossier = this.generateAndFillDocInfo(
                            titreTransfertDossier, IRENoDocumentInfoRom.TRANSFERT_DOSSIER_EN_COURS_A_UNE_AUTRE_CAISSE);

                    JadePublishDocumentInfo docInfoLettreAccompagnement = this.generateAndFillDocInfo(
                            titreTransfertDossier, IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);

                    JadePublishDocumentInfo docInfoChangementCaisse = this.generateAndFillDocInfo(
                            titreChangementCaisse,
                            IRENoDocumentInfoRom.LETTRE_POUR_INDICATION_DE_CHANGEMENT_DE_CAISSE_A_LA_CENTRALE,
                            isSendToGed());
                    if (listeRenteAccordee.size() > 0) {
                        Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();
                        for (RERenteAccordeeFamille uneRente : listeRenteAccordee) {
                            if (!JadeStringUtil.isBlankOrZero(uneRente.getCodePrestation())
                                    && JadeNumericUtil.isInteger(uneRente.getCodePrestation())) {
                                codesPrestation.add(CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                                        .getCodePrestation())));
                            }
                        }
                        docInfoChangementCaisse.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                                REGedUtils.getCleGedPourTypeRente(getSession(), REGedUtils
                                        .getTypeRentePourListeCodesPrestation(getSession(), codesPrestation, true)));
                    } else {
                        docInfoChangementCaisse.setDocumentProperty(
                                REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                                REGedUtils.getCleGedPourTypeRente(getSession(),
                                        REGedUtils.getTypeRentePourCetteDemandeRente(getSession(), demandeRente)));
                    }

                    JadePublishDocumentInfo docInfoCopieChangementCaisse = this.generateAndFillDocInfo(
                            titreChangementCaisse,
                            IRENoDocumentInfoRom.LETTRE_POUR_INDICATION_DE_CHANGEMENT_DE_CAISSE_A_LA_CENTRALE);

                    // 1. Création du document de transfert original
                    transfertValide = createDocumentOriginale();
                    mergedDoc.addDocument(transfertValide.getDocumentData(), docInfoTransfertDossier);

                    if (!isOnlyRaCompl) {
                        // 2. Création du document de changement de caisse
                        // originale
                        changementCaisse = createDocumentChangementOriginale();
                        mergedDoc.addDocument(changementCaisse.getDocumentData(), docInfoChangementCaisse);
                    }

                    // 3. Page de garde pour l'assuré(e)
                    PRLettreEnTeteOO pageGarde = createLettreEnTete(getIdTiersDemande());
                    mergedDoc.addDocument(pageGarde.getDocumentData(), docInfoLettreAccompagnement);

                    // 4. Création de la copie du document de transfert assuré
                    transfertValide = createDocumentCopie();
                    mergedDoc.addDocument(transfertValide.getDocumentData(), docInfoCopieTransfertDossier);

                    if (!isOnlyRaCompl) {
                        // 5. Création du document de changement de caisse copie
                        // pour la nouvelle caisse
                        REChangementCaisseOO changementCopie = createDocumentChangementCopie();
                        mergedDoc.addDocument(changementCopie.getDocumentData(), docInfoCopieChangementCaisse);
                    }

                    if (isAgenceTrouve()) {
                        // 6. Création copie de l'agence communale AVS
                        transfertValide = createDocumentCopie();
                        mergedDoc.addDocument(transfertValide.getDocumentData(), docInfoCopieTransfertDossier);
                    }

                    if (isCopieOfAi) {
                        // 7. Page de garde pour l'assuré(e)
                        PRLettreEnTeteOO pageGardeOfficeAi = createLettreEnTeteOfficeAi(idOfficeAi);
                        mergedDoc.addDocument(pageGardeOfficeAi.getDocumentData(), docInfoLettreAccompagnement);

                        // 8. Création copie de l'office AI
                        transfertValide = createDocumentCopie();
                        mergedDoc.addDocument(transfertValide.getDocumentData(), docInfoCopieTransfertDossier);

                        if (!isOnlyRaCompl) {
                            // 9. Création du document de changement de caisse
                            // copie pour la nouvelle caisse
                            REChangementCaisseOO changementCopieOfficeAi = createDocumentChangementCopie();
                            mergedDoc.addDocument(changementCopieOfficeAi.getDocumentData(),
                                    docInfoCopieChangementCaisse);
                        }
                    }
                }
            }

            for (List<RERenteAccordeeFamille> values : map.values()) {
                for (RERenteAccordeeFamille raATransfere : values) {
                    REBasesCalcul bc = new REBasesCalcul();
                    bc.setSession(getSession());
                    bc.setIdBasesCalcul(raATransfere.getIdBaseCalcul());
                    bc.retrieve();

                    RERenteCalculee rc = new RERenteCalculee();
                    rc.setSession(getSession());
                    rc.setIdRenteCalculee(bc.getIdRenteCalculee());
                    rc.retrieve();

                    REDemandeRente demande = new REDemandeRente();
                    demande.setSession(getSession());
                    demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                    demande.setIdRenteCalculee(rc.getIdRenteCalculee());
                    demande.retrieve();

                    demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
                    demande.update();
                }
            }

            this.createDocuments(mergedDoc);

            // Diminution des rentes accordées
            REDiminutionRenteAccordeeProcess process = new REDiminutionRenteAccordeeProcess();
            //
            for (List<RERenteAccordeeFamille> values : map.values()) {
                for (RERenteAccordeeFamille rad : values) {
                    if (!IREPrestationAccordee.CS_ETAT_DIMINUE.equals(rad.getCsEtatRenteAccordee())
                            && JadeStringUtil.isEmpty(rad.getDateFinDroit())) {
                        process.setSession(getSession());
                        process.setIdRenteAccordee(rad.getIdRenteAccordee());
                        process.setCsCodeMutation(IRERenteAccordee.CS_CODE_MUTATION_AUTRE_EVENEMENT);
                        process.setCsCodeTraitement("");
                        process.setDateFinDroit(getCessationPaiement());
                        process.setEMailAddress(getEMailAddress());
                        // empêche la double diminution des rentes complémentaires vieillesse
                        process.setIsDiminuerAutomatiquementLesRentesVieillesseComplementaires(false);
                        process.start();
                    }
                }
            }
        } catch (Exception e) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                            REGenererTransfertDossierValideProcess.class.getName(), JadeStringUtil
                                    .isBlank(messageErreur.toString()) ? e.toString() : messageErreur.toString()));
        } finally {
            try {
                if (getLogSession().hasMessages()) {
                    List<String> emails = new ArrayList<String>();
                    emails.add(getEMailAddress());
                    sendCompletionMail(emails);
                }
            } catch (Exception e) {
                new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                        REGenererTransfertDossierValideProcess.class.getName(), getSession().getLabel(
                                "ERREUR_ENVOI_MAIL_COMPLETION"));
            }
        }
    }

    /**
     * Recherche de l'office AI depuis la rente accordée. La recherche est d'abord réalisée dans la base de calcul et
     * ensuite dans la demande
     * de rente.</br>
     * La rente accordée doit être de type AI ou API.
     * 
     * @param renteAccordee La rente accordée à analyser
     * @return Le code office AI ou null si non trouvé
     * @throws Exception dans le cas ou des incohérence de données serait détectées
     */
    private String rechercherOfficeAI(RERenteAccordeeFamille renteAccordee) throws Exception {
        String codeOfficeAi = null;
        if (isRenteInvalidite(renteAccordee) || isRenteAPI(renteAccordee)) {
            /*
             * Recherche de l'office AI. L'office AI peut-être présent à 2 places.. Soit dans la base de
             * calcul, soit dans la demande associée à la rente accordée
             */
            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(getSession());
            bc.setIdBasesCalcul(renteAccordee.getIdBaseCalcul());
            bc.retrieve();

            // Si on ne peut trouver la base de calcul, on ne peut pas aller plus loin
            if (bc.isNew()) {
                String message = getSession().getLabel("ERREUR_IMPOSSIBLE_RETROUVER_BASE_CALCUL_LIEE_RENTE_ACCORDEE");
                message = message.replace("{0}", renteAccordee.getIdBaseCalcul());
                messageErreur.append(message.replace("{1}", renteAccordee.getIdRenteAccordee()));
                throw new Exception();
            }

            // Office AI retrouvé dans la base de calcul
            if (!JadeStringUtil.isBlankOrZero(bc.getCodeOfficeAi())) {
                codeOfficeAi = bc.getCodeOfficeAi();
            }
            // Office AI non trouvé, recherche dans la demande associé à la rente
            else {
                // Recherche de la rente calculée pour remonter à la demande
                if (JadeStringUtil.isEmpty(bc.getIdRenteCalculee())) {
                    String message = getSession().getLabel("ERREUR_ID_RENTE_CALCULEE_VIDE_DANS_BASE_CALCUL");
                    messageErreur.append(message.replace("{0}", bc.getIdBasesCalcul()));
                    throw new Exception();
                }
                RERenteCalculee renteCalculee = new RERenteCalculee();
                renteCalculee.setSession(getSession());
                renteCalculee.setIdRenteCalculee(bc.getIdRenteCalculee());
                renteCalculee.retrieve();

                // Normalement il ne doit y en avoir qu'une mais...
                REDemandeRenteManager demandeManager = new REDemandeRenteManager();
                demandeManager.setSession(getSession());
                demandeManager.setForIdRenteCalculee(renteCalculee.getIdRenteCalculee());
                demandeManager.find(BManager.SIZE_NOLIMIT);

                // Analyse du résultat retourné par le manager

                // Pas de résultat ?!
                if (demandeManager.getContainer().size() == 0) {
                    String message = getSession().getLabel("ERREUR_AUCUNE_DEMANDE_TROUVEE_DEPUIS_RENTE_CALCULEE");
                    messageErreur.append(message.replace("{0}", renteCalculee.getIdRenteCalculee()));
                    throw new Exception();
                }

                // Trop de résultat ?!
                else if (demandeManager.getContainer().size() > 1) {
                    String message = getSession().getLabel("ERREUR_PLUSIEURS_DEMANDES_TROUVEES_DEPUIS_RENTE_CALCULEE");
                    messageErreur.append(message.replace("{0}", renteCalculee.getIdRenteCalculee()));
                    throw new Exception();
                }

                // La on est bon, on à trouvé qu'une demande
                else {
                    // Recherche de la demande en fonction du type de la RA
                    if (isRenteAPI(renteAccordee)) {
                        REDemandeRente demandeRente = (REDemandeRente) demandeManager.getFirstEntity();
                        REDemandeRenteAPI demandeAPI = new REDemandeRenteAPI();
                        demandeAPI.setSession(getSession());
                        demandeAPI.setIdDemandeRente(demandeRente.getIdDemandeRente());
                        demandeAPI.retrieve();

                        if (JadeStringUtil.isBlank(demandeAPI.getCodeOfficeAI())) {
                            String message = getSession().getLabel(
                                    "ERREUR_OFFICE_AI_NON_TROUVE_DANS_BASE_CALCUL_ET_DEMANDE_API");
                            message = message.replace("{0}", bc.getIdBasesCalcul());
                            messageErreur.append(message.replace("{1}", demandeAPI.getIdDemandeRente()));
                            throw new Exception();
                        }
                        codeOfficeAi = demandeAPI.getCodeOfficeAI();
                    }

                    else if (isRenteInvalidite(renteAccordee)) {
                        REDemandeRente demandeRente = (REDemandeRente) demandeManager.getFirstEntity();
                        REDemandeRenteInvalidite demandeAI = new REDemandeRenteInvalidite();
                        demandeAI.setSession(getSession());
                        demandeAI.setIdDemandeRente(demandeRente.getIdDemandeRente());
                        demandeAI.retrieve();

                        if (JadeStringUtil.isBlank(demandeAI.getCodeOfficeAI())) {
                            String message = getSession().getLabel(
                                    "ERREUR_OFFICE_AI_NON_TROUVE_DANS_BASE_CALCUL_ET_DEMANDE_INVALIDITE");
                            message.replace("{0}", bc.getIdBasesCalcul());
                            messageErreur.append(message.replace("{1}", demandeAI.getIdDemandePrestation()));
                            throw new Exception();
                        }
                        codeOfficeAi = demandeAI.getCodeOfficeAI();
                    }

                    else {
                        String message = getSession().getLabel(
                                "ERREUR_PAS_DEMANDE_DE_TYPE_AI_OU_API_TROUVEE_DEPUIS_RENTE_CALCULEE");
                        messageErreur.append(message.replace("{0}", renteCalculee.getIdRenteCalculee()));
                        throw new Exception();
                    }
                }
            }
        }
        return codeOfficeAi;
    }

    public void setCessationPaiement(final String cessationPaiement) {
        CessationPaiement = cessationPaiement;
    }

    public void setDateEnvoi(final String dateEnvoi) {
        DateEnvoi = dateEnvoi;
    }

    private void setInfo(final PRInfoCompl info) {
        this.info = info;
    }

    public void setListeRenteAccordee(final List<RERenteAccordeeFamille> list) {
        listeRenteAccordee = list;
    }

    public void setMotifTransmission(final String motifTransmission) {
        this.motifTransmission = motifTransmission;
    }

    public void setNumAgence(final String numAgence) {
        NumAgence = numAgence;
    }

    public void setNumCaisse(final String numCaisse) {
        NumCaisse = numCaisse;
    }

    public void setRemarque(final String remarque) {
        this.remarque = remarque;
    }
}
