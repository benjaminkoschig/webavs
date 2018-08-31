package ch.globaz.vulpecula.business.models.decomptes;

public class DecompteSalaireAAnnoncerHistoriqueSearchComplexModel extends LigneDecompteSearchComplexModel {
    private static final long serialVersionUID = -3495317442190776574L;

    @Override
    public Class<DecompteSalaireAAnnoncerHistoriqueComplexModel> whichModelClass() {
        return DecompteSalaireAAnnoncerHistoriqueComplexModel.class;
    }
}
