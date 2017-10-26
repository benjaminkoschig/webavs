package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.droit.EtatDroit;
import ch.globaz.pegasus.business.domaine.droit.MotifDroit;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.rpc.domaine.RpcData;

public class RpcDataTest {

    @Test
    public void testIsMotifDroitSuppressionWithVersionDroit() throws Exception {
        RpcData data = new RpcData(new VersionDroit("", 1, EtatDroit.VALIDE, MotifDroit.DECES), new Dossier(),
                new Demande());
        assertThat(data.isMotifDroitSuppression()).isTrue();
        data = new RpcData(new VersionDroit("", 1, EtatDroit.VALIDE, MotifDroit.ADAPTATION_HOME), new Dossier(),
                new Demande());
        assertThat(data.isMotifDroitSuppression()).isFalse();

    }

    @Test
    public void testIsMotifDroitSuppressionWitOutVersionDroit() throws Exception {
        RpcData data = new RpcData(new Dossier(), new Demande());
        assertThat(data.isMotifDroitSuppression()).isFalse();
    }
}
