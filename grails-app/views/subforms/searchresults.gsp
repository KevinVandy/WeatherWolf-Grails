<g:if test="searchResult">
  <div id="searchresults">

    <div class="grid-1">
      <div id="currentWeather" class="card p-1 m-1">
        <div class="grid-3">
          <div class="all-center">
            <img src="${searchResult.currentWeather.icon}" style="width: 100px"/>

            <p>${searchResult.currentWeather.condition}</p>

            <p>${searchResult.currentWeather.temperature}</p>

            <p>${searchResult.currentWeather.humidity}%</p>

            <p>${searchResult.currentWeather.windSpeed} mph ${searchResult.currentWeather.windDirection}</p>
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
      <h3 class="text-center">5 Day Forecast</h3>

      <div class="grid-5">
        <g:each in="${searchResult.dayForecasts}" var="dayForecast">
          <div class="card m-1 p-1">
            <div class="all-center">
              <g:formatDate date="${dayForecast.date}" type="datetime" format="Day in week"/>
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

            <p class="text-center p">${dayForecast.windSpeed.round()} mph</p>

            <div class="grid-3 all-center">
              <div style="color: blue;">${dayForecast.minTemp.round()}</div>

              <div>${(dayForecast.precipitation * 100).round()} %</div>

              <div style="color: red;">${dayForecast.maxTemp.round()}</div>
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