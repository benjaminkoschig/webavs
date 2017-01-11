package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.COTaxe;
import globaz.aquila.service.taxes.ICOTaxeProducer;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.external.IntTiers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author Alexandre Cuva, 18-aug-2004
 */
public class CO01RequisitionPoursuite extends CODocumentRequisition {

    public static final String NUMERO_REFERENCE_INFOROM = "0023GCO";

    protected static final String SEPARATION_PARAGRAPHE = "\n\n";
    private static final long serialVersionUID = -8736377540169184861L;
    private static final String TEMPLATE_NAME = "CO_REQUISITION";
    private static final String ZERO_FRANC = "0.00";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private boolean isADBExecutee = false;
    private FWCurrency montantCreanceNette = new FWCurrency(0);
    private LinkedList<Map<String, String>> listDataSourceCreances = new LinkedList<Map<String, String>>();
    private LinkedList<Map<String, String>> listDataSourceTitreCreances = new LinkedList<Map<String, String>>();

    /**
     * Crée une nouvelle instance de la classe CO01RequisitionPoursuite.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO01RequisitionPoursuite() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO01RequisitionPoursuite(BSession parent) throws FWIException {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CO01RequisitionPoursuite.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RDP"));
        setNumeroReferenceInforom(CO01RequisitionPoursuite.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Ajoute les IM facturé à cette étape au dataSource
     * 
     * @param dataSource
     * @return montant total des IM
     */
    private void addInteretsInListDataSource() {
        if (montantTotalIM != null && JadeStringUtil.isBlankOrZero(montantTotalIM) != true) {
            Map<String, String> mapInterets = new HashMap<String, String>();

            mapInterets.put(
                    COParameter.F1,
                    formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 97)),
                            new Object[] { formatDate(getDateExecution()) }));
            mapInterets.put(CAExtraitCompte.SECTIONDATE_FIELD, getDateExecution());
            mapInterets.put(COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2));
            mapInterets.put(COParameter.F2, formatMontant(montantTotalIM));
            mapInterets.put(CARubrique.FIELD_NATURERUBRIQUE, APIRubrique.INTERETS_MORATOIRES);

            listDataSourceCreances.add(mapInterets);
        }
    }

    public Boolean isNouveauRegime(BSession session, String dateExecution) {
        try {
            String dateProduction = session.getApplication().getProperty("dateProductionNouveauCDP");
            if (dateProduction == null) {
                return false;
            }

            // retourne true si la date d'execution de la RP est supérieure ou égale à la date de production du nouveau
            // regime
            return BSessionUtil.compareDateFirstGreaterOrEqual(session, dateExecution, dateProduction);
        } catch (Exception e) {
            JadeLogger.error(e, "La propriété n'existe pas.");
            return false;
        }
    }

    /**
     * 
     * @param session
     * @return
     */
    public Boolean isNouveauRegime(BSession session) {
        try {
            // retourne true si la date d'execution de la RP est supérieure ou égale à la date de production du nouveau
            // regime
            return isNouveauRegime(session, getDateExecution());
        } catch (Exception e) {
            JadeLogger.error(e, "La propriété n'existe pas.");
            return false;
        }
    }

    /**
     * 
     * @throws Exception
     */
    private void setParametersEnCommun() throws Exception {
        // Renseigne les libellés selon la langue
        this.setParametres(COParameter.T3, getCatalogueTextesUtil().texte(getParent(), 2, 1));
        this.setParametres(COParameter.T5, getCatalogueTextesUtil().texte(getParent(), 2, 4));

        this.setParametres(COParameter.HEADER1, getCatalogueTextesUtil().texte(getParent(), 2, 6));

        // Renseigne les paramètres du document
        if (isADBExecutee) {
            this.setParametres(
                    COParameter.T2,
                    formatMessage(
                            new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 1, 2)),
                            new Object[] { historiqueService
                                    .getHistoriqueForLibEtape(getSession(), curContentieux,
                                            ICOEtape.CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI)
                                    .loadEtapeInfo(COEtapeInfoConfig.CS_NUMERO_ADB).getValeur() }));
        } else {
            this.setParametres(
                    COParameter.T2,
                    formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 1, 1)),
                            new Object[] { curContentieux.getNumPoursuite() }));
        }

        this.setParametres(COParameter.P_DEVISE, getCatalogueTextesUtil().texte(getParent(), 3, 2));

        this.setParametres(COParameter.ADRESSE, getInfoAdresse(curContentieux.getCompteAnnexe().getTiers()));
        this.setParametres(
                COParameter.T6,
                formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 2, 7)), new Object[] {
                        curContentieux.getCompteAnnexe().getRole().getDescription(getLangue()),
                        curContentieux.getCompteAnnexe().getIdExterneRole() }));
        this.setParametres(
                COParameter.T7,
                formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 2, 5)),
                        new Object[] { this._getProperty(CODocumentManager.JASP_PROP_BODY_NOM_ADRESSE_CAISSE) }));
        this.setParametres(
                COParameter.T8,
                formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 2, 8)),
                        new Object[] { getNumeroCCP() }));
    }

    /**
     * 
     * @throws Exception
     */
    private void repartitionMontantCreanceNegatif() throws Exception {
        // Si le montant de base est negatif, nous allons essayer de déduire les creances et d'arriver a un
        // montant de base a zero
        FWCurrency montantAJauger = new FWCurrency(montantCreanceNette.toString());
        if (montantCreanceNette.isNegative()) {
            montantAJauger.abs();

            for (Map<String, String> value : listDataSourceCreances) {
                // Si le montant de base est à zéro
                if (montantCreanceNette.isZero() == true) {
                    break;
                }

                FWCurrency montantLigne = new FWCurrency(value.get(COParameter.F2));

                if (montantAJauger.compareTo(new FWCurrency(value.get(COParameter.F2))) != 1) {
                    montantCreanceNette.add(montantAJauger);
                    montantLigne.sub(montantAJauger);
                    montantAJauger.sub(montantAJauger);
                    value.put(COParameter.F2, montantLigne.toString());
                } else {
                    montantAJauger.sub(montantLigne);
                    montantCreanceNette.add(montantLigne);
                    montantLigne = new FWCurrency(0);
                    value.put(COParameter.F2, montantLigne.toString());
                }
            }
        }
    }

    public Integer getIndexDSCreancesSearchNatureRubrique(String valueToSearch) {
        Integer index = null;

        for (int i = 0; i < listDataSourceCreances.size(); i++) {
            Map<String, String> object = listDataSourceCreances.get(i);

            String valueOfObject = object.get(CARubrique.FIELD_NATURERUBRIQUE);

            if (valueOfObject.equalsIgnoreCase(valueToSearch)) {
                index = i;
            }
        }

        return index;
    }

    public Integer getIndexDSTitreCreancesSearchNatureRubrique(String valueToSearch) {
        Integer index = null;

        for (int i = 0; i < listDataSourceTitreCreances.size(); i++) {
            Map<String, String> object = listDataSourceTitreCreances.get(i);

            String valueOfObject = object.get(CARubrique.FIELD_NATURERUBRIQUE);

            if (valueOfObject.equalsIgnoreCase(valueToSearch)) {
                index = i;
            }
        }

        return index;
    }

    /**
     * 
     * @param dataSource
     * @return
     * @throws Exception
     */
    private void verificationLignesCreances() throws Exception {
        int numeroteur = 2;

        Map<String, String> interets = null;
        // Supression des interets dans la liste pour la remettre a la fin
        if (getIndexDSCreancesSearchNatureRubrique(APIRubrique.INTERETS_MORATOIRES) != null) {
            Integer indexInterets = getIndexDSCreancesSearchNatureRubrique(APIRubrique.INTERETS_MORATOIRES);
            interets = listDataSourceCreances.get(indexInterets);
            FWCurrency montantInterets = new FWCurrency(interets.get(COParameter.F2));

            if (montantInterets.isZero() == true) {
                interets = null;
            }
            removeFromListDSCreances(indexInterets);
        }

        // --------------------------------------------------------
        // Commencement de la construction de la liste de créances
        // --------------------------------------------------------

        Map<String, String> taxeSommation = null;
        // La taxe de sommation doit toujours être en 2eme lignes si y'en a une.
        if (getIndexDSCreancesSearchNatureRubrique(APIRubrique.TAXE_SOMMATION) != null) {
            Integer indexSommation = getIndexDSCreancesSearchNatureRubrique(APIRubrique.TAXE_SOMMATION);
            taxeSommation = listDataSourceCreances.get(indexSommation);

            FWCurrency montantTaxeSom = new FWCurrency(taxeSommation.get(COParameter.F2));
            // Si la taxe de sommation a été déduite avec le montant de base et reste a Zero, on ne
            // l'affiche pas
            if (montantTaxeSom.isZero() == false) {
                taxeSommation.put(COParameter.F1, numeroteur + ") " + taxeSommation.get(COParameter.F1));
                numeroteur++;
            } else {
                taxeSommation = null;
            }
            removeFromListDSCreances(indexSommation);
        }

        // Toutes les créances (SANS Sommation et intérêts tardifs)
        for (int i = 0; i < listDataSourceCreances.size(); i++) {
            Map<String, String> value = listDataSourceCreances.get(i);
            FWCurrency montantLigne = new FWCurrency(value.get(COParameter.F2));
            if (montantLigne.isZero() == false) {
                value.put(COParameter.F1, numeroteur + ") " + value.get(COParameter.F1));
                numeroteur++;
            } else {
                removeFromListDSCreances(i);
                i--;
            }
        }

        // La taxe de sommation doit toujours être en 2eme lignes si y'en a une.
        if (taxeSommation != null) {
            listDataSourceCreances.addFirst(taxeSommation);
        }

        // Remettre les interets a la fin des creances si y'a des interets
        if (interets != null) {
            FWCurrency montantInterets = new FWCurrency(interets.get(COParameter.F2));
            // Si la taxe de sommation a été déduite avec le montant de base et reste a Zero, on ne
            // l'affiche pas
            if (montantInterets.isZero() == false) {
                interets.put(COParameter.F1, numeroteur + ") " + interets.get(COParameter.F1));
                listDataSourceCreances.addLast(interets);
                numeroteur++;
            }
        }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            // LES POINTS EN COMMUN ----------------------------
            initOfficePoursuite(getParent(), 2, 2, 2, 3);

            COHistorique historique = initHistorique();

            this._handleHeaders(null, curContentieux, false, false, true);

            String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                    curContentieux.getIdSection());

            setParametersEnCommun();
            // -------------------------------------------------
            if (isNouveauRegime(getSession()) == true
                    && CORequisitionPoursuiteUtil.isOfficeDontWantToUseNewRegime(getSession(), cantonOfficePoursuite) == false) {
                nouveauRegime(infoSection, historique);
            } else {
                ancienRegime(infoSection, historique);
            }
        } catch (Exception e) {
            this.log(e.getMessage());
        }
    }

    private void verificationIMDejaPresent(COContentieux contentieux) throws COTransitionException {
        FWCurrency cumulMontantInteret = new FWCurrency(0);
        for (Map<String, String> creance : listDataSourceCreances) {
            if (creance.get(CARubrique.FIELD_NATURERUBRIQUE).equals(APIRubrique.INTERETS_MORATOIRES)) {
                cumulMontantInteret.add(new FWCurrency(creance.get(COParameter.F2)));

            }
        }
        if (!cumulMontantInteret.isZero()) {
            getDocumentInfo().setRejectDocument(true);
            getDocumentInfo().setPublishDocument(false);
            getSession().addError(getSession().getLabel("RDP_IMEXISTANT_EXTRAITCOMPTE"));
            getDocumentInfo().setPreventFromPublish(false);
            getDocumentInfo().setDocumentNotes(
                    getSession().getLabel("RDP_IMEXISTANT_EXTRAITCOMPTE") + " - "
                            + contentieux.getCompteAnnexe().getIdExterneRole() + "/"
                            + contentieux.getSection().getIdExterne());
        }
    }

    /**
     * 
     * @param infoSection
     * @param historique
     * @throws Exception
     */
    private void nouveauRegime(String[] infoSection, COHistorique historique) throws Exception {
        this.setParametres(COParameter.L_SECTION_DESCR, "1) " + curContentieux.getSection().getDescription(getLangue())
                + " (" + curContentieux.getSection().getIdExterne() + ")");

        // Récupération des lignes de la partie CREANCE
        listDataSourceCreances = createSituationCompteDSNouveauRegimePourRdP(infoSection[2], COParameter.F1,
                COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2),
                ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE, infoSection[1]);

        // Déduction des paiements/compensations sur le montant de base de la créance
        montantCreanceNette = new FWCurrency(infoSection[0]);
        montantCreanceNette.add(getMontantTotalPaiement());

        verificationIMDejaPresent(curContentieux);
        // Repartir le montant de base negatif sur les autres creances positives
        repartitionMontantCreanceNegatif();
        // Ajout des interets tardifs (moratoire) dans la liste
        addInteretsInListDataSource();
        // Affichage ou non des creances (si montant a zero, ordonner la numerotation par exemple)
        verificationLignesCreances();

        if (!listDataSourceCreances.isEmpty()) {
            // Nous mettons les lignes des creances dans la datasource
            List<Map<String, String>> liste = new ArrayList<Map<String, String>>();
            if (!listDataSourceCreances.isEmpty()) {
                // Le setDataSource n'apprécie pas les LinkedList, donc on lui donne une Liste
                liste.addAll(listDataSourceCreances.subList(0, listDataSourceCreances.size()));
                this.setDataSource(liste);
            }
            listDataSourceTitreCreances = (LinkedList<Map<String, String>>) listDataSourceCreances.clone();
        }

        this.setParametres(COParameter.P_BASE, formatMontant(montantCreanceNette.toString()));

        String montantCreance = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(), curContentieux,
                getDateExecutionPlus1Day());

        if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
            this.setParametres(
                    COParameter.L_INTERET,
                    formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 95)), new Object[] {
                            formatDate(getDateExecutionPlus1Day()), getCatalogueTextesUtil().texte(getParent(), 3, 2),
                            formatMontant(montantCreance) }));
        }

        // Affichage des lignes dans Titre de la creances et autres
        initFooterNouveauRegime(historique);
    }

    /**
     * 
     * @param infoSection
     * @param historique
     * @throws Exception
     */
    private void ancienRegime(String[] infoSection, COHistorique historique) throws Exception {
        this.setParametres(COParameter.L_SECTION_DESCR, curContentieux.getSection().getDescription(getLangue()) + " ("
                + curContentieux.getSection().getIdExterne() + ")");
        this.setParametres(COParameter.P_BASE, formatMontant(infoSection[0]));

        String montantCreance = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(), curContentieux);
        if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
            this.setParametres(
                    COParameter.L_INTERET,
                    formatMessage(
                            new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 95)),
                            new Object[] {
                                    formatDate(CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(getSession(),
                                            getTransaction(), curContentieux)),
                                    getCatalogueTextesUtil().texte(getParent(), 3, 2), formatMontant(montantCreance) }));
        }

        List<Map<String, String>> dataSource = new LinkedList<Map<String, String>>();
        // Creation des lignes de creances
        List<Map<String, String>> listDataSource = createSituationCompteDS(infoSection[2], COParameter.F1,
                COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2),
                ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE, infoSection[1]);
        if (!listDataSource.isEmpty()) {
            dataSource.addAll(listDataSource);
            this.setDataSource(dataSource);
        }

        initFooter(historique);
    }

    public String getDateExecutionPlus1Day() {
        return JadeDateUtil.addDays(getDateExecution(), 1);
    }

    /**
     * retourne la date de naissance pour le tiers donné.
     * 
     * @param tiers
     * @return retourne la date de naissance pour le tiers donné.
     * @throws Exception
     */
    protected String getDateNaissance(IntTiers tiers) throws Exception {
        try {
            return formatDate(getTiersService().getDateNaissance(getSession(), tiers));
        } catch (Exception e) {
            this.log("Impossible de trouver la date de naissance : " + e.getMessage());
            return "";
        }
    }

    /**
     * @return L'adresse suivis de la date de naissance du tier si personne physique
     * @throws Exception
     *             si l'adresse ou la date de naissance n'existe pas.
     */
    private String getInfoAdresse(IntTiers affilie) throws Exception {
        StringBuffer texte = new StringBuffer("");
        texte.append(getAdressePrincipaleEnvoiOP(affilie));
        if (!JadeStringUtil.isEmpty(getDateNaissance(affilie))) {
            texte.append(getDateNaissance(affilie));
        }
        return texte.toString();
    }

    private String initTexteApresDecompte(COHistorique historique) {
        int numeroteur = 2;
        String texteApresDecompte = "";

        // CHANGEMENT DU TEXTE DE LA SOMMATION
        Integer indexSommation = getIndexDSTitreCreancesSearchNatureRubrique(APIRubrique.TAXE_SOMMATION);
        if (indexSommation != null) {
            Map<String, String> taxeSommation = listDataSourceTitreCreances.get(indexSommation);
            // La taxe de sommation doit toujours être en 2eme lignes si y'en a une.
            if (taxeSommation != null && taxeSommation.isEmpty() == false) {
                FWCurrency montantSommation = new FWCurrency(taxeSommation.get(COParameter.F2));
                if (montantSommation.isZero() == false) {
                    String libelleEtape = "";
                    try {
                        libelleEtape = CodeSystem.getLibelleIso(getSession(), historique.getEtape().getLibEtape(),
                                getCatalogueTextesUtil().getLangueDoc());

                    } catch (Exception e) {
                        libelleEtape = historique.getEtape().getLibEtapeLibelle();
                    }
                    texteApresDecompte = formatMessage(
                            new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 91)), new Object[] { "\n",
                                    numeroteur + ") " + libelleEtape, formatDate(historique.getDateExecution()) });

                    removeFromListDSTitreCreances(indexSommation);
                    numeroteur++;
                }
            }
        }

        // CHANGEMENT DU TEXTE DES INTERETS
        Integer indexInterets = getIndexDSTitreCreancesSearchNatureRubrique(APIRubrique.INTERETS_MORATOIRES);
        Map<String, String> interets = null;
        if (indexInterets != null) {
            interets = listDataSourceTitreCreances.get(indexInterets);
            removeFromListDSTitreCreances(indexInterets);
        }

        for (Map<String, String> value : listDataSourceTitreCreances) {
            FWCurrency montantLigne = new FWCurrency(value.get(COParameter.F2));
            if (montantLigne.isZero() == false) {
                texteApresDecompte += formatMessage(
                        new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 91)), new Object[] { "\n",
                                value.get(COParameter.F1), formatDate(value.get(CAExtraitCompte.SECTIONDATE_FIELD)) });
                numeroteur++;
            }
        }

        if (interets != null) {
            texteApresDecompte += "\n\n" + interets.get(COParameter.F1);
        }

        return texteApresDecompte;
    }

    /**
     * Supression de l'emplacement de la liste
     */
    private void removeFromListDSCreances(Integer index) {
        // Pour enlever de la liste, nous settons l'emplacement de l'objet a null et nous faisons un remove(null)
        // afin que le linkedList efface toutes les cases ayant une valeur a null
        listDataSourceCreances.set(index, null);
        listDataSourceCreances.remove(null);
    }

    /**
     * Supression de l'emplacement de la liste
     */
    private void removeFromListDSTitreCreances(Integer index) {
        // Pour enlever de la liste, nous settons l'emplacement de l'objet a null et nous faisons un remove(null)
        // afin que le linkedList efface toutes les cases ayant une valeur a null
        listDataSourceTitreCreances.set(index, null);
        listDataSourceTitreCreances.remove(null);
    }

    /**
     * Pied de page
     * 
     * @param body
     * @throws Exception
     */
    private void initFooterNouveauRegime(COHistorique historique) throws Exception {
        StringBuffer body = new StringBuffer();
        getCatalogueTextesUtil().dumpNiveau(getParent(), 7, body, "\n");

        String texteApresDecompte = initTexteApresDecompte(historique);

        // l'avance de frais pour commencer la poursuite
        ICOTaxeProducer producer = COServiceLocator.getTaxeService()
                .getTaxeProducer(getTransition().getEtapeSuivante());
        List list = producer.getListeTaxes(getSession(), curContentieux, getTransition().getEtapeSuivante());
        String montantTaxe = CO01RequisitionPoursuite.ZERO_FRANC;

        if (list.size() > 0) {
            // TODO SEL : Gère qu'une seule taxe. A améliorer (voir :
            // transition_de.jsp taxe). Est-ce que toutes les taxes vont dans
            // Avance de frais ?
            COTaxe taxe = (COTaxe) list.get(0);
            montantTaxe = taxe.getMontantTaxe();
        }

        this.setParametres(
                COParameter.T12,
                formatMessage(body, new Object[] { "\n", curContentieux.getSection().getIdExterne(),
                        curContentieux.getSection().getCompteAnnexe().getIdExterneRole(),
                        formatDate(curContentieux.getSection().getDateSection()), texteApresDecompte,
                        formatMontant(montantTaxe), curContentieux.getSection().getDescription(getLangue()),
                        giveLibelleInfoRom246(), formatDate(getDateExecution()) }));

        // Rajoute la date à la signature
        this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA,
                getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_DATA) + " "
                        + formatDate(getDateExecution()));
    }

    /**
     * Pied de page
     * 
     * @param body
     * @throws Exception
     */
    private void initFooter(COHistorique historique) throws Exception {
        StringBuffer body = new StringBuffer();
        getCatalogueTextesUtil().dumpNiveau(getParent(), 6, body, "\n");

        // Si on a un historique, on affiche la date de l'étape
        String texteSommation = "";
        String libelleEtape = "";
        try {
            libelleEtape = CodeSystem.getLibelleIso(getSession(), historique.getEtape().getLibEtape(),
                    getCatalogueTextesUtil().getLangueDoc());

        } catch (Exception e) {
            libelleEtape = historique.getEtape().getLibEtapeLibelle();
        }
        if (historique != null) {
            texteSommation = formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 91)),
                    new Object[] { "\n", libelleEtape, formatDate(historique.getDateExecution()) });
        }

        // -- l'avance de frais pour commencer la poursuite
        ICOTaxeProducer producer = COServiceLocator.getTaxeService()
                .getTaxeProducer(getTransition().getEtapeSuivante());
        List list = producer.getListeTaxes(getSession(), curContentieux, getTransition().getEtapeSuivante());
        String montantTaxe = CO01RequisitionPoursuite.ZERO_FRANC;
        if (list.size() > 0) {
            // TODO SEL : Gère qu'une seule taxe. A améliorer (voir :
            // transition_de.jsp taxe). Est-ce que toutes les taxes vont dans
            // Avance de frais ?
            COTaxe taxe = (COTaxe) list.get(0);
            montantTaxe = taxe.getMontantTaxe();
        }

        this.setParametres(
                COParameter.T12,
                formatMessage(body, new Object[] { "\n", curContentieux.getSection().getIdExterne(),
                        curContentieux.getSection().getCompteAnnexe().getIdExterneRole(),
                        formatDate(curContentieux.getSection().getDateSection()), texteSommation,
                        formatMontant(montantTaxe), curContentieux.getSection().getDescription(getLangue()),
                        giveLibelleInfoRom246() }));

        // Rajoute la date à la signature
        this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA,
                getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_DATA) + " "
                        + formatDate(getDateExecution()));
    }

    /**
     * @return
     * @throws Exception
     */
    private COHistorique initHistorique() throws Exception {
        // Contrôler si ADB déjà effectuer, si oui on récupères les infos de
        // l'ADB (n° d'ADB)
        COHistorique historique = null;
        // Charge l'historique de l'ADB
        historique = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                ICOEtape.CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI);
        // S'il n'y a pas d'ADB, on va chercher l'historique de la décision
        if (historique == null) {
            // Charge l'historique de la décision
            historique = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux, ICOEtape.CS_DECISION);
            // S'il n'y a pas de décision, on va chercher l'historique de la
            // sommation
            if (historique == null) {
                historique = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                        ICOEtape.CS_SOMMATION_ENVOYEE);
            }
        } else {
            setADBExecutee(true);
        }
        return historique;
    }

    public boolean isADBExecutee() {
        return isADBExecutee;
    }

    public void setADBExecutee(boolean isADBExecutee) {
        this.isADBExecutee = isADBExecutee;
    }
}
