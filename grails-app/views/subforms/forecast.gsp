<div id="forecast">
  <h2 class="text-center text-light"><g:message code="msg.fivedayforecast" default="5 Day Forecast"/>&nbsp;<span
      class="toggle-5day m-1 p-1">-</span></h2>

  <div class="grid-5" id="fiveday">
    <g:each in="${searchResult.dayForecasts}" var="dayForecast">
      <div class="card m p-1">
        <div class="all-center" style="font-size: 2em;">
          <g:formatDate date="${dayForecast.date}" format="E"/>
        </div>
        <hr/>

        <div class="grid-2">
          <div>
            <img src="${dayForecast.iconURL}" alt="${dayForecast.iconURL}" style="float: left; width: 75px"/>
          </div>

          <div class="text-center-inline">
            ${dayForecast.condition}
          </div>
        </div>
        <div class="all-center">${(dayForecast.precipitation).round(1)} in precipitation</div>
        <hr/>

        <p class="text-center p">${dayForecast.windSpeed.round()} mph winds</p>

        <div class="grid-3 all-center">
          <div style="color: blue; font-size: 2em;">${dayForecast.minTemp.round()}&deg;</div>

          <div></div>

          <div style="color: red; font-size: 2em;">${dayForecast.maxTemp.round()}&deg;</div>
        </div>
      </div>
    </g:each>
  </div>
</div>