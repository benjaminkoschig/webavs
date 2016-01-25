package ch.globaz.al.businessimpl.services.rubriques.comptables;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;

public class RubriquesComptablesBMSServiceImplTest {
    @Test
    public void testForRubriqueIndependantNaissanceSuppl() {
        assertEquals("rubrique.multicaisse.650.203.independant.naissance.suppl",
                RubriquesComptablesBMSServiceImpl.getRubriqueFor(ALCSPrestation.BONI_DIRECT, "650.203",
                        ALCSDossier.ACTIVITE_INDEPENDANT, ALCSDroit.TYPE_NAIS, ALCSDossier.STATUT_IS, "12.12.2012"));
    }

    @Test
    public void testForIndependantNaissance() {
        assertEquals("rubrique.multicaisse.650.203.independant.naissance",
                RubriquesComptablesBMSServiceImpl.getRubriqueFor(ALCSPrestation.BONI_DIRECT, "650.203",
                        ALCSDossier.ACTIVITE_INDEPENDANT, ALCSDroit.TYPE_NAIS, ALCSDossier.STATUT_NP, "12.12.2012"));
    }

    @Test
    public void testForRubriqueRestitution() {
        assertEquals("rubrique.multicaisse.650.203.restitution", RubriquesComptablesBMSServiceImpl.getRubriqueFor(
                ALCSPrestation.BONI_RESTITUTION, "650.203", ALCSDossier.ACTIVITE_INDEPENDANT, ALCSDroit.TYPE_NAIS,
                ALCSDossier.STATUT_IS, "12.12.2012"));
    }
}
