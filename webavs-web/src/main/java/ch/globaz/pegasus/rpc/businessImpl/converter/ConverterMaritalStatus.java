package ch.globaz.pegasus.rpc.businessImpl.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pyxis.domaine.EtatCivil;

public class ConverterMaritalStatus {
    private static final String ECH_MARITAL_STATUS_CELIBATAIRE = "1";
    private static final String ECH_MARITAL_STATUS_MARIE = "2";
    private static final String ECH_MARITAL_STATUS_VEUF_VEUVE = "3";
    private static final String ECH_MARITAL_STATUS_DIVORCE = "4";
    private static final String ECH_MARITAL_STATUS_NON_MARIE = "5";
    private static final String ECH_MARITAL_STATUS_PARTENAIRE_ENREGISTRE = "6";
    private static final String ECH_MARITAL_STATUS_PARTENARIAT_DISSOUS = "7";
    private static final String ECH_MARITAL_STATUS_INCONNU = "9";
    private static final Logger LOG = LoggerFactory.getLogger(ConverterMaritalStatus.class);

    public static String convert(EtatCivil etatCivil) {
        switch (etatCivil) {

            case CELIBATAIRE:
                return ECH_MARITAL_STATUS_CELIBATAIRE;
            case MARIE:
                return ECH_MARITAL_STATUS_MARIE;
            case VEUF:
                return ECH_MARITAL_STATUS_VEUF_VEUVE;
            case DIVORCE:
                return ECH_MARITAL_STATUS_DIVORCE;
            case LPART:
                return ECH_MARITAL_STATUS_PARTENAIRE_ENREGISTRE;
            case LPART_DISSOUT:
                return ECH_MARITAL_STATUS_PARTENARIAT_DISSOUS;
            case LPART_DIS_DECES:
            case LPART_SEPARE_FAIT:
            case SEPARE:
            case SEPARE_DE_FAIT:
            case UNDEFINED:

            default:
                return ECH_MARITAL_STATUS_INCONNU;
        }
    }

}
