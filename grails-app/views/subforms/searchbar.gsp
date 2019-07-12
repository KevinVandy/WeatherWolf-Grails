<form action="/weather/index" method="get">
  <table>
    <tr>
      <td>
        <input type="text" name="location" style=" width: 400px; font-size: 1.5em;" placeholder="<g:message code='msg.search.placeholder' />"
                     value="${params.location ?: ""}" required=""/>
      </td>
      <td>
        <select name="units">
          <option value="" disabled><g:message code="msg.units" default="Units"/></option>
          <option value="C">C&deg;</option>
          <option value="F" selected>F&deg;</option>
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
