package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaStatus;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCC;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;

@RunWith(Parameterized.class)
//@formatter:off
public class GenerateAnonnceForDecsionsAcTest {

    private List<PeriodeOcc> periodesNew =  new ArrayList<PeriodeOcc>();
    private List<PeriodeOcc> periodesOld =  new ArrayList<PeriodeOcc>();
    private List<SimpleCommunicationOCC> expecteds = new ArrayList<SimpleCommunicationOCC>();
    private static PcaStatus O = PcaStatus.OCTROI;
    private static PcaStatus OP = PcaStatus.OCTROI_PARTIEL;
    private static PcaStatus R = PcaStatus.REFUS;
    private static String REQ = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;
    private static String CONJ = IPCDroits.CS_ROLE_FAMILLE_CONJOINT;
    private static String ENF = IPCDroits.CS_ROLE_FAMILLE_ENFANT;

    @Parameterized.Parameters()
    public static Collection<Object[]> parameters() {
        Object[][] data = new Object[][] { { 
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", null, O, REQ } },
                },{ 
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { {} },
                    new Object[][] { {} },
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 1 } , { "06.2014", "08.2014", O, REQ, 1 },  { "09.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", null, O, REQ} } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 1 } , { "06.2014", "08.2014", O, REQ, 1 },  { "09.2014", null, R, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014",  "08.2014", O, REQ} } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 1 } , { "06.2014", "08.2014", OP, REQ, 1 },  { "09.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", null, O, REQ } } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 1 } , { "06.2014", "08.2014", R, REQ, 1 },  { "09.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", "05.2015", O, REQ }, { "09.2014", null, O, REQ } } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 1 } , { "06.2014", "08.2014", O, REQ, 2 },  { "09.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", "05.2015", O, REQ }, { "06.2014", "08.2014", O, REQ }, { "06.2014", "08.2014", O, CONJ }, { "09.2014", null, O, REQ } } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 1 } , { "06.2014", "08.2014", OP, REQ, 2 },  { "09.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", "05.2015", O, REQ }, { "06.2014", "08.2014", OP, REQ }, { "06.2014", "08.2014", OP, CONJ }, { "09.2014", null, O, REQ } } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 1 } , { "06.2014", "08.2014", R, REQ, 2 },  { "09.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", "05.2015", O, REQ }, { "09.2014", null, O, REQ } } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 2 } , { "06.2014", "08.2014", O, REQ, 3 },  { "09.2014", null, O, REQ, 2 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", "05.2015", O, REQ} ,  { "01.2014", "05.2015", O, CONJ}, { "06.2014", "08.2014", O, REQ }, { "06.2014", "08.2014", O, CONJ }, { "06.2014", "08.2014", O, ENF},  { "09.2014", null, O, REQ },{ "09.2014", null, O, CONJ} } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 2 },{ "01.2014", "05.2015", O, CONJ, 2 } , { "06.2014", "08.2014", O, REQ, 2 },{ "06.2014", "08.2014", O, CONJ, 2 }, { "09.2014", null, O, REQ, 2 },{ "09.2014", null, O, CONJ, 2 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", null, O, REQ, 1 },{ "01.2014", null, O, CONJ, 1 } } 
                },{
                    new Object[][] { { "01.2014", "05.2015", O, REQ, 2 } , { "06.2014", "08.2014", R, REQ, 2 },  { "09.2014", null, O, REQ, 2 } },
                    new Object[][] { { } },
                    new Object[][] { { "01.2014", "05.2015", O, REQ} ,  { "01.2014", "05.2015", O, CONJ}, { "09.2014", null, O, REQ },{ "09.2014", null, O, CONJ} } 
                },{ 
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { {  } },
                },{ 
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, OP, REQ, 1 } },
                    new Object[][] { {  } },
                },{ 
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ } },
                },{ 
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, R, REQ } },
                },{ 
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, OP, REQ, 1 } },
                    new Object[][] { { "01.2014", null, R, REQ } },
                },{ 
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { } },
                },{ 
                    new Object[][] { { "01.2014", null, OP, REQ, 1 } },
                    new Object[][] { { "01.2014", null, OP, REQ, 1 } },
                    new Object[][] { { } },
                },{ 
                    new Object[][] { { "01.2014", null, OP, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { } },
                },{ 
                    new Object[][] { { "01.2014", null, OP, REQ, 1 } },
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, OP, REQ }  },
                },{ 
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 2 } },
                    new Object[][] { { "01.2014", null, O, REQ }  },
                },{ 
                    new Object[][] { { "01.2014", null, O, REQ, 2 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ },{ "01.2014", null, O, CONJ}  }
                },{ 
                    new Object[][] { { "01.2014", null, O, REQ, 2 } },
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ },{ "01.2014", null, O, CONJ}  }
                },{ 
                    new Object[][] { { "01.2014", null, O, REQ, 2 } },
                    new Object[][] { { "01.2014", null, OP, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ },{ "01.2014", null, O, CONJ}  }
                },{ 
                    new Object[][] { { "01.2014", null, R, REQ, 2 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, R, REQ },{ "01.2014", null, R, CONJ}  }
                },{ 
                    new Object[][] { { "01.2014", null, R, REQ, 2 } },
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { {} }
                },{ 
                    new Object[][] { { "01.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 2 } },
                    new Object[][] { { "01.2014", null, R, REQ },  }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", O, REQ, 1 },{ "04.2014", "06.2014", O, REQ, 1 },{ "07.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", "03.2014", O, REQ, 1 },{ "04.2014", "06.2014", O, REQ, 1 },{ "07.2014", null, O, REQ, 1 } },
                    new Object[][] { { },  }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", R, REQ, 1 },{ "04.2014", "06.2014", R, REQ, 1 },{ "07.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", "03.2014", R, REQ, 1 },{ "04.2014", "06.2014", R, REQ, 1 },{ "07.2014", null, R, REQ, 1 } },
                    new Object[][] { { },  }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", OP, REQ, 1 },{ "04.2014", "06.2014", OP, REQ, 1 },{ "07.2014", null, OP, REQ, 1 } },
                    new Object[][] { { "01.2014", "03.2014", OP, REQ, 1 },{ "04.2014", "06.2014", OP, REQ, 1 },{ "07.2014", null, OP, REQ, 1 } },
                    new Object[][] { { },  }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", O, REQ, 1 },{ "04.2014", "06.2014", O, REQ, 1 },{ "07.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1} },
                    new Object[][] { { },  }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", O, REQ, 1 },{ "04.2014", "06.2014", O, REQ, 1 },{ "07.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1} },
                    new Object[][] { { "07.2014", null, R, REQ }, }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", O, REQ, 1 },{ "04.2014", "06.2014", R, REQ, 1 },{ "07.2014", null, R, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1} },
                    new Object[][] { { "04.2014", null, R, REQ }, }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", R, REQ, 1 },{ "04.2014", "06.2014", O, REQ, 1 },{ "07.2014", "09.2014", R, REQ, 1 },{ "10.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1} },
                    new Object[][] { { "01.2014", "03.2014", R, REQ },{ "04.2014", "06.2014", O, REQ }, { "10.2014", null, O, REQ } }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", O, REQ, 1 },{ "04.2014", "06.2014", O, REQ, 1 },{ "07.2014", "09.2014", R, REQ, 1 },{ "10.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", null, O, REQ, 1} },
                    new Object[][] { { "07.2014", "09.2014", R, REQ, }, { "10.2014", null, O, REQ } }
                },{ 
                    new Object[][] { { "01.2014", "03.2014", O, REQ, 1 },{ "04.2014", "06.2014", R, REQ, 1 },{ "07.2014", "09.2014", R, REQ, 1 },{ "10.2014", null, O, REQ, 1 } },
                    new Object[][] { { "01.2014", "06.2014", O, REQ, 1}, { "07.2014", null, O, REQ, 1} },
                    new Object[][] { { "04.2014", "09.2014", R, REQ, }, { "10.2014", null, O, REQ } }
                }
        };
        return Arrays.asList(data);
    }

  public  GenerateAnonnceForDecsionsAcTest(Object[][] newPeriodes, Object[][] oldPeriodes, Object[][] expecteds) {
      
        periodesOld = new ArrayList<PeriodeOcc>();
        for (Object[] periodeOcc : newPeriodes) {
            PeriodeOcc p = build(periodeOcc);
            periodesNew.add(p);
        }
        
        for (Object[] periodeOcc : oldPeriodes) {
            if(periodeOcc.length>0 ){
                PeriodeOcc p = build(periodeOcc);
                periodesOld.add(p);
            }
        }

        for (Object[] expected : expecteds) {
            if(expected.length>0){
                SimpleCommunicationOCC occ = new SimpleCommunicationOCC();
                occ.setDateEffet((String)expected[0]);
                occ.setDateFinEffet((String)expected[1]);
                occ.setEtatPC(((PcaStatus)expected[2]).getValue());
                occ.setIdTiers((String)expected[3]);
                occ.setIdTiersRequerant("idTiersRequerant");
                this.expecteds.add(occ);
            }
        }
    }

    private PeriodeOcc build(Object[] periodeOcc) {
      
        String dateDebut = (String) periodeOcc[0];
        String dateFin = (String) periodeOcc[1];
        PcaStatus statusPca = (PcaStatus) periodeOcc[2];
        String csRoleMembreFamille = (String) periodeOcc[3];
        Integer ids = (Integer) periodeOcc[4];
        PeriodeOcc p = buildPeriode(dateDebut, dateFin, statusPca, csRoleMembreFamille, ids);

        
        return p;
    }
    
    
    @Test
    public void test() throws DecisionException{
        GenerateAnonnceForDecsionsAc  generateAnonnceForDecsionsAc = new GenerateAnonnceForDecsionsAc();
        List<SimpleCommunicationOCC> communications  = generateAnonnceForDecsionsAc.genereCommunicationOCCValidation(periodesNew, periodesOld);
        Assert.assertEquals(expecteds.size(), communications.size());
        int i = 0;
        for (SimpleCommunicationOCC expected : expecteds) {
            SimpleCommunicationOCC c = communications.get(i);
            Assert.assertEquals("Vérif date début",expected.getDateEffet(),c.getDateEffet());
            Assert.assertEquals("Vérif date fin", expected.getDateFinEffet(),c.getDateFinEffet());
            Assert.assertEquals("Vérif etat pca", expected.getEtatPC(),c.getEtatPC());
            Assert.assertEquals("Vérif idTiers",expected.getIdTiers(),c.getIdTiers());
            Assert.assertEquals("Vérif idTiersRequerant",expected.getIdTiersRequerant(),c.getIdTiersRequerant());

            i++;
        }
    }

    private List<PlanDeCalculWitMembreFamille> buildListMembreFamille(Integer ids) {
        List<PlanDeCalculWitMembreFamille> membresFamille = new ArrayList<PlanDeCalculWitMembreFamille>();
        boolean conj = false;
        for (int i = 0; i < ids; i++) {
            PlanDeCalculWitMembreFamille membreFamille = new PlanDeCalculWitMembreFamille();
            membreFamille.getDroitMembreFamille().setId(String.valueOf(i));
            membresFamille.add(membreFamille);
            if(i==0){
                membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille().setCsRoleFamillePC(REQ);
            } else if(!conj){
                membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille().setCsRoleFamillePC(CONJ);
                conj = true;
            } else {
                membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille().setCsRoleFamillePC(IPCDroits.CS_ROLE_FAMILLE_ENFANT);
            }
            membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().setIdTiers(membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC());
        }

        return membresFamille;
    }

    private PeriodeOcc buildPeriode(String dateDebut, String dateFin, PcaStatus statusPca, String csRoleMembreFamille,
            Integer ids) {
        PeriodeOcc periodeOcc = new PeriodeOcc(dateDebut, dateFin, "10", statusPca, csRoleMembreFamille, "idTiersRequerant",
                buildListMembreFamille(ids), RoleMembreFamille.fromValue(csRoleMembreFamille));
        return periodeOcc;
    }
}
