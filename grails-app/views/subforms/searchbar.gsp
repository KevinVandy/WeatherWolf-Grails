<form action="/weather" method="get">
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
          <option value="" disabled><g:message code="msg.units" default="Units"/></option>
          <sec:ifLoggedIn>
            <option value="C" <g:if test="${params.units == 'C' || user.units == 'C'}">selected</g:if>>C&deg;</option>
            <option value="F" <g:if test="${!(params.units == 'C' || user.units == 'C')}">selected</g:if>>F&deg;</option>
          </sec:ifLoggedIn>
          <sec:ifNotLoggedIn>
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
