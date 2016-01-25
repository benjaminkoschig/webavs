package globaz.prestation.acor.xml;

import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRFichierACORPrinter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui adapte un document xml pour qu'il puisse etre inscrit ligne par ligne dans un fichier de commande.
 * </p>
 * 
 * @author vre
 */
public class PRDocumentAdapter implements PRFichierACORPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private PRAbstractXMLAdapter adapter;
    private BufferedReader document;
    private boolean forcerFichierVide;
    private String ligne;
    private String nomFichier;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe PRDocumentAdapter.
     * 
     * @param adapter
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     * @param forcerFichierVide
     *            DOCUMENT ME!
     */
    PRDocumentAdapter(PRAbstractXMLAdapter adapter, String nomFichier, boolean forcerFichierVide) {
        this.adapter = adapter;
        this.nomFichier = nomFichier;
        this.forcerFichierVide = forcerFichierVide;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * transforme un document xml en memoire en.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     */
    private BufferedReader buildDocument() throws PRACORException {
        Document document = adapter.buildDocument();

        try {
            // transformer le document en texte
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter output = new StringWriter();

            transformer.transform(new DOMSource(document), new StreamResult(output));

            // creer un reader pour la sortie ligne a ligne du document
            return new BufferedReader(new StringReader(output.toString()));
        } catch (Exception e) {
            throw new PRACORException("impossible de transformer le document xml de configuration de ACOR", e);
        }
    }

    /** 
     */
    @Override
    public void dispose() {
        // casse le cycle de references reciproques adapteur-fichier et permet
        // de garbage-collecter le tout
        adapter = null;
    }

    /**
     * getter pour l'attribut nom fichier.
     * 
     * @return la valeur courante de l'attribut nom fichier
     */
    @Override
    public String getNomFichier() {
        return nomFichier;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        if (document == null) {
            document = buildDocument();
        }

        if (ligne == null) {
            try {
                ligne = document.readLine();
            } catch (IOException e) {
                throw new PRACORException(
                        "impossible de lire la ligne suivante du document xml de configuration de ACOR", e);
            }
        }

        return ligne != null;
    }

    /**
     * getter pour l'attribut forcer fichier vide.
     * 
     * @return la valeur courante de l'attribut forcer fichier vide
     */
    @Override
    public boolean isForcerFichierVide() {
        return forcerFichierVide;
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public void printLigne(PrintWriter writer) throws PRACORException {
        writer.print(ligne);
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        cmd.append(ligne);

    }
}
