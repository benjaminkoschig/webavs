package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

public class OrdreVersementTypeResolverTestCase {

    private Map<String, String> getFieldsValues() throws IllegalAccessException {
        Field[] fields = IREOrdresVersements.class.getDeclaredFields();
        IREOrdresVersements ireOrdresVersements = new IREOrdresVersements() {
        };
        Map<String, String> map = new HashMap<String, String>();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                field.get(ireOrdresVersements);
                map.put(field.getName(), (String) field.get(ireOrdresVersements));
            }
        }
        return map;
    }

    private SimpleOrdreVersement newOv(String csType) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setCsType(csType);
        return ov;
    }

    @Test
    public void testINotsAllocationNoel() throws Exception {
        SimpleOrdreVersement ov = newOv(IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isAllocationNoel(ov));
    }

    @Test
    public void testIsAllocationNoel() throws Exception {
        SimpleOrdreVersement ov = newOv(IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isAllocationNoel(ov));
    }

    @Test
    public void testIsBeneficiarePrincipal() throws Exception {
        SimpleOrdreVersement ov = newOv(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isBeneficiarePrincipal(ov));
    }

    @Test
    public void testIsCreancier() throws Exception {
        SimpleOrdreVersement ov = newOv(IREOrdresVersements.CS_TYPE_TIERS);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isCreancier(ov));
        ov.setCsType(IREOrdresVersements.CS_TYPE_IMPOT_SOURCE);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isCreancier(ov));
        ov.setCsType(IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isCreancier(ov));
    }

    @Test
    public void testIsDette() throws Exception {
        SimpleOrdreVersement ov = newOv(IREOrdresVersements.CS_TYPE_DETTE);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isDette(ov));
    }

    @Test
    public void testIsJoursAppoint() throws Exception {
        SimpleOrdreVersement ov = newOv(IREOrdresVersements.CS_TYPE_JOURS_APPOINT);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isJoursAppoint(ov));
    }

    @Test
    public void testIsNotBeneficiarePrincipal() throws Exception {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        Map<String, String> map = getFieldsValues();
        for (Entry<String, String> o : map.entrySet()) {
            if (!IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(o.getValue())) {
                ov.setCsType(o.getValue());
                Assert.assertEquals(false, OrdreVersementTypeResolver.isBeneficiarePrincipal(ov));
            }
        }
    }

    @Test
    public void testIsNotCreancier() throws Exception {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        Map<String, String> map = getFieldsValues();
        for (Entry<String, String> o : map.entrySet()) {
            if (!IREOrdresVersements.CS_TYPE_TIERS.equals(o.getValue())
                    && !IREOrdresVersements.CS_TYPE_IMPOT_SOURCE.equals(o.getValue())
                    && !IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE.equals(o.getValue())) {
                ov.setCsType(o.getValue());
                Assert.assertEquals(false, OrdreVersementTypeResolver.isCreancier(ov));
            }
        }
    }

    @Test
    public void testIsNotDette() throws Exception {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        Map<String, String> map = getFieldsValues();
        for (Entry<String, String> o : map.entrySet()) {
            if (!IREOrdresVersements.CS_TYPE_DETTE.equals(o.getValue())) {
                ov.setCsType(o.getValue());
                Assert.assertEquals(false, OrdreVersementTypeResolver.isDette(ov));
            }
        }
    }

    @Test
    public void testIsNotJoursAppoint() throws Exception {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        Map<String, String> map = getFieldsValues();
        for (Entry<String, String> o : map.entrySet()) {
            if (!IREOrdresVersements.CS_TYPE_JOURS_APPOINT.equals(o.getValue())) {
                ov.setCsType(o.getValue());
                Assert.assertEquals(false, OrdreVersementTypeResolver.isJoursAppoint(ov));
            }
        }
    }

    @Test
    public void testIsNotRestitution() throws Exception {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        Map<String, String> map = getFieldsValues();
        for (Entry<String, String> o : map.entrySet()) {
            if (!IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(o.getValue())) {
                ov.setCsType(o.getValue());
                Assert.assertEquals(false, OrdreVersementTypeResolver.isRestitution(ov));
            }
        }
    }

    @Test
    public void testIsRestitution() throws Exception {
        SimpleOrdreVersement ov = newOv(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
        Assert.assertEquals(true, OrdreVersementTypeResolver.isRestitution(ov));
    }

}
