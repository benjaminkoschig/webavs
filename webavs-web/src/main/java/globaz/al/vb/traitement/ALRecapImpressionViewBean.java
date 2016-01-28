package globaz.al.vb.traitement;

import globaz.al.process.recapitulatifsEntreprises.ALRecapitulatifEntreprisesImprimerProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.constantes.ALConstPrestations;

/**
 * ViewBean g�rant le lancement de l'impression des r�caps
 * 
 * @author GMO
 * 
 */
public class ALRecapImpressionViewBean extends BJadePersistentObjectViewBean {

    /**
     * date p�riode des r�caps � imprimer
     */
    private String date = null;
    /**
     * date � imprimer sur les r�caps
     */
    private String dateImpression = null;

    /**
     * type de r�cap � imprimer
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
     * Retourne le type de r�cap
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
     * D�finit la date � prendre en compte pour le r�capitulatif
     * 
     * @param date
     *            date du r�capitulatif
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * D�finit la date devant figurer sur le document imprim�
     * 
     * @param dateImpression
     *            date du document imprim�
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
     * d�finit le type de r�cap � imprimer
     * 
     * @param typeRecap
     *            type de r�capitulation
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
