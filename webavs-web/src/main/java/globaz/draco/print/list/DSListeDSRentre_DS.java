package globaz.draco.print.list;

// ITEXT
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
class DSListeDSRentre_DS extends DSDeclarationListViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Iterator container = null;
    private DSDeclarationViewBean entity;

    public DSListeDSRentre_DS() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.04.2003 15:33:47)
     */
    @Override
    protected void _init() {
        container = null;
    }

    /**
     * Appele chaque champ du mod�le JRField : Field appeler
     */
    AFAffiliation getAffilie() throws Exception {
        AFAffiliation affilie = new AFAffiliation();
        affilie.setSession(getSession());
        affilie.setAffiliationId(entity.getAffiliationId());
        affilie.retrieve();
        return affilie;
    }

    public DSDeclarationViewBean getEntity() {
        return entity;
    }

    /**
     * Appele chaque champ du mod�le JRField : Field appeler
     */
    Map getFieldValues(AFAffiliation affilie) throws net.sf.jasperreports.engine.JRException {
        Map row = new HashMap();
        row.put(FWIImportParametre.getCol(1), affilie.getAffilieNumero());
        row.put(FWIImportParametre.getCol(2), affilie.getTiersNom());
        row.put(FWIImportParametre.getCol(3), entity.getDateRetourEff());
        row.put(FWIImportParametre.getCol(4), getSession().getCodeLibelle(entity.getEtat()));
        return row;
    }

    /**
     * Copier le contenu de cette m�thode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entit�.
     */
    boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            // Charge le container si pas encore charg�
            if (container == null) {
                this.find(0);
                container = getContainer().iterator();
            }
            // lit le nouveau entity
            if (container.hasNext()) {
                setEntity((DSDeclarationViewBean) container.next());
                entity = this.getEntity();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }

    public void setEntity(DSDeclarationViewBean entity) {
        this.entity = entity;
    }

}
