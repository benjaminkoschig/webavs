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
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.pyxis.loader.PaysLoader;

class MutationParser {
    private static final Logger LOG = LoggerFactory.getLogger(MutationParser.class);

    private static final String VALIDE = "1";
    private static final String HOMME = "1";
    private static final String FEMME = "2";
    private final Map<Exception, String> errors = new HashMap<Exception, String>();

    static Mutation parse(String line) {

        Mutation mutation = new Mutation();
        CsvLineParser csvLineParser = new CsvLineParser(line);
        mutation.setNss(csvLineParser.nextElementToNssFormate());
        mutation.setNewNss(csvLineParser.nextElementToNssFormate());
        mutation.setTypeMutation(TypeMutation.parse(csvLineParser.nextElementTrim()));
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
        mutation.setDateNaissance(csvLineParser.nextElementToDateRente("dateNaissance"));
        mutation.setCodeNationalite(csvLineParser.nextElementTrim());

        mutation.setSourceDonnees(csvLineParser.nextElementTrim());
        mutation.setDateDece(csvLineParser.nextElementToDateRente("dateDece"));
        String codeEtatCivile = csvLineParser.nextElementTrim();
        String codeRaison = csvLineParser.nextElementTrim();
        mutation.setEtatCivil(resovleEtatCivil(codeEtatCivile, codeRaison));

        mutation.setRaisonDuPartenariatDissous(codeRaison);
        mutation.setDateChangementEtatCivil(csvLineParser.nextElementToDateRente("dateChangementEtatCivil"));
        return mutation;
    }

    public static EtatCivil resovleEtatCivil(String codeEtatCivile, String codeRaison) {
        if (codeEtatCivile != null) {
            if ("1".equals(codeEtatCivile)) {
                return EtatCivil.CELIBATAIRE;
            } else if ("2".equals(codeEtatCivile)) {
                return EtatCivil.MARIE;
            } else if ("3".equals(codeEtatCivile)) {
                return EtatCivil.VEUF;
            } else if ("4".equals(codeEtatCivile)) {
                return EtatCivil.DIVORCE;
            } else if ("5".equals(codeEtatCivile)) {
                return EtatCivil.CELIBATAIRE; // a défaut de non marié
            } else if ("6".equals(codeEtatCivile)) {
                return EtatCivil.LPART;
            } else if ("7".equals(codeEtatCivile)) {
                // Les autres cas sont traités par dissous.
                if ("4".equals(codeRaison)) {
                    return EtatCivil.LPART_DIS_DECES;
                }
                return EtatCivil.LPART_DISSOUT;
            }
        }
        return EtatCivil.UNDEFINED;
    }

    public MutationsContainer parsFile(String pahtFile, PaysLoader paysLoader) {
        final MutationsContainer list = new MutationsContainer(paysLoader);
        BufferedReader fileBuffered = null;
        try {
            LOG.debug("Reade file {}", pahtFile);
            fileBuffered = new BufferedReader(new FileReader(pahtFile), 81920);
            String line = "";
            while ((line = fileBuffered.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        Mutation mutation = parse(line);
                        list.add(mutation);
                    } catch (Exception e) {
                        errors.put(e, line);
                    }
                }
            }
            LOG.debug("Nb mutation found {}", list.size());
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

    public Map<Exception, String> getErrors() {
        return errors;
    }
}
