package globaz.al.vb.traitement;

import globaz.al.process.recapitulatifsEntreprises.ALRecapitulatifEntreprisesImprimerProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.constantes.ALConstPrestations;

/**
 * ViewBean gérant le lancement de l'impression des récaps
 * 
 * @author GMO
 * 
 */
public class ALRecapImpressionViewBean extends BJadePersistentObjectViewBean {

    /**
     * date période des récaps à imprimer
     */
    private String date = null;
    /**
     * date à imprimer sur les récaps
     */
    private String dateImpression = null;

    /**
     * type de récap à imprimer
     */
    private String typeRecap = null;

    /**
     * Constructeur du viewBean
     */
    public ALRecapImpressionViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        ALRecapitulatifEntreprisesImprimerProcess process = new ALRecapitulatifEntreprisesImprimerProcess();
        process.setPeriodeRecap(getDate());
        process.setDateImpression(getDateImpression());

        if (ALConstPrestations.TYPE_COT_PAR.equals(typeRecap) || ALConstPrestations.TYPE_COT_PERS.equals(typeRecap)
                || ALConstPrestations.TYPE_DIRECT.equals(typeRecap)) {
            process.setTypeTraitRecapImpr(typeRecap);
        }

        process.setSession(getSession());
        BProcessLauncher.start(process, false);
    }

    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");

    }

    /**
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return dateImpression
     */
    public String getDateImpression() {
        return dateImpression;
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Retourne le type de récap
     * 
     * @return typeRecap
     */
    public String getTypeRecap() {
        return typeRecap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");

    }

    /**
     * Définit la date à prendre en compte pour le récapitulatif
     * 
     * @param date
     *            date du récapitulatif
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Définit la date devant figurer sur le document imprimé
     * 
     * @param dateImpression
     *            date du document imprimé
     */
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // DO NOTHING
    }

    /**
     * définit le type de récap à imprimer
     * 
     * @param typeRecap
     *            type de récapitulation
     */
    public void setTypeRecap(String typeRecap) {
        this.typeRecap = typeRecap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");

    }

}
