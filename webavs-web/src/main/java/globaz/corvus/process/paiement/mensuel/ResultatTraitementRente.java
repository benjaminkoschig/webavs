package globaz.corvus.process.paiement.mensuel;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente le résultat du traitement d'une rente par le paiement principale des rentes
 * 
 * @author lga
 * 
 */
public class ResultatTraitementRente {
    private String idRenteAccordee;
    private boolean result;
    private List<String> errors;

    /**
     * @param result
     * @param errors
     */
    public ResultatTraitementRente(String idRenteAccordee, boolean result, String error) {
        this.idRenteAccordee = idRenteAccordee;
        this.result = result;
        errors = new ArrayList<String>();
        if (!JadeStringUtil.isBlank(error)) {
            errors.add(error);
        }
    }

    /**
     * @param result
     * @param errors
     */
    public ResultatTraitementRente(String idRenteAccordee, boolean result, List<String> errors) {
        this.idRenteAccordee = idRenteAccordee;
        this.result = result;
        if (errors == null) {
            this.errors = new ArrayList<String>();
        } else {
            this.errors = errors;
        }
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public boolean getResult() {
        return result;
    }

    public List<String> getErrors() {
        return errors;
    }

}
