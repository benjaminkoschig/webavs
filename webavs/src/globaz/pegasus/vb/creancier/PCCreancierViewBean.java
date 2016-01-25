package globaz.pegasus.vb.creancier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCCreancierHandler;
import globaz.pegasus.utils.PCDroitHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.ListTotal;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.CalculPrestationsVerses;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCCreancierViewBean extends BJadePersistentObjectViewBean {

    private Creancier creancier;
    private Droit droitCourant = null;

    private BigDecimal totalDette = null;
    private BigDecimal totalVerse = null;
    private PCAccordeeSearch pcAccordeeSearch = null;
    private Map<String, SimpleJoursAppoint> mapJoursAppoints = null;

    public PCCreancierViewBean() {
        super();
        creancier = new Creancier();
        droitCourant = new Droit();
    }

    /**
     * @param creancier
     */
    public PCCreancierViewBean(Creancier creancier) {
        super();
        this.creancier = creancier;
    }

    private void initpcAccordeeSearch() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        pcAccordeeSearch = new PCAccordeeSearch();
        pcAccordeeSearch.setWhereKey(PCAccordeeSearch.FOR_CURRENT_VERSIONED_WITHOUT_COPIE);
        pcAccordeeSearch.setOrderKey(PCAccordeeSearch.ORDER_BY_DATE_DEBUT);
        pcAccordeeSearch.setForIdDemande(droitCourant.getDemande().getId());
        pcAccordeeSearch = PegasusServiceLocator.getPCAccordeeService().search(pcAccordeeSearch);

    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void add() throws CreancierException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        creancier.getSimpleCreancier().setCsEtat(IPCDroits.CS_ETAT_CREANCE_A_PAYE);
        creancier = PegasusServiceLocator.getCreancierService().create(creancier);
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        creancier = PegasusServiceLocator.getCreancierService().delete(creancier);
    }

    public String displayCreancierTier() {
        return PCCreancierHandler.displayCreancierTiers(creancier.getSimpleTiers());
    }

    private BigDecimal findMontantDette() throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {

        ListTotal<SimpleDetteComptatCompense> listTotal = PegasusServiceLocator.getDetteComptatCompenseService()
                .findListTotalCompense(droitCourant.getSimpleVersionDroit().getId(),
                        droitCourant.getSimpleDroit().getId());
        return listTotal.getTotal();
    }

    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getIdTiers())) {
            detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                    creancier.getSimpleCreancier().getIdTiers(), Boolean.TRUE,
                    creancier.getSimpleCreancier().getIdDomaineApplicatif(), JACalendar.todayJJsMMsAAAA(),
                    creancier.getSimpleCreancier().getIdAffilieAdressePaiment());
        }

        return detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    /**
     * @return creancier
     */
    public Creancier getCreancier() {
        return creancier;
    }

    @Override
    public String getId() {
        return creancier.getId();
    }

    public String getInfoTiers() {
        String infos = "";
        TiersSimpleModel simpleTiersSimpleModel = creancier.getSimpleTiers();
        if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
            infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
        }
        return infos;
    }

    public JadeAbstractModel[] getListAssure() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        DroitMembreFamilleEtenduSearch membreSearch = new DroitMembreFamilleEtenduSearch();

        membreSearch.setForIdDemande(creancier.getSimpleCreancier().getIdDemande());

        membreSearch = PegasusServiceLocator.getDroitService().searchDroitMemebreFamilleEtendu(membreSearch);

        return membreSearch.getSearchResults();
    }

    /**
     * Retourne le montant effectif à disposition
     * 
     * @return
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PmtMensuelException
     * @throws DecisionException
     */
    public Float getMontantADisposition(String idDemande) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PmtMensuelException,
            DecisionException {
        // recup map montant retro brut
        Map<String, String> mapMontantRetroBrut = PegasusServiceLocator.getPCAccordeeService()
                .getMapMontantRetroActifBrut(idDemande, droitCourant.getSimpleVersionDroit().getNoVersion());
        // Recup prestations verse
        CalculPrestationsVerses montantVerse = new CalculPrestationsVerses(droitCourant.getSimpleVersionDroit()
                .getNoVersion(), creancier.getSimpleCreancier().getIdDemande());
        totalVerse = montantVerse.getMontantVerse();

        BigDecimal sommeADispo = new BigDecimal(0);
        for (String montant : mapMontantRetroBrut.values()) {
            sommeADispo = sommeADispo.add(new BigDecimal(montant));// Integer.parseInt(montant);
        }
        for (SimpleJoursAppoint jAppoint : mapJoursAppoints.values()) {
            sommeADispo = sommeADispo.add(new BigDecimal(jAppoint.getMontantTotal()));// Integer.parseInt(montant);
        }
        sommeADispo = sommeADispo.subtract(totalVerse);
        return (sommeADispo).subtract(totalDette).floatValue();
    }

    private List<String> getListIdPcas(List<PCAccordee> listePca) {
        List<String> idsPca = new ArrayList<String>();

        for (PCAccordee pca : listePca) {
            idsPca.add(pca.getId());
        }
        return idsPca;
    }

    /**
     * Initialisation des jours d'appoints
     * Création d'une map de jours d'appoint
     * Ajout des jours d'appoint dans la pca
     * 
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void initJoursAppoint() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        List<PCAccordee> listePca = getListPcAccordee();

        mapJoursAppoints = new HashMap<String, SimpleJoursAppoint>();

        if (getListIdPcas(listePca).size() != 0) {
            List<SimpleJoursAppoint> listeJoursAppoint = new ArrayList<SimpleJoursAppoint>();

            SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
            search.setInIdsPca(getListIdPcas(listePca));
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = PegasusImplServiceLocator.getSimpleJoursAppointService().search(search);
            listeJoursAppoint = PersistenceUtil.typeSearch(search, search.whichModelClass());

            for (SimpleJoursAppoint joursAppoint : listeJoursAppoint) {

                String idPca = joursAppoint.getIdPCAccordee();

                for (PCAccordee pca : listePca) {
                    if (pca.getId().equals(idPca)) {
                        pca.addJoursAppoint(joursAppoint);
                        mapJoursAppoints.put(idPca, joursAppoint);
                        break;
                    }
                }
            }
        }

    }

    public List<PCAccordee> getListPcAccordee() throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        List<PCAccordee> list = new ArrayList<PCAccordee>();

        for (JadeAbstractModel model : pcAccordeeSearch.getSearchResults()) {

            list.add((PCAccordee) model);
        }

        return list;
    }

    public String getRequerantDetail() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return PCDroitHandler.getInfoHtmlRequerant(droitCourant.getSimpleVersionDroit().getIdVersionDroit());
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (creancier != null) && !creancier.isNew() ? new BSpy(creancier.getSpy()) : new BSpy(getSession());
    }

    /**
     * Retourne l'état CALCULE ou PAS du droit courant
     * 
     * @param idDemande
     * @return
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public Boolean isDroitCourantCalculeOrValide() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        return (droitCourant.getSimpleVersionDroit().getCsEtatDroit().equals(IPCDroits.CS_CALCULE) || droitCourant
                .getSimpleVersionDroit().getCsEtatDroit().equals(IPCDroits.CS_VALIDE));
    }

    /**
     * @throws Exception
     */
    @Override
    public void retrieve() throws Exception {
        // Droit courant
        DroitSearch search = new DroitSearch();
        search.setForIdDemandePc(creancier.getSimpleCreancier().getIdDemande());
        search.setWhereKey("currentVersion");
        droitCourant = (Droit) PegasusServiceLocator.getDroitService().searchDroit(search).getSearchResults()[0];
        totalDette = findMontantDette();
        initpcAccordeeSearch();
        initJoursAppoint();
    }

    /**
     * @param creancier
     *            the creancier to set
     */
    public void setCreancier(Creancier creancier) {
        this.creancier = creancier;

    }

    /**
     * @param idCreancier
     */
    @Override
    public void setId(String id) {
        creancier.setId(id);
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        creancier = PegasusServiceLocator.getCreancierService().update(creancier);
    }
}
