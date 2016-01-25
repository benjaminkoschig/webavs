<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.aquila.util.CODateUtils"%>

<%
	String jugementMainLevee = "JUGEMENT_MAIN_LEVEE";
	String decisionMainLevee = "DECISION_MAIN_LEVEE";
	String jugementMainleveeLabel = "jugement de mainlevée du";
	String decisionMainleveeLabel = "décision de mainlevée du";
%>

<jsp:include flush="true" page="saisie_email.jsp"/>

<tr>
	<td>Observations</td>
	<td class="control" colspan="5">
		<INPUT type="hidden" name="jugementMainlevee" value="<%=jugementMainLevee%>">
		<INPUT type="checkbox" name="jugementMainleveeChecked" id="jugement"><label for="jugement">&nbsp;<%=jugementMainleveeLabel%>&nbsp;</label>
		<ct:FWCalendarTag name="dateJugement" doClientValidation="CALENDAR" value="<%=CODateUtils.getTodayPlusDaysDDsMMsYYYY(0)%>"/>
	</td>
</tr>
<tr>
	<td></td>
	<td class="control" colspan="5">
		<INPUT type="hidden" name="decisionMainlevee" value="<%=decisionMainLevee%>">
		<INPUT type="checkbox" name="decisionMainleveeChecked" id="decision"><label for="decision">&nbsp;<%=decisionMainleveeLabel%>&nbsp;</label>
		<ct:FWCalendarTag name="dateDecision" doClientValidation="CALENDAR" value="<%=CODateUtils.getTodayPlusDaysDDsMMsYYYY(0)%>"/>
	</td>
</tr>
<tr>
	<td class="label"></td>
	<td class="control" colspan="5">		
		<TEXTAREA name="observationTexteLibre" id="observationTexteLibre" cols="40" rows="2"></TEXTAREA>
	</td>
</tr>