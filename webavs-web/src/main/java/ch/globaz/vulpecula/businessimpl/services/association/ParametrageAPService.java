package ch.globaz.vulpecula.businessimpl.services.association;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.domain.models.association.ModeleEntete;

public class ParametrageAPService {
    private List<ModeleEntete> modeles = new ArrayList<ModeleEntete>();

    public ParametrageAPService() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(
                    "ch/globaz/vulpecula/parametrage/parametrageAP.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(";");
                ModeleEntete modele = new ModeleEntete(splitted[0].trim(), splitted[1].trim(), splitted[2].trim(),
                        splitted[3].trim(), splitted[4].trim());
                modeles.add(modele);
            }
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
            }
        }
    }

    public List<ModeleEntete> getModeles() {
        return modeles;
    }

    public ModeleEntete findByIdAdministration(String idAssociationProfessionnelle) {
        for (ModeleEntete m : modeles) {
            if (m.getIdTiers().equals(idAssociationProfessionnelle)) {
                return m;
            }
        }
        return null;
    }

    public ModeleEntete findById(String id) {
        for (ModeleEntete m : modeles) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }
}
