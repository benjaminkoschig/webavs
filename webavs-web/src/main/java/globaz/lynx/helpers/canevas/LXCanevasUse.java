package globaz.lynx.helpers.canevas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.canevas.LXCanevasOperationViewBean;
import globaz.lynx.db.canevas.LXCanevasSectionViewBean;
import globaz.lynx.db.canevas.LXCanevasViewBean;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.db.informationcomptable.LXInformationComptable;
import globaz.lynx.db.informationcomptable.LXInformationComptableManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.utils.LXUtils;

public class LXCanevasUse {

    /**
     * Permet de pr�parer une facture depuis un canevas.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static FWViewBeanInterface useCanevas(BISession session, FWViewBeanInterface viewBean) {

        LXFactureViewBean facture = (LXFactureViewBean) viewBean;
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            // -----------------------
            // Cr�ation de la section
            // -----------------------
            // 1. R�cup�ration section canevas
            LXCanevasSectionViewBean canevasSection = LXHelperUtils.getCanevasSection(session, transaction,
                    facture.getIdSection());
            facture.setIdExterne(canevasSection.getIdExterne());
            // 2. R�cup�ration ou cr�ation de la section
            LXSection section = LXHelperUtils.getSection(session, facture, transaction, LXSection.CS_TYPE_FACTURE);
            facture.setIdSection(section.getIdSection());

            // -----------
            // R�cup�ration des ventilations et pr�paration des ventilations de
            // la facture
            // -----------
            // 1. R�cup�ration des ventilations du canevas
            LXCanevasViewBean canevasViewBean = new LXCanevasViewBean();
            canevasViewBean.setIdSociete(facture.getIdSociete());
            canevasViewBean.setIdOperationCanevas(facture.getIdOperation()); // la
            // facture
            // a
            // encore
            // l'idcanevasoperation
            LXHelperUtils.fillWithCanevasVentilations(session, canevasViewBean);
            // 2. On copies les ventilations du canevas dans des ventilations
            // pour facture
            facture.setVentilations(LXHelperUtils.getVentilationFactureFromCanevas(facture.getMontant(),
                    canevasViewBean.getVentilations()));
            // On met a jour le nombre de ventilations pour affichage (nombre de
            // ligne affiche = 3 par defaut)
            if (facture.getVentilations().size() > 3) {
                facture.setShowRows(facture.getVentilations().size());
            }

            // ----------
            // Cr�ation de l'op�ration
            // ----------
            // 1. R�cup�ration de l'op�ration canevas
            LXCanevasOperationViewBean canevasOperation = LXHelperUtils.getCanevasOperation(session, transaction,
                    facture.getIdOperation());
            // 2. Copie des infos sur la facture
            facture.setIdSection(section.getIdSection());
            facture.setCsTypeOperation(canevasOperation.getCsTypeOperation());
            facture.setDateFacture(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            facture.setIdOrganeExecution(canevasOperation.getIdOrganeExecution());
            facture.setTauxEscompte(canevasOperation.getTauxEscompte());
            facture.setReferenceBVR(canevasOperation.getReferenceBVR());

            // -- Cr�ation de l'�ch�ance
            // ---- R�cup�ration des infos comptable pour avoir l'�ch�ance
            LXInformationComptableManager managerInfo = new LXInformationComptableManager();
            managerInfo.setSession((BSession) session);
            managerInfo.setForIdFournisseur(facture.getIdFournisseur());
            managerInfo.setForIdSociete(facture.getIdSociete());
            managerInfo.find();

            if (managerInfo.size() == 1) {
                LXInformationComptable infoComptable = (LXInformationComptable) managerInfo.getFirstEntity();
                int nbJourEcheance = Integer.parseInt(infoComptable.getEcheance());

                JACalendar calendar = new JACalendarGregorian();
                facture.setDateEcheance(JACalendar.format(
                        LXUtils.getDateOuvrable(calendar.addDays(JACalendar.today(), nbJourEcheance)),
                        JACalendar.FORMAT_DDsMMsYYYY));
            }

            facture.setReferenceExterne(canevasOperation.getReferenceExterne());
            facture.setCsCodeTVA(canevasOperation.getCsCodeTVA());
            if (!JadeStringUtil.isBlank(canevasOperation.getMotif())) {
                facture.setMotif(canevasOperation.getMotif());
            }
            facture.setIdAdressePaiement(canevasOperation.getIdAdressePaiement());
            facture.setCsCodeIsoMonnaie(canevasOperation.getCsCodeIsoMonnaie());
            // 3. cr�ation de l'op�ration
            LXOperation operation = LXHelperUtils.createOperation(session, facture, transaction, section);

            // -----------
            // R�cup�ration de l'adresse de paiement
            // -----------
            LXHelperUtils.fillForLayoutOnly(session, facture);

            // -----------
            // Cr�ation des ventilations
            // -----------
            // 3. On cr�e les ventilations
            facture.setIdOperation(operation.getIdOperation()); // on oublie pas
            // de remettre
            // l'id de
            // l'op�ration
            // de la facture
            // (et non celui
            // du canevas)
            // LXHelperUtils.createMontantFromPourcentage(facture);
            LXHelperUtils.addVentilations(session, transaction, facture, operation);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            facture.setMessage(e.toString());
            facture.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    facture.setMessage(e.toString());
                    facture.setMsgType(FWViewBeanInterface.ERROR);
                } finally {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        facture.setMessage(e.toString());
                        facture.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }
            }
        }

        return facture;
    }
}
