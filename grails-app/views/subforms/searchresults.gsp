<g:if test="${searchResult.location && searchResult.currentWeather && searchResult.dayForecasts}">
  <div id="searchresults">
    <g:include view="subforms/currentweather.gsp"/>
    <g:include view="subforms/forecast.gsp"/>
  </div>
</g:if>
<g:else>
  <div class="all-center grid-3">
    <div></div>

    <p class="card text-danger"><g:message code="msg.invalidsearch" default="Invalid Search"/></p>

    <div></div>
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