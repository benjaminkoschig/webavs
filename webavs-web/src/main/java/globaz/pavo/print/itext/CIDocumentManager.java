package globaz.pavo.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import java.text.MessageFormat;
import java.util.Iterator;

public abstract class CIDocumentManager extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";

    /**
     * remplace dans message {n} par args[n].
     * <p>
     * Evite que {@link MessageFormat} ne lance une erreur ou ne se comporte pas correctement si le message contient des
     * apostrophes.
     * </p>
     * <p>
     * En attendant qu'elle soit dans le framework.
     * </p>
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }

        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
    }

    // code système du catalogue de texte pour le domaine (même code système
    // pour tous les pdf)
    protected String cs_domaine = "329000";

    // code système du catalogue de texte pour le type de document (un code
    // système par pdf)
    protected String cs_typeDocument = "";
    protected ICTDocument document;
    protected String idDocument = "";

    // private String langue = getSession().getIdLangueISO();
    private String langue = "";
    protected String nomDocument = "";

    // Champs pour le catalogue de textes
    protected int numDocument = 0;

    public CIDocumentManager() {
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @param fileName
     *            Le nom du fichier
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CIDocumentManager(BProcess parent, String fileName) throws FWIException {
        super(parent, CIApplication.APPLICATION_PAVO_REP, fileName);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @param fileName
     *            Le nom du fichier
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CIDocumentManager(BSession parent, String fileName) throws FWIException {
        super(parent, CIApplication.APPLICATION_PAVO_REP, fileName);
    }

    /**
     * @return La langue du document
     */
    protected String _getLangue() {
        return langue.toUpperCase();
    }

    protected void _setLangue(String langue) {
        this.langue = langue;
    }

    /**
     * Regroupe tous les textes du même niveau en les séparant par la chaine de caractères passée en paramètre.
     * 
     * @param niveau
     *            du catalogue de texte
     * @param out
     *            buffer regroupant les textes du même niveau
     * @param paraSep
     *            Chaine de séparation entre les positions du niveau.
     */
    protected void dumpNiveau(int niveau, StringBuffer out, String paraSep) {
        try {
            for (Iterator paraIter = loadCatalogue().getTextes(niveau).iterator(); paraIter.hasNext();) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }

                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.append(TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    /**
     * remplace dans message {n} par args[n].
     * <p>
     * Evite que {@link MessageFormat} ne lance une erreur ou ne se comporte pas correctement si le message contient des
     * apostrophes.
     * </p>
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    protected String formatMessage(StringBuffer message, Object[] args) {
        return formatMessage(message.toString(), args);
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getNomDocument() {
        return nomDocument;
    }

    protected StringBuffer getTexte(int niveau, int position) {
        StringBuffer resString = new StringBuffer("");
        try {
            // if (document == null) {
            // getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"),
            // FWMessage.ERREUR, "");
            // } else {
            ICTListeTextes listeTextes = loadCatalogue().getTextes(niveau);
            resString.append(listeTextes.getTexte(position));
            // }
        } catch (Exception e3) {
            getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                    getSession().getLabel("ERROR_GETTING_LIST_TEXT") + niveau + ":" + position);
        }
        return resString;
    }

    public String getTypeDocument() {
        return cs_typeDocument;
    }

    /**
     * retourne le catalogue de texte pour le document courant.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected ICTDocument loadCatalogue() throws Exception {
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

        loader.setActif(Boolean.TRUE);
        if (!JadeStringUtil.isBlank(getNomDocument())) {
            loader.setNom(getNomDocument());
        } else if (!JadeStringUtil.isBlank(getIdDocument())) {
            loader.setIdDocument(getIdDocument());
        } else {
            loader.setDefault(Boolean.TRUE);
        }

        loader.setCodeIsoLangue(langue);
        loader.setCsDomaine(cs_domaine);
        loader.setCsTypeDocument(getTypeDocument());

        ICTDocument[] candidats = loader.load();

        document = candidats[numDocument];
        return document;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public void setTypeDocument(String typeDocument) {
        cs_typeDocument = typeDocument;
    }

}
