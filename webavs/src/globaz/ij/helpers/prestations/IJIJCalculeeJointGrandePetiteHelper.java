package globaz.ij.helpers.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prestations.IIJIJCalculee;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.vb.prestations.IJIJCalculeeJointGrandePetiteViewBean;
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
                || IIJIJCalculee.CS_TYPE_PETITE_IJ.equals(ijcViewBean.getCsTypeIJ())) {

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
                    gijCalculee.setCsGenreReadaptation(ijcViewBean.getCsGenreReadaptation());
                    gijCalculee.setCsStatutProfessionnel(ijcViewBean.getCsStatutProfessionnel());
                    gijCalculee.setCsTypeBase(ijcViewBean.getCsTypeBase());
                    gijCalculee.setDateDebutDroit(ijcViewBean.getDateDebutDroit());
                    gijCalculee.setDateFinDroit(ijcViewBean.getDateFinDroit());
                    gijCalculee.setDateRevenu(ijcViewBean.getDateRevenu());
                    gijCalculee.setDemiIJACBrut(ijcViewBean.getDemiIJACBrut());
                    gijCalculee.setDifferenceRevenu(ijcViewBean.getDifferenceRevenu());
                    gijCalculee.setMontantBase(ijcViewBean.getMontantBase());
                    gijCalculee.setMontantIndemniteAssistance(ijcViewBean.getMontantIndemniteAssistance());
                    gijCalculee.setMontantIndemniteEnfant(ijcViewBean.getMontantIndemniteEnfant());
                    gijCalculee.setMontantIndemniteExploitation(ijcViewBean.getMontantIndemniteExploitation());
                    gijCalculee.setMontantBase(ijcViewBean.getMontantBase());
                    gijCalculee.setNbEnfants(ijcViewBean.getNbEnfants());
                    gijCalculee.setNoAVS(ijcViewBean.getNoAVS());
                    gijCalculee.setNoRevision(ijcViewBean.getNoRevision());
                    gijCalculee.setOfficeAI(ijcViewBean.getOfficeAI());
                    gijCalculee.setPourcentDegreIncapaciteTravail(ijcViewBean.getPourcentDegreIncapaciteTravail());
                    gijCalculee.setRevenuDeterminant(ijcViewBean.getRevenuDeterminant());
                    gijCalculee.setRevenuJournalierReadaptation(ijcViewBean.getRevenuJournalierReadaptation());
                    gijCalculee.setSupplementPersonneSeule(ijcViewBean.getSupplementPersonneSeule());

                    gijCalculee.update(transaction);

                } else {

                    IJPetiteIJCalculee pijCalculee = new IJPetiteIJCalculee();
                    pijCalculee.setSession((BSession) session);
                    pijCalculee.setIdIJCalculee(ijcViewBean.getIdIJCalculee());
                    pijCalculee.retrieve(transaction);

                    // mise a jours
                    pijCalculee.setCsGenreReadaptation(ijcViewBean.getCsGenreReadaptation());
                    pijCalculee.setCsStatutProfessionnel(ijcViewBean.getCsStatutProfessionnel());
                    pijCalculee.setCsTypeBase(ijcViewBean.getCsTypeBase());
                    pijCalculee.setCsModeCalcul(ijcViewBean.getCsModeCalcul());
                    pijCalculee.setDateDebutDroit(ijcViewBean.getDateDebutDroit());
                    pijCalculee.setDateFinDroit(ijcViewBean.getDateFinDroit());
                    pijCalculee.setDateRevenu(ijcViewBean.getDateRevenu());
                    pijCalculee.setDemiIJACBrut(ijcViewBean.getDemiIJACBrut());
                    pijCalculee.setDifferenceRevenu(ijcViewBean.getDifferenceRevenu());
                    pijCalculee.setMontantBase(ijcViewBean.getMontantBase());
                    pijCalculee.setMontantBase(ijcViewBean.getMontantBase());
                    pijCalculee.setNoAVS(ijcViewBean.getNoAVS());
                    pijCalculee.setNoRevision(ijcViewBean.getNoRevision());
                    pijCalculee.setOfficeAI(ijcViewBean.getOfficeAI());
                    pijCalculee.setPourcentDegreIncapaciteTravail(ijcViewBean.getPourcentDegreIncapaciteTravail());
                    pijCalculee.setRevenuDeterminant(ijcViewBean.getRevenuDeterminant());
                    pijCalculee.setRevenuJournalierReadaptation(ijcViewBean.getRevenuJournalierReadaptation());
                    pijCalculee.setSupplementPersonneSeule(ijcViewBean.getSupplementPersonneSeule());

                    pijCalculee.update(transaction);
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

}
