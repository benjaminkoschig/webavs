package ch.globaz.pegasus.factory.pca;

import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

public class SimplePCAccordeeFactory {
    public static SimplePCAccordee generate(String dateDebut, String dateFin, String idVersionDroit) {
        SimplePCAccordee accordee = new SimplePCAccordee();
        accordee.setDateDebut(dateDebut);
        accordee.setDateFin(dateFin);
        accordee.setIdVersionDroit(idVersionDroit);
        return accordee;
    }
}
