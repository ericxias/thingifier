package uk.co.compendiumdev.thingifier.api;

import uk.co.compendiumdev.thingifier.Thing;
import uk.co.compendiumdev.thingifier.Thingifier;
import uk.co.compendiumdev.thingifier.api.http.bodyparser.BodyParser;
import uk.co.compendiumdev.thingifier.api.response.ApiResponse;
import uk.co.compendiumdev.thingifier.api.restapihandlers.*;
import uk.co.compendiumdev.thingifier.generic.definitions.FieldValue;
import uk.co.compendiumdev.thingifier.generic.definitions.RelationshipVector;
import uk.co.compendiumdev.thingifier.generic.definitions.ThingDefinition;
import uk.co.compendiumdev.thingifier.generic.instances.ThingInstance;
import uk.co.compendiumdev.thingifier.query.SimpleQuery;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ThingifierRestAPIHandler {
    private final Thingifier thingifier;
    private final RestApiDeleteHandler delete;
    private final RestApiPostHandler post;
    private final RestApiPutHandler put;
    private final RestApiGetHandler get;

    public ThingifierRestAPIHandler(final Thingifier aThingifier) {
        this.thingifier = aThingifier;
        this.get = new RestApiGetHandler(aThingifier);
        this.delete = new RestApiDeleteHandler(aThingifier);
        this.post = new RestApiPostHandler(aThingifier);
        this.put = new RestApiPutHandler(aThingifier);
    }


    // TODO: we should be able to accept xml with correct content type
    // TODO: we should be able to accept html forms with correct content type
    // todo allow an accept text/html to create different output - (probably handled by routings rather than api)
    // TODO : this whole class needs to be refactored and wrapped with unit tests
    // todo : generate examples when outputing the api documentation

    // TODO: - listed here https://www.lisihocke.com/2018/07/testing-tour-stop-16-pair-exploring-an-api-with-thomas.html
    // TODO: ensure that relationshps enforce the type of thing e.g. if I pass in a GUID of the wrong type then it should not cross ref
    // TODO: possibly consider an X- header which has the number of items in the collection

    public ApiResponse get(final String url) {
        return get.handle(url);
    }

    public ApiResponse delete(final String url) {
        return delete.handle(url);
    }

    public ApiResponse post(final String url, final BodyParser args) {
        return post.handle(url, args);
    }

    public ApiResponse put(final String url, final BodyParser args) {
        return put.handle(url, args);
    }











}