<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.aquila.util.CODateUtils"%>
<jsp:include flush="true" page="saisie_email.jsp"/>
<TR>
	<TD class="label">Kontosituation am</TD>
	<TD class="control" colspan="5"><ct:FWCalendarTag name="dateSituationCompte" doClientValidation="CALENDAR" value="<%=CODateUtils.getTodayPlusDaysDDsMMsYYYY(0)%>"/></TD>
</TR>
<jsp:include flush="true" page="saisie_annexes.jsp"/>