package globaz.osiris.db.ordres.sepa;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOVforOR;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreRejete;
import globaz.osiris.db.ordres.CAOrdreRejeteManager;
import globaz.osiris.db.ordres.ContainerOrdreRejete;
import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import globaz.osiris.db.ordres.sepa.utils.CASepaOGConverterUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.configuration.HeaderFooter;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.Configurations;

public class CAListOrdreRejeteProcess extends BProcess {

    private static final long serialVersionUID = 7164797280525378834L;

    private static final String INFOROM_NUMBER = "0322GCA";

    private static final Logger logger = LoggerFactory.getLogger(CAListOrdreRejeteProcess.class);

    private String idOrdreGroupe;
    List<String> mailsList = new ArrayList<String>();
    List<String> reasons = new ArrayList<String>();
    private String email;

    private CAOrdreGroupe ordreGroupe;

    public void process(BSession session, CAOrdreGroupe og, List<String> reasons) {
        setOrdreGroupe(og);
        setReasons(reasons);
        setSession(session);
        if (mailsList.isEmpty()) {
            mailsList.add(getEmail());
        }

        logger.info("{} Début du traitement de génération des listes OrdreRejete", getName());

        List<ContainerOrdreRejete> ordreRejetesContainer = new ArrayList<ContainerOrdreRejete>();

        List<CAOrdreRejete> listOrdreRejete = getOrdreRejeteForOG(getOrdreGroupe());
        Map<String, List<CAOrdreRejete>> mapOrdreRej = new HashMap<String, List<CAOrdreRejete>>();
        for (CAOrdreRejete ordreRej : listOrdreRejete) {
            String key = ordreRej.getIdOperation();
            if (mapOrdreRej.containsKey(key)) {
                mapOrdreRej.get(key).add(ordreRej);
            } else {
                List<CAOrdreRejete> value = new ArrayList<CAOrdreRejete>();
                value.add(ordreRej);
                mapOrdreRej.put(key, value);
            }
        }

        Map<String, CAOVforOR> opMap = getOpMapOVfromOG(getOrdreGroupe(), mapOrdreRej.keySet());

        for (Map.Entry<String, List<CAOrdreRejete>> entryOR : mapOrdreRej.entrySet()) {

            ContainerOrdreRejete cor;
            cor = new ContainerOrdreRejete(entryOR.getValue(), opMap.get(entryOR.getKey()));
            ordreRejetesContainer.add(cor);

        }

        if (!APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET.equals(getOrdreGroupe().getIsoCsTransmissionStatutExec())
                || !ordreRejetesContainer.isEmpty() || !reasons.isEmpty()) {

            String generatedXlsFilePath;

            // besoin du context pour la gestion des traduction depuis simpleOutputList
            try {
                JadeThreadActivator.startUsingJdbcContext(this, getNewJadeThreadContext().getContext());

                generatedXlsFilePath = generateXls(getOrdreGroupe(), ordreRejetesContainer, getSession());

            } catch (Exception e) {
                throw new SepaException("could not generate XLS for OrdreRejeté: "
                        + getOrdreGroupe().getIdOrdreGroupe() + ": " + e, e);
            } finally {
                JadeThreadActivator.stopUsingContext(this);
            }

            try {
                // envoyer l'email
                sendMail(mailsList, generatedXlsFilePath, ordreRejetesContainer.isEmpty(), reasons);
            } catch (Exception e) {
                logger.error("une erreur est survenue lors du traitement", e);
            }
        }

        logger.info("{} Fin du traitement de génération des listes d'OrdreRejete", getName());
    }

    private JadeThreadContext getNewJadeThreadContext() throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(getSession().getApplicationId());
        ctxtImpl.setLanguage(getSession().getIdLangue());
        ctxtImpl.setUserEmail(getSession().getUserEMail());
        ctxtImpl.setUserId(getSession().getUserId());
        ctxtImpl.setUserName(getSession().getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(getSession().getUserId());

        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }

        return new JadeThreadContext(ctxtImpl);
    }

    private List<CAOrdreRejete> getOrdreRejeteForOG(CAOrdreGroupe og) {
        CAOrdreRejeteManager mgr = new CAOrdreRejeteManager();
        mgr.setSession(getSession());
        mgr.setForIdOG(og.getIdOrdreGroupe());

        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new SepaException("could not search for OrdreRejeté: " + og.getIdOrdreGroupe() + ": " + e, e);
        }
        List<CAOrdreRejete> ovs = mgr.toList();
        return ovs;
    }

    private Map<String, CAOVforOR> getOpMapOVfromOG(CAOrdreGroupe og, Set<String> idSet) {
        Map<String, CAOVforOR> ovsMap = new HashMap<String, CAOVforOR>();
        List<String> idList = new ArrayList<String>();
        idList.addAll(idSet);

        for (int i = 0; idList.size() > (1000 * i); i++) {
            String inIds = "";
            int max = 1000 + (i * 1000);
            if (idList.size() < max) {
                max = idList.size();
            }

            if (!(idList == null)) {
                inIds = StringUtils.join(idList.subList(i * 1000, max), ",");
            }
            CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
            mgr.setSession(getSession());
            mgr.setForIdOrdreGroupe(og.getIdOrdreGroupe());
            mgr.setInIdOperation(inIds);
            mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);
            try {
                mgr.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new SepaException("could not search for transactions: " + og.getIdOrdreGroupe() + ": " + e, e);
            }

            List<APICommonOdreVersement> ovs = mgr.toList();
            for (APICommonOdreVersement ov : ovs) {
                try {
                    CAOVforOR pojo = new CAOVforOR(ov);
                    ovsMap.put(ov.getIdOperation(), pojo);
                } catch (Exception e) {
                    logger.error(
                            "impossible de construire les info pour la ligne de détail sur l'OV" + ov.getIdOperation(),
                            e);
                }
            }
        }
        return ovsMap;
    }

    private void sendMail(List<String> mailsList, String joinFilePath, boolean hasNoOrdreRejetes, List<String> reasons)
            throws Exception {
        // ajout de la pièce jointe
        List<String> joinsFilesPathsList = new ArrayList<String>();

        String numTransac = getOrdreGroupe().getIsoNumLivraison();
        String subject;
        String body;

        // Si il n'y a pas ordreRejeté mais un motif il s'agit d'un rejet de A-Level
        if (hasNoOrdreRejetes && !reasons.isEmpty()) {
            subject = FWMessageFormat.format(getSession().getLabel("LIST_OSIRIS_ORDREREJETE_MAIL_SUBJECT_EMPTY"),
                    getOrdreGroupe().getNumLivraison());
            body = getSession().getLabel("LIST_OSIRIS_ORDREREJETE_MAIL_BODY_EMPTY");
            body += ArrayUtils.toString(reasons);
        } else {

            // joindre la pièce jointe
            joinsFilesPathsList.add(joinFilePath);
            // sujet et corps du mail
            subject = FWMessageFormat.format(getSession().getLabel("LIST_OSIRIS_ORDREREJETE_MAIL_SUBJECT_SUCCESS"),
                    numTransac);
            body = FWMessageFormat.format(getSession().getLabel("LIST_OSIRIS_ORDREREJETE_MAIL_BODY_SUCCESS"),
                    numTransac);

            body += ArrayUtils.toString(reasons);
        }
        // envoi
        ProcessMailUtils.sendMail(mailsList, subject, body, joinsFilesPathsList);
    }

    /**
     * Générer une liste au format XLS
     * 
     * @param list
     * @return
     * @throws Exception
     */
    static String generateXls(CAOrdreGroupe og, List<ContainerOrdreRejete> list, BSession session) throws Exception {
        String title = "";

        Configuration configuration = Configurations.buildeDefault();
        HeaderFooter headerFooter = configuration.getHeaderFooter();

        headerFooter.setRightTop(FWMessageFormat.format(session.getLabel("LIST_OSIRIS_ORDREREJETE_RIGHT_TOP"),
                new Date()));
        headerFooter.setLeftTop(INFOROM_NUMBER);
        headerFooter.setLeftBottom(" - " + session.getUserName());

        title = session.getLabel("LIST_OSIRIS_ORDREREJETE_TITLE");

        Details detail = new Details();
        detail.add(session.getLabel("LIST_OSIRIS_ORDREREJETE_DETAIL_CAISSE"), CASepaOGConverterUtils.getNomCaisse70(og));
        detail.newLigne();
        detail.add(session.getLabel("LIST_OSIRIS_ORDREREJETE_DETAIL_NUM_OG"), og.getIdOrdreGroupe());
        detail.newLigne();
        detail.add(session.getLabel("LIST_OSIRIS_ORDREREJETE_DETAIL_MOTIF"), og.getMotif());
        detail.newLigne();
        detail.add(session.getLabel("LIST_OSIRIS_ORDREREJETE_DETAIL_DATE_ECHEANCE"), og.getDateEcheance());
        detail.newLigne();
        detail.add(session.getLabel("LIST_OSIRIS_ORDREREJETE_DETAIL_OE"), og.getOrganeExecution().getNom());
        detail.newLigne();
        detail.add(session.getLabel("LIST_OSIRIS_ORDREREJETE_DETAIL_NUM_LIVRAISON"), og.getIsoNumLivraison());
        File file = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath(INFOROM_NUMBER + "_ISO20022_ListOrdreRejete_" + og.getIsoNumLivraison())
                .configure(configuration).addList(list).classElementList(ContainerOrdreRejete.class)
                .addTitle(title, Align.LEFT).addHeaderDetails(detail).asXls().build();
        return file.getAbsolutePath();
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    /**
     * used by process heritage (from servlet action) to execute de list generation
     * this one don't take any argument, but they are needed from viewbean (yes, viewbean extends this, don't ask me
     * why, work as Jade design)
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        setSendCompletionMail(false);
        if (getOrdreGroupe() == null) {
            CAOrdreGroupe og = new CAOrdreGroupe();
            og.setIdOrdreGroupe(getIdOrdreGroupe());
            og.setSession(getSession());
            og.retrieve();
            setOrdreGroupe(og);
        }
        process(getSession(), getOrdreGroupe(), new ArrayList<String>());
        return true;
    }

    private String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public void setIdOrdreGroupe(String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    @Override
    protected String getEMailObject() {
        return "Liste des Transactions rejetées";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public CAOrdreGroupe getOrdreGroupe() {
        return ordreGroupe;
    }

    public void setOrdreGroupe(CAOrdreGroupe ordreGroupe) {
        this.ordreGroupe = ordreGroupe;
    }

    /**
     * use this instead of set email
     * 
     * @param email
     */
    public void addMail(String email) {
        mailsList.add(email);
    }

    public String getEmail() {
        if (email == null) {
            return getSession().getUserEMail();
        }
        return email;
    }

    /**
     * nested setter for strange use of Bprocess into Viewbean in CA as ViewBean parent!
     * 
     * @param email from the jsp
     */
    @Deprecated
    public void setEmail(String email) {
        this.email = email;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

}
