<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0024";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&idTiers=" + ((request.getParameter("idTiers")!=null)?request.getParameter("idTiers"):"") ;
	actionNew  += "&_tabName=Banque";
	rememberSearchCriterias = true;	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<script>
		function cr1Changed() {
			if (document.getElementById('cr1').value == 'crPays') {
				document.getElementById('cr1PaysSpan').style.display="inline";
				document.getElementById('cr1Text').style.display="none";
				
				
			} else {
				document.getElementById('cr1PaysSpan').style.display="none";
				document.getElementById('cr1Text').style.display="inline";
				
			}
		}
		function cr2Changed() {
			if (document.getElementById('cr2').value == 'crPays') {
				document.getElementById('cr2PaysSpan').style.display="inline";
				document.getElementById('cr2Text').style.display="none";
			} else {
				document.getElementById('cr2PaysSpan').style.display="none";
				document.getElementById('cr2Text').style.display="inline";
			}
		}
		function cr3Changed() {
			if (document.getElementById('cr3').value == 'crPays') {
				document.getElementById('cr3PaysSpan').style.display="inline";
				document.getElementById('cr3Text').style.display="none";
			} else {
				document.getElementById('cr3PaysSpan').style.display="none";
				document.getElementById('cr3Text').style.display="inline";
			}
		}
		function cr4Changed() {
			if (document.getElementById('cr4').value == 'crPays') {
				document.getElementById('cr4PaysSpan').style.display="inline";
				document.getElementById('cr4Text').style.display="none";
			} else {
				document.getElementById('cr4PaysSpan').style.display="none";
				document.getElementById('cr4Text').style.display="inline";
			}
		}
	

</script>

<SCRIPT>

bFind = false;
usrAction = "pyxis.adressepaiement.adressePaiementData.list";

<% 

String libelleValue ="";
if((request.getParameter("createdId")!= null)&& (!"".equals(request.getParameter("createdId")))) { 
libelleValue= request.getParameter("createdId");
%>

bFind = true;
<%
}
%>



</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><span style="font-family:wingdings;font-weight:normal">*</span> - Zahlungsadresse<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
		<tr>
			<td valign="top">
				<table>
				<tr>
					<td>Konto-Nr.</td>
					<td>
						<input type="text" name="forCompteLike" class="libelleLong" onkeypress="document.getElementsByName('forIdAdressePaiement')[0].value='';">
			   		</td>
			   	</tr>
			   	<tr>
			   		<td>Status</td>
			   		<td>
						<ct:FWCodeSelectTag name="forCode"
							    defaut=""
								wantBlank="<%=true%>"
							    codeType="PYIBAN"/>
					</td>
				</tr>
				</table>
			</td>
			<td valign="top">
				<table>
				<tr>
				<td>
				<select name="crPaiement">
					<option value="crCcp">Postkonto-Nr.</option>
					<option value="crClearing">Clearing</option>
					<option value="crSwift">Swift</option>
					<option value="crIban" >Iban</option>
				</select>
				</td>
				<td>
				<input name="crPaiementText" type="text"  onkeypress="document.getElementsByName('forIdAdressePaiement')[0].value='';">
				</td>
				</tr>
				
				</table>
			</td>
			
		</tr>
		<tr>
			<td colspan="2"><hr><b>Begünstigter : </b></td>
		</tr>
		<tr>
			<td>
				<select name="cr1" id="cr1" onchange="cr1Changed()">
					<option value="crLocalite">Ort</option>
					<option value="crNpa">PLZ</option>
					<option value="crPays">Land</option>
					<option value="crNomPrenom">Name,Vorname</option>
					<option value="crDestinataire">Empfänger</option>
					<option value="crAvs" selected >SVN</option>
					<option value="crAffilie">Abr.-Nr.</option>
					<option value="crContribuable"><ct:FWLabel key='NUMERO_CONTRIBUABLE' /></option>
				</select><span id="cr1Text">
					<input name="cr1Text" class="libelleLong" type="text" onkeypress="document.getElementsByName('forIdAdressePaiement')[0].value='';">
				</span>
				<span id="cr1PaysSpan" style="display:none">
					<ct:FWListSelectTag name="cr1Pays" 
            			defaut=""
            			data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>
				</span>
			</td>
			<td>
				&nbsp;&nbsp; 
				<select name="cr2" id="cr2" onchange="cr2Changed()">
					<option value="crLocalite" selected>Ort</option>
					<option value="crNpa">PLZ</option>
					<option value="crPays">Land</option>
					<option value="crNomPrenom">Name,Vorname</option>
					<option value="crDestinataire">Empfänger</option>
					<option value="crAvs" >SVN</option>
					<option value="crAffilie">Abr.-Nr.</option>
					<option value="crContribuable"><ct:FWLabel key='NUMERO_CONTRIBUABLE' /></option>
				</select>
			
				<span id="cr2Text" style="display:inline">
					<input name="cr2Text" class="libelleLong" type="text" onkeypress="document.getElementsByName('forIdAdressePaiement')[0].value='';">
				</span>
				<span id="cr2PaysSpan" style="display:none">
					<ct:FWListSelectTag name="cr2Pays" 
            			defaut=""
            			data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>
				</span>

				<input type="hidden" name="forIdAdressePaiement" value="<%=libelleValue%>">
			</td>
			<td></td>
		</tr>
		<tr>
			<td colspan="2"><hr><b>Verwaltung : </b></td>
		</tr>
		<tr>
			<td colspan="2">
					
					Art <ct:FWCodeSelectTag name="forGenreAdministration"
			         	  defaut=""
						  codeType="PYGENREADM"
						  wantBlank="true"/>
				
					Kanton <ct:FWCodeSelectTag name="forCantonAdministration"
							defaut=""
							codeType="PYCANTON"
							wantBlank="true"
							/>
			
					Code <input name="forCodeAdministration" type="text" value="" size="8"> 
					</td>
		</tr>
		
		






<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
<!--
			<%=(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection")%>'><IMG name="btnNew" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnNew.gif" border="0"></A>
			
			
			
-->


<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>