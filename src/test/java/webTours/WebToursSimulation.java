package webTours;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class WebToursSimulation extends Simulation {

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("http://localhost:1080")
    .inferHtmlResources()
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36");
  
  private Map<CharSequence, String> headers_0 = Map.ofEntries(
    Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"),
    Map.entry("Accept-Encoding", "gzip, deflate, br, zstd"),
    Map.entry("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6"),
    Map.entry("Cache-Control", "max-age=0"),
    Map.entry("Sec-Fetch-Dest", "document"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "none"),
    Map.entry("Sec-Fetch-User", "?1"),
    Map.entry("Upgrade-Insecure-Requests", "1"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );
  
  private Map<CharSequence, String> headers_1 = Map.ofEntries(
    Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"),
    Map.entry("Accept-Encoding", "gzip, deflate, br, zstd"),
    Map.entry("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6"),
    Map.entry("Sec-Fetch-Dest", "frame"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Upgrade-Insecure-Requests", "1"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );
  
  private Map<CharSequence, String> headers_2 = Map.ofEntries(
    Map.entry("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8"),
    Map.entry("Accept-Encoding", "gzip, deflate, br, zstd"),
    Map.entry("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6"),
    Map.entry("Sec-Fetch-Dest", "image"),
    Map.entry("Sec-Fetch-Mode", "no-cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );
  
  private Map<CharSequence, String> headers_8 = Map.ofEntries(
    Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"),
    Map.entry("Accept-Encoding", "gzip, deflate, br, zstd"),
    Map.entry("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6"),
    Map.entry("Cache-Control", "max-age=0"),
    Map.entry("Origin", "http://localhost:1080"),
    Map.entry("Sec-Fetch-Dest", "frame"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-Fetch-User", "?1"),
    Map.entry("Upgrade-Insecure-Requests", "1"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );
  
  private Map<CharSequence, String> headers_15 = Map.ofEntries(
    Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"),
    Map.entry("Accept-Encoding", "gzip, deflate, br, zstd"),
    Map.entry("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6"),
    Map.entry("Sec-Fetch-Dest", "frame"),
    Map.entry("Sec-Fetch-Mode", "navigate"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-Fetch-User", "?1"),
    Map.entry("Upgrade-Insecure-Requests", "1"),
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );
  
  private Map<CharSequence, String> headers_18 = Map.ofEntries(
    Map.entry("sec-ch-ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows")
  );


  ChainBuilder startPage = exec(
      http("Start_Page")
        .get("/webtours/")
        .headers(headers_0)
        .resources(
              http("/webtours/header.html")
                  .get("/webtours/header.html")
                  .headers(headers_1),
              http("/webtours/images/hp_logo.png")
                  .get("/webtours/images/hp_logo.png")
          .headers(headers_2),
        http("/webtours/images/webtours.png")
          .get("/webtours/images/webtours.png")
          .headers(headers_2),
        http("/cgi-bin/welcome.pl?signOff=true")
          .get("/cgi-bin/welcome.pl?signOff=true")
          .headers(headers_1),
        http("/WebTours/home.html")
          .get("/WebTours/home.html")
          .headers(headers_1),
        http("/cgi-bin/nav.pl?in=home")
          .get("/cgi-bin/nav.pl?in=home")
          .headers(headers_1),
        http("/WebTours/images/mer_login.gif")
          .get("/WebTours/images/mer_login.gif")
          .headers(headers_2)
      )
  );

  ChainBuilder login = exec(
      pause(7),
      http("Login")
        .post("/cgi-bin/login.pl")
        .headers(headers_8)
        .formParam("userSession", "140210.511705144HcVcAHipVQfiDDDDtDcHcptzQVHf")
        .formParam("username", "jojo")
        .formParam("password", "bean")
        .formParam("login.x", "36")
        .formParam("login.y", "10")
        .formParam("JSFormSubmit", "off")
        .resources(
          http("/cgi-bin/nav.pl?page=menu&in=home")
            .get("/cgi-bin/nav.pl?page=menu&in=home")
            .headers(headers_1),
          http("/cgi-bin/login.pl?intro=true")
            .get("/cgi-bin/login.pl?intro=true")
            .headers(headers_1),
          http("/WebTours/images/flights.gif")
            .get("/WebTours/images/flights.gif")
            .headers(headers_2),
          http("/WebTours/images/itinerary.gif")
            .get("/WebTours/images/itinerary.gif")
            .headers(headers_2),
          http("/WebTours/images/signoff.gif")
            .get("/WebTours/images/signoff.gif")
            .headers(headers_2),
          http("/WebTours/images/in_home.gif")
            .get("/WebTours/images/in_home.gif")
            .headers(headers_2)
        )
  );

  ChainBuilder flights_Page = exec(
      pause(4),
      http("Flights_Page")
        .get("/cgi-bin/welcome.pl?page=search")
        .headers(headers_15)
        .resources(
          http("/cgi-bin/reservations.pl?page=welcome")
            .get("/cgi-bin/reservations.pl?page=welcome")
            .headers(headers_1),
          http("/cgi-bin/nav.pl?page=menu&in=flights")
            .get("/cgi-bin/nav.pl?page=menu&in=flights")
            .headers(headers_1),
          http("/WebTours/images/itinerary.gif")
            .get("/WebTours/images/itinerary.gif")
            .headers(headers_18),
          http("/WebTours/images/signoff.gif")
            .get("/WebTours/images/signoff.gif")
            .headers(headers_18),
          http("/WebTours/images/home.gif")
            .get("/WebTours/images/home.gif")
            .headers(headers_2),
          http("/WebTours/images/button_next.gif")
            .get("/WebTours/images/button_next.gif")
            .headers(headers_2),
          http("/WebTours/images/in_flights.gif")
            .get("/WebTours/images/in_flights.gif")
            .headers(headers_2)
        )
  );

  ChainBuilder find_Flights = exec(
      pause(4),
      http("Find_Flights")
        .post("/cgi-bin/reservations.pl")
        .headers(headers_8)
        .formParam("advanceDiscount", "0")
        .formParam("depart", "Denver")
        .formParam("departDate", "11/07/2024")
        .formParam("arrive", "Frankfurt")
        .formParam("returnDate", "11/08/2024")
        .formParam("numPassengers", "1")
        .formParam("seatPref", "None")
        .formParam("seatType", "Coach")
        .formParam("findFlights.x", "41")
        .formParam("findFlights.y", "7")
        .formParam(".cgifields", "roundtrip")
        .formParam(".cgifields", "seatType")
        .formParam(".cgifields", "seatPref")
    );

  ChainBuilder choose_Flights = exec(
      pause(4),
      http("Choose_Flights")
        .post("/cgi-bin/reservations.pl")
        .headers(headers_8)
        .formParam("outboundFlight", "011;343;11/07/2024")
        .formParam("numPassengers", "1")
        .formParam("advanceDiscount", "0")
        .formParam("seatType", "Coach")
        .formParam("seatPref", "None")
        .formParam("reserveFlights.x", "45")
        .formParam("reserveFlights.y", "11")
    );

  ChainBuilder reservation = exec(
      pause(3),
      http("Reservation")
        .post("/cgi-bin/reservations.pl")
        .headers(headers_8)
        .formParam("firstName", "Jojo")
        .formParam("lastName", "Bean")
        .formParam("address1", "")
        .formParam("address2", "")
        .formParam("pass1", "Jojo Bean")
        .formParam("creditCard", "")
        .formParam("expDate", "")
        .formParam("oldCCOption", "")
        .formParam("numPassengers", "1")
        .formParam("seatType", "Coach")
        .formParam("seatPref", "None")
        .formParam("outboundFlight", "011;343;11/07/2024")
        .formParam("advanceDiscount", "0")
        .formParam("returnFlight", "")
        .formParam("JSFormSubmit", "off")
        .formParam("buyFlights.x", "40")
        .formParam("buyFlights.y", "9")
        .formParam(".cgifields", "saveCC")
    );

  ChainBuilder homePage = exec(
      pause(5),
      http("Home_Page")
        .get("/cgi-bin/welcome.pl?page=menus")
        .headers(headers_15)
        .resources(
          http("/cgi-bin/login.pl?intro=true")
            .get("/cgi-bin/login.pl?intro=true")
            .headers(headers_1),
          http("/cgi-bin/nav.pl?page=menu&in=home")
            .get("/cgi-bin/nav.pl?page=menu&in=home")
            .headers(headers_1),
          http("/WebTours/images/itinerary.gif")
            .get("/WebTours/images/itinerary.gif")
            .headers(headers_18),
          http("/WebTours/images/signoff.gif")
            .get("/WebTours/images/signoff.gif")
            .headers(headers_18),
          http("/WebTours/images/in_home.gif")
            .get("/WebTours/images/in_home.gif")
            .headers(headers_2),
          http("/WebTours/images/flights.gif")
            .get("/WebTours/images/flights.gif")
            .headers(headers_2)
        )
  );

  ChainBuilder itinerary = exec(
      pause(2),
      http("Itinerary")
        .get("/cgi-bin/welcome.pl?page=itinerary")
        .headers(headers_15)
        .resources(
          http("/cgi-bin/itinerary.pl")
            .get("/cgi-bin/itinerary.pl")
            .headers(headers_1),
          http("/WebTours/images/cancelallreservations.gif")
            .get("/WebTours/images/cancelallreservations.gif")
            .headers(headers_2),
          http("/cgi-bin/nav.pl?page=menu&in=itinerary")
            .get("/cgi-bin/nav.pl?page=menu&in=itinerary")
            .headers(headers_1),
          http("/WebTours/images/flights.gif")
            .get("/WebTours/images/flights.gif")
            .headers(headers_18),
          http("/WebTours/images/signoff.gif")
            .get("/WebTours/images/signoff.gif")
            .headers(headers_18),
          http("/WebTours/images/home.gif")
            .get("/WebTours/images/home.gif")
            .headers(headers_2),
          http("/WebTours/images/cancelreservation.gif")
            .get("/WebTours/images/cancelreservation.gif")
            .headers(headers_2),
          http("/WebTours/images/in_itinerary.gif")
            .get("/WebTours/images/in_itinerary.gif")
            .headers(headers_2)
        )
  );

  ChainBuilder cancel_Reservation = exec(
      pause(6),
      http("Cancel_Reservation")
        .post("/cgi-bin/itinerary.pl")
        .headers(headers_8)
        .formParam("1", "on")
        .formParam("flightID", "420-795-JB")
        .formParam("removeFlights.x", "62")
        .formParam("removeFlights.y", "9")
        .formParam(".cgifields", "1")
  );

  ChainBuilder sign_Off = exec(
      pause(3),
      http("Sign_Off")
        .get("/cgi-bin/welcome.pl?signOff=1")
        .headers(headers_15)
        .resources(
          http("/WebTours/home.html")
            .get("/WebTours/home.html")
            .headers(headers_1),
          http("/cgi-bin/nav.pl?in=home")
            .get("/cgi-bin/nav.pl?in=home")
            .headers(headers_1),
          http("/WebTours/images/mer_login.gif")
            .get("/WebTours/images/mer_login.gif")
            .headers(headers_2)
        )
  );
  ScenarioBuilder userScn = scenario("WebToursSimulation").exec(
          startPage,
          login,
          flights_Page,
          find_Flights,
          choose_Flights,
          reservation,
          homePage,
          itinerary,
          cancel_Reservation,
          sign_Off
  );

  {
	  setUp(userScn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
