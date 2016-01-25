package ch.globaz.pegasus.factory.droit;

import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

public class SimpleVersionDroitFactory {

    public static final String ID_VERSION_DROIT = "1512";

    private static SimpleVersionDroit generate(String noVersion, String idVersionDroit) {
        SimpleVersionDroit droit = new SimpleVersionDroit();
        droit.setCsEtatDroit("");
        droit.setCsMotif("");
        droit.setDateAnnonce("");
        droit.setId(idVersionDroit);
        droit.setIdDroit("");
        droit.setNoVersion(noVersion);
        return droit;
    }

    public static SimpleVersionDroit generateVersion2() {
        SimpleVersionDroit droit = SimpleVersionDroitFactory.generate("2", SimpleVersionDroitFactory.ID_VERSION_DROIT);
        return droit;
    }

    public static SimpleVersionDroit generateVersionInitial() {
        SimpleVersionDroit droit = SimpleVersionDroitFactory.generate("1", SimpleVersionDroitFactory.ID_VERSION_DROIT);
        return droit;
    }
}
