package globaz.corvus.topaz;

import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.utils.REGedUtils;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.api.ITIPersonne;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.corvus.domaine.constantes.DegreImpotenceAPI;
import ch.globaz.corvus.process.attestationsfiscales.REFamillePourAttestationsFiscales;
import ch.globaz.corvus.process.attestationsfiscales.RERentePourAttestationsFiscales;
import ch.globaz.corvus.process.attestationsfiscales.RETiersPourAttestationsFiscales;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REAttestationsFiscalesOO extends REAbstractJobOO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_ATTESTATION_FISCALE";

    private String annee;
    private CatalogueText catalogueTextesAttestationsFiscales;
    private CatalogueText catalogueTextesDecision;
    private List<JadeCodeSysteme> codesSystemesGenresRentes;
    private String dateDernierPaiement;
    private String dateImpression;
    private SortedSet<REFamillePourAttestationsFiscales> familles;
    private String codeIsoLangue;
    private boolean isSendToGed;

    public REAttestationsFiscalesOO() {
        super(true);

        annee = "";
        dateDernierPaiement = "";
        dateImpression = "";
        familles = new TreeSet<REFamillePourAttestationsFiscales>();
        codesSystemesGenresRentes = new ArrayList<JadeCodeSysteme>();

    }

    @Override
    protected List<CatalogueText> definirCataloguesDeTextes() {
        List<CatalogueText> catalogues = new ArrayList<CatalogueText>();

        catalogueTextesAttestationsFiscales = new CatalogueText();
        catalogueTextesAttestationsFiscales.setCodeIsoLangue(getCodeIsoLangue());
        catalogueTextesAttestationsFiscales.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueTextesAttestationsFiscales.setCsTypeDocument(IRECatalogueTexte.CS_ATTESTATION_FISCALE);
        catalogueTextesAttestationsFiscales.setNomCatalogue("openOffice");
        catalogues.add(catalogueTextesAttestationsFiscales);

        catalogueTextesDecision = new CatalogueText();
        catalogueTextesDecision.setCodeIsoLangue(getCodeIsoLangue());
        catalogueTextesDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueTextesDecision.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
        catalogueTextesDecision.setNomCatalogue("openOffice");
        catalogues.add(catalogueTextesDecision);

        return catalogues;
    }

    private DocumentData genererAttestationPourUneFamille(REFamillePourAttestationsFiscales famille) throws Exception {
        RETiersPourAttestationsFiscales tiersCorrespondance = famille.getTiersPourCorrespondance();

        // remplissage des champs statiques (paragraphes, titres, en-tête de tableau, etc...)
        DocumentData data = getDonneesPreRemplies(catalogueTextesAttestationsFiscales);

        // remplissage de l'en-tête et signature en fonction du tiers de correspondance
        genererEnTeteSignature(tiersCorrespondance, data);

        data.addData("nss_requerant", tiersCorrespondance.getNumeroAvs());
        data.addData("nomPrenom_requerant", tiersCorrespondance.getNom() + " " + tiersCorrespondance.getPrenom());

        if (tiersCorrespondance.getCodeIsoLangue().equalsIgnoreCase("de")) {
            data.addData("titre_tiers", tiersCorrespondance.getTitreTiers());
        } else {
            data.addData("titre_tiers", tiersCorrespondance.getTitreTiers() + ",");
        }

        boolean hasAPI = false;
        DegreImpotenceAPI degreAPI = null;
        double montantTotal = 0.0;

        Collection tableauBeneficiaires = new Collection("TableauRenteAccordee");

        List<RETiersPourAttestationsFiscales> tiersBeneficiaires = new ArrayList<RETiersPourAttestationsFiscales>(
                famille.getTiersBeneficiaires());
        // Tri pour avoir la rente principale en haut de la liste
        // puis les complémentaire triées par tiers dans l'ordre alphabétique
        Collections.sort(tiersBeneficiaires, new Comparator<RETiersPourAttestationsFiscales>() {

            @Override
            public int compare(RETiersPourAttestationsFiscales tiers1, RETiersPourAttestationsFiscales tiers2) {

                for (RERentePourAttestationsFiscales renteTiers1 : tiers1.getRentes()) {
                    CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(renteTiers1
                            .getCodePrestation()));
                    if (codePrestation.isRentePrincipale()) {
                        return -1;
                    }
                }

                for (RERentePourAttestationsFiscales renteTiers2 : tiers2.getRentes()) {
                    CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(renteTiers2
                            .getCodePrestation()));
                    if (codePrestation.isRentePrincipale()) {
                        return 1;
                    }
                }

                return tiers1.compareTo(tiers2);
            }
        });

        boolean hasRetroactifSurPlusieursAnnees = famille.getHasRetroactifSurPlusieursAnnees();

        /*
         * Dans le cas ou la famille possède du rétro sur plusieurs années, un traitement particulier est requis pour
         * attester les rentes.
         * On recherche la date de décision la plus élevée dans l'année fiscales et on atteste toutes les rentes le mois
         * suivants cette date de décision
         */
        Date dateDeDecisionFinale = null;
        if (hasRetroactifSurPlusieursAnnees) {
            SimpleDateFormat reader = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat yearsWriter = new SimpleDateFormat("yyyy");
            SimpleDateFormat yearsMonthWriter = new SimpleDateFormat("yyyyMM");
            int dateDeDecisionInteger = 0;
            for (RETiersPourAttestationsFiscales tiers : tiersBeneficiaires) {
                for (RERentePourAttestationsFiscales rente : tiers.getRentes()) {
                    Date dateDeDecision = null;
                    // date vide, on s'en bat les ....
                    if (JadeStringUtil.isBlankOrZero(rente.getDateDecision())) {
                        continue;
                    }

                    int year = 0;
                    int yearMonth = 0;

                    try {
                        dateDeDecision = reader.parse(rente.getDateDecision());
                        year = Integer.valueOf(yearsWriter.format(dateDeDecision));
                        yearMonth = Integer.valueOf(yearsMonthWriter.format(dateDeDecision));
                    } catch (Exception e) {
                        // Bon ben pas de bol, problème de données du coup on ignore cette rente
                        // TODO logger le binz
                    }

                    // Que les décisions dans l'année fiscales
                    if (year == 0 || !Integer.valueOf(getAnnee()).equals(year)) {
                        continue;
                    }

                    if (yearMonth > dateDeDecisionInteger) {
                        dateDeDecisionInteger = yearMonth;
                        dateDeDecisionFinale = dateDeDecision;
                    }

                }
            }

            if (dateDeDecisionFinale != null) {
                int value = Integer.valueOf(yearsMonthWriter.format(dateDeDecisionFinale));
                value++; // ajout d'un mois
                dateDeDecisionFinale = yearsMonthWriter.parse(String.valueOf(value));
            }
        }
        /*
         * Traitement normal
         */

        // création de la liste des rentes accordées pour cette famille
        for (RETiersPourAttestationsFiscales unTiersBeneficiaire : tiersBeneficiaires) {

            StringBuilder infoTiers = new StringBuilder();
            infoTiers.append(unTiersBeneficiaire.getNumeroAvs()).append(" ").append(unTiersBeneficiaire.getNom())
                    .append(" ").append(unTiersBeneficiaire.getPrenom());

            for (RERentePourAttestationsFiscales uneRenteDuBeneficiaire : unTiersBeneficiaire.getRentes()) {

                String dateMoisDebut = null;

                if (hasRetroactifSurPlusieursAnnees && dateDeDecisionFinale != null) {

                    SimpleDateFormat writer = new SimpleDateFormat("MM.yyyy");
                    dateMoisDebut = "01." + writer.format(dateDeDecisionFinale);
                } else {
                    dateMoisDebut = "01." + getMoisDebut(uneRenteDuBeneficiaire);
                }

                String dateMoisFin = getDerniereDateMois(getMoisFin(uneRenteDuBeneficiaire));
                int nbMois = JadeDateUtil.getNbMonthsBetween(dateMoisDebut, dateMoisFin);

                /*
                 * Si décision de vieillesse pour un couple et que 2 décisions on étés prise en avance pour une des 2
                 * personne
                 * La date de fin de droit sera plus petite que la date de fin ce qui amène des montant négatif sur
                 * l'attestation fiscales
                 */
                if (nbMois > 0) {

                    CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer
                            .parseInt(uneRenteDuBeneficiaire.getCodePrestation()));

                    // si API, on ne l'affiche pas dans la liste, mais une remarque sera ajouté plus tard
                    // avec le degré de cette API
                    if (codePrestation.isAPI()) {
                        hasAPI = true;
                        degreAPI = codePrestation.getDegreImpotenceAPI();
                        continue;
                    }

                    DataList ligneInfoBeneficiaire = new DataList("ligneInfoAssure");
                    ligneInfoBeneficiaire.addData("infoTiers", infoTiers.toString());
                    tableauBeneficiaires.add(ligneInfoBeneficiaire);

                    DataList ligneInfoRente = new DataList("ligneInfoRente");

                    String traductionCodePrestation = null;
                    StringBuilder csCodePrestation = new StringBuilder();
                    csCodePrestation.append("52821").append(codePrestation);
                    if (JadeStringUtil.isBlankOrZero(uneRenteDuBeneficiaire.getFractionRente())) {
                        csCodePrestation.append("0");
                    } else {
                        csCodePrestation.append(uneRenteDuBeneficiaire.getFractionRente());
                    }

                    // cas particulier des rentes de veuf (pour la description de la rente)
                    if (ITIPersonne.CS_HOMME.equals(unTiersBeneficiaire.getCsSexe())
                            && (codePrestation.getCodePrestation() == 13)) {
                        // texte "Rente de veuf" pris depuis le catalogue de texte de la décision de rente
                        traductionCodePrestation = getTexte(catalogueTextesDecision, 2, 11);
                    } else if (ITIPersonne.CS_HOMME.equals(unTiersBeneficiaire.getCsSexe())
                            && (codePrestation.getCodePrestation() == 23)) {
                        // texte "Rente de veuf extraordinaire" pris depuis le catalogue de texte de la décision de
                        // rente
                        traductionCodePrestation = getTexte(catalogueTextesDecision, 2, 12);
                    } else {
                        traductionCodePrestation = getTraductionGenreRente(csCodePrestation.toString(),
                                tiersCorrespondance.getCodeIsoLangue());
                    }

                    if (traductionCodePrestation == null) {
                        throw new Exception("Unable to find a translation for the code prestation : "
                                + csCodePrestation.toString());
                    } else {
                        ligneInfoRente.addData("genre_rente", traductionCodePrestation);
                    }

                    StringBuilder periodeRente = new StringBuilder();
                    periodeRente.append(dateMoisDebut).append(" - ").append(dateMoisFin);

                    ligneInfoRente.addData("periode_rente", periodeRente.toString());

                    double montantAnnuel = Double.parseDouble(JANumberFormatter.deQuote(uneRenteDuBeneficiaire
                            .getMontantPrestation())) * nbMois;
                    montantTotal += montantAnnuel;

                    ligneInfoRente.addData("montant_rente", new FWCurrency(montantAnnuel).toStringFormat());

                    tableauBeneficiaires.add(ligneInfoRente);
                }
            }
        }
        data.add(tableauBeneficiaires);

        data.addData("montant_total", new FWCurrency(montantTotal).toStringFormat());

        // table vide pour enlever la section "Autres rentes" (utilisée uniquement pour les attestations uniques)
        Collection autreTable = new Collection("TableauAutre");
        data.add(autreTable);

        if (hasAPI) {
            String texteAPI = null;
            switch (degreAPI) {
                case FAIBLE:
                    texteAPI = getTexte(catalogueTextesAttestationsFiscales, 4, 2);
                    break;
                case MOYEN:
                    texteAPI = getTexte(catalogueTextesAttestationsFiscales, 4, 3);
                    break;
                case GRAVE:
                    texteAPI = getTexte(catalogueTextesAttestationsFiscales, 4, 4);
                    break;
                default:
                    // rien
                    break;
            }

            if (!JadeStringUtil.isBlank(texteAPI)) {
                data.addData("TITRE_API", getTexte(catalogueTextesAttestationsFiscales, 4, 1));
                data.addData("TEXTE_API", texteAPI);
            }
        }

        if (famille.hasPlusieursAdressePaiement()) {
            data.addData("PLUSIEURS_ATT_FAMILLE", getTexte(catalogueTextesAttestationsFiscales, 4, 6));
        }
        // Si PC en décembre
        if (famille.getHasRentePC()) {
            data.addData("HAS_PC_DECEMBRE", getTexte(catalogueTextesAttestationsFiscales, 4, 7));
        }
        // Si rétroactif
        if (famille.getHasRetroactif()) {
            String texte = getTexte(catalogueTextesAttestationsFiscales, 4, 8);
            if (!JadeStringUtil.isBlank(texte)) {
                texte = texte.replace("{annee}", getAnnee());
            }
            data.addData("HAS_RETROACTIF", texte);
        }

        data.addData("SALUTATION_ATTESTATION", PRStringUtils.replaceString(
                getTexte(catalogueTextesAttestationsFiscales, 5, 1), "{TitreSalutation}",
                tiersCorrespondance.getTitreTiers()));

        return data;
    }

    @Override
    protected void genererDocument() throws Exception {
        if (JadeStringUtil.isBlank(annee)) {
            throw new Exception("Year mandatory");
        }
        if (JadeStringUtil.isBlank(dateDernierPaiement)) {
            throw new Exception("Last paiement date mandatory");
        }

        JadePrintDocumentContainer documentGlobal = new JadePrintDocumentContainer();

        JadePublishDocumentInfo pubInfosDestination = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfosDestination.setOwnerEmail(getAdresseEmail());
        pubInfosDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getAdresseEmail());
        pubInfosDestination.setDocumentTitle(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
        pubInfosDestination.setDocumentSubject(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
        pubInfosDestination.setArchiveDocument(false);
        pubInfosDestination.setPublishDocument(true);
        pubInfosDestination.setDocumentType(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES_ANNUELLE);
        pubInfosDestination.setDocumentTypeNumber(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES_ANNUELLE);

        documentGlobal.setMergedDocDestination(pubInfosDestination);

        try {
            JadeCodeSystemeService codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();
            codesSystemesGenresRentes = codeSystemeService.getFamilleCodeSysteme("REGENRPRST");
        } catch (JadeApplicationServiceNotAvailableException ex) {
            ex.printStackTrace();
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }

        for (REFamillePourAttestationsFiscales uneFamille : familles) {

            JadePublishDocumentInfo singleDocInfo = JadePublishDocumentInfoProvider.newInstance(this);
            singleDocInfo.setOwnerEmail(getAdresseEmail());
            singleDocInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getAdresseEmail());
            singleDocInfo.setDocumentTitle(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
            singleDocInfo.setDocumentSubject(getSession().getLabel("ATTESTATION_FISCALE_RENTE"));
            singleDocInfo.setArchiveDocument(isSendToGed);
            singleDocInfo.setPublishDocument(false);
            singleDocInfo.setDocumentType(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES_ANNUELLE);
            singleDocInfo.setDocumentTypeNumber(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES_ANNUELLE);
            singleDocInfo.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE, REGedUtils
                    .getCleGedPourTypeRente(getSession(), REGedUtils.getTypeRentePourListeCodesPrestation(getSession(),
                            getCodePrestationFamille(uneFamille), false, true)));

            // bz-7950
            RETiersPourAttestationsFiscales tiersCorrespondance = null;
            try {
                tiersCorrespondance = uneFamille.getTiersPourCorrespondance();
                TIDocumentInfoHelper.fill(singleDocInfo, tiersCorrespondance.getIdTiers(), getSession(), null, null,
                        null);
            } catch (Exception e) {
                if (tiersCorrespondance != null) {
                    System.out.println("Erreur lors de l'indexation GED pour idTiers "
                            + tiersCorrespondance.getIdTiers() + e.toString());
                } else {
                    System.out.println("Erreur lors de l'indexation GED - " + e.toString());
                }
            }
            documentGlobal.addDocument(genererAttestationPourUneFamille(uneFamille), singleDocInfo);
        }

        this.createDocuments(documentGlobal);
    }

    private void genererEnTeteSignature(RETiersPourAttestationsFiscales tiersCorrespondance, DocumentData data) {
        try {
            // CatalogueText catalogueLangueTiers = catalogueTextesAttestationsFiscales.get(tiersCorrespondance
            // .getCodeIsoLangue().toLowerCase());

            data.addData("SIGNATURE", getTexte(catalogueTextesAttestationsFiscales, 7, 1));

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
            crBean.setAdresse(tiersCorrespondance.getAdresseCourrierFormatee());

            ICaisseReportHelperOO caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                    getSession().getApplication(), tiersCorrespondance.getCodeIsoLangue());
            caisseHelper.setTemplateName(REAttestationsFiscalesOO.FICHIER_MODELE_ENTETE_CORVUS);

            if (Boolean.parseBoolean(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
                StringBuilder nomCollaboration = new StringBuilder();
                nomCollaboration.append(getTexte(catalogueTextesAttestationsFiscales, 6, 1)).append(" ")
                        .append(getSession().getUserFullName());

                crBean.setNomCollaborateur(nomCollaboration.toString());
                crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            }

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du document
            if (Boolean
                    .parseBoolean(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            if (JadeDateUtil.isGlobazDate(dateImpression)) {
                StringBuilder dateImpression = new StringBuilder(" ");
                if ("fr".equalsIgnoreCase(tiersCorrespondance.getCodeIsoLangue())) {
                    dateImpression.append("le ");
                } else if ("it".equalsIgnoreCase(tiersCorrespondance.getCodeIsoLangue())) {
                    dateImpression.append("il ");
                }
                dateImpression.append(JACalendar.format(this.dateImpression, tiersCorrespondance.getCodeIsoLangue()));

                crBean.setDate(dateImpression.toString());
            } else if (JadeDateUtil.isGlobazDateMonthYear(dateImpression)) {
                JADate date = new JADate(dateImpression);
                StringBuilder dateImpression = new StringBuilder();
                dateImpression.append(" ").append(
                        JACalendar.getMonthName(date.getMonth(), tiersCorrespondance.getCodeIsoLangue()));
                dateImpression.append(" ").append(Integer.toString(date.getYear()));

                crBean.setDate(dateImpression.toString());
            }

            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);
            data = caisseHelper.addSignatureParameters(data, crBean);

            data.addData("idEntete", "CAISSE_SPEC_ATTESTATIONS_FISCALES");

        } catch (Exception ex) {
            getLogSession().error(getName(), ex.toString());
        }
    }

    public String getAnnee() {
        return annee;
    }

    private Set<CodePrestation> getCodePrestationFamille(REFamillePourAttestationsFiscales famille) {
        Set<CodePrestation> codesPrestation = new HashSet<CodePrestation>();

        for (RERentePourAttestationsFiscales uneRente : famille.getRentesDeLaFamille()) {
            if (!JadeStringUtil.isBlankOrZero(uneRente.getCodePrestation())
                    && JadeNumericUtil.isInteger(uneRente.getCodePrestation())) {
                CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRente
                        .getCodePrestation()));
                codesPrestation.add(codePrestation);
            }
        }

        return codesPrestation;
    }

    public String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    private String getDerniereDateMois(String moisAnnee) {
        Calendar calendarMoisFin = Calendar.getInstance();
        calendarMoisFin.set(Calendar.MONTH, Integer.parseInt(moisAnnee.substring(0, 2)) - 1);
        calendarMoisFin.set(Calendar.YEAR, Integer.parseInt(moisAnnee.substring(3)));
        return calendarMoisFin.getActualMaximum(Calendar.DAY_OF_MONTH) + "." + moisAnnee;
    }

    @Override
    public String getDescription() {
        return getName();
    }

    private DocumentData getDonneesPreRemplies(CatalogueText catalogueLangueTiers) {
        DocumentData data = new DocumentData();

        data.addData("idProcess", "REAttestationFiscaleOO");

        data.addData("TITRE_ATTESTATION", getTexte(catalogueLangueTiers, 1, 1));
        data.addData("annee_attestation", getAnnee());
        data.addData("PARAGRAPHE1_ATTESTATION", getTexte(catalogueLangueTiers, 2, 1));
        data.addData("ENTETE_TAB_ASSURE", getTexte(catalogueLangueTiers, 3, 1));
        data.addData("ENTETE_TAB_PERIODE", getTexte(catalogueLangueTiers, 3, 2));
        data.addData("ENTETE_TAB_MONTANT", getTexte(catalogueLangueTiers, 3, 3));
        data.addData("TOTAL", getTexte(catalogueLangueTiers, 3, 4));
        data.addData("CHF", getTexte(catalogueLangueTiers, 3, 5));

        return data;
    }

    public SortedSet<REFamillePourAttestationsFiscales> getFamilles() {
        return familles;
    }

    /**
     * @return the isSendToGed
     */
    public final boolean getIsSendToGed() {
        return isSendToGed;
    }

    private String getMoisDebut(RERentePourAttestationsFiscales rente) {
        String moisDebut;
        if (JadeDateUtil.isDateMonthYearBefore(rente.getDateDebutDroit(), "01." + annee)) {
            moisDebut = "01." + annee;
        } else {
            moisDebut = rente.getDateDebutDroit();
        }
        return moisDebut;
    }

    private String getMoisFin(RERentePourAttestationsFiscales rente) {
        String moisFin;

        if (annee.equals(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateDernierPaiement))) {
            moisFin = JadeDateUtil.convertDateMonthYear(dateDernierPaiement);
        } else {
            moisFin = "12." + annee;
        }

        if (JadeDateUtil.isDateMonthYearBefore(rente.getDateFinDroit(), moisFin)) {
            moisFin = rente.getDateFinDroit();
        }
        return moisFin;
    }

    @Override
    public String getName() {
        return getSession().getLabel("ATTESTATION_FISCALE_RENTE");
    }

    private String getTraductionGenreRente(String csGenreRente, String codeIsoLangue) {
        for (JadeCodeSysteme unCodeSysteme : codesSystemesGenresRentes) {
            if (unCodeSysteme.getIdCodeSysteme().equals(csGenreRente)) {
                return unCodeSysteme.getTraduction(Langues.getLangueDepuisCodeIso(codeIsoLangue));
            }
        }
        return null;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDernierPaiement(String dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setFamilles(SortedSet<REFamillePourAttestationsFiscales> familles) {
        this.familles = familles;
    }

    /**
     * @param isSendToGed
     *            the isSendToGed to set
     */
    public final void setIsSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

}
