package ch.globaz.corvus.process.attestationsfiscales;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.prestation.domaine.CodePrestation;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.attestationsFiscales.REDonneesPourAttestationsFiscales;
import globaz.corvus.db.attestationsFiscales.REDonneesPourAttestationsFiscalesManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.excel.REListeExcelAttestationsFiscalesNonSorties;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.topaz.REAttestationsFiscalesOO;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

public class REGenererAttestationsFiscalesProcess extends BProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String annee;
    private List<REDonneesPourAttestationsFiscales> attestationInvalides;
    private List<JadeCodeSysteme> codesSystemesGenresRentes;
    private String dateImpression;
    private boolean isSendToGed;
    private String NssA;
    private String NssDe;

    private REAbstractAnalyseurLot analyseurLot1;
    private REAbstractAnalyseurLot analyseurLot2;
    private REAbstractAnalyseurLot analyseurLot3;
    private REAbstractAnalyseurLot analyseurLot4;
    private REAbstractAnalyseurLot analyseurLot5;
    private REAbstractAnalyseurLot analyseurLot6;
    private REAbstractAnalyseurLot analyseurLot7;
    private REAbstractAnalyseurLot analyseurLot8;

    private List<REFamillePourAttestationsFiscales> famillesDuLot1;
    private List<REFamillePourAttestationsFiscales> famillesDuLot2;
    private List<REFamillePourAttestationsFiscales> famillesDuLot3;
    private List<REFamillePourAttestationsFiscales> famillesDuLot4;
    private List<REFamillePourAttestationsFiscales> famillesDuLot5;
    private List<REFamillePourAttestationsFiscales> famillesDuLot6;
    private List<REFamillePourAttestationsFiscales> famillesDuLot7;
    private List<REFamillePourAttestationsFiscales> famillesDuLot8;
    private List<REFamillePourAttestationsFiscales> famillesSansLot;
    private List<REFamillePourAttestationsFiscales> famillesAvecRentesAPIOnly;

    public REGenererAttestationsFiscalesProcess() {
        super();

        famillesDuLot1 = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesDuLot2 = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesDuLot3 = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesDuLot4 = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesDuLot5 = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesDuLot6 = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesDuLot7 = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesDuLot8 = new ArrayList<REFamillePourAttestationsFiscales>();

        famillesSansLot = new ArrayList<REFamillePourAttestationsFiscales>();
        famillesAvecRentesAPIOnly = new ArrayList<REFamillePourAttestationsFiscales>();
        codesSystemesGenresRentes = new ArrayList<JadeCodeSysteme>();
        attestationInvalides = new ArrayList<REDonneesPourAttestationsFiscales>();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));

            analyseurLot1 = new REAnalyseurLot1(annee);
            analyseurLot2 = new REAnalyseurLot2(annee);
            analyseurLot3 = new REAnalyseurLot3(annee);
            analyseurLot4 = new REAnalyseurLot4(annee);
            analyseurLot5 = new REAnalyseurLot5(annee);
            analyseurLot6 = new REAnalyseurLot6(annee);
            analyseurLot7 = new REAnalyseurLot7(annee);
            analyseurLot8 = new REAnalyseurLot8(annee);

            JadeCodeSystemeService codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();
            codesSystemesGenresRentes = codeSystemeService.getFamilleCodeSysteme("REGENRPRST");

            List<REFamillePourAttestationsFiscales> familles = recupererEtAgregerLesDonness();
            for (REFamillePourAttestationsFiscales uneFamille : familles) {

                // Les familles avec que des rentes API sont écartées dans un lot spécifique -> pas d'attestation pour
                // ces gens la !
                if (hasOnlyRenteAPI(uneFamille)) {
                    famillesAvecRentesAPIOnly.add(uneFamille);
                    continue;
                }

                // les familles sans adresses de courrier sortent sur la feuille Excel des non sorties
                RETiersPourAttestationsFiscales tiersPourCorrespondance = REAttestationsFiscalesUtils
                        .getTiersCorrespondanceAvecAdresseValide(uneFamille, annee);

                if (tiersPourCorrespondance == null) {
                    famillesSansLot.add(uneFamille);
                    continue;
                } else {
                    uneFamille.setTiersPourCorrespondance(tiersPourCorrespondance);
                }
                if (REAttestationsFiscalesUtils.isAjournementMontant0(uneFamille, 0)) {
                    famillesSansLot.add(uneFamille);
                    continue;
                }

                if (REAttestationsFiscalesUtils.hasRenteDateFinAvantDebutUniquement(uneFamille)) {
                    famillesSansLot.add(uneFamille);
                    continue;
                }

                // Les lots 1 à 4 ne contiennent pas de rétroactif
                if (analyseurLot1.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(false);
                    famillesDuLot1.add(uneFamille);
                } else if (analyseurLot2.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(false);
                    famillesDuLot2.add(uneFamille);
                } else if (analyseurLot3.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(false);
                    famillesDuLot3.add(uneFamille);
                } else if (analyseurLot4.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(false);
                    famillesDuLot4.add(uneFamille);
                }
                // Les lots 5 à 8 contiennent du rétroactif
                else if (analyseurLot5.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(true);
                    famillesDuLot5.add(uneFamille);
                } else if (analyseurLot6.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(true);
                    uneFamille.setHasRetroactifSurPlusieursAnnees(true);
                    famillesDuLot6.add(uneFamille);
                } else if (analyseurLot7.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(true);
                    famillesDuLot7.add(uneFamille);
                } else if (analyseurLot8.isFamilleDansLot(uneFamille)) {
                    uneFamille.setHasRetroactif(true);
                    uneFamille.setHasRetroactifSurPlusieursAnnees(true);
                    famillesDuLot8.add(uneFamille);
                } else {
                    famillesSansLot.add(uneFamille);
                }
            }

            ecrireLesStatistiquesDansLeMail();
            creerFichierDeStatistiquesExcel();
            lancerProcessusOpenOffice();

            return true;
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    /**
     * Test si la famille ne possède que des rentes de type API
     *
     * @param famille
     * @return <code>true</code> si la famille ne possède que des rentes de type API
     */
    private boolean hasOnlyRenteAPI(REFamillePourAttestationsFiscales famille) {
        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            CodePrestation codePrestation = CodePrestation
                    .getCodePrestation(Integer.parseInt(uneRente.getCodePrestation()));
            if (codePrestation.isAPI()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private void chargerAdresseCourrierEtTitreTiers(RETiersPourAttestationsFiscales tiers) throws Exception {
        ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);

        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getIdTiers());

        ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }

        tiers.setTitreTiers(tiersTitre.getFormulePolitesse(tiers.getCsLangue()));
        String adresseCourrier = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), tiers.getIdTiers(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

        if (JadeStringUtil.isBlank(adresseCourrier)) {
            adresseCourrier = PRTiersHelper.getAdresseDomicileFormatee(getSession(), tiers.getIdTiers());
        }

        if (JadeStringUtil.isBlank(adresseCourrier) && (tiers.getRentes().size() > 0)) {
            Iterator<RERentePourAttestationsFiscales> iterateurDesRentes = tiers.getRentes().iterator();
            do {
                RERentePourAttestationsFiscales uneRenteDuTiers = iterateurDesRentes.next();

                TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(getSession(), null,
                        uneRenteDuTiers.getIdTiersAdressePaiement(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, null,
                        null);
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                source.load(adresse);

                new TIAdressePaiementBeneficiaireFormater().format(source);

            } while (iterateurDesRentes.hasNext() && JadeStringUtil.isBlank(adresseCourrier));
        }

        tiers.setAdresseCourrierFormatee(adresseCourrier);
    }

    private void chargerAdressesCourrierEtTitresFamilles(List<REFamillePourAttestationsFiscales> familles)
            throws Exception {

        for (REFamillePourAttestationsFiscales uneFamille : familles) {
            RETiersPourAttestationsFiscales tiersRequerant = uneFamille.getTiersRequerant();

            chargerAdresseCourrierEtTitreTiers(tiersRequerant);

            // on charge les adresses de courrier de chacun des membres de la famille pour les attestations fiscales
            // de rente de survivant
            if (REAttestationsFiscalesUtils.isAttestationRenteSurvivant(uneFamille)) {
                for (RETiersPourAttestationsFiscales unTiersBeneficiaire : uneFamille.getTiersBeneficiaires()) {
                    chargerAdresseCourrierEtTitreTiers(unTiersBeneficiaire);
                    if (JadeStringUtil.isBlank(unTiersBeneficiaire.getAdresseCourrierFormatee())) {
                        unTiersBeneficiaire.setAdresseCourrierFormatee(getAdressePaiementFormatee(unTiersBeneficiaire));
                    }
                }
            }
        }
    }

    private void creerFichierDeStatistiquesExcel() {
        if (hasAttesationsNonSorties()) {
            REGenrererListeExcelAttestationsNonSortiesProcess process = new REGenrererListeExcelAttestationsNonSortiesProcess(
                    new REListeExcelAttestationsFiscalesNonSorties(getSession(), annee, famillesSansLot),
                    getEMailAddress());
            process.run();
        }
    }

    private boolean hasAttesationsNonSorties() {
        return famillesSansLot.size() > 0;
    }

    private void ecrireLesStatistiquesDansLeMail() {
        String intro = FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_INTRO"), annee);
        StringBuilder message = new StringBuilder(intro);

        if (!hasAttesationsNonSorties()) {
            message.append("\n\n");
            message.append(getSession().getLabel("ATTESTATION_FISCALE_PAS_ATTESTATION_NON_SORTIE"));
            message.append("\n\n");
            logMailInfo(message.toString());
            message = new StringBuilder();
        }

        logMailInfo("\n\n");

        message = new StringBuilder();

        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_1"), famillesDuLot1.size())
                        + "\n");
        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_2"), famillesDuLot2.size())
                        + "\n");
        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_3"), famillesDuLot3.size())
                        + "\n");
        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_4"), famillesDuLot4.size())
                        + "\n");
        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_5"), famillesDuLot5.size())
                        + "\n");
        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_6"), famillesDuLot6.size())
                        + "\n");
        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_7"), famillesDuLot7.size())
                        + "\n");
        logMailInfo(
                FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_8"), famillesDuLot8.size())
                        + "\n");

        logMailInfo(getSession().getLabel("ATTESTATION_FISCALE_STAT_LOT_REMARQUE") + "\n");

        logMailInfo(message.toString());
        message = new StringBuilder(intro);

        message.append("\n\n");
        Integer total = famillesDuLot1.size() + famillesDuLot2.size() + famillesDuLot3.size() + famillesDuLot4.size()
                + famillesDuLot5.size() + famillesDuLot6.size() + famillesDuLot7.size() + +famillesDuLot8.size();
        message.append(FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_TOTAL"), total, annee))
                .append("\n");
        message.append(FWMessageFormat.format(getSession().getLabel("ATTESTATION_FISCALE_STAT_SANS_LOT"),
                famillesSansLot.size())).append("\n");

        logMailInfo(message.toString());

        // descriptions des lots
        for (int ctr = 1; ctr < 9; ctr++) {
            message = new StringBuilder();
            message.append("\n").append(getSession().getLabel("ATTESTATION_FISCALE_DESCRIPTION_LOT_" + ctr))
                    .append("\n");
            logMailInfo(message.toString());
        }

        message = new StringBuilder();
        message.append("\n").append(getSession().getLabel("ATTESTATION_FISCALE_DESCRIPTION_LOT_REMARQUE")).append("\n");

        logMailInfo(message.toString());

        // / On vas insérer dans le mail la liste des attestations avec un code
        // system rente invalide
        for (REDonneesPourAttestationsFiscales attestation : attestationInvalides) {
            message = new StringBuilder();
            message.append("\n")
                    .append("Le code système [" + attestation.getCodePrestation() + "." + attestation.getFractionRente()
                            + "] ne correspond à aucune rente valide pour le cas : "
                            + attestation.getNumeroAvsTiersBaseCalcul());

            message.append("\n");
            logMailInfo(message.toString());
        }
    }

    /**
     * Ajoute une ligne d'info dans le mail
     *
     * @param message
     */
    private void logMailInfo(String message) {
        getMemoryLog().logMessage(message, FWMessage.INFORMATION,
                REGenererAttestationsFiscalesProcess.class.getSimpleName());
    }

    private String getAdressePaiementFormatee(RETiersPourAttestationsFiscales unTiersBeneficiaire) {
        String idTiersAdressePaiement = null;

        for (RERentePourAttestationsFiscales uneRenteDuTiers : unTiersBeneficiaire.getRentes()) {
            CodePrestation codePrestation = CodePrestation
                    .getCodePrestation(Integer.parseInt(uneRenteDuTiers.getCodePrestation()));
            if (codePrestation.isAI()) {
                continue;
            }
            idTiersAdressePaiement = uneRenteDuTiers.getIdTiersAdressePaiement();
            if (!JadeStringUtil.isBlank(idTiersAdressePaiement)) {
                break;
            }
        }

        if (JadeStringUtil.isBlank(idTiersAdressePaiement)) {
            return "";
        }

        try {
            TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(getSession(), null,
                    idTiersAdressePaiement, REApplication.CS_DOMAINE_ADRESSE_CORVUS, null, null);
            TIAdressePaiementDataSource dataSource = new TIAdressePaiementDataSource();
            dataSource.load(adressePaiement);
            return new TIAdresseFormater().format(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getAnnee() {
        return annee;
    }

    private String getCodeIsoFromCsLangue(String csLangueTiersBaseCalcul) {
        if (ITITiers.CS_FRANCAIS.equals(csLangueTiersBaseCalcul)) {
            return "fr";
        } else if (ITITiers.CS_ALLEMAND.equals(csLangueTiersBaseCalcul)) {
            return "de";
        } else if (ITITiers.CS_ITALIEN.equals(csLangueTiersBaseCalcul)) {
            return "it";
        }
        return null;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    @Override
    protected String getEMailObject() {
        StringBuilder subject = new StringBuilder();
        subject.append(getSession().getLabel("ATTESTATION_FISCALE_RENTE")).append(" ").append(annee);
        return subject.toString();
    }

    /**
     * @return the isSendToGed
     */
    public final boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * @return the nssA
     */
    public String getNssA() {
        return NssA;
    }

    /**
     * @return the nssDe
     */
    public String getNssDe() {
        return NssDe;
    }

    private RERentePourAttestationsFiscales getRente(REDonneesPourAttestationsFiscales uneDonnee, String annee)
            throws Exception {

        RERentePourAttestationsFiscales rente = new RERentePourAttestationsFiscales();
        rente.setIdRenteAccordee(uneDonnee.getIdRenteAccordee());
        rente.setCodePrestation(uneDonnee.getCodePrestation());
        rente.setDateDebutDroit(uneDonnee.getDateDebutDroit());
        rente.setDateDecision(uneDonnee.getDateDecision());
        rente.setDateFinDroit(uneDonnee.getDateFinDroit());
        rente.setFractionRente(uneDonnee.getFractionRente());
        rente.setIdTiersAdressePaiement(uneDonnee.getIdTiersAdressePaiement());
        rente.setIdTiersBeneficiaire(uneDonnee.getIdTiersBeneficiaire());
        rente.setIsRenteBloquee(uneDonnee.getIsPrestationBloquee());

        REPrestationsDuesManager mgr = new REPrestationsDuesManager();
        mgr.setSession(getSession());
        mgr.setForIdRenteAccordes(uneDonnee.getIdRenteAccordee());
        mgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
        mgr.find(BManager.SIZE_NOLIMIT);

        if (mgr.size() == 0) {
            throw new Exception("Aucune PrestationDue trouvée pour la RenteAccordee avec l'id ["
                    + uneDonnee.getIdRenteAccordee() + "] pour l'annee [" + annee + "]");
        }

        String dateDebut = "01.12." + annee;
        String dateDeFinMin = "01.01." + annee;
        List<REPrestationDue> list = new ArrayList<REPrestationDue>();

        for (Object o : mgr.getContainer()) {
            REPrestationDue prestationDue = (REPrestationDue) o;

            if (JadeStringUtil.isBlankOrZero(prestationDue.getDateDebutPaiement())) {
                throw new Exception("Aucune PrestationDue avec l'id [" + prestationDue.getIdPrestationDue()
                        + "] pour la RenteAccordee avec l'id [" + uneDonnee.getIdRenteAccordee()
                        + "] ne possède pas de date de début de paiement");
            }

            // dateDeFin == null && dateDébut <= 12.2012
            if (JadeStringUtil.isBlankOrZero(prestationDue.getDateFinPaiement())) {
                if (JadeDateUtil.isDateBefore("01." + prestationDue.getDateDebutPaiement(), dateDebut)
                        || dateDebut.equals("01." + prestationDue.getDateDebutPaiement())) {
                    list.add(prestationDue);
                }
            }

            // dateDeFin <> 0 && dateDeFin > 00.2012 && dateDeDebut <= 12.2012
            if (!JadeStringUtil.isBlankOrZero(prestationDue.getDateFinPaiement())) {
                if (JadeDateUtil.isDateAfter("01." + prestationDue.getDateFinPaiement(), dateDeFinMin)
                        || dateDeFinMin.equals("01." + prestationDue.getDateFinPaiement())) {
                    if (JadeDateUtil.isDateBefore("01." + prestationDue.getDateDebutPaiement(), dateDebut)
                            || dateDebut.equals("01." + prestationDue.getDateDebutPaiement())) {
                        list.add(prestationDue);
                    }
                }
            }
        }

        if (list.size() == 0) {
            throw new Exception("Aucune PrestationDue trouvé pour la RenteAccordee avec l'id ["
                    + uneDonnee.getIdRenteAccordee() + "] pour l'année [" + annee + "]");
        }
        if (list.size() > 1) {
            throw new Exception("Plusieurs PrestationDue trouvées pour la RenteAccordee avec l'id ["
                    + uneDonnee.getIdRenteAccordee() + "] pour l'année [" + annee + "]");
        }

        rente.setMontantPrestation(list.get(0).getMontant());

        return rente;
    }

    private RERetenuePourAttestationsFiscales getRetenue(REDonneesPourAttestationsFiscales uneDonnee) {
        RERetenuePourAttestationsFiscales retenue = new RERetenuePourAttestationsFiscales();
        retenue.setIdRetenue(uneDonnee.getIdRetenue());
        retenue.setCsType(uneDonnee.getCsTypeRetenue());
        retenue.setDateDebut(uneDonnee.getDateDebutRetenue());
        retenue.setDateFin(uneDonnee.getDateFinRetenue());
        return retenue;
    }

    private RETiersPourAttestationsFiscales getTiersBaseCalcul(REDonneesPourAttestationsFiscales uneDonnee) {
        RETiersPourAttestationsFiscales tiers = new RETiersPourAttestationsFiscales();
        tiers.setIdTiers(uneDonnee.getIdTiersBaseCalcul());
        tiers.setNom(uneDonnee.getNomTiersBaseCalcul());
        tiers.setPrenom(uneDonnee.getPrenomTiersBaseCalcul());
        tiers.setNumeroAvs(uneDonnee.getNumeroAvsTiersBaseCalcul());
        tiers.setCsSexe(uneDonnee.getCsSexeTiersBaseCalcul());
        tiers.setDateNaissance(uneDonnee.getDateNaissanceTiersBaseCalcul());
        tiers.setDateDeces(uneDonnee.getDateDecesTiersBaseCalcul());
        tiers.setCsLangue(uneDonnee.getCsLangueTiersBaseCalcul());
        tiers.setCodeIsoLangue(getCodeIsoFromCsLangue(uneDonnee.getCsLangueTiersBaseCalcul()));
        return tiers;
    }

    private RETiersPourAttestationsFiscales getTiersBeneficiaire(REDonneesPourAttestationsFiscales uneDonnee) {
        RETiersPourAttestationsFiscales tiers = new RETiersPourAttestationsFiscales();
        tiers.setIdTiers(uneDonnee.getIdTiersBeneficiaire());
        tiers.setNom(uneDonnee.getNomTiersBeneficiaire());
        tiers.setPrenom(uneDonnee.getPrenomTiersBeneficiaire());
        tiers.setNumeroAvs(uneDonnee.getNumeroAvsTiersBeneficiaire());
        tiers.setCsSexe(uneDonnee.getCsSexeTiersBeneficiaire());
        tiers.setDateNaissance(uneDonnee.getDateNaissanceTiersBeneficiaire());
        tiers.setDateDeces(uneDonnee.getDateDecesTiersBeneficiaire());
        tiers.setCsLangue(uneDonnee.getCsLangueTiersBeneficiaire());
        tiers.setCodeIsoLangue(getCodeIsoFromCsLangue(uneDonnee.getCsLangueTiersBeneficiaire()));
        return tiers;
    }

    public SortedSet<REFamillePourAttestationsFiscales> getToutesLesFamillesDansLots() {
        SortedSet<REFamillePourAttestationsFiscales> toutesLesFamillesDansUnLot = new TreeSet<REFamillePourAttestationsFiscales>();
        toutesLesFamillesDansUnLot.addAll(famillesDuLot1);
        toutesLesFamillesDansUnLot.addAll(famillesDuLot2);
        toutesLesFamillesDansUnLot.addAll(famillesDuLot3);
        toutesLesFamillesDansUnLot.addAll(famillesDuLot4);
        toutesLesFamillesDansUnLot.addAll(famillesDuLot5);
        toutesLesFamillesDansUnLot.addAll(famillesDuLot6);
        toutesLesFamillesDansUnLot.addAll(famillesDuLot7);
        toutesLesFamillesDansUnLot.addAll(famillesDuLot8);
        return toutesLesFamillesDansUnLot;
    }

    private String getTraductionGenreRente(String csGenreRente, String codeIsoLangue) {
        for (JadeCodeSysteme unCodeSysteme : codesSystemesGenresRentes) {
            if (unCodeSysteme.getIdCodeSysteme().equals(csGenreRente)) {
                return unCodeSysteme.getTraduction(Langues.getLangueDepuisCodeIso(codeIsoLangue));
            }
        }
        return null;
    }

    private final JadeContext initContext(BSession session) throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        return ctxtImpl;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Lance un processus OpenOffice par langue
     *
     * @throws Exception
     */
    private void lancerProcessusOpenOffice() throws Exception {

        // Récupération de la date du dernier paiement mensuel
        String dateDernierPmtMensuel = REPmtMensuel.getDateDernierPmt(getSession());
        if (JadeStringUtil.isBlankOrZero(dateDernierPmtMensuel)
                || REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPmtMensuel)) {
            String message = getSession().getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
            throw new RETechnicalException(message);
        }

        // On récupère toutes les familles dans un Set
        SortedSet<REFamillePourAttestationsFiscales> toutesLesFamilles = getToutesLesFamillesDansLots();

        Map<String, SortedSet<REFamillePourAttestationsFiscales>> famillesParLangues = new HashMap<String, SortedSet<REFamillePourAttestationsFiscales>>();
        // On boucle sur toutes les familles et on les tries par la langue du tiers de correspondance
        for (REFamillePourAttestationsFiscales famille : toutesLesFamilles) {
            String isoLangue = famille.getTiersPourCorrespondance().getCodeIsoLangue();

            if (!famillesParLangues.containsKey(isoLangue)) {
                famillesParLangues.put(isoLangue, new TreeSet<REFamillePourAttestationsFiscales>());
            }
            famillesParLangues.get(isoLangue).add(famille);
        }

        // Pour chacune des langues on lance un nouveau processus open office
        for (String codeIsoLangue : famillesParLangues.keySet()) {
            REAttestationsFiscalesOO attestationsFiscalesOO = creerProcessOO();
            attestationsFiscalesOO.setDateDernierPaiement(dateDernierPmtMensuel);
            attestationsFiscalesOO.setCodeIsoLangue(codeIsoLangue);
            attestationsFiscalesOO.setFamilles(famillesParLangues.get(codeIsoLangue));
            BProcessLauncher.startJob(attestationsFiscalesOO);
        }

    }

    private REAttestationsFiscalesOO creerProcessOO() {
        REAttestationsFiscalesOO attestationsFiscalesOO = new REAttestationsFiscalesOO();
        attestationsFiscalesOO.setSession(getSession());
        attestationsFiscalesOO.setAdresseEmail(getEMailAddress());
        attestationsFiscalesOO.setAnnee(getAnnee());
        attestationsFiscalesOO.setDateImpression(getDateImpression());
        attestationsFiscalesOO.setIsSendToGed(getIsSendToGed());
        return attestationsFiscalesOO;
    }

    private List<REFamillePourAttestationsFiscales> recupererEtAgregerLesDonness() throws Exception {
        REDonneesPourAttestationsFiscalesManager manager = new REDonneesPourAttestationsFiscalesManager();
        manager.setSession(getSession());
        manager.setForAnnee(annee);
        manager.setForNssA(getNssA());
        manager.setForNssDe(getNssDe());
        manager.find(BManager.SIZE_NOLIMIT);

        List<REDonneesPourAttestationsFiscales> attestationValides = new ArrayList<REDonneesPourAttestationsFiscales>();

        // Test que les codes rentes avec la fraction soit valides
        for (int i = 0; i < manager.size(); i++) {
            REDonneesPourAttestationsFiscales attestation = (REDonneesPourAttestationsFiscales) manager.get(i);
            StringBuilder csCodePrestation = new StringBuilder();
            csCodePrestation.append("52821").append(attestation.getCodePrestation());
            if (JadeStringUtil.isBlankOrZero(attestation.getFractionRente())) {
                csCodePrestation.append("0");
            } else {
                csCodePrestation.append(attestation.getFractionRente());
            }
            // Le code ISO de la langue est en dure...
            // C'est n'est pas le sommet mais dans notre cas, on veut simplement tester le fait que le code system
            // existe. Ce test ne doit pas être tributaire de la langue.
            if (getTraductionGenreRente(csCodePrestation.toString(), "fr") == null) {
                attestationInvalides.add(attestation);
            } else {
                attestationValides.add(attestation);
            }
        }

        // TODO supprimer cette boucle inutile, les données sont à double
        // et cause des problèmes à la génération des docs si une fraction rentes est invalides...
        // for (int i = 0; i < manager.size(); i++) {
        // attestationValides.add((REDonneesPourAttestationsFiscales) manager.get(i));
        // }

        Set<String> listeBeneficiairePCDecembre = rechercherLesPcs();

        return transformer(attestationValides, annee, listeBeneficiairePCDecembre);
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public final void setIsSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setNssA(String nssA) {
        NssA = nssA;
    }

    public void setNssDe(String nssDe) {
        NssDe = nssDe;
    }

    private Set<String> rechercherLesPcs() {
        BPreparedStatement statement = null;
        Set<String> set = new HashSet<String>();
        try {
            statement = new BPreparedStatement(getSession().getCurrentThreadTransaction());
            statement.prepareStatement(creerRequeteRecherchePCs(getAnneeAsInteger()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String idTiers = String.valueOf(resultSet.getInt(1));
                if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                    set.add(idTiers);
                }
            }

        } catch (Exception e) {
            // What can i do with this here ?! Nothing, so y re-throw them
            throw new IllegalStateException("Exception occur when executing request : " + e.toString(), e);
        } finally {
            if (statement != null) {
                statement.closePreparedStatement();
            }
        }
        return set;
    }

    private String creerRequeteRecherchePCs(int annee) {
        StringBuilder request = new StringBuilder();
        String schema = Jade.getInstance().getDefaultJdbcSchema();
        request.append("SELECT distinct(ZTITBE)");
        request.append(" from " + schema + ".REPRACC AS repraccPC");
        request.append(" INNER JOIN " + schema + ".PCPCACC AS pcAccordee ON");
        request.append(" (");
        request.append(
                "    repraccPC.ZTIPRA = pcAccordee.CUIPRA OR (repraccPC.ZTIPRA = pcAccordee.CUIPRC and pcAccordee.CUIPRC<>0)");
        request.append(" )");
        request.append(" INNER JOIN " + schema + ".PCPLCAL AS pcPlancalcul ON");
        request.append(" pcAccordee.CUIPCA = pcPlancalcul.CVIPCA ");
        request.append(" AND pcPlancalcul.CVBPLR = '1' AND pcPlancalcul.CVLEPC in (" + PcaEtatCalcul.OCTROYE.getValue()
                + ", " + PcaEtatCalcul.OCTROY_PARTIEL.getValue() + ")");
        request.append(" AND pcAccordee.CUTETA = 64029002");
        request.append(" AND repraccPC.ZTTGEN = 52849002");
        request.append(" AND pcAccordee.CUBSUP <> 1");
        request.append(" AND pcAccordee.CUDDEB <" + String.valueOf((annee + 1)) + "00"); // année + 1
        request.append(
                " AND (pcAccordee.CUDFIN is null OR pcAccordee.CUDFIN =0 OR pcAccordee.CUDFIN = " + annee + "12)");
        return request.toString();
    }

    /**
     * <p>
     * Découpe l'attestation en plusieurs si des rentes de survivants de cette famille sont payées à des endroits
     * différents.
     * </p>
     * <p>
     * Si des rentes autres que survivants sont payées à des endroits différents, la propriété
     * {@link REFamillePourAttestationsFiscales#hasPlusieursAdressePaiement()} sera à <code>true</code> afin qu'une
     * phrase supplémentaire soit imprimée dans l'attestation fiscale de cette famille.
     * </p>
     *
     * @param values
     * @return
     */
    private List<REFamillePourAttestationsFiscales> spliterSiNecessaire(
            Collection<REFamillePourAttestationsFiscales> values) {
        List<REFamillePourAttestationsFiscales> familles = new ArrayList<REFamillePourAttestationsFiscales>();

        for (REFamillePourAttestationsFiscales uneFamille : values) {

            boolean isRenteSurvivant = false;
            boolean hasAdressePmtAZero = false;
            Map<String, Set<RETiersPourAttestationsFiscales>> tiersParIdAdressePaiement = new HashMap<String, Set<RETiersPourAttestationsFiscales>>();

            for (RETiersPourAttestationsFiscales unTiersBeneficiaire : uneFamille.getTiersBeneficiaires()) {
                for (RERentePourAttestationsFiscales uneRenteDuTiers : unTiersBeneficiaire.getRentes()) {

                    if (JadeStringUtil.isBlankOrZero(uneRenteDuTiers.getIdTiersAdressePaiement())) {
                        hasAdressePmtAZero = true;
                    }

                    CodePrestation codePrestation = CodePrestation
                            .getCodePrestation(Integer.parseInt(uneRenteDuTiers.getCodePrestation()));
                    if (codePrestation.isAPI()) {
                        continue;
                    }
                    if (codePrestation.isSurvivant()) {
                        isRenteSurvivant = true;
                    }

                    if (tiersParIdAdressePaiement.containsKey(uneRenteDuTiers.getIdTiersAdressePaiement())) {
                        Set<RETiersPourAttestationsFiscales> tiersAvecCetteAdressePaiement = tiersParIdAdressePaiement
                                .get(uneRenteDuTiers.getIdTiersAdressePaiement());
                        tiersAvecCetteAdressePaiement.add(unTiersBeneficiaire);
                    } else {
                        tiersParIdAdressePaiement.put(uneRenteDuTiers.getIdTiersAdressePaiement(),
                                new HashSet<RETiersPourAttestationsFiscales>(Arrays.asList(unTiersBeneficiaire)));
                    }
                }
            }

            if (tiersParIdAdressePaiement.size() > 1) {
                if (isRenteSurvivant) {
                    for (Set<RETiersPourAttestationsFiscales> tiersPourUneAdresse : tiersParIdAdressePaiement
                            .values()) {
                        REFamillePourAttestationsFiscales nouvelleFamille = new REFamillePourAttestationsFiscales();
                        for (RETiersPourAttestationsFiscales unTiers : tiersPourUneAdresse) {
                            if (nouvelleFamille.getTiersRequerant() == null) {
                                nouvelleFamille.setTiersRequerant(unTiers);
                            }
                            nouvelleFamille.getMapTiersBeneficiaire().put(unTiers.getIdTiers(), unTiers);
                        }
                        familles.add(nouvelleFamille);
                    }
                } else {
                    if (hasAdressePmtAZero && tiersParIdAdressePaiement.size() == 2) {
                        uneFamille.setHasPlusieursAdressePaiement(false);
                    } else {
                        uneFamille.setHasPlusieursAdressePaiement(true);
                    }
                    familles.add(uneFamille);
                }
            } else {
                familles.add(uneFamille);
            }
        }

        return familles;
    }

    private int getAnneeAsInteger() {
        return Integer.valueOf(annee);
    }

    /**
     * Regroupe les données brutes par tiers requérant (tiers auquel l'attestation fiscale sera envoyée) et ajoute tous
     * les tiers bénéficiaires (et leurs rentes) liés à ce tiers requérant (par ID tiers de la base de calcul)
     *
     * @param donnees
     *            les données brutes chargées par {@link REDonneesPourAttestationsFiscalesManager}
     * @return les données regroupées par tiers requérant
     * @throws Exception
     *             dans le cas où un problème survient lors de la récupération des adresses de courrier/paiement et du
     *             titre du tiers requérant
     */
    public List<REFamillePourAttestationsFiscales> transformer(List<REDonneesPourAttestationsFiscales> donnees,
            String annee, Set<String> listeBeneficiairePCDecembre) throws Exception {
        Map<String, REFamillePourAttestationsFiscales> famillesParIdTiersBaseCalcul = new HashMap<String, REFamillePourAttestationsFiscales>();

        for (REDonneesPourAttestationsFiscales uneDonnee : donnees) {
            REFamillePourAttestationsFiscales uneFamille;
            if (famillesParIdTiersBaseCalcul.containsKey(uneDonnee.getIdTiersBaseCalcul())) {
                uneFamille = famillesParIdTiersBaseCalcul.get(uneDonnee.getIdTiersBaseCalcul());
            } else {
                uneFamille = new REFamillePourAttestationsFiscales();
                RETiersPourAttestationsFiscales tiersRequerant = getTiersBaseCalcul(uneDonnee);
                // ADD
                if (listeBeneficiairePCDecembre.contains(tiersRequerant.getIdTiers())) {
                    tiersRequerant.setHasPcEnDecembre(true);
                }
                // ADD
                uneFamille.setTiersRequerant(tiersRequerant);
                famillesParIdTiersBaseCalcul.put(uneDonnee.getIdTiersBaseCalcul(), uneFamille);
            }

            RETiersPourAttestationsFiscales unTiersBeneficaire;
            if (uneFamille.getMapTiersBeneficiaire().containsKey(uneDonnee.getIdTiersBeneficiaire())) {
                unTiersBeneficaire = uneFamille.getMapTiersBeneficiaire().get(uneDonnee.getIdTiersBeneficiaire());
            } else {
                unTiersBeneficaire = getTiersBeneficiaire(uneDonnee);
                // ADD
                if (listeBeneficiairePCDecembre.contains(unTiersBeneficaire.getIdTiers())) {
                    unTiersBeneficaire.setHasPcEnDecembre(true);
                }
                // ADD

                uneFamille.getMapTiersBeneficiaire().put(uneDonnee.getIdTiersBeneficiaire(), unTiersBeneficaire);
            }

            RERentePourAttestationsFiscales uneRenteDuBeneficiaire = null;
            if (unTiersBeneficaire.getMapRentes().containsKey(uneDonnee.getIdRenteAccordee())) {
                uneRenteDuBeneficiaire = unTiersBeneficaire.getMapRentes().get(uneDonnee.getIdRenteAccordee());
            } else {
                try {
                    uneRenteDuBeneficiaire = getRente(uneDonnee, annee);
                    unTiersBeneficaire.getMapRentes().put(uneDonnee.getIdRenteAccordee(), uneRenteDuBeneficiaire);
                } catch (Exception e) {
                    String message = "Erreur pour le tiers bénéficiaire [" + uneDonnee.getNumeroAvsTiersBeneficiaire()
                            + "] : " + e.toString();
                    getMemoryLog().logMessage(message, FWViewBeanInterface.WARNING,
                            REGenererAttestationsFiscalesProcess.class.getSimpleName());
                }
            }
            if (uneRenteDuBeneficiaire != null) {
                if (!uneRenteDuBeneficiaire.getMapRetenues().containsKey(uneDonnee.getIdRetenue())) {
                    uneRenteDuBeneficiaire.getMapRetenues().put(uneDonnee.getIdRetenue(), getRetenue(uneDonnee));
                }
            }
        }

        List<REFamillePourAttestationsFiscales> familles = spliterSiNecessaire(famillesParIdTiersBaseCalcul.values());

        chargerAdressesCourrierEtTitresFamilles(familles);

        // tri par ordre alphabétique (nom, prénom) des tiers requérant
        Collections.sort(familles);
        return familles;
    }
}
