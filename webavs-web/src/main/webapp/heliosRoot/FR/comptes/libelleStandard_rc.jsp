<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="GCF4011";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "helios.comptes.libelleStandard.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des libellés standards<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

	        			<TR>           
	        			<TD width="108">Pour le mandat</TD>         
			            <TD width="282">
						 <ct:FWListSelectTag name="forIdMandat"
							 defaut=""
							 data="<%=globaz.helios.translation.CGListes.getMandatListe(session)%>"/>
						  </TD>
					  </TR>

                        <TR>
                            <TD nowrap width="128">Acronyme</TD>
                            <TD nowrap colspan="2" width="298">
	                            <INPUT type="text" name="forIdLibelleStandard" size="5" maxlength="5" value="" tabindex="-1">
       	                    </TD>            	                    
                         </TR>

                        <TR>
                            <TD nowrap width="128">Acronyme commençant par</TD>
                            <TD nowrap colspan="2" width="298">
	                            <INPUT type="text" name="beginWithIdLibelleStandard" size="5" maxlength="5" value="" tabindex="-1">
       	                    </TD>            	                    
                         </TR>
                                                       
                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>