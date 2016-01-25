<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.leo.db.envoi.LEEnvoiListViewBean viewBean = (globaz.leo.db.envoi.LEEnvoiListViewBean) request.getAttribute("viewBean");
    size = viewBean.getSize();
	menuName="envoi-liste";
	detailLink = "leo?userAction=leo.envoi.envoi.afficher&selectedId=";
	String actionSearch = "";
	String trName = "";
	boolean firstLine = true;
	
	//on sauvegarde les paramètres de provenance
	String paramProvenance = "";
	for(int z=0;z<globaz.leo.db.envoi.LEEnvoiListViewBean.NBRE_MAX_CRITERE_ENTREE;z++){
		if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(z+1)) != null){
			paramProvenance+="&"+globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(z+1)+"=";
			paramProvenance+=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(z+1)));
		}
		if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(z+1)) != null){	
			paramProvenance+="&"+globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(z+1)+"=";
			paramProvenance+=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(z+1)));
		}
		if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(z+1)) != null){	
			paramProvenance+="&"+globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(z+1)+"=";
			paramProvenance+=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(z+1)));
		}
		if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(z+1)) != null){	
			paramProvenance+="&"+globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(z+1)+"=";
			paramProvenance+=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(z+1)));
		}
	}
	// on sauve egalement la source
	if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK) != null){
			paramProvenance+="&"+globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK+"=";
			paramProvenance+=java.net.URLDecoder.decode(globaz.globall.util.JAUtil.replaceString(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK),"\\","\\\\"));
	}
	// traitement au cas où la valeur d'un paramètre contient un '
	paramProvenance = paramProvenance.replace('\'',' ');
	//pareil pour les critères de recherche
	String criteresRecherche = "";
	if(request.getParameter("forDate")!=null){
		criteresRecherche+="&forDate="+java.net.URLDecoder.decode(request.getParameter("forDate"));
	}
	if(request.getParameter("forDateRappel")!=null){
		criteresRecherche+="&forDateRappel="+java.net.URLDecoder.decode(request.getParameter("forDateRappel"));
	}
	if(request.getParameter("forDateReception")!=null){
		criteresRecherche+="&forDateReception="+java.net.URLDecoder.decode(request.getParameter("forDateReception"));
	}
	if(request.getParameter("forLibelle")!=null){
		criteresRecherche+="&forLibelle="+java.net.URLDecoder.decode(request.getParameter("forLibelle"));
	}
	if(request.getParameter("forUserName")!=null){
		criteresRecherche+="&forUserName="+java.net.URLDecoder.decode(request.getParameter("forUserName"));
	}
	if(request.getParameter("forTypeDocument")!=null){
		criteresRecherche+="&forTypeDocument="+java.net.URLDecoder.decode(request.getParameter("forTypeDocument"));
	}
	criteresRecherche = criteresRecherche.replace('\'',' ');
	
	/***************************************************/
	String paramLibelle = request.getParameter("forLibelle")==null?"":request.getParameter("forLibelle");
	String paramForTypeDocument = request.getParameter("forTypeDocument")==null?"":request.getParameter("forTypeDocument");
	String paramForDate = request.getParameter("forDate")==null?"":request.getParameter("forDate");
	String paramForDateRappel = request.getParameter("forDateRappel")==null?"":request.getParameter("forDateRappel");
	String paramForDateReception = request.getParameter("forDateReception")==null?"":request.getParameter("forDateReception");
	String paramForUserName = request.getParameter("forUserName")==null?"":request.getParameter("forUserName");
	String paramTypeProv1 = request.getParameter("typeProv1")==null?"":request.getParameter("typeProv1");
	String paramValProv1 = request.getParameter("valProv1")==null?"":request.getParameter("valProv1");
	String paramValInterProv1 = request.getParameter("valInterProv1")==null?"":request.getParameter("valInterProv1");
	String paramTypeInterProv1 = request.getParameter("typeInterProv1")==null?"":request.getParameter("typeInterProv1");
	String paramTypeProv2 = request.getParameter("typeProv2")==null?"":request.getParameter("typeProv2");
	String paramValProv2 = request.getParameter("valProv2")==null?"":request.getParameter("valProv2");
	String paramValInterProv2 = request.getParameter("valInterProv2")==null?"":request.getParameter("valInterProv2");
	String paramTypeInterProv2 = request.getParameter("typeInterProv2")==null?"":request.getParameter("typeInterProv2");
	String paramTypeProv3 = request.getParameter("typeProv3")==null?"":request.getParameter("typeProv3");
	String paramValProv3 = request.getParameter("valProv3")==null?"":request.getParameter("valProv3");
	String paramValInterProv3 = request.getParameter("valInterProv3")==null?"":request.getParameter("valInterProv3");
	String paramTypeInterProv3 = request.getParameter("typeInterProv3")==null?"":request.getParameter("typeInterProv3");
	String paramTypeProv4 = request.getParameter("typeProv4")==null?"":request.getParameter("typeProv4");
	String paramValProv4 = request.getParameter("valProv4")==null?"":request.getParameter("valProv4");
	String paramValInterProv4 = request.getParameter("valInterProv4")==null?"":request.getParameter("valInterProv4");
	String paramTypeInterProv4 = request.getParameter("typeInterProv4")==null?"":request.getParameter("typeInterProv4");
	String paramGoBack = globaz.globall.util.JAUtil.replaceString(request.getParameter("goBack")==null?"":request.getParameter("goBack"),"\\","\\\\");
	/***************************************************/
	int indexStyle = 0;
%>
<SCRIPT language="Javascript">
function change (e) {
	bodyNode = e.parentElement.parentElement.parentElement;
	var nodeId = e.parentElement.name;
	
	for(var i=0; i<bodyNode.children.length; i++){
		if(bodyNode.children[i].nodeName == 'TR'){
			if(bodyNode.children[i].id == nodeId){
				if(bodyNode.children[i].style.display =='block'){
					bodyNode.children[i].style.display='none';
				} else{
					bodyNode.children[i].style.display='block';
					//bodyNode.children[i].style.background= bodyNode.style.background;
				}
			}
		}
	}
	
	if (e.signe=="-") {
		//A cacher
    	e.innerHTML="+";
		e.signe="+";
	} else {
		//A montrer
    	e.innerText="-";
	    e.signe="-";
    }
}

function annulerEtape(idJournalisation) {
	if (confirm("Die Gesamtheit der darunterliegenden Etappen wird verloren sein. Wollen Sie diese wirklich löschen ?")) {
		top.fr_main.location.href = "<%=request.getContextPath()%>/leo?userAction=leo.envoi.envoi.annulerEtape&journalId=" + idJournalisation + "&forLibelle=<%=paramLibelle%>&forTypeDocument=<%=paramForTypeDocument%>&forDate=<%=paramForDate%>&forDateRappel=<%=paramForDateRappel%>&forDateReception=<%=paramForDateReception%>&forUserName=<%=paramForUserName%>";	
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="4">Erstellungsdatum</TH>
	<TH>&nbsp;</TH>
	<TH>Empfänger</TH>
	<TH>Mahnungsdatum</TH>
	<TH>Empfangsdatum</TH>
	<TH>Benutzer</TH>
	<ct:ifhasright element="leo.envoi.envoi.annulerEtape" crud="u">
		<TH>Etappe annullieren</TH>
	</ct:ifhasright>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    	<%
    	globaz.leo.db.envoi.LEEnvoiViewBean line = (globaz.leo.db.envoi.LEEnvoiViewBean)viewBean.getEntity(i);
    	if(line.isJournalInitial()){
    		indexStyle++; 
    	}
    	condition = (indexStyle % 2 == 0);%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%		
String dateRappel = line.getDateRappel();
String dateReception = line.getDateReception();
actionDetail = targetLocation  + "='" + detailLink + line.getIdJournalisation()+ paramProvenance+ criteresRecherche 
+ "&dateRappel="+dateRappel + "&dateReception="+dateReception+"&_method=add"+"&"
+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE +"=" + line.getTiers().getIdTiers() + "'";		
		
		if(!line.isJournalInitial()){
			firstLine = false;
		%>
		<script language="javascript">
			node = document.getElementsByTagName('TABLE')[0];
			trNode<%=i%> = node.children[0].children[node.children[0].children.length-1];
			trNode<%=i%>.style.display = 'none';
			trNode<%=i%>.id = '<%=trName%>';
		</script>
		<%				
		} else {
			firstLine = true;
		}
	//}
	%>

	<TD class="mtd" width="" >
		<ct:menuPopup menu="LE-etapeDetail" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdJournalisation()%>"/>
			<ct:menuParam key="journalId" value="<%=line.getIdJournalisation()%>"/>
			<ct:menuParam key="forLibelle" value="<%=paramLibelle%>"/>
			<ct:menuParam key="forTypeDocument" value="<%=paramForTypeDocument%>"/>
			<ct:menuParam key="forDate" value="<%=paramForDate%>"/>
			<ct:menuParam key="forDateRappel" value="<%=paramForDateRappel%>"/>
			<ct:menuParam key="forDateReception" value="<%=paramForDateReception%>"/>
			<ct:menuParam key="forUserName" value="<%=paramForUserName%>"/>
			<ct:menuParam key="typeProv1" value="<%=paramTypeProv1%>"/>
			<ct:menuParam key="valProv1" value="<%=paramValProv1%>"/>
			<ct:menuParam key="valInterProv1" value="<%=paramValInterProv1%>"/>
			<ct:menuParam key="typeInterProv1" value="<%=paramTypeInterProv1%>"/>
			<ct:menuParam key="typeProv2" value="<%=paramTypeProv2%>"/>
			<ct:menuParam key="valProv2" value="<%=paramValProv2%>"/>
			<ct:menuParam key="valInterProv2" value="<%=paramValInterProv2%>"/>
			<ct:menuParam key="typeInterProv2" value="<%=paramTypeInterProv2%>"/>
			<ct:menuParam key="typeProv3" value="<%=paramTypeProv3%>"/>
			<ct:menuParam key="valProv3" value="<%=paramValProv3%>"/>
			<ct:menuParam key="valInterProv3" value="<%=paramValInterProv3%>"/>
			<ct:menuParam key="typeInterProv3" value="<%=paramTypeInterProv3%>"/>
			<ct:menuParam key="typeProv4" value="<%=paramTypeProv4%>"/>
			<ct:menuParam key="valProv4" value="<%=paramValProv4%>"/>
			<ct:menuParam key="valInterProv4" value="<%=paramValInterProv4%>"/>
			<ct:menuParam key="typeInterProv4" value="<%=paramTypeInterProv4%>"/>
			<ct:menuParam key="goBack" value="<%=paramGoBack%>"/>
			<ct:menuParam key="idTiersVueGlobale" value="<%=line.getTiers().getIdTiers()%>"/>
     	</ct:menuPopup>   
	</TD>
	
	<%--A activer pour le trie par colonne --%>
		<%if(firstLine && !line.getIdSuivant().equals("0") && !line.getIdSuivant().equals(line.getIdJournalisation())){
			trName = "jour_"+i;%>
			<TD class="mtd" name="<%=trName%>"><span class="but" onclick="change(this);">+</span></TD>
		<%} else {%>
			<TD class="mtd">&nbsp;</TD>
		<%}%>
	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getDate())?"&nbsp;": line.getDate()%></TD>
	<TD class="mtd">
		<FONT face="<%="".equals(line.getPoliceIcone())?"&nbsp;":line.getPoliceIcone()%>"
			size="3"><%="".equals(line.getIcone())?"&nbsp;":"&#"+line.getIcone()%></FONT>
	</TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>"><%="".equals(line.getLibelleAffichage())?"&nbsp;": line.getLibelleAffichage()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getDestinataire())?"&nbsp;": line.getDestinataire()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getDateRappel())?"&nbsp;": line.getDateRappel()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getDateReception())?"&nbsp;": line.getDateReception()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getIdUtilisateur())?"&nbsp;": line.getIdUtilisateur()%></TD>
	<ct:ifhasright element="leo.envoi.envoi.annulerEtape" crud="u">
		<TD align="center" class="mtd" onClick="annulerEtape(<%=line.getIdJournalisation()%>);"><IMG src="<%=request.getContextPath() + "/images/icon_back_small.png"%>"/></TD>
	</ct:ifhasright>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>