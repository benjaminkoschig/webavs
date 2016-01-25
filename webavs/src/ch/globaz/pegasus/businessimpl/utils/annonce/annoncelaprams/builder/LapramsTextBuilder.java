package ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.builder;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCHomes;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.models.annonce.AnnonceLaprams;
import ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.model.LapramsDataMediator;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class LapramsTextBuilder implements ILapramsBuilder {

    /**
     * Classe utilitaire pour construire une ligne/enregistrement LAPRAMS
     * 
     * @author ECO
     */
    private static class LineBuilder {
        private final static String BLANK = " ";
        private final static String BLANK_ZERO = "0";

        /**
         * Taille fixe de la ligne. Les trous sont complétés par des espaces BLANK
         */
        private static final int LINE_LENGTH = 421; // 390;

        private StringBuffer sb = new StringBuffer(LineBuilder.LINE_LENGTH);

        public LineBuilder() {
            super();
        }

        public void add(String sequence) {
            if (sequence == null) {
                sequence = "";
            }
            sb.append(sequence);
        }

        public void add(String sequence, int size) {
            String val = sequence;
            if (sequence != null) {
                val = sequence.trim();
            }
            if (JadeStringUtil.isEmpty(val)) {
                addBlank(size);
            } else {
                this.add(JadeStringUtil.leftJustify(val, size));
            }
        }

        public void addAvs(String noAvs) {
            if (noAvs == null) {
                addBlank(13);
            } else {
                this.add(noAvs.replaceAll("\\.", ""));
            }
        }

        public void addBlank(int size) {
            for (int i = 0; i < size; i++) {
                sb.append(LineBuilder.BLANK);
            }
        }

        public void addBlankZero(int size) {
            for (int i = 0; i < size; i++) {
                sb.append(LineBuilder.BLANK_ZERO);
            }
        }

        public void addDate(String globazDate, boolean isDateEnd) {
            if (JadeStringUtil.isEmpty(globazDate)) {
                addBlank(8);
            } else {
                // detecte si c'est une date MM.AAAA, et convertit la date en 01.MM.AAAA
                if (globazDate.length() == 7) {
                    globazDate = "01." + globazDate;
                }

                if (isDateEnd) {
                    globazDate = JadeDateUtil.addMonths(globazDate, 1);
                    globazDate = JadeDateUtil.addDays(globazDate, -1);
                }

                // format date de JJ.MM.AAAA en AAAAMMJJ
                String dateFormatte = globazDate.substring(6) + globazDate.substring(3, 5) + globazDate.substring(0, 2);
                this.add(dateFormatte);
            }
        }

        public void addDateEnd(String globazDate) {
            addDate(globazDate, true);
        }

        public void addDateStart(String globazDate) {
            addDate(globazDate, false);
        }

        /**
         * Ajoute un champ montant au buffer, au format sans décimale. En cas de nombre invalide, un blanc est ajouté à
         * la place.
         * 
         * @param number
         *            champ à ajouter. Peut être null ou vide.
         * @param size
         *            taille du champ
         */
        public void addNumberAmount(String number, int size) {
            if (JadeNumericUtil.isNumeric(number)) {
                // on convertit le montant de type "xx.00" en "xx", sans les décimales.
                int montant = (int) Float.parseFloat(number);
                this.add(JadeStringUtil.rightJustifyInteger(String.valueOf(montant), size));
            } else {
                addBlankZero(size);
            }
        }

        public void addWithZero(String sequence, int size) {
            if (JadeStringUtil.isEmpty(sequence)) {
                addBlank(size);
            } else {
                this.add(JadeStringUtil.rightJustifyInteger(sequence, size));
            }
        }

        public String toFormattedString() {
            addBlank(sb.capacity() - sb.length());
            sb.append("\r\n");
            return sb.toString();
        }
    }

    private enum OutputTypes {
        OUTPUT_SASH(0, "SASH", IPCHomes.CS_SERVICE_ETAT_SASH),
        OUTPUT_SPAS(1, "SPAS", IPCHomes.CS_SERVICE_ETAT_SPAS);

        String caption;
        String cs;
        int value;

        private OutputTypes(int val, String title, String cs) {
            value = val;
            caption = title;
            this.cs = cs;
        }
    }

    private static final int LONGUEUR_CHAMP_DF = 8;
    private static final int LONGUEUR_CHAMP_NPA = 6;
    private static final int LONGUEUR_CHAMP_OFS = 4;
    private static final int LONGUEUR_CHAMP_TEXT = 40;
    private static final String TYPE_ENREGISTREMENT_DONNEES_CONJOINT = "200";
    private static final String TYPE_ENREGISTREMENT_DONNEES_FINANCIERES_CONJOINT = "400";
    private static final String TYPE_ENREGISTREMENT_DONNEES_FINANCIERES_REQUERANT = "300";
    private static final String TYPE_ENREGISTREMENT_DONNEES_REQUERANT = "100";

    private String mailGest = null;
    private BProcess process = null;
    private int typeOutput;

    public StringBuffer[] build(LapramsDataMediator dataMediator) throws AnnonceException {

        StringBuffer[] outputs = new StringBuffer[2];
        outputs[OutputTypes.OUTPUT_SPAS.value] = new StringBuffer();
        outputs[OutputTypes.OUTPUT_SASH.value] = new StringBuffer();

        for (AnnonceLaprams annonce : dataMediator.getAnnonces()) {

            try {
                dataMediator.setDefaultAnnonce(annonce);
                // Lors que l'on créer une décision de suppression on en génére une seul on aura donc une seul
                // annonce. Ceci n'est pas juste pour les cas ou les 2 sont en home
                if (dataMediator.isDecisionDeSuppression()) {
                    // && dataMediator.isBothInHome()
                    if (dataMediator.isDecisionModifDece()) {
                        if (dataMediator.isRequerantDead()) {
                            writeAnnonce(dataMediator, outputs, annonce);
                        }
                        if (dataMediator.isBothInHome()) {
                            if (dataMediator.isConjointDead()) {
                                dataMediator.setDefaultAnnonceSuppressionForConjoint(annonce);
                                writeAnnonce(dataMediator, outputs, annonce);
                            }
                        }
                    } else {
                        writeAnnonce(dataMediator, outputs, annonce);
                        if (dataMediator.isBothInHome()) {
                            dataMediator.setDefaultAnnonceSuppressionForConjoint(annonce);
                            writeAnnonce(dataMediator, outputs, annonce);
                        }
                    }
                } else {
                    writeAnnonce(dataMediator, outputs, annonce);
                }
            } catch (Exception e) {
                e.printStackTrace();

                PersonneEtendueComplexModel personne = dataMediator.getPersonneEtendue(false);

                System.out.println("NSS:" + personne.getPersonneEtendue().getNumAvsActuel() + " idDecsion:"
                        + annonce.getSimpleDecisionHeader().getId() + ", idAnnonce:"
                        + annonce.getSimpleAnnonceLaprams().getId());
                throw new AnnonceException("ERROR in the annonce: (" + personne.getPersonneEtendue().getNumAvsActuel()
                        + " idDecsion:" + annonce.getSimpleDecisionHeader().getId() + ", idAnnonce:"
                        + annonce.getSimpleAnnonceLaprams().getId() + ")", e);
            }

        }

        return outputs;
    }

    @Override
    public void build(LapramsDataMediator dataMediator, BProcess process) throws AnnonceException,
            JadePersistenceException {

        if (process == null) {
            try {
                throw new Exception("LapramsTextBuilder: Pas de process!");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.process = process;

        StringBuffer[] outputs = this.build(dataMediator);

        try {
            writeFile(outputs[OutputTypes.OUTPUT_SPAS.value],
                    generateFilename(OutputTypes.OUTPUT_SPAS, dataMediator.getDateRapport()), dataMediator);
            writeFile(outputs[OutputTypes.OUTPUT_SASH.value],
                    generateFilename(OutputTypes.OUTPUT_SASH, dataMediator.getDateRapport()), dataMediator);
        } catch (IOException e) {

            throw new AnnonceException("I/O error ", e);

        }

    }

    private String generateFilename(OutputTypes outputType, String dateRapport) throws AnnonceException {
        try {
            Calendar cal = JadeDateUtil.getGlobazCalendar(dateRapport);
            int year = cal.get(Calendar.YEAR);
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            String city;
            city = EPCProperties.GESTION_ANNONCES_LAPRAMS_SUFFIXE.getValue();
            return "LAPRAMS_" + outputType.caption + "_" + year + "_" + week + "_" + city;
        } catch (Exception e) {
            throw new AnnonceException("An error happened while retrieving a property!", e);
        }
    }

    public String getMailGest() {
        return mailGest;
    }

    private JadePublishDocumentInfo getPubInfos() throws Exception {
        JadePublishDocumentInfo pubInfos = new PegasusPubInfoBuilder().publish().getPubInfo();

        if (typeOutput == OutputTypes.OUTPUT_SPAS.value) {
            pubInfos.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_LAPRAMS_SPAS);
            pubInfos.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_LAPRAMS_SPAS);

            pubInfos.setDocumentSubject(BSessionUtil.getSessionFromThreadContext().getLabel("ANNONCE_LAPRAMS_SPAS"));

        } else if (typeOutput == OutputTypes.OUTPUT_SASH.value) {
            pubInfos.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_LAPRAMS_SASH);
            pubInfos.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_LAPRAMS_SASH);

            pubInfos.setDocumentSubject(BSessionUtil.getSessionFromThreadContext().getLabel("ANNONCE_LAPRAMS_SASH"));
        }

        pubInfos.setOwnerEmail(mailGest);

        return pubInfos;

    }

    @Override
    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    private void writeAnnonce(LapramsDataMediator dataMediator, StringBuffer[] outputs, AnnonceLaprams annonce)
            throws AnnonceException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean hasConjoint = dataMediator.hasConjoint();
        // création du flux de sortie
        StringBuffer output = new StringBuffer();

        System.out.println(annonce.getPcAccordee().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());

        writeDonneesRequerant(output, dataMediator);
        if (hasConjoint) {
            writeDonneesConjoint(output, dataMediator);
        }
        if (!dataMediator.isDecisionDeSuppression()) {
            writeDonneesFinancieresRequerant(output, dataMediator);
            if (hasConjoint && dataMediator.isConjointDomicile()) {
                writeDonneesFinancieresConjoint(output, dataMediator);
            }
        } else {
            LineBuilder line = new LineBuilder();
            writeCle(dataMediator, line, LapramsTextBuilder.TYPE_ENREGISTREMENT_DONNEES_FINANCIERES_REQUERANT);
            line.addNumberAmount("", 176);
            writeDonneRepondant(dataMediator, line);

            if (hasConjoint && dataMediator.isConjointDomicile()) {
                writeCle(dataMediator, line, LapramsTextBuilder.TYPE_ENREGISTREMENT_DONNEES_FINANCIERES_CONJOINT);
                line.addNumberAmount("", 104);
            }

            output.append(line.toFormattedString());
        }

        // écriture du buffer
        if (OutputTypes.OUTPUT_SPAS.cs.equals(annonce.getSimpleAnnonceLaprams().getCsTypeHome())) {
            typeOutput = OutputTypes.OUTPUT_SPAS.value;
            outputs[typeOutput].append(output);

        } else {
            typeOutput = OutputTypes.OUTPUT_SASH.value;
            outputs[typeOutput].append(output);
        }
    }

    private void writeCle(LapramsDataMediator dataMediator, LineBuilder line, String typeEnregistrement)
            throws AnnonceException {
        line.addAvs(dataMediator.getNoAVSPC());
        line.add(typeEnregistrement);
        line.addDateStart(dataMediator.getDateDecisionPC());
        line.addDateStart(dataMediator.getDateDebutValidite());
        line.addDateEnd(dataMediator.getDateFinValidite());

        line.addDateStart(dataMediator.getDateDecompte());

    }

    private void writeDonneesConjoint(StringBuffer output, LapramsDataMediator dataMediator) throws AnnonceException {
        LineBuilder line = new LineBuilder();

        writeCle(dataMediator, line, LapramsTextBuilder.TYPE_ENREGISTREMENT_DONNEES_CONJOINT);
        // données conjoint
        line.addAvs(dataMediator.getNoAvs(true));
        line.add(dataMediator.getNom(true), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getPrenom(true), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getAutresPrenoms(true), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getSexe(true), 1);
        line.addDateStart(dataMediator.getDateNaissance(true));
        line.addDateStart(dataMediator.getDateDeces(true));
        line.add(dataMediator.getCodeEtatCivil(true), 2);
        line.addWithZero(dataMediator.getCodePaysOrigine(true), LapramsTextBuilder.LONGUEUR_CHAMP_OFS);
        line.addWithZero(dataMediator.getCodeCommuneOrigine(true), LapramsTextBuilder.LONGUEUR_CHAMP_OFS);
        line.add(dataMediator.getNomCommuneOrigine(true), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.addWithZero(dataMediator.getCodePermisSejour(true), 2);

        line.add(dataMediator.getRueDomicile(true), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getNumeroRueDomicile(true), 20);
        line.add(dataMediator.getLieuDit(true), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.addWithZero(dataMediator.getNoPostal(true), LapramsTextBuilder.LONGUEUR_CHAMP_NPA);
        line.add(dataMediator.getLieu(true), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);

        output.append(line.toFormattedString());

    }

    private void writeDonneesFinancieresConjoint(StringBuffer output, LapramsDataMediator dataMediator)
            throws AnnonceException {
        LineBuilder line = new LineBuilder();
        writeCle(dataMediator, line, LapramsTextBuilder.TYPE_ENREGISTREMENT_DONNEES_FINANCIERES_CONJOINT);

        line.addNumberAmount(dataMediator.getRevenuActivitesLucratives(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getRentesAVSAI(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getAutresRentes(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getRendementFortuneMobiliere(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getAutresRevenus(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getMontantPCDecision(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getValeurLocativeLogement(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getLoyerAnnuelReel(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getChargesAnnuellesReelles(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getDepensesPersonnellesAnnuelles(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getCotisationAVSAI(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getInteretHypoImmeuble(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getAutresDeductions(true), LapramsTextBuilder.LONGUEUR_CHAMP_DF);

        output.append(line.toFormattedString());
    }

    private void writeDonneesFinancieresRequerant(StringBuffer output, LapramsDataMediator dataMediator)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AnnonceException {
        LineBuilder line = new LineBuilder();

        writeCle(dataMediator, line, LapramsTextBuilder.TYPE_ENREGISTREMENT_DONNEES_FINANCIERES_REQUERANT);

        // données financieres annuelles requerant seul
        line.addNumberAmount(dataMediator.getFortuneMobiliere(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getBiensDessaisis(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getValeurFiscaleImmeubles(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getValeurVenaleImmeubles(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getDeductionLegaleImmeuble(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getDeductionDettesHypo(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getDeductionAutresDettes(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);

        line.addNumberAmount(dataMediator.getRevenuActivitesLucratives(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getRentesAVSAI(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getAutresRentes(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getRendementFortuneMobiliere(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getAutresRevenus(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);

        line.addNumberAmount(dataMediator.getMontantPCDecision(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);

        line.addNumberAmount(dataMediator.getValeurLocativeLogement(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);

        line.addNumberAmount(dataMediator.getLoyerAnnuelReel(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getChargesAnnuellesReelles(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getDepensesPersonnellesAnnuelles(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getComplementCantonalPourDepensesPersonnelles(false),
                LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getCotisationAVSAI(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getInteretHypoImmeuble(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);
        line.addNumberAmount(dataMediator.getAutresDeductions(false), LapramsTextBuilder.LONGUEUR_CHAMP_DF);

        line.addNumberAmount(dataMediator.getAllocationPourImpotent(), LapramsTextBuilder.LONGUEUR_CHAMP_DF);

        writeDonneRepondant(dataMediator, line);

        output.append(line.toFormattedString());

    }

    private void writeDonneesRequerant(StringBuffer output, LapramsDataMediator dataMediator) throws AnnonceException {
        LineBuilder line = new LineBuilder();

        // clé
        writeCle(dataMediator, line, LapramsTextBuilder.TYPE_ENREGISTREMENT_DONNEES_REQUERANT);

        // données requérant
        line.addAvs(dataMediator.getNoAvs(false));
        line.add(dataMediator.getNom(false), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getPrenom(false), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getAutresPrenoms(false), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getSexe(false), 1);
        line.addDateStart(dataMediator.getDateNaissance(false));
        line.addDateStart(dataMediator.getDateDeces(false));
        line.add(dataMediator.getCodeEtatCivil(false), 2);
        line.addWithZero(dataMediator.getCodePaysOrigine(false), LapramsTextBuilder.LONGUEUR_CHAMP_OFS);
        line.addWithZero(dataMediator.getCodeCommuneOrigine(false), LapramsTextBuilder.LONGUEUR_CHAMP_OFS);
        line.add(dataMediator.getNomCommuneOrigine(false), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.addWithZero(dataMediator.getCodePermisSejour(false), 2);
        line.add(dataMediator.getTypeCoupleRequerant(), 1);
        line.add(dataMediator.getRueDomicile(false), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getNumeroRueDomicile(false), 20);
        line.add(dataMediator.getLieuDit(false), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.addWithZero(dataMediator.getNoPostal(false), LapramsTextBuilder.LONGUEUR_CHAMP_NPA);
        line.add(dataMediator.getLieu(false), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getCodeEnfantsRequerant(), 1);

        // données séjour
        line.addWithZero(dataMediator.getNoEMSRequerant(), 5);
        line.addNumberAmount(dataMediator.getFraisSejourAnnuelsRequerant(), 8);
        line.addWithZero(dataMediator.getCodeTypeLitRequerant(), 1);
        line.addDateStart(dataMediator.getDateEntreeHomeRequerant());
        line.addDateEnd(dataMediator.getDateSortieHomeRequerant());
        line.addWithZero(dataMediator.getCodeDestinationSortieRequerant(), 2);
        line.addDateStart(dataMediator.getDateDebutSejourLitRequerant());

        output.append(line.toFormattedString());

    }

    private void writeDonneRepondant(LapramsDataMediator dataMediator, LineBuilder line) {
        line.add(dataMediator.getRepondantNom(), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getRepondantPrenom(), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getRepondantRue(), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.add(dataMediator.getRepondantRueNumero(), 20);
        line.add(dataMediator.getRepondantLieuDit(), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.addWithZero(dataMediator.getRepondantNpa(), LapramsTextBuilder.LONGUEUR_CHAMP_NPA);
        line.add(dataMediator.getRepondantLocalite(), LapramsTextBuilder.LONGUEUR_CHAMP_TEXT);
        line.addWithZero(dataMediator.getCodeLienRepondant(), 2);
    }

    private void writeEntete(StringBuffer output, LapramsDataMediator dataMediator) throws JadePersistenceException {
        LineBuilder line = new LineBuilder();
        line.addDateStart(dataMediator.getDateEnvoi());
        line.addWithZero(String.valueOf(dataMediator.getSequence()), 3);
        output.insert(0, line.toFormattedString());
    }

    private void writeFile(StringBuffer output, String filename, LapramsDataMediator dataMediator) throws IOException,
            AnnonceException, JadePersistenceException {
        // S'il n'y a pas de donnée pour le buffer actuel, inutile d'écrire
        if (output.length() == 0) {
            return;
        }
        writeEntete(output, dataMediator);

        String pathname = Jade.getInstance().getPersistenceDir() + filename + ".txt";
        File fo = new File(pathname);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fo));
        writer.append(output);
        writer.close();

        // envoi mail
        try {
            JadePublishDocumentInfo pubInfos = getPubInfos();

            process.registerAttachedDocument(pubInfos, pathname);

        } catch (Exception e) {
            throw new AnnonceException("An error happened while sending a LAPRAMS mail!", e);
        }

    }

}
