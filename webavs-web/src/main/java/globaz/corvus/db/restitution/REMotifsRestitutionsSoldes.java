package globaz.corvus.db.restitution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.contentieux.CAMotifContentieux;

public class REMotifsRestitutionsSoldes extends BEntity {

    private static final long serialVersionUID = 1L;
    private String idMotifBlocage = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String commentaire = "";
    private String motif = "";
    private String idSection = "";

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public String getIdMotifBlocage() {
        return idMotifBlocage;
    }

    public void setIdMotifBlocage(String idMotifBlocage) {
        this.idMotifBlocage = idMotifBlocage;
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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMotifBlocage = statement.dbReadNumeric(CAMotifContentieux.FIELD_IDMOTIFBLOCAGE);
        dateDebut = statement.dbReadDateAMJ(CAMotifContentieux.FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(CAMotifContentieux.FIELD_DATEFIN);
        commentaire = statement.dbReadString(CAMotifContentieux.FIELD_COMMENTAIRE);
        motif = statement.dbReadString(REMotifsRestitutionsSoldesManager.CODE_SYSTEME_LABEL_FIELD);
        idSection = statement.dbReadString(CAMotifContentieux.FIELD_IDSECTION);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

}
