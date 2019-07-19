<form action="/weather/index" method="get">
  <table>
    <tr>
      <td>
        <div id="remote" style="overflow: visible;">
          <input type="text" name="location" style=" width: 400px; font-size: 1.5em;" placeholder="<g:message code='msg.search.placeholder'/>"
                 value="${params.location ?: ""}" class="typeahead" required/>
        </div>
      </td>
      <td>
        <select name="units">
          <sec:ifLoggedIn>
            <option value="" disabled><g:message code="msg.units" default="Units"/></option>
            <option value="C" <g:if test="${user.units == 'C'}">selected</g:if>>C&deg;</option>
            <option value="F" <g:if test="${user.units == 'F'}">selected</g:if>>F&deg;</option>
          </sec:ifLoggedIn>
          <sec:ifNotLoggedIn>
            <option value="" disabled <g:if test="${!params.units}">selected</g:if>><g:message code="msg.units" default="Units"/></option>
            <option value="C" <g:if test="${params.units == 'C'}">selected</g:if>>C&deg;</option>
            <option value="F" <g:if test="${params.units != 'C'}">selected</g:if>>F&deg;</option>
          </sec:ifNotLoggedIn>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="<g:message code="msg.seeforecast"/>" class="btn-block btn-dark p m-0"/>
      </td>
    </tr>
  </table>
</form>
