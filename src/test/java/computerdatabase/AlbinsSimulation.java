package computerdatabase;


import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

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

    ChainBuilder searchForComputer =
        exec(
            http("Load home page")
                .get("/computers")
        )
            .exec(
                http("Search for a computer")
                    .get("/computers?f=macbook")
            )
            .exec(
                http("Load a specific computer")
                    .get("/computers/517")
            )
            .exec(
                http("Edit specific computer")
                    .post("/computers/517")
                    .formParam("name", "MacBook 13-inch Core 2 Duo 2.13GHz (MC240LL/A) DDR2 Model")
                    .formParam("introduced", "2013-12-10")
                    .formParam("discontinued", "2020-12-10")
                    .formParam("company", "1")
            );

    ChainBuilder createComputer =
        exec(
            http("Load page to create a new computer")
                .get("/computers/new")
        )
            .exec(
                http("Create a new computer")
                    .post("/computers")
                    .formParam("name", "Albin's Computer")
                    .formParam("introduced", "2022-12-22")
                    .formParam("discontinued", "2028-12-22")
                    .formParam("company", "2")
            );

    ChainBuilder browse = repeat(5, "n").on(
        exec(http("Page #{n}")
            .get("/computers?p=#{n}"))
            .pause(2)
    );

    private ScenarioBuilder scn = scenario("AlbinsSimulation")
        .exec(searchForComputer, browse, createComputer);

    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
