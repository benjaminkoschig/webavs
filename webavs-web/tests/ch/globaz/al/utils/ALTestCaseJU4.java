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
 * Classe mère pour tous les cas de test. Définit les méthode <code>setUp</code> et <code>tearDown</code>
 * 
 * @author jts
 * 
 */
public abstract class ALTestCaseJU4 {

    /**
     * Vérifie si deux instances d'un modèle est identique
     * 
     * @param ignore
     *            liste de méthode à ignorer
     * @param model
     *            l'instance référence
     * @param copie
     *            l'instance à valider
     * 
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected void checkModelCopie(ArrayList<String> ignore, JadeSimpleModel model, JadeSimpleModel copie)
            throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {

        // vérifier si on a bien deux instances et pas une référence
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
     * Méthode exécutant les traitements devant obligatoirement être effectués à la fin d'un test
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
     * Méthode initialisant un modèle en remplissant chaque champ avec une valeur fictive. A utiliser pour le test de
     * services de copie
     * 
     * @param modelClassName
     *            Nom de la classe à instancier
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
     * Met à jour un paramètre de l'application AF
     * 
     * @param parmName
     *            Nom du paramètre à modifier
     * @param value
     *            nouvelle valeur
     * @param date
     *            date pour laquelle rechercher le paramètre
     * @throws Exception
     *             Exception levée si le paramètre n'a pas été touvé, si plusieurs cas sont possible ou si une erreur
     *             s'est produite pendant la mise à jour
     */
    protected void updateParamater(String parmName, String value, String date) throws Exception {

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, parmName, date);

        param.setValeurAlphaParametre(value);
        param.setValeurNumParametre(value);
        JadePersistenceManager.update(param);
    }
}
