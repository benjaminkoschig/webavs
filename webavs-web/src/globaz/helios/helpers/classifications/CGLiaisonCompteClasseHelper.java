package globaz.helios.helpers.classifications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.helios.db.classifications.CGClasseCompte;
import globaz.helios.db.classifications.CGClassification;
import globaz.helios.db.classifications.CGLiaisonCompteClasse;
import globaz.helios.db.classifications.CGLiaisonCompteClasseViewBean;
import globaz.helios.servlet.CGActionLiaisonCompteClasse;
import java.util.Iterator;
import java.util.Vector;

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
public class CGLiaisonCompteClasseHelper extends FWHelper {

    /**
     * Constructor for CGLiaisonCompteClasseHelper.
     */
    public CGLiaisonCompteClasseHelper() {
        super();
    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        CGLiaisonCompteClasseViewBean vBean = (CGLiaisonCompteClasseViewBean) viewBean;

        vBean.setMsgType(FWViewBeanInterface.OK);
        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            if (CGActionLiaisonCompteClasse.ACTION_AJOUTER_LIAISON.equals(action.getActionPart())) {

                Vector v = vBean.getNewClassesComptes();
                Iterator iter = v.iterator();
                while (iter.hasNext()) {
                    String idClasseCompte = (String) iter.next();

                    // On ne peut ajouter / supprimer des liasons entre classe
                    // et classe de comptes,
                    // uniquement sur des classification de type manuel
                    CGClasseCompte cc = new CGClasseCompte();
                    cc.setSession((BSession) session);
                    cc.setIdClasseCompte(idClasseCompte);
                    cc.retrieve(transaction);

                    CGClassification classification = new CGClassification();
                    classification.setSession((BSession) session);

                    classification.setIdClassification(cc.getIdClassification());
                    classification.retrieve(transaction);

                    if (!CGClassification.CS_TYPE_MANUEL.equals(classification.getIdTypeClassification())) {
                        throw new Exception(((BSession) session).getLabel("LIAISON_CC_CLASSIFICATION_READ_ONLY"));
                    }

                    // Création des liasons
                    CGLiaisonCompteClasse lien = new CGLiaisonCompteClasse();
                    lien.setSession(vBean.getSession());
                    lien.setIdCompte(vBean.getIdCompte());
                    lien.setIdClasseCompte(idClasseCompte);
                    lien.add(transaction);
                }
            } else if (CGActionLiaisonCompteClasse.ACTION_SUPPRIMER_LIAISON.equals(action.getActionPart())) {
                Vector v = vBean.getCurrentClassesComptes();
                Iterator iter = v.iterator();
                while (iter.hasNext()) {
                    String idClasseCompte = (String) iter.next();

                    // On ne peut ajouter / supprimer des liasons entre classe
                    // et classe de comptes,
                    // uniquement sur des classification de type manuel
                    CGClasseCompte cc = new CGClasseCompte();
                    cc.setSession((BSession) session);
                    cc.setIdClasseCompte(idClasseCompte);
                    cc.retrieve(transaction);

                    CGClassification classification = new CGClassification();
                    classification.setSession((BSession) session);

                    classification.setIdClassification(cc.getIdClassification());
                    classification.retrieve(transaction);

                    if (!CGClassification.CS_TYPE_MANUEL.equals(classification.getIdTypeClassification())) {
                        throw new Exception(((BSession) session).getLabel("LIAISON_CC_CLASSIFICATION_READ_ONLY"));
                    }

                    // Création des liasons
                    CGLiaisonCompteClasse lien = new CGLiaisonCompteClasse();
                    lien.setSession(vBean.getSession());
                    lien.setIdCompte(vBean.getIdCompte());
                    lien.setIdClasseCompte(idClasseCompte);
                    lien.retrieve(transaction);
                    if (!lien.isNew()) {
                        lien.delete(transaction);
                    }
                }
            }
        } catch (Exception e) {
            try {
                transaction.addErrors(e.getMessage());
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            e.printStackTrace();
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
        } finally {
            try {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                vBean.setMsgType(FWViewBeanInterface.ERROR);
                e.printStackTrace();
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return vBean;
    }

}
