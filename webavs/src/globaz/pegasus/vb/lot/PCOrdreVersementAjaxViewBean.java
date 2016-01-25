/**
 * 
 */
package globaz.pegasus.vb.lot;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.prestation.tools.PRSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.OrdreVersement;
import ch.globaz.pegasus.business.models.lot.OrdreVersementSearch;
import ch.globaz.pegasus.business.models.lot.OrdreversementTiers;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementTypeResolver;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.ComputedOvGenerate;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.OrdreVersementDisplay;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.OrdreVersementStandardDisplay;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.OrdreVersementWrapper;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.OrdreversmentDetteDisplay;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dma
 */
public class PCOrdreVersementAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class OvKey implements Comparable<OvKey> {
        private String csType;
        private String label;
        private Integer ordre;

        public OvKey(String csType, Integer ordre, String label) {
            super();
            this.csType = csType;
            this.ordre = ordre;
            this.label = label;
        }

        @Override
        public int compareTo(OvKey o) {
            return ordre.compareTo(o.getOrdre());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            OvKey other = (OvKey) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (csType == null) {
                if (other.csType != null) {
                    return false;
                }
            } else if (!csType.equals(other.csType)) {
                return false;
            }
            return true;
        }

        public String getCsType() {
            return csType;
        }

        public String getLabel() {
            return label;
        }

        public Integer getOrdre() {
            return ordre;
        }

        private PCOrdreVersementAjaxViewBean getOuterType() {
            return PCOrdreVersementAjaxViewBean.this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + getOuterType().hashCode();
            result = (prime * result) + ((csType == null) ? 0 : csType.hashCode());
            return result;
        }
    }

    private String adressePaiement = null;
    private String adressePaiementConjoint;
    private String beneficiaire = null;
    private String csDomaineOv = null;
    private boolean displayOvsRow = true;
    private transient String libelleForOv = null;
    private transient Map<String, List<OrdreVersement>> mapOV = null;
    private transient BigDecimal montantDisponible = new BigDecimal(0);
    private String montantOv;
    private OrdreVersement ordreVersement = null;
    private String ordreVersementCsType = null;
    private String ordreVersementCsTypeLibelle;
    private transient OrdreVersementSearch ordreVersementSearch = null;
    private transient OrdreVersementDisplay ov;
    private transient Map<OvKey, OrdreVersementWrapper> ovDetteCreancier = new TreeMap<OvKey, OrdreVersementWrapper>();
    private transient Map<OvKey, OrdreVersementWrapper> ovPeriode = new TreeMap<OvKey, OrdreVersementWrapper>();
    private transient Map<String, OrdreVersementWrapper> ovs = new HashMap<String, OrdreVersementWrapper>();
    private transient List<OrdreVersement> ovsBd;
    private transient Map<OvKey, OrdreVersementWrapper> ovVersement = new TreeMap<OvKey, OrdreVersementWrapper>();

    // private transient OrdreVersementWrapper ovVersement = new OrdreVersementWrapper();

    /**
     * Constructeur simple
     */
    public PCOrdreVersementAjaxViewBean() {
        initList();
        ordreVersement = new OrdreVersement();
    }

    /**
     * Constructeur simple
     */
    public PCOrdreVersementAjaxViewBean(OrdreVersement ordreVersement) {
        initList();
        this.ordreVersement = ordreVersement;
    }

    @Override
    public void add() throws Exception {
    }

    private void addInOvDetteCreancier(OrdreVersementDisplay ovDisplay, OvKey key) {
        if (!ovDetteCreancier.containsKey(key)) {
            ovDetteCreancier.put(key, new OrdreVersementWrapper());
        }
        ovDetteCreancier.get(key).addOv(ovDisplay);
    }

    private void addInOvPeriode(OrdreVersementDisplay ovDisplay, OvKey key) {
        if (!ovPeriode.containsKey(key)) {
            OrdreVersementWrapper ordreVersementWrapper = new OrdreVersementWrapper();
            ovPeriode.put(key, ordreVersementWrapper);
        }
        ovPeriode.get(key).addOv(ovDisplay);
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public void find() throws Exception {

        OvKey keyDette = new OvKey(IREOrdresVersements.CS_TYPE_DETTE, 4, "JSP_LOT_ORDRE_VERSEMENT_L_PC_DETTE_CA");
        OvKey keyCreancier = new OvKey(IREOrdresVersements.CS_TYPE_TIERS, 5, "JSP_LOT_ORDRE_VERSEMENT_L_PC_CREANCIER");
        OvKey keyRestitution = new OvKey(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, 3,
                "JSP_LOT_ORDRE_VERSEMENT_L_PC_DEJA_VERSEE");
        OvKey keyBeneficiaire = new OvKey(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, 1,
                "JSP_LOT_ORDRE_VERSEMENT_L_PC_DUE");
        OvKey KeyJoursAppoint = new OvKey(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, 1,
                "JSP_LOT_ORDRE_VERSEMENT_L_JOURS_APPOINTS");
        OvKey keyAllocationNoel = new OvKey(IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL, 2,
                "JSP_LOT_ORDRE_VERSEMENT_L_PC_ALLOCATION_NOEL");
        OvKey keyOrdreVersement = new OvKey(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL
                + ComputedOvGenerate.PAIEMENT, 1, "");

        ovPeriode = new HashMap<PCOrdreVersementAjaxViewBean.OvKey, OrdreVersementWrapper>();
        montantDisponible = new BigDecimal(0);
        Map<String, OrdreVersementDisplay> mapAllocationNoel = new HashMap<String, OrdreVersementDisplay>();

        if (!displayOvsRow) {
            ComputedOvGenerate ovGenerate = new ComputedOvGenerate();

            ovs = ovGenerate.findComputedOvByIdPrestation(ordreVersementSearch.getForIdPrestation());

            for (Entry<String, OrdreVersementWrapper> entry : ovs.entrySet()) {
                if (entry.getKey().equals(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {
                    ovPeriode.put(keyBeneficiaire, entry.getValue());
                    montantDisponible = montantDisponible.add(entry.getValue().getSum());
                } else if (entry.getKey().equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION)) {
                    entry.getValue().negateSum();
                    ovPeriode.put(keyRestitution, entry.getValue());
                    montantDisponible = montantDisponible.add(entry.getValue().getSum());
                } else if (entry.getKey().equals(IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL)) {
                    ovPeriode.put(keyAllocationNoel, entry.getValue());
                    montantDisponible = montantDisponible.add(entry.getValue().getSum());
                } else if (entry.getKey().equals(IREOrdresVersements.CS_TYPE_DETTE)
                        || entry.getKey().equals(IREOrdresVersements.CS_TYPE_IMPOT_SOURCE)
                        || entry.getKey().equals(IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE)) {
                    entry.getValue().negateSum();
                    if (!ovDetteCreancier.containsKey(keyDette)) {
                        ovDetteCreancier.put(keyDette, new OrdreVersementWrapper());
                    }
                    ovDetteCreancier.get(keyDette).addAll(entry.getValue());
                } else if (entry.getKey().equals(IREOrdresVersements.CS_TYPE_TIERS + ComputedOvGenerate.PAIEMENT)) {
                    entry.getValue().negateSum();
                    ovDetteCreancier.put(new OvKey(entry.getKey(), 5, "JSP_LOT_ORDRE_VERSEMENT_L_PC_CREANCIER"),
                            entry.getValue());
                } else if (entry.getKey().equals(
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL + ComputedOvGenerate.PAIEMENT)) {
                    ovVersement.put(keyOrdreVersement, entry.getValue());
                } else if (entry.getKey().equals(
                        IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL + ComputedOvGenerate.PAIEMENT)) {
                    List<OrdreVersementDisplay> allocationNoels = entry.getValue().getOvs();
                    for (OrdreVersementDisplay allocation : allocationNoels) {
                        mapAllocationNoel.put(allocation.getDescriptionOv(), allocation);
                    }
                }
            }

            if (mapAllocationNoel.size() > 0) {
                if (ovVersement.containsKey(keyOrdreVersement)) {
                    for (OrdreVersementDisplay ov : ovVersement.get(keyOrdreVersement).getOvs()) {
                        OrdreVersementDisplay ovAllocation = mapAllocationNoel.get(ov.getDescriptionOv());
                        if (ovAllocation != null) {
                            ovVersement.get(keyOrdreVersement).addMontant(ov, ovAllocation.getMontant());
                            // this.montantDisponible = this.montantDisponible.add(ovAllocation.getMontant());
                        }
                    }
                }
            }

        } else {
            PegasusServiceLocator.getOrdreVersementService().search(ordreVersementSearch);
            // this.ovsBd = PersistenceUtil.typeSearch(this.ordreVersementSearch,
            // this.ordreVersementSearch.whichModelClass());
            for (JadeAbstractModel model : ordreVersementSearch.getSearchResults()) {
                OrdreVersement ov = (OrdreVersement) model;
                OrdreVersementDisplay ovDisplay = generateOvByOvRow(ov);
                if (OrdreVersementTypeResolver.isBeneficiarePrincipal(ov.getSimpleOrdreVersement())) {
                    addInOvPeriode(ovDisplay, keyBeneficiaire);
                    montantDisponible = montantDisponible.add(ovDisplay.getMontant());
                } else if (OrdreVersementTypeResolver.isRestitution(ov.getSimpleOrdreVersement())) {
                    addInOvPeriode(ovDisplay, keyRestitution);
                    montantDisponible = montantDisponible.subtract(ovDisplay.getMontant());
                } else if (OrdreVersementTypeResolver.isAllocationNoel(ov.getSimpleOrdreVersement())) {
                    addInOvPeriode(ovDisplay, keyAllocationNoel);
                } else if (OrdreVersementTypeResolver.isCreancier(ov.getSimpleOrdreVersement())) {
                    addInOvDetteCreancier(ovDisplay, keyCreancier);
                } else if (OrdreVersementTypeResolver.isDette(ov.getSimpleOrdreVersement())) {
                    addInOvDetteCreancier(ovDisplay, keyDette);
                } else if (OrdreVersementTypeResolver.isJoursAppoint(ov.getSimpleOrdreVersement())) {
                    addInOvPeriode(ovDisplay, KeyJoursAppoint);
                }
            }
            for (Entry<OvKey, OrdreVersementWrapper> entry : ovPeriode.entrySet()) {
                OrdreVersementWrapper ovWrapper = entry.getValue();

                Collections.sort(ovWrapper.getOvs(), new Comparator<OrdreVersementDisplay>() {
                    @Override
                    public int compare(OrdreVersementDisplay o1, OrdreVersementDisplay o2) {
                        if (o1.getNoPeriode().equals(o2.getNoPeriode())) {
                            if (o1.isRequerant()) {
                                return -1;
                            } else {
                                return 1;
                            }
                        } else {
                            return o1.getNoPeriode().compareTo(o2.getNoPeriode());
                        }
                    }
                });
                if (entry.getKey().equals(keyRestitution)) {
                    entry.getValue().negateSum();
                }
            }

            for (Entry<OvKey, OrdreVersementWrapper> entry : ovDetteCreancier.entrySet()) {
                entry.getValue().negateSum();
            }
        }
    }

    private APIRubrique findRubrique(String idCodeReferenceRubrique) throws ComptabiliserLotException {
        APIRubrique rubrique;
        APIReferenceRubrique referenceRubrique;
        try {
            referenceRubrique = (APIReferenceRubrique) PRSession.connectSession(
                    BSessionUtil.getSessionFromThreadContext(), "OSIRIS").getAPIFor(APIReferenceRubrique.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Technical exception, error to retrieve the reference rubrique", e);
        }

        rubrique = referenceRubrique.getRubriqueByCodeReference(idCodeReferenceRubrique);
        if (rubrique == null) {
            throw new ComptabiliserLotException("No rubrique was found with this reférenceRubrique: "
                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(idCodeReferenceRubrique) + " "
                    + idCodeReferenceRubrique);
        }
        return rubrique;
    }

    private OrdreVersementDisplay generateOvByOvRow(OrdreVersement ov) {
        if (OrdreVersementTypeResolver.isDette(ov.getSimpleOrdreVersement())) {
            return new OrdreversmentDetteDisplay(ov.getSimpleOrdreVersement().getIdSectionDetteEnCompta(),
                    new BigDecimal(JadeStringUtil.isBlankOrZero(ov.getSimpleOrdreVersement().getMontant()) ? ov
                            .getSimpleOrdreVersement().getMontantDetteModifier() : ov.getSimpleOrdreVersement()
                            .getMontant()), ov.getSimpleOrdreVersement().getCsType(), ov.getSimpleOrdreVersement()
                            .getIdOrdreVersement(), ov.getSimpleOrdreVersement().getNoGroupePeriode(), ov
                            .getSimplePrestation().getIdTiersBeneficiaire()
                            .equals(ov.getSimpleOrdreVersement().getIdTiers()));
        } else {
            OrdreversementTiers tiers = new OrdreversementTiers();
            tiers.setDateNaissance(ov.getTiers().getPersonne().getDateNaissance());
            tiers.setDesignation1(ov.getTiers().getTiers().getDesignation1());
            tiers.setDesignation2(ov.getTiers().getTiers().getDesignation2());
            tiers.setIdPays(ov.getTiers().getTiers().getIdPays());
            tiers.setIdTiers(ov.getTiers().getTiers().getIdTiers());
            tiers.setNumAvs(ov.getTiers().getPersonneEtendue().getNumAvsActuel());
            tiers.setSexe(ov.getTiers().getPersonne().getSexe());
            return new OrdreVersementStandardDisplay(ov.getSimpleOrdreVersement().getIdDomaineApplication(), ov
                    .getSimpleOrdreVersement().getIdTiersAdressePaiement(), new BigDecimal(ov.getSimpleOrdreVersement()
                    .getMontant()), tiers, ov.getSimpleOrdreVersement().getId(), ov.getSimpleOrdreVersement()
                    .getCsType(), ov.getSimpleOrdreVersement().getCsTypeDomaine(), !JadeStringUtil.isBlankOrZero(ov
                    .getSimpleOrdreVersement().getIdTiersAdressePaiementConjoint()), ov.getSimpleOrdreVersement()
                    .getNoGroupePeriode(), ov.getSimplePrestation().getIdTiersBeneficiaire()
                    .equals(ov.getSimpleOrdreVersement().getIdTiers()));
        }
    }

    private String getAdressePaiement(String idTiersAdresse, String idDomainApplication, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(idTiersAdresse)) {
            detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiersAdresse,
                    Boolean.TRUE, idDomainApplication, date, null);
        }
        return detailTiers != null ? JadeStringUtil.toNotNullString(detailTiers.getAdresseFormate()) : "";
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    // public String displayCreancier(OrdreVersement ordreVersement) throws JadeApplicationServiceNotAvailableException,
    // JadePersistenceException, JadeApplicationException {
    // // JadeStringUtil.isEmpty(lot.getDateEnvoi()) ? JACalendar.todayJJsMMsAAAA() : lot.getDateEnvoi(),
    // String format = "";
    // if (IREOrdresVersements.CS_TYPE_TIERS.equals(ordreVersement.getSimpleOrdreVersement().getCsType())
    // || IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE.equals(ordreVersement.getSimpleOrdreVersement()
    // .getCsType())
    // || IREOrdresVersements.CS_TYPE_IMPOT_SOURCE
    // .equals(ordreVersement.getSimpleOrdreVersement().getCsType())) {
    //
    // AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
    // ordreVersement.getSimpleOrdreVersement().getIdTiersAdressePaiement(), Boolean.TRUE,
    // ordreVersement.getSimpleOrdreVersement().getIdDomaineApplication(), JACalendar.todayJJsMMsAAAA(),
    // "");
    // if ((adresse != null) && (adresse.getFields() != null)) {
    //
    // format = adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1)
    // + new String(new char[] { '\r', '\n' })
    // + adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2)
    // + new String(new char[] { '\r', '\n' }) + " IBAN: "
    // + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE)
    // + new String(new char[] { '\r', '\n' });
    // }
    // return format;
    // }
    //
    // if (IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE.equals(ordreVersement.getSimpleOrdreVersement().getCsType())) {
    // // AdministrationAdresseSearchComplexModel search = new AdministrationAdresseSearchComplexModel();
    // // search.setForIdTiersAdministration(ordreVersement.getSimpleOrdreVersement().getIdTiers());
    // // search.setForIdApplication(ordreVersement.getSimpleOrdreVersement().getIdDomaineApplication());
    // // search.setForIdTiersAdministration(ordreVersement.getSimpleOrdreVersement().getIdTiers());
    // //
    // TIBusinessServiceLocator.getAdministrationService().findAdministrationAdresse(administrationAdresseSearchComplexModel);
    // // return format;
    // }
    //
    // return format;
    // }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return ordreVersement;
    }

    public String getDescriptionSection(String idSection) throws JadeApplicationException {
        return CABusinessServiceLocator.getSectionService().findDescription(idSection);
    }

    public boolean getDisplayOvsRow() {
        return displayOvsRow;
    }

    @Override
    public String getId() {
        return ordreVersement.getId();
    }

    public String getLibelleForOv() {
        if (montantDisponible.signum() == 1) {
            libelleForOv = "JSP_LOT_ORDRE_VERSEMENT_VERSEMENT_POSITIF";
        } else if (montantDisponible.signum() == -1) {
            libelleForOv = "JSP_LOT_ORDRE_VERSEMENT_VERSEMENT_NEGATIF";
        } else {
            libelleForOv = "JSP_LOT_ORDRE_VERSEMENT_VERSEMENT_NEUTRE";
        }
        return libelleForOv;
    }

    public BigDecimal getMontantDisponible() {
        return montantDisponible;
    }

    public String getMontantOv() {
        return montantOv;
    }

    public String getOrdreVersementCsType() {
        return ordreVersementCsType;
    }

    public String getOrdreVersementCsTypeLibelle() {
        return ordreVersementCsTypeLibelle;
    }

    public OrdreVersementDisplay getOv() {
        return ov;
    }

    public Map<OvKey, OrdreVersementWrapper> getOvDetteCreancier() {
        return ovDetteCreancier;
    }

    public Map<OvKey, OrdreVersementWrapper> getOvPeriode() {
        return ovPeriode;
    }

    public List<OrdreVersement> getOvsBd() {
        return ovsBd;
    }

    public Map<OvKey, OrdreVersementWrapper> getOvVersement() {
        return ovVersement;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return ordreVersementSearch;
    }

    public SimplePrestation getSimplePrestation() {
        return ((OrdreVersement) ordreVersementSearch.getSearchResults()[0]).getSimplePrestation();
    }

    @Override
    public void initList() {
        ordreVersementSearch = new OrdreVersementSearch();
    }

    @Override
    public void retrieve() throws Exception {
        if (!displayOvsRow) {
            ComputedOvGenerate ovGenerate = new ComputedOvGenerate();

            ovs = ovGenerate.findComputedOvByIdPrestation(ordreVersementSearch.getForIdPrestation());

            Map<String, OrdreVersementDisplay> map = new HashMap<String, OrdreVersementDisplay>();

            for (Entry<String, OrdreVersementWrapper> entry : ovs.entrySet()) {
                for (OrdreVersementDisplay ov : entry.getValue().getOvs()) {
                    map.put(ov.getId(), ov);
                }
            }

            ov = map.get(ordreVersement.getId());
            ordreVersement = new OrdreVersement();
            if (ov instanceof OrdreVersementStandardDisplay) {
                OrdreVersementStandardDisplay display = ((OrdreVersementStandardDisplay) ov);
                adressePaiement = getAdressePaiement(display.getIdTiersAdressePaiement(),
                        display.getIdDomainApplication(),
                        JadeStringUtil.isEmpty(ovGenerate.getLot().getDateEnvoi()) ? JACalendar.todayJJsMMsAAAA()
                                : ovGenerate.getLot().getDateEnvoi());

                if (display.getIdRefRurbrique() != null) {
                    csDomaineOv = findRubrique(display.getIdRefRurbrique()).getDescription();
                }
            }
        } else {
            ordreVersement = PegasusServiceLocator.getOrdreVersementService().read(
                    ordreVersement.getId().split("_id:")[1]);
            ov = generateOvByOvRow(ordreVersement);
            SimpleLot lot = CorvusServiceLocator.getLotService().read(ordreVersement.getSimplePrestation().getIdLot());

            if (!JadeStringUtil.isBlankOrZero(ordreVersement.getSimpleOrdreVersement().getIdTiersAdressePaiement())) {
                adressePaiement = getAdressePaiement(ordreVersement.getSimpleOrdreVersement()
                        .getIdTiersAdressePaiement(), ordreVersement.getSimpleOrdreVersement()
                        .getIdDomaineApplication(),
                        JadeStringUtil.isEmpty(lot.getDateEnvoi()) ? JACalendar.todayJJsMMsAAAA() : lot.getDateEnvoi());
            }

            if (!JadeStringUtil.isBlankOrZero(ordreVersement.getSimpleOrdreVersement()
                    .getIdTiersAdressePaiementConjoint())) {
                adressePaiementConjoint = getAdressePaiement(ordreVersement.getSimpleOrdreVersement()
                        .getIdTiersAdressePaiementConjoint(), ordreVersement.getSimpleOrdreVersement()
                        .getIdDomaineApplicationConjoint(),
                        JadeStringUtil.isEmpty(lot.getDateEnvoi()) ? JACalendar.todayJJsMMsAAAA() : lot.getDateEnvoi());
            }
            csDomaineOv = getISession().getCodeLibelle(ordreVersement.getSimpleOrdreVersement().getCsTypeDomaine());
        }
        montantOv = new FWCurrency(ov.getMontant().toString()).toStringFormat();
        beneficiaire = ov.getDescriptionOv();
        ordreVersementCsType = ov.getCsTypeOv();
        ordreVersementCsTypeLibelle = getISession().getCodeLibelle(ov.getCsTypeOv());
    }

    public void setDisplayOvsRow(boolean displayOvsRow) {
        this.displayOvsRow = displayOvsRow;
    }

    @Override
    public void setId(String newId) {
        ordreVersement.setId(newId);
    }

    public void setOrdreVersement(OrdreVersement ordreVersement) {
        this.ordreVersement = ordreVersement;
    }

    @Override
    public void update() throws Exception {
        PegasusServiceLocator.getOrdreVersementService().update(ordreVersement);
        find();
    }

}
