package io.vertx.json.schema.impl;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.pointer.JsonPointer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RefTest {

  private static final JsonObject CIRCULAR = new JsonObject(
    "{\n" +
      "  \"$id\": \"http://www.example.com/circular/\",\n" +
      "  \"definitions\": {\n" +
      "    \"addressWithCity\": {\n" +
      "      \"type\": \"object\",\n" +
      "      \"properties\": {\n" +
      "        \"street_address\": {\n" +
      "          \"type\": \"string\"\n" +
      "        },\n" +
      "        \"city\": {\n" +
      "          \"type\": \"string\"\n" +
      "        },\n" +
      "        \"subAddress\": {\n" +
      "          \"anyOf\": [\n" +
      "            {\n" +
      "              \"$ref\": \"http://www.example.com/circular/#/definitions/addressWithCity\"\n" +
      "            }, {\n" +
      "              \"$ref\": \"http://www.example.com/circular/#/definitions/addressWithState\"\n" +
      "            }\n" +
      "          ]\n" +
      "        }\n" +
      "      }\n" +
      "    },\n" +
      "    \"addressWithState\" : {\n" +
      "      \"type\": \"object\",\n" +
      "      \"properties\": {\n" +
      "        \"street_address\": {\n" +
      "          \"type\": \"string\"\n" +
      "        },\n" +
      "        \"state\": {\n" +
      "          \"type\": \"string\"\n" +
      "        },\n" +
      "        \"subAddress\": {\n" +
      "          \"anyOf\": [\n" +
      "            {\n" +
      "              \"$ref\": \"http://www.example.com/circular/#/definitions/addressWithCity\"\n" +
      "            }, {\n" +
      "              \"$ref\": \"http://www.example.com/circular/#/definitions/addressWithState\"\n" +
      "            }\n" +
      "          ]\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
  );

  @Test
  public void testRef() {
    JsonObject resolved = JsonRef.resolve(CIRCULAR);

    assertNotNull(resolved);
    assertNotNull(resolved.getJsonObject("definitions"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("subAddress"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties"));
    assertNotNull(
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("street_address"));
    assertEquals(
      "string",
      resolved
        .getJsonObject("definitions")
        .getJsonObject("addressWithCity")
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("subAddress")
        .getJsonArray("anyOf")
        .getJsonObject(1)
        .getJsonObject("properties")
        .getJsonObject("street_address")
        .getString("type"));
  }

  @Test
  public void testCase2() {
    JsonObject schema = new JsonObject("{\"$id\":\"http://www.example.com/\",\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"definitions\":{\"address\":{\"type\":\"object\",\"properties\":{\"street_address\":{\"type\":\"string\"},\"city\":{\"type\":\"string\"},\"state\":{\"type\":\"string\"},\"subAddress\":{\"$ref\":\"http://www.example.com/#/definitions/address\"}}},\"req\":{\"required\":[\"billing_address\"]}},\"type\":\"object\",\"properties\":{\"billing_address\":{\"$ref\":\"#/definitions/address\"},\"shipping_address\":{\"$ref\":\"#/definitions/address\"}}}");

    //System.out.println(schema.encodePrettily());
    JsonObject resolved = JsonRef.resolve(schema);

    assertThat(JsonPointer.from("/properties/billing_address/properties/city/type").queryJson(resolved)).isEqualTo("string");
  }
}