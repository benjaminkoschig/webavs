/**
 * 
 */
package ch.globaz.amal.utils;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererDefaultStringAdapter;
import org.junit.Assert;

/**
 * Classe m�re de tous les tests. D�finit les m�thode <code>setUp</code> et <code>tearDown</code>
 * 
 * @author DHI
 * 
 */
public class AMTestCase {

    /**
     * M�thode ex�cutant les traitements devant obligatoirement �tre effectu�s � la fin d'un test
     */
    protected void doFinally() {
        // Toujours afficher s/ sortie system les logs du thread en cours
        System.out.println(new JadeBusinessMessageRendererDefaultStringAdapter().render(JadeThread.logMessages(),
                JadeThread.currentLanguage()));

        // Pas de modification en DB
        try {
            JadeThread.rollbackSession();
        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail(e1.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */

    protected void setUp() throws Exception {

        JadeThreadActivator.startUsingJdbcContext(this, AMContextProvider.getContext());
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */

    protected void tearDown() throws Exception {

        JadeThreadActivator.stopUsingContext(this);
    }

}
