<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.pavo.util.CIUtil"%>
<%@page import="java.util.HashSet"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran = "CCI3333" ;

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress = objSession.getUserEMail()!=null?objSession.getUserEMail():"";
	//globaz.pavo.db.compte.CICompteIndividuelExtournerViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelExtournerViewBean)session.getAttribute ("viewBean");

	//userActionValue = "pavo.compte.compteIndividuel.extourner";	
	
 	CICompteIndividuelCertificatAssuranceViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelCertificatAssuranceViewBean)session.getAttribute("viewBean");    
	userActionValue = "pavo.compte.compteIndividuelCertificatAssurance.executer";
	
	tableHeight = 150;
	int autoDigitAff = CIUtil.getAutoDigitAff(session);
	

%>

<%@page import="globaz.pavo.db.compte.CICompteIndividuelCertificatAssuranceViewBean"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>


<SCRIPT language="JavaScript">
top.document.title = "Impression du certificat d'Assurance "
var langue = "<%=languePage%>"; 

</SCRIPT>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	
function trim(valueToTrim)
{
  var lre = /^\s*/;
  var rre = /\s*$/;
  
  valueToTrim = valueToTrim.replace(lre, "");
  valueToTrim = valueToTrim.replace(rre, "");
  // tester si le numéro avs entré comporte slt des numéros et/ou des .
  var cre = /((\d|\.)+)/;
  if (!cre.test(valueToTrim)) {
	valueToTrim = "";
  }
  return valueToTrim;
}

function validateCharForDate(input, event) {
	var valueStr = new String(input.value);	
	var keyCode = new String(String.fromCharCode(event.keyCode));
	
	if (genericFilter(new RegExp("[0-9\.]"), keyCode)) {
	}
	else {		
		event.keyCode = '';
		return;
	}	
}

function setNotFound(tag){
	document.forms[0].elements('nomAffilie').value = "";
	document.forms[0].elements('langueImp').value = "";
}

function updateEmployeurPartenaire(tag) {
	if (tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('nomAffilie').value = tag.select[tag.select.selectedIndex].nom;
		document.getElementById('langueImp').value = tag.select[tag.select.selectedIndex].langue;
		document.getElementById('forTypeAffilie').value = tag.select[tag.select.selectedIndex].typeAffiliation;
	}
}
	
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression du certificat d'Assurance<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR>
							<TD>
								NNSS
							</TD>
							<TD colspan ="3">
								<INPUT type="text" name="numeroAvsInv" size="17" class="disabled" tabindex="-1" readonly value="<%=viewBean.getNssFormate()%>">
	
								
							</TD>
						</tr>				
						<TR>
							<TD>
								Assuré
							</TD>
							<TD colspan ="3">
								<INPUT type="text" name="nomInv" size="70" class="disabled" tabindex="-1" readonly value="<%=viewBean.getNomPrenom()%>">
							</TD>
						</tr>			
						
						<tr>
							<td>
								Date de naissance &nbsp;
							</td>
							<td>
							<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissance()%>">
							&nbsp;
							Sexe &nbsp;
							<input type="text" size = "7" class='disabled' name="sexeForDisplay" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelle()%>">
							Pays &nbsp;
							<input type="text" size = "45" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormate()%>">
							</td>
					  </tr>
					 
					   <tr>
		  					  			<%String jspLocation = servletContext + mainServletPath + "Root/ti_select_all.jsp";%>
					  		<td>							
								No Affilie &nbsp;
							</td>
							<td>  
							<ct:FWPopupList onChange="updateEmployeurPartenaire(tag);"  name="likeInIdAffiliation" value="" className="libelle" jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigitAff%>" size="14" minNbrDigit="3"/>
							<input type="text" name="nomAffilie" size="36" class="disabled" readonly value=" " tabIndex="-1">
								<input id="forTypeAffilie" type="hidden" name="forTypeAffilie" value="<%=viewBean.getForTypeAffilie()%>">										
							</td>
				  		</tr>	
					  <tr>
							<td >							
								Langue de correspondance (par défaut langue de l'affilié)
							</td>
						<td>
					  <%
						HashSet except = new HashSet();
						except.add("503003");
						except.add("503005");
						except.add("503006");
						except.add("503007");
						%>
						
						<ct:FWCodeSelectTag codeType="PYLANGUE" name="langueImp" defaut="<%=viewBean.getLangueImp()%>" wantBlank="false" except="<%=except%>"/>			
										&nbsp;&nbsp;motif &nbsp;
											
										<select id="motif" name="motif">
										  	  <option value ="21"></option>
											  <option value ="15">15,25</option>
											  <option value ="31">31,41,33,43</option>
											  <option value ="36">36,46</option>								  
										</select>
									 
									  &nbsp;&nbsp;Employeur&nbsp;
					            	<input type="checkbox" name="employeur" >
					            		
									</td>			            	 
						  	
					   	            	 
					  </tr>
					 
					 
					 
					  
					 				  			
			           
        			
		  			
		  			<tr> 
			            <td height="2">Adresse E-Mail</td>
			            <td height="2">
			             <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=eMailAddress%>">			             
			            </td>
        			</tr>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>