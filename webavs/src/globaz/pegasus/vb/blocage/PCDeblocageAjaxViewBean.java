package globaz.pegasus.vb.blocage;

import globaz.framework.bean.JadeAbstractAjaxCrudDetailViewBean;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDeblocage;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocageSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.adresse.AdressePaiement;
import ch.globaz.pegasus.businessimpl.utils.adresse.AdresseHandler;
import ch.globaz.pyxis.business.model.AvoirPaiementSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * ViewBean ajax pour la gestion des déblocages
 * L'action add ets court-cicuité de la manière suivante:
 * - Valider liberation, validation de la liberation du deblocage
 * - Devalider liberation, dévalidation
 * - traitement par defautr (action null), ajout des entitées
 * 
 * @author dma
 * @author sce
 * 
 */
public class PCDeblocageAjaxViewBean extends
        JadeAbstractAjaxCrudDetailViewBean<SimpleLigneDeblocage, SimpleLigneDeblocageSearch> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action = null;
    @SuppressWarnings("unused")
    // utilisé pour la serialization en ajax
    private AdressePaiement adressePaiement = null;
    private String idAvoirPaiementUnique = null;
    private String idPca = null;
    private SimpleLigneDeblocage simpleDeblocage = new SimpleLigneDeblocage();
    private String designationTiers1 = null;
    private String designationTiers2 = null;

    public String getDesignationTiers1() {
        return designationTiers1;
    }

    public void setDesignationTiers1(String designationTiers1) {
        this.designationTiers1 = designationTiers1;
    }

    public String getDesignationTiers2() {
        return designationTiers2;
    }

    public void setDesignationTiers2(String designationTiers2) {
        this.designationTiers2 = designationTiers2;
    }

    @Override
    public void add() throws Exception {
        if (IPCActions.PARAM_ACTION_DEBLOCAGE_VALIDER_LIBERATION.equals(action)) {
            PegasusServiceLocator.getBlocageService().libererBlocage(idPca);
        } else if (IPCActions.PARAM_ACTION_DEBLOCAGE_DEVALIDER_LIBERATION.equals(action)) {
            PegasusServiceLocator.getBlocageService().devaliderLiberation(idPca);
        } else {
            // ajout des entitées exception si le montant est à zero
            checkAmount(simpleDeblocage);

            // si le check amount n'a pas généré de JadeThreadLogError...
            if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                getCurrentEntity().setCsEtat(IPCDeblocage.CS_ETAT_ENREGISTRE);

                if (isBlocageCreancier() && JadeStringUtil.isEmpty(simpleDeblocage.getIdApplicationAdressePaiement())) {
                    if (JadeStringUtil.isEmpty(idAvoirPaiementUnique)) {
                        throw new BlocageException("Unable to find the application the idAvoirPaiementUnique is null!");
                    }
                    AvoirPaiementSimpleModel avoirPaiementSimpleModel = new AvoirPaiementSimpleModel();
                    avoirPaiementSimpleModel.setId(idAvoirPaiementUnique);
                    avoirPaiementSimpleModel = (AvoirPaiementSimpleModel) JadePersistenceManager
                            .read(avoirPaiementSimpleModel);
                    simpleDeblocage.setIdApplicationAdressePaiement(avoirPaiementSimpleModel.getIdApplication());
                    simpleDeblocage.setIdTiersAdressePaiement(avoirPaiementSimpleModel.getIdTiers());

                    // TODO numeroExterne = null;
                    adressePaiement = AdresseHandler.convertAdressePaiement(TIBusinessServiceLocator
                            .getAdresseService().getAdressePaiementTiers(simpleDeblocage.getIdTiersAdressePaiement(),
                                    false, simpleDeblocage.getIdApplicationAdressePaiement(),
                                    JACalendar.todayJJsMMsAAAA(), avoirPaiementSimpleModel.getIdExterne()));

                    TiersSimpleModel tiersCreancier = TIBusinessServiceLocator.getTiersService().read(
                            simpleDeblocage.getIdTiersCreancier());

                    designationTiers1 = tiersCreancier.getDesignation1();
                    designationTiers2 = tiersCreancier.getDesignation2();

                }
                super.add();
            }

        }
    }

    /**
     * On teste ici que les montants soit différents de 0.00
     * 
     * @param deblocage l'entitée concernée par le check
     * @throws Exception, exception rmonté avec messgae UF
     */
    private void checkAmount(SimpleLigneDeblocage deblocage) throws Exception {
        if ("0.00".equals(deblocage.getMontant())) {
            dealExceptionAmountZero(deblocage.getCsTypeDeblocage());
        }
    }

    /**
     * Gestion de l'exception résultant du check du montant à 0
     * 
     * @param csTypeDeblocage le type de déblocage posant problème
     * @throws Exception l'exception thrower
     */
    private void dealExceptionAmountZero(String csTypeDeblocage) throws Exception {

        EPCTypeDeblocage typeDeblocage = EPCTypeDeblocage.getEnumByCsCode(csTypeDeblocage);

        switch (typeDeblocage) {

            case CS_CREANCIER:
                JadeThread.logError(getClass().getName(), "pegasus.deblocage.montant.creancier.zero");
                break;
            case CS_DETTE_EN_COMPTA:
                JadeThread.logError(getClass().getName(), "pegasus.deblocage.montant.dette.zero");
                break;
            case CS_VERSEMENT_BENEFICIAIRE:
                JadeThread.logError(getClass().getName(), "pegasus.deblocage.montant.versement.zero");
        }

    }

    public String getAction() {
        return action;
    }

    @Override
    public SimpleLigneDeblocage getCurrentEntity() {
        return simpleDeblocage;
    }

    public String getIdAvoirPaiementUnique() {
        return idAvoirPaiementUnique;
    }

    public String getIdPca() {
        return idPca;
    }

    @Override
    public JadeCrudService<SimpleLigneDeblocage, SimpleLigneDeblocageSearch> getService()
            throws JadeApplicationServiceNotAvailableException {
        return PegasusServiceLocator.getSimpleDeblocageService();
    }

    public SimpleLigneDeblocage getSimpleDeblocage() {
        return simpleDeblocage;
    }

    private boolean isBlocageCreancier() {
        return EPCTypeDeblocage.CS_CREANCIER.getCsCode().equals(simpleDeblocage.getCsTypeDeblocage());
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void setCurrentEntity(SimpleLigneDeblocage entite) {
        simpleDeblocage = entite;
    }

    public void setIdAvoirPaiementUnique(String idAvoirPaiementUnique) {
        this.idAvoirPaiementUnique = idAvoirPaiementUnique;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setSimpleDeblocage(SimpleLigneDeblocage simpleDeblocage) {
        this.simpleDeblocage = simpleDeblocage;
    }

    @Override
    public void update() throws Exception {
        getCurrentEntity().setCsEtat(IPCDeblocage.CS_ETAT_ENREGISTRE);

        if (JadeStringUtil.isBlankOrZero(getCurrentEntity().getMontant()) && !isBlocageCreancier()) {
            super.delete();
            setCurrentEntity(null);
        } else {
            super.update();
        }

    }
}
