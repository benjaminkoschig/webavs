/**
 * 
 */
package globaz.perseus.process.paiements;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.perseus.process.PFAbstractJob;

import java.util.ArrayList;
import java.util.List;

import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFPaiementMensuelRentePontProcess extends PFAbstractJob {

	private String adresseMail = null;

	/**
	 * @return the adresseMail
	 */
	public String getAdresseMail() {
		return this.adresseMail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see globaz.jade.job.JadeJob#getDescription()
	 */
	public String getDescription() {
		return "Paiement mensuel rente-pont";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see globaz.jade.job.JadeJob#getName()
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see globaz.perseus.process.PFAbstractJob#process()
	 */
	@Override
	protected void process() throws Exception {
		try {
			PerseusServiceLocator.getPmtMensuelRentePontService().executerPaiementMensuel(this.getSession(),
					this.getLogSession());
		} catch (Exception e) {
			JadeThread.logError(this.getClass().getName(),
					"Erreur technique grave : " + e.toString() + " : " + e.getMessage());
			e.printStackTrace();
		}
		if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
			JadeBusinessMessage[] messages = JadeThread.logMessages();
			for (int i = 0; i < messages.length; i++) {
				this.getLogSession().addMessage(messages[i]);
			}
		}
		List<String> email = new ArrayList<String>();
		email.add(this.getAdresseMail());
		this.sendCompletionMail(email);
	}

	/**
	 * @param adresseMail
	 *            the adresseMail to set
	 */
	public void setAdresseMail(String adresseMail) {
		this.adresseMail = adresseMail;
	}

}
