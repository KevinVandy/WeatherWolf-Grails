<form action="weather/result" method="get">
  <table>
    <tr>
      <td>
        <g:textField name="location" placeholder="City, [State/Province], Country" style="width: 400px;" required=""/>
      </td>
      <td>
        <select placeholder="units">
          <option value="" disabled hidden >Units</option>
          <option value="C">C&deg;</option>
          <option value="F" selected>F&deg;</option>
        </select>
      </td>
      <td>
        <input type="submit" value="See Forecast" class="btn-block btn-dark p"/>
      </td>

    </tr>
  </table>
</form>
