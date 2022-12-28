package computerdatabase;


import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class AlbinsSimulation extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://computer-database.gatling.io")
        .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("en-US,en;q=0.9")
        .doNotTrackHeader("1")
        .upgradeInsecureRequestsHeader("1")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

    private ScenarioBuilder scn = scenario("AlbinsSimulation")
        .exec(
            http("request_0")
                .get("/computers")
        )
        .pause(3)
        .exec(
            http("request_1")
                .get("/computers?f=macbook")
        )
        .pause(2)
        .exec(
            http("request_2")
                .get("/computers/517")
        )
        .pause(18)
        .exec(
            http("request_3")
                .post("/computers/517")
                .formParam("name", "MacBook 13-inch Core 2 Duo 2.13GHz (MC240LL/A) DDR2 Model")
                .formParam("introduced", "2013-12-10")
                .formParam("discontinued", "2020-12-10")
                .formParam("company", "1")
        )
        .pause(16)
        .exec(
            http("request_4")
                .get("/computers?p=1&n=10&s=name&d=asc")
        )
        .pause(2)
        .exec(
            http("request_5")
                .get("/computers?p=2&n=10&s=name&d=asc")
        )
        .pause(7)
        .exec(
            http("request_6")
                .get("/computers/new")
        )
        .pause(20)
        .exec(
            http("request_7")
                .post("/computers")
                .formParam("name", "Albin's Computer")
                .formParam("introduced", "2022-12-22")
                .formParam("discontinued", "2028-12-22")
                .formParam("company", "2")
        );

    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
