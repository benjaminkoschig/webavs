package ch.globaz.vulpecula.process.comptabilite;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.comptabilite.importationcg.Csv2JaxbBuilder;
import ch.globaz.vulpecula.comptabilite.importationcg.MyProdisMyAccCsv;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.ImportEcritures;

/**
 * Process permettant de lancer le traitement de transformation d'un fichier CSV de compta MyProdis en un fichier xml
 * pour la compta générale (helios)
 * 
 * @since WebBMS 1.0
 */
public class ImportationCGProcess extends BProcess {
    private static final long serialVersionUID = -6431396237521702640L;

    private String csvFilename = "";
    private String journalLibelle = "";
    private String journalDate = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        List<MyProdisMyAccCsv> liste = Csv2JaxbBuilder.readCsv(csvFilename);

        // On trie par mandat
        Map<String, List<MyProdisMyAccCsv>> ecritureParMandat = new HashMap<String, List<MyProdisMyAccCsv>>();
        for (MyProdisMyAccCsv ecriture : liste) {
            if (isAborted()) {
                return false;
            }

            if (ecritureParMandat.containsKey(ecriture.getIdSociete())) {
                List<MyProdisMyAccCsv> listMandat = ecritureParMandat.get(ecriture.getIdSociete());
                listMandat.add(ecriture);
            } else {
                List<MyProdisMyAccCsv> listMandat = new ArrayList<MyProdisMyAccCsv>();
                listMandat.add(ecriture);
                ecritureParMandat.put(ecriture.getIdSociete(), listMandat);
            }
        }

        // On crée les fichiers xml par mandat
        for (String key : ecritureParMandat.keySet()) {
            if (isAborted()) {
                return false;
            }

            List<MyProdisMyAccCsv> listMandat = ecritureParMandat.get(key);

            // *** Treat data CSV ***
            ImportEcritures importEcriture = Csv2JaxbBuilder.buildXml(listMandat, journalLibelle, journalDate);

            // *** Create file ***
            String mandatName = key + "-" + MyProdisMyAccCsv.getMandat(key);
            String dstFilepathFinal = Jade.getInstance().getPersistenceDir() + mandatName + ".xml";
            Csv2JaxbBuilder.createXmlFile(importEcriture, dstFilepathFinal);
            JadePublishDocumentInfo docInfo = JadePublishDocumentInfoProvider.newInstance(this);
            docInfo.setDocumentSubject(getSubject() + " " + mandatName);
            registerAttachedDocument(docInfo, dstFilepathFinal);
        }

        return true;
    }

    @Override
    protected String getEMailObject() {
        return "Importation écritures MyProdis, mandat :";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @return the csvFilepath
     */
    public String getCsvFilename() {
        return csvFilename;
    }

    /**
     * @return the journalLibelle
     */
    public String getJournalLibelle() {
        return journalLibelle;
    }

    /**
     * @return the journalDate
     */
    public String getJournalDate() {
        return journalDate;
    }

    /**
     * @param csvFilepath the csvFilepath to set
     */
    public void setCsvFilename(String csvFilename) {
        this.csvFilename = csvFilename;
    }

    /**
     * @param journalLibelle the journalLibelle to set
     */
    public void setJournalLibelle(String journalLibelle) {
        this.journalLibelle = journalLibelle;
    }

    /**
     * @param journalDate the journalDate to set
     */
    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

}
