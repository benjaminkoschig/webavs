package ch.globaz.perseus.businessimpl.utils;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.perseus.business.constantes.CSEtatCivil;
import ch.globaz.perseus.business.constantes.CSFormation;
import ch.globaz.perseus.business.constantes.CSPaysISO;
import ch.globaz.perseus.business.constantes.CSSexePersonne;
import ch.globaz.perseus.business.constantes.CSSituationActivite;
import ch.globaz.perseus.business.constantes.CSSourceEnfant;
import ch.globaz.perseus.business.constantes.CSStatutSejour;
import ch.globaz.perseus.business.constantes.CSTypeConjoint;

public class StatsOFS {

    public static String CODE_ISO_SUISSE = "CHE";
    public static String NE_SAIS_PAS = "-1";
    public static String NON = "2";
    public static String NOUVEAU_DOSSIER = "1";
    public static String OUI = "1";
    public static String REOUVERTURE_DOSSIER_APRES_SIX_MOIS = "2";
    public static String SOZIALLEISTUNGSTRAEGERID = "225285";

    public static String getNeSaisPasSiAucuneValeur(String value) {
        if (JadeStringUtil.isEmpty(value)) {
            value = StatsOFS.NE_SAIS_PAS;
        }

        return value;

    }

    public static String getOuiNon(String value) {
        String valueReturn = StatsOFS.OUI;

        if (JadeStringUtil.isEmpty(value) || "0.00".equals(value)) {
            valueReturn = StatsOFS.NON;
        }
        return valueReturn;
    }

    public static String getValueFromCodeSystem(String codeSystem) {
        String value = null;

        CSSexePersonne sexePersonne = CSSexePersonne.getEnumFromCodeSystem(codeSystem);
        if (null != sexePersonne) {
            value = sexePersonne.getValue();
        }

        CSEtatCivil etatCivil = CSEtatCivil.getEnumFromCodeSystem(codeSystem);
        if (null != etatCivil) {
            value = etatCivil.getValue();
        }

        CSStatutSejour statutSejour = CSStatutSejour.getEnumFromCodeSystem(codeSystem);
        if (null != statutSejour) {
            value = statutSejour.getValue();
        }

        CSTypeConjoint typeConjoint = CSTypeConjoint.getEnumFromCodeSystem(codeSystem);
        if (null != typeConjoint) {
            value = typeConjoint.getValue();
        }

        CSSourceEnfant sourceEnfant = CSSourceEnfant.getEnumFromCodeSystem(codeSystem);
        if (null != sourceEnfant) {
            value = sourceEnfant.getValue();
        }

        CSFormation formation = CSFormation.getEnumFromCodeSystem(codeSystem);
        if (null != formation) {
            value = formation.getValue();
        }

        CSPaysISO isoPays = CSPaysISO.getEnumFromCodeSystem(codeSystem);
        if (null != isoPays) {
            value = isoPays.getValue();
        }

        CSSituationActivite situationActivite = CSSituationActivite.getEnumFromCodeSystem(codeSystem);
        if (null != situationActivite) {
            value = situationActivite.getValue();
        }

        if (null == value) {
            value = StatsOFS.NE_SAIS_PAS;
        }

        return value;
    }

}
