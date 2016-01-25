<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.corvus.vb.avances.REAvanceViewBean"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Set"%>
<%@ page import="globaz.corvus.vb.rentesaccordees.REAdressePmtUtil"%>
<%@ page import="globaz.corvus.api.avances.IREAvances"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	// Les labels de cette page commence par la préfix "JSP_LOT_D"

	idEcran="PRE0081";
	REAvanceViewBean viewBean = (REAvanceViewBean) session.getAttribute("viewBean");	
	selectedIdValue = viewBean.getIdAvance();	
	String desc = viewBean.getNss() + " - " + viewBean.getNom() + " " + viewBean.getPrenom();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionsannonces" showTab="menu">
</ct:menuChange>

<SCRIPT language="javascript">

//Tableau des adresses de paiements
//---------------------------------------------------------------------------
//--idiersAdrPmt  |  Nom Prénom    |  Adr pmt                              --
//---------------------------------------------------------------------------
//--  1234        |  Dupont Jean   |  Crédit Suisse\n clearing 123145\n... --
//--  445678      |  Donzé Mélanie |  CCP 10-4567-23\n...                  --
//---------------------------------------------------------------------------

<%

int size = -1;
if (viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire())!=null) {
	size = viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire()).size();
}

if (size>0) {	
	Map mapAdrPmt = viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire());
	Set keys = mapAdrPmt.keySet();
	Iterator iter = keys.iterator();
	int i = 0;
%>	
	adressePmtArray = new Array(<%=size%>);
	<%while (iter.hasNext()) {
		String k = (String)iter.next();
		REAdressePmtUtil adpmt = (REAdressePmtUtil)mapAdrPmt.get(k);			
		%>
		adressePmtArray[<%=i%>] = new Array(5);
		adressePmtArray[<%=i%>][0] = "<%=adpmt.getIdTiers()%>";
		adressePmtArray[<%=i%>][1] = "<%=adpmt.getNom() + " " + adpmt.getPrenom()%>";
		adressePmtArray[<%=i%>][2] = "<%=adpmt.getCcpOuBanqueFormatte()%>";
		adressePmtArray[<%=i%>][3] = "<%=adpmt.getAdresseFormattee()%>";
		adressePmtArray[<%=i%>][4] = "<%=adpmt.getCsDomaine()%>";
		

		
	<%	i++;
	}%>
<%}%>


	function add(){
	    document.forms[0].elements('userAction').value="corvus.avances.avance.ajouter";
	}

	function upd(){
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="corvus.avances.avance.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="corvus.avances.avance.modifier";
	    }
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="corvus.avances.avance.chercher";
		}
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="corvus.avances.avance.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}


	function nssFailure() {
		
		document.getElementById("idTiersBeneficiaire").value=null;
		document.getElementById("nss").value=null;
		document.getElementById("nomPrenom").value="";
				
	}

	function changeMbrFamille(selectObj) {

		
		 var idx = selectObj.selectedIndex;
		 var key = selectObj.options[idx].value;	

		 		 	
			if (idx==0) {
				<%
					System.out.println("idTiers Benef = " + viewBean.getIdTiersBeneficiaire());
					System.out.println("idTiers Benef Pyxis = " + viewBean.getIdTiersAdressePmtDepuisPyxis());
					System.out.println("domaine = " + viewBean.getCsDomaine());
					
					
					REAdressePmtUtil elm = new REAdressePmtUtil();
					elm.setAdresseFormattee(viewBean.getAdresseFormattee());
					elm.setCcpOuBanqueFormatte(viewBean.getCcpOuBanqueFormatte());
					elm.setIdTiers(viewBean.getIdTiersBeneficiaire());	
					elm.setCsDomaine(viewBean.getCsDomaine());

				%>
				document.getElementById("idTiersAdrPmt").value = "<%=viewBean.getIdTiersAdrPmt()%>";					
				document.getElementById("description").value = '<%=desc%>';
			 	document.getElementById("ccpOuBanqueF").innerHTML = "<%=elm.getCcpOuBanqueFormatte()%>";
			 	document.getElementById("adresseF").innerHTML = "<%=elm.getAdresseFormattee()%>";			 				 			
			 	document.getElementById("idTiersBeneficiaire").value = "<%=elm.getIdTiers()%>";
			 	document.getElementById("idTierRequerant").value = "<%=elm.getIdTiers()%>";
			 	document.getElementById("csDomaine").value = "<%=elm.getCsDomaine()%>";
			 	
			}
			else {
			 for(var i=0; i < adressePmtArray.length; i++) {
					if (key==adressePmtArray[i][0]+"-"+adressePmtArray[i][4]) {
						document.getElementById("description").value = adressePmtArray[i][1];
					 	document.getElementById("ccpOuBanqueF").innerHTML = adressePmtArray[i][2];
					 	document.getElementById("adresseF").innerHTML = adressePmtArray[i][3];
					 	document.getElementById("idTiersAdrPmt").value = adressePmtArray[i][0];
					 	document.getElementById("idTiersBeneficiaire").value = adressePmtArray[i][0];
					 	document.getElementById("idTierRequerant").value = adressePmtArray[i][0];		
					 	document.getElementById("csDomaine").value = adressePmtArray[i][4];					 						 						 	
					}
			}
		}
		 
	}
	

	
	  function readOnly(flag) {
		  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
		    for(i=0; i < document.forms[0].length; i++) {
		        if (!document.forms[0].elements[i].readOnly && 
		        	document.forms[0].elements[i].className != 'forceDisable' &&
		        	document.forms[0].elements[i].type != 'hidden') {
		            document.forms[0].elements[i].disabled = flag;
		        }
		    }
		  }
	
	
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
<%= viewBean.getPageTitle() %><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>


						<TR>
								<TD>
									<strong>
										<ct:FWLabel key="JSP_AVANCE_REQUERANT_INFO"/>
									</strong>
								</TD>							
								<TD colspan="3">													
			            		<re:PRDisplayRequerantInfoTag	session="<%=(BSession) viewBean.getSession()%>"
																idTiers="<%=viewBean.getIdTiersBeneficiaire()%>"
																style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
								<INPUT type="hidden" name="idTiersBeneficiaire" value="<%=viewBean.getIdTiersBeneficiaire()%>"/>
								<!-- utilisé pour recharger le tiers dans l'écran de recherche. -->
								<INPUT type="hidden" name="idTierRequerant" value="<%=viewBean.getIdTiersBeneficiaire()%>"/>
			            	</TD>
						</TR>

						<TR><TD colspan="4"><BR/></TD></TR>

						<TR>						
							<TD valign="top">
								<LABEL for="addressePaiement"><ct:FWLabel key="JSP_ADDRESSE_PAIEMENT"/></LABEL>
								<INPUT type="hidden" name="idTiersAdrPmt" value="<%=viewBean.getIdTiersAdpmt()%>">
								<INPUT type="hidden" name="csDomaine" value="<%=viewBean.getCsDomaine()%>">
								<input type="hidden" name="csDomaineAvance" value="<%= viewBean.getConstantCsDomaineAvanceRente()%>"/>
							</TD>
							<TD colspan="3" rowspan="2" valign="top">
								<PRE><span class="IJAfficheText" id="ccpOuBanqueF"><%=viewBean.getCcpOuBanqueFormatte()%></span></PRE>
								<PRE><span class="IJAfficheText" id="adresseF"><%=viewBean.getAdresseFormattee()%></span></PRE>
							</TD>							
						</TR>

						<TR><TD>
						<ct:FWSelectorTag
								name="selecteurAdresses"

								methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>"
								providerApplication="pyxis"
								providerPrefix="TI"
								providerAction="pyxis.adressepaiement.adressePaiement.chercher"
								target="fr_main"
								redirectUrl="<%=mainServletPath%>"/>
						</TD>
						<TD colspan="3">&nbsp;</TD>
						</TR>
						
						<TR>								
							<TD colspan="3">
								&nbsp;
							</TD>
							<TD colspan="1">
								<select name="selectMbrFam" onchange="changeMbrFamille(this);">
									<option value="0"><ct:FWLabel key="JSP_RAC_R_SELECT_ADRESSE_DE"/></option>
								<%													
									Map mapAdrPmtParMbrFamille = viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire());
									if (mapAdrPmtParMbrFamille!=null) {
										Set keys = mapAdrPmtParMbrFamille.keySet();
										Iterator iter = keys.iterator();
										while (iter.hasNext()) {
											String k = (String)iter.next();
											REAdressePmtUtil admpt = (REAdressePmtUtil)mapAdrPmtParMbrFamille.get(k);
								%>
										<option value="<%=admpt.getIdTiers()%>-<%=admpt.getCsDomaine()%>"><%=admpt.getNom() + " " + admpt.getPrenom()%></option>
								<%		}
									}
								%>
								</select>
							</TD>
						</TR>									


						<TR><TD colspan="4"><BR/></TD></TR>
						<TR><TD colspan="4"><BR/></TD></TR>
						<TR><TD colspan="4"><HR></TD></TR>
						<TR>
							<TD colspan="4">
								<b><ct:FWLabel key="JSP_AVANCE_D_ACOMPTE_UNIQUE"/></b>
							</TD>
						</TR>
						<tr>
							<TD><ct:FWLabel key="JSP_AVANCE_D_DATE_DEBUT_1ERACOMPTE"/></TD>
							<TD>					
								<%if (IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE.equals(viewBean.getCsEtat1erAcompte()) || IREAvances.CS_ETAT_1ER_ACOMPTE_ANNULE.equals(viewBean.getCsEtat1erAcompte())) {%>
									<input type="text" name="dateDebutPmt1erAcompte" value="<%=viewBean.getDateDebutPmt1erAcompte()%>" class="disabled" readonly/>
								<%} else { %>
									<input	id="dateDebutPmt1erAcompte"
											name="dateDebutPmt1erAcompte"
											data-g-calendar="type:default"
											value="<%=viewBean.getDateDebutPmt1erAcompte()%>" />
								<%} %>
							</TD>

							<TD><ct:FWLabel key="JSP_AVANCE_D_DATE_PMT_1ERACOMPTE"/></TD>
							<TD>					
								<INPUT type="text" class="disabled" name="dummy2" readonly="readonly" value="<%=viewBean.getDatePmt1erAcompte()%>">								
							</TD>								
						</tr>
						<TR>
							<TD><ct:FWLabel key="JSP_AVANCE_D_MONTANT_1ERACOMPTE"/></TD>													

								<%if (IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE.equals(viewBean.getCsEtat1erAcompte()) || IREAvances.CS_ETAT_1ER_ACOMPTE_ANNULE.equals(viewBean.getCsEtat1erAcompte())) {%>
									<TD><INPUT type="text"  style="text-align:right" name="montant1erAcompte" value="<%=new globaz.framework.util.FWCurrency(viewBean.getMontant1erAcompte()).toStringFormat()%>" readonly class="disabled"></TD>
								<%} else { %>
									<TD><INPUT type="text"  style="text-align:right" name="montant1erAcompte" value="<%=new globaz.framework.util.FWCurrency(viewBean.getMontant1erAcompte()).toStringFormat()%>" ></TD>
								<%} %>
							
							<TD><ct:FWLabel key="JSP_AVANCE_D_ETAT_1ERACOMPTE"/></TD>
							<TD><INPUT name="libelle2" type="text" value="<%=viewBean.getCsEtat1AcompteLibelle()%>" class="disabled" readonly>								
								<INPUT type="hidden" class="disabled" name="idLot" readonly value="<%=viewBean.getIdAvance()%>">
							</TD>					
							
						</TR>
							
						<TR><TD colspan="4"><HR></TD></TR>
						<TR>
							<TD colspan="4">
								<b><ct:FWLabel key="JSP_AVANCE_D_ACOMPTE_MENSUEL"/></b>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_AVANCE_D_DATE_DEBUT_ACOMPTE"/></TD>
							<TD>
							
								<%if (IREAvances.CS_ETAT_ACOMPTE_ATTENTE.equals(viewBean.getCsEtatAcomptes()) ||
									  JadeStringUtil.isBlankOrZero(viewBean.getCsEtatAcomptes())) {%>
										<input	id="dateDebutAcompte"
												name="dateDebutAcompte"
												data-g-calendar="type:default"
												value="<%=viewBean.getDateDebutAcompte()%>" />
								<%} else { %>
										<input type="text" name="dateDebutAcompte" value="<%=viewBean.getDateDebutAcompte()%>" readonly class="disabled"/>
								<%} %>
							
							
								
							</TD>
							<TD>
								<ct:FWLabel key="JSP_AVANCE_D_DATE_FIN_ACOMPTE"/>
							</TD>
							<TD>
								<%if (IREAvances.CS_ETAT_ACOMPTE_TERMINE.equals(viewBean.getCsEtatAcomptes())) {%>
									<input type="text" name="dateFinAcompte" value="<%=viewBean.getDateFinAcompte()%>" readonly class="disabled"/>
								<%} else { %>
									<input	id="dateFinAcompte"
											name="dateFinAcompte"
											data-g-calendar="type:default"
											value="<%=viewBean.getDateFinAcompte()%>" />
								<%} %>
							
														
								
							</TD>
						</TR>

						<TR>
							<TD><ct:FWLabel key="JSP_AVANCE_D_MONTANT_MENSUEL"/></TD>
							
								<%if (IREAvances.CS_ETAT_ACOMPTE_ATTENTE.equals(viewBean.getCsEtatAcomptes()) ||
									  JadeStringUtil.isBlankOrZero(viewBean.getCsEtatAcomptes())) {%>
									<TD><INPUT type="text"  style="text-align:right" name="montantMensuel" value="<%=new globaz.framework.util.FWCurrency(viewBean.getMontantMensuel()).toStringFormat()%>"></TD>
								<%} else { %>
									<TD><INPUT type="text"  style="text-align:right" name="montantMensuel" value="<%=new globaz.framework.util.FWCurrency(viewBean.getMontantMensuel()).toStringFormat()%>" readonly class="disabled"></TD>
								<%} %>

							

							<TD><ct:FWLabel key="JSP_AVANCE_D_ETAT_ACOMPTES"/></TD>
							<TD><INPUT name="libelle1" type="text" value="<%=viewBean.getCsEtatAcomptesLibelle()%>" class="disabled" readonly>								
							</TD>											
						</TR>						
							<TR><TD colspan="4"><HR></TD></TR>					
						
						
					


							
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>