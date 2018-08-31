package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSimpleModel;

public class SynchronisationEbuComplexModel extends JadeComplexModel {
	private static final long serialVersionUID = 1877797392853943951L;
	
	private DecompteSimpleModel decompteSimpleModel;
	private SynchronisationEbuSimpleModel synchronisationEbuSimpleModel;

	public SynchronisationEbuComplexModel() {
		decompteSimpleModel = new DecompteSimpleModel();
		synchronisationEbuSimpleModel = new SynchronisationEbuSimpleModel();
	}
	
	@Override
	public String getId() {
		return synchronisationEbuSimpleModel.getId();
	}

	@Override
	public String getSpy() {
		return  synchronisationEbuSimpleModel.getSpy();
	}

	@Override
	public void setId(String id) {
		synchronisationEbuSimpleModel.setId(id);		
	}

	@Override
	public void setSpy(String spy) {
		synchronisationEbuSimpleModel.setSpy(spy);		
	}

	/**
	 * @return the decompteSimpleModel
	 */
	public DecompteSimpleModel getDecompteSimpleModel() {
		return decompteSimpleModel;
	}

	/**
	 * @param decompteSimpleModel the decompteSimpleModel to set
	 */
	public void setDecompteSimpleModel(DecompteSimpleModel decompteSimpleModel) {
		this.decompteSimpleModel = decompteSimpleModel;
	}

	/**
	 * @return the synchronisationEbuSimpleModel
	 */
	public SynchronisationEbuSimpleModel getSynchronisationEbuSimpleModel() {
		return synchronisationEbuSimpleModel;
	}

	/**
	 * @param synchronisationEbuSimpleModel the synchronisationEbuSimpleModel to set
	 */
	public void setSynchronisationEbuSimpleModel(
			SynchronisationEbuSimpleModel synchronisationEbuSimpleModel) {
		this.synchronisationEbuSimpleModel = synchronisationEbuSimpleModel;
	}


}
