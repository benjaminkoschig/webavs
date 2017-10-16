/*
 * Globaz SA.
 */
package globaz.naos.db.controleLpp;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class AFSuiviLppAnnuelSalarie extends JadeEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private Long idAffiliation;
    private String numeroAffiliation;
    private String nss;
    private String nomSalarie;
    private Integer moisDebut;
    private Integer moisFin;
    private Integer annee;
    private Montant salaire;
    private Integer niveauSecurite;

    // Champ non persisté
    private Montant seuilEntree;

    @Override
    protected void writeProperties() {
        this.write(AFSuiviLppAnnuelSalariesTableDef.ID_AFFILIATION, idAffiliation);
        this.write(AFSuiviLppAnnuelSalariesTableDef.NUMERO_AFFILIE, numeroAffiliation);
        this.write(AFSuiviLppAnnuelSalariesTableDef.NSS, nss);
        this.write(AFSuiviLppAnnuelSalariesTableDef.NOM_SALARIE, nomSalarie);
        this.write(AFSuiviLppAnnuelSalariesTableDef.MOIS_DEBUT, moisDebut);
        this.write(AFSuiviLppAnnuelSalariesTableDef.MOIS_FIN, moisFin);
        this.write(AFSuiviLppAnnuelSalariesTableDef.ANNEE, annee);
        this.write(AFSuiviLppAnnuelSalariesTableDef.SALAIRE, salaire, CONVERTER_MONTANT);
        this.write(AFSuiviLppAnnuelSalariesTableDef.NIVEAU_SECURITE, niveauSecurite);
    }

    @Override
    protected void readProperties() {
        id = this.read(AFSuiviLppAnnuelSalariesTableDef.ID);
        idAffiliation = this.read(AFSuiviLppAnnuelSalariesTableDef.ID_AFFILIATION);
        numeroAffiliation = this.read(AFSuiviLppAnnuelSalariesTableDef.NUMERO_AFFILIE);
        nss = this.read(AFSuiviLppAnnuelSalariesTableDef.NSS);
        nomSalarie = this.read(AFSuiviLppAnnuelSalariesTableDef.NOM_SALARIE);
        moisDebut = this.read(AFSuiviLppAnnuelSalariesTableDef.MOIS_DEBUT);
        moisFin = this.read(AFSuiviLppAnnuelSalariesTableDef.MOIS_FIN);
        annee = this.read(AFSuiviLppAnnuelSalariesTableDef.ANNEE);
        salaire = this.read(AFSuiviLppAnnuelSalariesTableDef.SALAIRE, CONVERTER_MONTANT);
        niveauSecurite = this.read(AFSuiviLppAnnuelSalariesTableDef.NIVEAU_SECURITE);
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return AFSuiviLppAnnuelSalariesTableDef.class;
    }

    @Override
    public String getIdEntity() {
        return id;
    }

    @Override
    public void setIdEntity(String id) {
        this.id = id;
    }

    public Long getIdAffiliation() {
        return idAffiliation;
    }

    public void setIdAffiliation(Long idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNomSalarie() {
        return nomSalarie;
    }

    public void setNomSalarie(String nomSalarie) {
        this.nomSalarie = nomSalarie;
    }

    public Integer getMoisDebut() {
        return moisDebut;
    }

    public void setMoisDebut(Integer moisDebut) {
        this.moisDebut = moisDebut;
    }

    public Integer getMoisFin() {
        return moisFin;
    }

    public void setMoisFin(Integer moisFin) {
        this.moisFin = moisFin;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Montant getSalaire() {
        return salaire;
    }

    public void setSalaire(Montant salaire) {
        this.salaire = salaire;
    }

    public Integer getNiveauSecurite() {
        return niveauSecurite;
    }

    public void setNiveauSecurite(Integer niveauSecurite) {
        this.niveauSecurite = niveauSecurite;
    }

    public String getNumeroAffiliation() {
        return numeroAffiliation;
    }

    public void setNumeroAffiliation(String numeroAffiliation) {
        this.numeroAffiliation = numeroAffiliation;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public Montant getSeuilEntree() {
        return seuilEntree;
    }

    public void setSeuilEntree(Montant seuilEntree) {
        this.seuilEntree = seuilEntree;
    }

}
