package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.globall.util.JAException;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.Ecriture;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.EcritureFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.SectionFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.SectionPegasus;

public class SectionRubriqueResolverTestCase {

    private Ecriture genertateEcritures(String id) {
        Ecriture e = new Ecriture();
        e.setSection(SectionPegasus.DECISION_PC);
        e.setCompteAnnexe(new CompteAnnexeSimpleModel());
        e.getCompteAnnexe().setIdCompteAnnexe(id);
        return e;
    }

    private SectionRubriqueResolver resolver() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = new SectionRubriqueResolver(new JournalSimpleModel(), "2013");
        SectionRubriqueResolver spy = Mockito.spy(resolver);

        APIRubrique api = new CARubrique();
        Mockito.doReturn(api).when(spy).findRubrique(Matchers.anyString());

        Mockito.doReturn(new SectionSimpleModel()).when(spy)
                .createSection(Matchers.any(CompteAnnexeSimpleModel.class), Matchers.anyString(), Matchers.anyString());
        return spy;
    }

    @Test
    public void testResloveRubrique() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();
        resolver.resloveRubrique("1");
        Mockito.verify(resolver, Mockito.times(1)).findRubrique(Matchers.anyString());
        resolver.resloveRubrique("1");
        Mockito.verify(resolver, Mockito.times(1)).findRubrique(Matchers.anyString());
        resolver.resloveRubrique("12");
        Mockito.verify(resolver, Mockito.times(2)).findRubrique(Matchers.anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResloveRubriqueError() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();
        resolver.resloveRubrique(null);
    }

    @Test
    public void testResloveSectionForDetteCredit() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();
        SectionSimpleModel section = SectionFactory.generateSection("120", "15821");
        resolver.resolveSection(EcritureFactory.generateEcrituresDetteCredit(150, section));
    }

    @Test
    public void testResloveSectionForDetteDebit() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();
        resolver.resolveSection(EcritureFactory.generateEcrituresDetteDebit(1, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
    }

    @Test
    public void testResolvedSectionEcritureIdChange() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();

        Ecriture e = genertateEcritures("2");
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(1)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());

        e = genertateEcritures("2");
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(1)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());

        e = genertateEcritures("21");
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(2)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void testResolvedSectionEcritureSectionChange() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();

        Ecriture e = genertateEcritures("2");
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(1)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());

        e = genertateEcritures("2");
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(1)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());

        e = genertateEcritures("2");
        e.setSection(SectionPegasus.RESTIUTION);
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(2)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolvedSectionErrorCompteAnnexeNull() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();
        Ecriture e = new Ecriture();
        e.setSection(SectionPegasus.RESTIUTION);
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(1)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolvedSectionErrorEcritureNull() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();
        resolver.resolveSection(null);
        Mockito.verify(resolver, Mockito.times(1)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolvedSectionErrorSectionNull() throws ComptabiliserLotException, JAException {
        SectionRubriqueResolver resolver = resolver();
        Ecriture e = new Ecriture();
        e.setCompteAnnexe(new CompteAnnexeSimpleModel());
        e.getCompteAnnexe().setIdCompteAnnexe("2");
        resolver.resolveSection(e);
        Mockito.verify(resolver, Mockito.times(1)).createSection(Matchers.any(CompteAnnexeSimpleModel.class),
                Matchers.anyString(), Matchers.anyString());

    }

    @Test
    public void testSectionRubriqueResolver() {
        new SectionRubriqueResolver(new JournalSimpleModel(), "2013");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSectionRubriqueResolverError() {
        new SectionRubriqueResolver(null, "2013");
    }

}
