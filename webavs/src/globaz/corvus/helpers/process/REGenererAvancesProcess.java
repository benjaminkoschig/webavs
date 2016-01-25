package globaz.corvus.helpers.process;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.avances.REListeExcelAvancesProcess;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.db.avances.REAvanceJointTiers;
import globaz.corvus.db.avances.REAvanceManager;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.excel.REListeExcelAvances;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import ch.globaz.common.properties.CommonProperties;

public class REGenererAvancesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            setSendCompletionMail(false);

            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));

            REListeExcelAvancesProcess process = new REListeExcelAvancesProcess(new REListeExcelAvances(getSession(),
                    "", getListeAvanceUnique(), getListeAvanceMensuel(),
                    CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue()), getEMailAddress());

            process.run();

        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
        return true;

    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return "";
    }

    /**
     * Traite et retourne les avances mensuels sous forme de liste
     * 
     * @return
     */
    private ArrayList<REAvanceJointTiers> getListeAvanceMensuel() {
        // liste de retour
        ArrayList<REAvanceJointTiers> listeAvanceMensuel = new ArrayList<REAvanceJointTiers>();
        // Recherche des avances mensuel
        REAvanceManager mgr = new REAvanceManager();
        mgr.setSession(getSession());
        mgr.setForCsEtatAcomptesDifferentDe(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
        mgr.setForDateDebutAcompteMensuelNotZero(true);

        try {
            mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            JadeThread.logError(toString(), "Error during retrieving avance unique: " + e.getStackTrace());
        }

        // Iteration sur les résultats
        for (Iterator<REAvance> iter = mgr.iterator(); iter.hasNext();) {
            // Avance
            REAvance avance = iter.next();
            // Tiers
            PRTiersWrapper tiers = null;
            try {
                tiers = PRTiersHelper.getTiersParId(getSession(), avance.getIdTiersBeneficiaire());
            } catch (Exception e) {
                JadeThread.logError(toString(),
                        "Error happened during retrieving tiers for avance: " + e.getStackTrace());
            }
            // Instanciation du pojo enscapsulant
            REAvanceJointTiers avanceWithTiers = new REAvanceJointTiers();
            avanceWithTiers.setAvance(avance);
            avanceWithTiers.setDateNaissance(tiers.getDateNaissance());
            avanceWithTiers.setNom(tiers.getNom());
            avanceWithTiers.setPrenom(tiers.getPrenom());
            avanceWithTiers.setNss(tiers.getNSS());
            // on set le type de demande en fonction du domaine
            if (avance.getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
                avanceWithTiers.setTypeDemande(getSession().getLabel("EXCEL_AVANCES_TYPE_DEMANDE_PC"));
            } else if (avance.getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_RFM)) {
                avanceWithTiers.setTypeDemande(getSession().getLabel("EXCEL_AVANCES_TYPE_DEMANDE_RFM"));
            } else {
                avanceWithTiers.setTypeDemande(getTypeDemandeRente(avance.getIdTiersBeneficiaire()));
            }
            // Ajout dans la liste
            listeAvanceMensuel.add(avanceWithTiers);
        }
        return listeAvanceMensuel;
    }

    /**
     * Traite et retourne les avances uniques sous forme de liste
     * 
     * @return
     */
    private ArrayList<REAvanceJointTiers> getListeAvanceUnique() {
        // liste de retour
        ArrayList<REAvanceJointTiers> listeAvanceUnique = new ArrayList<REAvanceJointTiers>();
        // Recherche des avances uniques
        REAvanceManager mgr = new REAvanceManager();
        mgr.setSession(getSession());
        // mgr.setForCsEtat1erAcomptesDifferentDe(IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE);
        mgr.setForCsEtat1erAcomptes(IREAvances.CS_ETAT_1ER_ACOMPTE_ATTENTE);
        mgr.setForDateDebut1erAcompteNotZero(true);
        try {
            mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            JadeThread.logError(toString(), "Error during retrieving avance unique: " + e.getStackTrace());
        }

        // Iteration sur les résultats
        for (Iterator<REAvance> iter = mgr.iterator(); iter.hasNext();) {
            // Avance
            REAvance avance = iter.next();
            // Tiers
            PRTiersWrapper tiers = null;
            try {
                tiers = PRTiersHelper.getTiersParId(getSession(), avance.getIdTiersBeneficiaire());
            } catch (Exception e) {
                JadeThread.logError(toString(),
                        "Error happened during retrieving tiers for avance: " + e.getStackTrace());
            }
            // Instanciation du pojo enscapsulant
            REAvanceJointTiers avanceWithTiers = new REAvanceJointTiers();
            avanceWithTiers.setAvance(avance);
            avanceWithTiers.setDateNaissance(tiers.getDateNaissance());
            avanceWithTiers.setNom(tiers.getNom());
            avanceWithTiers.setPrenom(tiers.getPrenom());
            avanceWithTiers.setNss(tiers.getNSS());
            // on set le type de demande en fonction du domaine
            if (avance.getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
                avanceWithTiers.setTypeDemande(getSession().getLabel("EXCEL_AVANCES_TYPE_DEMANDE_PC"));
            } else if (avance.getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_RFM)) {
                avanceWithTiers.setTypeDemande(getSession().getLabel("EXCEL_AVANCES_TYPE_DEMANDE_RFM"));
            } else {
                avanceWithTiers.setTypeDemande(getTypeDemandeRente(avance.getIdTiersBeneficiaire()));
            }
            // Ajout dans la liste
            listeAvanceUnique.add(avanceWithTiers);
        }
        return listeAvanceUnique;
    }

    /**
     * Retourne le type de demande de rentes Chaine vide si problème ou demande pas trouvé
     * 
     * @param idTiers
     * @return
     */
    private String getTypeDemandeRente(String idTiers) {
        // Recherche de la demande
        REDemandeRenteJointDemandeManager demMgr = new REDemandeRenteJointDemandeManager();
        demMgr.setSession(getSession());
        demMgr.setForIdTiersRequ(idTiers);
        demMgr.setForCSEtatDemandeNotIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE + ","
                + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE + "," + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE
                + "," + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
        try {
            demMgr.find(getTransaction());
        } catch (Exception e) {
            JadeThread.logError(toString(), "Error during retrieveing demande for avances: " + e.getStackTrace());
        }

        // Si pas de demande retourné, on recherche une demande validé
        if (demMgr.isEmpty()) {
            demMgr.setForCsEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
            demMgr.setForCSEtatDemandeNotIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE + ","
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE + ","
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
            try {
                demMgr.find(getTransaction());
            } catch (Exception e) {
                JadeThread.logError(toString(), "Error during retrieveing demande for avances: " + e.getStackTrace());
            }
            // Si toujours vide on retourne une chaine vide
            if (demMgr.isEmpty()) {
                return "";
            } else {
                REDemandeRenteJointDemande demande = (REDemandeRenteJointDemande) demMgr.getFirstEntity();
                return getSession().getCodeLibelle(demande.getCsTypeDemande());
            }
        } else {
            REDemandeRenteJointDemande demande = (REDemandeRenteJointDemande) demMgr.getFirstEntity();
            return getSession().getCodeLibelle(demande.getCsTypeDemande());
        }
    }

    private final JadeContext initContext(BSession session) throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        return ctxtImpl;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_LONG;
    }

}
