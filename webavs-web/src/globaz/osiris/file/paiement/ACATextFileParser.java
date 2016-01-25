package globaz.osiris.file.paiement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author oca Chargement des données de déclaration de salaire depuis un fichier plat
 */
public abstract class ACATextFileParser implements ICAFileParser {

    private BufferedReader br = null;
    private String codeIsoMonnaie = null;
    private FileInputStream fi = null;
    private boolean isReady = false;
    // structaure d'un élement a lire (une ligne)
    private String line = null; // doir être initaialisé a null !
    private int size = -1;

    private String source = null;

    protected void _init() throws Exception {

        try {
            if (!isReady) {
                fi = new FileInputStream(getSource());
                br = new BufferedReader(new InputStreamReader(fi));
                isReady = true;
            }
        } catch (Exception e) {
            // on catch l'exception pour pouvoir fermer correctement les fichier
            // au besoin
            close();

            // puis on transmet l'exception plus haut
            throw e;
        }

    }

    // package visibility, only the factory is suppose to call it.
    abstract void addField(TextField field);

    @Override
    public void close() {
        if (fi != null) {
            try {
                fi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (br != null) {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isReady = false;
    }

    /**
     * @return
     */
    @Override
    public String getCodeIsoMonnaie() {
        return codeIsoMonnaie;
    }

    /**
     * Returns the source.
     * 
     * @return String
     */
    @Override
    public String getSource() {
        return source;
    }

    @Override
    public boolean hasNext() throws Exception {
        if (!isReady) {
            _init();
        }

        if (line != null) {
            // la ligne a déjà été lue, mas pas encore été retourné par next();
            return true;
        } else {
            // lecture de la prochaine ligne, (mis en cache pour que le prochain
            // next() n'aie
            // pas besoin de relire cette ligne)
            line = br.readLine();
            while ("".equals(line)) {
                line = br.readLine();
            }
            if (line != null) {
                // le fichier n'est pas terminé, on a trouvé une ligne
                return true;
            }
        }
        // fin du fichier, on a pas trouvé de ligne a lire.
        close();
        return false;
    }

    protected String next() throws Exception {
        String tmpLine = null;
        if (!isReady) {
            _init();
        }
        try {
            // si il n'y a rien dans le cache, on lit dans le fichier
            if (line == null) {
                line = br.readLine();
                // saute les lignes vide
                while ("".equals(line)) {
                    line = br.readLine();
                }
            }
        } catch (Exception e) {
            // catch l'Exception pour pouvoir fermer le fichier si besoin
            close();
            // puis remonte l'Exception
            throw e;
        }

        tmpLine = line;
        // vide le cache pour lire de toute facon la ligne suivante
        line = null;

        return tmpLine;
    }

    /**
     * @param string
     */
    @Override
    public void setCodeIsoMonnaie(String string) {
        codeIsoMonnaie = string;
    }

    /**
     * Sets the source.
     * 
     * @param source
     *            The source to set
     */
    @Override
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * retourne le nombre de record contenu dans le fichier, -1 en cas de probleme
     */
    @Override
    public int size() throws Exception {
        try {
            if (size == -1) {
                size = 0;
                FileInputStream sfi = new FileInputStream(getSource());
                BufferedReader sbr = new BufferedReader(new InputStreamReader(sfi));
                while (sbr.readLine() != null) {
                    size++;
                }
                sfi.close();
                sbr.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            size = -1;
        }
        return size;

    }

}
