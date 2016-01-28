package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import ch.globaz.vulpecula.documents.rectificatif.PTImpressionRectificatifProcess;

public class PTImpressionRectificatifImpl implements IntModuleFacturation {

	public PTImpressionRectificatifImpl() {
		super();
	}

	@Override
	public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
		return false;
	}

	@Override
	public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModule) throws Exception {
		return false;
	}

	@Override
	public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
		return false;
	}

	@Override
	public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModule) throws Exception {
		return false;
	}

	@Override
	public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {

		PTImpressionRectificatifProcess process = new PTImpressionRectificatifProcess();
		process.setParentWithCopy(context);
		process.setNumeroPassageFacturation(passage.getIdPassage());
		process.setEMailAddress(context.getEMailAddress());
		process.executeProcess();

		// contrôler si le process a fonctionné
		return !(context.isAborted() || context.isOnError() || context.getSession().hasErrors());

	}

	@Override
	public boolean generer(IFAPassage passage, BProcess context, String idModule) throws Exception {
		return true;
	}

	@Override
	public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
		return false;
	}

	@Override
	public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
		return this.comptabiliser(passage, context);
	}

	@Override
	public boolean regenerer(IFAPassage passage, BProcess context, String idModule) throws Exception {
		return this.generer(passage, context, idModule);
	}

	@Override
	public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
		return this.comptabiliser(passage, context);
	}

	@Override
	public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModule) throws Exception {
		return this.generer(passage, context, idModule);
	}

	@Override
	public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
		return false;
	}
}
