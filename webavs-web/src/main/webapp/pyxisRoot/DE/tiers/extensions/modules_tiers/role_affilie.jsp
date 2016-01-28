<%@ page import="globaz.pyxis.extensions.*,globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");

%>
<tr>
	<td >
	<!-- 
#########################################################################
	
 SCRIPT DU MODULES	
	
######################################################################### 
-->	
	<script>
		var elemIdAffilie = "EXT_TIERS_MODULE_ROLE_<%=TIRole.CS_AFFILIE%>";	
		var fieldNameAffilie = "typeTiers";
		/*
		 * Affiche ou non le module suivant les valeurs du viewBean.
		 * si on n'affiche pas le module, on fait aussi un reset des champ du module
		 */
		 
		<%if (!((viewBean.CS_AFFILIE.equals(viewBean.getTypeTiers()))
		||(viewBean.CS_ASSUREAFFILIE.equals(viewBean.getTypeTiers())))) {%>
			document.getElementById(elemIdAffilie).style.display="none";
			clearModuleFieldsAffilie();	
		<%}%>
		
		function clearModuleFieldsAffilie() {
			var elems = document.getElementById(elemIdAffilie).getElementsByTagName('input');
			for (i=0;i<elems.length;i++) {
				if (elems[i].type.toUpperCase() !="BUTTON") {
					elems[i].value="";
					elems[i].checked="";
				}
			}
			var elems = document.getElementById(elemIdAffilie).getElementsByTagName('select');
			for (i=0;i<elems.length;i++) {
				elems[i].value="";
			}		
		}
			
		function toggleVisibilityAffilie() {
			try {
				elemToHide = document.getElementById(elemIdAffilie);
				elem = document.getElementById(fieldNameAffilie);
				
				if (! ( (elem.value==<%=viewBean.CS_ASSUREAFFILIE%>) ||  (elem.value==<%=viewBean.CS_AFFILIE%>)  ) )
				 {
				 	elemToHide.style.display="none";
				 	clearModuleFieldsAffilie();
				} else {
					elemToHide.style.display="block";
				}
			} catch(ex){
				alert(ex);
			} 
		}
			
		/*
		 * Registre une action sur le onchange d'un element
		 */
		try {

			// code a ajouter
			
			var newCodeAffilie ="toggleVisibilityAffilie();";
			
			var code = new String(document.getElementsByName(fieldNameAffilie)[0].onchange);
			if (code != null) {

				rExp = /\{/g;
				code = code.replace(rExp,"");
				rExp = /\}/g;
				code = code.replace(rExp,"");
				rExp = /function.*/g;
				code = code.replace(rExp,"");
			}
			<%
			// si on est en method add, les module peuvent être caché
			if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equalsIgnoreCase("add"))) { %>
				document.getElementsByName(fieldNameAffilie)[0].onchange = new Function ( code+";"+newCodeAffilie);
			<%}%>
		} catch( ex) {}

		/* fin de l'ajout de l'action */
	</script>

<!-- 
#########################################################################
	
 CHAMPS DU MODULES	
	
######################################################################### 
-->
	
	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueNumAffilie()"><%}%>
    N°Affilié
    <%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	</td>
	<td >
		<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>
			<INPUT class="libelleDisabled"  readonly type="text" value="<%=viewBean.getNumAffilieActuel()%>">

		<%} else {%>
		<INPUT class="libelle"  name="numAffilieActuel" type="text" value="<%=viewBean.getNumAffilieActuel()%>">
		<%}%>
	</td>
</tr>
<!--
<tr>
	<td>N°OP</td>
	<td><input type="text"></td>
</tr>
-->

<tr>
	<td>Du</td>
	<td>
		<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equalsIgnoreCase("add"))) {%>
	 		<ct:FWCalendarTag name="debutActivite" 
				value="<%=viewBean.getDebutActivite()%>" />
		<%} else {%>
			<b><%=viewBean.getDebutActivite()%></b>
		<%}%>
	</td>
</tr>
<tr>
	<td>Au</td>	  
	<td>
	  		<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equalsIgnoreCase("add"))) {%>
	 		<ct:FWCalendarTag name="finActivite" 
				value="<%=viewBean.getFinActivite()%>"/>
		<%} else {%>
			<b><%=viewBean.getFinActivite()%></b>
		<%}%>
	</td>
</tr>
<tr>
	<td>Genre</td>
	<td>
		<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equalsIgnoreCase("add"))) {%>
	     <ct:FWCodeSelectTag name="genreAffilie"

                		defaut="<%=viewBean.getGenreAffilie()%>"
                		codeType="VETYPEAFFI" 
	     />
		<%} else {%>
			<b><%=viewBean.getSession().getCodeLibelle(viewBean.getGenreAffilie())%></b>
		<%}%>
	</td>
</tr>


