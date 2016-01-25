package globaz.naos.process.nbrAssures;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.utils.CEUtils;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.exceptions.AFTechnicalException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Process de génération de la liste du nombre d'assurés par affiliation paritaire
 * 
 * @author sco
 */
public class AFImpressionListeNbrAssuresByAffiliationProcess extends BProcess {

    private static final long serialVersionUID = -7816542441362010939L;
    private String forAnnee;
    private String fromNumAffilie;
    private String toNumAffilie;
    private String forIdAssurance;
    private static final String NOM_FICHIER = "listeNbrAssuresByAffiliation";
    private static final String SEPARATOR = ";";
    private static final String NUM_INFOROM = "0309CAF";

    private List<String> lignes = new ArrayList<String>();

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean status = true;

        try {

            // 1. Récupération et préparation des données
            createData();

            // 2. Impression des données
            createImpression();

        } catch (Exception e) {
            JadeLogger.error(this, e);

            this._addError(getTransaction(), getSession().getLabel("NAOS_JAVA_IMPRESSION_LISTE_NBR_ASSURES_ERROR"));

            String messageInformation = "Assurance id : " + getForIdAssurance() + "\n";
            messageInformation += "Annee : " + getForAnnee() + "\n";
            messageInformation += "Num affilie from : " + getFromNumAffilie() + "\n";
            messageInformation += "Num affilie to : " + getToNumAffilie() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    /**
     * Permet la création du fichier csv
     * 
     * @throws IOException
     */
    private void createImpression() throws IOException {

        // Si aucune donnée, on le précise
        if (lignes.size() == 0) {
            getMemoryLog().logMessage(getSession().getLabel("NAOS_JAVA_IMPRESSION_LISTE_NBR_ASSURES_AUCUNE_DONNEE"),
                    FWMessage.INFORMATION, this.getClass().getName());
            return;
        }

        // Ecriture du fichier
        String filePath = createFile();

        // Renseignement du doc info
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setDocumentTypeNumber(NUM_INFOROM);

        // Attachement du document
        this.registerAttachedDocument(docInfo, filePath);
    }

    private String createFile() {

        FileOutputStream out = null;
        File f = null;
        try {
            f = File.createTempFile(NOM_FICHIER, ".csv");
            f.deleteOnExit();
            out = new FileOutputStream(f);
            write(out);
            return f.getAbsolutePath();
        } catch (IOException e) {
            throw new AFTechnicalException("Unabled to create the file : " + NOM_FICHIER, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new AFTechnicalException("Unabled to close the stream for file " + NOM_FICHIER, e);
            }
        }
    }

    /**
     * REcherche des donées en base et consolidation en vue d'une impression.
     */
    private void createData() {

        // Récupération des données
        AFNbrAssuresManager manager = new AFNbrAssuresManager();
        manager.setForAnnee(getForAnnee());
        manager.setFromNumAffilie(getFromNumAffilie());
        manager.setToNumAffilie(getToNumAffilie());
        manager.setForIdAssurance(getForIdAssurance());
        manager.setSession(getSession());
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new AFTechnicalException("Unabled to retrieve data for the the manager 'AFNbrAssuresManager'", e);
        }

        // Ajout entête du fichier csv
        if (manager.size() > 0) {
            StringBuilder str = new StringBuilder();
            str.append(getSession().getLabel("NAOS_JAVA_COLONNE_NUM_AFFILIE")).append(SEPARATOR);
            str.append(getSession().getLabel("NAOS_JAVA_COLONNE_NOM")).append(SEPARATOR);
            str.append(getSession().getLabel("NAOS_JAVA_COLONNE_ASSURANCE")).append(SEPARATOR);
            str.append(getSession().getLabel("NAOS_JAVA_COLONNE_ANNEE")).append(SEPARATOR);
            str.append(getSession().getLabel("NAOS_JAVA_COLONNE_NBR_ASSURES"));

            lignes.add(str.toString());
        }

        // Formatage des données pour être sous la forme csv
        for (int i = 0; i < manager.size(); i++) {
            AFNbrAssures nbrAssures = (AFNbrAssures) manager.getEntity(i);
            StringBuilder ligne = new StringBuilder();
            ligne.append(nbrAssures.getNumAffilie()).append(SEPARATOR);
            ligne.append(nbrAssures.getNomTiers() + " " + nbrAssures.getPrenomTiers()).append(SEPARATOR);
            ligne.append(nbrAssures.getLibelleAssurance()).append(SEPARATOR);
            ligne.append(nbrAssures.getAnnee()).append(SEPARATOR);
            ligne.append(nbrAssures.getNbrAssures());
            // ligne.append("\n");

            lignes.add(ligne.toString());
        }

    }

    private void write(FileOutputStream out) {

        PrintStream ps = new PrintStream(out);

        for (Iterator<?> it = lignes.iterator(); it.hasNext();) {
            ps.println((String) it.next());
        }

        ps.close();

    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("NAOS_JAVA_MAIL_IMPRESSION_LISTE_NBR_ASSURES_ERROR");
        } else {
            return getSession().getLabel("NAOS_JAVA_MAIL_IMPRESSION_LISTE_NBR_ASSURES");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @return the annee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @param annee the annee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @return the fromNumAffilie
     */
    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @param fromNumAffilie the fromNumAffilie to set
     */
    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    /**
     * @return the toNumAffilie
     */
    public String getToNumAffilie() {
        return toNumAffilie;
    }

    /**
     * @param toNumAffilie the toNumAffilie to set
     */
    public void setToNumAffilie(String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

    /**
     * @return the forIdAssurance
     */
    public String getForIdAssurance() {
        return forIdAssurance;
    }

    /**
     * @param forIdAssurance the forIdAssurance to set
     */
    public void setForIdAssurance(String forIdAssurance) {
        this.forIdAssurance = forIdAssurance;
    }

}
