package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import java.text.SimpleDateFormat;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class BuilderDf {
    public static SimpleDateFormat formater = new SimpleDateFormat("MM.yyyy");
    public static SimpleDateFormat ddsMMsYYYY = new SimpleDateFormat("dd.MM.yyyy");

    public static DonneeFinanciere createDF(RoleMembreFamille roleMembreFamille) {

        DonneeFinanciere df = null;
        Date date = new Date("01.2014");

        df = new DonneeFinanciereHeader(roleMembreFamille, date, null, "20");

        return df;
    }

    public static DonneeFinanciere createDfRequerant() {
        return createDF(RoleMembreFamille.REQUERANT);
    }

    public static DonneeFinanciere createDfEnfant() {
        return createDF(RoleMembreFamille.ENFANT);
    }

    public static DonneeFinanciere createDfConjoin() {
        return createDF(RoleMembreFamille.CONJOINT);
    }

    public static DonneeFinanciere createDF() {

        DonneeFinanciere df = null;

        Date date = new Date("01.2014");
        df = new DonneeFinanciereHeader(RoleMembreFamille.REQUERANT, date, null, "20");

        return df;
    }
}
