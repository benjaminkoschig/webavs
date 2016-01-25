package globaz.osiris.stack.rules;

/**
 * Insérez la description du type ici. Date de création : (06.09.2002 09:28:13)
 * 
 * @author: Sébastien Chappatte
 */
public class Executable extends globaz.framework.utils.rules.FWExecutable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private globaz.framework.utils.urls.FWUrlsStack m_stack;

    /**
     * Commentaire relatif au constructeur Executable.
     */
    public Executable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();
        m_stack = stack;
    }

    /**
     * Méthode à implémenter pour décrire l'action de cet objet.
     */
    @Override
    protected void exec() {
        m_stack.pop();
    }
}