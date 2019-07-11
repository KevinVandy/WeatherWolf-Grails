<g:if test="${searchResult.currentWeather && searchResult.dayForecasts}">
  <div id="searchresults">

    <div id="currentWeather" class="card p-1 m-1">
      <div class="grid-2">
        <div class="all-center">
          <img src="${searchResult.currentWeather.icon}" style="width: 100px"/>

          <p>${searchResult.currentWeather.condition}</p>

          <div class="grid-2">
            <div class="m-1">
              <p>${searchResult.currentWeather.temperature.round()}&deg;</p>
            </div>

            <div class="m-1">
              <p>${searchResult.currentWeather.humidity}%</p>
            </div>
          </div>

          <p>${searchResult.currentWeather.windSpeed.round()} mph ${searchResult.currentWeather.windDirection} wind</p>

        </div>

        <div class="all-center">
          <h1 class="text-center"><g:message code="msg.currentweather" default="Current Weather"/></h1>

          <h2>
            ${searchResult.location.city}
            <g:if test="${searchResult.location.stateProvince}">, ${searchResult.location.stateProvince}</g:if>
            <br/>${searchResult.location.country}
          </h2>
        </div>

        <div class="all-center">

        </div>
      </div>
    </div>

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
            <hr/>

            <p class="text-center p">${dayForecast.windSpeed.round()} mph winds</p>

            <div class="grid-3 all-center">
              <div style="color: blue; font-size: 2em;">${dayForecast.minTemp.round()}&deg;</div>

              <div>${(dayForecast.precipitation * 100).round()}%</div>

              <div style="color: red; font-size: 2em;">${dayForecast.maxTemp.round()}&deg;</div>
            </div>
          </div>
        </g:each>
      </div>
    </div>
  </div>
</g:if>
<g:else>
  <div class="card all-center" >
    <p class="text-danger">Invalid Search</p>
  </div>
</g:else>
<script>
    $(".toggle-5day").click(function () {
        $("#fiveday").slideToggle(500);
        if ($(".toggle-5day").html() === "-") {
            $(".toggle-5day").html("+");
        } else {
            $(".toggle-5day").html("-");
        }
    });
</script>