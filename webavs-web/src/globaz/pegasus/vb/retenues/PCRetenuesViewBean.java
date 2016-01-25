package globaz.pegasus.vb.retenues;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.comptes.CATypeSectionManager;
import globaz.pegasus.utils.PCDroitHandler;
import globaz.pegasus.utils.PCUserHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCRetenuesViewBean extends BJadePersistentObjectViewBean {

    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePmtDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineApplicatifDepuisPyxis", "idApplication" },
            new String[] { "numAffilieDepuisPyxis", "idExterneAvoirPaiement" } };

    public static Object[] getMethodesSelAdressePaiement() {
        return PCRetenuesViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    private String dateDernierPaiement = null;
    private String dateProchainPaiement = null;
    private String idPca;
    private String messageAvantProchaiementPaiement = null;
    private PCAccordee pcAccordee = null;
    private ListPCAccordee pcAccordeeList = null;

    public PCRetenuesViewBean() {
        super();
    }

    public PCRetenuesViewBean(SimpleRetenuePayement simpleRetenuePayement) {
        super();
    }

    @Override
    public void add() throws SimpleRetenuePayementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
    }

    @Override
    public void delete() throws SimpleRetenuePayementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
    }

    public String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public String getDateProchainPaiement() {
        return dateProchainPaiement;
    }

    public String getEtatPca() {
        return BSessionUtil.getSessionFromThreadContext()
                .getCodeLibelle(pcAccordee.getSimplePCAccordee().getCsEtatPC());
    }

    @Override
    public String getId() {
        return idPca;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getIdTiersFamilleInline() throws MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        StringBuilder concat = new StringBuilder();
        try {
            MembreFamilleVO[] mbf = HeraServiceLocator.getMembreFamilleService().searchMembresFamilleRequerant(
                    ISFSituationFamiliale.CS_DOMAINE_RENTES,
                    getPcAccordee().getPersonneEtendue().getTiers().getIdTiers(),
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

            boolean isFirstId = true;
            if (mbf.length < 1) {
                return "";
            }
            for (int i = 0; i < mbf.length; i++) {
                MembreFamilleVO membreFamilleVO = mbf[i];
                if (!JadeNumericUtil.isInteger(membreFamilleVO.getIdTiers())) {
                    continue;
                }
                if (isFirstId) {
                    isFirstId = false;
                } else {
                    concat.append("|");
                }
                concat.append(membreFamilleVO.getIdTiers());
            }

        } catch (MembreFamilleException e) {
            return "";
        }
        return concat.toString();
    }

    public String getMessageAvantProchaiementPaiement() {
        return messageAvantProchaiementPaiement;
    }

    /*
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return PCRetenuesViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    public String getMontantConjoint() {
        return new FWCurrency(pcAccordee.getSimplePrestationsAccordeesConjoint().getMontantPrestation())
                .toStringFormat();
    }

    public String getMontantRequerant() {
        return new FWCurrency(pcAccordee.getSimplePrestationsAccordees().getMontantPrestation()).toStringFormat();
    }

    public PCAccordee getPcAccordee() {
        return pcAccordee;
    }

    /**
     * Retourne l'état de la pca en fonction du résultat du plan de calcul PARTIEL, OCTROI, ou montant si octroyé
     * 
     * @return
     */
    public String getPCAResultState() {

        if (pcAccordeeList.getSimplePlanDeCalcul().getEtatPC() == null) {
            return "";
        }
        // si octroi
        if (pcAccordeeList.getSimplePlanDeCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {
            return new FWCurrency(pcAccordeeList.getSimplePlanDeCalcul().getMontantPCMensuelle()).toStringFormat();
        }
        // refus ou octroi partiel
        else {
            // octroi partiel
            if (pcAccordeeList.getSimplePlanDeCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
                return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECISION_OCTROI_PARTIEL");
            }
            // refus
            else {
                return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECISION_REFUS");
            }
        }
    }

    /**
     * Retourne le modele complex de la personne
     * 
     * @return personneComplexModel
     */
    private PersonneEtendueComplexModel getPersonneEtendue(String membre) {
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return pcAccordee.getPersonneEtendue();
        } else if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
            return pcAccordee.getPersonneEtendueConjoint();
        } else {
            return null;
        }
    }

    public String getReqeurantDetail() {
        return PCDroitHandler.getInfoHtmlRequerant(pcAccordee.getSimpleVersionDroit(), pcAccordee.getPersonneEtendue());
    }

    public String getRequerantInfos() {
        String reqInfos = PCUserHelper.getDetailAssure(getSession(),
                getPersonneEtendue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        return reqInfos;
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
        return null;
    }

    public String getTypeLibelle(String code) {
        return getSession().getCode(code);
    }

    public List<String[]> getTypesSectionsCA() {
        List<String[]> list = new ArrayList<String[]>();

        CATypeSectionManager manTypeSection = new CATypeSectionManager();
        manTypeSection.setSession(getSession());

        try {
            manTypeSection.find();

            for (int i = 0; i < manTypeSection.size(); i++) {
                CATypeSection elm = (CATypeSection) manTypeSection.get(i);
                list.add(new String[] { elm.getIdTypeSection(), elm.getDescription(getSession().getIdLangueISO()) });
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public boolean isDom2R() {
        return !JadeStringUtil.isBlankOrZero(pcAccordee.getSimplePCAccordee().getIdPrestationAccordeeConjoint());
    }

    public boolean isUpdatable() {
        return IPCDroits.CS_VALIDE.equals(pcAccordee.getSimpleVersionDroit().getCsEtatDroit())
                || IPCDroits.CS_CALCULE.equals(pcAccordee.getSimpleVersionDroit().getCsEtatDroit());
    }

    /**
     * @throws Exception
     */
    @Override
    public void retrieve() throws Exception {
        pcAccordee = PegasusServiceLocator.getPCAccordeeService().readDetail(idPca);
        dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        dateDernierPaiement = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        pcAccordeeList = PegasusServiceLocator.getPCAccordeeService().read(idPca);
        messageAvantProchaiementPaiement = JadeThread.getMessage("pegasus.retenu.dateDebut.avantProchaiementPaiement");
    }

    @Override
    public void setId(String id) {
        idPca = id;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws SimpleRetenuePayementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
    }
}
