package globaz.ij.helpers.prestations;

import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prestations.IIJIJCalculee;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.*;
import globaz.ij.vb.prestations.IJIJCalculeeJointGrandePetiteViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 *
 * @author bsc
 */
public class IJIJCalculeeJointGrandePetiteHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        IJIJCalculeeJointGrandePetiteViewBean ijcViewBean = (IJIJCalculeeJointGrandePetiteViewBean) viewBean;

        // pas de mise a jours pour les AIT
        if (IIJIJCalculee.CS_TYPE_GRANDE_IJ.equals(ijcViewBean.getCsTypeIJ())
                || IIJIJCalculee.CS_TYPE_PETITE_IJ.equals(ijcViewBean.getCsTypeIJ())
                || IIJPrononce.CS_FPI.equals(ijcViewBean.getCsTypeIJ())) {

            BTransaction transaction = null;

            try {

                transaction = (BTransaction) ((BSession) session).newTransaction();
                transaction.openTransaction();

                if (IIJIJCalculee.CS_TYPE_GRANDE_IJ.equals(ijcViewBean.getCsTypeIJ())) {

                    IJGrandeIJCalculee gijCalculee = new IJGrandeIJCalculee();
                    gijCalculee.setSession((BSession) session);
                    gijCalculee.setIdIJCalculee(ijcViewBean.getIdIJCalculee());
                    gijCalculee.retrieve(transaction);

                    // mise a jours
                    gijCalculee.setMontantIndemniteAssistance(ijcViewBean.getMontantIndemniteAssistance());
                    gijCalculee.setMontantIndemniteEnfant(ijcViewBean.getMontantIndemniteEnfant());
                    gijCalculee.setMontantIndemniteExploitation(ijcViewBean.getMontantIndemniteExploitation());
                    gijCalculee.setNbEnfants(ijcViewBean.getNbEnfants());
                    majIjCalculeCommun(ijcViewBean, gijCalculee, session);

                    gijCalculee.update(transaction);

                } else if (IIJIJCalculee.CS_TYPE_PETITE_IJ.equals(ijcViewBean.getCsTypeIJ())) {

                    IJPetiteIJCalculee pijCalculee = new IJPetiteIJCalculee();
                    pijCalculee.setSession((BSession) session);
                    pijCalculee.setIdIJCalculee(ijcViewBean.getIdIJCalculee());
                    pijCalculee.retrieve(transaction);

                    // mise a jours
                    pijCalculee.setCsModeCalcul(ijcViewBean.getCsModeCalcul());
                    majIjCalculeCommun(ijcViewBean, pijCalculee, session);

                    pijCalculee.update(transaction);
                } else {
                    IJFpiCalculee fpiCalculee = new IJFpiCalculee();
                    fpiCalculee.setSession((BSession) session);
                    fpiCalculee.setIdIJCalculee(ijcViewBean.getIdIJCalculee());
                    fpiCalculee.retrieve(transaction);

                    // mise a jours
                    fpiCalculee.setMontantEnfants(ijcViewBean.getMontantEnfant());
                    fpiCalculee.setNbEnfants(ijcViewBean.getNbEnfants());
                    fpiCalculee.setSalaireMensuel(ijcViewBean.getSalaireMensuel());
                    fpiCalculee.setCsModeCalcul(ijcViewBean.getCsModeCalcul());
                    majIjCalculeCommun(ijcViewBean, fpiCalculee, session);

                    fpiCalculee.update(transaction);
                }

                IJIndemniteJournaliere ijInt = ijcViewBean.getIndemniteJournaliereInterne();
                if (!ijInt.isNew()) {
                    ijInt.update(transaction);
                }

                IJIndemniteJournaliere ijExt = ijcViewBean.getIndemniteJournaliereExterne();
                if (!ijExt.isNew()) {
                    ijExt.update(transaction);
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                            transaction.rollback();
                        } else {
                            transaction.commit();
                        }
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
        }

    }


    private void majIjCalculeCommun(IJIJCalculeeJointGrandePetiteViewBean ijcViewBean, IJIJCalculee ijCalculee, BISession session) {
        ijCalculee.setCsGenreReadaptation(ijcViewBean.getCsGenreReadaptation());
        ijCalculee.setCsStatutProfessionnel(ijcViewBean.getCsStatutProfessionnel());
        ijCalculee.setCsTypeBase(ijcViewBean.getCsTypeBase());
        ijCalculee.setDateDebutDroit(ijcViewBean.getDateDebutDroit());
        ijCalculee.setDateFinDroit(ijcViewBean.getDateFinDroit());
        ijCalculee.setDateRevenu(ijcViewBean.getDateRevenu());
        ijCalculee.setDemiIJACBrut(ijcViewBean.getDemiIJACBrut());
        ijCalculee.setDifferenceRevenu(ijcViewBean.getDifferenceRevenu());
        ijCalculee.setMontantBase(ijcViewBean.getMontantBase());
        ijCalculee.setNoAVS(ijcViewBean.getNoAVS());
        ijCalculee.setNoRevision(ijcViewBean.getNoRevision());
        ijCalculee.setOfficeAI(ijcViewBean.getOfficeAI());
        ijCalculee.setPourcentDegreIncapaciteTravail(ijcViewBean.getPourcentDegreIncapaciteTravail());
        ijCalculee.setRevenuDeterminant(ijcViewBean.getRevenuDeterminant());
        ijCalculee.setRevenuJournalierReadaptation(ijcViewBean.getRevenuJournalierReadaptation());
        ijCalculee.setSupplementPersonneSeule(ijcViewBean.getSupplementPersonneSeule());
        if (!JadeStringUtil.isBlankOrZero((ijcViewBean.getGenreReadaptationAnnonce()))) {
            try {
                JadeCodeSysteme codeSysteme = JadeBusinessServiceLocator.getCodeSystemeService().getCodeSysteme(ijcViewBean.getCsGenreReadaptationAnnonces());
                if (codeSysteme != null) {
                    ijCalculee.setGenreReadaptationAnnonce(codeSysteme.getCodeUtilisateur(Langues.getLangueDepuisCodeIso(((BSession) session).getIdLangueISO())));
                }
            } catch (Exception e) {
                JadeLogger.error(this, "Impossible de transformer le code système en code utilisateur du genre de réadaptation annonce : " + e);
            }
        }
    }

}
