package globaz.osiris.stack.rules;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (06.09.2002 09:28:13)
 * 
 * @author: S�bastien Chappatte
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
     * M�thode � impl�menter pour d�crire l'action de cet objet.
     */
    @Override
    protected void exec() {
        m_stack.pop();
    }
}