package ch.globaz.pegasus.businessimpl.services.models.calcul.joursAppoint;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;

/**
 * @author dma
 */

public class CalculJourAppoint {

    /**
	 * 	// @formatter:off
	 * Permet de calculer les jours d'appoints. Le calcule suivant est fait: 
	 * (montantPcaCourrant - montantPcaPrecedent) / nbJours
	 * Si le montant calculer est �gale � zero aucun jour d'appoint ne sera g�n�r� la fonction renv�ra null
	 * 
	 * @param dateEntreeHome
	 *            : D�finit quand la personne est rentr� dans le home, obligatoir
	 * @param montantPcaCourrant
	 *            : D�finir le montant de la pcaCourante, obligatoir
	 * @param montantPcaPrecedant
	 *            : D�finir le montant de la pcaPrecedante, ce paramettre peut �tre vide
	 * @return L'objet jour d'appoint
	 */
	public  SimpleJoursAppoint generateJoursAppoint(final Date dateEntreeHome, final BigDecimal montantPcaCourrant,
			final  BigDecimal montantPcaPrecedant) {
		
		if (dateEntreeHome == null) {
			throw new IllegalArgumentException("Unable to generateJoursAppoint, the dateEntreeHome is null!");
		}
		
		Calendar calEntreeHome = Calendar.getInstance();
		calEntreeHome.setTime(dateEntreeHome);
	
		int nbJoursMois = calEntreeHome.getActualMaximum(Calendar.DAY_OF_MONTH);
		int nbJours = (nbJoursMois - (calEntreeHome.get(Calendar.DAY_OF_MONTH))) + 1;
		
		BigDecimal montantJournalierJoursAppoint = computeMontantJourAppoint(montantPcaCourrant, montantPcaPrecedant,
				nbJoursMois);

		SimpleJoursAppoint sja = new SimpleJoursAppoint();
		sja.setMontantJournalier(String.valueOf(montantJournalierJoursAppoint));
		sja.setMontantTotal(computMontantTotal(nbJours, montantJournalierJoursAppoint).toString());
		sja.setNbrJoursAppoint(String.valueOf(nbJours));
		SimpleDateFormat datedfDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		sja.setDateEntreHome(datedfDateFormat.format(dateEntreeHome));
		if (montantJournalierJoursAppoint.compareTo(new BigDecimal(0)) == 1) {
			return sja;
		} else {
			return null;
		}
	}

	private BigDecimal computMontantTotal(int nbJours, BigDecimal montantJournalierJoursAppoint) {
		return montantJournalierJoursAppoint.multiply(new BigDecimal(nbJours)).setScale(0,RoundingMode.CEILING);
	}
	
	
	/**
	 * Recalcule le montant du jour d'appoint en fonction des montant pass� en param�tre.
	 * Set ce montant dans l'objet simpleJoursAppoint donn�e en param�tre
	 * @param simpleJoursAppoint: jour d'appoint pour le quelle on veut recalculer le montant
	 * @param montantPcaCourrant: montant de la PCA courrante
	 * @param montantPcaPrecedant: montant de la PCA prcedeante
	 */
	public void updateMontantJoursAppoint(SimpleJoursAppoint simpleJoursAppoint, final BigDecimal montantPcaCourrant,
			final  BigDecimal montantPcaPrecedant) {
		
		if (simpleJoursAppoint == null) {
			throw new IllegalArgumentException("Unable to updateJoursAppoint, the simpleJoursAppoint is null!");
		}
		
		int nbJoursMois = JadeDateUtil.getGlobazCalendar(simpleJoursAppoint.getDateEntreHome()).getActualMaximum(Calendar.DAY_OF_MONTH);
		
		BigDecimal montantJournalierJoursAppoint = computeMontantJourAppoint(montantPcaCourrant, montantPcaPrecedant,
				nbJoursMois);

		simpleJoursAppoint.setMontantJournalier(String.valueOf(montantJournalierJoursAppoint));
		simpleJoursAppoint.setMontantTotal(computMontantTotal(Integer.valueOf(simpleJoursAppoint.getNbrJoursAppoint()), montantJournalierJoursAppoint).toString());
	}



	private BigDecimal computeMontantJourAppoint(final BigDecimal montantPcaCourrant,
			final BigDecimal montantPcaPrecedant, int nbJoursMois) {
		BigDecimal montantNet = montantPcaCourrant;
		
		if (montantPcaCourrant == null) {
			throw new IllegalArgumentException("Unable to generateJoursAppoint, the montantPcaCourrant is null!");
		}
	
		if (montantPcaPrecedant != null) {
			montantNet = montantPcaCourrant.subtract(montantPcaPrecedant);
		}
		
		 
		BigDecimal montantJournalierJoursAppoint = montantNet.divide(new BigDecimal(nbJoursMois), 8, RoundingMode.HALF_UP);
		
		FWCurrency montant = new FWCurrency(montantJournalierJoursAppoint.toString());
		montant.round(FWCurrency.ROUND_5CT);
		
		montantJournalierJoursAppoint = montant.getBigDecimalValue(); //new BigDecimal(Math.ceil(montantJournalierJoursAppoint.doubleValue() * 20) / 20,new MathContext(10, RoundingMode.HALF_UP)); 

		return montantJournalierJoursAppoint.setScale(2);
	}
}