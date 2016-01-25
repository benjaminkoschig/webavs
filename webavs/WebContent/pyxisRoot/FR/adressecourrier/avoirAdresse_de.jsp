<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->

<%
	idEcran ="GTI0019";
    globaz.pyxis.db.adressecourrier.TIAvoirAdresseViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIAvoirAdresseViewBean)session.getAttribute ("viewBean");
    selectedIdValue = viewBean.getIdAdresseIntUnique();
    bButtonNew = objSession.hasRight(userActionNew, "ADD");
    if ("add".equals(request.getParameter("_method"))) {
	    bButtonValidate = objSession.hasRight(userActionNew, "ADD");
	    bButtonCancel = objSession.hasRight(userActionNew, "ADD");
    } else {
	    bButtonValidate = objSession.hasRight(userActionNew, "UPDATE");
	    bButtonCancel   = objSession.hasRight(userActionNew, "UPDATE");
    }
    
   
       actionNew += "&idTiers="+viewBean.getIdTiers();
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<style>
	.navLinkTiers {
	cursor:hand;
	border : 0 0 0 0;
	color:blue;
	text-decoration:underline;
	background : #B3C4DB;
	
	margin : 0 0 0 0;
	padding : 0 0 0 0;
	width : 100%;
	font-weight:normal;
	font-size: 8pt
	
	}
</style>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->


top.document.title = "Tiers - Adresse suite détail"
var historyMaximized = <%=(viewBean.isAllAdressesMaximized())?"true":"false"%>;
function add() {
    document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.ajouter"
    
    // typeAdresse
    try {
    var idx = 0;
    for (idx=0;idx< document.forms[0].elements('typeAdresse').options.length;idx++) {
     if (document.forms[0].elements('typeAdresse').options[idx].value =="<%=viewBean.getTypeAdresse() %>"){
      break;
     }
    }
    document.forms[0].elements('typeAdresse').options[idx].selected = true;    
	} catch (e)  {}
	


}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.modifier";
	


    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add"){
 	document.forms[0].elements('userAction').value="";
 	top.fr_appicons.icon_back.click();
  }
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.supprimer";
        document.forms[0].submit();
    }
}


function init(){

}

function postInit(){
	<%if (!"add".equals(request.getParameter("_method"))) {%>
		try {
		document.getElementById('copy').disabled='';
		document.getElementById('copyToIdApplication').disabled='';
		} catch (e) {}
	<%}%>
	var navLinks = document.getElementsByName("navLink");
	
	for (i=0;i<navLinks.length;i++) {
			navLinks[i].disabled = '';
	}
}
function onClickCopy() {
	if (document.getElementsByName("copyToIdApplication")[0].value !="") {
		document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.executer";
		action(COMMIT);
	}
}



function toggleMinMax() {
	if (document.getElementById('maintd').style.display!='none') {
		document.getElementById('maintd').style.display='none';
		historyMaximized = innerFrame.document.getElementsByName('toggleHist')[0].checked;
		if (!innerFrame.document.getElementsByName('toggleHist')[0].checked) {
			innerFrame.document.getElementsByName('toggleHist')[0].click();
		} 
		
	} else {
		document.getElementById('maintd').style.display='block';
		if (historyMaximized != innerFrame.document.getElementsByName('toggleHist')[0].checked)
		{  
			innerFrame.document.getElementsByName('toggleHist')[0].click();  
		}
	}
}


/*
*/
// stop hiding -->
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><span style="font-family:wingdings;font-weight:normal">+</span><span style="font-family:webdings;font-weight:normal">€</span> - Attacher une adresse de courrier
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

<tr>
<td width="100%">

<table id="innerTable" border ="1" width="100%" height="100%">
<tr>

<td  id="maintd" width="400" valign="top" height="100%" >

<table >
	<%if ("add".equals(request.getParameter("_method"))) {
		
	%>
		

		<tr><td colspan=3>
		<%if (request.getAttribute("warn")!= null) {%>
			<div style="padding : 0.4cm;border:solid 1 px black;background:#ffffcc">
			<b>Avertissement :</b> <%=request.getAttribute("warn")%>
			</div>
		<%}%>
		
		
		
		
		
			<h3><u>Nouveau</u></h3>
		</td></tr>
	<%}%>

     <tr>
       <TD colspan=2>
		<span style="font-family:webdings;font-size:12 pt">€</span>
		<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Tiers</A>
	<br>
	<INPUT name="tiers" type="text" value="<%=globaz.globall.util.JAUtil.replaceString(viewBean.getNom(),"\"","&quot;")%>" class="libelleLongDisabled" readonly></td>
      </tr>

	<tr>
		<td><INPUT type="hidden" name="ecran" value='<%=session.getAttribute("ecran")%>'>
	</td> 
	</tr>
	<tr>
            <TD nowrap colspan=2><span style="font-family:wingdings;font-size:12 pt">+</span> Adresse<br>
		<TEXTAREA rows="8" cols="25"  readonly class="libelleLongDisabled" style="font-weight:bold;font-size:8pt" ><%=viewBean.getDetailAdresse()%></TEXTAREA>
		
		<%
		Object[] adresseMethodsName = new Object[]{
			new String[]{"setIdAdresse","getIdAdresseUnique"}
		};
		// pour remplir automatiquement le nom et prenom ds la creation d'une adresse
		Object[] adresseParams= new Object[]{new String[]{"idTiers","idTiers"} };
		%>
		<ct:FWSelectorTag 
		name="adresseSelector" 
		
		methods="<%=adresseMethodsName %>"
		providerApplication ="pyxis"
		providerPrefix="TI"
		providerAction ="pyxis.adressecourrier.adresse.chercher"
		providerActionParams ="<%=adresseParams%>"
		/>
		</td>
		
      </tr>
     
	<tr>
		<td><INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>'>
		</td>
	</tr>
	
	<tr>
	        <td colspan=2><br><span style="font-family:wingdings;font-size:12 pt">1</span>Domaine<br>
	        <%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
			<ct:FWCodeSelectTag 
				name="idApplication" 
				defaut="<%=viewBean.getIdApplication()%>" 
				codeType="PYAPPLICAT"
				except="<%=viewBean.getExceptIdApplication()%>"
				
				/>
			<%} else {%>
	 		<input  type="text" readonly class="libelleLongDisabled" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getIdApplication())%>" >
 		<%}%>
	 	</td>			
	</tr> 
	 				
	<tr>
	        <td colspan=2><br>Type<br><ct:FWCodeSelectTag 
				name="typeAdresse" 
				defaut="<%=viewBean.getTypeAdresse()%>" 
				codeType="PYTYPEADR"/>
               
	 	</td>			
	</tr>
	<tr>
		<td colspan=2>
	<%
		java.util.Vector listIdExterne = viewBean.getIdExterneList();
		if (listIdExterne.size()>1) { %>
			<br>Numéro<br>
			<ct:FWListSelectTag name="idExterne" data="<%=listIdExterne%>" defaut="<%=viewBean.getIdExterne()%>"/>
	<% } else { %>
	   		<INPUT type="hidden" name="idTiidExterneers" value='<%=viewBean.getIdExterne()%>'>
	<% } %>
		</td>
	</tr>
	<tr>
	       <td colspan=2 nowrap><br>Du
			<ct:FWCalendarTag name="dateDebutRelation" 
			value="<%=viewBean.getDateDebutRelation()%>"  />
		 au 
			<ct:FWCalendarTag name="dateFinRelation" 
			value="<%=viewBean.getDateFinRelation()%>"  /> 

		<INPUT type="hidden" name="stillInScenario" value="<%=request.getParameter("stillInScenario")%>">
		
		</td>
	</tr>

	<ct:ifhasright crud="c" element="pyxis.adressecourrier.avoirAdresse">
		<%if (viewBean.hasCopyToIdApplication()) {%>
		<%if (!((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")))) {%>
		<tr>
		<td colspan="2"><hr></td>
		</tr>
		<tr>
		
		<td colspan=2><ct:FWCodeSelectTag 
					name="copyToIdApplication" 
					defaut="" 
					codeType="PYAPPLICAT"
					wantBlank="true"
					except="<%=viewBean.getExceptIdApplication()%>"
					/>&nbsp;
					<input id="copy" value="Copier" type="button" onclick="onClickCopy()";></td>
		</tr>
		<%}}%>
	</ct:ifhasright>
	

</table>

</td>


<td id="adrframe" width="100%">
	<iframe id="innerFrame" scrolling="YES"  style="border : solid 1px black; width:100%;" height="100%" src="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.adressesTiers">
	</iframe>
</td>



</tr>
</table>
<% 
globaz.pyxis.application.TIApplication app = (globaz.pyxis.application.TIApplication)globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("PYXIS");
if (!app.hiddeNavigationBar()) { %>
<table cellpadding=0 cellspacing=0 width="100%">
			<tr>
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.tiers">
						<input name="navLink"  class="navLinkTiers" value="1 Recherche" accesskey="1" type="button" 
						onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.chercher'">
					</ct:ifhasright>
				</td>
		
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.tiers">
						<input name="navLink" class="navLinkTiers"  value="2 Tiers" accesskey="2" type="button" 
						onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdTiers()%>'">
					</ct:ifhasright>
				</td>
			
				<td>
					<ct:ifhasright crud="r" element="pyxis.adressecourrier.avoirAdresse">
						<input name="navLink" class="navLinkTiers"  value="3 Adresses de courrier" accesskey="3" type="button" 
						onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.adressecourrier.avoirAdresse.afficher&_method=add&back=_sl&idTiers=<%=viewBean.getIdTiers()%>'">
					</ct:ifhasright>
				</td>
	
				<td>
					<ct:ifhasright crud="r" element="pyxis.adressepaiement.avoirPaiement">
						<input name="navLink" class="navLinkTiers"  value="4 Adresses de paiement" accesskey="4" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.adressepaiement.avoirPaiement.afficher&_method=add&back=_sl&idTiers=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
		
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.avoirContact">
						<input name="navLink" class="navLinkTiers"  value="5 Contacts" accesskey="5" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.avoirContact.afficher&_method=add&idTiers=<%=viewBean.getIdTiers()%>'">
 		 			</ct:ifhasright>
 		 		</td>
		
				<td>
					<ct:ifhasright crud="r" element="naos.affiliation.affiliation">
						<input name="navLink" class="navLinkTiers"  value="6 Affiliation" accesskey="6" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\naos?userAction=naos.affiliation.affiliation.chercher&idTiers=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
		
				<td>
					<ct:ifhasright crud="r" element="pyxis.tiers.compositionTiers">
						<input name="navLink" class="navLinkTiers"  value="7 Liens entre tiers" accesskey="7" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.compositionTiers.chercher&idTiers=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
		
				<td>
					<ct:ifhasright crud="u" element="pyxis.tiers.compositionTiers">
						<input name="navLink" class="navLinkTiers"  value="8 Conjoint" accesskey="8" type="button" 
		 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.compositionTiers.dirigerConjoint&selectedId=<%=viewBean.getIdTiers()%>'">
	 		 		</ct:ifhasright>
	 		 	</td>
			</tr>
		</table>
<%}%>



<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>
	
<script>
	document.getElementById('innerTable').style.setExpression("height","document.body.clientHeight-150");
	document.getElementsByTagName('table')[0].style.setExpression("height","document.body.clientHeight-35");
</script>
	
<%if (!"add".equals(request.getParameter("_method"))) {%>
<ct:menuChange displayId="options" menuId="adresseCourrier-detail" showTab="options" checkAdd="no">
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdAdresse()%>"/>
</ct:menuChange>
<%} else {%>
<ct:menuChange displayId="options" menuId="tiers-detail" showTab="options" checkAdd="no">
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdTiers()%>" checkAdd="no"/>
<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>" checkAdd="no"/>
</ct:menuChange>


<% }%>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>