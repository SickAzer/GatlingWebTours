package webTours;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class UserRegistration extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:1080")
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif",
                    ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf",
                    ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif," +
                    "image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-US,en;q=0.9,ru;q=0.8,de-DE;q=0.7,de;q=0.6")
            .upgradeInsecureRequestsHeader("1")
            .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                    " Chrome/130.0.0.0 Safari/537.36");

    FeederBuilder<String> feeder = csv("users_input_data.csv").queue();

    // Сценарий регистрации пользователя
    private ScenarioBuilder registration = scenario("UserRegistration")
            .exec(
                    repeat(csv("users_input_data.csv").recordsCount()).on(
                            feed(feeder),
                            exec(
                                    http("request_0")
                                            .get("/webtours/")
                                            .resources(
                                                    http("request_1")
                                                            .get("/webtours/header.html"),
                                                    http("request_2")
                                                            .get("/cgi-bin/welcome.pl?signOff=true"),
                                                    http("request_3")
                                                            .get("/cgi-bin/nav.pl?in=home"),
                                                    http("request_4")
                                                            .get("/WebTours/home.html")
                                            ),
                                    http("request_5")
                                            .get("/cgi-bin/login.pl?username=&password=&getInfo=true"),
                                    http("request_6")
                                            .post("/cgi-bin/login.pl")
                                            .formParam("username", "#{userName}")
                                            .formParam("password", "#{password}")
                                            .formParam("passwordConfirm", "#{password}")
                                            .formParam("firstName", "#{firstName}")
                                            .formParam("lastName", "#{lastName}")
                                            .formParam("address1", "#{address1}")
                                            .formParam("address2", "#{address2}")
                                            .formParam("register.x", "72")
                                            .formParam("register.y", "11")
                                            .check(
                                                    substring("Thank you, <b>#{userName}</b>, for registering")
                                            ),
                                    http("request_7")
                                            .get("/cgi-bin/welcome.pl?page=menus")
                                            .resources(
                                                    http("request_8")
                                                            .get("/cgi-bin/nav.pl?page=menu&in=home"),
                                                    http("request_9")
                                                            .get("/cgi-bin/login.pl?intro=true")
                                            ),
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
                                                    substring(" A Session ID has been created and" +
                                                            " loaded into a cookie called MSO.")
                                            )
                            )
                    )
            );

    {
        setUp(registration.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
