package ch.globaz.pegasus.businessimpl.services.models.calcul.joursAppoint;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;

@RunWith(Parameterized.class)
public class CalculJourAppointTestCase {

    private String date;
    private BigDecimal montantCourrant;
    private BigDecimal montantPrecedent;
    private String montantTotal;
    private int nbJours;
    private String montantJourApp;

    public CalculJourAppointTestCase(String date, BigDecimal montantCourrant, BigDecimal montantPrecedent, int nbJours,
            String montantJourApp, String montantTotal) throws ParseException {
        super();
        this.date = date;
        this.montantCourrant = montantCourrant;
        this.montantPrecedent = montantPrecedent;
        this.nbJours = nbJours;
        this.montantJourApp = montantJourApp;
        this.montantTotal = montantTotal;
    }

    @Parameterized.Parameters(name = "Date: {0}, mnt PCA  courrante: {1}, mnt PCA precedente: {2}, Expected mntJourApp: {3}  Expected montant jourApp: {4}, Expect montantTotal: {5}")
    public static Collection<Object[]> getParameters() {
        // @formatter:off
		return Arrays.asList(new Object[][] { 
				{ "31.03.2014", new BigDecimal(310), null, 1, "10.20" , "10" },
				{ "01.03.2014", new BigDecimal(310), null, 31, "10.20", "316" },
				{ "31.03.2014", new BigDecimal(300), null, 1, "9.85", "10" },
				{ "30.04.2014", new BigDecimal(300), null, 1, "9.85", "10" },
				{ "01.04.2014", new BigDecimal(300), null, 30, "9.85", "296" },
				{ "30.04.2014", new BigDecimal(3600), new BigDecimal(600), 1, "98.65", "99"},
				{ "01.04.2014", new BigDecimal(3600), new BigDecimal(600), 30, "98.65", "2960" },
				{ "01.04.2014", new BigDecimal(600), new BigDecimal(600), 30, null, "0"},
				{ "01.04.2014", new BigDecimal(600), new BigDecimal(800), 30, null, "0"},
				{ "31.03.2014", new BigDecimal(3600), new BigDecimal(500), 1, "101.90", "102"},
				{ "01.03.2014", new BigDecimal(3600), new BigDecimal(500), 31, "101.90", "3159" }, 
				{ "01.03.2014", new BigDecimal(3600), new BigDecimal(1200), 31, "78.90", "2446" },
				{ "30.03.2014", new BigDecimal(13.33), null, 2, "0.45", "1" },
				{ "30.03.2014", new BigDecimal(14.57), null, 2, "0.50", "1" },
				{ "30.03.2014", new BigDecimal(14.88), null, 2, "0.50", "1" },
		});
	}

	@Test
	public void testGenerateJoursAppoint() throws Exception {
		SimpleJoursAppoint sja = createJourAppoint();
		if(sja!=null) {
			Assert.assertEquals("Nb jours: ",String.valueOf(nbJours), sja.getNbrJoursAppoint());
			if(montantJourApp != null){
			Assert.assertEquals("Montant: ",montantJourApp.toString(), sja.getMontantJournalier());
			}
			Assert.assertEquals("Montant total: ",montantTotal, sja.getMontantTotal());

			Assert.assertEquals("Date entre home: ",date, sja.getDateEntreHome());
		} else if( montantJourApp == null) {
			Assert.assertTrue("Doit être null: ", true);
		} else {
			Assert.fail("Aucun jour d'appoint existe la valeur est null");
		}
	}
	

	private SimpleJoursAppoint createJourAppoint() throws ParseException {
		CalculJourAppoint calculJourAppoint = new CalculJourAppoint();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		SimpleJoursAppoint sja = calculJourAppoint.generateJoursAppoint(sdf.parse(date), montantCourrant,
				montantPrecedent);
		return sja;
	}

	@Test
	public void testUpdateMontantJoursAppoint() throws Exception {
		SimpleJoursAppoint sja = createJourAppoint();
        if(sja!=null){
			CalculJourAppoint calculJourAppoint = new CalculJourAppoint();
			calculJourAppoint.updateMontantJoursAppoint(sja, montantCourrant,montantPrecedent);
		     if(montantJourApp != null){
			   Assert.assertEquals("Montant: ",montantJourApp.toString(), sja.getMontantJournalier());
		     }
		}
	}
}
