package globaz.osiris.db.contentieux;

import globaz.globall.util.JANumberFormatter;

/**
 * Insérez la description du type ici. Date de création : (17.09.2002 11:47:28)
 * 
 * @author: Administrator
 */
public class CALigneExtraitCompte {
    private String codeProvenancePmt = "";
    private String date = new String();
    private String dateJournal = new String();
    private String description = new String();
    private String horsCompteAnnexe = new String();
    private String idExterne = new String();
    private String idSectionCompensation = "";
    private String provenancePmt = "";
    private String sectionCompensationDeSur;
    private String total = new String();

    /**
     * Commentaire relatif au constructeur CALigneExtraitCompte.
     */
    public CALigneExtraitCompte() {
        super();
    }

    public String getCodeProvenancePmt() {
        return codeProvenancePmt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:48:03)
     * 
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the dateJournal
     */
    public String getDateJournal() {
        return dateJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:48:52)
     * 
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:51:10)
     * 
     * @return String
     */
    public String getHorsCompteAnnexe() {
        return JANumberFormatter.formatNoRound(horsCompteAnnexe, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:50:33)
     * 
     * @return String
     */
    public String getIdExterne() {
        return idExterne;
    }

    public String getIdSectionCompensation() {
        return idSectionCompensation;
    }

    public String getProvenancePmt() {
        return provenancePmt;
    }

    public String getSectionCompensationDeSur() {
        return sectionCompensationDeSur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.09.2002 13:30:35)
     * 
     * @return String
     */
    public String getTotal() {
        return total;
    }

    public void setCodeProvenancePmt(String codeProvenancePmt) {
        this.codeProvenancePmt = codeProvenancePmt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:48:03)
     * 
     * @param newDate
     *            String
     */
    public void setDate(String newDate) {
        date = newDate;
    }

    /**
     * @param dateJournal
     *            the dateJournal to set
     */
    public void setDateJournal(String dateJournal) {
        this.dateJournal = dateJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:48:52)
     * 
     * @param newDescription
     *            String
     */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:51:10)
     * 
     * @param newHorsCompteAnnexe
     *            String
     */
    public void setHorsCompteAnnexe(String newHorsCompteAnnexe) {
        horsCompteAnnexe = JANumberFormatter.deQuote(newHorsCompteAnnexe);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 11:50:33)
     * 
     * @param newIdExterne
     *            String
     */
    public void setIdExterne(String newIdExterne) {
        idExterne = newIdExterne;
    }

    public void setIdSectionCompensation(String idSectionCompensation) {
        this.idSectionCompensation = idSectionCompensation;
    }

    public void setProvenancePmt(String provenancePmt) {
        this.provenancePmt = provenancePmt;
    }

    public void setSectionCompensationDeSur(String sectionCompensationDeSur) {
        this.sectionCompensationDeSur = sectionCompensationDeSur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.09.2002 13:30:35)
     * 
     * @param newTotal
     *            String
     */
    public void setTotal(String newTotal) {
        total = newTotal;
    }

}
