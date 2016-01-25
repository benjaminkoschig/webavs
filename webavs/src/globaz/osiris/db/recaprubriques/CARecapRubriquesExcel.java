package globaz.osiris.db.recaprubriques;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.naos.api.IAFAffiliation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.pyxis.api.osiris.TITiersOSI;
import globaz.pyxis.constantes.IConstantes;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class CARecapRubriquesExcel extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // CACAPTAP variable
    private String idCompteAnnexe;
    private String idExterneRole;

    private String idTiers;
    private String sumMasse;
    // CAOPERP variable
    private String sumMontant;

    /**
     * Return le nom de la table (CAOPERP).
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setSumMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        setSumMasse(statement.dbReadNumeric(CAOperation.FIELD_MASSE));

        setIdCompteAnnexe(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE));
        setIdTiers(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDTIERS));
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
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

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not needed here
    }

    public String getAdresseTiers() {

        TITiersOSI tiers = new TITiersOSI();
        tiers.setISession(getSession());
        try {
            tiers.retrieve(getIdTiers());
            if (!tiers.isNew()) {
                String domaine = getDomaine();
                return tiers.getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_COURRIER, domaine, null, JACalendar
                        .today().toString());
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Renvoi l'ancien numéro d'affilié
     * 
     * @return String ancien numéro d'affilié
     */
    public String getAncienNumeroAffilie() {
        IAFAffiliation affiliation = null;
        String ancienNumeroAffilie = "";
        try {
            BSession sessionAF = (BSession) GlobazSystem.getApplication("NAOS").newSession();
            getISession().connectSession(sessionAF);
            affiliation = (IAFAffiliation) sessionAF.getAPIFor(IAFAffiliation.class);
            Hashtable criteres = new Hashtable();
            criteres.put(IAFAffiliation.FIND_FOR_IDTIERS, getIdTiers());
            criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, getIdExterneRole());
            IAFAffiliation[] affiliations = affiliation.findAffiliation(criteres);
            // s'il y a plusieurs résultats on prend celui le plus récent
            if ((affiliations != null) && (affiliations.length > 0)) {
                // Ajout des Affiliations dans le TreeMap
                TreeMap affiliationsSort = new TreeMap();
                for (int i = 0; i < affiliations.length; i++) {
                    affiliationsSort.put(affiliations[i].getDateDebut(), affiliations[i]);
                }
                affiliation = (IAFAffiliation) affiliationsSort.get(affiliationsSort.lastKey());
                ancienNumeroAffilie = affiliation.getAncienAffilieNumero();
            }
        } catch (Exception e) {
            return ancienNumeroAffilie;

        }
        return ancienNumeroAffilie;
    }

    public String getDomaine() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            return compteAnnexe._getDefaultDomainFromRole();
        } catch (Exception e) {
            return null;
        }
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
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public String getNomTiers() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdTiers(getIdTiers());

        return compteAnnexe.getTiers().getNom();
    }

    public String getRoleDescription() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            return compteAnnexe.getCARole().getDescription();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getSumMasse() {
        return sumMasse;
    }

    /**
     * @return
     */
    public String getSumMontant() {
        return sumMontant;
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
    public void setIdExterneRole(String string) {
        idExterneRole = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setSumMasse(String string) {
        sumMasse = string;
    }

    /**
     * @param string
     */
    public void setSumMontant(String string) {
        sumMontant = string;
    }
}
