<%@page import="globaz.pyxis.db.divers.TIApplication"%>
<%@ page import="globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");
%>



<tr>
	<td colspan="4" ><hr></td>
</tr>
<tr>
<td colspan="4" >
	<b><ct:FWLabel key='POLITESSE' /> :</b>
	<table>
	
	
 	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>
	<tr>
		<td><ct:FWLabel key='APERCU' /></td>
		<td><b>FR</b>&nbsp;<%=viewBean.getFormulePolitesse(globaz.pyxis.constantes.IConstantes.CS_TIERS_LANGUE_FRANCAIS)%></td>
		<td><b>DE</b>&nbsp;<%=viewBean.getFormulePolitesse(globaz.pyxis.constantes.IConstantes.CS_TIERS_LANGUE_ALLEMAND)%></td>
		<td><b>IT</b>&nbsp;<%=viewBean.getFormulePolitesse(globaz.pyxis.constantes.IConstantes.CS_TIERS_LANGUE_ITALIEN)%></td>
	</tr>
	<%}%>
	<tr>
		<td><ct:FWLabel key='REMPLACER_PAR' /> :</td>
		<td><b>FR</b>&nbsp;<input  type="text" name="politesseSpecFr" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getPolitesseSpecFr(),"\"","&quot;")%>"></td>
		<td><b>DE</b>&nbsp;<input  type="text" name="politesseSpecDe" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getPolitesseSpecDe(),"\"","&quot;")%>"></td>
		<td><b>IT</b>&nbsp;<input  type="text" name="politesseSpecIt" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getPolitesseSpecIt(),"\"","&quot;")%>"></td>		
	</tr>
	</table>
</td>
</tr>
<tr>
	<td colspan="4" ><hr></td>
</tr>

<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>

<tr >
	<td colspan="4">
	<table >
		<tr>
			<td><%=viewBean.getSession().getCodeLibelle(globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_DOMICILE)%>&nbsp;(<b><%=viewBean.getCantonDomicile()%></b>)
				
			</td>
			<td><%=viewBean.getSession().getCodeLibelle(globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_COURRIER)%>&nbsp;(<b><%=viewBean.getSession().getCodeLibelle(viewBean.getDomaineCourrier())%></b>)
			
			</td>
			<td>Indirizzo di pagamento&nbsp;(<b><%=viewBean.getSession().getCodeLibelle(viewBean.getDomainePaiement())%></b>)
			
			</td>
		</tr>
		<tr>
			<td>
			  <TEXTAREA tabindex="-1" rows="9" cols="25"  readonly class="libelleLongDisabled" style="font-weight:normal;font-size:8pt" ><%
			  try {
					out.println(viewBean.getAdresseAsString(globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_DOMICILE,false));
				} catch(Exception ex) {
				    out.println(viewBean.getSession().getLabel("PLUSIEURS_ADRESSES_ACTIVES_ERROR") + viewBean.getIdTiers());
				}%></TEXTAREA>
			</td>
			<td>
			  <TEXTAREA tabindex="-1" rows="9" cols="25"  readonly class="libelleLongDisabled" style="font-weight:normal;font-size:8pt" ><%=viewBean.getAdresseCourrier()%></TEXTAREA>

			</td>
			<td>
			  <TEXTAREA tabindex="-1" rows="9" cols="25"  readonly class="libelleLongDisabled" style="font-weight:normal;font-size:8pt" ><%=viewBean.getAdressePaiement()%></TEXTAREA>
			</td>
		</tr>
	</table>
	</td>
</tr>
<% 
globaz.pyxis.application.TIApplication app = (globaz.pyxis.application.TIApplication)globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("PYXIS");
if (!app.hiddeNavigationBar()) { %>
<tr  valign ="bottom" >	
	<td   colspan="4"  valign="bottom">
		<table cellpadding=0 cellspacing=0 width="100%">
			<tr>
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.tiers">
						<input name="navLink"  class="navLinkTiers" value="1 Ricerca" accesskey="1" type="button" 
						onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.chercher'">
					</ct:ifhasright>
				</td>
		
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.tiers">
						<input name="navLink" class="navLinkTiers"  value="2 Terzi" accesskey="2" type="button" 
						onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdTiers()%>'">
					</ct:ifhasright>
				</td>
			
				<td>
					<ct:ifhasright crud="r" element="pyxis.adressecourrier.avoirAdresse">
						<input name="navLink" class="navLinkTiers"  value="3 Indirizzi della corrispondenza" accesskey="3" type="button" 
						onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.adressecourrier.avoirAdresse.afficher&_method=add&back=_sl&idTiers=<%=viewBean.getIdTiers()%>'">
					</ct:ifhasright>
				</td>
	
				<td>
					<ct:ifhasright crud="r" element="pyxis.adressepaiement.avoirPaiement">
						<input name="navLink" class="navLinkTiers"  value="4 Indirizzi di pagamento" accesskey="4" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.adressepaiement.avoirPaiement.afficher&_method=add&back=_sl&idTiers=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
		
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.avoirContact">
						<input name="navLink" class="navLinkTiers"  value="5 Contatti  " accesskey="5" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.avoirContact.afficher&_method=add&idTiers=<%=viewBean.getIdTiers()%>'">
 		 			</ct:ifhasright>
 		 		</td>
		
				<td>
					<ct:ifhasright crud="r" element="naos.affiliation.affiliation">
						<input name="navLink" class="navLinkTiers"  value="6 Affiliazione" accesskey="6" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\naos?userAction=naos.affiliation.affiliation.chercher&idTiers=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
		
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.compositionTiers">
						<input name="navLink" class="navLinkTiers"  value="7 Legami tra terzi" accesskey="7" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.compositionTiers.chercher&idTiers=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
		
				<td>
					<ct:ifhasright crud="u" element="pyxis.tiers.compositionTiers">
						<input name="navLink" class="navLinkTiers"  value="8 Congiunto" accesskey="8" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.compositionTiers.dirigerConjoint&selectedId=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
			</tr>
		</table>
	</td>
</tr>
<%}%>
<%} else { %>
<!-- ****************************************
	Creazione rapida
********************************************* -->
<tr>
	<td colspan="4" >
		
		<!-- Page 1 -->
		
		<fieldset>
		<input name="extDomaine" type="hidden" value="<%=viewBean.getExtDomaine()%>" >
		<u><b>Creazione rapida</b></u>&nbsp;(<b><%=viewBean.getSession().getCodeLibelle(viewBean.getExtDomaine())%>
		<% if (globaz.pyxis.constantes.IConstantes.CS_APPLICATION_DEFAUT.equals(viewBean.getExtDomaine())) {%>
			/ <input accesskey=" " type="radio" name="extType" <%=((globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_DOMICILE.equals(viewBean.getExtType()))?"CHECKED":"")%>
				onclick="document.getElementById('casePostaleTR').style.visibility='hidden';document.getElementById('extCasePostale').value='';"
				value="<%=globaz.pyxis.db.adressecourrier.TIAvoirAdresse.CS_DOMICILE%>" ><%=viewBean.getSession().getCodeLibelle(globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_DOMICILE)%>
				&nbsp;		
			<input type="radio" name="extType" <%=((globaz.pyxis.db.adressecourrier.TIAvoirAdresse.CS_COURRIER.equals(viewBean.getExtType()))?"CHECKED":"")%>
				onclick="document.getElementById('casePostaleTR').style.visibility='visible';"
				value="<%=globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_COURRIER%>"><%=viewBean.getSession().getCodeLibelle(globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_COURRIER)%>
		<%} else {%>
			/ <%=viewBean.getSession().getCodeLibelle(globaz.pyxis.db.adressecourrier.TIAvoirAdresse.CS_COURRIER)%><input name="extType" type="hidden" value="<%=globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_COURRIER%>" >
		<%}%>
		</b>)<br><br> 
		
		<div id="zone3page1">
		<table>
			<tr>
				<td colspan="2">
					<table>
						<tr>
							<td>Attenzione</td>
							<td>
								<input   name="extAttention" type="text" class="libelleLong" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getExtAttention(),"\"","&quot;")%>">
								<input name="extWantCommit" type="hidden" value="false" >
							</td>
						</tr>
						<tr>
							<td >Via, n° </td>
							<td>
								<input    name="extRue" type="text" class="libelleLong" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getExtRue(),"\"","&quot;")%>"
								onkeyup="autoComplete(this,'<%=request.getContextPath()%>/pyxis?userAction=pyxis.ajax.rue.do&rueLike='+this.value,this,extNpa)">
								<input   name="extNumeroRue" type="text" size="6" value="<%=viewBean.getExtNumeroRue()%>">
							</td>
						</tr>
						<tr id="casePostaleTR" style="visibility: <%=globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_COURRIER.equals(viewBean.getExtType())?"visible":"hidden" %>">
							<td>Casella postale</td>
							<td><input id="extCasePostale" name="extCasePostale" type="text" class="libelleLong" value="<%=viewBean.getExtCasePostale()%>"></td>
						</tr>
						<tr>
							<td >Ric. Loc.</td>
							<td><input   name="extNpa" type="text" class="libelleLong" value="<%=viewBean.getExtNpa()%>" 
									onkeyup="autoComplete(this,'<%=request.getContextPath()%>/pyxis?userAction=pyxis.ajax.localite.do&npaLike='+this.value,document.getElementsByName('extLocalite')[0],document.getElementsByName('extIdLocalite')[0])">
							<%
							Object[] localiteMethodsName = new Object[]{
								new String[]{"setExtIdLocalite","getIdLocalite"},
								new String[]{"setExtLocalite","getLocalite"},
								new String[]{"setExtNpa","getNumPostalEntier"}
							};
							Object[] localiteParams = new Object[]{new String[]{"extNpa","_pos"} };
							%>
							<ct:FWSelectorTag 
								name="localiteSelector" 
								
								methods="<%=localiteMethodsName %>"
								providerApplication ="pyxis"
								providerPrefix="TI"
								providerAction ="pyxis.adressecourrier.localite.chercher"
								providerActionParams ="<%=localiteParams%>"
								/>
							</td>
						</tr>
						<tr>
							<td>Località</td>
							<td>
								<textarea tabindex="-1" size=2 name="extLocalite" type="text" readonly class="libelleLongDisabled" ><%=viewBean.getExtLocalite()%></textarea>
								<input name="extIdLocalite" type="hidden" length="6" value="<%=viewBean.getExtIdLocalite()%>">
							</td>
						</tr>
					</table>
				</td>
				<td colspan="2">
					<b>Pagamento</b><br>
					<table>
						<tr>
							<td>N° CCP</td>
							<td><input accesskey=" " name="extCcp" type="text" class="libelleLong" value="<%=viewBean.getExtCcp()%>"></td>
						</tr>
						<tr>
							<td>N° Conto</td>
							<td>
								<input   name="extCompte" type="text" class="libelleLong" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getExtCompte(),"\"","&quot;")%>">
								<%
									java.util.HashSet except = new java.util.HashSet();
									except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_INCORRECT);
									except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_OK);
									except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT);
									except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT_AUTRE);
									except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_FUSION);
								%>
								<ct:FWCodeSelectTag name="extCodeCompte"
							              defaut=""
											wantBlank="<%=true%>"
							        		codeType="PYIBAN"
							              except="<%=except%>"/>
							</td>
						</tr>
						<tr>
							<td>Ric. Banca</td>
							<td>
								<input  name="extClearing" type="text" class="libelleLong" value="<%=viewBean.getExtClearing()%>"
									onkeyup="autoComplete(this,'<%=request.getContextPath()%>/pyxis?userAction=pyxis.ajax.banque.do&banqueLike='+this.value,document.getElementsByName('extBanque')[0],document.getElementsByName('extIdBanque')[0])">
								<%
									Object[] banqueMethodsName = new Object[]{
									new String[]{"setExtClearing","getClearing"},
									new String[]{"setExtBanque","getNom"},
									new String[]{"setExtIdBanque","getIdTiersBanque"},
									};
									
								%>
								<ct:FWSelectorTag 
									name="banqueSelector" 
									
									methods="<%=banqueMethodsName %>"
									providerApplication ="pyxis"
									providerPrefix="TI"
									providerAction ="pyxis.tiers.banque.chercher"
									/>
										
									
									
							</td>
						</tr>
						<tr>
							<td>Banca</td>
							<td>
								<textarea tabindex="-1" size=2 name="extBanque" type="text" readonly class="libelleLongDisabled" ><%=viewBean.getExtBanque()%></textarea>
								<input name="extIdBanque" type="hidden" length="6" value="<%=viewBean.getExtIdBanque()%>">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			
		</table>
		
		</div>
		
		<!-- Page 2 -->
		
		<div id="zone3page2" style="display:none;">
			<b>Mezzi di comunicazione</b><br>
			<table >
				
				<tr>
					<td>	
						<ct:FWCodeSelectTag name="extMoyType1"
					      defaut="<%=viewBean.getExtMoyType1()%>"
						  wantBlank="<%=true%>"
					      codeType="PYTYPECOMM"/>
					</td>
					<td><input name="extMoyVal1" type="text" class="libelleLong" value="<%=viewBean.getExtMoyVal1()%>"></td>
					
				</tr>
				<tr>
					<td>
						<ct:FWCodeSelectTag name="extMoyType2"
					      defaut="<%=viewBean.getExtMoyType2()%>"
						  wantBlank="<%=true%>"
					      codeType="PYTYPECOMM"/>

					</td>
					<td><input name="extMoyVal2" type="text" class="libelleLong" value="<%=viewBean.getExtMoyVal2()%>"></td>
					
				</tr>
				<tr>
					<td>
						<ct:FWCodeSelectTag name="extMoyType3"
					      defaut="<%=viewBean.getExtMoyType3()%>"
						  wantBlank="<%=true%>"
					      codeType="PYTYPECOMM"/>
					</td>
					<td><input name="extMoyVal3" type="text" class="libelleLong" value="<%=viewBean.getExtMoyVal3()%>"></td>
					
				</tr>
				<tr>
					<td>
						<ct:FWCodeSelectTag name="extMoyType4"
					      defaut="<%=viewBean.getExtMoyType4()%>"
						  wantBlank="<%=true%>"
					      codeType="PYTYPECOMM"/>
					</td>
					<td><input name="extMoyVal4" type="text" class="libelleLong" value="<%=viewBean.getExtMoyVal4()%>"></td>
					
				</tr>
				<tr>
					<td>
						<ct:FWCodeSelectTag name="extMoyType5"
					      defaut="<%=viewBean.getExtMoyType5()%>"
						  wantBlank="<%=true%>"
					      codeType="PYTYPECOMM"/>
					</td>
					<td><input name="extMoyVal5" type="text" class="libelleLong" value="<%=viewBean.getExtMoyVal5()%>"></td>
					
				</tr>
			</table>
		</div>

		</fieldset>				
		
		<script>
			function page2() {
				
				document.getElementById('zone3page2').style.display='block';
				document.getElementById('zone3page1').style.display='none';
				document.getElementsByName('extMoyType1')[0].focus();
			}
			function page1() {
				
				document.getElementById('zone3page1').style.display='block';
				document.getElementById('zone3page2').style.display='none';
				document.getElementsByName('extAttention')[0].focus();
			}
		</script>
	</td>
</tr>
<tr>
	<td  colspan="4" align="right">
			<input type="button" accesskey="1" onClick="page1();" value="Page 1">
			<input type="button" accesskey="2" onClick="page2();" value="Page 2">
	</td>
		
</tr>
<%}%>
			