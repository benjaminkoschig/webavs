package ch.globaz.utils.tests.load;

import static junit.framework.Assert.*;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.globall.db.BEntity;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.naos.db.affiliation.AFAffiliation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.hera.business.models.famille.DateNaissanceConjoint;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.utils.tests.Persistence;

public class JsonDumpFilesProviderTest {

    @Ignore
    @Test
    public void testAllResultReturn() {
        Map<Persistence, Map<String, List<?>>> dataMap = JsonDumpFilesProvider.getAllDatasFind();

        assertNotNull(dataMap.get(Persistence.NEW));
        assertNotNull(dataMap.get(Persistence.OLD));

        System.out.println(dataMap.get(Persistence.NEW).size() + " dump trouvées pour la nouvelle persistence");
        if (dataMap.get(Persistence.NEW).size() > 0) {

            for (String modelName : dataMap.get(Persistence.NEW).keySet()) {
                System.out.println(modelName);
                List<JadeAbstractModel> modelsList = (List<JadeAbstractModel>) dataMap.get(Persistence.NEW).get(
                        modelName);
                assertNotNull(modelsList);

                for (JadeAbstractModel model : modelsList) {
                    assertNotNull(model.getId());
                    assertNotNull(model.getSpy());
                }

            }
        }

        System.out.println(dataMap.get(Persistence.OLD).size() + " dump trouvées pour l'ancienne persistence");
        if (dataMap.get(Persistence.OLD).size() > 0) {

            for (String modelName : dataMap.get(Persistence.OLD).keySet()) {
                System.out.println(modelName);
                List<BEntity> modelsList = (List<BEntity>) dataMap.get(Persistence.OLD).get(modelName);
                assertNotNull(modelsList);

                for (BEntity model : modelsList) {
                    assertNotNull(model.getId());
                    assertNotNull(model.getSpy());
                }

            }
        }

    }

    /**
     * Necessite un fichier globaz.corvus.db.annones.ReAnnonceRente.json
     */
    @Ignore
    @Test
    public void testCompletGenerationEtRecuperation() {

        ArrayList<?> liste = (ArrayList<?>) JsonDumpFilesProvider
                .getListeForModele("globaz.corvus.db.annonces.REAnnonceRente");
        assertEquals(liste.size(), 10);

        REAnnonceRente annonce = (REAnnonceRente) liste.get(0);
        assertNotNull(annonce);

    }

    @Ignore
    @Test
    public void testGetListeForModele() {

        // neccessite les fichiers json DateNaissanceConjoint, VersionDroit, AFAfilliation
        ArrayList<?> liste = (ArrayList<?>) JsonDumpFilesProvider
                .getListeForModele("ch.globaz.pegasus.business.models.droit.VersionDroit");
        assertEquals(liste.size(), 100);

        liste = (ArrayList<?>) JsonDumpFilesProvider
                .getListeForModele("ch.globaz.hera.business.models.famille.DateNaissanceConjoint");
        assertEquals(liste.size(), 100);

        liste = (ArrayList<?>) JsonDumpFilesProvider.getListeForModele("globaz.naos.db.affiliation.AFAffiliation");
        assertEquals(liste.size(), 100);
    }

    @SuppressWarnings("unchecked")
    @Ignore
    @Test
    public void testModelType() {
        // neccessite les fichiers json DateNaissanceConjoint, VersionDroit, AFAfilliation
        List<VersionDroit> liste = (ArrayList<VersionDroit>) JsonDumpFilesProvider
                .getListeForModele("ch.globaz.pegasus.business.models.droit.VersionDroit");
        assertEquals(liste.size(), 100);

        VersionDroit version = liste.get(0);
        assertNotNull(version);

        ArrayList<DateNaissanceConjoint> listeDateNaissanceConjoint = (ArrayList<DateNaissanceConjoint>) JsonDumpFilesProvider
                .getListeForModele("ch.globaz.hera.business.models.famille.DateNaissanceConjoint");
        assertEquals(listeDateNaissanceConjoint.size(), 100);

        DateNaissanceConjoint conjoint = listeDateNaissanceConjoint.get(0);
        assertNotNull(conjoint);

        ArrayList<AFAffiliation> listeAF = (ArrayList<AFAffiliation>) JsonDumpFilesProvider
                .getListeForModele("globaz.naos.db.affiliation.AFAffiliation");
        assertEquals(listeAF.size(), 100);

        AFAffiliation affiliation = listeAF.get(0);
        assertNotNull(affiliation);

    }
}
