<%@ page import="globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.commons.nss.*"%>
   
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");
%>
<tr>
	<!-- col1 -->	
	<td valign="top">
		<table>
			<tr>
	<td>
 	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueTitre()"><%}%>
	Anrede
	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	</td>
				<td>
					<input  type="hidden" name="typeTiers" value="500006">
			        <ct:FWCodeSelectTag name="titreTiers"
			            	defaut="<%=viewBean.getTitreTiers()%>"
					codeType="PYTITRE" wantBlank="true"/>
					<!-- champs caché pour la creation de l'historique -->
					<input type ="hidden" name="motifModifTitre" value="">
					<input type ="hidden" name="dateModifTitre" value="">
					<input type ="hidden" name="oldTitre" value="<%=viewBean.getOldTitre()%>">
					<script>
						document.getElementById("titreTiers").accessKey=" " 
					</script>
					
				</td>
			</tr>
			<tr>
	<td>
	
 	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueDesignation1()"><%}%>
	Name oder Firmenname
	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	
	</td>
	<td>
		<INPUT   name="designation1" maxlength="40" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getDesignation1(),"\"","&quot;")%>"  onblur="fieldFormat(this,'')" class="libelleLong">
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifDesignation1" value="">
		<input type ="hidden" name="dateModifDesignation1" value="">
		<input type ="hidden" name="oldDesignation1" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getOldDesignation1(),"\"","&quot;")%>">

	</td>
			</tr>
			<tr>
			
			
			
	<td>
 	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueDesignation2()"><%}%>
	Vorname oder Firmenname
	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	</td>
	<td>
	  <INPUT  name="designation2" maxlength="40" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getDesignation2(),"\"","&quot;")%>"  onblur="fieldFormat(this,'')" class="libelleLong">
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifDesignation2" value="">
		<input type ="hidden" name="dateModifDesignation2" value="">
		<input type ="hidden" name="oldDesignation2" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getOldDesignation2(),"\"","&quot;")%>">
	</td>
			</tr>
			<tr>
	<td>
 	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueDesignation3()"><%}%>
	Name (weiter) 1
	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	</td>
	<td>
	  <INPUT  name="designation3" maxlength="40" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getDesignation3(),"\"","&quot;")%>" class="libelleLong">
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifDesignation3" value="">
		<input type ="hidden" name="dateModifDesignation3" value="">
		<input type ="hidden" name="oldDesignation3" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getOldDesignation3(),"\"","&quot;")%>">

	</td>
			</tr>
			<tr>
	<td>
 	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueDesignation4()"><%}%>
	Name (weiter) 2
	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	</td>
	<td>
		<INPUT  name="designation4" maxlength="40" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getDesignation4(),"\"","&quot;")%>" class="libelleLong">
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifDesignation4" value="">
		<input type ="hidden" name="dateModifDesignation4" value="">
		<input type ="hidden" name="oldDesignation4" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getOldDesignation4(),"\"","&quot;")%>">
	</td>
			</tr>
			<tr>
	<td>
		 <%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueNumAvs()"><%}%>
	   	 SVN
	    <%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	</td>
	<td>
		<!--
		<INPUT   name="numAvsActuel" type="text" class="libelleLong"  value="<%=viewBean.getNumAvsActuel()%>"  onblur="avsAction(this)">
		-->
		
		<%
			String ean13 ="false";
			String prefxieNSS = globaz.pyxis.application.TIApplication.pyxisApp().getNSSPrefixe();
			if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { 
		      if (viewBean.getNumAvsActuel().length()==16) {
		        ean13 ="true";
		        if (viewBean.getNumAvsActuel().startsWith(prefxieNSS)) {
		            viewBean.setNumAvsActuel(viewBean.getNumAvsActuel().substring(3));
		        }
		      } else {
		        ean13 ="false";
		      }     
		    } else {
	          ean13 =viewBean.getNumAvsActuelNNSS();
	          if ("true".equals(ean13)) {
	            if (viewBean.getNumAvsActuel().startsWith(prefxieNSS)) {
	              viewBean.setNumAvsActuel(viewBean.getNumAvsActuel().substring(3));
	            }
		      }
		    }
			
			
		%>
		<ct1:nssPopup prefixePays="<%=globaz.pyxis.application.TIApplication.pyxisApp().getNSSPrefixe()%>"  avsMinNbrDigit="99" nssMinNbrDigit="99" newnss="<%=ean13%>" name="numAvsActuel" value="<%=viewBean.getNumAvsActuel()%>" onChange="avsAction(document.getElementsByName('numAvsActuel')[0])"/>
		
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifAvs" value="">
		<input type ="hidden" name="dateModifAvs" value="">
		<input type ="hidden" name="oldNumAvs" value="<%=viewBean.getOldNumAvs()%>">
		
	</td>
			</tr>
			<tr>
<td>Geburtsdatum</td>
	<td nowrap>
		<ct:FWCalendarTag name="dateNaissance" 
			value="<%=viewBean.getDateNaissance()%>" 
			doClientValidation="CALENDAR"
			/>
		
		Todesdatum 
		
			<ct:FWCalendarTag name="dateDeces" 
				value="<%=viewBean.getDateDeces()%>"
				doClientValidation="CALENDAR"/>
		
	</td>			
	</tr>
			<tr>
			<td>Geschlecht</td>
	<td>
	  <ct:FWCodeRadioTag name="sexe"
			defaut="<%=viewBean.getSexe()%>"
			codeType="PYSEXE"
			orientation="H"
			libelle="libelle" />
		
	</td></tr>
		</table>
	</td>
	
	<!-- col2 -->	
	<td valign="top">
		<table>
			<tr>
				<td>Zivilstand</td>
				<td>
					<ct:FWCodeSelectTag name="etatCivil" defaut="<%=viewBean.getEtatCivil()%>" codeType="PYETATCIVI" wantBlank="true" />	
					<script>
						
						document.getElementById("etatCivil").accessKey=" "; 
					</script>
				</td>
			</tr>
		
	<tr>
	<td><ct:FWLabel key="NOM_JEUNE_FILLE"/></td>
	<td>
		<INPUT name="nomJeuneFille" maxlength="40" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getNomJeuneFille(),"\"","&quot;")%>"  onblur="fieldFormat(this,'')" class="libelleLong">
	</td>
	</tr>

	<tr>			
	<td>Sprache</td>
		<td>
		  <ct:FWCodeSelectTag name="langue"
		    defaut="<%=viewBean.getLangue()%>"
	      codeType="PYLANGUE"/>
		
	</td>			
	</tr>
	<tr>
	<td>
 	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><a  href="javascript:HistoriqueNationalite()"><%}%>
	Nationalität
	<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></a><%}%>
	</td>
	<td nowrap><span>
		
		<ct:FWListSelectTag name="idPays" 
            		defaut="<%=viewBean.getIdPays()%>"
            		data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>
		
		
	<script>
	/*
		var typed
	reset = window.setInterval("typed = ''",500);
	select = document.getElementById("idPays");
	function clickPays(){
		window.clearInterval(reset);
		reset = window.setInterval("typed = ''",500);
		typed += String.fromCharCode(event.keyCode);

		this.value=  findValueInSelect(this,typed);
	}
	function findValueInSelect(elem,typed) {
		for (i =0;i<elem.options.length;i++) {
			if(
				(elem.options[i].value.toUpperCase()==typed.toUpperCase())	
			){
				return elem.options[i].value;
			}
		}
		return "";
	}

	select.onkeypress = clickPays;
*/
		</script>
		
		
		
		</span>
		
		
		
		
		
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifPays" value="">
		<input type ="hidden" name="dateModifPays" value="">
		<input type ="hidden" name="oldPays" value="<%=viewBean.getOldPays()%>">
	</td>			
	</tr>
	<%globaz.pyxis.application.TIApplication app = (globaz.pyxis.application.TIApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("PYXIS");
			 if (app.isIdTiersExterneVisible()) { %>
	<tr>
		<td>N°Interne</td>
		<td>
			
			<%  if (app.isIdTiersExterneReadOnly()) {%>
				<input type ="text" class="libelleLongDisabled" readonly value="<%=viewBean.idTiersExterneFormate()%>" >
			<% } else {%>
				<input name="idTiersExterne" type ="text" class="libelleLong" value="<%=viewBean.idTiersExterneFormate()%>" >
			<%}%>
			
		</td>
	</tr>
	<%}%>
	<tr>
	<td nowrap>
		<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %><A  href="javascript:HistoriqueNumContribuable()"><%}%>
	    <ct:FWLabel key='NUMERO_CONTRIBUABLE' /><%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %></A><%}%>
	</td>
	<td>
		<INPUT  name="numContribuableActuel" class="libelleLong" type="text" value="<%=viewBean.getNumContribuableActuel()%>">
		<!-- champs caché pour la creation de l'historique -->
		<input type ="hidden" name="motifModifContribuable" value="">
		<input type ="hidden" name="dateModifContribuable" value="">
		<input type ="hidden" name="oldNumContribuable" value="<%=viewBean.getOldNumContribuable()%>">
	</td>
	</tr>
	<tr>
	<td>Natürliche Person
	</td>
	<td>
		<input type="checkbox" name="personnePhysique" <%=(viewBean.getPersonnePhysique().booleanValue())?"CHECKED":""%>>
		&nbsp;&nbsp;Inaktiv <input  type="checkbox" name="inactif" <%=(viewBean.getInactif().booleanValue())?"CHECKED":""%>>
		
	</td>
			</tr>
	
	
	<tr>
		<td>&nbsp;</td>
		<td></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td></td>
	</tr>
	
	<tr>
	<td>&nbsp;</td>
	<td>
	</td>
	</tr>
		</table>
	</td>
</tr>

<!-- fin -->


	
	
	

	



	

	
	
	

	




