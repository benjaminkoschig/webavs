package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleFactoryException;
import globaz.globall.db.BSession;
import java.lang.reflect.InvocationTargetException;

/**
 * Factory pour l'instantiation des r�gles de validation des plausibilit�s des annonces RAPG
 * 
 * @author dde
 * @author lga
 */
public class RulesFactory {

    /**
     * Retourne une instance de Rule correspondant au code pass� en argument
     * 
     * @param code
     *            Le code correspondant � la r�gle qui doit �tre retourn�
     * @return Une instance de la r�gle de validation correspondant au ode fournit en argument
     * @throws APRuleFactoryException
     *             En cas d'erreur d'instanciation de la r�gle
     */
    public static Rule getRule(String code, BSession session) throws APRuleFactoryException {
        String ruleClass = Rule.class.getName() + code;
        Rule rule = null;
        try {
            rule = (Rule) Class.forName(ruleClass).getDeclaredConstructors()[0].newInstance(code);
            rule.setSession(session);
        } catch (IllegalArgumentException e) {
            throw new APRuleFactoryException("IllegalArgumentException thrown on instanciation of Rule [" + ruleClass
                    + "]");
        } catch (SecurityException e) {
            throw new APRuleFactoryException("SecurityException thrown on instanciation of Rule [" + ruleClass + "]");
        } catch (InstantiationException e) {
            throw new APRuleFactoryException("InstantiationException thrown on instanciation of Rule [" + ruleClass
                    + "]");
        } catch (IllegalAccessException e) {
            throw new APRuleFactoryException("IllegalAccessException thrown on instanciation of Rule [" + ruleClass
                    + "]");
        } catch (InvocationTargetException e) {
            throw new APRuleFactoryException("InvocationTargetException thrown on instanciation of Rule [" + ruleClass
                    + "]");
        } catch (ClassNotFoundException e) {
            throw new APRuleFactoryException("ClassNotFoundException thrown on instanciation of Rule [" + ruleClass
                    + "]");
        }
        return rule;
    }
}
