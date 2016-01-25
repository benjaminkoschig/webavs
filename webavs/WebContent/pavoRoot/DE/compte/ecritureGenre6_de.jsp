<%@page import="globaz.globall.util.JANumberFormatter"%>
<%try{ %>
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.pavo.db.compte.CIEcritureGenre6ViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%	
	CIEcritureGenre6ViewBean viewBean = (CIEcritureGenre6ViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.compte.ecritureGenre6.executer";
		idEcran ="CCI3011";
		String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
		
		//On cache le montant pour les utilisateurs n'ayant pas assez de droits
		viewBean.giveEcriture().cacherMontantSiProtege(false);
		
		//si le montant est caché on masque le bouton valider
		if(viewBean.giveEcriture().getCacherMontant()){
			showProcessButton=false;
		}
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- tpl:put name="zoneScripts" --%>
<script>
function updateForm(tag){
	field = document.forms[0].elements('nomPrenom');
	if (tag.select) {
		nom = tag.select[tag.select.selectedIndex].nom;
		field.value = nom;
		document.forms[0].elements('dateNaissance').value = tag.select[tag.select.selectedIndex].date;
		document.forms[0].elements('sexe').value = tag.select[tag.select.selectedIndex].sexeFormate;
		document.forms[0].elements('pays').value = tag.select[tag.select.selectedIndex].paysFormate;
	}
}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Buchung einem existierten IK zuteilen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
								<td>SVN (eingegeben)</td><td> <input type ="text" class='disabled' readonly tabindex='-1' size = "16" value ="<%=viewBean.getNss()%>"></td>
							<tr>
								<td>Name, Vorname</td><td> <input type ="text" class='disabled' readonly tabindex='-1' size = "80" value ="<%=viewBean.getNomPrenom()%>"></td>
							</tr>	
							<tr>
								<td>
									Arbeitgeber
								</td>
								<td>
								<input type = "text" size="40" value="<%=viewBean.giveEcriture().getNoNomEmployeur()%>" class='disabled' readonly tabindex='-1'>
								</td>
							</tr>
							<tr>
								<td>
									Schlüsselzahl
								</td>
								<td>
									<input class='disabled' readonly tabindex='-1' size="3" maxlength="3"
								value="<%=viewBean.giveEcriture().getGreFormat()%>">
								</td>
							</tr>
							<tr>
						<td>Periode</td>
						<td colspan="">
							<input size='3' class='disabled' readonly tabindex='-1'
							value='<%=viewBean.giveEcriture().getMoisDebutPad()%>'> <b> - </b> 
							<input size='3'  class='disabled' readonly tabindex='-1'
							value='<%=viewBean.giveEcriture().getMoisFinPad()%>'> <b> . </b> 
							<input size='5' class='disabled' readonly tabindex='-1' 
							value='<%=JANumberFormatter.formatZeroValues(viewBean.giveEcriture().getAnnee(),false,true)%>'>

						</td>
							<tr>
								<td>
									Betrag
								</td>
								<td>
									<input class='disabled' readonly size='12' value="<%=viewBean.giveEcriture().getMontantFormat()%>" style="text-align: right" tabindex="-1">
								</td>
							</tr>
							<tr>
								<td>
									Ziel IK
								</td>
								<td>
								<nss:nssPopup
								validateOnChange="false" onChange="updateForm(tag);"
								name="avs" jspName="<%=jspLocation%>"
								avsMinNbrDigit="8" avsAutoNbrDigit="11"
								nssMinNbrDigit="7" nssAutoNbrDigit="10"
								value=""
								
							/>
							<input name='nomPrenom' class='disabled' size="70" readonly
							value="<%=viewBean.getNomPrenomDest()%>" tabindex="-1"> 
								</td>
								
							</tr>
							<tr>
						<td>
							&nbsp;
						</td>
						<td colspan="4">
							Geburtsdatum &nbsp;
							<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "">
							&nbsp;Geschlecht &nbsp;
							<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "">
							Heimatstaat &nbsp;
							<input type="text" size = "45" class='disabled' name="pays" readonly tabindex='-1' value = "">
						</td>
					
					</tr>
							
						
							
							<tr>
								<td>E-Mail</td><td><input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmailAddress()%>">&nbsp;</td>
							</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
<%}catch(Exception e){
	e.printStackTrace();
}%>