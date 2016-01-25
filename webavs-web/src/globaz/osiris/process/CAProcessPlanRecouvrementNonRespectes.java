package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrementManager;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.print.itext.list.CAIListPlanRecouvNonRespectes;
import globaz.osiris.print.itext.list.CAIRappelPlanRecouv;
import globaz.osiris.utils.CASursisPaiement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author kurkus, 27 mai 05
 * @author sel, 13 juillet 2007
 */
public class CAProcessPlanRecouvrementNonRespectes extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String TRICA_NUMERO = "Numero";
    private String beforeNoAffilie = "";

    private String dateDocument = "";
    private String dateRef = "";
    private String delaiRappel = "";
    private String delaiSuspension = "";
    private CAIListPlanRecouvNonRespectes document = null;
    private String emailObjet = "";
    private String fromNoAffilie = "";
    private List idRoles;
    private String tacheEff = ""; // tâche à effectuer
    private String triCA = "";
    private String typeImpression = "pdf";

    /**
     * Constructor for CAProcessPlanRecouvrementNonRespectes.
     */
    public CAProcessPlanRecouvrementNonRespectes() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessPlanRecouvrementNonRespectes.
     * 
     * @param parent
     */
    public CAProcessPlanRecouvrementNonRespectes(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessPlanRecouvrementNonRespectes.
     * 
     * @param session
     */
    public CAProcessPlanRecouvrementNonRespectes(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        String numeroRefInforom = CAIRappelPlanRecouv.NUMERO_REFERENCE_INFOROM;
        try {
            switch (getTacheEffAsInteger()) {
                case 1:
                    impressionPlanNonRespecte();
                    numeroRefInforom = CAIListPlanRecouvNonRespectes.NUMERO_REFERENCE_INFOROM;
                    break;
                case 2:
                    suspensionAuto();
                    break;
                case 3:
                    // Imprime les rappels
                    actionsSurPlanNonRespecte(false, true);
                    break;
                case 4:
                    // Imprime les rappels avec l'échéancier
                    actionsSurPlanNonRespecte(false, true);
                    break;
            }

            if (isAborted()) {
                getAttachedDocuments().clear();
                return false;
            } else {
                // Fusionne les documents
                if ((getTacheEffAsInteger() == 1) && "xls".equals(getTypeImpression())) {
                    return true;
                }

                fusionneDocuments(numeroRefInforom);
                return true;
            }
        } catch (Exception e) {
            this._addError(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
            return false;
        }
    }

    /**
     * <ul>
     * <li/>annule:true rappel:true = Suspend les plans ; caisse avec rappel
     * <li/>annule:false rappel:true = Imprime les rappels
     * <li/>annule:true rappel:false = Suspend les plans ; caisse sans rappel
     * <li/>annule:false rappel:false = Imprime la liste
     * </ul>
     * La suspension a lieu si un rappel a été émis et si le délai entre le rappel et la suspension est dépassé. La
     * suspension a lieu si le délai entre la date d'échéance et la date de suspension est dépassé.
     * 
     * @param annule
     *            : supendre les plans
     * @param rappel
     *            : imprimer les rappels
     * @return La listes des plans
     * @throws JAException
     */
    private void actionsSurPlanNonRespecte(boolean annule, boolean rappel) throws JAException {
        // Pour le parcours des plans
        CAPlanRecouvrementManager planManager = new CAPlanRecouvrementManager();
        try {
            initPlanManager(planManager);

            if (isAborted()) {
                return;
            }

            setProgressScaleValue(planManager.size());
            setEmailObjet(getSession().getLabel("OSIRIS_IMPRESSION_AUCUN_SURSIS"));
            // Parcours des sursis au paiement
            for (int i = 0; (i < planManager.size()) && !isAborted(); i++) {
                String idPlanRecouvrement = ((CAPlanRecouvrement) planManager.getEntity(i)).getIdPlanRecouvrement();

                if (countEcheancePlan(annule, rappel, idPlanRecouvrement) > 0) { // >
                    // 0
                    // signifie
                    // :
                    // plan
                    // non
                    // respecté
                    CAPlanRecouvrement plan = (CAPlanRecouvrement) planManager.getEntity(i);
                    if (annule) {
                        // Définit l'objet du mail
                        setEmailObjet(getSession().getLabel("PLAN_SUSPENSION_EMAIL"));
                        // Le plan de paiement est mis à annulé et pour chaque
                        // section couverte on sette la date de fin à la date du
                        // jour.
                        CASursisPaiement.annulerPlan(this, getTransaction(), plan, JACalendar.todayJJsMMsAAAA(),
                                Boolean.TRUE);
                    }
                    if (!annule && rappel) {
                        // Définit l'objet du mail
                        setEmailObjet(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS"));
                        // Met à jour les échéances
                        // Création du document
                        CASursisPaiement.createRappel(this, getSession(), plan.getIdPlanRecouvrement(), getDateRef());
                        CASursisPaiement.updateEcheancesEchues(getTransaction(), plan.getIdPlanRecouvrement(),
                                getDateRef(), getDateLimite(getDelaiRappelAsInteger()));

                        if (getTacheEffAsInteger() == 4) {
                            // Imprimer l'échéancier
                            CASursisPaiement.createEcheancier(this, getTransaction(), plan);
                        }
                    }
                    setProgressDescription("Plan:" + plan.getIdPlanRecouvrement());
                }
                incProgressCounter();
            }
        } catch (Exception e) {
            super._addError(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
            super.setMsgType(FWViewBeanInterface.WARNING);
            super.setMessage(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
            throw new JAException(getSession().getLabel("OSIRIS_PLAN_NON_RESP") + " : " + e.getMessage());
        } finally {
            planManager = null;
        }
    }

    /**
     * Retourne le nombre d'échéance correspondant aux critères pour le plan donné.
     * <ul>
     * <li/>annule:true rappel:false = Suspend les plans ; caisse sans rappel
     * <li/>annule:false rappel:true = Imprime les rappels
     * <li/>annule:true rappel:true = Suspend les plans ; caisse avec rappel
     * </ul>
     * 
     * @param annule
     * @param rappel
     * @param echeancePlanManager
     * @param idPlanRecouvrement
     * @return le nombre d'échéance pour le plan
     * @throws JAException
     * @throws Exception
     */
    private int countEcheancePlan(boolean annule, boolean rappel, String idPlanRecouvrement) throws JAException,
            Exception {
        // Pour le parcours des échéances pour déterminé si le plan est non
        // respecté
        CAEcheancePlanManager echeancePlanManager = new CAEcheancePlanManager();
        // Recherche les écheances pour le plan de recouvrement dont l'échéance
        // dépasse le délai
        echeancePlanManager.setSession(getSession());
        echeancePlanManager.setForIdPlanRecouvrement(idPlanRecouvrement);
        echeancePlanManager.setForDateEffectiveIsNull(); // Echéance sans
        // paiement
        if (annule && !rappel) {
            echeancePlanManager.setToDateExigibilite(getDateLimite(getDelaiSuspensionAsInteger()).toStr("."));
        }
        if (!annule && rappel) {
            echeancePlanManager.setToDateExigibilite(getDateLimite(getDelaiRappelAsInteger()).toStr("."));
            echeancePlanManager.setToDateRappel(getDateLimite(getDelaiRappelAsInteger()).toStr("."));
        }
        if (annule && rappel) {
            echeancePlanManager.setToDateRappel(getDateLimite(getDelaiSuspensionAsInteger()).toStr("."));
            echeancePlanManager.setForDateRappelIsNotNull(); // Echéance avec
            // rappel
        }
        echeancePlanManager.find(getTransaction());

        return echeancePlanManager.size();
    }

    /**
     * Fusionne les documents. <br>
     * Envoie un e-mail avec les pdf fusionnés. <br>
     * 
     * @author: sel Créé le : 16 nov. 06
     * @throws Exception
     */
    private void fusionneDocuments(String numeroRefInforom) throws Exception {
        // Fusionne les documents (Décision, voies de droit et échéancier)
        // Les documents fusionnés sont effacés (théoriquement!!)
        JadePublishDocumentInfo info = createDocumentInfo();
        // Envoie un e-mail avec les pdf fusionnés
        info.setPublishDocument(true);
        info.setArchiveDocument(false);
        info.setDocumentTypeNumber(numeroRefInforom);
        this.mergePDF(info, false, 500, false, null);
    }

    /**
     * @return the beforeNoAffilie
     */
    public String getBeforeNoAffilie() {
        return beforeNoAffilie;
    }

    /**
     * @return the dateDocument
     */
    public String getDateDocument() {
        return dateDocument;
    }

    /**
     * Détermine la date limite (dateRef - delai)
     * 
     * @param delai
     *            à soustraire à la date de référence
     * @return la date limite à utiliser pour les echéances.
     * @throws JAException
     */
    private JADate getDateLimite(int delai) throws JAException {
        JACalendar cal = new JACalendarGregorian();
        JADate dateLimite;
        dateLimite = cal.addDays(new JADate(getDateRef()), -delai);
        return dateLimite;
    }

    /**
     * @return la date de référence. Sinon, la date du jour.
     */
    public String getDateRef() {
        if (JadeStringUtil.isBlank(dateRef)) {
            return JACalendar.todayJJsMMsAAAA();
        } else {
            return dateRef;
        }
    }

    /**
     * @return the delaiRappel
     */
    public String getDelaiRappel() {
        return delaiRappel;
    }

    /**
     * @return the delaiRappel de l'écran sinon le delai stocké dans FWPARP
     */
    public int getDelaiRappelAsInteger() {
        int delaiRappel = CAParametres.getDelaiRappel(getTransaction());

        if (!JadeStringUtil.isBlank(getDelaiRappel())) {
            delaiRappel = Integer.parseInt(getDelaiRappel());
        }

        return delaiRappel;
    }

    /**
     * @return the delaiSuspension
     */
    public String getDelaiSuspension() {
        return delaiSuspension;
    }

    /**
     * @return the delaiSuspension de l'écran sinon le delai stocké dans FWPARP
     */
    public int getDelaiSuspensionAsInteger() {
        int delaiSuspension = CAParametres.getDelaiSuspension(getTransaction());

        if (!JadeStringUtil.isBlank(getDelaiSuspension())) {
            delaiSuspension = Integer.parseInt(getDelaiSuspension());
        }

        return delaiSuspension;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // Sujet du mail
        if (!JadeStringUtil.isBlank(getEmailObjet())) {
            return getEmailObjet();
        }
        if (document == null) {
            return getSession().getLabel("PLAN_SUSPENSION_EMAIL");
        } else {
            return document.getDocumentTitle();
        }
    }

    /**
     * @return the emailObjet
     */
    public String getEmailObjet() {
        return emailObjet;
    }

    /**
     * @return the fromNoAffilie
     */
    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    /**
     * @return the idRoles
     */
    public List getIdRoles() {
        return idRoles;
    }

    public String getTacheEff() {
        return tacheEff;
    }

    /**
     * @return the tacheEff
     */
    public int getTacheEffAsInteger() {
        return Integer.parseInt(tacheEff);
    }

    public String getTriCA() {
        return triCA;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * Imprime la liste des sursis au paiement non respecté.
     * 
     * @throws FWIException
     * @throws Exception
     */
    private void impressionPlanNonRespecte() throws FWIException, Exception {
        // Instancie le document du plan de recouvrement non respecté
        document = new CAIListPlanRecouvNonRespectes(this);
        document.setSession(getSession());
        // Demander le traitement du document
        document.setEMailAddress(getEMailAddress());
        document.setDateRef(getDateRef());
        document.setTriCA(getTriCA());
        document.setBeforeNoAffilie(getBeforeNoAffilie());
        document.setFromNoAffilie(getFromNoAffilie());
        document.setIdRoles(getIdRoles());
        document.setTypeImpression(typeImpression);

        document.executeProcess();

        // Tester si abort
        if (isAborted()) {
            throw new Exception(this.getClass().getName() + "._executeProcess() : Error, document "
                    + document.getImporter().getDocumentTemplate() + " aborted !");
        }
        if (document.getDocumentList().size() <= 0) {
            throw new Exception(this.getClass().getName() + "._executeProcess() : Error, document "
                    + document.getImporter().getDocumentTemplate() + " can not be created !");
        }
    }

    /**
     * Trouve les plans actifs, correspondant aux critères de l'écran.
     * 
     * @param planManager
     * @throws Exception
     */
    private void initPlanManager(CAPlanRecouvrementManager planManager) throws Exception {
        planManager.setSession(getSession());
        planManager.setForIdEtat(CAPlanRecouvrement.CS_ACTIF);
        if (getTriCA().equals(CAProcessPlanRecouvrementNonRespectes.TRICA_NUMERO)) {
            planManager.setOrderBy(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        } else {
            planManager.setOrderBy(CACompteAnnexe.FIELD_DESCRIPTION);
        }

        // Liste de role
        if (getIdRoles() != null) {
            StringBuffer clause = new StringBuffer("");
            for (Iterator idRoleIter = getIdRoles().iterator(); idRoleIter.hasNext();) {
                clause.append((String) idRoleIter.next());

                if (idRoleIter.hasNext()) {
                    clause.append(",");
                }
            }
            planManager.setForSelectionRole(clause.toString());
        }
        // planManager.setForSelectionRole(CARole.listeIdsRolesPourUtilisateurCourant(getSession()));
        // // TODO mettre cette ligne dans le else ??

        // si typePlanBVR==on, la case est cochée.
        // si typePlanBVR=="", la case est pas cochée.
        ArrayList listForIdModeRecouvrementIn = new ArrayList();
        listForIdModeRecouvrementIn.add(CAPlanRecouvrement.CS_BVR);
        planManager.setForIdModeRecouvrementIn(listForIdModeRecouvrementIn);

        // Tranche d'affilies
        if (!JadeStringUtil.isBlank(getFromNoAffilie())) {
            planManager.setFromIdExternalRole(getFromNoAffilie());
        }
        if (!JadeStringUtil.isBlank(getBeforeNoAffilie())) {
            planManager.setUntilIdExternalRole(getBeforeNoAffilie());
        }

        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
    }

    /**
     * getter pour l'attribut selected id role.
     * 
     * @param idRole
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut selected id role
     */
    public boolean isSelectedIdRole(String idRole) {
        return (idRoles != null) && idRoles.contains(idRole);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param beforeNoAffilie
     *            the beforeNoAffilie to set
     */
    public void setBeforeNoAffilie(String beforeNoAffilie) {
        this.beforeNoAffilie = beforeNoAffilie;
    }

    /**
     * @param dateDocument
     *            the dateDocument to set
     */
    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDateRef(String date) {
        dateRef = date;
    }

    /**
     * @param delaiRappel
     *            the delaiRappel to set
     */
    public void setDelaiRappel(String delaiRappel) {
        this.delaiRappel = delaiRappel;
    }

    /**
     * @param delaiSuspension
     *            the delaiSuspension to set
     */
    public void setDelaiSuspension(String delaiSuspension) {
        this.delaiSuspension = delaiSuspension;
    }

    /**
     * @param emailObjet
     *            the emailObjet to set
     */
    public void setEmailObjet(String emailObjet) {
        this.emailObjet = emailObjet;
    }

    /**
     * @param fromNoAffilie
     *            the fromNoAffilie to set
     */
    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    /**
     * @param idRoles
     *            the idRoles to set
     */
    public void setIdRoles(List idRoles) {
        this.idRoles = idRoles;
    }

    /**
     * @param tacheEff
     *            the tacheEff to set
     */
    public void setTacheEff(String tacheEff) {
        this.tacheEff = tacheEff;
    }

    public void setTriCA(String string) {
        triCA = string;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

    /**
     * Suspend les sursis au paiement.
     * 
     * @throws FWIException
     * @throws Exception
     */
    private void suspensionAuto() throws FWIException, Exception {
        // Si la caisse gère les rappels sur les plans de paiement
        if (CAApplication.getApplicationOsiris().getCAParametres().isRappelSurPlan()) {
            // la suspension a lieu si un rappel a été émis et si le délai entre
            // le rappel et la suspension est dépassé
            actionsSurPlanNonRespecte(true, true);
        } else {
            // la suspension a lieu si le délai entre la date d'échéance et la
            // date de suspension est dépassé
            actionsSurPlanNonRespecte(true, false);
        }
        document = null;
    }
}
