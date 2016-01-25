package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.annonce.SequenceException;
import ch.globaz.pegasus.business.models.annonce.SimpleSequence;
import ch.globaz.pegasus.business.models.annonce.SimpleSequenceSearch;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleSequenceService;

public class ResolveSequenceTestCase {

    private static String BUSINESS_KEY = "KEY";
    private static String DOMAINE = "DOMAINE";

    private int resolveSequence(SimpleSequenceService service) throws SequenceException, JadePersistenceException {
        ResolveSequence sequence = new ResolveSequence();
        return sequence.resolveSequence(ResolveSequenceTestCase.DOMAINE, ResolveSequenceTestCase.BUSINESS_KEY, service);
    }

    private SimpleSequenceService serviceHaseNoSequence() {

        SimpleSequenceService service = new SimpleSequenceService() {

            @Override
            public int count(SimpleSequenceSearch search) throws SequenceException, JadePersistenceException {
                return 0;
            }

            @Override
            public SimpleSequence create(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                simpleSequence.setId("1");
                return simpleSequence;
            }

            @Override
            public SimpleSequence delete(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                simpleSequence.setId("0");
                return simpleSequence;
            }

            @Override
            public List<SimpleSequence> find(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
                    JadePersistenceException {
                List<SimpleSequence> list = new ArrayList<SimpleSequence>();
                SimpleSequence simpleSequence = new SimpleSequence();
                simpleSequence.setId("1");
                simpleSequence.setSequence("1");
                list.add(simpleSequence);
                return list;
            }

            @Override
            public SimpleSequence read(String idSimpleSequence) throws SequenceException, JadePersistenceException {
                return null;
            }

            @Override
            public SimpleSequenceSearch search(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
                    JadePersistenceException {
                return null;
            }

            @Override
            public SimpleSequence update(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                return simpleSequence;
            }
        };
        return service;
    }

    private SimpleSequenceService serviceHaseSequence() {

        SimpleSequenceService service = new SimpleSequenceService() {

            @Override
            public int count(SimpleSequenceSearch search) throws SequenceException, JadePersistenceException {
                return 0;
            }

            @Override
            public SimpleSequence create(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                simpleSequence.setId("1");
                return simpleSequence;
            }

            @Override
            public SimpleSequence delete(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                simpleSequence.setId("0");
                return simpleSequence;
            }

            @Override
            public List<SimpleSequence> find(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
                    JadePersistenceException {
                List<SimpleSequence> list = new ArrayList<SimpleSequence>();
                SimpleSequence simpleSequence = new SimpleSequence();
                simpleSequence.setId("1");
                simpleSequence.setSequence("1");
                simpleSequence.setBusinessKey(ResolveSequenceTestCase.BUSINESS_KEY);
                list.add(simpleSequence);
                return list;
            }

            @Override
            public SimpleSequence read(String idSimpleSequence) throws SequenceException, JadePersistenceException {
                return null;
            }

            @Override
            public SimpleSequenceSearch search(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
                    JadePersistenceException {
                return null;
            }

            @Override
            public SimpleSequence update(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                return simpleSequence;
            }
        };
        return service;
    }

    private SimpleSequenceService serviceNewSequence() {
        SimpleSequenceService service = new SimpleSequenceService() {

            @Override
            public int count(SimpleSequenceSearch search) throws SequenceException, JadePersistenceException {
                return 0;
            }

            @Override
            public SimpleSequence create(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                simpleSequence.setId("2");
                return simpleSequence;
            }

            @Override
            public SimpleSequence delete(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                simpleSequence.setId("0");
                return simpleSequence;
            }

            @Override
            public List<SimpleSequence> find(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
                    JadePersistenceException {
                List<SimpleSequence> list = new ArrayList<SimpleSequence>();
                return list;
            }

            @Override
            public SimpleSequence read(String idSimpleSequence) throws SequenceException, JadePersistenceException {
                return null;
            }

            @Override
            public SimpleSequenceSearch search(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
                    JadePersistenceException {
                return null;
            }

            @Override
            public SimpleSequence update(SimpleSequence simpleSequence) throws SequenceException,
                    JadePersistenceException {
                return simpleSequence;
            }
        };
        return service;
    }

    @Test
    public void whenKeyMatchThenMustReturn1() throws Exception {
        SimpleSequenceService service = serviceHaseSequence();
        Assert.assertEquals(1, resolveSequence(service));
    }

    @Test
    public void whenKeyNotMatchThenMustReturn2() throws Exception {
        SimpleSequenceService service = serviceHaseNoSequence();
        Assert.assertEquals(2, resolveSequence(service));
    }

    @Test
    public void whenNoSequenceExistThenMustReturn1() throws Exception {
        SimpleSequenceService service = serviceNewSequence();
        Assert.assertEquals(1, resolveSequence(service));
    }

}
