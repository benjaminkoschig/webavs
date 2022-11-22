package ch.globaz.corvus.process.attestationsfiscales;

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
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.*;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.util.*;

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


                // Les familles avec que des rentes API sont �cart�es dans un lot sp�cifique -> pas d'attestation pour
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
                if (REAttestationsFiscalesUtils.isAjournementMontant0(uneFamille)) {
                    famillesSansLot.add(uneFamille);
                    continue;
                }

                if (REAttestationsFiscalesUtils.hasRenteDateFinAvantDebutUniquement(uneFamille)) {
                    famillesSansLot.add(uneFamille);
                    continue;
                }
                if (REAttestationsFiscalesUtils.hasDecisionEnDecembreAndCreancier(uneFamille, getSession(), annee)) {
                    famillesSansLot.add(uneFamille);
                    continue;
                }
                if (REAttestationsFiscalesUtils.hasRenteQuiSeChevauchent(uneFamille, getAnneeAsInteger())) {
                    famillesSansLot.add(uneFamille);
                    continue;
                }
                if (REAttestationsFiscalesUtils.hasDecisionRetroDateCourantAndDecisionCourantDateRetro(uneFamille, getSession(), getAnneeAsInteger())) {
                    famillesSansLot.add(uneFamille);
                    continue;
                }
                // Les lots 1 � 4 ne contiennent pas de r�troactif
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
                // Les lots 5 � 8 contiennent du r�troactif
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
     * Test si la famille ne poss�de que des rentes de type API
     *
     * @param famille
     * @return <code>true</code> si la famille ne poss�de que des rentes de type API
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
        String titreFromAdresse;
        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getIdTiers());

        ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }


        String adresseCourrier = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), tiers.getIdTiers(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");
        titreFromAdresse = PRTiersHelper.getTitreFromAdresseCourrier(getSession(), tiers.getIdTiers(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

        if (JadeStringUtil.isBlank(adresseCourrier)) {
            adresseCourrier = PRTiersHelper.getAdresseDomicileFormatee(getSession(), tiers.getIdTiers());
            titreFromAdresse = PRTiersHelper.getTitrefromAdresseDomicileFormatee(getSession(), tiers.getIdTiers());
        }
        tiersTitre.setTitreTiers(titreFromAdresse);
        tiers.setTitreTiers(tiersTitre.getFormulePolitesse(tiers.getCsLangue()));
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

    //TODO : A utiliser si on a besoin d'appliquer le titre (Mr,Madame,...) de l'adresse du courier pour des cas sp�cifiques
//    private boolean hasOnlyRenteOrphelins(RETiersPourAttestationsFiscales tiers) {
//        for(RERentePourAttestationsFiscales rentes :   tiers.getRentes()){
//            CodePrestation codePrestation = CodePrestation
//                    .getCodePrestation(Integer.parseInt(rentes.getCodePrestation()));
//                if(codePrestation.isRenteComplementairePourEnfant())
//
//
//        }
//
//
//    }

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
                        if (uneFamille.getTiersRequerant().getIdTiers().equals(unTiersBeneficiaire.getIdTiers())
                                && JadeStringUtil.isBlank(uneFamille.getTiersRequerant().getAdresseCourrierFormatee())) {
                            uneFamille.getTiersRequerant().setAdresseCourrierFormatee(unTiersBeneficiaire.getAdresseCourrierFormatee());
                        }
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

        // / On vas ins�rer dans le mail la liste des attestations avec un code
        // system rente invalide
        for (REDonneesPourAttestationsFiscales attestation : attestationInvalides) {
            message = new StringBuilder();
            message.append("\n")
                    .append("Le code syst�me [" + attestation.getCodePrestation() + "." + attestation.getFractionRente()
                            + "] ne correspond � aucune rente valide pour le cas : "
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
            String titreFromAdresse = dataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE);
            unTiersBeneficiaire.setTitreTiers(titreFromAdresse);
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
        rente.setQuotiteRente(uneDonnee.getQuotiteRente());
        rente.setIdTiersAdressePaiement(uneDonnee.getIdTiersAdressePaiement());
        rente.setIdTiersBeneficiaire(uneDonnee.getIdTiersBeneficiaire());
        rente.setIsRenteBloquee(uneDonnee.getIsPrestationBloquee());

        REPrestationsDuesManager mgr = new REPrestationsDuesManager();
        mgr.setSession(getSession());
        mgr.setForIdRenteAccordes(uneDonnee.getIdRenteAccordee());
        mgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
        mgr.find(BManager.SIZE_NOLIMIT);

        if (mgr.size() == 0) {
            throw new Exception("Aucune PrestationDue trouv�e pour la RenteAccordee avec l'id ["
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
                        + "] ne poss�de pas de date de d�but de paiement");
            }

            // dateDeFin == null && dateD�but <= 12.2012
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
            throw new Exception("Aucune PrestationDue trouv� pour la RenteAccordee avec l'id ["
                    + uneDonnee.getIdRenteAccordee() + "] pour l'ann�e [" + annee + "]");
        }
        if (list.size() > 1) {
            throw new Exception("Plusieurs PrestationDue trouv�es pour la RenteAccordee avec l'id ["
                    + uneDonnee.getIdRenteAccordee() + "] pour l'ann�e [" + annee + "]");
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

        // R�cup�ration de la date du dernier paiement mensuel
        String dateDernierPmtMensuel = REPmtMensuel.getDateDernierPmt(getSession());
        if (JadeStringUtil.isBlankOrZero(dateDernierPmtMensuel)
                || REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPmtMensuel)) {
            String message = getSession().getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
            throw new RETechnicalException(message);
        }

        // On r�cup�re toutes les familles dans un Set
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
        manager.setOrderByDateDecisionDesc(true);
        manager.find(BManager.SIZE_NOLIMIT);

        List<REDonneesPourAttestationsFiscales> attestationValides = new ArrayList<REDonneesPourAttestationsFiscales>();

        // Test que les codes rentes avec la fraction soit valides
        for (int i = 0; i < manager.size(); i++) {
            REDonneesPourAttestationsFiscales attestation = (REDonneesPourAttestationsFiscales) manager.get(i);
            StringBuilder csCodePrestation = new StringBuilder();
            csCodePrestation.append("52821").append(attestation.getCodePrestation());

            if (!JadeStringUtil.isBlankOrZero(attestation.getFractionRente())) {
                csCodePrestation.append(attestation.getFractionRente());
            } else if (Arrays.asList(REGenresPrestations.GENRE_PRESTATIONS_AI).contains(attestation.getCodePrestation())) {
                if (StringUtils.equals(REGenresPrestations.GENRE_50, attestation.getCodePrestation()) || StringUtils.equals(REGenresPrestations.GENRE_70, attestation.getCodePrestation())) {
                    csCodePrestation.append("0");
                } else {
                    csCodePrestation.append("1");
                }
            } else {
                csCodePrestation.append("0");
            }
            // Le code ISO de la langue est en dure...
            // C'est n'est pas le sommet mais dans notre cas, on veut simplement tester le fait que le code system
            // existe. Ce test ne doit pas �tre tributaire de la langue.
            if (getTraductionGenreRente(csCodePrestation.toString(), "fr") == null) {
                attestationInvalides.add(attestation);
            } else {
                attestationValides.add(attestation);
            }
        }

        // TODO supprimer cette boucle inutile, les donn�es sont � double
        // et cause des probl�mes � la g�n�ration des docs si une fraction rentes est invalides...
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
        request.append(" AND pcAccordee.CUDDEB <" + String.valueOf((annee + 1)) + "00"); // ann�e + 1
        request.append(
                " AND (pcAccordee.CUDFIN is null OR pcAccordee.CUDFIN =0 OR pcAccordee.CUDFIN = " + annee + "12)");
        return request.toString();
    }

    /**
     * <p>
     * D�coupe l'attestation en plusieurs si des rentes de survivants de cette famille sont pay�es � des endroits
     * diff�rents.
     * </p>
     * <p>
     * Si des rentes autres que survivants sont pay�es � des endroits diff�rents, la propri�t�
     * {@link REFamillePourAttestationsFiscales#hasPlusieursAdressePaiement()} sera � <code>true</code> afin qu'une
     * phrase suppl�mentaire soit imprim�e dans l'attestation fiscale de cette famille.
     * </p>
     *
     * @param values
     * @return
     */
    private List<REFamillePourAttestationsFiscales> spliterSiNecessaire(
            Collection<REFamillePourAttestationsFiscales> values) {
        List<REFamillePourAttestationsFiscales> familles = new ArrayList<REFamillePourAttestationsFiscales>();
        List<Integer> listCodesForRemarqueAdresseDiff = new ArrayList<>();
        boolean hasRenteEnfant = false;
        boolean hasRenteComplPourConjoint = false;
        for (REFamillePourAttestationsFiscales uneFamille : values) {
            boolean hasRentePrincipal = false;

            List<String> listIdAdressePaiement = new ArrayList<>();

            listCodesForRemarqueAdresseDiff.add(10);
            listCodesForRemarqueAdresseDiff.add(13);
            listCodesForRemarqueAdresseDiff.add(20);
            listCodesForRemarqueAdresseDiff.add(23);
            listCodesForRemarqueAdresseDiff.add(50);
            listCodesForRemarqueAdresseDiff.add(70);
            String idAdressePaiementRentePrincipal = null;
            for (RETiersPourAttestationsFiscales unTiersBeneficiaire : uneFamille.getTiersBeneficiaires()) {
                for (RERentePourAttestationsFiscales uneRenteDuTiers : unTiersBeneficiaire.getRentes()) {

                    CodePrestation codePrestation = CodePrestation
                            .getCodePrestation(Integer.parseInt(uneRenteDuTiers.getCodePrestation()));
                    if (codePrestation.isAPI()) {
                        continue;
                    }
                    /**
                     * V�rifier si la famille une rente principal 10/20, 13/23 ou 50/70 et que l'adresse b�n�ficiaire de la rente correpond � l'adresse principal de l'attestation.
                     */
                    if (listCodesForRemarqueAdresseDiff.contains(codePrestation.getCodePrestation())
                            && uneRenteDuTiers.getIdTiersAdressePaiement().equals(uneFamille.getTiersRequerant().getIdTiers())) {
                        hasRentePrincipal = true;
                        idAdressePaiementRentePrincipal = uneRenteDuTiers.getIdTiersAdressePaiement();
                    }

                }
                List<RERentePourAttestationsFiscales> listRentesSorted = sortRentesForIdAdressePaiement(unTiersBeneficiaire.getRentes());

                for (RERentePourAttestationsFiscales rente : listRentesSorted) {
                    String idAdressePaiement = rente.getIdTiersAdressePaiement();

                    if (!listIdAdressePaiement.contains(idAdressePaiement)
                            && !JadeStringUtil.isBlankOrZero(idAdressePaiement)) {
                        listIdAdressePaiement.add(idAdressePaiement);
                    }
                }
            }

            if (listIdAdressePaiement.size() > 1) {

                splitSiOrphelin(familles, uneFamille, idAdressePaiementRentePrincipal);

                if (isHasPlusieursAdresses(uneFamille) && hasRentePrincipal) {
                    /**
                     * V�rifier si il a des rentes compl�mentaires
                     */
                    for (RERentePourAttestationsFiscales uneRenteDuTiers : uneFamille.getRentesDeLaFamille()) {
                        CodePrestation codePrestation = CodePrestation
                                .getCodePrestation(Integer.parseInt(uneRenteDuTiers.getCodePrestation()));
                        if (codePrestation.isRenteComplementaireForConjoint()) {
                            uneFamille.setHasRenteConjointAdressePaiementSepare(true);
                        }
                        if (codePrestation.isRenteComplementaires()) {
                            uneFamille.setHasPlusieursAdressePaiement(true);
                        }
                    }
                }
                familles.add(uneFamille);
            } else {
                familles.add(uneFamille);
            }
        }

        return familles;
    }

    /**
     * Test si plusieur adresses de paiement apr�s le splitting
     *
     * @param uneFamille
     * @return vrai si plusieurs adresses de paiement pour la famille
     */
    private boolean isHasPlusieursAdresses(REFamillePourAttestationsFiscales uneFamille) {
        List<String> listIdAdressePaiement = new ArrayList<>();
        for (RERentePourAttestationsFiscales rente : uneFamille.getRentesDeLaFamille()) {
            String idAdressePaiement = rente.getIdTiersAdressePaiement();
            if (!listIdAdressePaiement.contains(idAdressePaiement)
                    && !JadeStringUtil.isBlankOrZero(idAdressePaiement)) {
                listIdAdressePaiement.add(idAdressePaiement);
            }
        }
        return listIdAdressePaiement.size() > 1;
    }

    /**
     * Creer une nouvelle famille (= une attestation suppl�mentaire) si il y un cas orphelin avec une adresse diff�rente
     *
     * @param familles
     * @param uneFamille
     * @param idAdressePaiementRentePrincipal
     */
    private void splitSiOrphelin(List<REFamillePourAttestationsFiscales> familles, REFamillePourAttestationsFiscales uneFamille, String idAdressePaiementRentePrincipal) {
        Map<String, REFamillePourAttestationsFiscales> mapFamilleOrphelins = new HashMap<>();
        for (RETiersPourAttestationsFiscales unTiers : uneFamille.getTiersBeneficiaires()) {
            List<RERentePourAttestationsFiscales> listTmp = new ArrayList<>(unTiers.getRentes());
            for (RERentePourAttestationsFiscales rente : listTmp) {
                CodePrestation codePrestation = CodePrestation
                        .getCodePrestation(Integer.parseInt(rente.getCodePrestation()));
                if (isOrphelin(idAdressePaiementRentePrincipal, rente, codePrestation)) {
                    createNewAttestation(familles, mapFamilleOrphelins, rente, unTiers);
                }
            }
        }
    }

    /**
     * Test s'il s'agit d'un enfant avec une rente de survivant = orphelin
     *
     * @param idAdressePaiementRentePrincipal
     * @param rente
     * @param codePrestation
     * @return vrai si orphelin
     */
    private boolean isOrphelin(String idAdressePaiementRentePrincipal, RERentePourAttestationsFiscales rente, CodePrestation codePrestation) {
        return codePrestation.isSurvivant()
                && (idAdressePaiementRentePrincipal == null
                || !idAdressePaiementRentePrincipal.equals(rente.getIdTiersAdressePaiement()))
                && !JadeStringUtil.isBlankOrZero(rente.getIdTiersAdressePaiement());
    }

    /**
     * Cr�e une nouvelle attestation avec la reste de survivant. La rente est supprim�e du Tiers li� � l'ancienne attestation
     *
     * @param familles
     * @param mapFamilleOrphelins
     * @param rente
     * @param unTiers
     */
    private void createNewAttestation(List<REFamillePourAttestationsFiscales> familles, Map<String, REFamillePourAttestationsFiscales> mapFamilleOrphelins, RERentePourAttestationsFiscales rente, RETiersPourAttestationsFiscales unTiers) {
        RETiersPourAttestationsFiscales newTiers = duplicateTierWithRente(unTiers, rente);
        if (mapFamilleOrphelins.get(rente.getIdTiersAdressePaiement()) == null) {
            // cr�e une nouvelle famille pour impression sur une autre attestation
            REFamillePourAttestationsFiscales nouvelleFamille = new REFamillePourAttestationsFiscales();
            nouvelleFamille.setTiersRequerant(newTiers);
            nouvelleFamille.getMapTiersBeneficiaire().put(newTiers.getIdTiers(), newTiers);
            familles.add(nouvelleFamille);
            mapFamilleOrphelins.put(rente.getIdTiersAdressePaiement(), nouvelleFamille);
        } else {
            // Si plusieurs enfants orphelins avec m�me adresse de paiement, ajout dans la m�me attestation
            if (mapFamilleOrphelins.get(rente.getIdTiersAdressePaiement()).getMapTiersBeneficiaire().get(newTiers.getIdTiers()) == null) {
                mapFamilleOrphelins.get(rente.getIdTiersAdressePaiement()).getMapTiersBeneficiaire().put(newTiers.getIdTiers(), newTiers);
            } else {
                // tiers d�j� pr�sent ajout de la rente
                mapFamilleOrphelins.get(rente.getIdTiersAdressePaiement()).getMapTiersBeneficiaire().get(newTiers.getIdTiers()).getMapRentes().put(rente.getIdRenteAccordee(), rente);
            }
        }
    }

    /**
     * Duplique le tier
     *
     * @param tier
     * @param rente
     * @return Le nouveau tiers avec la rente de survivant
     */
    private RETiersPourAttestationsFiscales duplicateTierWithRente(RETiersPourAttestationsFiscales tier, RERentePourAttestationsFiscales rente) {
        // retire la rente orphelin du tier li� � l'ancienne attestation
        tier.getMapRentes().remove(rente.getIdRenteAccordee());

        // cr�� un nouveau tier identique avec cette rente orphelin pour une nouvelle attestation
        RETiersPourAttestationsFiscales newTier = new RETiersPourAttestationsFiscales();
        newTier.setAdresseCourrierFormatee(tier.getAdresseCourrierFormatee());
        newTier.setCodeIsoLangue(tier.getCodeIsoLangue());
        newTier.setCsLangue(tier.getCsLangue());
        newTier.setCsSexe(tier.getCsSexe());
        newTier.setDateDeces(tier.getDateDeces());
        newTier.setDateNaissance(tier.getDateNaissance());
        newTier.setIdTiers(tier.getIdTiers());
        newTier.setNom(tier.getNom());
        newTier.setNumeroAvs(tier.getNumeroAvs());
        newTier.setPrenom(tier.getPrenom());
        newTier.setTitreTiers(tier.getTitreTiers());
        newTier.setHasPcEnDecembre(tier.hasPcEnDecembre());
        newTier.getMapRentes().put(rente.getIdRenteAccordee(), rente);
        return newTier;
    }

    /**
     * Trie les adresses de paiement. Ne retourne qu'une seul adresse (la plus r�cente) pour le requ�rant
     *
     * @param listRentes
     * @return
     */
    private List<RERentePourAttestationsFiscales> sortRentesForIdAdressePaiement(Collection<RERentePourAttestationsFiscales> listRentes) {
        List<RERentePourAttestationsFiscales> rentes = new ArrayList<>(listRentes);
        Collections.sort(rentes, new Comparator<RERentePourAttestationsFiscales>() {
            public int compare(RERentePourAttestationsFiscales rente1, RERentePourAttestationsFiscales rente2) {
                if (!JadeStringUtil.isBlankOrZero(rente1.getIdTiersAdressePaiement())
                        && JadeStringUtil.isBlankOrZero(rente2.getIdTiersAdressePaiement())) {
                    return -1;
                } else if (JadeStringUtil.isBlankOrZero(rente1.getIdTiersAdressePaiement())
                        && !JadeStringUtil.isBlankOrZero(rente2.getIdTiersAdressePaiement())) {
                    return 1;
                } else {
                    Date date1 = JadeDateUtil.getGlobazDate("01." + rente1.getDateDebutDroit());
                    Date date2 = JadeDateUtil.getGlobazDate("01." + rente2.getDateDebutDroit());
                    if (date2.before(date1)) {
                        return -1;
                    } else {
                        return 1;
                    }

                }
            }
        });
        boolean hasUniqueAdress = false;
        for (RERentePourAttestationsFiscales rente : rentes) {
            CodePrestation code = CodePrestation.getCodePrestation(Integer.parseInt(rente.getCodePrestation()));
            if (code.isSurvivant() && code.isRentePrincipale()) {
                hasUniqueAdress = true;
            }
        }
        if (hasUniqueAdress) {
            // ne prendre que la plus r�cente
            return rentes.subList(0, 1);
        }
        return rentes;
    }


    private int getAnneeAsInteger() {
        return Integer.valueOf(annee);
    }

    /**
     * Regroupe les donn�es brutes par tiers requ�rant (tiers auquel l'attestation fiscale sera envoy�e) et ajoute tous
     * les tiers b�n�ficiaires (et leurs rentes) li�s � ce tiers requ�rant (par ID tiers de la base de calcul)
     *
     * @param donnees les donn�es brutes charg�es par {@link REDonneesPourAttestationsFiscalesManager}
     * @return les donn�es regroup�es par tiers requ�rant
     * @throws Exception dans le cas o� un probl�me survient lors de la r�cup�ration des adresses de courrier/paiement et du
     *                   titre du tiers requ�rant
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
                    String message = "Erreur pour le tiers b�n�ficiaire [" + uneDonnee.getNumeroAvsTiersBeneficiaire()
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

        // tri par ordre alphab�tique (nom, pr�nom) des tiers requ�rant
        Collections.sort(familles);
        return familles;
    }
}
