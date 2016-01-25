package ch.globaz.vulpecula.business.models.decomptes;

public class DecompteSalaireAAnnoncerSearchComplexModel extends LigneDecompteSearchComplexModel {
    private static final long serialVersionUID = -3495317442190776574L;

    @Override
    public Class<DecompteSalaireAAnnoncerComplexModel> whichModelClass() {
        return DecompteSalaireAAnnoncerComplexModel.class;
    }
}
