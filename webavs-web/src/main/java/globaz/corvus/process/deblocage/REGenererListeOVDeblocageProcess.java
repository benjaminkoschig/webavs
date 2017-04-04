package globaz.corvus.process.deblocage;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.deblocage.REDeblocageVersement;
import globaz.corvus.db.deblocage.REDeblocageVersementManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.AdressePaiement;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.services.AdressePaiementLoader;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;

public class REGenererListeOVDeblocageProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private static final String NUM_INFOROM = "5159PRE";

    private String forIdLot;

    @Override
    protected void _executeCleanUp() {
        // nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        // récupération des données
        REDeblocageVersementManager manager = new REDeblocageVersementManager();
        manager.setSession(getSession());
        manager.setForIdLot(forIdLot);
        manager.find(BManager.SIZE_NOLIMIT);

        List<REDeblocageVersement> lstDeblocageVersement = manager.toList();

        if (lstDeblocageVersement.isEmpty()) {
            return true;
        }

        // Conversion des données pour l'impresssion
        List<REOVDeblocageForListContainer> listOv = prepareDateToImpress(lstDeblocageVersement);

        // génération du document
        String docPath = genererFileExcel(listOv).getAbsolutePath();

        // Publication du document
        publishDocument(docPath);

        return true;
    }

    private void publishDocument(String docPath) throws IOException {
        // Publication du document

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(REApplication.DEFAULT_APPLICATION_CORVUS);
        docInfo.setDocumentTitle(getSession().getLabel("LISTE_DEBLOCAGE_OV_NOM_FICHIER"));
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(NUM_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);
    }

    private List<REOVDeblocageForListContainer> prepareDateToImpress(List<REDeblocageVersement> lst) {

        List<REOVDeblocageForListContainer> listOV = new ArrayList<REOVDeblocageForListContainer>();

        for (REDeblocageVersement dv : lst) {

            if (dv.getLigneDeblocage().getType().isNotCompensation()) {
                REOVDeblocageForListContainer con = new REOVDeblocageForListContainer();
                con.setMontant(dv.getMontant());

                try {
                    AdressePaiementLoader loader = new AdressePaiementLoader(getSession());
                    AdressePaiement adr = loader.searchAdressePaiement(dv.getLigneDeblocage()
                            .getIdTiersAdressePaiement(), dv.getLigneDeblocage().getIdApplicationAdressePaiement());

                    con.setAdressePaiement(getSession().getLabel("LISTE_DEBLOCAGE_OV_COMPTE") + " : "
                            + adr.getBanque().getCompte() + "\n" + getSession().getLabel("LISTE_DEBLOCAGE_OV_CLEARING")
                            + " : " + adr.getBanque().getClearing() + "\n" + adr.getBanque().getDesignation1() + "\n"
                            + adr.getBanque().getNpa() + " " + adr.getBanque().getLocalite());
                } catch (RuntimeException e) {
                    JadeLogger.error("", e);
                }

                con.setDesciption(dv.formatInformationCompteForListe());

                listOV.add(con);
            }
        }

        return listOV;
    }

    private File genererFileExcel(List<REOVDeblocageForListContainer> listOv) {

        Details detail = new Details();
        detail.add(getSession().getLabel("LISTE_DEBLOCAGE_OV_NUM_IMFOROM"), NUM_INFOROM);
        detail.newLigne();
        detail.add(getSession().getLabel("LISTE_DEBLOCAGE_OV_DATE_IMPRESSION"), Date.now().toString());
        detail.newLigne();
        detail.add(getSession().getLabel("LISTE_DEBLOCAGE_OV_NOM_LOT"), getForIdLot());
        detail.newLigne();

        SimpleOutputListBuilderJade simpleOutputListBuilderJade = SimpleOutputListBuilderJade.newInstance();

        File file = simpleOutputListBuilderJade.session(getSession())
                .outputNameAndAddPath(NUM_INFOROM + "_" + getSession().getLabel("LISTE_DEBLOCAGE_OV_NOM_FICHIER"))
                .globazTheme().headerLeftTop(NUM_INFOROM).addTranslater().addList(listOv)
                .classElementList(REOVDeblocageForListContainer.class)
                .addTitle(getSession().getLabel("LISTE_DEBLOCAGE_OV_TITRE_LISTE"), Align.CENTER)
                .addHeaderDetails(detail).asPdf().build();
        simpleOutputListBuilderJade.close();

        return file;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LISTE_DEBLOCAGE_OV_EMAIL_OBJECT_ERREUR");
        } else {
            return getSession().getLabel("LISTE_DEBLOCAGE_OV_EMAIL_OBJECT_SUCESS");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

}
