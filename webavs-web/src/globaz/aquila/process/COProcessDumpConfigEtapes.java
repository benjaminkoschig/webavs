package globaz.aquila.process;

import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COEtapeInfoConfigManager;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.db.access.batch.COSequenceManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * Envoie un mail contenant une partie des options de configuration d'Aquila sous forme de requêtes SQL.
 * </p>
 * 
 * @author vre
 */
public class COProcessDumpConfigEtapes extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -5038197217738815055L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean baseDuplique;
    private int counter;
    private String csSequenceBase = "";
    private Boolean includeDelete;
    private List infoConfigs = new LinkedList();

    private Map oldIdEtapeToEtape = new HashMap();
    private Boolean recomputeIndexes;
    private String schema;
    private List sequences = new LinkedList();
    private List transitions = new LinkedList();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        // charger les séquences
        COSequenceManager sequenceManager = new COSequenceManager();

        sequenceManager.setSession(getSession());
        sequenceManager.find();

        for (int id = 0; id < sequenceManager.size(); ++id) {
            COSequence sequence = (COSequence) sequenceManager.get(id);

            sequences.add(sequence);
        }

        // On dumpe les requêtes dans un fichier temporaire
        File file = File.createTempFile("temp", ".sql");
        PrintWriter writer = new PrintWriter(new FileWriter(file));

        try {
            COInsertQueryBuilder builder = new COInsertQueryBuilder();

            dumpEtapes(builder, writer);
            dumpTransitions(builder, writer);
            dumpInfosConfigs(builder, writer);

            if (baseDuplique.booleanValue()) {
                dumpCopies(builder, writer);
            }
        } finally {
            writer.close();
        }

        // on ajoute le fichier aux documents attachés
        this.registerAttachedDocument(file.getAbsolutePath());

        return true;
    }

    private void dumpComment(PrintWriter writer, String type) {
        writer.println();
        writer.println();
        writer.println("--");
        writer.print("-- dump des ");
        writer.println(type);
        writer.println("--");
        writer.println();
    }

    private void dumpCopies(COInsertQueryBuilder builder, PrintWriter writer) throws Exception {
        builder.reset(getSchema(), getTransaction());

        for (Iterator seqIter = sequences.iterator(); seqIter.hasNext();) {
            COSequence sequence = (COSequence) seqIter.next();
            Integer base = new Integer(100000);

            if (sequence.getLibSequence().equals(csSequenceBase)) {
                continue;
            }

            // dump des étapes
            dumpComment(writer, "étapes - " + sequence.getLibSequenceLibelle());

            List etapes = new ArrayList(oldIdEtapeToEtape.values());

            Collections.sort(etapes, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    COEtape etape1 = (COEtape) o1;
                    COEtape etape2 = (COEtape) o2;

                    return Integer.valueOf(etape1.getIdEtape()).compareTo(Integer.valueOf(etape2.getIdEtape()));
                }
            });

            for (Iterator etapeIter = etapes.iterator(); etapeIter.hasNext();) {
                COEtape etape = (COEtape) etapeIter.next();
                int id = Integer.parseInt(etape.getIdEtape());

                etape.setIdEtape(String.valueOf(id + base.intValue()));
                etape.setIdSequence(sequence.getIdSequence());
                builder.printInsertQuery(writer, etape);
            }

            // dump des transitions
            dumpComment(writer, "transitions - " + sequence.getLibSequenceLibelle());

            for (Iterator tranIter = transitions.iterator(); tranIter.hasNext();) {
                COTransition transition = (COTransition) tranIter.next();
                int id = Integer.parseInt(transition.getIdTransition());

                transition.setIdTransition(String.valueOf(id + base.intValue()));

                // etape
                id = Integer.parseInt(transition.getIdEtape());
                transition.setIdEtape(String.valueOf(id + base.intValue()));

                // etape suivante
                id = Integer.parseInt(transition.getIdEtapeSuivante());
                transition.setIdEtapeSuivante(String.valueOf(id + base.intValue()));

                builder.printInsertQuery(writer, transition);
            }

            // dump des informations par étapes
            dumpComment(writer, "infos configs - " + sequence.getLibSequenceLibelle());

            for (Iterator infoIter = infoConfigs.iterator(); infoIter.hasNext();) {
                COEtapeInfoConfig info = (COEtapeInfoConfig) infoIter.next();
                int id = Integer.parseInt(info.getIdEtapeInfoConfig());

                info.setIdEtapeInfoConfig(String.valueOf(id + base.intValue()));

                // etape
                id = Integer.parseInt(info.getIdEtape());
                info.setIdEtape(String.valueOf(id + base.intValue()));

                builder.printInsertQuery(writer, info);
            }
        }
    }

    private void dumpDelete(PrintWriter writer, String tableName) {
        writer.print("DELETE FROM ");
        writer.print(schema);
        writer.print(".");
        writer.print(tableName);
        writer.println(";");
    }

    private void dumpEtapes(COInsertQueryBuilder builder, PrintWriter writer) throws Exception {
        counter = 0;
        builder.reset(schema, getTransaction());

        // on écrit un petit commentaire
        dumpComment(writer, "étapes");

        if (includeDelete.booleanValue()) {
            dumpDelete(writer, ICOEtapeConstante.TABLE_NAME);
        }

        // chargement et exportation des entités
        COEtapeManager etapeManager = new COEtapeManager();

        if (baseDuplique.booleanValue()) {
            etapeManager.setForLibSequence(csSequenceBase);
        }

        etapeManager.setSession(getSession());
        etapeManager.changeManagerSize(0);
        etapeManager.find();

        for (int id = 0; id < etapeManager.size(); ++id) {
            COEtape etape = (COEtape) etapeManager.get(id);

            if (recomputeIndexes.booleanValue()) {
                int newId = ++counter;

                oldIdEtapeToEtape.put(etape.getIdEtape(), etape);
                etape.setIdEtape(String.valueOf(newId));
            } else {
                oldIdEtapeToEtape.put(etape.getIdEtape(), etape);
            }

            builder.printInsertQuery(writer, etape);
        }
    }

    private void dumpInfosConfigs(COInsertQueryBuilder builder, PrintWriter writer) throws Exception {
        counter = 0;
        builder.reset(schema, getTransaction());

        // on écrit un petit commentaire
        dumpComment(writer, "infos configs");

        if (includeDelete.booleanValue()) {
            dumpDelete(writer, COEtapeInfoConfig.TABLE_NAME_CONFIG);
        }

        COEtapeInfoConfigManager infosManager = new COEtapeInfoConfigManager();

        infosManager.setSession(getSession());
        infosManager.changeManagerSize(0);
        infosManager.setForIdEtapeIn(oldIdEtapeToEtape.keySet());
        infosManager.find();

        for (int id = 0; id < infosManager.size(); ++id) {
            COEtapeInfoConfig etapeInfoConfig = (COEtapeInfoConfig) infosManager.get(id);

            if (recomputeIndexes.booleanValue()) {
                etapeInfoConfig.setIdEtapeInfoConfig(String.valueOf(++counter));
                etapeInfoConfig
                        .setIdEtape(((COEtape) oldIdEtapeToEtape.get(etapeInfoConfig.getIdEtape())).getIdEtape());
            }

            infoConfigs.add(etapeInfoConfig);
            builder.printInsertQuery(writer, etapeInfoConfig);
        }
    }

    private void dumpTransitions(COInsertQueryBuilder builder, PrintWriter writer) throws Exception {
        counter = 0;
        builder.reset(schema, getTransaction());

        // écrire un petit commentaire
        dumpComment(writer, "transitions");

        if (includeDelete.booleanValue()) {
            dumpDelete(writer, COTransition.TABLE_NAME);
        }

        COTransitionManager transitionManager = new COTransitionManager();

        transitionManager.setSession(getSession());
        transitionManager.changeManagerSize(0);
        transitionManager.setForIdEtapeSuivanteIn(oldIdEtapeToEtape.keySet());
        transitionManager.find();

        for (int id = 0; id < transitionManager.size(); ++id) {
            COTransition transition = (COTransition) transitionManager.get(id);

            if (recomputeIndexes.booleanValue()) {
                transition.setIdTransition(String.valueOf(++counter));
                transition.setIdEtape(((COEtape) oldIdEtapeToEtape.get(transition.getIdEtape())).getIdEtape());
                transition.setIdEtapeSuivante(((COEtape) oldIdEtapeToEtape.get(transition.getIdEtapeSuivante()))
                        .getIdEtape());
            }

            transitions.add(transition);
            builder.printInsertQuery(writer, transition);
        }
    }

    /**
     * @return vrai s'il faut faire un dump des données pour une séquence et recopier ce dump pour les autres séquences
     */
    public Boolean getBaseDuplique() {
        return baseDuplique;
    }

    /**
     * @return le cs de la séquence sur laquelle se baser s'il faut recopier les valeurs en base pour les autres
     *         séquences.
     */
    public String getCsSequenceBase() {
        return csSequenceBase;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("AQUILA_DUMP_CONFIG");
    }

    /**
     * @return vrai pour inclure dans le script des requêtes delete pour les tables dumpée
     */
    public Boolean getIncludeDelete() {
        return includeDelete;
    }

    /**
     * @return vrai s'il faut recalculer les identifiants des lignes.
     */
    public Boolean getRecomputeIndexes() {
        return recomputeIndexes;
    }

    /**
     * Retourne le nom du schema à utiliser pour préfixer les noms de table.
     * <p>
     * Si null, aucun nom de schéma n'est utilisé
     * </p>
     * 
     * @return le nom du schéma ou null
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue() {@link GlobazJobQueue#READ_SHORT}
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @see #getBaseDuplique()
     */
    public void setBaseDuplique(Boolean baseDuplique) {
        this.baseDuplique = baseDuplique;
    }

    /**
     * @see #getCsSequenceBase()
     */
    public void setCsSequenceBase(String csSequenceBase) {
        this.csSequenceBase = csSequenceBase;
    }

    /**
     * @see #getIncludeDelete()
     */
    public void setIncludeDelete(Boolean includeDelete) {
        this.includeDelete = includeDelete;
    }

    /**
     * @see #getRecomputeIndexes()
     */
    public void setRecomputeIndexes(Boolean recomputeIndexes) {
        this.recomputeIndexes = recomputeIndexes;
    }

    /**
     * @see #getSchema()
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }
}
