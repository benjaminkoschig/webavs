package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.factory.pca.CalculPcaReplaceFactory;

@RunWith(Parameterized.class)
//@formatter:off
public class GeneratePcaToDeleteTestCase {
	

	@Parameterized.Parameters(name = "NewPca:{0}, Anciennes PCA : {1}, Expected : {2}")
	public static Collection<Object[]> getParameters() {
		return Arrays.asList(new Object[][] { 
				{new String[0][0],new String[0][0],0},
				{new String[][]{{"01.2013",null,"1"}},new String[0][0],0},
				{new String[][]{
						{"01.2012","12.2012","1"},
						{"01.2013",null,"2"}
						},
						new String[0][0],
						0
				},
				{
					new String[][]{
							{"01.2012","02.2012","1"},
							{"03.2012","05.2012","2"},
							{"06.2012","09.2012","3"},
							{"10.2012",null,"4"}
					},
					new String[][]{
							{"01.2012","02.2012","1"},
							{"03.2012","05.2012","2"},
							{"06.2012","09.2012","3"},
							{"10.2012",null,"4"}
					},
					0
				},
				{
					new String[][]{
							{"01.2014",null,"1"},
					},
					new String[][]{
							{"01.2014","02.2014","1"},
							{"03.2014","","2"},
					},
					1
				},
				{
					new String[][]{
							{"01.2014","","1"},
					},
					new String[][]{
							{"01.2014","02.2014","1"},
							{"03.2014",null,"2"},
					},
					1
				},
				{
					new String[][]{
							{"01.2014","","1"},
					},
					new String[][]{
							{"01.2014","02.2014","1"},
							{"03.2014","","2"},
					},
					1
				},
				{
					new String[][]{
							{"01.2012","02.2012","1"},
							{"03.2012","05.2012","2"},
					},
					new String[][]{
							{"01.2012","02.2012","1"},
							{"03.2012","05.2012","2"},
							{"06.2012","09.2012","3"},
							{"10.2012",null,"4"}
					},
					0
				},
				{
					new String[][]{
							{"06.2012","09.2012","3"},
							{"10.2012",null,"4"}
					},
					new String[][]{
							{"01.2012","03.2012","1"},
							{"04.2012","05.2012","2"},
							{"06.2012","09.2012","3"},
							{"10.2012",null,"4"}
					},
					0
				}, 
				{
					new String[][]{
							{"06.2012","07.2012","1"},
							{"08.2012","09.2012","4"},
							{"10.2012",null,"2"}
					},
					new String[][]{
							{"06.2012","09.2012","1"},
							{"10.2012",null,"2"}
					},
					0
				}, 
				{
					new String[][]{
							{"06.2012","07.2012","1"},
							{"08.2012","11.2012","3"},
							{"12.2012",null,"4"}
					},
					new String[][]{
							{"06.2012","09.2012","1"},
							{"10.2012",null,"2"}
					},
					1
				}, 

				{// cas 1
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					0
				},
				{// cas 2
					new String[][]{
							{"07.2014",null,"2"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					0
				},
				{// cas 3
					new String[][]{
							{"01.2014","06.2014","1"},
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					0
				},
				{// cas 4
					new String[][]{
							{"01.2014","03.2014","1"},
							{"04.2014","06.2014","1"},
							{"07.2014","09.2014","1"},
							{"10.2014",null,"2"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					0
				},				
				{// cas 5
					new String[][]{
							{"01.2014","03.2014","1"},
							{"04.2014","06.2014","1"},
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					0
				},				
				{// cas 6
					new String[][]{
							{"07.2014","09.2014","1"},
							{"10.2014",null,"2"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					0
				},	
				
				{// cas 9
					new String[][]{
							{"01.2014",null,"1"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					1
				},
				
				{ // cas 10
					new String[][]{
							{"01.2014","03.2014","1"},
							{"04.2014",null,"3"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					1
				}, 
				
				{ // cas 11
					new String[][]{
							{"01.2014","08.2014","1"},
							{"09.2014",null,"3"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					1
				}, 
				{ // cas 12
					new String[][]{
							{"01.2014","03.2014","1"},
							{"04.2014","08.2014","3"},
							{"09.2014",null,"4"}
					},
					new String[][]{
							{"01.2014","06.2014","1"},
							{"07.2014",null,"2"}
					},
					1
				}, 

				{ 
					new String[][]{
							{"06.2012","07.2012","1"},
							{"08.2012","11.2012","3"}
					},
					new String[][]{
							{"06.2012","09.2012","1"},
							{"10.2012","12.2012","2"}
					},
					1
				}, 
				{
					new String[][]{
							{"02.2013","12.2013","3"},
							{"01.2014",null,"1"}
					},
					new String[][]{
							{"02.2013","09.2013","3"},
							{"10.2013","12.2013","2"},
							{"01.2014",null,"1"}
					},
					1
				}
				,
				{
					new String[][]{
							{"06.2012","07.2012","1"},
							{"08.2012","11.2012","3"}
					},
					new String[][]{
							{"06.2012","09.2012","1"},
							{"10.2012",null,"2"}
					},
					1
				}, {
				new String[][]{
						{"06.2012","07.2012","1"},
				},
				new String[][]{
						{"06.2012","07.2012","1"},
						{"08.2012",null,"2"}
				},
				0
				}, {
					new String[][]{
							{"06.2012",null,"1"},
					},
					new String[][]{
							{"06.2012",null,"1"},
					},
					0
					}
					
				
			});
	}

	//				&& ((dateFin == this.dateFinMax) || (((dateFin != null) && dateFin.equals(this.dateFinMax)) || JadeDateUtil.isDateMonthYearBefore(dateFin, this.dateFinMax))))

	
	private List<SimplePCAccordee> allNewPca;
	private CalculPcaReplaceSearch anciennPca;
	private int nbExpected;
	

	public GeneratePcaToDeleteTestCase( String[][] newPca, String[][] annciennPca, Integer nbExpected) {
		super();
		allNewPca = generatePcaNew(newPca);
		anciennPca = generateAnciennePcaSearch(annciennPca);
		this.nbExpected = nbExpected;
	}

	private CalculPcaReplaceSearch generateAnciennePcaSearch(String[][] periodesAnciennesPCA) {
		JadeAbstractModel[] anciennesPCAccordees = new CalculPcaReplace[periodesAnciennesPCA.length];
		for (int i = 0; i < periodesAnciennesPCA.length; i++) {
			String[] periodes = periodesAnciennesPCA[i];
			CalculPcaReplace pca =  CalculPcaReplaceFactory.generateInitial(periodes[0], periodes[1]);
			pca.getSimplePCAccordee().setIdEntity( periodes[2]);
			anciennesPCAccordees[i] = pca;
		}
		CalculPcaReplaceSearch search = new CalculPcaReplaceSearch();
		search.setSearchResults(anciennesPCAccordees);
		return search;
	}
	
	private List<SimplePCAccordee> generatePcaNew(String[][] params) {
		List<SimplePCAccordee> pcas = new ArrayList<SimplePCAccordee>(params.length);
		for (int i = 0; i < params.length; i++) {
			String[] param = params[i];
			SimplePCAccordee pca = new SimplePCAccordee();
			pca.setDateDebut(param[0]);
			pca.setDateFin(param[1]);
			pca.setIdEntity(param[2]);
			pca.setIdVersionDroit("1234");
			pcas.add(pca);
		}
		return pcas;
	}
	
	@Test
    @Ignore
	public void givenPcaReplacedEmptyThenNoPcaToDelete() throws Exception {
		GeneratePcaToDelete generatePcaToDelete = new GeneratePcaToDelete(allNewPca,anciennPca);
		List<CalculPcaReplace> list = generatePcaToDelete.generate();
		Assert.assertEquals(nbExpected, list.size());
		if(list.size()>0){
			for(CalculPcaReplace pca:list) {
				Assert.assertTrue(pca.getSimplePCAccordee().getIsSupprime());
				Assert.assertEquals(allNewPca.get(0).getIdVersionDroit(),pca.getSimplePCAccordee().getIdVersionDroit());
			}
		}
	}

}
