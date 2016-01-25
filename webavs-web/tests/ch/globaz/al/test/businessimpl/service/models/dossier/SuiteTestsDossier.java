package ch.globaz.al.test.businessimpl.service.models.dossier;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tests liés aux modèles de dossier
 * 
 * @author jts
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(value = { CommentaireModelServiceImplTest.class, CopieModelServiceImplTest.class,
        DossierModelServiceImplTest.class, DossierBusinessServiceImplTest.class })
public class SuiteTestsDossier {

}
