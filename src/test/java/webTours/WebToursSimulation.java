package webTours;

import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class WebToursSimulation extends Simulation {

  private static final int CLICK_THINK_TIME = 3;
  private static final int FORM_THINK_TIME = 8;
  private static final int RAMP_UP_TIME = 30;
  private static final int HOLD_LOAD_TIME = 640;
  private static final int PACE = 160;
  private static final int SCENARIO_LOOP_DURATION = 700;

  // Пользовательские данные содержатся в файле .csv
   FeederBuilder<String> feeder = csv("users_input_data.csv").circular();

   HttpProtocolBuilder httpProtocol = http
    .baseUrl("http://localhost:1080")
    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
    .disableCaching();

  private Map<CharSequence, String> headers_0 = Map.ofEntries(
    Map.entry("Cache-Control", "max-age=0"),
    Map.entry("Sec-Fetch-Dest", "document"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "none"),
    Map.entry("Sec-Fetch-User", "?1"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );

  private Map<CharSequence, String> headers_1 = Map.ofEntries(
    Map.entry("Sec-Fetch-Dest", "frame"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );

  private Map<CharSequence, String> headers_2 = Map.ofEntries(
    Map.entry("Cache-Control", "max-age=0"),
    Map.entry("Origin", "http://localhost:1080"),
    Map.entry("Sec-Fetch-Dest", "frame"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-Fetch-User", "?1"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );

  private Map<CharSequence, String> headers_3 = Map.ofEntries(
    Map.entry("Sec-Fetch-Dest", "frame"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-Fetch-User", "?1"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );

   // Стартовая страница
   ChainBuilder startPage = exec(
      http("Start_Page")
        .get("/webtours/")
        .headers(headers_0)
        .resources(
          http("/webtours/header.html")
            .get("/webtours/header.html")
            .headers(headers_1),
          http("/cgi-bin/welcome.pl?signOff=true")
            .get("/cgi-bin/welcome.pl?signOff=true")
            .headers(headers_1)
            .check(
              substring("A Session ID has been created and loaded into a cookie called MSO")
            ),
          http("/WebTours/home.html")
            .get("/WebTours/home.html")
            .headers(headers_1),
          http("/cgi-bin/nav.pl?in=home")
            .get("/cgi-bin/nav.pl?in=home")
            .headers(headers_1)
            .check(
              css("[name=\"userSession\"]", "value").exists().saveAs("userSession")
            )
        )
        .check(
          status().is(200)
        )
  );

   // Вход в учетную запись
   ChainBuilder login = exec(
      pause(FORM_THINK_TIME),
      http("Login")
        .post("/cgi-bin/login.pl")
        .headers(headers_2)
        .formParam("userSession", "#{userSession}")
        .formParam("username", "#{userName}")
        .formParam("password", "#{password}")
        .formParam("login.x", "36")
        .formParam("login.y", "10")
        .formParam("JSFormSubmit", "off")
        .resources(
          http("/cgi-bin/nav.pl?page=menu&in=home")
            .get("/cgi-bin/nav.pl?page=menu&in=home")
            .headers(headers_1),
          http("/cgi-bin/login.pl?intro=true")
            .get("/cgi-bin/login.pl?intro=true")
            .headers(headers_1)
            .check(
              substring("Don't forget to sign off when\n" +
                      "you're done!")
            )
        )
        .check(
          status().is(200),
          substring("User password was correct")
        )
  );

   // Переход к поиску доступных рейсов
   ChainBuilder flightsPage = exec(
      pause(CLICK_THINK_TIME),
      http("Flights_Page")
        .get("/cgi-bin/welcome.pl?page=search")
        .headers(headers_3)
        .resources(
          http("/cgi-bin/reservations.pl?page=welcome")
            .get("/cgi-bin/reservations.pl?page=welcome")
            .headers(headers_1),
          http("/cgi-bin/nav.pl?page=menu&in=flights")
            .get("/cgi-bin/nav.pl?page=menu&in=flights")
            .headers(headers_1)
        )
        .check(
          status().is(200),
          substring("User has returned to the search page")
        )
  );

   // Поиск доступных рейсов
   ChainBuilder findFlights = exec(
      pause(FORM_THINK_TIME),
      http("Find_Flights")
        .post("/cgi-bin/reservations.pl")
        .headers(headers_2)
        .formParam("advanceDiscount", "0")
        .formParam("depart", "Denver")
        .formParam("departDate", "#{departDate}")
        .formParam("arrive", "#{arrive}")
        .formParam("returnDate", "#{returnDate}")
        .formParam("numPassengers", "#{numPassengers}")
        .formParam("seatPref", "#{seatPref}")
        .formParam("seatType", "#{seatType}")
        .formParam("roundtrip", "#{roundTrip}")
        .formParam("findFlights.x", "41")
        .formParam("findFlights.y", "7")
        .formParam(".cgifields", "roundtrip")
        .formParam(".cgifields", "seatType")
        .formParam(".cgifields", "seatPref")
        .check(
          status().is(200),
          substring("Flight Selections"),
          css("[name=\"outboundFlight\"][checked=\"checked\"]", "value").exists().saveAs("outboundFlight"),
          css("[name=\"returnFlight\"][checked=\"checked\"]", "value")
            .withDefault("outboundFlight not found")
            .saveAs("returnFlight")
        )
    );

   // Выбор доступных рейсов
   ChainBuilder chooseFlights = exec(
      pause(FORM_THINK_TIME),
      http("Choose_Flights")
        .post("/cgi-bin/reservations.pl")
        .headers(headers_2)
        .formParam("outboundFlight", "011;343;11/07/2024")
        .formParam("numPassengers", "#{numPassengers}")
        .formParam("advanceDiscount", "0")
        .formParam("seatType", "#{seatType}")
        .formParam("seatPref", "{seatPref}")
        .formParam("reserveFlights.x", "45")
        .formParam("reserveFlights.y", "11")
        .check(
          status().is(200),
          substring("Flight Reservation")
        )
    );

   // Регистрация и оплата
   ChainBuilder reservation = exec(
      pause(FORM_THINK_TIME),
      http("Reservation")
        .post("/cgi-bin/reservations.pl")
        .headers(headers_2)
        .formParam("firstName", "#{firstName}")
        .formParam("lastName", "#{lastName}")
        .formParam("address1", "#{address1}")
        .formParam("address2", "#{address2}")
        .formParam("pass1", "#{firstName} #{lastName}")
        .formParam("creditCard", "#{creditCard}")
        .formParam("expDate", "#{expDate}")
        .formParam("oldCCOption", "")
        .formParam("numPassengers", "#{numPassengers}")
        .formParam("seatType", "#{seatType}")
        .formParam("seatPref", "#{seatPref}")
        .formParam("outboundFlight", "#{outboundFlight}")
        .formParam("advanceDiscount", "0")
        .formParam("returnFlight", "#{returnFlight}")
        .formParam("JSFormSubmit", "off")
        .formParam("buyFlights.x", "40")
        .formParam("buyFlights.y", "9")
        .formParam(".cgifields", "saveCC")
        .check(
          status().is(200),
          substring("Reservation Made!")
        )
    );

   // Выход на домашнюю страницу
   ChainBuilder homePage = exec(
      pause(CLICK_THINK_TIME),
      http("Home_Page")
        .get("/cgi-bin/welcome.pl?page=menus")
        .headers(headers_3)
        .resources(
          http("/cgi-bin/login.pl?intro=true")
            .get("/cgi-bin/login.pl?intro=true")
            .headers(headers_1),
          http("/cgi-bin/nav.pl?page=menu&in=home")
            .get("/cgi-bin/nav.pl?page=menu&in=home")
            .headers(headers_1)
        )
        .check(
          status().is(200),
          substring("User has returned to the home page")
        )
  );

    // Запланированные рейсы
   ChainBuilder itinerary = exec(
      pause(CLICK_THINK_TIME),
      http("Itinerary")
        .get("/cgi-bin/welcome.pl?page=itinerary")
        .headers(headers_3)
        .resources(
          http("/cgi-bin/itinerary.pl")
            .get("/cgi-bin/itinerary.pl")
            .headers(headers_1)
            .check(
              css("[name=\"flightID\"]", "value").exists().saveAs("flightID")
            ),
          http("/cgi-bin/nav.pl?page=menu&in=itinerary")
            .get("/cgi-bin/nav.pl?page=menu&in=itinerary")
            .headers(headers_1)
        )
        .check(
          status().is(200),
          substring("User wants the intineraries.  Since user has already logged on,\n" +
                  " we can give them the menu in the navbar.")
        )
  );

   // Отмена запланированного рейса
   ChainBuilder cancelReservation = exec(
      pause(FORM_THINK_TIME),
      http("Cancel_Reservation")
        .post("/cgi-bin/itinerary.pl")
        .headers(headers_2)
        .formParam("1", "on")
        .formParam("flightID", "#{flightID}")
        .formParam("removeFlights.x", "62")
        .formParam("removeFlights.y", "9")
        .formParam(".cgifields", "1")
        .check(
          status().is(200),
          substring("Flights List")
        )
  );

   // Выход из учетной записи
   ChainBuilder signOff = exec(
      pause(CLICK_THINK_TIME),
      http("Sign_Off")
        .get("/cgi-bin/welcome.pl?signOff=1")
        .headers(headers_3)
        .resources(
          http("/WebTours/home.html")
            .get("/WebTours/home.html")
            .headers(headers_1),
          http("/cgi-bin/nav.pl?in=home")
            .get("/cgi-bin/nav.pl?in=home")
            .headers(headers_1)
        )
        .check(
          status().is(200),
          substring(" A Session ID has been created and loaded into a cookie called MSO.")
        )
  );

  // Сборка сценария
  ScenarioBuilder userScn = scenario("WebToursSimulation").exec(
          feed(feeder),
          during(SCENARIO_LOOP_DURATION).on(
            pace(PACE)
              .exec(
                startPage,
                login,
                flightsPage,
                findFlights,
                chooseFlights,
                reservation,
                homePage,
                itinerary,
                cancelReservation,
                signOff
              )
          )
  );

  // Настройка порядка подключения пользователей
  {
	  setUp(
        userScn.injectClosed(
                rampConcurrentUsers(0).to(10).during(RAMP_UP_TIME),
                constantConcurrentUsers(10).during(HOLD_LOAD_TIME),
                rampConcurrentUsers(10).to(20).during(RAMP_UP_TIME),
                constantConcurrentUsers(20).during(HOLD_LOAD_TIME),
                rampConcurrentUsers(20).to(30).during(RAMP_UP_TIME),
                constantConcurrentUsers(30).during(HOLD_LOAD_TIME)
        ).protocols(httpProtocol)
      );
  }
}
