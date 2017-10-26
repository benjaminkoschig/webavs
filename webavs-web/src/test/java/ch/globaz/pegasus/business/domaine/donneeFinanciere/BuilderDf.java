package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import java.text.SimpleDateFormat;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class BuilderDf {
    public static SimpleDateFormat formater = new SimpleDateFormat("MM.yyyy");
    public static SimpleDateFormat ddsMMsYYYY = new SimpleDateFormat("dd.MM.yyyy");

    public static DonneeFinanciere createDF(RoleMembreFamille roleMembreFamille) {
        return new DonneeFinanciereHeader(roleMembreFamille, new Date("01.2014"), null, "20", "1");
    }

    public static DonneeFinanciere createDF(Date debut, Date fin) {
        return new DonneeFinanciereHeader(RoleMembreFamille.REQUERANT, debut, fin, "20", "1");
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
        return new DonneeFinanciereHeader(RoleMembreFamille.REQUERANT, new Date("01.2014"), null, "20", "1");
    }
}
