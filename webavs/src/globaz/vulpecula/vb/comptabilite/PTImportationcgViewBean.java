package globaz.vulpecula.vb.comptabilite;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;

/**
 * Description de la classe
 * 
 * @since WebBMS 1.0
 */
public class PTImportationcgViewBean extends PTListeProcessViewBean {
    private String dateJournal = null;
    private String libelle = null;
    private String importFilename = null;

    /**
     * @return the dateJournal
     */
    public String getDateJournal() {
        return dateJournal;
    }

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @param dateJournal the dateJournal to set
     */
    public void setDateJournal(String dateJournal) {
        this.dateJournal = dateJournal;
    }

    /**
     * @param libelle the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * @return the importFilename
     */
    public String getImportFilename() {
        return importFilename;
    }

    /**
     * @param importFilename the importFilename to set
     */
    public void setImportFilename(String importFilename) {
        this.importFilename = importFilename;
    }
}
