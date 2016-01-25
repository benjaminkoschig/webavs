package ch.globaz.al.utils;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererDefaultStringAdapter;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeSimpleModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Classe m�re pour tous les cas de test. D�finit les m�thode <code>setUp</code> et <code>tearDown</code>
 * 
 * @author jts
 * 
 */
public abstract class ALTestCaseJU4 {

    /**
     * V�rifie si deux instances d'un mod�le est identique
     * 
     * @param ignore
     *            liste de m�thode � ignorer
     * @param model
     *            l'instance r�f�rence
     * @param copie
     *            l'instance � valider
     * 
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected void checkModelCopie(ArrayList<String> ignore, JadeSimpleModel model, JadeSimpleModel copie)
            throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {

        // v�rifier si on a bien deux instances et pas une r�f�rence
        Assert.assertNotSame(model, copie);

        String modelClassNam = model.getClass().getName();
        Method[] methods = Class.forName(modelClassNam).getMethods();

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (modelClassNam.equals(method.getDeclaringClass().getName()) && (method.getName().indexOf("get") == 0)
                    && !ignore.contains(method.getName())) {
                Assert.assertEquals(method.invoke(model, new Object[] {}), method.invoke(copie, new Object[] {}));
            }
        }
    }

    /**
     * M�thode ex�cutant les traitements devant obligatoirement �tre effectu�s � la fin d'un test
     */
    protected void doFinally() {
        printJadeBusinessMessage();

        // Pas de modification en DB
        try {
            JadeThread.rollbackSession();
        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail(e1.toString());
        }
    }

    /**
     * M�thode initialisant un mod�le en remplissant chaque champ avec une valeur fictive. A utiliser pour le test de
     * services de copie
     * 
     * @param modelClassName
     *            Nom de la classe � instancier
     * @return l'instance
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected JadeSimpleModel initTestModel(String modelClassName) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, InvocationTargetException {

        Method[] methods = Class.forName(modelClassName).getMethods();
        JadeSimpleModel model = (JadeSimpleModel) Class.forName(modelClassName).newInstance();

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            if (modelClassName.equals(method.getDeclaringClass().getName()) && (method.getName().indexOf("set") == 0)) {

                Class<?>[] params = method.getParameterTypes();

                if ("java.lang.String".equals(params[0].getName())) {
                    method.invoke(model, new Object[] { "test" });
                } else if ("java.lang.Boolean".equals(params[0].getName())) {
                    method.invoke(model, new Object[] { true });
                } else {
                    Assert.fail("Not supported argument type");
                }
            }
        }
        return model;
    }

    protected boolean printJadeBusinessMessage() {
        if (JadeBusinessMessageLevels.ERROR == JadeThread.logMaxLevel()) {
            System.out.println(new JadeBusinessMessageRendererDefaultStringAdapter().render(JadeThread.logMessages(),
                    JadeThread.currentLanguage()));
            JadeThread.logClear();
            return true;
        } else {
            return false;
        }

    }

    @Before
    public void setUp() throws Exception {
        JadeThreadActivator.startUsingJdbcContext(this, ContextProvider.getContext());
    }

    @After
    public void tearDown() {
        JadeThreadActivator.stopUsingContext(this);
    }

    /**
     * Met � jour un param�tre de l'application AF
     * 
     * @param parmName
     *            Nom du param�tre � modifier
     * @param value
     *            nouvelle valeur
     * @param date
     *            date pour laquelle rechercher le param�tre
     * @throws Exception
     *             Exception lev�e si le param�tre n'a pas �t� touv�, si plusieurs cas sont possible ou si une erreur
     *             s'est produite pendant la mise � jour
     */
    protected void updateParamater(String parmName, String value, String date) throws Exception {

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, parmName, date);

        param.setValeurAlphaParametre(value);
        param.setValeurNumParametre(value);
        JadePersistenceManager.update(param);
    }
}
