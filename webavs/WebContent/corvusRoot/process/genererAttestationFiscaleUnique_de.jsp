<%@ page language="java" errorPage="/errorPage.jsp"
	import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>

<%@page import="globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.corvus.vb.process.REGenererAttestationFiscaleUniqueViewBean"%>
<%@page import="globaz.corvus.vb.process.REGenererAttestationFiscaleViewBean"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Set"%>

<%@ include file="/theme/process/header.jspf"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1"%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/corvusRoot/process/genererAttestationFiscaleUnique_de.js"></script>

<%
	// Les labels de cette page commence par la préfix "JSP_ATT_FISC"
	idEcran = "PRE2007";
	
	REGenererAttestationFiscaleUniqueViewBean viewBean = (REGenererAttestationFiscaleUniqueViewBean) session.getAttribute("viewBean");
	FWController controller = (FWController) session.getAttribute("objController");
	String nss = viewBean.getNSS();

	if (!JadeStringUtil.isEmpty(viewBean.getIdTiers())) {
		userActionValue = IREActions.ACTION_GENERER_ATTESTATION_FISCALE_UNIQUE + ".executer";	
	} else {
		userActionValue = IREActions.ACTION_GENERER_ATTESTATION_FISCALE + ".afficher";
	}
%>

<%@ include file="/theme/process/javascripts.jspf"%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu" />

<!-- Modèle d'une ligne d'un des deux tableaux -->
<script type="text/template" id="templateRow" >
	<TR>
		<TD style="WIDTH: 40%"><TEXTAREA cols=60 data-usage="assure">{0}</TEXTAREA></TD>
		<TD style="WIDTH: 26%" align=center>
			<INPUT class=libelleLong data-usage="periode" value="{1}" >
		</TD>
		<TD>
			<DIV style="POSITION: relative; DISPLAY: inline">
			<INPUT style="TEXT-ALIGN: right" data-usage="montant" value="{2}" >
			<INPUT style="POSITION: absolute; LEFT: 180px" type="button" data-usage="deleteRow" value="<%=viewBean.getLabel("JSP_ATT_FISC_BOUTON_SUPPRIMER_LIGNE") %>">
			</DIV>
		</TD>
	</TR>
</script>

<%@ include file="/theme/process/bodyStart.jspf"%>
<ct:FWLabel key="JSP_ATT_FISC_UNIQUE_TITRE" />
<%@ include file="/theme/process/bodyStart2.jspf"%>
<%
	if (JadeStringUtil.isEmpty(viewBean.getIdTiers())) {
%>
<tr>
	<td colspan="7"><font color="#FF0000"> <strong> <ct:FWLabel
					key="ERREUR_NSS_INTROUVALBE_ATT_FISC" />
		</strong>
	</font> <font color="#FF0000"> <strong> <%=nss%>
		</strong>
	</font></td>
</tr>
<%
	} else {
%>
<tr>
	<td><input type="hidden" name="genererAttestation" value="true" />
		<input type="hidden" name="attTable_assure" value="" /> <input
		type="hidden" name="attTable_periode" value="" /> <input
		type="hidden" name="attTable_montant" value="" /> <input
		type="hidden" name="table_assure" value="" /> <input type="hidden"
		name="isSendToGed" value="<%=viewBean.getIsSendToGed()%>" /> <input
		type="hidden" name="table_periode" value="" /> <input type="hidden"
		name="table_montant" value="" /> <input type="hidden"
		name="anneeAttestations" value="<%=viewBean.getAnneeAttestations()%>" />
	</td>
</tr>
<tr>
	<td><label for="adresse"> <strong> <ct:FWLabel
					key="JSP_ATT_FISC_UNIQUE_ADRESSE" />
		</strong>
	</label> <br /> <textarea name="adresse" cols="40" rows="6"><%=viewBean.getAdresse()%></textarea>
		<br /></td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td><label for="concerne"> <strong> <ct:FWLabel
					key="JSP_ATT_FISC_UNIQUE_CONCERNE" />
		</strong>
	</label></td>
</tr>
<tr>
	<td><textarea name="concerne" cols="60" rows="2""><%=viewBean.getConcerne()%></textarea>
	</td>
</tr>
<tr>
	<td><textarea name="sousConcerne" cols="60" rows="2""><%=viewBean.getSousConcerne()%></textarea>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td><label for="titre"> <strong> <ct:FWLabel
					key="JSP_ATT_FISC_UNIQUE_TITRETIERS" />
		</strong>
	</label> <br /> <input type="text" name="titre"
		value="<%=viewBean.getTitre()%>" class="libelleLong" /></td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td><label for="para1"> <strong> <ct:FWLabel
					key="JSP_ATT_FISC_UNIQUE_PARAGRAPHE" />
		</strong>
	</label> <br /> <textarea name="para1" cols="85" rows="4"><%=viewBean.getPara1()%></textarea>
		<br /></td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td colspan="6">
		<%
			if (viewBean.getMapassure().isEmpty()) {
		%> <input id="cnt" type="hidden" value="1" /> <%
 	} else {
 %> <input id="cnt" type="hidden"
		value="<%=viewBean.getMapassure().size()%>" /> <%
 	}
 %>
		<table id="attTable" width="83%" border="1">
			<tbody>
				<tr>
					<th style="font-size: 11px;"><ct:FWLabel
							key="JSP_ATT_FISC_UNIQUE_ASSURE" /></th>
					<th style="font-size: 11px;"><ct:FWLabel
							key="JSP_ATT_FISC_UNIQUE_PERIODE" /></th>
					<th style="font-size: 11px;"><ct:FWLabel
							key="JSP_ATT_FISC_UNIQUE_MONTANT" /></th>
				</tr>
			</tbody>
		</table>
	</td>
</tr>
<tr>
	<td align="left"><input type="button" value="<%=viewBean.getLabel("JSP_ATT_FISC_BOUTON_AJOUTER_LIGNE") %>" data-usage="addRow"/>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td colspan="6">
		<%
			if (viewBean.getOVDesignation().isEmpty()) {
		%> <input id="cnt2" type="hidden" value="<%=1%>" /> <%
 	} else {
 %> <input id="cnt2" type="hidden"
		value="<%=viewBean.getOVDesignation().size()%>" /> <%
 	}
 %>
		<table id="Table" width="83%" border="1">
			<tbody>
			</tbody>
		</table>
	</td>
</tr>
<tr>
	<td align="left"><input type="button" value="<%=viewBean.getLabel("JSP_ATT_FISC_BOUTON_AJOUTER_LIGNE") %>" data-usage="addRow"/>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<%
	if (!JadeStringUtil.isEmpty(viewBean.getTitreAPI())) {
%>
<tr>
	<td><label for="API"> <strong> <ct:FWLabel
					key="JSP_ATT_FISC_UNIQUE_API" />
		</strong>
	</label></td>
</tr>
<tr>
	<td><textarea name="titreAPI" cols="85" rows="2""><%=viewBean.getTitreAPI()%></textarea>
	</td>
</tr>
<tr>
	<td><textarea name="paraAPI" cols="85" rows="2""><%=viewBean.getParaAPI()%></textarea>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<%
	}
%>
<tr>
	<td><label for="salutation"> <strong> <ct:FWLabel
					key="JSP_ATT_FISC_UNIQUE_SIGNATURE" />
		</strong>
	</label> <br /> <textarea name="salutation" cols="85" rows="3"><%=viewBean.getSalutation()%></textarea>
		<br /></td>
</tr>
<tr>
	<td>&nbsp;</td>
</tr>
<%
	}
%>
<%@ include file="/theme/process/footer.jspf"%>
<%@ include file="/theme/process/bodyClose.jspf"%>

<script type="text/javascript">
		// Méthode d'initialisation
		$(document).ready(function() {
			var assure, periode, montant;
			
			// Prépopulation des deux tableaux 
<%if (!viewBean.getMapassure().isEmpty()) {
	Set<String> keys = viewBean.getMapassure().keySet();
		
	ArrayList<String> list = new ArrayList<String>(keys);
	Collections.sort(list);
		
for (String key : list) {%>
			assure = "<%=viewBean.getMapassure().get(key)%>" + "\n" + "<%=viewBean.getMaplibelleRente().get(key)%>";
			periode = "<%=viewBean.getMapperiode().get(key)%>";
			montant = "<%=viewBean.getMapmontant().get(key)%>";
			addRow(attTable, assure, periode, montant);
<%}
} else {%>
			addRow(attTable, '','','');
<%}
		
	if (!viewBean.getOVDesignation().isEmpty()) {
		Set<String> keys = viewBean.getOVDesignation().keySet();
		
		ArrayList<String> list = new ArrayList<String>(keys);
		Collections.sort(list);
		
		for (String key : list) {%>
			v1 = "<%=viewBean.getOVDesignation().get(key)%>";
			v2 = "<%=viewBean.getOVType().get(key)%>";
			v3 = "<%=viewBean.getOVMontant().get(key)%>";
		
			addRow(Table, v1, v2, v3);
<%}
} else {%>
			addRow(Table, '', '', '');
<%}%>
		
			// Assignation du callback aux boutons "Effacer ligne"
			$("form").on("click","[data-usage='deleteRow']", function(){
				deleteRow(this);
			});
				
			// Assignation du callback aux boutons "Ajouter ligne"
			$("form").on("click","[data-usage='addRow']", function() {
				var table = $(this).parent().parent().prev().find("table");
					
				addRow(table,'','','');
			});
		});	
</script>