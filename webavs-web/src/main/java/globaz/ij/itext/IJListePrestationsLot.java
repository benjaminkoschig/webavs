/*
 * Created on Jul 11, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.ij.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAVector;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationJointLotPrononce;
import globaz.ij.db.prestations.IJPrestationJointLotPrononceManager;
import globaz.ij.db.prestations.IJRepartJointCotJointPrestJointEmployeur;
import globaz.ij.db.prestations.IJRepartJointCotJointPrestJointEmployeurManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import com.lowagie.text.DocumentException;

/**
 * @author cuva
 * 
 *         Imprime une liste des prestations pour un id_lot donné, document d'information avant comptabilisation.
 */
public class IJListePrestationsLot extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List beneficiare;
    private Map finalTotauxMap = new Hashtable();
    // ---- fields -->
    private String idLot = null;
    private Map totauxMap;

    private boolean ajouterCommunePolitique = false;

    public boolean isAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

    // ---- Constructeur -->
    /**
     * Constructeur par defaut
     */
    public IJListePrestationsLot() throws Exception {
        this(new BSession(IJApplication.DEFAULT_APPLICATION_IJ));
    }

    @Override
    protected void bindPageHeader() throws Exception {

        _addHeaderLine(getFontCompanyName(), _getCompanyName(), null, null, getFontDate(),
                JACalendar.format(JACalendar.today()));

        _addHeaderLine(null, null, getFontDocumentTitle(), _getDocumentTitle(), null, null);

        if (ajouterCommunePolitique) {
            _addHeaderLine(getFontDate(),
                    getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()) + " : "
                            + getSession().getUserId(), null, null, null, null);

        }

    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public IJListePrestationsLot(BProcess parent) throws Exception {
        this();
        super.setParentWithCopy(parent);
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public IJListePrestationsLot(BSession session) {
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("LISTE_CONTROLE_TITLE"),
                new IJPrestationJointLotPrononceManager(), IJApplication.DEFAULT_APPLICATION_IJ);
    }

    // ---- Surcharge -->

    public static void main(String[] args) {
        try {
            BSession session = new BSession(IJApplication.DEFAULT_APPLICATION_IJ);
            session.connect("globazf", "ssiiadm");
            IJListePrestationsLot process = new IJListePrestationsLot();
            process.setSession(session);
            process.setIdLot("66");
            process.setEMailAddress("acu");
            JadeLogger.enableDebug(true);
            BProcessLauncher.start(process);

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Méthode appelée avant l'exécution du rapport
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList#_beforeExecuteReport()
     **/
    @Override
    public void _beforeExecuteReport() {
        IJPrestationJointLotPrononceManager manager = (IJPrestationJointLotPrononceManager) _getManager();
        manager.setSession(getSession());
        manager.setForIdLot(getIdLot());
        manager.setOrderBy(IJPrestationJointLotPrononce.FIELDNAME_NOM + ", "
                + IJPrestationJointLotPrononce.FIELDNAME_PRENOM + ", " + IJPrononce.FIELDNAME_ID_PRONONCE + ", "
                + IJPrestation.FIELDNAME_IDPRESTATION);

        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.CONTROLE_PRESTATIONS_LOT_IJ);

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("LISTE_CONTROLE_TITLE") + " " + idLot);

        try {
            ajouterCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, this.getClass().getName());
        }

        super._beforeExecuteReport();
    }

    private void addMontant(String field, String value, Map table) {
        FWCurrency montant;
        if (table.containsKey(field)) {
            montant = (FWCurrency) table.get(field);
        } else {
            montant = new FWCurrency(0);
        }
        montant.add(value);
        table.put(field, montant);
    }

    private void addMontant(String field, String value, Map table, boolean dependant) {
        FWCurrency montant;
        Map montants;
        if (JadeStringUtil.isEmpty(field)) {
            return;
        }
        if (!table.containsKey(field)) {
            montants = new HashMap(2);
            montants.put("true", new FWCurrency(0));
            montants.put("false", new FWCurrency(0));
        } else {
            montants = (Map) table.get(field);
        }
        montant = (FWCurrency) montants.get("" + dependant);
        montant.add(value);
        // On ajoute la valeur et on remonte la liste
        // TODO: Check if needed, not such if yes
        montants.put("" + dependant, montant);
        table.put(field, montants);
    }

    /**
     * Ajoute une ligne dans la table de données (utiliser <code>_addCell(Object)</code>)
     * 
     * @param entity
     *            l'entité contenant les données
     * @exception java.lang.Exception
     *                en cas s'erreur
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#addRow(globaz.globall.db.BEntity)
     **/
    @Override
    protected void addRow(BEntity value) throws FWIException {
        IJPrestationJointLotPrononce entityPrestation = (IJPrestationJointLotPrononce) value;
        IJRepartJointCotJointPrestJointEmployeurManager repManager = new IJRepartJointCotJointPrestJointEmployeurManager();
        repManager.setSession(getSession());
        repManager.setForIdPrestation(entityPrestation.getIdPrestation());
        FWCurrency montantBrut;
        JAVector emptyLine = new JAVector();
        if (ajouterCommunePolitique) {
            emptyLine.add("");
        }
        emptyLine.add("");
        emptyLine.add("");
        emptyLine.add("");
        emptyLine.add("");
        emptyLine.add("");
        try {
            // Impression de la prestation
            montantBrut = new FWCurrency(entityPrestation.getMontantBrut());
            this._addDataTableRow(emptyLine);
            preparePeriodeTable();
            if (ajouterCommunePolitique) {
                _addCell("");
            }
            _addCell("");
            _addCell(entityPrestation.getDateDebut());
            _addCell(entityPrestation.getDateFin());
            _addCell(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"));
            _addCell(montantBrut.toStringFormat());
            this._addDataTableRow();
            // Preparation des benificiares
            repManager.find();
            JAVector benef;
            String lastBenef = "";
            IJRepartJointCotJointPrestJointEmployeur entity;

            FWCurrency totalVentilations = new FWCurrency("0");

            // Les repartitions sont triees par id repartition, les ventilations
            // sont donc a la fin de la liste, car
            // elles sont crees en dernier. On parcour la liste de la fin au
            // debut pour avoir les ventilations en premier
            // et pour en faire la somme pour calculer le montant versé.
            for (int i = repManager.size() - 1; i >= 0; i--) {
                entity = (IJRepartJointCotJointPrestJointEmployeur) repManager.get(i);
                benef = new JAVector();

                // une ventillation
                if (JadeStringUtil.toDouble(entity.getMontantVentile()) > 0) {
                    if (ajouterCommunePolitique) {
                        benef.add("");
                    }
                    benef.add("");
                    benef.add("");
                    benef.add(getPaiementAdresseBeneficiaireFormate(entity.loadAdressePaiement(null)));
                    benef.add(getSession().getLabel("LIST_CTRL_VENTILATION"));
                    benef.add(new FWCurrency(entity.getMontantVentile()).toStringFormat());
                    beneficiare.add(benef);
                    totalVentilations.add(new FWCurrency(entity.getMontantVentile()));
                }// une cotisation
                else if (JadeStringUtil.toDouble(entity.getMontantBrut()) != 0) {

                    this.addMontant(entity.getGenreCotisation(), entity.getMontantCotisation(), totauxMap);
                    this.addMontant(entity.getGenreCotisation(), entity.getMontantCotisation(), finalTotauxMap,
                            entity.isBeneficiaireEmployeur());
                    // Beneficiare
                    if (!lastBenef.equals(entity.getIdRepartitionPaiement())) {
                        if (ajouterCommunePolitique) {
                            benef.add("");
                        }
                        benef.add("");
                        benef.add(getPaiementAdresseBeneficiaireFormate(entity.loadAdressePaiement(null)));
                        benef.add("");
                        benef.add(getSession().getCodeLibelle(entity.getTypePaiement()));
                        FWCurrency montantNet = new FWCurrency(entity.getMontantNet());
                        montantNet.sub(totalVentilations);
                        benef.add(montantNet.toStringFormat());
                        beneficiare.add(benef);
                        this.addMontant(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"), entity.getMontantBrut(),
                                finalTotauxMap, entity.isBeneficiaireEmployeur());
                        this.addMontant(getSession().getLabel("LIST_CTRL_MONTANT_TOT"), entity.getMontantNet(),
                                finalTotauxMap, entity.isBeneficiaireEmployeur());
                        lastBenef = entity.getIdRepartitionPaiement();
                    }
                } else if (JadeStringUtil.toDouble(entity.getMontantBrut()) == 0) {
                    this.addMontant(entity.getGenreCotisation(), entity.getMontantCotisation(), totauxMap);
                    this.addMontant(entity.getGenreCotisation(), entity.getMontantCotisation(), finalTotauxMap,
                            entity.isBeneficiaireEmployeur());
                    // Beneficiare
                    if (!lastBenef.equals(entity.getIdRepartitionPaiement())) {
                        if (ajouterCommunePolitique) {
                            benef.add("");
                        }
                        benef.add("");
                        benef.add(getPaiementAdresseBeneficiaireFormate(entity.loadAdressePaiement(null)));
                        benef.add("");
                        benef.add("");
                        benef.add("");
                        // benef.add(getSession().getCodeLibelle(entity.getTypePaiement()));
                        // FWCurrency montantNet = new FWCurrency(entity.getMontantNet());
                        // montantNet.sub(totalVentilations);
                        // benef.add(montantNet.toStringFormat());
                        beneficiare.add(benef);
                        this.addMontant(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"), entity.getMontantBrut(),
                                finalTotauxMap, entity.isBeneficiaireEmployeur());
                        this.addMontant(getSession().getLabel("LIST_CTRL_MONTANT_TOT"), entity.getMontantNet(),
                                finalTotauxMap, entity.isBeneficiaireEmployeur());
                        lastBenef = entity.getIdRepartitionPaiement();
                    }
                }
            }
            // On a tous recuperé on peut imprimer
            // Impression des montants
            Iterator itCoti = totauxMap.keySet().iterator();
            String key;
            FWCurrency montantNet = new FWCurrency(montantBrut.doubleValue());
            while (itCoti.hasNext()) {
                key = (String) itCoti.next();
                if (ajouterCommunePolitique) {
                    _addCell("");
                }
                _addCell("");
                _addCell("");
                _addCell("");
                if (IJRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE.equals(key)) {
                    _addCell(getSession().getLabel("LIST_CTRL_IMPOTS_SOURCE"));
                } else {
                    _addCell(key);
                }
                _addCell(((FWCurrency) totauxMap.get(key)).toStringFormat());
                montantNet.add((FWCurrency) totauxMap.get(key));
                this._addDataTableRow();
            }
            if (ajouterCommunePolitique) {
                _addCell("");
            }
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell(getSession().getLabel("LIST_CTRL_MONTANT_TOT"));
            _addCell(montantNet.toStringFormat());
            this._addDataTableRow();

            // On imprime les bénéficiares
            Iterator itBenef = beneficiare.iterator();
            while (itBenef.hasNext()) {
                this._addDataTableRow((JAVector) itBenef.next());
            }
            // On reset la liste des bénéficiaires et des totaux.
            beneficiare.clear();
            totauxMap.clear();
        } catch (Exception e) {
            throw new FWIException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #beginGroup(int,
     * globaz.globall.db.BEntity, globaz.globall.db.BEntity)
     */
    @Override
    protected void beginGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {

        if (0 != level) {
            return;
        }

        IJPrestationJointLotPrononce entity = (IJPrestationJointLotPrononce) nextEntity;
        // N'est pas le 1er groupement
        if (lastEntity != null) {

            if (ajouterCommunePolitique) {
                _addCell("__________________________________________________________________");
            }

            _addCell("__________________________________________________________________");
            _addCell("__________________________________________________________________");
            _addCell("__________________________________________________________________");
            _addCell("_________________________________");
            _addCell("____________________");

            try {
                this._addDataTableRow();
            } catch (DocumentException e) {
                throw new FWIException(e);
            }
        }

        if (ajouterCommunePolitique) {
            _addCell("\n" + entity.getCommunePolitique());
        }

        // Impression de la ligne d'inscription Prononce
        _addCell("\n" + entity.getNoAVS() + " / " + entity.getNom() + " " + entity.getPrenom() + " / "
                + entity.getDateNaissance() + " / " + getLibelleCourtSexe(entity.getCsSexe()) + " / "
                + getLibellePays(entity.getCsNationalite()));
        _addCell("");
        _addCell("\n" + entity.getDateFin());
        _addCell("\n" + getSession().getCodeLibelle(entity.getTypeIJ()));
        _addCell("\n" + entity.getOfficeAI());

        try {
            this._addDataTableRow();
        } catch (DocumentException e) {
            throw new FWIException(e);
        }

        // Initiaition des attribut du groupe
        beneficiare = new ArrayList();
        totauxMap = new Hashtable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #endGroup(int,
     * globaz.globall.db.BEntity, globaz.globall.db.BEntity)
     */
    @Override
    protected void endGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {
        // TODO Auto-generated method stub
        super.endGroup(level, lastEntity, nextEntity);
    }

    /**
     * Surcharge.
     * 
     * @param level
     * @param entity
     * @return
     * @throws globaz.framework.printing.itext.exception.FWIException
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#getGroupValue(int,
     *      globaz.globall.db.BEntity)
     **/
    @Override
    protected Object getGroupValue(int level, BEntity nextEntity) throws FWIException {
        IJPrestationJointLotPrononce entity = (IJPrestationJointLotPrononce) nextEntity;

        return entity.getIdBaseIndemnisation();
    }

    // <-- Properties -->
    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est passé en paramètre
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est passé en paramètre
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

    }

    /**
     * getter pour l'attribut paiement adresse beneficiaire formate
     * 
     * @return la valeur courante de l'attribut paiement adresse beneficiaire formate
     */
    private String getPaiementAdresseBeneficiaireFormate(TIAdressePaiementData adresse) {
        String retValue = "";

        try {

            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                retValue = new TIAdressePaiementBeneficiaireFormater().format(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retValue;
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

        if (ajouterCommunePolitique) {
            IJPrestationJointLotPrononceManager manager = (IJPrestationJointLotPrononceManager) _getManager();
            Set<String> setIdTiers = new HashSet<String>();
            Map<String, String> mapIdTiersCommunePolitique = new HashMap<String, String>();

            for (int i = 0; i < manager.size(); i++) {
                IJPrestationJointLotPrononce entityPrestation = (IJPrestationJointLotPrononce) manager.getEntity(i);
                if (!JadeStringUtil.isBlankOrZero(entityPrestation.getIdTiers())) {
                    setIdTiers.add(entityPrestation.getIdTiers());
                }

            }

            mapIdTiersCommunePolitique = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(), getSession());

            JAVector vectorManagerContainer = _getManager().getContainer();
            for (Object aContainerElement : vectorManagerContainer) {
                IJPrestationJointLotPrononce entityPrestation = (IJPrestationJointLotPrononce) aContainerElement;

                String communePolitique = mapIdTiersCommunePolitique.get(entityPrestation.getIdTiers());
                if (!JadeStringUtil.isEmpty(communePolitique)) {
                    entityPrestation.setCommunePolitique(communePolitique);
                }
            }

            Collections.sort(vectorManagerContainer);

            this._addColumnLeft(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()),
                    1);
        }

        this._addColumnLeft(getSession().getLabel("LIST_CTRL_COL_DETAIL_BEN"), 1);
        this._addColumnLeft("", 2);
        this._addColumnLeft(getSession().getLabel("LIST_CTRL_COL_PERIODE"), 2);
        this._addColumnLeft(getSession().getLabel("LIST_CTRL_COL_GENRE_IJ"), 1);
        this._addColumnRight(getSession().getLabel("LIST_CTRL_COL_COMMISSION"), 1);
        _groupManual();
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

    /**
     * Prepare la definition d'une nouvelle table de Prestation.
     * 
     * @return
     * @throws FWIException
     */
    private void preparePeriodeTable() throws FWIException {

        if (ajouterCommunePolitique) {
            _addCell("");
        }
        _addCell("");
        _addCell("\n" + getSession().getLabel("LIST_CTRL_PERIODE_DE"));
        _addCell("\n" + getSession().getLabel("LIST_CTRL_PERIODE_A"));
        _addCell("");
        _addCell("");
        try {
            this._addDataTableGroupRow();
        } catch (DocumentException e) {
            throw new FWIException(e);
        }

    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * Surcharge.
     * 
     * @throws globaz.framework.printing.itext.exception.FWIException
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#summary()
     **/
    @Override
    protected void summary() throws FWIException {
        String key = null;
        List orderedList = new ArrayList();
        FWIDocumentTable tbl = new FWIDocumentTable();
        tbl.addColumn("");
        tbl.addColumn(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"), FWITableModel.RIGHT, 1);
        orderedList.add(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"));
        Iterator itColumn = finalTotauxMap.keySet().iterator();
        // Display the names
        while (itColumn.hasNext()) {
            key = (String) itColumn.next();
            if (!getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)
                    && !getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                orderedList.add(key);

                if (IJRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE.equals(key)) {
                    tbl.addColumn(getSession().getLabel("LIST_CTRL_IMPOTS_SOURCE"), FWITableModel.RIGHT, 1);
                } else {
                    tbl.addColumn(key, FWITableModel.RIGHT, 1);
                }
            }
        }
        tbl.addColumn(getSession().getLabel("LIST_CTRL_MONTANT_TOT"), FWITableModel.RIGHT, 1);
        orderedList.add(getSession().getLabel("LIST_CTRL_MONTANT_TOT"));
        tbl.endTableDefinition();

        // Pour l'affichage des totaux
        FWCurrency totauxIndBrut = new FWCurrency("0.00");
        FWCurrency totauxMontantTotal = new FWCurrency("0.00");

        // Affichage des Affiliés
        Iterator itAff = orderedList.iterator();
        tbl.addCell(getSession().getLabel("LIST_CTRL_AFFILIE"));
        Map values = null;
        while (itAff.hasNext()) {
            key = (String) itAff.next();
            values = (Map) finalTotauxMap.get(key);
            tbl.addCell(((FWCurrency) values.get(Boolean.TRUE.toString())).toStringFormat());

            if (getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)) {
                totauxIndBrut.add(((FWCurrency) values.get(Boolean.TRUE.toString())));
            }
            if (getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                totauxMontantTotal.add(((FWCurrency) values.get(Boolean.TRUE.toString())));
            }
        }
        tbl.addRow();
        // Affichage des Assurés
        Iterator itAss = orderedList.iterator();
        tbl.addCell(getSession().getLabel("LIST_CTRL_ASSURE"));
        while (itAss.hasNext()) {
            key = (String) itAss.next();
            values = (Map) finalTotauxMap.get(key);
            tbl.addCell(((FWCurrency) values.get(Boolean.FALSE.toString())).toStringFormat());
            if (getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)) {
                totauxIndBrut.add(((FWCurrency) values.get(Boolean.FALSE.toString())));
            }
            if (getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                totauxMontantTotal.add(((FWCurrency) values.get(Boolean.FALSE.toString())));
            }
        }
        tbl.addRow();
        // ligne de separation
        Iterator it = orderedList.iterator();
        tbl.addCell("______________________________");
        while (it.hasNext()) {
            it.next();
            tbl.addCell("______________________________");
        }
        tbl.addRow();
        // Affichage des totaux
        tbl.addCell(getSession().getLabel("LIST_CTRL_TOTAUX"));
        it = orderedList.iterator();
        while (it.hasNext()) {
            key = (String) it.next();

            if (getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)) {
                tbl.addCell(totauxIndBrut.toStringFormat());
            } else if (getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                tbl.addCell(totauxMontantTotal.toStringFormat());
            } else {
                tbl.addCell("");
            }
        }
        tbl.addRow();

        super._addPageBreak();
        super._addTable(tbl);

        super.summary();
    }

}
