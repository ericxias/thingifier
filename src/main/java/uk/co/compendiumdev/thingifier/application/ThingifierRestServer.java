package uk.co.compendiumdev.thingifier.application;

import uk.co.compendiumdev.thingifier.Thing;
import uk.co.compendiumdev.thingifier.Thingifier;
import uk.co.compendiumdev.thingifier.api.*;
import uk.co.compendiumdev.thingifier.generic.definitions.RelationshipDefinition;

import static spark.Spark.*;

public class ThingifierRestServer {

    private String justThePath(String path){
        if(path.startsWith("/")){
            return path.substring(1);
        }
        return path;
    }

    public ThingifierRestServer(String[] args, String path, Thingifier thingifier) {


        before((request, response) -> {
            response.type("application/json");
        });

        // TODO : this now needs HTTP level automated coverage

        // configure it based on a thingifier
        ApiRoutingDefinition routingDefinitions = new ApiRoutingDefinitionGenerator(thingifier).generate();

        for(RoutingDefinition defn : routingDefinitions.definitions()){
            switch (defn.verb()){
                case GET:
                    if(defn.status().isReturnedFromCall()) {
                        get(defn.url(), (request, response) -> {
                            ApiResponse apiResponse = thingifier.api().get(justThePath(request.pathInfo()));
                            response.status(apiResponse.getStatusCode());
                            return apiResponse.getBody();
                        });
                    }
                    break;
                case POST:
                    if(defn.status().isReturnedFromCall()) {
                        post(defn.url(), (request, response) -> {
                            ApiResponse apiResponse = thingifier.api().post(justThePath(request.pathInfo()), request.body());
                            response.status(apiResponse.getStatusCode());
                            return apiResponse.getBody();
                        });
                    }
                    break;
                case HEAD:
                    if(!defn.status().isReturnedFromCall()) {
                        head(defn.url(), (request, response) -> {
                            response.status(defn.status().value());return "";
                        });
                    }
                    break;
                case DELETE:
                    if(!defn.status().isReturnedFromCall()) {
                        delete(defn.url(), (request, response) -> {
                            response.status(defn.status().value());return "";
                        });
                    }else{
                        delete(defn.url(), (request, response) -> {
                            ApiResponse apiResponse = thingifier.api().delete(justThePath(request.pathInfo()));
                            response.status(apiResponse.getStatusCode()); return apiResponse.getBody();});
                    }
                    break;
                case PATCH:
                    if(!defn.status().isReturnedFromCall()) {
                        patch(defn.url(), (request, response) -> {
                            response.status(defn.status().value());return "";
                        });
                    }
                    break;
                case PUT:
                    if(!defn.status().isReturnedFromCall()) {
                        put(defn.url(), (request, response) -> {
                            response.status(defn.status().value());return "";
                        });
                    }else{
                        put(defn.url(), (request, response) -> {
                            ApiResponse apiResponse = thingifier.api().put(justThePath(request.pathInfo()), request.body());
                            response.status(apiResponse.getStatusCode());
                            return apiResponse.getBody();
                        });
                    }
                    break;
                case OPTIONS:
                    if(!defn.status().isReturnedFromCall()) {
                        options(defn.url(), (request, response) -> {
                            response.status(defn.status().value());
                            response.header(defn.header(), defn.headerValue());
                            return "";
                        });
                    }
                    break;
            }
        }


        // nothing else is supported
        head("*", (request, response) -> {response.status(404); return "";});
        get("*", (request, response) -> {response.status(404); return "";});
        options("*", (request, response) -> {response.status(404); return "";});
        put("*", (request, response) -> {response.status(404); return "";});
        post("*", (request, response) -> {response.status(404); return "";});
        patch("*", (request, response) -> {response.status(404); return "";});
        delete("*", (request, response) -> {response.status(404); return "";});

        exception(RuntimeException.class, (e, request, response) -> {
            response.status(400);
            response.body(ApiResponse.getErrorMessageJson(e.getMessage()));
        });

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(ApiResponse.getErrorMessageJson(e.getMessage()));
        });

    }
}
