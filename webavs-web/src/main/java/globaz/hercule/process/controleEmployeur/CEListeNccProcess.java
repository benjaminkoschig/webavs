/*
 * Globaz SA.
 */
package globaz.hercule.process.controleEmployeur;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.pyxis.db.tiers.TIRole;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.core.Details;

public class CEListeNccProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private String annee;

    public CEListeNccProcess() {
        super();
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            // 1. Récupération de la liste des affiliés suivis
            List<LUJournalViewBean> listAffiliesSuivis = findAffiliesSuivis();

            // 2. Création et enrichissement de données pour chaque ligne
            List<AffilieSuiviForListe> list = createContenuListe(listAffiliesSuivis);

            // 3. Création du document
            genererDocument(list);

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("EXECUTION_GENERATOIN_LIST_NCC_ERREUR"));

            String messageInformation = "";
            if (!JadeStringUtil.isEmpty(getAnnee())) {
                messageInformation = "Impression de la liste des affiliés suivis \"Non certifiés conformes\" : "
                        + getAnnee() + "\n";
            }
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());
        }

        return true;
    }

    private void genererDocument(List<AffilieSuiviForListe> list) throws IOException {
        String nomDoc = generateFileName(AffilieSuiviForListe.NUMERO_INFOROM);

        Details details = new Details();
        details.add(getSession().getLabel("LIST_NCC_GENERE_LE"), Date.now().getSwissValue());
        details.add(getSession().getLabel("LIST_NCC_UTILISATEUR"), getSession().getUserFullName());
        details.newLigne();

        File file = SimpleOutputListBuilderJade.newInstance().session(getSession()).addTranslater("LIST_NCC")
                .addList(list).classElementList(AffilieSuiviForListe.class).addHeaderDetails(details).asXls()
                .outputName(nomDoc).build();

        // 4. Attachement du doc au process pour envoyer par mail.
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
        docInfo.setDocumentTitle(getSession().getLabel("LIST_NCC_NOM_DOC"));
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AffilieSuiviForListe.NUMERO_INFOROM);
        registerAttachedDocument(docInfo, file.getAbsolutePath());
    }

    private List<AffilieSuiviForListe> createContenuListe(List<LUJournalViewBean> listAffiliesSuivis) throws Exception {

        List<AffilieSuiviForListe> list = new ArrayList<AffilieSuiviForListe>();

        if (!listAffiliesSuivis.isEmpty()) {
            setProgressScaleValue(listAffiliesSuivis.size());

            // PArcours de la liste pour créer chaque ligne de la liste.
            for (LUJournalViewBean entity : listAffiliesSuivis) {
                incProgressCounter();

                AffilieSuiviForListe aff = new AffilieSuiviForListe();
                aff.setNumeroAffilie(entity.getLibelle());
                aff.setDescription(entity.getDestinataire());

                LUReferenceProvenanceListViewBean refProvList = new LUReferenceProvenanceListViewBean();
                refProvList.setSession(getSession());
                refProvList.setForIdJournalisation(entity.getIdJournalisation());
                refProvList.find(BManager.SIZE_NOLIMIT);

                List<LUReferenceProvenanceViewBean> listProv = refProvList.toList();

                aff.setAnneeSuivi(findAnneeSuivi(listProv));

                // récupération de l'affilié pour avoir les dates de début et de fin d'affiliation
                AFAffiliation affiliation = AFAffiliationUtil.getAffiliation(findIdAffiliation(listProv), getSession());
                aff.setDateDebutAffiliation(affiliation.getDateDebut());
                aff.setDateFinAffiliation(affiliation.getDateFin());

                list.add(aff);
            }
        }

        return list;
    }

    private static String findIdAffiliation(List<LUReferenceProvenanceViewBean> listProv) {
        return findParam(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, listProv);
    }

    private static String findAnneeSuivi(List<LUReferenceProvenanceViewBean> listProv) {
        return findParam(ILEConstantes.CS_PARAM_GEN_PERIODE, listProv);
    }

    private static String findParam(String param, List<LUReferenceProvenanceViewBean> listProv) {
        for (LUReferenceProvenanceViewBean ent : listProv) {
            if (param.equals(ent.getTypeReferenceProvenance())) {
                return ent.getIdCleReferenceProvenance();
            }
        }

        return "";
    }

    private List<LUJournalViewBean> findAffiliesSuivis() throws Exception {
        // Création des critères de provenance
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                CEApplication.DEFAULT_APPLICATION_HERCULE);
        if (!JadeStringUtil.isEmpty(getAnnee())) {
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, getAnnee());
        }

        LUJournalListViewBean manager = new LUJournalListViewBean();
        manager.setSession(getSession());
        manager.setProvenance(provenanceCriteres);
        manager.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
        manager.setForValeurCodeSysteme("6200079");
        manager.setForDateReception("0");
        manager.find(BManager.SIZE_NOLIMIT);

        return manager.toList();
    }

    String generateFileName(String numeroInforom) {
        return Jade.getInstance().getPersistenceDir() + numeroInforom + "_"
                + getName().replaceAll(" ", "_").toLowerCase(new Locale(getSession().getIdLangueISO())) + "_"
                + JadeUUIDGenerator.createStringUUID();
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("GENERATION_LIST_NCC_ERREUR");
        } else {
            return getSession().getLabel("GENERATION_LIST_NCC_OK");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    protected void _executeCleanUp() {
        // Not implemented
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
