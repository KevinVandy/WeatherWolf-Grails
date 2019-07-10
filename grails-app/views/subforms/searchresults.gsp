<g:if test="${searchResult}">
  <div id="searchresults">
    <div class="grid-1">
      <div id="currentWeather" class="card p-1 m-1">
        <div class="grid-3">
          <div class="all-center">
            <div class="grid-2">
              <div class="all-center">
                <img src="${searchResult.currentWeather.icon}" style="width: 100px"/>

                <p>${searchResult.currentWeather.condition}</p>
              </div>

              <div class="all-center">

                <p>${searchResult.currentWeather.temperature.round()}&deg;</p>

                <p>${searchResult.currentWeather.humidity}%</p>

                <p>${searchResult.currentWeather.windSpeed.round()} mph ${searchResult.currentWeather.windDirection} wind</p>

              </div>

            </div>

          </div>

          <div>
            <h1 class="text-center">Current Weather</h1>
          </div>

          <div class="all-center">
            <h2>
              ${searchResult.location.city}
              <g:if test="${searchResult.location.stateProvince}">, ${searchResult.location.stateProvince}</g:if>
              <br/>${searchResult.location.country}
            </h2>
          </div>
        </div>
      </div>
    </div>
    <div id="forecast">
      <h2 class="text-center text-light">5 Day Forecast</h2>
      <div class="grid-5">
        <g:each in="${searchResult.dayForecasts}" var="dayForecast">
          <div class="card m-1 p-1">
            <div class="all-center" style="font-size: 2em;">
              <g:formatDate date="${dayForecast.date}" format="E" />
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
            <hr/>

            <p class="text-center p">${dayForecast.windSpeed.round()} mph winds</p>

            <div class="grid-3 all-center">
              <div style="color: blue; font-size: 2em;">${dayForecast.minTemp.round()}&deg;</div>

              <div>${(dayForecast.precipitation * 100).round()}% rain</div>

              <div style="color: red; font-size: 2em;">${dayForecast.maxTemp.round()}&deg;</div>
            </div>
          </div>
        </g:each>
      </div>
    </div>
  </div>
</g:if>
<g:else>
  Invalid Search
</g:else>