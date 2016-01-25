/*
 * Créé le 11 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.stats;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un manager un peu spécial et qui permet de calculer les stat OFAS. SEULES LA METHODE getCount() PEUT ETRE UTILISEE !!
 * </p>
 * 
 * @author bsc
 */
public class APStatsDroitAPGSitProManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final int STAT_NB_ALLOC_EXPL_TRAV_AGR_FOR_ANNEE = 0;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forAnnee;
    private int typeStat;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getSqlCount(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSqlCount(BStatement statement) {
        if (typeStat == STAT_NB_ALLOC_EXPL_TRAV_AGR_FOR_ANNEE) {
            return createForNbAllocExplTravAgrQuery(statement);
        } else {
            return "";
        }
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    private String createForNbAllocExplTravAgrQuery(BStatement statement) {
        StringBuffer query = new StringBuffer();

        // sélectionner tous les droits
        query.append("SELECT COUNT(*)");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        query.append(" ON ");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append(" = ");
        query.append(APSituationProfessionnelle.FIELDNAME_IDDROIT);

        query.append(" WHERE ");
        // pour l'annee
        query.append(APDroitLAPG.FIELDNAME_DATEDEPOT);
        query.append("<");
        query.append(_dbWriteNumeric(statement.getTransaction(), getForAnneeFin()));
        query.append(" AND ");
        query.append(APDroitLAPG.FIELDNAME_DATEDEPOT);
        query.append(">");
        query.append(_dbWriteNumeric(statement.getTransaction(), getForAnneeDebut()));

        // avec une allocation d'esploitation
        query.append(" AND ");
        query.append(APSituationProfessionnelle.FIELDNAME_ISALLOCEXPLOIT);
        query.append("=");
        query.append(_dbWriteString(statement.getTransaction(), "1"));

        // pour un travailleur agricole
        query.append(" AND ");
        query.append(APSituationProfessionnelle.FIELDNAME_ISCOLLABOAGRICOLE);
        query.append("=");
        query.append(_dbWriteString(statement.getTransaction(), "1"));

        return query.toString();
    }

    /**
     * donne le dernier jour de l'annee qui precede forAnnee
     * 
     * @return
     */
    private String getForAnneeDebut() {
        if (!JadeStringUtil.isEmpty(forAnnee)) {
            return Integer.toString(Integer.parseInt(forAnnee) - 1) + "1231";
        } else {
            return "";
        }
    }

    /**
     * donne le permier jour de l'annee qui suit forAnnee
     * 
     * @return
     */
    private String getForAnneeFin() {
        if (!JadeStringUtil.isEmpty(forAnnee)) {
            return Integer.toString(Integer.parseInt(forAnnee) + 1) + "0000";
        } else {
            return "";
        }
    }

    /**
     * Intialise le manager de manière à ce qu'il compte le nombre d'allocations d'exploitation attribuees aux membres
     * de la famille travaillant dans l'exploitation agricole
     * 
     * @param forAnnee
     *            l'annee prise en compte pour la stat
     */
    public void setForNbAllocExplTravAgr(String forAnnee) {
        this.forAnnee = forAnnee;
        typeStat = STAT_NB_ALLOC_EXPL_TRAV_AGR_FOR_ANNEE;
    }
}
