package com.webapp.sample

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.mashape.unirest.http.Unirest
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.json.JSONObject
import sun.security.rsa.RSAPrivateCrtKeyImpl
import sun.security.rsa.RSAPublicKeyImpl
import java.io.IOException
import java.util.*
import javax.crypto.Cipher
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("cart")
class CartResource{

    fun checkToken(cookie: String): Claims {
        var publicKey = (this::class.java.classLoader).getResource("pki/Public.key").readText().toByteArray()
        publicKey = Base64.getDecoder().decode(publicKey)
        val res = Jwts.parser().setSigningKey(RSAPublicKeyImpl(publicKey)).parseClaimsJws(cookie).body
        return res
    }

    @GET
    @Path("read")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
            //example of using session get
    fun  readCart(@CookieParam("JwtToken") cookie: String, @QueryParam("date") date : String): Response {
        //returns a json array of items
        val claims = checkToken(cookie)
        claims.get("subject", String::class.java)
        val cartResponse = Unirest.get("http://srvjavausers:8081/rest/session/get")
                .header("Authorization", "bearer "  + "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiVG9rZW5UeX" +
                        "BlIjoic2VydmljZSIsIlBlcm1pc3Npb25zIjpbInNlc3Npb246bW9kaWZ5Iiwic2Vzc2lvbjpyZWFkIl0sI" +
                        "lJvbGVzIjpbInNlc3Npb25PcGVyYXRvciJdfQ.mRjvZxYYZnqzVcevHBIvOkz5izN6Y-SdCW7Bsn_67hQs" +
                        "lpD6T3x2GGarcGO0ZPHLAVSE6CeFS4WfCVLW2H5uuq3GMKkiwzuFqaXvMUDMdaDeq797DfaTIVMTEUTq7H9" +
                        "xWKBXSK2Ft9FXbfAvL4HGy0iCSeynPHBUoGe3zcigjxVMX6WptBAlB7wLJuonMbgTo6ALy3odFd1ueU9Z" +
                        "_1qhG429c1wJsLY8frTNKwXNDkzOcHsW11bnJiZlqD4JrnJWT4YWIq10WZgHk4NXVxO3rO-Ew9HCygFF9" +
                        "nvwsy1C7d4rR6V9roj1NJjQDTtRwA-MECx1l_S7u0kd7j1gWjV1tQ")
                .queryString("key", date)
                .queryString("id", "node0cih2cve5n4co12vvvb10c383h0")
                .asString()

        val arr = cartResponse.rawBody
        return Response.ok().entity(arr).build()
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
            //example of using session set
    fun  addCart(@CookieParam("JwtToken") cookie: String, @QueryParam("date") date : String, body: String): Response {
        val mapper = ObjectMapper()

        val cartResponse= Unirest.post("http://srvjavausers:8081/rest/session/set/")
                .queryString("key", date)
                .queryString("id", "node0cih2cve5n4co12vvvb10c383h0")
                .header("Content-Type", "application/json")
                .header("authorization", "bearer " + "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiVG9rZW5UeX" +
                        "BlIjoic2VydmljZSIsIlBlcm1pc3Npb25zIjpbInNlc3Npb246bW9kaWZ5Iiwic2Vzc2lvbjpyZWFkIl0sI" +
                        "lJvbGVzIjpbInNlc3Npb25PcGVyYXRvciJdfQ.mRjvZxYYZnqzVcevHBIvOkz5izN6Y-SdCW7Bsn_67hQs" +
                        "lpD6T3x2GGarcGO0ZPHLAVSE6CeFS4WfCVLW2H5uuq3GMKkiwzuFqaXvMUDMdaDeq797DfaTIVMTEUTq7H9" +
                        "xWKBXSK2Ft9FXbfAvL4HGy0iCSeynPHBUoGe3zcigjxVMX6WptBAlB7wLJuonMbgTo6ALy3odFd1ueU9Z" +
                        "_1qhG429c1wJsLY8frTNKwXNDkzOcHsW11bnJiZlqD4JrnJWT4YWIq10WZgHk4NXVxO3rO-Ew9HCygFF9" +
                                "nvwsy1C7d4rR6V9roj1NJjQDTtRwA-MECx1l_S7u0kd7j1gWjV1tQ")
                .body(mapper.writeValueAsString(mapper.readTree(body)))
                .asString()
        val obj = cartResponse.rawBody

        return Response.ok().entity(obj).build()
        }


}


