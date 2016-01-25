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
				{ "31.03.2014", new BigDecimal(310), null, 1, "10.00" , "10" },
				{ "01.03.2014", new BigDecimal(310), null, 31, "10.00", "310" },
				{ "31.03.2014", new BigDecimal(300), null, 1, "9.70", "10" },
				{ "30.04.2014", new BigDecimal(300), null, 1, "10.00", "10" },
				{ "01.04.2014", new BigDecimal(300), null, 30, "10.00", "300" },
				{ "30.04.2014", new BigDecimal(3600), new BigDecimal(600), 1, "100.00", "100"},
				{ "01.04.2014", new BigDecimal(3600), new BigDecimal(600), 30, "100.00", "3000" },
				{ "01.04.2014", new BigDecimal(600), new BigDecimal(600), 0, null, "0"},
				{ "01.04.2014", new BigDecimal(600), new BigDecimal(800), 0, null, "0"},
				{ "31.03.2014", new BigDecimal(3600), new BigDecimal(500), 1, "100.00", "100"},
				{ "01.03.2014", new BigDecimal(3600), new BigDecimal(500), 31, "100.00", "3100" }, 
				{ "01.03.2014", new BigDecimal(3600), new BigDecimal(1200), 31, "77.40", "2400" },
				{ "30.03.2014", new BigDecimal(13.33), null, 2, "0.45", "1" },
				{ "30.03.2014", new BigDecimal(14.57), null, 2, "0.45", "1" },
				{ "30.03.2014", new BigDecimal(14.88), null, 2, "0.50", "1" },
		});
	}

	@Test
	public void testGenerateJoursAppoint() throws Exception {
		SimpleJoursAppoint sja = createJourAppoint();
		if(sja!=null) {
			Assert.assertEquals("Nb jours: ",String.valueOf(nbJours), sja.getNbrJoursAppoint());
			Assert.assertEquals("Montant: ",montantJourApp.toString(), sja.getMontantJournalier());
			Assert.assertEquals("Montant total: ",montantTotal, sja.getMontantTotal());

			Assert.assertEquals("Date entre home: ",date, sja.getDateEntreHome());
		} else if( montantJourApp == null) {
			Assert.assertTrue("Doit �tre null: ", true);
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
			Assert.assertEquals("Montant: ",montantJourApp.toString(), sja.getMontantJournalier());
	
		}
	}
}
