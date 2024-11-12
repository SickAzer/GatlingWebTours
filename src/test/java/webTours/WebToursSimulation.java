package webTours;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class WebToursSimulation extends Simulation {

    private static final Duration CLICK_THINK_TIME = Duration.ofSeconds(3);
    private static final Duration FORM_THINK_TIME = Duration.ofSeconds(8);;
    private static final Duration RAMP_UP_TIME = Duration.ofSeconds(30);;
    private static final Duration HOLD_LOAD_TIME = Duration.ofSeconds(640);;
    private static final Duration PACE = Duration.ofSeconds(160);;
    private static final Duration SCENARIO_LOOP_DURATION = Duration.ofSeconds(2010);;

    // Протокол
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:1080")
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif",
                    ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf",
                    ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9," +
                    "image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6")
            .upgradeInsecureRequestsHeader("1")
            .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                    " (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
            .disableCaching();

    // Стартовая страница
    ChainBuilder startPage =
            exec(
            http("Start_Page")
                    .get("/webtours/")
                    .check(
                            status().is(200)
                    )
            ).exec(
                    http("/webtours/header.html")
                            .get("/webtours/header.html"),
                    http("/cgi-bin/welcome.pl?signOff=true")
                            .get("/cgi-bin/welcome.pl?signOff=true")
                            .check(
                                    substring("A Session ID has been created and" +
                                            " loaded into a cookie called MSO")
                            ),
                    http("/WebTours/home.html")
                            .get("/WebTours/home.html"),

                    http("/cgi-bin/nav.pl?in=home")
                            .get("/cgi-bin/nav.pl?in=home")
                            .check(
                                    css("[name=\"userSession\"]", "value")
                                            .exists().saveAs("userSession")
                            )
            );

    // Вход в учетную запись
    ChainBuilder login = exec(
            pause(FORM_THINK_TIME),
            http("Login")
                    .post("/cgi-bin/login.pl")
                    .formParam("userSession", "#{userSession}")
                    .formParam("username", "#{userName}")
                    .formParam("password", "#{password}")
                    .formParam("login.x", "36")
                    .formParam("login.y", "10")
                    .formParam("JSFormSubmit", "off")
                    .resources(
                            http("/cgi-bin/nav.pl?page=menu&in=home")
                                    .get("/cgi-bin/nav.pl?page=menu&in=home"),
                            http("/cgi-bin/login.pl?intro=true")
                                    .get("/cgi-bin/login.pl?intro=true")
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
                    .resources(
                            http("/cgi-bin/reservations.pl?page=welcome")
                                    .get("/cgi-bin/reservations.pl?page=welcome"),
                            http("/cgi-bin/nav.pl?page=menu&in=flights")
                                    .get("/cgi-bin/nav.pl?page=menu&in=flights")
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
                    .formParam("advanceDiscount", "0")
                    .formParam("depart", "#{depart}")
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
                            css("[name=\"outboundFlight\"][checked=\"checked\"]", "value")
                                    .withDefault("outboundFlight not found")
                                    .exists()
                                    .saveAs("outboundFlight"),
                            css("[name=\"returnFlight\"][checked=\"checked\"]", "value")
                                    .withDefault("returnFlight not found")
                                    .saveAs("returnFlight")
                    )
    );

    // Выбор доступных рейсов
    ChainBuilder chooseFlights = exec(
            pause(FORM_THINK_TIME),
            http("Choose_Flights")
                    .post("/cgi-bin/reservations.pl")
                    .formParam("outboundFlight", "011;343;11/07/2024")
                    .formParam("numPassengers", "#{numPassengers}")
                    .formParam("advanceDiscount", "0")
                    .formParam("seatType", "#{seatType}")
                    .formParam("seatPref", "#{seatPref}")
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
                    .resources(
                            http("/cgi-bin/login.pl?intro=true")
                                    .get("/cgi-bin/login.pl?intro=true"),
                            http("/cgi-bin/nav.pl?page=menu&in=home")
                                    .get("/cgi-bin/nav.pl?page=menu&in=home")
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
                    .resources(
                            http("/cgi-bin/itinerary.pl")
                                    .get("/cgi-bin/itinerary.pl")
                                    .check(
                                            css("[name=\"flightID\"]", "value")
                                                    .withDefault("flightID not found")
                                                    .exists()
                                                    .saveAs("flightID")
                                    ),
                            http("/cgi-bin/nav.pl?page=menu&in=itinerary")
                                    .get("/cgi-bin/nav.pl?page=menu&in=itinerary")
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
                    .formParam("1", "on")
                    .formParam("flightID", "#{flightID}")
                    .formParam("removeFlights.x", "62")
                    .formParam("removeFlights.y", "9")
                    .formParam(".cgifields", "1")
                    .check(
                            status().is(200),
                            substring("No flights have been reserved.")
                    )
    );

    // Выход из учетной записи
    ChainBuilder signOff = exec(
            pause(CLICK_THINK_TIME),
            http("Sign_Off")
                    .get("/cgi-bin/welcome.pl?signOff=1")
                    .resources(
                            http("/WebTours/home.html")
                                    .get("/WebTours/home.html"),
                            http("/cgi-bin/nav.pl?in=home")
                                    .get("/cgi-bin/nav.pl?in=home")
                    )
                    .check(
                            status().is(200),
                            substring(" A Session ID has been created and loaded into a cookie called MSO.")
                    )
    );

    // Пользовательские данные содержатся в файле .csv
    FeederBuilder<String> feeder = csv("users_input_data.csv").circular();

    // Сборка сценария покупки билета
    ScenarioBuilder userScn = scenario("WebToursSimulation").exec(
            during(SCENARIO_LOOP_DURATION).on(
                    feed(feeder),
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
                        rampConcurrentUsers(0).to(44).during(RAMP_UP_TIME),
                        constantConcurrentUsers(44).during(HOLD_LOAD_TIME),
                        rampConcurrentUsers(44).to(88).during(RAMP_UP_TIME),
                        constantConcurrentUsers(88).during(HOLD_LOAD_TIME),
                        rampConcurrentUsers(88).to(132).during(RAMP_UP_TIME),
                        constantConcurrentUsers(132).during(HOLD_LOAD_TIME)
                ).protocols(httpProtocol)
        );
    }
}
