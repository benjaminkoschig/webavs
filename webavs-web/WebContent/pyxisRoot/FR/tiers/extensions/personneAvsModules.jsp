<%@ page import="globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");
boolean creation = true;
if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { 
	creation = false;
}
%>

<tr >
	<td colspan="4">
	<script>
		function selectModule(elem) {
			var module = elem.value;
			
			for (i=0;i<elem.options.length;i++) {
				document.getElementById(elem.options[i].value).style.display="none";
			}
			document.getElementById(elem.value).style.display="block";
			
		}
	
	</script>
	
	<hr size="1" noshade style="color:gray"></td>

</tr>

<!-- information relative au rôles -->
	
<tr>
	<td colspan="4" >
	
	<style>
	.moduleContainer{
		border-top:solid 1px black;
		border-left:solid 1px black;
		border-bottom:solid 2px black;
		border-right:solid 2px black;
		background:#Bed0ea;
		height:100%;
	}
	
	</style>
	
	<!--
	<span  style=overflow:auto;width:800;height:240">
	-->
	<table border=0 >
	<tr>
	<%	
		
		java.util.ArrayList modules = viewBean.getModules(false);
		java.util.Iterator itListe = modules.iterator();
		int nbExt = 0;
		
		
		while (itListe.hasNext()) {
				
				if (viewBean.hasExtension((String)itListe.next())) {
			 		nbExt++;
				}
		}
		
		itListe = modules.iterator();
		boolean selectorOn = (nbExt >3);
		
		if (selectorOn) {%>
			<td valign="top" align="left" height="100%">
					<table  align="left" border=0 class="moduleContainer"  style="display:block">
					<!--<tr><th colspan="2" class="moduleTitle" align="left">Select</th></tr>-->
					<tr><td><select id="extSelectorId" size="13" onchange="selectModule(this)">
			<%
			while (itListe.hasNext()) {
				String extName = (String)itListe.next();
				if (viewBean.hasExtension(extName)) {
			 		String extLibelle = viewBean.getExtension(extName).getLibelle(viewBean.getSession());
					%>
					<option value="<%=extName%>"><%=extLibelle%></option>
					
					<%
				}
			}%>
		
					</select></td></tr>		  		
				  <tr height="100%"><td></td></tr>
				</table>
			</td>

		<%} // fin selectorOn


		java.util.Iterator it = modules.iterator();
		boolean first = true;
		while (it.hasNext()) {
			String extName = (String)it.next();
			if (viewBean.hasExtension(extName)) {
		 		String pageName = viewBean.getExtension(extName).getExtensionScreenName();
				%><td valign="top" align="left" height="100%">
					<table id ="<%=extName%>" align="left" border=0 class="moduleContainer"  style="display:<%=((selectorOn&&(!first))?"none":"block")%>">
					<tr><th colspan="2" class="moduleTitle" align="left"><%=viewBean.getExtension(extName).getLibelle(viewBean.getSession())%></th></tr>
				  		<jsp:include page="<%=pageName%>" flush="true" />
				  		<tr height="100%"><td></td></tr>
					</table>
					</td>
				
	<%			first = false;
			}
		}
	%>
	</tr>
	</table>		
	<!--
	</span>
	-->
	</td>
</tr>
	
	
