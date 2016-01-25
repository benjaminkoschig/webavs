package globaz.lynx.helpers.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.canevas.LXCanevasOperation;
import globaz.lynx.db.canevas.LXCanevasOperationManager;
import globaz.lynx.db.canevas.LXCanevasOperationViewBean;
import globaz.lynx.db.canevas.LXCanevasSection;
import globaz.lynx.db.canevas.LXCanevasSectionManager;
import globaz.lynx.db.canevas.LXCanevasSectionViewBean;
import globaz.lynx.db.canevas.LXCanevasVentilationListViewBean;
import globaz.lynx.db.canevas.LXCanevasVentilationViewBean;
import globaz.lynx.db.canevas.LXCanevasViewBean;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.db.fournisseur.LXFournisseurViewBean;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.operation.LXOperationViewBean;
import globaz.lynx.db.paiement.LXPaiementViewBean;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.db.section.LXSectionManager;
import globaz.lynx.db.section.LXSectionViewBean;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean;
import globaz.lynx.db.ventilation.LXVentilationListViewBean;
import globaz.lynx.db.ventilation.LXVentilationViewBean;
import globaz.lynx.service.helios.LXHeliosService;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.lynx.utils.LXUtils;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class LXHelperUtils {

    private static final String CENT_POUR_CENT = "100.00";

    /**
     * Ajoute les ventilations du canevas.
     * 
     * @param session
     * @param transaction
     * @facture
     * @param canOpe
     * @throws Exception
     */
    public static void addCanevasVentilations(BISession session, BTransaction transaction, LXCanevasViewBean canevas,
            LXCanevasOperation canOpe) throws Exception {
        Iterator<LXCanevasVentilationViewBean> it = canevas.getVentilations().iterator();

        while (it.hasNext()) {
            LXCanevasVentilationViewBean canVent = it.next();

            LXHelperUtils.createCanevasVentilation(session, transaction, canevas, canOpe, canVent);
        }
    }

    /**
     * Ajoute les ventilations de la facture.
     * 
     * @param session
     * @param transaction
     * @facture
     * @param operation
     * @throws Exception
     */
    public static void addVentilations(BISession session, BTransaction transaction, LXFactureViewBean facture,
            LXOperation operation) throws Exception {
        Iterator<LXVentilationViewBean> it = facture.getVentilations().iterator();

        while (it.hasNext()) {
            LXVentilationViewBean ventilation = it.next();

            LXHelperUtils.createVentilation(session, transaction, facture, operation, ventilation);
        }
    }

    /**
     * Contrôle le modulo de la reference BVR.
     * 
     * @param referenceBVR
     * @throws Exception
     */
    public static void checkReferenveBVRModulo(BTransaction transaction, String referenceBVR) throws Exception {
        String tmp = referenceBVR.substring(0, referenceBVR.length() - 1);
        tmp = JadeStringUtil.removeChar(tmp, ' ');

        String moduloInserted = referenceBVR.substring(referenceBVR.length() - 1);
        String moduloCalculated = new Integer(JAUtil.getKeyNumberModulo10(tmp)).toString();

        if (!moduloCalculated.equals(moduloInserted)) {
            throw new Exception(transaction.getSession().getLabel("BVR_MODULO_INCORRECT"));
        }
    }

    /**
     * Créer l'opération pour la facture.
     * 
     * @param session
     * @param canevas
     * @param transaction
     * @param section
     * @return
     * @throws Exception
     */
    public static LXCanevasOperation createCanevasOperation(BISession session, LXCanevasViewBean canevas,
            BTransaction transaction, LXCanevasSection section) throws Exception {
        LXCanevasOperation canOpe = new LXCanevasOperation();
        canOpe.setSession((BSession) session);

        canOpe.setIdSectionCanevas(section.getIdSectionCanevas());
        canOpe.setCsTypeOperation(canevas.getCsTypeOperation());
        canOpe.setLibelle(canevas.getLibelle());
        canOpe.setIdOrganeExecution(canevas.getIdOrganeExecution());
        canOpe.setTauxEscompte(canevas.getTauxEscompte());
        if (!JadeStringUtil.isBlank(canOpe.getReferenceBVR()) && !"null".equals(canOpe.getReferenceBVR())) {
            canOpe.setReferenceBVR(canevas.getReferenceBVR());
        }
        canOpe.setReferenceExterne(canevas.getReferenceExterne());
        canOpe.setCsCodeTVA(canevas.getCsCodeTVA());
        if (!JadeStringUtil.isBlank(canevas.getMotif())) {
            canOpe.setMotif(canevas.getMotif());
        }

        canOpe.setIdAdressePaiement(canevas.getIdAdressePaiement());

        canOpe.setCsCodeIsoMonnaie(canevas.getCsCodeIsoMonnaie());

        if (canevas.isCsCodeIsoMonnaieEtranger()) {
            canOpe.setMontantMonnaie(canevas.getMontant());

            LXCanevasVentilationViewBean ventilation = canevas.getVentilations().get(0);
            canOpe.setMontant(ventilation.getMontant());
            canOpe.setCoursMonnaie(ventilation.getCoursMonnaie());
        } else {
            canOpe.setMontant(canevas.getMontant());
        }

        canOpe.add(transaction);

        if (canOpe.hasErrors()) {
            throw new Exception(canOpe.getErrors().toString());
        }

        if (canOpe.isNew()) {
            throw new Exception(transaction.getSession().getLabel("ADD_OPERATION_IMPOSSIBLE"));
        }
        return canOpe;
    }

    /**
     * Créée une nouvelle canevas section.
     * 
     * @param session
     * @param facture
     * @param transaction
     * @return
     * @throws Exception
     */
    public static LXCanevasSection createCanevasSection(BISession session, LXCanevasViewBean facture,
            BTransaction transaction) throws Exception {
        LXCanevasSection section = new LXCanevasSection();
        section.setSession((BSession) session);

        section.setIdSociete(facture.getIdSociete());
        section.setIdFournisseur(facture.getIdFournisseur());
        section.setIdExterne(facture.getIdExterne());

        section.add(transaction);

        if (section.hasErrors()) {
            throw new Exception(section.getErrors().toString());
        }

        if (section.isNew()) {
            throw new Exception(transaction.getSession().getLabel("ADD_SECTION_IMPOSSIBLE"));// TODO
            // SCO
            // :
            // Label
        }

        return section;
    }

    /**
     * Ajoute les paramètres nécessaires puis valide en base de données la ventilation.
     * 
     * @param session
     * @param transaction
     * @param canevas
     * @param canOpe
     * @param ventilation
     * @throws Exception
     */
    public static void createCanevasVentilation(BISession session, BTransaction transaction, LXCanevasViewBean canevas,
            LXCanevasOperation canOpe, LXCanevasVentilationViewBean canVent) throws Exception {
        canVent.setSession((BSession) session);
        canVent.setIdVentilationCanevas("");

        canVent.setIdOperationCanevas(canOpe.getIdOperationCanevas());

        canVent.add(transaction);

        if (canVent.hasErrors() || canVent.isNew()) {
            throw new Exception(((BSession) session).getLabel("VENTILATION_CREATION_PROBLEME"));
        }
    }

    /**
     * Créer l'opération pour la facture.
     * 
     * @param session
     * @param facture
     * @param transaction
     * @param section
     * @return
     * @throws Exception
     */
    public static LXOperation createOperation(BISession session, LXFactureViewBean facture, BTransaction transaction,
            LXSection section) throws Exception {
        LXOperation operation = new LXOperation();
        operation.setSession((BSession) session);

        operation.setIdSection(section.getIdSection());
        operation.setIdJournal(facture.getIdJournal());
        operation.setCsTypeOperation(facture.getCsTypeOperation());
        operation.setDateOperation(facture.getDateFacture());
        operation.setLibelle(facture.getLibelle());
        operation.setIdOrganeExecution(facture.getIdOrganeExecution());
        operation.setTauxEscompte(facture.getTauxEscompte());
        operation.setEstBloque(facture.getEstBloque());
        operation.setCsMotifBlocage(facture.getCsMotifBlocage());
        if (!JadeStringUtil.isBlank(facture.getReferenceBVR()) && !"null".equals(facture.getReferenceBVR())) {
            operation.setReferenceBVR(facture.getReferenceBVR());
        }
        operation.setDateEcheance(facture.getDateEcheance());
        operation.setReferenceExterne(facture.getReferenceExterne());
        operation.setCsCodeTVA(facture.getCsCodeTVA());
        if (!JadeStringUtil.isBlank(facture.getMotif())) {
            operation.setMotif(facture.getMotif());
        }

        operation.setIdAdressePaiement(facture.getIdAdressePaiement());

        operation.setCsCodeIsoMonnaie(facture.getCsCodeIsoMonnaie());

        if (facture.isCsCodeIsoMonnaieEtranger()) {
            operation.setMontantMonnaie(facture.getMontant());

            LXVentilationViewBean ventilation = facture.getVentilations().get(0);
            operation.setMontant(ventilation.getMontant());
            operation.setCoursMonnaie(ventilation.getCoursMonnaie());
        } else {
            operation.setMontant(facture.getMontant());
        }

        operation.add(transaction);

        if (operation.hasErrors()) {
            throw new Exception(operation.getErrors().toString());
        }

        if (operation.isNew()) {
            throw new Exception(transaction.getSession().getLabel("ADD_OPERATION_IMPOSSIBLE"));
        }
        return operation;
    }

    /**
     * Créée une nouvelle section.
     * 
     * @param session
     * @param facture
     * @param transaction
     * @return
     * @throws Exception
     */
    public static LXSection createSection(BISession session, LXFactureViewBean facture, BTransaction transaction,
            String typeSection) throws Exception {
        LXSection section = new LXSection();
        section.setSession((BSession) session);

        section.setIdJournal(facture.getIdJournal());
        section.setIdSociete(facture.getIdSociete());
        section.setIdFournisseur(facture.getIdFournisseur());
        section.setCsTypeSection(typeSection);
        section.setDateSection(facture.getDateFacture());
        section.setIdExterne(facture.getIdExterne());

        section.add(transaction);

        if (section.hasErrors()) {
            throw new Exception(section.getErrors().toString());
        }

        if (section.isNew()) {
            throw new Exception(transaction.getSession().getLabel("ADD_SECTION_IMPOSSIBLE"));
        }

        return section;
    }

    /**
     * Ajoute les paramètres nécessaires puis valide en base de données la ventilation.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @param operation
     * @param ventilation
     * @throws Exception
     */
    public static void createVentilation(BISession session, BTransaction transaction, LXFactureViewBean facture,
            LXOperation operation, LXVentilationViewBean ventilation) throws Exception {
        ventilation.setSession((BSession) session);
        ventilation.setIdVentilation("");

        ventilation.setIdOperation(operation.getIdOperation());

        ventilation.add(transaction);

        if (ventilation.hasErrors() || ventilation.isNew()) {
            throw new Exception(((BSession) session).getLabel("VENTILATION_CREATION_PROBLEME"));
        }
    }

    /**
     * Effacer le canevas opération du canevas.
     * 
     * @param session
     * @param transaction
     * @param idoperation
     * @throws Exception
     */
    public static void deleteCanevasOperation(BISession session, BTransaction transaction, String idOperationCanevas)
            throws Exception {
        LXCanevasOperationViewBean canOpe = LXHelperUtils.getCanevasOperation(session, transaction, idOperationCanevas);
        canOpe.delete(transaction);

        if (canOpe.hasErrors()) {
            throw new Exception(canOpe.getErrors().toString());
        }
    }

    /**
     * Effacer le canevas section du canevas. Si cette dernière n'est plus rattachée à aucun canevas opération.
     * 
     * @param session
     * @param transaction
     * @param idSectionCanevas
     * @throws Exception
     */
    public static void deleteCanevasSection(BISession session, BTransaction transaction, String idSectionCanevas)
            throws Exception {
        LXCanevasOperationManager manager = new LXCanevasOperationManager();
        manager.setSession((BSession) session);

        manager.setForIdSectionCanevas(idSectionCanevas);

        manager.find(transaction);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            LXCanevasSectionViewBean canSect = LXHelperUtils.getCanevasSection(session, transaction, idSectionCanevas);
            canSect.delete(transaction);

            if (canSect.hasErrors()) {
                throw new Exception(canSect.getErrors().toString());
            }
        }

    }

    /**
     * Supprime une ventilation.
     * 
     * @param session
     * @param transaction
     * @param canVent
     * @throws Exception
     */
    public static void deleteCanevasVentilation(BISession session, BTransaction transaction,
            LXCanevasVentilationViewBean canVent) throws Exception {
        canVent.setSession((BSession) session);
        canVent.retrieve(transaction);

        canVent.delete(transaction);
    }

    /**
     * Supprime les ventilations de la facture.
     * 
     * @param session
     * @param transaction
     * @param canevas
     * @throws Exception
     */
    public static void deleteCanevasVentilations(BISession session, BTransaction transaction, LXCanevasViewBean canevas)
            throws Exception {
        Iterator<LXCanevasVentilationViewBean> it = canevas.getVentilations().iterator();

        while (it.hasNext()) {
            LXCanevasVentilationViewBean canVent = it.next();
            LXHelperUtils.deleteCanevasVentilation(session, transaction, canVent);
        }
    }

    /**
     * Effacer l'opération de la facture.
     * 
     * @param session
     * @param transaction
     * @param idoperation
     * @throws Exception
     */
    public static void deleteOperation(BISession session, BTransaction transaction, String idOperation)
            throws Exception {
        LXOperationViewBean operation = LXHelperUtils.getOperation(session, transaction, idOperation);
        operation.delete(transaction);

        if (operation.hasErrors()) {
            throw new Exception(operation.getErrors().toString());
        }
    }

    /**
     * Effacer la section de la facture. Si cette dernière n'est plus rattachée à aucune opération.
     * 
     * @param session
     * @param transaction
     * @param idSection
     * @throws Exception
     */
    public static void deleteSection(BISession session, BTransaction transaction, String idSection) throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession((BSession) session);

        manager.setForIdSection(idSection);

        manager.find(transaction);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            LXSectionViewBean section = LXHelperUtils.getSection(session, transaction, idSection);
            section.delete(transaction);

            if (section.hasErrors()) {
                throw new Exception(section.getErrors().toString());
            }
        }

    }

    /**
     * Supprime une ventilation.
     * 
     * @param session
     * @param transaction
     * @param ventilation
     * @throws Exception
     */
    public static void deleteVentilation(BISession session, BTransaction transaction, LXVentilationViewBean ventilation)
            throws Exception {
        ventilation.setSession((BSession) session);
        ventilation.retrieve(transaction);

        ventilation.delete(transaction);
    }

    /**
     * Supprime les ventilations de la facture.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void deleteVentilations(BISession session, BTransaction transaction, LXFactureViewBean facture)
            throws Exception {
        Iterator<LXVentilationViewBean> it = facture.getVentilations().iterator();

        while (it.hasNext()) {
            LXVentilationViewBean ventilation = it.next();
            LXHelperUtils.deleteVentilation(session, transaction, ventilation);
        }
    }

    /**
     * Ajoute les informations nécessaires à la présentation uniquement d'un canevas.
     * 
     * @param session
     * @param facture
     * @throws Exception
     */
    public static void fillForLayoutOnly(BISession session, LXCanevasViewBean facture) throws Exception {
        LXFournisseurViewBean fournisseur = LXHelperUtils.getFournisseur(session, null, facture.getIdFournisseur());

        facture.setIdExterneFournisseur(fournisseur.getIdExterne());

        if (JadeStringUtil.isIntegerEmpty(facture.getIdAdressePaiement())) {
            TIAdressePaiementDataSource dataSource = LXTiersService.getAdresseFournisseurPaiementAsDataSource(
                    (BSession) session, null, fournisseur.getIdTiers());

            String tmpCCP = "" + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CCP);
            if (JadeStringUtil.isBlank(tmpCCP)
                    && !JadeStringUtil.isBlank(""
                            + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP))) {
                tmpCCP = "" + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP);
            }
            facture.setCcpFournisseur(tmpCCP);

            TIAdresseFormater formater = new TIAdresseFormater();
            if (LXOperation.CS_TYPE_FACTURE_CAISSE.equals(facture.getCsTypeOperation())) {
                TIAdresseDataSource dataSourceCourrier = LXTiersService.getAdresseFournisseurCourrierAsDataSource(
                        (BSession) session, null, fournisseur.getIdTiers());
                facture.setAdressePaiementFournisseur(formater.format(dataSourceCourrier));
            } else {
                facture.setAdressePaiementFournisseur(formater.format(dataSource));
            }

            facture.setAdresseBanque(LXUtils.formateBanque(dataSource));
            facture.setClearingBanque(dataSource.getData().get(
                    TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CLEARING));
        } else {
            facture.fillWithSpecificAdressePaiement(facture.getIdAdressePaiement());
        }
    }

    /**
     * Ajoute les informations nécessaires à la présentation uniquement.
     * 
     * @param session
     * @param facture
     * @throws Exception
     */
    public static void fillForLayoutOnly(BISession session, LXFactureViewBean facture) throws Exception {
        LXFournisseurViewBean fournisseur = LXHelperUtils.getFournisseur(session, null, facture.getIdFournisseur());

        facture.setIdExterneFournisseur(fournisseur.getIdExterne());

        TIAdressePaiementDataSource dataSource;
        if (!JadeStringUtil.isBlankOrZero(facture.getIdAdressePaiement())) {
            dataSource = LXTiersService.getAdresseFournisseurPaiementAsDataSource((BSession) session,
                    facture.getIdAdressePaiement());
        } else {
            dataSource = LXTiersService.getAdresseFournisseurPaiementAsDataSource((BSession) session, null,
                    fournisseur.getIdTiers());
        }

        if (dataSource == null) {
            throw new Exception("Erreur ! L'adresse de paiement n'existe pas !! " + facture.getIdAdressePaiement());
        }

        String tmpCCP = "" + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CCP);
        if (JadeStringUtil.isBlank(tmpCCP)
                && !JadeStringUtil.isBlank(""
                        + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP))) {
            tmpCCP = "" + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP);
        }
        facture.setCcpFournisseur(tmpCCP);

        TIAdresseFormater formater = new TIAdresseFormater();
        if (LXOperation.CS_TYPE_FACTURE_CAISSE.equals(facture.getCsTypeOperation())) {
            TIAdresseDataSource dataSourceCourrier = LXTiersService.getAdresseFournisseurCourrierAsDataSource(
                    (BSession) session, null, fournisseur.getIdTiers());
            facture.setAdressePaiementFournisseur(formater.format(dataSourceCourrier));
        } else {
            facture.setAdressePaiementFournisseur(formater.format(dataSource));
        }

        facture.setAdresseBanque(LXUtils.formateBanque(dataSource));
        facture.setClearingBanque(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CLEARING));
        facture.setCompte(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_COMPTE));

        if (dataSource != null) {
            facture.setNomBanque(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_D1)
                    + " \n" + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_COMPTE));
        }
    }

    /**
     * Mise à jour avec le canevas operation.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    public static void fillWithCanevasOperation(BISession session, LXCanevasViewBean canevas) throws Exception {
        LXCanevasOperationViewBean canOpe = LXHelperUtils.getCanevasOperation(session, null,
                canevas.getIdOperationCanevas());

        canevas.setIdSectionCanevas(canOpe.getIdSectionCanevas());
        canevas.setIdOrganeExecution(canOpe.getIdOrganeExecution());
        canevas.setLibelle(canOpe.getLibelle());
        canevas.setReferenceBVR(canOpe.getReferenceBVR());
        canevas.setReferenceExterne(canOpe.getReferenceExterne());
        canevas.setTauxEscompte(canOpe.getTauxEscompte());
        canevas.setCsTypeOperation(canOpe.getCsTypeOperation());
        canevas.setCsCodeTVA(canOpe.getCsCodeTVA());
        canevas.setIdAdressePaiement(canOpe.getIdAdressePaiement());

        canevas.setCsCodeIsoMonnaie(canOpe.getCsCodeIsoMonnaie());

        if (canevas.isCsCodeIsoMonnaieEtranger()) {
            canevas.setMontant(canOpe.getMontantMonnaie());
        } else {
            canevas.setMontant(canOpe.getMontant());
        }

        if (!JadeStringUtil.isBlank(canOpe.getMotif())) {
            canevas.setMotif(canOpe.getMotif());
        }
    }

    /**
     * Mise à jour avec le canevas section.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    public static void fillWithCanevasSection(BISession session, LXCanevasViewBean canevas) throws Exception {
        LXCanevasSectionViewBean section = LXHelperUtils
                .getCanevasSection(session, null, canevas.getIdSectionCanevas());

        canevas.setIdFournisseur(section.getIdFournisseur());
        canevas.setIdExterne(section.getIdExterne());
        canevas.setIdSociete(section.getIdSociete());
    }

    /**
     * Retrouve les canevas ventilations du canevas.
     * 
     * @param session
     * @param canevas
     * @throws Exception
     */
    public static void fillWithCanevasVentilations(BISession session, LXCanevasViewBean canevas) throws Exception {
        LXSocieteDebitriceViewBean societe = LXHelperUtils.getSociete(session, null, canevas.getIdSociete());

        LXCanevasVentilationListViewBean manager = new LXCanevasVentilationListViewBean();
        manager.setSession((BSession) session);

        manager.setForIdOperationCanevas(canevas.getIdOperationCanevas());

        manager.find(BManager.SIZE_NOLIMIT);

        ArrayList<LXCanevasVentilationViewBean> attachedVentilations = new ArrayList<LXCanevasVentilationViewBean>();
        for (int i = 0; i < manager.size(); i++) {
            LXCanevasVentilationViewBean canVent = (LXCanevasVentilationViewBean) manager.get(i);
            canVent.setIdExterneCompte(LXHelperUtils.getIdExterneCompte(session, societe.getIdMandat(),
                    canVent.getIdCompte(), JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY)));

            attachedVentilations.add(canVent);

        }

        if (attachedVentilations.isEmpty()) {
            throw new Exception(((BSession) session).getLabel("AUCUNE_VENTILATION"));
        }

        canevas.setShowRows(manager.size());

        canevas.setVentilations(attachedVentilations);
    }

    /**
     * Mise à jour avec la section.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    public static void fillWithOperation(BISession session, LXFactureViewBean facture) throws Exception {
        LXOperationViewBean operation = LXHelperUtils.getOperation(session, null, facture.getIdOperation());

        facture.setIdSection(operation.getIdSection());
        facture.setIdJournal(operation.getIdJournal());
        facture.setIdOrdreGroupe(operation.getIdOrdreGroupe());
        facture.setIdOperationSrc(operation.getIdOperationSrc());
        facture.setIdOperationLiee(operation.getIdOperationLiee());
        facture.setNumeroTransaction(operation.getNumeroTransaction());
        facture.setCsEtat(operation.getCsEtatOperation());
        facture.setCsMotifBlocage(operation.getCsMotifBlocage());
        facture.setDateEcheance(operation.getDateEcheance());
        facture.setDateFacture(operation.getDateOperation());
        facture.setEstBloque(operation.getEstBloque());
        facture.setIdOrganeExecution(operation.getIdOrganeExecution());
        facture.setLibelle(operation.getLibelle());
        facture.setReferenceBVR(operation.getReferenceBVR());
        facture.setReferenceExterne(operation.getReferenceExterne());
        facture.setTauxEscompte(operation.getTauxEscompte());
        facture.setCsTypeOperation(operation.getCsTypeOperation());
        facture.setCsCodeTVA(operation.getCsCodeTVA());
        facture.setIdAdressePaiement(operation.getIdAdressePaiement());

        facture.setCsCodeIsoMonnaie(operation.getCsCodeIsoMonnaie());

        if (facture.isCsCodeIsoMonnaieEtranger()) {
            facture.setMontant(operation.getMontantMonnaie());
        } else {
            facture.setMontant(operation.getMontant());
        }

        if (!JadeStringUtil.isBlank(operation.getMotif())) {
            facture.setMotif(operation.getMotif());
        }
    }

    /**
     * Mise à jour avec la section.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    public static void fillWithSection(BISession session, LXFactureViewBean facture) throws Exception {
        LXSectionViewBean section = LXHelperUtils.getSection(session, null, facture.getIdSection());

        facture.setIdFournisseur(section.getIdFournisseur());
        facture.setIdExterne(section.getIdExterne());
        facture.setIdSociete(section.getIdSociete());
    }

    /**
     * Retrouve les ventilations de la facture.
     * 
     * @param session
     * @param facture
     * @throws Exception
     */
    public static void fillWithVentilations(BISession session, LXFactureViewBean facture) throws Exception {
        LXSocieteDebitriceViewBean societe = LXHelperUtils.getSociete(session, null, facture.getIdSociete());

        LXVentilationListViewBean manager = new LXVentilationListViewBean();
        manager.setSession((BSession) session);

        manager.setForIdOperation(facture.getIdOperation());

        manager.find(BManager.SIZE_NOLIMIT);

        ArrayList<LXVentilationViewBean> attachedVentilations = new ArrayList<LXVentilationViewBean>();
        for (int i = 0; i < manager.size(); i++) {
            LXVentilationViewBean ventilation = (LXVentilationViewBean) manager.get(i);
            ventilation.setIdExterneCompte(LXHelperUtils.getIdExterneCompte(session, societe.getIdMandat(),
                    ventilation.getIdCompte(), facture.getDateFacture()));

            attachedVentilations.add(ventilation);

        }

        if (attachedVentilations.isEmpty()) {
            throw new Exception(((BSession) session).getLabel("AUCUNE_VENTILATION"));
        }

        facture.setShowRows(manager.size());

        if (!facture.isJournalEditable()) {
            facture.setMaxRows(manager.size());
        }

        facture.setVentilations(attachedVentilations);
    }

    /**
     * Retrouve un canevas opération suivant l'id operation canevas en paramètre.
     * 
     * @param session
     * @param transaction
     * @param idOperationCanevas
     * @return
     * @throws Exception
     */
    public static LXCanevasOperationViewBean getCanevasOperation(BISession session, BTransaction transaction,
            String idOperationCanevas) throws Exception {

        LXCanevasOperationViewBean canOpe = new LXCanevasOperationViewBean();

        canOpe.setSession((BSession) session);
        canOpe.setIdOperationCanevas(idOperationCanevas);
        canOpe.retrieve(transaction);

        if (canOpe.isNew() || canOpe.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("OPERATION_NON_RESOLUE")); // TODO
            // SCO
            // :
            // Label
        }

        return canOpe;
    }

    /**
     * Retrouve un canevas section suivant l'id section canevas en paramètre.
     * 
     * @param session
     * @param transaction
     * @param idSection
     * @return
     * @throws Exception
     */
    public static LXCanevasSectionViewBean getCanevasSection(BISession session, BTransaction transaction,
            String idSectionCanevas) throws Exception {

        LXCanevasSectionViewBean canSect = new LXCanevasSectionViewBean();

        canSect.setSession((BSession) session);
        canSect.setIdSectionCanevas(idSectionCanevas);
        canSect.retrieve(transaction);

        if (canSect.isNew() || canSect.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("SECTION_NON_RESOLUE")); // TODO
            // SCO
            // :
            // Label
        }

        return canSect;
    }

    /**
     * Return la section du canevas à laquelle s'applique le canevas. Si elle n'existe pas encore, la méthode la créée.
     * 
     * @param session
     * @param canevas
     * @param transaction
     * @return
     * @throws Exception
     */
    public static LXCanevasSection getCanevasSection(BISession session, LXCanevasViewBean canevas,
            BTransaction transaction) throws Exception {
        // On recherche tj la section avec l'idexterne afin d'éviter les
        // problèmes lors de l'utilisation de l'autocomplete.
        // Exemple le client sélectionne un numéro grâce à l'autocomplete (=>
        // sélection idsection et idexterne) puis modifit le numéro (idexterne)
        // affiché.

        LXCanevasSectionManager manager = new LXCanevasSectionManager();
        manager.setSession((BSession) session);

        manager.setForIdSociete(canevas.getIdSociete());
        manager.setForIdFournisseur(canevas.getIdFournisseur());
        manager.setForIdExterne(canevas.getIdExterne());

        manager.find(transaction);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            return LXHelperUtils.createCanevasSection(session, canevas, transaction);
        } else {
            return (LXCanevasSection) manager.getFirstEntity();
        }
    }

    /**
     * Retrouve le fournisseur pour l'id passée en paramètre.
     * 
     * @param session
     * @param transaction
     * @param idFournisseur
     * @return
     * @throws Exception
     */
    public static LXFournisseurViewBean getFournisseur(BISession session, BTransaction transaction, String idFournisseur)
            throws Exception {

        LXFournisseurViewBean fournisseur = new LXFournisseurViewBean();

        fournisseur.setSession((BSession) session);
        fournisseur.setIdFournisseur(idFournisseur);
        fournisseur.retrieve(transaction);

        if (fournisseur.isNew() || fournisseur.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("FOURNISSEUR_NON_RESOLUE"));
        }

        return fournisseur;
    }

    /**
     * Return l'id externe d'un compte.
     * 
     * @param session
     * @param idMandat
     * @param idCompte
     * @param forDate
     * @return
     * @throws Exception
     */
    public static String getIdExterneCompte(BISession session, String idMandat, String idCompte, String forDate)
            throws Exception {
        CGExerciceComptable exercice = LXHeliosService.getExerciceComptable((BSession) session, idMandat, forDate);

        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession((BSession) session);

        manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        manager.setForIdCompte(idCompte);

        manager.find();

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(((BSession) session).getLabel("AUCUN_COMPTE_RESOLU"));
        }

        return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdExterne();
    }

    /**
     * Return le journal en cours.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @return
     * @throws Exception
     */
    public static LXJournal getJournal(BISession session, BTransaction transaction, LXFactureViewBean facture)
            throws Exception {
        LXJournal journal = new LXJournal();
        journal.setSession((BSession) session);

        journal.setIdJournal(facture.getIdJournal());

        journal.retrieve(transaction);

        if (journal.isNew() || journal.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("VALIDATE_JOURNAL_INCONNU"));
        }

        return journal;
    }

    /**
     * Retrouve une opération suivant l'id operation en paramètre.
     * 
     * @param session
     * @param transaction
     * @param idOperation
     * @return
     * @throws Exception
     */
    public static LXOperationViewBean getOperation(BISession session, BTransaction transaction, String idOperation)
            throws Exception {

        LXOperationViewBean operation = new LXOperationViewBean();

        operation.setSession((BSession) session);
        operation.setIdOperation(idOperation);
        operation.retrieve(transaction);

        if (operation.isNew() || operation.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("OPERATION_NON_RESOLUE"));
        }

        return operation;
    }

    /**
     * Return le journal en cours.
     * 
     * @param session
     * @param transaction
     * @param paiement
     * @return
     * @throws Exception
     */
    public static LXJournal getOrdreGroupe(BISession session, BTransaction transaction, LXPaiementViewBean paiement)
            throws Exception {
        LXJournal journal = new LXJournal();
        journal.setSession((BSession) session);

        journal.setIdJournal(paiement.getIdJournal());

        journal.retrieve(transaction);

        if (journal.isNew() || journal.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("VALIDATE_JOURNAL_INCONNU"));
        }

        return journal;
    }

    // TODO SCO : Changer les libellés
    /**
     * Retrouve une section suivant l'id section en paramètre.
     * 
     * @param session
     * @param transaction
     * @param idSection
     * @return
     * @throws Exception
     */
    public static LXSectionViewBean getSection(BISession session, BTransaction transaction, String idSection)
            throws Exception {

        LXSectionViewBean section = new LXSectionViewBean();

        section.setSession((BSession) session);
        section.setIdSection(idSection);
        section.retrieve(transaction);

        if (section.isNew() || section.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("SECTION_NON_RESOLUE"));
        }

        return section;
    }

    /**
     * Return la section à laquelle s'applique la facture. Si elle n'existe pas encore, la méthode la créée.
     * 
     * @param session
     * @param facture
     * @param transaction
     * @return
     * @throws Exception
     */
    public static LXSection getSection(BISession session, LXFactureViewBean facture, BTransaction transaction,
            String typeSection) throws Exception {
        // On recherche tj la section avec l'idexterne afin d'éviter les
        // problèmes lors de l'utilisation de l'autocomplete.
        // Exemple le client sélectionne un numéro grâce à l'autocomplete (=>
        // sélection idsection et idexterne) puis modifit le numéro (idexterne)
        // affiché.

        LXSectionManager manager = new LXSectionManager();
        manager.setSession((BSession) session);

        manager.setForIdSociete(facture.getIdSociete());
        manager.setForIdFournisseur(facture.getIdFournisseur());
        manager.setForIdExterne(facture.getIdExterne());
        manager.setForCsTypeSection(typeSection);

        manager.find(transaction);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            return LXHelperUtils.createSection(session, facture, transaction, typeSection);
        } else {
            return (LXSection) manager.getFirstEntity();
        }
    }

    /**
     * Récupère la société débitrice.
     * 
     * @param session
     * @param transaction
     * @param idSociete
     * @return
     * @throws Exception
     */
    public static LXSocieteDebitriceViewBean getSociete(BISession session, BTransaction transaction, String idSociete)
            throws Exception {

        LXSocieteDebitriceViewBean societe = new LXSocieteDebitriceViewBean();
        societe.setSession((BSession) session);
        societe.setIdSociete(idSociete);
        societe.retrieve(transaction);

        if (societe.isNew() || societe.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("SOCIETE_DEBITRICE_NOT_FOUND"));
        }

        return societe;
    }

    /**
     * Permet de copier les ventilations du canevas en ventilation de facture
     * 
     * @param listCanevasVentilation
     * @return
     * @throws Exception
     */
    public static ArrayList<LXVentilationViewBean> getVentilationFactureFromCanevas(String montantFacture,
            ArrayList<LXCanevasVentilationViewBean> listCanevasVentilation) throws Exception {
        ArrayList<LXVentilationViewBean> listVentilations = new ArrayList<LXVentilationViewBean>();

        if (listCanevasVentilation == null) {
            return listVentilations;
        }

        Iterator<LXCanevasVentilationViewBean> it = listCanevasVentilation.iterator();

        while (it.hasNext()) {
            LXCanevasVentilationViewBean canevasVentilation = it.next();

            LXVentilationViewBean factureVentilation = new LXVentilationViewBean();
            factureVentilation.setIdCompte(canevasVentilation.getIdCompte());
            factureVentilation.setIdCentreCharge(canevasVentilation.getIdCentreCharge());
            factureVentilation.setMontant(canevasVentilation.getMontant());
            factureVentilation.setLibelle(canevasVentilation.getLibelle());
            factureVentilation.setMontantMonnaie(canevasVentilation.getMontantMonnaie());
            factureVentilation.setCoursMonnaie(canevasVentilation.getCoursMonnaie());
            factureVentilation.setCodeDebitCredit(canevasVentilation.getCodeDebitCredit());
            factureVentilation.setIdExterneCompte(canevasVentilation.getIdExterneCompte());

            if (!JadeStringUtil.isDecimalEmpty(canevasVentilation.getPourcentage())) {
                BigDecimal calcul = new BigDecimal(canevasVentilation.getPourcentage());
                calcul = calcul.divide(new BigDecimal(LXHelperUtils.CENT_POUR_CENT), 2, 6);
                calcul = calcul.multiply(new BigDecimal(montantFacture));

                factureVentilation.setMontant(calcul.toString());
            } else if (!JadeStringUtil.isEmpty(montantFacture) && !montantFacture.equals(factureVentilation)) {

                factureVentilation.setMontant(montantFacture);

            }

            listVentilations.add(factureVentilation);
        }

        return listVentilations;
    }

    /**
     * La ventilation doit contenir plus d'une ligne. Minimum un débit et un crédit.
     * 
     * @param session
     * @param facture
     * @throws Exception
     */
    public static void testMinimumDebitCredit(BISession session, LXFactureViewBean facture) throws Exception {
        if ((facture.getVentilations() == null) || (facture.getVentilations().size() <= 1)) {
            throw new Exception(((BSession) session).getLabel("VENTILATION_DEBIT_CREDIT_MINIMUM"));
        }
    }

    /**
     * Le total du débit de la ventilation doit être égal au total des crédits. <br/>
     * Prévient l'utilisation du "Control-Enter" de l'utilisateur.
     * 
     * @param session
     * @param facture
     * @throws Exception
     */
    public static void testTotalDebitCredit(BISession session, LXFactureViewBean facture) throws Exception {
        FWCurrency totalDebit = new FWCurrency();
        FWCurrency totalCredit = new FWCurrency();

        Iterator<LXVentilationViewBean> it = facture.getVentilations().iterator();
        while (it.hasNext()) {
            LXVentilationViewBean ventilation = it.next();

            if (ventilation.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                    || ventilation.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
                totalDebit.add(ventilation.getMontant());
            } else {
                totalCredit.add(ventilation.getMontant());
            }
        }

        if (totalCredit.compareTo(totalDebit) != 0) {
            throw new Exception(((BSession) session).getLabel("VENTILATION_DEBIT_CREDIT_DIFF"));
        }

        if (!facture.isCsCodeIsoMonnaieEtranger()) {
            FWCurrency montantFacture = new FWCurrency(facture.getMontant());
            if ((totalCredit.compareTo(montantFacture) != 0) || (totalDebit.compareTo(montantFacture) != 0)) {
                throw new Exception(((BSession) session).getLabel("VENTILATION_TOTAL_DEBIT_CREDIT_DIFF"));
            }
        }
    }

    /**
     * Mise à jour de l'opération de la facture.
     * 
     * @param session
     * @param transaction
     * @param canevas
     * @return
     * @throws Exception
     */
    public static LXCanevasOperationViewBean updateCanevasOperation(BISession session, BTransaction transaction,
            LXCanevasViewBean canevas) throws Exception {
        LXCanevasOperationViewBean operation = LXHelperUtils.getCanevasOperation(session, transaction,
                canevas.getIdOperationCanevas());

        operation.setLibelle(canevas.getLibelle());
        operation.setIdOrganeExecution(canevas.getIdOrganeExecution());
        operation.setTauxEscompte(canevas.getTauxEscompte());
        if (!JadeStringUtil.isBlank(canevas.getReferenceBVR()) && !"null".equals(canevas.getReferenceBVR())) {
            operation.setReferenceBVR(canevas.getReferenceBVR());
        }
        operation.setReferenceExterne(canevas.getReferenceExterne());
        operation.setCsCodeTVA(canevas.getCsCodeTVA());
        operation.setIdAdressePaiement(canevas.getIdAdressePaiement());

        operation.setCsCodeIsoMonnaie(canevas.getCsCodeIsoMonnaie());

        if (canevas.isCsCodeIsoMonnaieEtranger()) {
            operation.setMontantMonnaie(canevas.getMontant());

            LXCanevasVentilationViewBean ventilation = canevas.getVentilations().get(0);
            operation.setMontant(ventilation.getMontant());
            operation.setCoursMonnaie(ventilation.getCoursMonnaie());
        } else {
            operation.setMontant(canevas.getMontant());
        }

        if (!JadeStringUtil.isBlank(canevas.getMotif())) {
            operation.setMotif(canevas.getMotif());
        }

        operation.update(transaction);

        if (operation.hasErrors()) {
            throw new Exception(operation.getErrors().toString());
        }

        if (operation.isNew()) {
            throw new Exception(transaction.getSession().getLabel("UPDATE_IMPOSSIBLE")); // TODO
            // SCO
            // :
            // LABEL
        }

        return operation;
    }

    /**
     * Mise à jour de la section d'un canevas.
     * 
     * @param session
     * @param transaction
     * @param canevas
     * @throws Exception
     */
    public static void updateCanevasSection(BISession session, BTransaction transaction, LXCanevasViewBean canevas)
            throws Exception {
        LXCanevasSectionViewBean section = LXHelperUtils.getCanevasSection(session, transaction,
                canevas.getIdSectionCanevas());

        if ((!section.getIdExterne().equals(canevas.getIdExterne()))
                || (!section.getIdFournisseur().equals(canevas.getIdFournisseur()))) {
            LXCanevasOperationManager manager = new LXCanevasOperationManager();
            manager.setSession((BSession) session);

            manager.setForIdSectionCanevas(canevas.getIdSectionCanevas());

            manager.find(transaction);

            if (manager.hasErrors()) {
                throw new Exception(manager.getErrors().toString());
            }

            if (manager.isEmpty() || (manager.size() > 1)) {
                throw new Exception(transaction.getSession().getLabel("NUMERO_MULTIPLE_FACTURE")); // TODO
                // SCO
                // :
                // Label
            }

            section.setIdExterne(canevas.getIdExterne());
            section.setIdFournisseur(canevas.getIdFournisseur());
            section.update(transaction);
        }

    }

    /**
     * Mise à jour d'une modification. Uniquement si une valeur a été modifiée.
     * 
     * @param session
     * @param transaction
     * @param ventilation
     * @throws Exception
     */
    public static void updateCanevasVentilation(BISession session, BTransaction transaction,
            LXCanevasVentilationViewBean ventilation) throws Exception {
        LXCanevasVentilationViewBean newVentilationValue = new LXCanevasVentilationViewBean();
        newVentilationValue.setIdCompte(ventilation.getIdCompte());
        newVentilationValue.setIdCentreCharge(ventilation.getIdCentreCharge());
        newVentilationValue.setLibelle(ventilation.getLibelle());
        newVentilationValue.setCodeDebitCredit(ventilation.getCodeDebitCredit());

        newVentilationValue.setMontant(ventilation.getMontant());
        newVentilationValue.setMontantMonnaie(ventilation.getMontantMonnaie());
        newVentilationValue.setCoursMonnaie(ventilation.getCoursMonnaie());
        newVentilationValue.setPourcentage(ventilation.getPourcentage());

        ventilation.setSession((BSession) session);
        ventilation.retrieve(transaction);

        if (ventilation.hasErrors() || ventilation.isNew()) {
            throw new Exception(((BSession) session).getLabel("VENTILATION_NON_RESOLUE"));
        }

        if (!ventilation.isEqualsTo(newVentilationValue)) {
            ventilation.setIdCompte(newVentilationValue.getIdCompte());
            ventilation.setIdCentreCharge(newVentilationValue.getIdCentreCharge());
            ventilation.setLibelle(newVentilationValue.getLibelle());
            ventilation.setCodeDebitCredit(newVentilationValue.getCodeDebitCredit());

            ventilation.setMontant(newVentilationValue.getMontant());
            ventilation.setMontantMonnaie(newVentilationValue.getMontantMonnaie());
            ventilation.setCoursMonnaie(newVentilationValue.getCoursMonnaie());
            ventilation.setPourcentage(newVentilationValue.getPourcentage());

            ventilation.update(transaction);
        }
    }

    /**
     * Mise à jour des ventilations d'un canevas .
     * 
     * @param session
     * @param transaction
     * @param canevas
     * @param operation
     * @throws Exception
     */
    public static void updateCanevasVentilations(BISession session, BTransaction transaction,
            LXCanevasViewBean canevas, LXCanevasOperationViewBean operation) throws Exception {
        Iterator<LXCanevasVentilationViewBean> it = canevas.getVentilations().iterator();

        while (it.hasNext()) {
            LXCanevasVentilationViewBean ventilation = it.next();

            if (JadeStringUtil.isIntegerEmpty(ventilation.getIdVentilationCanevas())) {
                LXHelperUtils.createCanevasVentilation(session, transaction, canevas, operation, ventilation);
            } else {
                if (!JadeStringUtil.isDecimalEmpty(ventilation.getMontant())
                        || !JadeStringUtil.isDecimalEmpty(ventilation.getMontantMonnaie())
                        || !JadeStringUtil.isDecimalEmpty(ventilation.getPourcentage())) {
                    LXHelperUtils.updateCanevasVentilation(session, transaction, ventilation);
                } else {
                    LXHelperUtils.deleteCanevasVentilation(session, transaction, ventilation);
                }
            }
        }
    }

    /**
     * Mise à jour de l'opération de la facture.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @return
     * @throws Exception
     */
    public static LXOperationViewBean updateOperation(BISession session, BTransaction transaction,
            LXFactureViewBean facture) throws Exception {
        LXOperationViewBean operation = LXHelperUtils.getOperation(session, transaction, facture.getIdOperation());

        operation.setDateOperation(facture.getDateFacture());
        operation.setLibelle(facture.getLibelle());
        operation.setIdOrganeExecution(facture.getIdOrganeExecution());
        operation.setTauxEscompte(facture.getTauxEscompte());
        operation.setEstBloque(facture.getEstBloque());
        operation.setCsMotifBlocage(facture.getCsMotifBlocage());
        if (!JadeStringUtil.isBlank(facture.getReferenceBVR()) && !"null".equals(facture.getReferenceBVR())) {
            operation.setReferenceBVR(facture.getReferenceBVR());
        }
        operation.setDateEcheance(facture.getDateEcheance());
        operation.setReferenceExterne(facture.getReferenceExterne());
        operation.setCsCodeTVA(facture.getCsCodeTVA());
        operation.setIdAdressePaiement(facture.getIdAdressePaiement());
        operation.setCsEtatOperation(facture.getCsEtat());

        operation.setCsCodeIsoMonnaie(facture.getCsCodeIsoMonnaie());

        if (facture.isCsCodeIsoMonnaieEtranger()) {
            operation.setMontantMonnaie(facture.getMontant());

            LXVentilationViewBean ventilation = facture.getVentilations().get(0);
            operation.setMontant(ventilation.getMontant());
            operation.setCoursMonnaie(ventilation.getCoursMonnaie());
        } else {
            operation.setMontant(facture.getMontant());
        }

        if (!JadeStringUtil.isBlank(facture.getMotif())) {
            operation.setMotif(facture.getMotif());
        }

        operation.update(transaction);

        if (operation.hasErrors()) {
            throw new Exception(operation.getErrors().toString());
        }

        if (operation.isNew()) {
            throw new Exception(transaction.getSession().getLabel("UPDATE_IMPOSSIBLE"));
        }

        return operation;
    }

    /**
     * Mise à jour de la section d'une facture.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void updateSection(BISession session, BTransaction transaction, LXFactureViewBean facture)
            throws Exception {
        LXSectionViewBean section = LXHelperUtils.getSection(session, transaction, facture.getIdSection());

        if ((!section.getIdExterne().equals(facture.getIdExterne()))
                || (!section.getIdFournisseur().equals(facture.getIdFournisseur()))) {
            LXOperationManager manager = new LXOperationManager();
            manager.setSession((BSession) session);

            manager.setForIdSection(facture.getIdSection());

            manager.find(transaction);

            if (manager.hasErrors()) {
                throw new Exception(manager.getErrors().toString());
            }

            if (manager.isEmpty() || (manager.size() > 1)) {
                throw new Exception(transaction.getSession().getLabel("NUMERO_MULTIPLE_FACTURE"));
            }

            section.setIdExterne(facture.getIdExterne());
            section.setIdFournisseur(facture.getIdFournisseur());
            section.update(transaction);
        }

    }

    /**
     * Mise à jour d'une modification. Uniquement si une valeur a été modifiée.
     * 
     * @param session
     * @param transaction
     * @param ventilation
     * @throws Exception
     */
    public static void updateVentilation(BISession session, BTransaction transaction, LXVentilationViewBean ventilation)
            throws Exception {
        LXVentilationViewBean newVentilationValue = new LXVentilationViewBean();
        newVentilationValue.setIdCompte(ventilation.getIdCompte());
        newVentilationValue.setIdCentreCharge(ventilation.getIdCentreCharge());
        newVentilationValue.setLibelle(ventilation.getLibelle());
        newVentilationValue.setCodeDebitCredit(ventilation.getCodeDebitCredit());

        newVentilationValue.setMontant(ventilation.getMontant());
        newVentilationValue.setMontantMonnaie(ventilation.getMontantMonnaie());
        newVentilationValue.setCoursMonnaie(ventilation.getCoursMonnaie());

        ventilation.setSession((BSession) session);
        ventilation.retrieve(transaction);

        if (ventilation.hasErrors() || ventilation.isNew()) {
            throw new Exception(((BSession) session).getLabel("VENTILATION_NON_RESOLUE"));
        }

        if (!ventilation.isEqualsTo(newVentilationValue)) {
            ventilation.setIdCompte(newVentilationValue.getIdCompte());
            ventilation.setIdCentreCharge(newVentilationValue.getIdCentreCharge());
            ventilation.setLibelle(newVentilationValue.getLibelle());
            ventilation.setCodeDebitCredit(newVentilationValue.getCodeDebitCredit());

            ventilation.setMontant(newVentilationValue.getMontant());
            ventilation.setMontantMonnaie(newVentilationValue.getMontantMonnaie());
            ventilation.setCoursMonnaie(newVentilationValue.getCoursMonnaie());

            ventilation.update(transaction);
        }
    }

    /**
     * Mise à jour des ventilations de la facture.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @param operation
     * @throws Exception
     */
    public static void updateVentilations(BISession session, BTransaction transaction, LXFactureViewBean facture,
            LXOperationViewBean operation) throws Exception {
        Iterator<LXVentilationViewBean> it = facture.getVentilations().iterator();

        while (it.hasNext()) {
            LXVentilationViewBean ventilation = it.next();

            if (JadeStringUtil.isIntegerEmpty(ventilation.getIdVentilation())) {
                LXHelperUtils.createVentilation(session, transaction, facture, operation, ventilation);
            } else {
                if (!JadeStringUtil.isDecimalEmpty(ventilation.getMontant())
                        || !JadeStringUtil.isDecimalEmpty(ventilation.getMontantMonnaie())) {
                    LXHelperUtils.updateVentilation(session, transaction, ventilation);
                } else {
                    LXHelperUtils.deleteVentilation(session, transaction, ventilation);
                }
            }
        }
    }

    /**
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void validateCommonPart(BISession session, BTransaction transaction, LXFactureViewBean facture)
            throws Exception {
        // TODO SCO : Libelle generique

        if (JadeStringUtil.isBlank(facture.getIdJournal()) || JadeStringUtil.isIntegerEmpty(facture.getIdJournal())) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_JOURNAL_INCONNU"));
        }

        if (!facture.getForceEstBloqueUpdate().booleanValue()
                && !LXHelperUtils.getJournal(session, transaction, facture).isOuvert()) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_JOURNAL_ETAT_PAS_OUVERT"));
        }

        if (JadeStringUtil.isBlank(facture.getIdSociete()) || JadeStringUtil.isIntegerEmpty(facture.getIdSociete())) {
            throw new Exception(((BSession) session).getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }

        if (JadeStringUtil.isBlank(facture.getIdFournisseur())
                || JadeStringUtil.isIntegerEmpty(facture.getIdFournisseur())) {
            throw new Exception(((BSession) session).getLabel("VAL_IDENTIFIANT_FOURNISSEUR"));
        }

        LXApplication application = (LXApplication) GlobazServer.getCurrentSystem().getApplication(
                LXApplication.DEFAULT_APPLICATION_LYNX);
        application.getNumeroFactureFormatter().checkIdExterne(session, facture.getIdExterne());

        if (JadeStringUtil.isBlank(facture.getDateFacture())) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_DATE_FACTURE"));
        }

        if (JadeStringUtil.isBlank(facture.getLibelle())) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_LIBELLE"));
        }

        if (JadeStringUtil.isBlank(facture.getMontant())) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_MONTANT"));
        }

        LXHelperUtils.testMinimumDebitCredit(session, facture);
        LXHelperUtils.testTotalDebitCredit(session, facture);
    }

    /**
     * Validation de la présence de l'id operation
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void validateIdOperation(BISession session, BTransaction transaction, String idOperation)
            throws Exception {

        if (JadeStringUtil.isIntegerEmpty(idOperation)) {
            throw new Exception(transaction.getSession().getLabel("VAL_IDENTIFIANT_OPERATION"));
        }
    }

    /**
     * Validation de la présence de l'id operation canevas
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void validateIdOperationCanevas(BISession session, BTransaction transaction, String idOperationCanevas)
            throws Exception {

        if (JadeStringUtil.isIntegerEmpty(idOperationCanevas)) {
            throw new Exception(transaction.getSession().getLabel("VAL_IDENTIFIANT_OPERATION"));
        }
    }

    /**
     * Constructeur
     */
    protected LXHelperUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
