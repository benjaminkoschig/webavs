package ch.globaz.corvus.process.dnra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pyxis.domaine.Sexe;

class MutationParser {
    private static final Logger LOG = LoggerFactory.getLogger(MutationParser.class);

    private static final String VALIDE = "1";
    private static final String HOMME = "1";
    private static final String FEMME = "2";

    static Mutation parse(String line) {

        Mutation mutation = new Mutation();
        CsvLineParser csvLineParser = new CsvLineParser(line);
        mutation.setNss(csvLineParser.nextElementToNssFormate());
        mutation.setNewNss(csvLineParser.nextElementToNssFormate());
        mutation.setCodeMutation(csvLineParser.nextElementTrim());
        mutation.setValide(VALIDE.equals(csvLineParser.nextElementTrim()));
        String nomPrenom = csvLineParser.nextElementTrim();
        if (!nomPrenom.isEmpty()) {
            String[] np = nomPrenom.replace("\"", "").split(",");
            if (np.length > 0) {
                mutation.setNom(np[0]);
            }
            if (np.length > 1) {
                mutation.setPrenom(np[1]);
            }
        }
        String sexe = csvLineParser.nextElementTrim();
        if (HOMME.equals(sexe)) {
            mutation.setSexe(Sexe.HOMME);
        } else if (FEMME.equals(sexe)) {
            mutation.setSexe(Sexe.FEMME);
        } else {
            mutation.setSexe(Sexe.UNDEFINDED);
        }
        mutation.setDateNaissance(csvLineParser.nextElementToDate("dateNaissance"));
        mutation.setCodeNationalite(csvLineParser.nextElementTrim());

        mutation.setSourceDonnees(csvLineParser.nextElementTrim());
        mutation.setDateDece(csvLineParser.nextElementToDate("dateDece"));
        mutation.setCodeEtatCivil(csvLineParser.nextElementTrim());
        mutation.setRaisonDuPartenariatDissous(csvLineParser.nextElementTrim());
        mutation.setDateChangementEtatCivil(csvLineParser.nextElementToDate("dateChangementEtatCivil"));
        return mutation;
    }

    public static MutationsContainer parsFile(String pahtFile) {
        final MutationsContainer list = new MutationsContainer();
        final Map<Exception, String> errors = new HashMap<Exception, String>();
        BufferedReader fileBuffered = null;
        try {
            LOG.debug("Reade file {}", pahtFile);
            fileBuffered = new BufferedReader(new FileReader(pahtFile), 81920);
            String line = "";
            int nbFiltred = 0;
            while ((line = fileBuffered.readLine()) != null) {

                try {
                    Mutation mutation = parse(line);
                    if (mutation.isValide()) {
                        list.add(mutation);
                    } else {
                        nbFiltred++;
                    }

                } catch (Exception e) {
                    errors.put(e, line);
                }
            }
            LOG.debug("Nb mutation found {}", list.size());
            LOG.debug("Nb mutation filtred {}", nbFiltred);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileBuffered != null) {
                IOUtils.closeQuietly(fileBuffered);
            }
            for (Entry<Exception, String> entry : errors.entrySet()) {
                LOG.error("Unalble to parse this line: {}", entry.getValue(), entry.getKey());
            }
        }
        return list;
    }
}
