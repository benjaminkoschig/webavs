/*
 * Globaz SA.
 */
package globaz.hercule.db.noncertifiesconformes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.declarationStructuree.CESaisieMasseReceptionViewBean;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.pyxis.db.tiers.TIRole;

public class CESaisieReceptionNCCViewBean extends BJadePersistentObjectViewBean {

    private String forDateReception;
    private String forIdAffiliation;
    private String forNumeroAffilie;
    private String forAnnee;

    public CESaisieReceptionNCCViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {

        if (JadeStringUtil.isBlankOrZero(getForIdAffiliation())) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(((BSession) getISession()).getLabel("ERR_NO_AFFILIE_VIDE"));
            return;
        }

        BSession session = (BSession) getISession();

        try {

            AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(getForIdAffiliation(), session);

            if (affiliation == null) {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(((BSession) getISession()).getLabel("AUCUNE_AFFILIATION_TROUVE"));
                return;
            }

            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    CEApplication.DEFAULT_APPLICATION_HERCULE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, getForAnnee());

            viewBean.setSession(session);
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_NON_CERTIFIES_CONFORMES);

            viewBean.find(BManager.SIZE_USEDEFAULT);

            /**
             * les critères saisis ci-dessus garantissent un enregistrement unique si ce n'est pas le cas, il faut lever
             * une exception
             */
            if (viewBean.isEmpty()) {
                throw new HerculeException(CESaisieMasseReceptionViewBean.class.getName() + "\n"
                        + session.getLabel("SUIVI_NCC_NON_TROUVE"));
            }

            genererReception(viewBean);

        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
            return;
        }
    }

    private void genererReception(LUJournalListViewBean viewBean) throws Exception {

        LUJournalViewBean vBean = new LUJournalViewBean();
        vBean = (LUJournalViewBean) viewBean.getEntity(0);
        BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
        BSession sessionLeo = (BSession) remoteApplication.newSession(getISession());
        LEJournalHandler journalHandler = new LEJournalHandler();

        BTransaction transaction = null;
        try {
            transaction = new BTransaction((BSession) getISession());
            transaction.openTransaction();
            journalHandler.genererJournalisationReception(vBean.getIdJournalisation(), getForDateReception(),
                    sessionLeo, transaction);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (transaction != null && transaction.isOpened()) {
                transaction.closeTransaction();
            }
        }
    }

    @Override
    public void delete() throws Exception {
        // Not implemented
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // Not implemented
    }

    @Override
    public void setId(String newId) {
        // Not implemented
    }

    @Override
    public void update() throws Exception {
        // Not implemented
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getForDateReception() {
        return forDateReception;
    }

    public void setForDateReception(String forDateReception) {
        this.forDateReception = forDateReception;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

}
