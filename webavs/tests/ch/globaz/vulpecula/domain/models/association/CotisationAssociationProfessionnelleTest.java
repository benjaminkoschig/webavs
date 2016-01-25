package ch.globaz.vulpecula.domain.models.association;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class CotisationAssociationProfessionnelleTest {
    @Test
    public void groupByAssociationGenre_GivenTwoCotisationsWithSameGenreAndAssociation_ShouldReturn1Collection() {
        CotisationAssociationProfessionnelle cap1 = create("1", GenreCotisationAssociationProfessionnelle.MEMBRE);
        CotisationAssociationProfessionnelle cap2 = create("1", GenreCotisationAssociationProfessionnelle.MEMBRE);
        assertEquals(1, CotisationAssociationProfessionnelle.groupByAssociationGenre(Arrays.asList(cap1, cap2)).size());
    }

    @Test
    public void groupByAssociationGenre_GivenTwoCotisationsWithDifferentsGenreAndSameAssociation_ShouldReturn2Collection() {
        CotisationAssociationProfessionnelle cap1 = create("1", GenreCotisationAssociationProfessionnelle.MEMBRE);
        CotisationAssociationProfessionnelle cap2 = create("1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE);
        assertEquals(2, CotisationAssociationProfessionnelle.groupByAssociationGenre(Arrays.asList(cap1, cap2)).size());
    }

    @Test
    public void groupByAssociationGenre_GivenTwoCotisationsWithSameGenreAndDifferentAssociation_ShouldReturn2Collection() {
        CotisationAssociationProfessionnelle cap1 = create("1", GenreCotisationAssociationProfessionnelle.MEMBRE);
        CotisationAssociationProfessionnelle cap2 = create("2", GenreCotisationAssociationProfessionnelle.MEMBRE);
        assertEquals(2, CotisationAssociationProfessionnelle.groupByAssociationGenre(Arrays.asList(cap1, cap2)).size());
    }

    @Test
    public void groupByAssociationGenre_GivenTwoCotisationsWithDifferentGenreAndDifferentAssociation_ShouldReturn2Collection() {
        CotisationAssociationProfessionnelle cap1 = create("1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE);
        CotisationAssociationProfessionnelle cap2 = create("2", GenreCotisationAssociationProfessionnelle.MEMBRE);
        assertEquals(2, CotisationAssociationProfessionnelle.groupByAssociationGenre(Arrays.asList(cap1, cap2)).size());
    }

    private CotisationAssociationProfessionnelle create(String idAssociation,
            GenreCotisationAssociationProfessionnelle genre) {
        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = new CotisationAssociationProfessionnelle();
        Administration associationProfessionnelle = new Administration();
        associationProfessionnelle.setId(idAssociation);
        cotisationAssociationProfessionnelle.setGenre(genre);
        cotisationAssociationProfessionnelle.setAssociationProfessionnelle(associationProfessionnelle);
        return cotisationAssociationProfessionnelle;
    }
}
