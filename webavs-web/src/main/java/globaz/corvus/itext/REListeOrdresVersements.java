package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.ordresversements.REListeOrdresVersementsEntity;
import globaz.corvus.db.ordresversements.REListeOrdresVersementsManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.prestation.domaine.PrestationAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Imprime la liste des ordres de versement d'un lot. La liste doit contenir tout ce qui sera paye en comptabilisant ce
 * lot.
 */
public class REListeOrdresVersements extends FWIAbstractManagerDocumentList {

    /**
     * Petite classe interne Pojo pour contenir les données nécessaire à la génération de chaque lignes.</br>
     * Cette classe étend BEntity afin de pour remplacer les entités dans le container du manager.
     * 
     * @author lga
     * 
     */
    private class Pojo extends BEntity implements Comparable<Pojo> {

        private static final long serialVersionUID = 1L;
        private REListeOrdresVersementsEntity entity;
        PRTiersWrapper beneficiairePrincipal;
        private String communePolitique;

        public Pojo(REListeOrdresVersementsEntity entity, PRTiersWrapper beneficiairePrincipal) {
            this.entity = entity;
            this.beneficiairePrincipal = beneficiairePrincipal;
        }

        public REListeOrdresVersementsEntity getEntity() {
            return entity;
        }

        public PRTiersWrapper getBeneficiairePrincipal() {
            return beneficiairePrincipal;
        }

        /**
         * @return the idTiers
         */
        public final String getIdTiersCommunePolitique() {
            return beneficiairePrincipal.getIdTiers();
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        /**
         * @return the nom
         */
        public final String getNom() {
            return beneficiairePrincipal.getNom();
        }

        /**
         * @return the prenom
         */
        public final String getPrenom() {
            return beneficiairePrincipal.getPrenom();
        }

        @Override
        public int compareTo(Pojo o) {
            if (getAjouterCommunePolitique()) {
                int result1 = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (result1 != 0) {
                    return result1;
                }
            }

            int result2 = getNom().compareTo(o.getNom());
            if (result2 != 0) {
                return result2;
            }

            return getPrenom().compareTo(o.getPrenom());
        }

        @Override
        protected String _getTableName() {
            return null;
        }

        @Override
        protected void _readProperties(BStatement statement) throws Exception {
            // Nothing to do
        }

        @Override
        protected void _validate(BStatement statement) throws Exception {
            // Nothing to do
        }

        @Override
        protected void _writePrimaryKey(BStatement statement) throws Exception {
            // Nothing to do
        }

        @Override
        protected void _writeProperties(BStatement statement) throws Exception {
            // Nothing to do
        }

    }

    private static final long serialVersionUID = 1L;
    private String forIdLot;
    private BigDecimal montantEnFaveurDuBeneficiairePrincipal;
    private BigDecimal montantTotalVerse;
    private List<Pojo> ordresVersements;
    private boolean ajouterCommunePolitique;

    public REListeOrdresVersements() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
    }

    public REListeOrdresVersements(BProcess parent) throws Exception {
        this();
        super.setParentWithCopy(parent);
    }

    public REListeOrdresVersements(BSession session) {
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("LISTE_OVE_TITRE_MAIL"),
                new REListeOrdresVersementsManager(), REApplication.DEFAULT_APPLICATION_CORVUS);

        forIdLot = null;
        montantEnFaveurDuBeneficiairePrincipal = BigDecimal.ZERO;
        montantTotalVerse = BigDecimal.ZERO;
        ordresVersements = new ArrayList<Pojo>();
    }

    @Override
    protected void bindPageHeader() throws Exception {
        super.bindPageHeader();
        if (getAjouterCommunePolitique()) {
            String utilisateur = getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey())
                    + " : " + getSession().getUserId();
            this._addHeaderLine(getFontDate(), utilisateur, null, null, null, null);
        }
    }

    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_ORDRES_DE_VERSEMENTS_LOT);

        REListeOrdresVersementsManager manager = (REListeOrdresVersementsManager) getManager();
        manager.setSession(getSession());
        manager.setForIdLot(Long.parseLong(forIdLot));

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(FWMessageFormat.format(getSession().getLabel("LISTE_OVE_TITRE"), forIdLot));

    }

    @Override
    protected void addRow(BEntity value) throws FWIException {
        Pojo pojo = (Pojo) value;
        REListeOrdresVersementsEntity entity = pojo.getEntity();

        switch (entity.getType()) {
            case BENEFICIAIRE_PRINCIPAL:
                // on imprime la ligne concernant le bénéficiaire principal
                ordresVersements.add(pojo);
                // pas de break, car on veut le même traitement que pour les intérêts moratoires (à savoir ajouter le
                // montant à ce qui va être versé à l'assuré
            case INTERET_MORATOIRE:
                // pour ces types on ajoute le montant aux intérêts du bénéficiaire principale.
                // la ligne n'es pas imprimée
                montantEnFaveurDuBeneficiairePrincipal = montantEnFaveurDuBeneficiairePrincipal
                        .add(entity.getMontant());
                break;

            case IMPOT_A_LA_SOURCE:
            case DIMINUTION_DE_RENTE:
                // on déduit la créance de ce qui va être payé au bénéficiaire principal
                montantEnFaveurDuBeneficiairePrincipal = montantEnFaveurDuBeneficiairePrincipal.subtract(entity
                        .getMontant());
                break;

            case DETTE:
            case DETTE_RENTE_AVANCES:
            case DETTE_RENTE_DECISION:
            case DETTE_RENTE_PRST_BLOQUE:
            case DETTE_RENTE_RESTITUTION:
            case DETTE_RENTE_RETOUR:
                // la ligne n'es pas imprimée
                if (entity.isCompense()) {
                    // si l'OV est compensé, on déduit la dette de ce qui va être payé au bénéficiaire principal
                    montantEnFaveurDuBeneficiairePrincipal = montantEnFaveurDuBeneficiairePrincipal.subtract(entity
                            .getMontant());
                }
                break;

            case CREANCIER:
            case ASSURANCE_SOCIALE:
                montantEnFaveurDuBeneficiairePrincipal = montantEnFaveurDuBeneficiairePrincipal.subtract(entity
                        .getMontant());
                // on imprimera aussi ce le créancier recevra
                ordresVersements.add(pojo);
                break;

            case ALOCATION_DE_NOEL:
            case JOURS_APPOINT:
                // on ne fait rien pour ces types d'ordre de versement (spécifique PC)
                break;
        }

    }

    @Override
    protected void beginGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {

        // mise a zero du montant pour la prestations
        montantEnFaveurDuBeneficiairePrincipal = BigDecimal.ZERO;

        // suppression des ordres de versement de la prestation précédente
        ordresVersements.clear();
    }

    /**
     * Impression des OV après lecture et traitement de tous les OV de la prestation
     */
    @Override
    protected void endGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {

        try {
            BSessionUtil.initContext(getSession(), this);
            RETiersForJspUtils tiersForJspUtils = RETiersForJspUtils.getInstance(getSession());

            Iterator<Pojo> iterator = ordresVersements.iterator();
            while (iterator.hasNext()) {
                Pojo pojo = iterator.next();

                REListeOrdresVersementsEntity ov = pojo.getEntity();
                BigDecimal montant = ov.getMontant();

                if (ov.getType() == TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL) {
                    montant = montantEnFaveurDuBeneficiairePrincipal;
                }

                if (montant.compareTo(BigDecimal.ZERO) > 0) {

                    if (getAjouterCommunePolitique()) {
                        _addCell(pojo.getCommunePolitique());
                    }
                    Decision decision = CorvusServiceLocator.getDecisionService().getDecisionPourIdOrdreVersement(
                            ov.getIdOrdreVersement());

                    PrestationAccordee prestationAccordeePrincipale = decision.getRenteAccordeePrincipale();
                    PersonneAVS beneficiairePrincipal = decision.getBeneficiairePrincipal();
                    PRTiersWrapper assure = PRTiersHelper.getPersonneAVS(getSession(), beneficiairePrincipal.getId()
                            .toString());
                    if (assure != null) {
                        _addCell(tiersForJspUtils.getDetailsTiers(assure, false, false));
                    } else {
                        _addCell("Tiers non-défini : idTiers=" + beneficiairePrincipal.getId());
                    }

                    String idTiersAdressePaiement = null;

                    switch (ov.getType()) {
                        case ASSURANCE_SOCIALE:
                        case CREANCIER:
                            idTiersAdressePaiement = ov.getIdTiersAdressePaiement().toString();
                            break;

                        default:
                            idTiersAdressePaiement = prestationAccordeePrincipale.getAdresseDePaiement().getId()
                                    .toString();
                            break;
                    }

                    TIAdressePaiementData apData = PRTiersHelper.getAdressePaiementData(
                            getSession(),
                            null,
                            idTiersAdressePaiement,
                            REApplication.CS_DOMAINE_ADRESSE_CORVUS,
                            null,
                            JadeStringUtil.isEmpty(ov.getDateEnvoi()) ? new JADate().toStr(".") : PRDateFormater
                                    .convertDate_AAAAMMJJ_to_JJxMMxAAAA(ov.getDateEnvoi()));

                    _addCell(getAdressePaiementBanqueCCP(apData));
                    _addCell(getAdressePaiementBeneficiaire(apData));

                    _addCell(new FWCurrency(montant.toString()).toStringFormat());
                    // mise a jour du montant total verse
                    montantTotalVerse = montantTotalVerse.add(montant);
                }

                if (iterator.hasNext()) {
                    this._addDataTableGroupRow();
                }
            }
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }
    }

    /**
     * @return l'adresse de paiement du bénéficiaire mise au format CCP
     */
    private String getAdressePaiementBanqueCCP(TIAdressePaiementData adresse) {
        String retValue = "";
        try {
            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                if (JadeStringUtil.isEmpty(source.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CCP))) {
                    retValue = new TIAdressePaiementBanqueFormater().format(source);
                } else {
                    retValue = new TIAdressePaiementCppFormater().format(source);
                }

            }
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }

        return retValue;
    }

    /**
     * @return l'adresse de paiement du bénéficiaire mise au format
     */
    private String getAdressePaiementBeneficiaire(TIAdressePaiementData adresse) {
        String retValue = "";

        try {

            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                retValue = new TIAdressePaiementBeneficiaireFormater().format(source);
            }
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }

        return retValue;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * On groupe les lignes par prestation
     */
    @Override
    protected Object getGroupValue(int level, BEntity entity) throws FWIException {
        return ((Pojo) entity).getEntity().getIdPrestation();
    }

    /**
     * Initialise la table des données
     * <p>
     * <u>Utilisation</u>:
     * <ul>
     * <li><code>_addColumn(..)</code> permet de déclarer les colonnes
     * <li><code>_group...(..)</code> permet de déclarer les groupages
     * </ul>
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#initializeTable()
     **/
    @Override
    protected void initializeTable() {
        if (getAjouterCommunePolitique()) {
            this._addColumnCenter(getSession()
                    .getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 1);
        }
        this._addColumnLeft(getSession().getLabel("LISTE_OVE_DETAIL_ASSURE"), 6);

        this._addColumnLeft(getSession().getLabel("LISTE_OVE_ADRESSE_PAIEMENT"), 3);
        this._addColumnLeft("", 3);

        this._addColumnRight(getSession().getLabel("LISTE_OVE_MONTANT"), 1);
        _groupManual();

        Set<String> setIdTiers = new HashSet<String>();
        JAVector pojos = new JAVector();

        try {
            BSessionUtil.initContext(getSession(), this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Pojo pojo = null;
        try {
            for (Object object : getManager().getContainer()) {
                REListeOrdresVersementsEntity ov = (REListeOrdresVersementsEntity) object;

                Decision decision;
                PRTiersWrapper assure = null;
                decision = CorvusServiceLocator.getDecisionService().getDecisionPourIdOrdreVersement(
                        ov.getIdOrdreVersement());
                PersonneAVS beneficiairePrincipal = decision.getBeneficiairePrincipal();
                assure = PRTiersHelper.getPersonneAVS(getSession(), beneficiairePrincipal.getId().toString());

                pojo = new Pojo((REListeOrdresVersementsEntity) object, assure);
                setIdTiers.add(pojo.getIdTiersCommunePolitique());
                pojos.add(pojo);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }

        RELot lot = null;
        try {
            lot = new RELot();
            lot.setSession(getSession());
            lot.setId(forIdLot);
            lot.retrieve();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (lot.isNew()) {
            throw new RuntimeException("No RELot found with id [" + forIdLot + "]");
        }

        String dateCreationLot = lot.getDateCreationLot();

        // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
        if (getAjouterCommunePolitique()) {
            Date date;
            try {
                date = new SimpleDateFormat("dd.MM.yyyy").parse(dateCreationLot);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Map<String, String> mapCommuneParIdTiers = PRTiersHelper
                    .getCommunePolitique(setIdTiers, date, getSession());

            for (Object p : pojos) {
                ((Pojo) p).setCommunePolitique(mapCommuneParIdTiers.get(((Pojo) p).getIdTiersCommunePolitique()));
            }
        }

        try {
            Collections.sort(pojos);
        } catch (RuntimeException e1) {
            throw new RuntimeException(e1);
        }
        getManager().setContainer(pojos);

    }

    @Override
    protected void summary() throws FWIException {
        // on imprime le total à la fin du document
        try {
            JAVector row = null;
            if (getAjouterCommunePolitique()) {
                row = new JAVector(5);
                row.add("_____________");
            } else {
                row = new JAVector(4);
            }

            row.add("____________________________________________________________________________________");
            row.add("_______________________________________________");
            row.add("______________________________________________");
            row.add("_____________________");
            this._addDataTableRow(row);

            row.clear();
            if (getAjouterCommunePolitique()) {
                row.add("");
            }
            row.add(getSession().getLabel("LISTE_OVE_MONTANT_TOTAL"));
            row.add("");
            row.add("");
            row.add(new FWCurrency(montantTotalVerse.toString()).toStringFormat());
            this._addDataTableRow(row);
        } catch (Exception e) {
            throw new FWIException(e);
        }
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process (constantes dans <code>GlobazJobQueue</code>).
     * 
     * @return la Job Queue à utiliser pour soumettre le process
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     **/
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }
}
