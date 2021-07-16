package globaz.pegasus.process.comptabilisation;

import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.PegasusRepositoryLocator;
import ch.globaz.pegasus.business.constantes.IPCTypeRestiLegal;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitution;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;

import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.osiris.api.*;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.tools.PRSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PCRestiPCLegalComptaProcess extends BProcess {
    private String dateComptable = "";
    private String dateSurDocument = "";
    private String nss = "";
    private String idTiers = "";
    private APIRubrique REST_AVS_FED = null;
    private APIRubrique REST_AI_FED = null;
    private APIRubrique REST_AVS_SUB = null;
    private APIRubrique REST_AI_SUB = null;
    private APIRubrique REST_AVS_CANT = null;
    private APIRubrique REST_AI_CANT = null;
    private APIRubrique REST_AVS_RFM = null;
    private APIRubrique REST_AI_RFM = null;
    private StringBuilder mailDetail = new StringBuilder();
    Map<String, CodeSystem> mapCsRubrique;
    Map<String, String> mapRubriques = new HashMap<>();
    private SimpleRestitution simpleRestitution = null;
    private final String SIGNE_RESTI = "-";

    private class Paiement {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------
        public String libelleRubrique = "";
        public String montant = "";
        public APIRubrique rubrique = null;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        public Paiement(String libelleRubrique, String montant, APIRubrique rubrique) {
            this.libelleRubrique = libelleRubrique;
            this.montant = montant;
            this.rubrique = rubrique;
        }
    }

    private class Rubrique {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------
        public String libelleRubrique = "";
        public APIRubrique rubrique = null;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        public Rubrique(String libelleRubrique, APIRubrique rubrique) {
            this.libelleRubrique = libelleRubrique;
            this.rubrique = rubrique;
        }
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        boolean noErrorBeforeClose = false;
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            final BISession sessionOsiris = PRSession.connectSession(getSession(), "OSIRIS");
            final APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne) sessionOsiris
                    .getAPIFor(APIGestionComptabiliteExterne.class);
            compta.setDateValeur(dateComptable);
            compta.setTransaction(getTransaction());
            comptaMemoryLog.setSession((BSession) sessionOsiris);
            compta.setMessageLog(comptaMemoryLog);
            compta.setProcess(this);
            compta.setEMailAddress(getEMailAddress());
            compta.setLibelle(getSession().getLabel("LIBELLE_JOURNAL_RESTI_PC_LEGALEMENT") + nss);
            mapCsRubrique = CodeSystemUtils.getCodesSystemes("OSIREFRUB");
            initIdsRubriques(sessionOsiris);
            List<Paiement> listVentilations = getVentilations();

            APIJournal journal = compta.createJournal();
            CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(journal.getIdJournal(),
                    idTiers, IntRole.ROLE_RENTIER, nss, true);
            // on créé un numero de facture unique qui servira a creer la section
            String noFactureNormale = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(), IntRole.ROLE_RENTIER,
                    nss, APISection.ID_TYPE_SECTION_RESTITUTION, String.valueOf(new JADate(dateComptable).getYear()),
                    APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);

            // création de la section "normale" elle servira pour toutes les
            // écritures hormis les restitutions et les
            // compensations sur facture future
            APISection sectionNormale = compta.getSectionByIdExterne(ca.getIdCompteAnnexe(),
                    APISection.ID_TYPE_SECTION_RESTITUTION, noFactureNormale);


            listVentilations.stream().forEach(paiement ->
                    doEcriture(compta,  paiement.montant, paiement.rubrique, ca.getIdCompteAnnexe(), sectionNormale.getIdSection(), null)
            );
            FWMemoryLog beforeCloseComptaMemoryLog = new FWMemoryLog();
            // si pas d'erreurs avant le close, on sauvegarde les messages du
            // comptaMemoryLog
            // pour les restaurer si une erreure survient durant le close
            if (!comptaMemoryLog.hasErrors()) {
                noErrorBeforeClose = true;
                beforeCloseComptaMemoryLog.logMessage(comptaMemoryLog);
            }
            compta.comptabiliser();
            // si pas d'erreurs avant le close et en erreur après le close, on
            // restaure l'ancien
            // memory log pour masquer l'exception.
            // Elle sera directement traitee dans la compta.
            if (noErrorBeforeClose && comptaMemoryLog.hasErrors()) {
                comptaMemoryLog = beforeCloseComptaMemoryLog;
            }
            String idJournal = journal.getIdJournal();
            simpleRestitution.setIdJournal(idJournal);
            simpleRestitution.setId("");
            simpleRestitution = PegasusServiceLocator.getRestitutionService().create(simpleRestitution);
        } catch (Exception e) {
            // si l'exception survient durant le close -> noErrorBeforeClose ==
            // true, l'exception n'est pas remontee
            // Elle sera directement traitee dans la compta.
            JadeThread.rollbackSession();
            if (!noErrorBeforeClose) {

                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
                return false;
            }
        } finally {
            if (comptaMemoryLog != null && comptaMemoryLog.size() > 0) {

                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: START LOG OSIRIS :::::::::::::::::::::::::::::");

                for (int i = 0; i < comptaMemoryLog.size(); i++) {

                    getMemoryLog()
                            .logMessage(null, comptaMemoryLog.getMessage(i).getComplement(),
                                    comptaMemoryLog.getMessage(i).getTypeMessage(),
                                    comptaMemoryLog.getMessage(i).getIdSource());

                }
                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: END LOG OSIRIS :::::::::::::::::::::::::::::");

            }
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
            JadeThread.closeSession();
        }

        return true;
    }


    private void initIdsRubriques(BISession sessionOsiris) throws Exception {
        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);
        String idRubrique = "";
        if (simpleRestitution.getTypeRestPCAVSFed() == null) {
            REST_AVS_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_FED_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AVS_FED_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCAVSFed()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AVS_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_FED_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_FED_HOME_EMS;
                    break;
                case IPCTypeRestiLegal.HOME_ESE:
                    REST_AVS_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_FED_HOME_ESE);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_FED_HOME_ESE;
                    break;
                case IPCTypeRestiLegal.HOME_EPSM:
                    REST_AVS_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_FED_HOME_EPSM);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_FED_HOME_EPSM;
                    break;
                default:
                    REST_AVS_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_FED_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_FED_DOMICILE;
            }
        }
        mapRubriques.put("REST_AVS_FED", mapCsRubrique.get(idRubrique).getTraduction());

        if (simpleRestitution.getTypeRestPCAIFed() == null) {
            REST_AI_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_FED_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AI_FED_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCAIFed()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AI_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_FED_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_FED_HOME_EMS;
                    break;
                case IPCTypeRestiLegal.HOME_ESE:
                    REST_AI_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_FED_HOME_ESE);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_FED_HOME_ESE;
                    break;
                case IPCTypeRestiLegal.HOME_EPSM:
                    REST_AI_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_FED_HOME_EPSM);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_FED_HOME_EPSM;
                    break;
                default:
                    REST_AI_FED = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_FED_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_FED_DOMICILE;
            }
        }
        mapRubriques.put("REST_AI_FED", mapCsRubrique.get(idRubrique).getTraduction());
        if (simpleRestitution.getTypeRestPCAvsSubside() == null) {
            REST_AVS_SUB = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_SUB_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AVS_SUB_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCAvsSubside()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AVS_SUB = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_SUB_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_SUB_HOME_EMS;
                    break;
                default:
                    REST_AVS_SUB = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_SUB_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_SUB_DOMICILE;
            }
        }
        mapRubriques.put("REST_AVS_SUB", mapCsRubrique.get(idRubrique).getTraduction());
        if (simpleRestitution.getTypeRestPCAISubside() == null) {
            REST_AI_SUB = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_SUB_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AI_SUB_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCAISubside()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AI_SUB = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_SUB_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_SUB_HOME_EMS;
                    break;
                default:
                    REST_AI_SUB = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_SUB_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_SUB_DOMICILE;
            }
        }
        mapRubriques.put("REST_AI_SUB", mapCsRubrique.get(idRubrique).getTraduction());
        if (simpleRestitution.getTypeRestPCAvsCantonal() == null) {
            REST_AVS_CANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_CANT_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AVS_CANT_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCAvsCantonal()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AVS_CANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_CANT_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_CANT_HOME_EMS;
                    break;
                default:
                    REST_AVS_CANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_CANT_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_CANT_DOMICILE;
            }
        }
        mapRubriques.put("REST_AVS_CANT", mapCsRubrique.get(idRubrique).getTraduction());
        if (simpleRestitution.getTypeRestPCAICantonal() == null) {
            REST_AI_CANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_CANT_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AI_CANT_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCAICantonal()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AI_CANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_CANT_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_CANT_HOME_EMS;
                    break;
                default:
                    REST_AI_CANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_CANT_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_CANT_DOMICILE;
            }
        }
        mapRubriques.put("REST_AI_CANT", mapCsRubrique.get(idRubrique).getTraduction());

        if (simpleRestitution.getTypeRestPCRfmAvs() == null) {
            REST_AVS_RFM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_RFM_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AVS_RFM_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCRfmAvs()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AVS_RFM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_RFM_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_RFM_HOME_EMS;
                    break;
                default:
                    REST_AVS_RFM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AVS_RFM_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AVS_RFM_DOMICILE;
            }
        }
        mapRubriques.put("REST_AVS_RFM", mapCsRubrique.get(idRubrique).getTraduction());

        if (simpleRestitution.getTypeRestPCRfmAI() == null) {
            REST_AI_RFM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_RFM_DOMICILE);
            idRubrique = APIReferenceRubrique.PC_REST_AI_RFM_DOMICILE;
        } else {
            switch (simpleRestitution.getTypeRestPCRfmAI()) {
                case IPCTypeRestiLegal.HOME_EMS:
                    REST_AI_RFM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_RFM_HOME_EMS);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_RFM_HOME_EMS;
                    break;
                default:
                    REST_AI_RFM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_REST_AI_RFM_DOMICILE);
                    idRubrique = APIReferenceRubrique.PC_REST_AI_RFM_DOMICILE;
            }
        }
        mapRubriques.put("REST_AI_RFM", mapCsRubrique.get(idRubrique).getTraduction());

    }

    private List<Paiement> getVentilations() throws Exception {
        List<Paiement> list = new ArrayList<>();
        Paiement paiement;
        StringBuilder emptyRubrique = new StringBuilder();
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCAvsFederal())) {
            paiement = new Paiement(mapRubriques.get("REST_AVS_FED"), simpleRestitution.getMontantRestitutionPCAvsFederal(), REST_AVS_FED);
            list.add(paiement);
        }
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCAIFederal())) {
            paiement = new Paiement(mapRubriques.get("REST_AI_FED"), simpleRestitution.getMontantRestitutionPCAIFederal(), REST_AI_FED);
            list.add(paiement);
        }
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCAvsSubside())) {
            paiement = new Paiement(mapRubriques.get("REST_AVS_SUB"), simpleRestitution.getMontantRestitutionPCAvsSubside(), REST_AVS_SUB);
            list.add(paiement);
        }
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCAISubside())) {
            paiement = new Paiement(mapRubriques.get("REST_AI_SUB"), simpleRestitution.getMontantRestitutionPCAISubside(), REST_AI_SUB);
            list.add(paiement);
        }
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCAvsCantonal())) {
            paiement = new Paiement(mapRubriques.get("REST_AVS_CANT"), simpleRestitution.getMontantRestitutionPCAvsCantonal(), REST_AVS_CANT);
            list.add(paiement);
        }
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCAICantonal())) {
            paiement = new Paiement(mapRubriques.get("REST_AI_CANT"), simpleRestitution.getMontantRestitutionPCAICantonal(), REST_AI_CANT);
            list.add(paiement);
        }
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCRfmAvs())) {
            paiement = new Paiement(mapRubriques.get("REST_AVS_RFM"), simpleRestitution.getMontantRestitutionPCRfmAvs(), REST_AVS_RFM);
            list.add(paiement);
        }
        if (!JadeStringUtil.isBlankOrZero(simpleRestitution.getMontantRestitutionPCRfmAI())) {
            paiement = new Paiement(mapRubriques.get("REST_AI_RFM"), simpleRestitution.getMontantRestitutionPCRfmAI(), REST_AI_RFM);
            list.add(paiement);
        }

        list.stream().forEach(p -> {
            if (p.rubrique == null) {
                emptyRubrique.append("No rubrique mapped : " + p.libelleRubrique + "\n");
            }
        });

        if (!JadeStringUtil.isBlankOrZero(emptyRubrique.toString())) {
            throw new Exception(emptyRubrique.toString());
        }
        return list;

    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private JadeThreadContext initThreadContext(BSession session) throws Exception {

        JadeThreadContext context;
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
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public String getDateComptable() {
        return dateComptable;
    }

    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    public SimpleRestitution getSimpleRestitution() {
        return simpleRestitution;
    }

    public void setSimpleRestitution(SimpleRestitution simpleRestitution) {
        this.simpleRestitution = simpleRestitution;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * écrit une écriture en compta. Ne fait rien si le montant est nul.
     *
     * @param compta          une instance de APIProcessComptabilisation
     * @param montantSigne    Le montant a écrire, signé
     * @param rubrique        l'id de la rubrique
     * @param idCompteAnnexe  l'id du compta annexe
     * @param idSection       l'id de la section
     * @param anneeCotisation l'année de la cotisation, null s'il ne s'agit pas d'une cotisation
     */
    private void doEcriture(APIGestionComptabiliteExterne compta, String montantSigne, APIRubrique rubrique,
                            String idCompteAnnexe, String idSection, String anneeCotisation) {
        if (!JadeStringUtil.isBlankOrZero(montantSigne)) {
            FWCurrency montant = new FWCurrency(SIGNE_RESTI+montantSigne);
            boolean positif = true;

            if (montant.isNegative()) {
                montant.negate();
                positif = false;
            }

            APIEcriture ecriture = compta.createEcriture();
            ecriture.setIdCompteAnnexe(idCompteAnnexe);
            ecriture.setIdSection(idSection);
            ecriture.setDate(dateComptable);
            ecriture.setIdCompte(rubrique.getIdRubrique());
            ecriture.setMontant(montant.toString());

            mailDetail.append(java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ECRITURE"), new Object[]{
                    montantSigne.toString(), rubrique.getIdExterne()}) + "\n");

            if (positif) {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            }

            if (anneeCotisation != null) {
                ecriture.setAnneeCotisation(anneeCotisation);
            }

            compta.addOperation(ecriture);
        }
    }


    public StringBuilder getMailDetail() {
        return mailDetail;
    }

    public void setMailDetail(StringBuilder mailDetail) {
        this.mailDetail = mailDetail;
    }
}
