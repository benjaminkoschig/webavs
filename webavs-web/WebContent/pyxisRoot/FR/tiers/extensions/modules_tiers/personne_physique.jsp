<%@ page import="globaz.pyxis.extensions.*,globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");

%>
<tr>
	<td>
<!-- 
#########################################################################
	
 SCRIPT DU MODULES	
	
######################################################################### 
-->	
	<script>
		var elemId = "EXT_TIERS_MODULE_PERSONNE_PHYSIQUE";	
		
		/*
		 * Affiche ou non le module suivant les valeurs du viewBean.
		 * si on n'affiche pas le module, on fait aussi un reset des champ du module
		 */
		 
		<%if (!viewBean.getPersonnePhysique().booleanValue()) {%>
			document.getElementById(elemId).style.display="none";
			clearModuleFields();	
		<%}%>
		
		function clearModuleFields() {
			var elems = document.getElementById(elemId).getElementsByTagName('input');
			for (i=0;i<elems.length;i++) {
				if (elems[i].type.toUpperCase() !="BUTTON") {
					elems[i].value="";
					elems[i].checked="";
				}
			}
			var elems = document.getElementById(elemId).getElementsByTagName('select');
			for (i=0;i<elems.length;i++) {
				elems[i].value="";
			}		
		}
			
		function toggleVisibility() {
			try {
				elem = document.getElementById(elemId);
				visible = elem.style.display;
				if (visible =='block') {
				 	elem.style.display="none";
				 	clearModuleFields();
				} else {
					elem.style.display="block";
				}
			} catch(ex){
				alert(ex);
			} 
		}
			
		/*
		 * Registre une action sur le onclick d'un element
		 */
		try {

			// code a ajouter
			var fieldName = "personnePhysique";
			var newCode ="toggleVisibility();";
			
			var code = new String(document.getElementsByName(fieldName)[0].onclick);
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
				document.getElementsByName(fieldName)[0].onclick = new Function ( code+";"+newCode);
			<%}%>
			
		} catch( ex) {}

		/* fin de l'ajout de l'action */
	</script>

<!-- 
#########################################################################
	
 CHAMPS DU MODULES	
	
######################################################################### 
-->

	    <%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>
	    	<a href="javascript:HistoriqueNumAvs()">
	    <%}%>
	    N°AVS
	    <%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>
	    	</a>
	    <%}%>
		</td>
		<td>
			<INPUT  name="numAvsActuel" type="text" value="<%=viewBean.getNumAvsActuel()%>"  onblur="avsAction(this)">

			<!-- champs caché pour la creation de l'historique -->
			<input type ="hidden" name="motifModifAvs" value="">
			<input type ="hidden" name="dateModifAvs" value="">
			<input type ="hidden" name="oldNumAvs" value="<%=viewBean.getOldNumAvs()%>">
		</td>
</tr>

<tr>
<td>Sexe</td>
	<td>
	  <ct:FWCodeRadioTag name="sexe"
			defaut="<%=viewBean.getSexe()%>"
			codeType="PYSEXE"
			orientation="H"
			libelle="libelle" />
	</td>
</tr>
<tr>
	<td>Naissance</td>
	<td>
		<ct:FWCalendarTag name="dateNaissance" 
			value="<%=viewBean.getDateNaissance()%>" />
		Décès 
			<ct:FWCalendarTag name="dateDeces" 
				value="<%=viewBean.getDateDeces()%>"/>
	</td>
</tr>
<tr>
<td>Etat civil</td>
	<td>
		<ct:FWCodeSelectTag name="etatCivil" defaut="<%=viewBean.getEtatCivil()%>" codeType="PYETATCIVI" wantBlank="true" />	
	
	</td>
</tr>
<tr>
	<%if (!"".equals(viewBean.getIdConjoint())) {%>
	<td><A  href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdConjoint()%>">Conjoint</A></td>
	<%} else {%>
	<td>Conjoint</td>
	<%}%>
	<td>
		<textarea rows="3" tabindex="-1" name="conjoint" type="text" size="31" readonly class="libelleLongDisabled" style="font-weight:normal;font-size:8pt;" ><%=viewBean.getConjoint()%></textarea>
        <%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>
		<input  type="button" onClick="_meth.value=_method.value;userAction.value='pyxis.tiers.compositionTiers.dirigerConjoint';submit()" value="...">
		<%}%>
	   <INPUT type="hidden" name="idConjoint" value="<%=viewBean.getIdConjoint()%>">
	   <INPUT type="hidden" name="_meth" value="">	
	</td>
</tr>

