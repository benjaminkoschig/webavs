package globaz.apg.db.cot.pers;

import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant;
import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependantLoader;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointDemande;
import globaz.apg.db.droits.APDroitLAPGJointDemandeManager;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.pojo.wrapper.APPeriodesRevenuIndependantWrapper;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.util.LinkedList;
import java.util.List;

/**
 * @author scr
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class APPeriodesRevenuIndependant extends BEntity implements IAPPeriodesRevenuIndependantLoader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String dateFin = null;
    private String revenuIndependant = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        ;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        ;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        ;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        ;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant#getDateDebut()
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant#getDateFin()
     */
    public String getDateFin() {
        return dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant#getRevenuIndependant ()
     */
    public String getRevenuIndependant() {
        return revenuIndependant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant#load(java.lang.String , java.lang.String,
     * java.lang.String)
     */
    @Override
    public IAPPeriodesRevenuIndependant[] load(String idTiers, String dateDebut, String dateFin) throws Exception {

        List result = new LinkedList();

        // On récupère tous les droits de ce tiers, pour la période donnée.
        APDroitLAPGJointDemandeManager mgr = new APDroitLAPGJointDemandeManager();
        mgr.setSession(getSession());
        mgr.setForIdTiers(idTiers);
        mgr.setForDroitContenuDansDateDebut(dateDebut);
        mgr.setForDroitContenuDansDateFin(dateFin);
        mgr.setForEtatDroit(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
        mgr.find();

        boolean isElem = false;
        for (int i = 0; i < mgr.size(); i++) {
            APDroitLAPGJointDemande entity = (APDroitLAPGJointDemande) mgr.getEntity(i);

            // On récupère sa situation professionnelle et on contrôle qu'il ait
            // bien indépendant.
            APSituationProfessionnelleManager mgrSitProf = new APSituationProfessionnelleManager();
            mgrSitProf.setSession(getSession());
            mgrSitProf.setForIdDroit(entity.getIdDroit());
            mgrSitProf.find();
            for (int j = 0; j < mgrSitProf.size(); j++) {
                APSituationProfessionnelle sp = (APSituationProfessionnelle) mgrSitProf.getEntity(j);
                if (sp.getIsIndependant().booleanValue()) {
                    isElem = true;
                    APPeriodesRevenuIndependant periode = new APPeriodesRevenuIndependant();
                    periode.setDateDebut(entity.getDateDebutDroit());
                    periode.setDateFin(entity.getDateFinDroit());
                    periode.setRevenuIndependant(sp.getRevenuIndependant());
                    result.add(periode);
                    break;
                }
            }
        }

        if (result.isEmpty()) {
            return new APPeriodesRevenuIndependantWrapper[0];
        } else {
            APPeriodesRevenuIndependantWrapper[] res = new APPeriodesRevenuIndependantWrapper[result.size()];
            IAPPeriodesRevenuIndependant[] bouclementsAlfa = (IAPPeriodesRevenuIndependant[]) result
                    .toArray(new IAPPeriodesRevenuIndependant[result.size()]);

            for (int i = 0; i < result.size(); i++) {
                res[i] = new APPeriodesRevenuIndependantWrapper();
                res[i].setRevenuIndependant(bouclementsAlfa[i].getRevenuIndependant());
                res[i].setDateDebut(bouclementsAlfa[i].getDateDebut());
                res[i].setDateFin(bouclementsAlfa[i].getDateFin());
            }

            return res;
        }
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param currency
     */
    public void setRevenuIndependant(String s) {
        revenuIndependant = s;
    }

}
