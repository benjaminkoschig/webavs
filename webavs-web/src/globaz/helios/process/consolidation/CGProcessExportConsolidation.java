package globaz.helios.process.consolidation;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGBilanListViewBean;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGComptePertesProfitsListViewBean;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.parser.CGBilanLine;
import globaz.helios.parser.CGBilanParser;
import globaz.helios.parser.CGComptePertesProfitsLine;
import globaz.helios.parser.CGComptePertesProfitsParser;
import globaz.helios.process.consolidation.utils.CGExportConsolidationXmlParser;
import globaz.helios.process.consolidation.utils.CGProcessConsolidationUtil;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.xml.JadeXmlWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

public class CGProcessExportConsolidation extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CGExerciceComptableViewBean exerciceComptable;
    private String idExerciceComptable;

    /**
     * Constructor for CGProcessExportConsolidation.
     */
    public CGProcessExportConsolidation() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessExportConsolidation.
     * 
     * @param parent
     */
    public CGProcessExportConsolidation(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessExportConsolidation.
     * 
     * @param session
     */
    public CGProcessExportConsolidation(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        CGExportConsolidationXmlParser xmlParser = new CGExportConsolidationXmlParser();
        xmlParser.addAttributAgenceToRoot(JadeStringUtil.fillWithZeroes(getNoAgence(), 3));
        xmlParser.addAttributCaisseToRoot(getNoCaisse());

        xmlParser.addAttributDateDebutToRoot(getExerciceComptable().getDateDebut());
        xmlParser.addAttributDateFinToRoot(getExerciceComptable().getDateFin());

        addCompteBilanToXml(xmlParser);

        this.registerAttachedDocument(exportToFile(xmlParser));

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

    }

    /**
     * Ajout des comptes de bilans pour les périodes 1-13 au fichier xml.
     * 
     * @param xmlParser
     * @throws Exception
     */
    private void addCompteBilanToXml(CGExportConsolidationXmlParser xmlParser) throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_ASC_AND_TYPE_ASC);
        manager.find();

        for (int i = 0; i < manager.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) manager.get(i);

            Element elementPeriode = xmlParser.addPeriode(periode);

            if (!periode.getCode().equals(CGPeriodeComptable.CS_CODE_CLOTURE)) {
                exportBilan(xmlParser, periode, elementPeriode);
                exportPertesProfits(xmlParser, periode, elementPeriode);
            }
        }
    }

    /**
     * Exportation des comptes de bilan.
     * 
     * @param xmlParser
     * @param periode
     * @param elementPeriode
     * @throws Exception
     */
    private void exportBilan(CGExportConsolidationXmlParser xmlParser, CGPeriodeComptable periode,
            Element elementPeriode) throws Exception {
        CGBilanListViewBean listManager = new CGBilanListViewBean();
        listManager.setSession(getSession());

        listManager.setForSoldeOpen(true);
        listManager.setForIdExerciceComptable(getIdExerciceComptable());
        listManager.setReqComptabilite(CodeSystem.CS_DEFINITIF);
        listManager.setInclurePeriodesPrec(new Boolean(false));
        listManager.setGroupIdCompteOfas(true);
        listManager.setReqDomaine(CGCompte.CS_COMPTE_TOUS);

        listManager.setReqForListPeriodesComptable(periode.getIdPeriodeComptable());

        listManager.find(BManager.SIZE_NOLIMIT);

        if (!listManager.hasErrors() && !listManager.isEmpty()) {
            ArrayList lines = CGBilanParser.getLinesToPrint(listManager, getExerciceComptable());

            for (int j = 0; j < lines.size(); j++) {
                CGBilanLine line = (CGBilanLine) lines.get(j);
                if ((line != null) && (!JadeStringUtil.isBlank(line.getIdExterne()))
                        && CGProcessConsolidationUtil.isIdExterneExportable(line.getIdExterne())) {
                    xmlParser.addCompteInformationsToPeriode(elementPeriode, line.getIdExterne(), line.getLibelle(),
                            line.getIdGenre(), line.getSoldeActif(), line.getSoldePassif(), line.getIdNature(),
                            line.getIdDomaine());
                }
            }
        }
    }

    private void exportPertesProfits(CGExportConsolidationXmlParser xmlParser, CGPeriodeComptable periode,
            Element elementPeriode) throws Exception {
        CGComptePertesProfitsListViewBean listManager = new CGComptePertesProfitsListViewBean();
        listManager.setSession(getSession());

        listManager.setForSoldeOpen(true);
        listManager.setForIdExerciceComptable(getIdExerciceComptable());
        listManager.setReqComptabilite(CodeSystem.CS_DEFINITIF);
        listManager.setInclurePeriodesPrec(new Boolean(false));
        listManager.setGroupIdCompteOfas(true);
        listManager.setReqDomaine(CGCompte.CS_COMPTE_TOUS);

        listManager.setReqForListPeriodesComptable(periode.getIdPeriodeComptable());

        listManager.find(BManager.SIZE_NOLIMIT);

        if (!listManager.hasErrors() && !listManager.isEmpty()) {
            ArrayList lines = CGComptePertesProfitsParser.getLinesToPrint(listManager, getExerciceComptable());

            for (int j = 0; j < lines.size(); j++) {
                CGComptePertesProfitsLine line = (CGComptePertesProfitsLine) lines.get(j);
                if ((line != null) && (!JadeStringUtil.isBlank(line.getIdExterne()))
                        && CGProcessConsolidationUtil.isIdExterneExportable(line.getIdExterne())) {
                    xmlParser.addCompteInformationsToPeriode(elementPeriode, line.getIdExterne(), line.getLibelle(),
                            line.getIdGenre(), line.getSoldeCharges(), line.getSoldeProduits(), line.getIdNature(),
                            line.getIdDomaine());
                }
            }
        }
    }

    /**
     * Export le Document xml vers un fichier.
     * 
     * @param xmlParser
     * @return
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws TransformerException
     */
    private String exportToFile(CGExportConsolidationXmlParser xmlParser) throws UnsupportedEncodingException,
            FileNotFoundException, IOException, TransformerException {
        String destination = Jade.getInstance().getHomeDir() + "/" + CGApplication.APPLICATION_HELIOS_REP
                + "/work/consolidationOut" + System.currentTimeMillis() + ".xml";
        Writer out = new OutputStreamWriter(new FileOutputStream(destination), "ISO-8859-1");
        out.write(JadeXmlWriter.asXML(xmlParser.getDocument()));
        out.close();

        return destination;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("CONSOLIDATION_EXPORT_ERROR");
        } else {
            return getSession().getLabel("CONSOLIDATION_EXPORT_OK");
        }
    }

    /**
     * Retourne l'exercice comptable en cours.
     * 
     * @return
     * @throws Exception
     */
    private CGExerciceComptableViewBean getExerciceComptable() throws Exception {
        if (exerciceComptable == null) {
            exerciceComptable = CGProcessConsolidationUtil.getExerciceComptable(getSession(), getTransaction(),
                    getIdExerciceComptable());
        }

        return exerciceComptable;
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Retourne le no d'agence de l'application.
     * 
     * @return
     */
    public String getNoAgence() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getExerciceComptable().getMandat().getNoCaisse())) {
                return getExerciceComptable().getMandat().getNoAgence();
            } else {
                CGApplication application = (CGApplication) GlobazServer.getCurrentSystem().getApplication(
                        CGApplication.DEFAULT_APPLICATION_HELIOS);
                return CaisseHelperFactory.getInstance().getNoAgence(application);
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le no de caisse de l'application.
     * 
     * @return
     */
    public String getNoCaisse() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getExerciceComptable().getMandat().getNoCaisse())) {
                return getExerciceComptable().getMandat().getNoCaisse();
            } else {
                CGApplication application = (CGApplication) GlobazServer.getCurrentSystem().getApplication(
                        CGApplication.DEFAULT_APPLICATION_HELIOS);
                return CaisseHelperFactory.getInstance().getNoCaisse(application);
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

}
