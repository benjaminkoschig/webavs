/*
 * Créé le 21 janv. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.api.APITypeSection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CATypeSection;

/**
 * @author jts 21 janv. 05 08:07:56
 * @revision SCO 11 mars 2010
 */
public class CAApercuInteretMoratoire extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOMAINE_CA = "CA";
    public static final String DOMAINE_FA = "FA";
    private CACompteAnnexe compteAnnexe = null;
    private String dateCalcul = "";
    private String dateFacturation = "";
    private String description = "";
    private String domaine = CAApercuInteretMoratoire.DOMAINE_CA;
    // De FAENTFP
    private FAEnteteFacture enteteFacture = null;

    // De CASECTP
    private String idCompteAnnexe = "";
    private String IdEnteteFacture = "";

    private String idExterne = "";
    // De CACPTAP
    private String idExterneRole = "";
    private String idGenreInteret = "";
    // De CAIMDCP
    private String idInteretMoratoire = "";
    private String idJournalCalcul = "";
    private String idJournalFacturation = "";

    private String idSection = "";
    private String idTiers = "";
    private String idTypeSection = "";

    private CAJournal journal = null;

    private String motifCalcul = "";
    private String nombreLignes = "";

    private FAPassage passage = null;
    private String sectionDate = "";
    // De CAIMDEP
    private String totalMontantInt = ""; // somme des MONTANTINTERET

    private CATypeSection typeSection = null;

    @Override
    protected String _getTableName() {
        return "CAIMDCP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // De CAIMDCP
        idInteretMoratoire = statement.dbReadNumeric("IDINTERETMORATOIRE");
        idJournalCalcul = statement.dbReadNumeric("IDJOURNALCALCUL");
        dateCalcul = statement.dbReadDateAMJ("DATECALCUL");
        idSection = statement.dbReadNumeric("IDSECTION");
        idGenreInteret = statement.dbReadNumeric("IDGENREINTERET");
        motifCalcul = statement.dbReadNumeric("MOTIFCALCUL");
        idJournalFacturation = statement.dbReadNumeric("IDJOUFAC");
        dateFacturation = statement.dbReadDateAMJ("DATEFACTURATION");

        // De CAIMDEP
        totalMontantInt = statement.dbReadNumeric("TOTALMONTANTINT");
        nombreLignes = statement.dbReadNumeric("NOMBRELIGNES");

        // De CASECTP
        idCompteAnnexe = statement.dbReadNumeric("IDCOMPTEANNEXE");
        idExterne = statement.dbReadString("IDEXTERNE");
        idTypeSection = statement.dbReadNumeric("IDTYPESECTION");
        sectionDate = statement.dbReadDateAMJ("SECTIONDATE");

        // De CACPTAP
        idExterneRole = statement.dbReadString("IDEXTERNEROLE");
        description = statement.dbReadString("DESCRIPTION");

        // De FAENTFP
        IdEnteteFacture = statement.dbReadString("IDSECTIONFACTURE");
        idTiers = statement.dbReadNumeric("IDTIERS");

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDINTERETMORATOIRE",
                this._dbWriteNumeric(statement.getTransaction(), getIdInteretMoratoire(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return
     */
    public CAJournal get_journal() {
        return journal;
    }

    public CACompteAnnexe getCompteAnnexe() {
        if (compteAnnexe == null) {
            compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setISession(getSession());
            compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
            try {
                compteAnnexe.retrieve();
                if (compteAnnexe.isNew()) {
                    compteAnnexe = null;
                }
            } catch (Exception e) {
                compteAnnexe = null;
            }
        }

        return compteAnnexe;
    }

    /**
     * @return
     */
    public String getDateCalcul() {
        return dateCalcul;
    }

    /**
     * @return
     */
    public String getDateFacturation() {
        return dateFacturation;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public String getDomaine() {
        return domaine;
    }

    public FAEnteteFacture getEnteteFacture() {
        if (enteteFacture == null) {
            enteteFacture = new FAEnteteFacture();
            enteteFacture.setISession(getSession());
            enteteFacture.setIdEntete(getIdEnteteFacture());
            try {
                enteteFacture.retrieve();
            } catch (Exception e) {
                return null;
            }

        }
        return enteteFacture;
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdEnteteFacture() {
        return IdEnteteFacture;
    }

    /**
     * @return
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * @return
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return
     */
    public String getIdGenreInteret() {
        return idGenreInteret;
    }

    /**
     * @return
     */
    public String getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    public String getIdJournal() {
        return isDomaineCA() ? getIdJournalCalcul() : getIdJournalFacturation();
    }

    /**
     * retourne un idJournal ou un idPassage selon le domaine (ComptaAux/Facturation)
     */
    public String getIdJournalCalcul() {
        return idJournalCalcul;
    }

    /**
     * @return
     */
    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    /**
     * @return
     */
    public String getIdSection() {
        return idSection;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getIdTypeSection() {
        return idTypeSection;
    }

    public CAJournal getJournal() {
        if (journal == null) {
            journal = new CAJournal();
            journal.setISession(getSession());
            journal.setIdJournal(getIdJournalCalcul());
            try {
                journal.retrieve();
                if (journal.isNew()) {
                    journal = null;
                }
            } catch (Exception e) {
                journal = null;
            }
        }

        return journal;
    }

    public String getJournalDate() {
        return isDomaineCA() ? getJournal().getDate() : getPassage().getDateFacturation();
    }

    public String getJournalLibelle() {
        return isDomaineCA() ? getJournal().getLibelle() : getPassage().getLibelle();
    }

    public String getLibelleDescription() {
        return getSectionDescriptor().getDescription(getSession().getIdLangueISO());
    }

    /**
     * @return
     */
    public String getMotifCalcul() {
        return motifCalcul;
    }

    /**
     * @return
     */
    public String getNombre() {
        return nombreLignes;
    }

    public FAPassage getPassage() {
        if (passage == null) {
            passage = new FAPassage();
            passage.setISession(getSession());
            passage.setIdPassage(getIdJournalFacturation());
            try {
                passage.retrieve();
            } catch (Exception e) {
                return null;
            }
        }
        return passage;
    }

    /**
     * @return
     */
    public String getSectionDate() {
        return sectionDate;
    }

    private APISectionDescriptor getSectionDescriptor() {
        APISectionDescriptor sectionDescriptor = null;
        try {
            // Récupérer le descripteur selon le type de section
            Class cl = Class.forName(getTypeSection().getNomClasse());
            sectionDescriptor = (APISectionDescriptor) cl.newInstance();
            // Passer la section
            sectionDescriptor.setISession(getSession());
            sectionDescriptor.setSection(getIdExterne(), getIdTypeSection(), null, getSectionDate(), null, null);
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
        // Retourne le descripteur
        return sectionDescriptor;
    }

    public String getStatus() {
        return CAInteretMoratoire.getStatus(getPassage(), getMotifCalcul());
    }

    /**
     * @return
     */
    public String getTotalMontantInteret() {
        return totalMontantInt;
    }

    public APITypeSection getTypeSection() {
        if (typeSection == null) {
            typeSection = new CATypeSection();
            typeSection.setISession(getSession());
            typeSection.setIdTypeSection(getIdTypeSection());
            try {
                typeSection.retrieve();
                if (typeSection.isNew()) {
                    typeSection = null;
                }
            } catch (Exception e) {
                typeSection = null;
            }
        }

        return typeSection;
    }

    /**
     * Returns true is the viewbean applies for "Comptabilité Auxiliaire", false for "Facturation"
     * 
     * @return
     */
    protected boolean isDomaineCA() {
        return domaine.equals(CAApercuInteretMoratoire.DOMAINE_CA);
    }

    /**
     * @param journal
     */
    public void set_journal(CAJournal j) {
        journal = j;
    }

    /**
     * @param string
     */
    public void setDateCalcul(String string) {
        dateCalcul = string;
    }

    /**
     * @param string
     */
    public void setDateFacturation(String string) {
        dateFacturation = string;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setDomaine(String string) {
        domaine = string;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdEnteteFacture(String string) {
        IdEnteteFacture = string;
    }

    /**
     * @param string
     */
    public void setIdExterne(String string) {
        idExterne = string;
    }

    /**
     * @param string
     */
    public void setIdExterneRole(String string) {
        idExterneRole = string;
    }

    /**
     * @param string
     */
    public void setIdGenreInteret(String string) {
        idGenreInteret = string;
    }

    /**
     * @param string
     */
    public void setIdInteretMoratoire(String string) {
        idInteretMoratoire = string;
    }

    /**
     * @param string
     */
    public void setIdJournalCalcul(String string) {
        idJournalCalcul = string;
    }

    /**
     * @param string
     */
    public void setIdJournalFacturation(String string) {
        idJournalFacturation = string;
    }

    /**
     * @param string
     */
    public void setIdSection(String string) {
        idSection = string;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param string
     */
    public void setIdTypeSection(String string) {
        idTypeSection = string;
    }

    /**
     * @param string
     */
    public void setMotifCalcul(String string) {
        motifCalcul = string;
    }

    /**
     * @param string
     */
    public void setNombre(String string) {
        nombreLignes = string;
    }

    /**
     * @param string
     */
    public void setSectionDate(String string) {
        sectionDate = string;
    }

    /**
     * @param string
     */
    public void setTotalMontantInteret(String string) {
        totalMontantInt = string;
    }

    /**
     * @param section
     */
    public void setTypeSection(CATypeSection section) {
        typeSection = section;
    }

}
