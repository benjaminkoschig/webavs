package ch.globaz.orion.ws.service;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.math.BigDecimal;

/**
 * Classe représentant une occurence du manager du même nom
 * 
 * @author sco
 * 
 */
public class AFMassesForAffilie extends BEntity {

    private static final long serialVersionUID = 579381462288792114L;

    private String idAffilie;
    private String numAffilie;
    private String idCotisation;
    private BigDecimal masseCotisation;
    private String libelleFr;
    private String libelleDe;
    private String libelleIt;
    private String typeAssurance;
    private String codeCanton;
    private String raisonSociale;
    private String dateDebut;
    private String dateFin;
    private String idRubrique;
    private String genreCoti;

    @Override
    protected String _getTableName() {
        // Not implemented
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffilie = statement.dbReadNumeric("MAIAFF");
        numAffilie = statement.dbReadString("MALNAF");
        idCotisation = statement.dbReadString("MEICOT");
        masseCotisation = statement.dbReadBigDecimal("MEMMAP");
        libelleFr = statement.dbReadString("MBLLIF");
        libelleDe = statement.dbReadString("MBLLID");
        libelleIt = statement.dbReadString("MBLLII");
        typeAssurance = statement.dbReadNumeric("MBTTYP");
        codeCanton = statement.dbReadNumeric("MBTCAN");
        raisonSociale = statement.dbReadString("MADESL");
        dateDebut = statement.dbReadDateAMJ("MEDDEB");
        dateFin = statement.dbReadDateAMJ("MEDFIN");
        idRubrique = statement.dbReadNumeric("MBIRUB");
        genreCoti = statement.dbReadNumeric("MBTGEN");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not implemented
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public BigDecimal getMasseCotisation() {
        return masseCotisation;
    }

    public void setMasseCotisation(BigDecimal masseCotisation) {
        this.masseCotisation = masseCotisation;
    }

    public String getLibelleFr() {
        return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    public String getLibelleDe() {
        return libelleDe;
    }

    public void setLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
    }

    public String getLibelleIt() {
        return libelleIt;
    }

    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public void setTypeAssurance(String typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public String getCodeCanton() {
        return codeCanton;
    }

    public void setCodeCanton(String codeCanton) {
        this.codeCanton = codeCanton;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public String getGenreCoti() {
        return genreCoti;
    }

    public void setGenreCoti(String genreCoti) {
        this.genreCoti = genreCoti;
    }

}
