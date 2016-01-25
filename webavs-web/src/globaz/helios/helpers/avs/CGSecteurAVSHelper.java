package globaz.helios.helpers.avs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.helios.db.avs.CGSecteurAVSViewBean;
import globaz.helios.db.classifications.CGClassification;
import globaz.helios.db.classifications.CGClassificationListViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGMandat;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 23 sept. 04
 * 
 * @author scr
 * 
 */
public class CGSecteurAVSHelper extends FWHelper {

    /**
     * Constructor for CGLiaisonCompteClasseHelper.
     */
    public CGSecteurAVSHelper() {
        super();
    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {
        if (action.getActionPart().equals("ajouterSecteur")) {

            viewBean.setMsgType(FWViewBeanInterface.OK);
            // charge l'exercice comptable
            CGSecteurAVSViewBean vBean = (CGSecteurAVSViewBean) viewBean;
            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                // pas d'increment pour cette classe
                CGMandat mandat = new CGMandat();
                mandat.setSession((BSession) session);
                mandat.setIdMandat(vBean.getIdMandat());
                mandat.retrieve(transaction);
                if (mandat.isNew() || !mandat.isEstComptabiliteAVS().booleanValue()) {
                    throw new Exception(((BSession) session).getLabel("SECTEUR_AVS_MANDAT_NON_AVS"));
                }

                CGClassificationListViewBean classificationMgr = new CGClassificationListViewBean();
                classificationMgr.setSession((BSession) session);
                classificationMgr.setForIdMandat(vBean.getIdMandat());
                classificationMgr.setForIdTypeClassification(CGClassification.CS_TYPE_AVS_COMPTE);
                classificationMgr.find(transaction, 2);

                CGClassification classification = null;
                if (classificationMgr.size() == 0) {
                    throw new Exception(this.getClass().getName() + "_afterAdd() : No classification found!!!");
                } else if (classificationMgr.size() > 1) {
                    throw new Exception(this.getClass().getName() + "_afterAdd() : Too many classifications found!!!");
                } else {
                    classification = (CGClassification) classificationMgr.getEntity(0);
                }

                String idClassificationCompte = classification.getIdClassification();

                classificationMgr.setForIdTypeClassification(CGClassification.CS_TYPE_AVS_SECTEUR);
                classificationMgr.find(transaction, 2);
                if (classificationMgr.size() == 0) {
                    throw new Exception(this.getClass().getName() + "_afterAdd() : No classification found!!!");
                } else if (classificationMgr.size() > 1) {
                    throw new Exception(this.getClass().getName() + "_afterAdd() : Too many classifications found!!!");
                } else {
                    classification = (CGClassification) classificationMgr.getEntity(0);
                }

                String idClassificationSecteur = classification.getIdClassification();

                CGExerciceComptableManager exerMgr = new CGExerciceComptableManager();
                exerMgr.setSession((BSession) session);
                exerMgr.setForIdMandat(vBean.getIdMandat());
                exerMgr.setBetweenDateDebutDateFin(JACalendar.todayJJsMMsAAAA());
                exerMgr.find(transaction, 2);
                if (exerMgr.size() != 1) {
                    throw new Exception(this.getClass().getName()
                            + "._afterAdd() :Aucun exercice ouvert pour la date : " + JACalendar.todayJJsMMsAAAA());
                }

                CGExerciceComptable exercice = (CGExerciceComptable) exerMgr.getEntity(0);
                vBean.ouvrir((BTransaction) transaction, exercice, idClassificationCompte, idClassificationSecteur);
            } catch (Exception e) {
                try {
                    transaction.addErrors(e.getMessage());
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                e.printStackTrace();
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            } finally {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    e.printStackTrace();
                } finally {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return viewBean;
    }

}
