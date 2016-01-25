<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@page import="globaz.naos.db.affiliation.AFListeNbrAssuresByAffiliationViewBean"%>
<%@page import="globaz.naos.db.assurance.AFAssuranceManager"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored ="false" %>

<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran = "CAF2020";

	//Récupération des beans
	AFListeNbrAssuresByAffiliationViewBean viewBean = (AFListeNbrAssuresByAffiliationViewBean) session.getAttribute ("viewBean");

	String jspLocation = servletContext + mainServletPath + "Root/assurance_select.jsp";
	
	userActionValue = "naos.affiliation.listeNbrAssuresByAffiliation.executer";
	
%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='NAOS_JSP_CAF2020_NOM_ECRAN'/>";
</SCRIPT>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
	function init() {
	}
	
	function postInit() {
		$('#widgetNumAffilieFrom').val('${viewBean.fromNumAffilie}');
		$('#widgetNumAffilieTo').val('${viewBean.toNumAffilie}');
		$('#widgetAssurance').val('${viewBean.libelleAssurance}');
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="NAOS_JSP_CAF2020_NOM_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD><ct:FWLabel key="NAOS_JSP_CAF2020_ANNEE"/></TD>
		<TD><INPUT type="text" name="forAnnee" maxlength="4" size="4" value="${viewBean.forAnnee}"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="NAOS_JSP_CAF2020_FROM_NUMERO_AFFILIE"/></TD>
		<TD>
		    <ct:widget name="widgetNumAffilieFrom" id="widgetNumAffilieFrom" >
				<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
					<ct:widgetCriteria criteria="likeNumeroAffilie" label="NAOS_JSP_CAF2020_AFFILIE"/>								
					<ct:widgetLineFormatter format="#{numAffilie}  #{nom} (#{typeAffiliationLabel})"/>
					<ct:widgetJSReturnFunction>
						<script type="text/javascript">
							function(element){			
								$('#widgetNumAffilieFrom').val($(element).attr('numAffilie'));
								$('#fromNumAffilie').val($(element).attr('numAffilie'));
							}
						</script>										
					</ct:widgetJSReturnFunction>
				</ct:widgetManager>
			</ct:widget>
			<ct:inputHidden name="fromNumAffilie" id="fromNumAffilie"/>		
			&nbsp;
			&nbsp;
			<ct:FWLabel key="NAOS_JSP_CAF2020_TO_NUMERO_AFFILIE"/>
			&nbsp;
			&nbsp;
			<ct:widget name="widgetNumAffilieTo" id="widgetNumAffilieTo" >
				<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
					<ct:widgetCriteria criteria="likeNumeroAffilie" label="NAOS_JSP_CAF2020_AFFILIE"/>								
					<ct:widgetLineFormatter format="#{numAffilie}  #{nom} (#{typeAffiliationLabel})"/>
					<ct:widgetJSReturnFunction>
						<script type="text/javascript">
							function(element){			
								$('#widgetNumAffilieTo').val($(element).attr('numAffilie'));
								$('#toNumAffilie').val($(element).attr('numAffilie'));
							}
						</script>										
					</ct:widgetJSReturnFunction>
				</ct:widgetManager>
			</ct:widget>
			<ct:inputHidden name="toNumAffilie" id="toNumAffilie"/>										
		</TD>
	</TR>
		<TR>
		<TD><ct:FWLabel key="NAOS_JSP_CAF2020_ASSURANCE"/></TD>		
		<TD><ct:widget name="widgetAssurance" id="widgetAssurance" style="width: 470px;">
				<ct:widgetManager managerClassName="<%=AFAssuranceManager.class.getName()%>" defaultSearchSize="5">
					<ct:widgetCriteria criteria="forLibelleCourtOuLongLike"  label="NAOS_JSP_CAF2020_ASSURANCE"/>		
					<ct:widgetCriteria criteria="forGenreAssurance"  label="NAOS_JSP_CAF2020_AFFILIE" fixedValue="801001"/>			
					<ct:widgetLineFormatter format="#{assuranceLibelle}"/>
					<ct:widgetJSReturnFunction>
						<script type="text/javascript">
							function(element){	
								$('#widgetAssurance').val($(element).attr('assuranceLibelle'));
								$('#idAssurance').val($(element).attr('assuranceId'));
								$('#libelleAssurance').val($(element).attr('assuranceLibelle'));
							}
						</script>										
					</ct:widgetJSReturnFunction>
				</ct:widgetManager>
			</ct:widget>
			<ct:inputHidden name="idAssurance" id="idAssurance"/>	
			<ct:inputHidden name="libelleAssurance" id="libelleAssurance"/>	
		</TD>
	</TR>
	<TR>
       	<TD width="23%" height="2"><ct:FWLabel key="NAOS_JSP_CAF2020_EMAIL"/></TD>
       	<TD height="2"><INPUT type="text" name="mail" maxlength="40" size="40" style="width:8cm;" value="${viewBean.mail}"></TD>
    </TR>
          				
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>