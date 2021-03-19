package ch.globaz.pegasus.process.adaptationPrimeAssuranceMaladie.executeImport;

import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.*;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.primeassurancemaladie.PrimeAssuranceMaladieEnum;
import ch.globaz.pegasus.primeassurancemaladie.PrimeAssuranceMaladieFromCSV;
import ch.globaz.pegasus.process.adaptation.PCAdaptationUtils;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.exolab.castor.persist.ClassMolderHelper.LOG;

public class PCProcessImportationPrimeAssuranceMaladieStep implements JadeProcessStepInterface, JadeProcessStepHtmlCutomable,
        JadeProcessStepBeforable, JadeProcessStepCheckable, JadeProcessStepInit, JadeProcessStepInfoCurrentStep {

    private HashMap<String, PrimeAssuranceMaladieFromCSV> listePrimeAssuranceMaladieFromCSV= null;
    private boolean isAdaptationAnnuel = true;

    private static final String CSV_EXTENSION = ".csv";
    private String pathCsvToimport = "";
    private static final char SEPARATOR = ';';
    private JadeProcessExecut infoProcess;
    // Référence au nom dans le menu qui execute le process PEGASUSMenu.xml
    private static String NAME_PROCESS_LAMAL = "AdaptationPrimeLAmalPC";

    @Override
    public void checker(JadeProcessStep step, Map<Enum<?>, String> map) {
        String date = map.get(PCProcessAdapationEnum.DATE_ADAPTATION);
        pathCsvToimport = map.get(PCProcessAdapationEnum.PATH_CSV_TO_IMPORT);
        if (JadeStringUtil.isEmpty(date)) {
            JadeThread.logError("", "pegasus.process.adaptation.dateAdaptation.mandatory");
        }
        if (JadeStringUtil.isEmpty(pathCsvToimport)) {
            JadeThread.logError("", "pegasus.process.adaptation.pathCsvToImport.mandatory");
        }
    }

    @Override
    public String customHtml(JadeProcessStep step, Map<Enum<?>, String> map) {
        StringBuilder action = new StringBuilder();
        if (step.getKeyStep().equals(infoProcess.getCurrentStep().getKeyStep())) {
            action.append(PCAdaptationUtils.createHtmlForButtonList(step));
            // Est utilisé dans le js pour afficher une autre popup si l'on est sur cette étape
            action.append("<div id='isStepImportPrime' value='true' style='display:none'></div>");
        } else {
            action.append("<div id='isStepImportPrime' value='false' style='display:none'></div>");
        }
        return action.toString();
    }


    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        // S'il s'agit du process des primes LAMal, le comportement du process devra être différent.
        if (infoProcess !=null && this.infoProcess.getSimpleExecutionProcess().getName().contains(NAME_PROCESS_LAMAL)){
            isAdaptationAnnuel = false;
        }
        return new PCProcessImportationPrimeAssuranceMaladieHandler(listePrimeAssuranceMaladieFromCSV, isAdaptationAnnuel);
    }


    private void loadListePrimeAssuranceMaladieFromCSV() {
        if (listePrimeAssuranceMaladieFromCSV == null) {
            listePrimeAssuranceMaladieFromCSV = new HashMap<>();
            importFiles();
        }
    }

    /**
     * Traitement des fichiers d'import de prime d'assurance Maladie
     */
    private void importFiles() throws TechnicalException {
        try {

            if (!JadeStringUtil.isBlankOrZero(pathCsvToimport)) {
                importFile(pathCsvToimport);

            } else {
                LOG.warn("Le chemin du CSV est non défini");
                throw new TechnicalException("PCProcessImportationPrimeAssuranceMaladieStep#import : Chemin CSV non définie !");
            }
        } catch (Exception e) {
            LOG.error("PCProcessImportationPrimeAssuranceMaladieStep#import : erreur lors de l'importation des fichiers", e);
            throw new TechnicalException("PCProcessImportationPrimeAssuranceMaladieStep#import : erreur lors de l'importation " + e.getMessage());
        }

    }

    private void importFile(String nomFichierCsvDistant) throws IOException, JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException {
        if (nomFichierCsvDistant.endsWith(CSV_EXTENSION)) {
            // Traitement du fichier CSV
            String fileName = JadeFsFacade.readFile(nomFichierCsvDistant);
            BufferedReader reader = null;
            FileInputStream fileinputStream = null;
            InputStreamReader inputStreamReader = null;
            try {
                fileinputStream = new FileInputStream(fileName);
                inputStreamReader = new InputStreamReader(fileinputStream);
                reader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = reader.readLine()) != null) {

                    // Regex des NSS pour s'assurer sur la ligne du CSV commence bien par un NSS.
                    Pattern patternRegexNss = Pattern.compile("[0-9]{1,3}.[0-9]{1,4}.[0-9]{1,4}.[0-9]{1,2}");
                    Pattern patternRegexNssWithoutDot = Pattern.compile("[0-9]{13}");
                    Matcher matcherRegexNss = patternRegexNss.matcher(line) ;
                    Matcher matcherRegexNssWithoutDot = patternRegexNssWithoutDot.matcher(line) ;
                    boolean isLineStartWithNSS = matcherRegexNss.lookingAt() ;
                    boolean isLineStartWithNSSWithoutDot = matcherRegexNssWithoutDot.lookingAt() ;

                    if (isLineStartWithNSS || isLineStartWithNSSWithoutDot) {
                        // On extrait les éléments de la ligne
                        PrimeAssuranceMaladieFromCSV assuranceMaladie = extractDataFromCSV(line, isLineStartWithNSSWithoutDot);
                        listePrimeAssuranceMaladieFromCSV.put(assuranceMaladie.getNss(), assuranceMaladie);
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (fileinputStream != null) {
                    fileinputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            }
        }
    }

    private PrimeAssuranceMaladieFromCSV extractDataFromCSV(String line, boolean isLineStartWithNSSWithoutDot) {
        PrimeAssuranceMaladieFromCSV assuranceMaladieFromCSV = new PrimeAssuranceMaladieFromCSV();

        String regex = Character.toString(SEPARATOR);
        // Suppression des espaces de fin de ligne
        line = line.trim();

        // Split des données
        String[] datasTemp = line.split(regex);
        for (int i = 0; i < datasTemp.length; i++) {
            PrimeAssuranceMaladieEnum enumPrimeAssu = PrimeAssuranceMaladieEnum.fromIndex(String.valueOf(i));
            if (Objects.nonNull(enumPrimeAssu)) {
                switch (enumPrimeAssu) {
                    case NSS:
                        if (isLineStartWithNSSWithoutDot) {
                            assuranceMaladieFromCSV.setNss(reformatNSSWithDot(datasTemp[i]));
                        } else {
                            assuranceMaladieFromCSV.setNss(datasTemp[i]);
                        }
                        break;
                    case NOM:
                        assuranceMaladieFromCSV.setNom(datasTemp[i]);
                        break;
                    case PRENOM:
                        assuranceMaladieFromCSV.setPrenom(datasTemp[i]);
                        break;
                    case DATE_NAISSANCE:
                        assuranceMaladieFromCSV.setDateNaissance(datasTemp[i]);
                        break;
                    case MONTANT:
                        assuranceMaladieFromCSV.setMontant(datasTemp[i]);
                        break;
                    case NOM_CAISSE:
                        assuranceMaladieFromCSV.setNomCaisse(datasTemp[i]);
                        break;
                    default:
                        break;
                }
            }
        }
        return assuranceMaladieFromCSV;
    }

    private String reformatNSSWithDot(String nssWithoutDot) {
        StringBuilder nssWithDot = new StringBuilder(nssWithoutDot);
        nssWithDot.insert(3, ".");
        nssWithDot.insert(8, ".");
        nssWithDot.insert(13, ".");
        return nssWithDot.toString();
    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException, JadePersistenceException {
        this.pathCsvToimport = map.get(PCProcessAdapationEnum.PATH_CSV_TO_IMPORT);
    }

    @Override
    public void init(JadeProcessExecut jadeProcessExecut) {
        this.pathCsvToimport = jadeProcessExecut.getProperties().get(PCProcessAdapationEnum.PATH_CSV_TO_IMPORT);
        loadListePrimeAssuranceMaladieFromCSV();
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut infoProcess) {
        this.infoProcess = infoProcess;
    }
}
