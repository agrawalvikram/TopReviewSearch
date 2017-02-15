package com.search;

import com.google.gson.Gson;
import com.google.inject.*;
import com.search.datastore.DataStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;

import java.net.URL;

import static spark.Spark.*;


/**
 * The Main Application.
 */
@Slf4j
public class App {

    private static final String LOG4J_CONF = "/log4j.properties";
    private Injector injector;
    private DataStore dataStore;

    /**
     * Gets greeting.
     *
     * @return the greeting
     */
    public String getGreeting() {
        return "Hello world.";
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        App app = new App();
        app.bootStrap();
        app.initializeDatastore(args);
        app.initApis();
    }

    private void bootStrap() {
        try {
            // Load log4j property file
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource("log4j.properties");
            PropertyConfigurator.configure(url);

            // initialize injector
            injector = Guice.createInjector(new AppModule());
        } catch (Exception e) {
            log.error("Unable to bootstrap the app: " + e.getMessage());
        }
    }

    private void initializeDatastore(String[] args) {
        try {
            if(args.length < 2)
                throw new Exception("Insufficient number of parameters");
            String fileName = args[0];
            Integer reviewCount = Integer.parseInt(args[1]);
            dataStore = injector.getInstance(DataStore.class);
            dataStore.initialize(args);

        } catch (Exception e) {
            log.error("Unable to initialize data store: " + e.getMessage(), e);
            log.error("Closing application");
            System.exit(0);
        }
    }

    private void initApis() {

        final Gson gson = new Gson();

        path("/api", () -> {

            get("/search", (req, res) -> {
                log.info(req.queryString());
                String countString = req.queryMap().get("count").value();
                String queryString = req.queryMap().get("query").value();
                if(countString == null || queryString == null) {
                    res.status(HttpStatus.BAD_REQUEST_400);
                    return "parameter count or query is not passed correctly";
                }
                return dataStore.getResults(queryString, countString);
            }, gson::toJson);

            after((request, response) -> {
                response.header("Content-Encoding", "gzip");
            });

        });
    }

}
