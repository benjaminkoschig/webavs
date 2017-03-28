/*
 * Globaz SA
 */
package globaz.corvus.db.lignedeblocageventilation;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class RELigneDeblocageVentilation extends JadeEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private Long idLigneDeblocage;
    private Long idSectionSource;
    private Montant montant;

    @Override
    protected void writeProperties() {
        this.write(RELigneDeblocageVentilationTableDef.ID_LIGNE_DEBLOCAGE, idLigneDeblocage);
        this.write(RELigneDeblocageVentilationTableDef.ID_SECTION_SOURCE, idSectionSource);
        this.write(RELigneDeblocageVentilationTableDef.MONTANT, montant, CONVERTER_MONTANT);
    }

    @Override
    protected void readProperties() {
        id = this.read(RELigneDeblocageVentilationTableDef.ID);
        idLigneDeblocage = this.read(RELigneDeblocageVentilationTableDef.ID_LIGNE_DEBLOCAGE);
        idSectionSource = this.read(RELigneDeblocageVentilationTableDef.ID_SECTION_SOURCE);
        montant = this.read(RELigneDeblocageVentilationTableDef.MONTANT, CONVERTER_MONTANT);
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return RELigneDeblocageVentilationTableDef.class;
    }

    @Override
    public String getIdEntity() {
        return id;
    }

    @Override
    public void setIdEntity(String id) {
        this.id = id;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public Long getIdLigneDeblocage() {
        return idLigneDeblocage;
    }

    public void setIdLigneDeblocage(Long idLigneDeblocage) {
        this.idLigneDeblocage = idLigneDeblocage;
    }

    public Long getIdSectionSource() {
        return idSectionSource;
    }

    public void setIdSectionSource(Long idSectionSource) {
        this.idSectionSource = idSectionSource;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }
}
