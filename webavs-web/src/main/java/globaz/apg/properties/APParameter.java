package globaz.apg.properties;

import ch.globaz.common.business.services.ParametreService;
import ch.globaz.common.businessimpl.CommonServiceLocator;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Type énuméré utilisé pour définir les paramètres FWPARP liés au module APG et MATERNITE
 *
 * @author lga
 */
public enum APParameter {

    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_0_ENFANT("MAXTXJO_0"),
    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_1_ENFANT("MAXTXJO_1"),
    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_2_ENFANT("MAXTXJO_2"),
    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_PLUS_DE_2_ENFANT("MAXTXJO_3"),
    NOMBRE_JOURS_ISOLES_DEMENAGEMENT("ISOLEDEMEN"),
    NOMBRE_JOURS_ISOLES_NAISSANCE("ISOLENAISS"),
    NOMBRE_JOURS_ISOLES_MARIAGE_LPART("ISOLEMARIA"),
    NOMBRE_JOURS_ISOLES_DECES("ISOLEDECES"),
    NOMBRE_JOURS_ISOLES_INSPECTION_RECRUTEMENT_LIBERATION("ISOLEDIVER"),
    NOMBRE_JOURS_ISOLES_CONGE_JEUNESSE("ISOLEJEUNE"),
    NOMBRE_JOURS_ISOLES_DECES_DEMI_JOUR("ISOLEDECED"),
    GARDE_PARENTAL_INDE_JOURS_MAX("PANGARDIND"),
    GARDE_PARENTAL_JOURS_SANS_IDEMNISATION("PANGARDJOU"),
    QUARANTAINE_JOURS_SANS_INDEMISATION("PANQUARJOU"),
    INDEPENDANT_JOURS_SANS_INDEMISATION("PANINDEJOU"),
    QUARANTAINE_JOURS_MAX("PANQUARNJO"),
    MANIFESTATION_INDERDITE_DATE_MINI("PANMANIDAT"),
    FERMETURE_EMTREPRISE_DATE_MINI("PANFERMDAT"),
    REVENU_INDEPENDANT_MIN("PANINREVMI"),
    REVENU_INDEPENDANT_MAX("PANINREVMA"),
    INDEPENDANT_PERTE_DE_GAIN_MAX("PANINDDEBU"),
    SALARIE_EVENEMENTIEL_DATE("PANEVENDAT"),
    SALARIE_EVENEMENTIEL_REVENU_MIN("PANEVENEMI"),
    SALARIE_EVENEMENTIEL_REVENU_MAX("PANEVENEMA"),
    DIRECTIVE_NOVEMBRE_2020("PANNOVDATE"),
    DIRECTIVE_JANVIER_2021("PANVULDATD"),
    DIRECTIVE_JANVIER_2021_FIN("PANVULDATF"),
    PATERNITE("PATERNDATE"),
    PATERNITE_JOUR_MAX("PATJOURMAX"),
    PATERNITE_MOIS_MAX("PATMOISMAX"),
    MATERNITE_EXT_JOUR_MIN("MATEXTMIN"),
    MATERNITE_EXT_JOUR_MAX("MATEXTMAX"),
    PROCHE_AIDANT_DATE_DE_DEBUT("PROAIDDATE"),
    PROCHE_AIDANT_JOUR_MAX("PAIJOURMAX", Integer.class),
    PROCHE_AIDANT_MOIS_MAX("PAIMOISMAX", Integer.class);

    private final String parameterName;
    private final ParametreService parametreService = CommonServiceLocator.getParametreService();
    private final Class<?> type;

    APParameter(String parameterName) {
        this(parameterName, null);
    }

    APParameter(final String parameterName, final Class<?> type) {
        this.parameterName = parameterName;
        this.type = type;
    }

    /**
     * Retourne le nom du paramètre
     *
     * @return le nom du paramètre
     */
    public String getParameterName() {
        return parameterName;
    }

    @SuppressWarnings("unchecked")
    public <T> T findValue(String date, BSession session) {
        if (Integer.class.equals(this.type)) {
            return (T) parametreService.getInteger(() -> this.parameterName, date, session);
        }
        throw new CommonTechnicalException("The type is not yet implemented");
    }

    public <T> T findValueOrWithDateNow(String date, BSession session) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            date = Dates.nowFormatSwiss();
        }
        return this.findValue(date, session);
    }
}
