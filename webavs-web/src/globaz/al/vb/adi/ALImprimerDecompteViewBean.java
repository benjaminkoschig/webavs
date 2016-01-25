package globaz.al.vb.adi;

import globaz.al.process.adiDecomptes.ALAdiDecomptesImpressionProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * ViewBean gérant le lancement de l'impression des déclarations de versement
 * 
 * @author PTA
 * 
 */
public class ALImprimerDecompteViewBean extends BJadePersistentObjectViewBean {
    /**
     * identifiant du décompte adi à imprimer
     */
    private String idDecompteAdi = null;
    /**
     * indique si le décompte doit être envoyé en GED ou non lors de l'impression
     */
    private boolean isPrintGed = false;

    /**
     * type de décompte Adi à imprimer
     */
    private String typeDecompte = null;

    /**
     * Copnstructeur du viewBean
     */

    public ALImprimerDecompteViewBean() {
        super();
        setIdDecompteAdi(idDecompteAdi);
        setTypeDecompte(typeDecompte);

    }

    @Override
    public void add() throws Exception {
        ALAdiDecomptesImpressionProcess adiDecompteProcess = new ALAdiDecomptesImpressionProcess();

        adiDecompteProcess.setIdDecompteAdi(idDecompteAdi);
        adiDecompteProcess.setTypeDecompte(typeDecompte);
        adiDecompteProcess.setEnvoiGED(isPrintGed);
        adiDecompteProcess.setSession(getSession());
        BProcessLauncher.start(adiDecompteProcess, false);

    }

    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");
    }

    @Override
    public String getId() {
        return idDecompteAdi;
    }

    /**
     * @return the idDecompteAdi
     */
    public String getIdDecompteAdi() {
        return idDecompteAdi;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @return the typeDecompte
     */
    public String getTypeDecompte() {
        return typeDecompte;
    }

    public boolean isPrintGed() {
        return isPrintGed;
    }

    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");

    }

    @Override
    public void setId(String newId) {
        idDecompteAdi = newId;

    }

    /**
     * @param idDecompteAdi
     *            the idDecompteAdi to set
     */
    public void setIdDecompteAdi(String idDecompteAdi) {
        this.idDecompteAdi = idDecompteAdi;
    }

    public void setPrintGed(boolean isPrintGed) {
        this.isPrintGed = isPrintGed;
    }

    /**
     * @param typeDecompte
     *            the typeDecompte to set
     */
    public void setTypeDecompte(String typeDecompte) {
        this.typeDecompte = typeDecompte;
    }

    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");
    }

}
