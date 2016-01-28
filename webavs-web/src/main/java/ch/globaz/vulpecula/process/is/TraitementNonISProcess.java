package ch.globaz.vulpecula.process.is;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.context.JadeThread;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import java.util.List;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.is.ImpotSourceService;
import ch.globaz.vulpecula.business.services.is.ProcessusAFService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.is.HistoriqueProcessusAf;
import ch.globaz.vulpecula.domain.models.registre.TypeFacturation;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.util.I18NUtil;

public class TraitementNonISProcess extends BProcessWithContext {
    private static final long serialVersionUID = -6539358010435862391L;

    private String idProcessusIndirect;

    private List<EntetePrestationComplexModel> entetesPaiementsIndirects;

    private ImpotSourceService impotSourceService = VulpeculaServiceLocator.getImpotSourceService();
    private ProcessusAFService processusAFService = VulpeculaServiceLocator.getProcessusAFService();

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        setSendCompletionMail(false);
        setSendMailOnError(true);

        traiterIndirects();
        print();

        return true;
    }

    private String ajouterHistorique(String idProcessus) {
        HistoriqueProcessusAf historique = new HistoriqueProcessusAf();
        historique.setIdProcessus(idProcessus);
        HistoriqueProcessusAf histo = VulpeculaRepositoryLocator.getHistoriqueProcessusAfRepository()
                .create(historique);
        return histo.getId();
    }

    private CompteAnnexeSimpleModel findCompteAnnexe(String idJournal, String idTiers, String numAvs) throws Exception {
        return CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(idJournal, idTiers,
                CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication()), numAvs,
                true);
    }

    private boolean traiterIndirects() throws Exception {
        String idHistorique = ajouterHistorique(idProcessusIndirect);
        JadeThread.commitSession();

        try {
            String idJournal = processusAFService.getProcessusAF(idProcessusIndirect).getPassageModel().getIdJournal();
            for (EntetePrestationComplexModel entete : entetesPaiementsIndirects) {
                // On ne traite que les employeurs qui sont en note de crédit
                if (!TypeFacturation.NOTE_DE_CREDIT.getValue().equals(entete.getTypeFacturation())) {
                    continue;
                }

                String idCompteAnnexe = findCompteAnnexe(idJournal, entete.getIdTiersAffilie(),
                        entete.getNumeroAffilie()).getIdCompteAnnexe();
                CAOperationOrdreVersementManager cao = new CAOperationOrdreVersementManager();
                cao.setForIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);
                cao.setForIdJournal(idJournal);
                cao.setForIdCompteAnnexe(idCompteAnnexe);
                cao.find();
                if (cao.size() == 0) {
                    getMemoryLog().logMessage(
                            I18NUtil.getMessageFromResource("vulpecula.is.aucun_ov_lie_entete", entete.getPeriodeDe(),
                                    entete.getPeriodeA(), entete.getMontantTotal(),
                                    entete.getNom() + " " + entete.getPrenom()), FWMessage.AVERTISSEMENT,
                            getClass().getName());
                } else if (cao.size() > 1) {
                    getMemoryLog().logMessage(
                            I18NUtil.getMessageFromResource("vulpecula.is.plusieurs_ovs_lie_entete",
                                    entete.getPeriodeDe(), entete.getPeriodeA(), entete.getMontantTotal(),
                                    entete.getNom() + " " + entete.getPrenom()), FWMessage.AVERTISSEMENT,
                            getClass().getName());
                }
                for (Object o : cao) {
                    CAOperationOrdreVersement ov = (CAOperationOrdreVersement) o;
                    ov.setEstBloque(true);
                    ov.update();
                }
            }

        } catch (Exception e) {
            VulpeculaRepositoryLocator.getHistoriqueProcessusAfRepository().deleteById(idHistorique);
            throw e;
        }
        return true;
    }

    private void print() {
        ImpressionRecapitulatifs process = new ImpressionRecapitulatifs();
        process.setTypeTraitRecapImpr(ALConstPrestations.TRAITEMENT_NUM_PROCESSUS);
        process.setNumProcessus(idProcessusIndirect);
        process.setDateImpression(Date.now().getSwissValue());
        process.setSession(getSession());
        process.run();
    }

    private void retrieve() {
        entetesPaiementsIndirects = impotSourceService.getEntetesPrestations(idProcessusIndirect);
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    @Override
    public String getSubjectDetail() {
        return super.getSubjectDetail();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public String getIdProcessusIndirect() {
        return idProcessusIndirect;
    }

    public void setIdProcessusIndirect(String idProcessusIndrect) {
        idProcessusIndirect = idProcessusIndrect;
    }
}
