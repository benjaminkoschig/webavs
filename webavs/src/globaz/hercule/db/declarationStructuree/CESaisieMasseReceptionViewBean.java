package globaz.hercule.db.declarationStructuree;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEAffilie;
import globaz.hercule.db.controleEmployeur.CEAffilieManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.pyxis.db.tiers.TIRole;

/**
 * @author SCO
 * @since 15 juil. 2010
 */
public class CESaisieMasseReceptionViewBean extends BJadePersistentObjectViewBean {

    private String forAnnee = "";
    private String forDateReception = "";
    // Attributs
    private String forIdAffiliation = "";
    private String forNumeroAffilie = "";

    /**
     * Constructeur de CESaisieMasseReceptionViewBean
     */
    public CESaisieMasseReceptionViewBean() {
        super();
    }

    /**
     * Cette fonction transfère la date de réception d'un document dans LEO afin de clôturer un envoi
     */
    @Override
    public void add() throws Exception {

        if (JadeStringUtil.isBlankOrZero(getForIdAffiliation())) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(((BSession) getISession()).getLabel("ERR_NO_AFFILIE_VIDE"));
            return;
        }

        CEAffilieManager manager = new CEAffilieManager();
        manager.setSession((BSession) getISession());
        manager.setForIdAffiliation(forIdAffiliation);

        try {
            manager.find();
            /**
             * les critères saisis ci-dessus garantissent un enregistrement unique si ce n'est pas le cas, il faut lever
             * une exception
             */
            if (manager.size() != 1) {
                throw new Exception(CESaisieMasseReceptionViewBean.class.getName() + "\n"
                        + ((BSession) getISession()).getLabel("DS_SAISIE_MASSE_RECEPTION_ERREUR_FIND_ENREGISTREMENT"));
            }
            CEAffilie entityCEAffilie = (CEAffilie) manager.getEntity(0);
            /**
             * Recherche dans la gestion des envois s'il y a un envoi qui concerne le document qu'on a reçu
             */
            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, entityCEAffilie.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, entityCEAffilie.getNumAffilie());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    CEApplication.DEFAULT_APPLICATION_HERCULE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, entityCEAffilie.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, getForAnnee());

            viewBean.setISession(getISession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS_STRUCTURE);

            viewBean.find();

            /**
             * les critères saisis ci-dessus garantissent un enregistrement unique si ce n'est pas le cas, il faut lever
             * une exception
             */
            if (viewBean.size() < 1) {
                throw new HerculeException(CESaisieMasseReceptionViewBean.class.getName() + "\n"
                        + ((BSession) getISession()).getLabel("DS_SAISIE_MASSE_RECEPTION_ERREUR_FIND_ENREGISTREMENT"));
            }

            /**
             * Génération de la reception du document dans LEO
             */
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

                // Nouvelle couverture pour cette affilié. getForAnnee() + 4
                // ans.
                int anneeCouverture = CEUtils.transformeStringToInt(getForAnnee()) + 4;
                String idCouverture = CEControleEmployeurService.findIdCouvertureActiveByNumAffilie(
                        (BSession) getISession(), entityCEAffilie.getNumAffilie());
                CEControleEmployeurService.updateCouverture((BSession) getISession(), transaction, idCouverture, ""
                        + anneeCouverture, entityCEAffilie.getIdAffiliation(), entityCEAffilie.getNumAffilie());

                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            } finally {
                if (transaction != null && transaction.isOpened()) {
                    transaction.closeTransaction();
                }
            }

        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
            return;
        }
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForDateReception() {
        return forDateReception;
    }

    /**
     * getter
     */

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    public void setForDateReception(String newForDateReception) {
        forDateReception = newForDateReception;
    }

    /**
     * setter
     */

    public void setForIdAffiliation(String newForIdAffiliation) {
        forIdAffiliation = newForIdAffiliation;
    }

    public void setForNumeroAffilie(String newForNumeroAffilie) {
        forNumeroAffilie = newForNumeroAffilie;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
