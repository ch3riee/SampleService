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

    /*@GET
    @Path("read")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
            //example of using session get
    fun  readCart(@CookieParam("JwtToken") cookie: String, @QueryParam("date") date : String): Response {
        //returns a json array of items
        val claims = checkToken(cookie)
        claims.get("subject", String::class.java)
        //first get tempSecret
        val temp = Unirest.get("http://srvjavausers:8081/rest/services/getServiceToken")
                .queryString("name", "sample")
                .asJson()
        val encrypted = temp.body.getObject().getString("tempSecret")
        var privateKey = (this::class.java.classLoader).getResource("pki/private.key")
                .readText()
                .toByteArray()
        privateKey = Base64.getDecoder().decode(privateKey)
        val cipher2 = Cipher.getInstance("RSA")
        cipher2.init(Cipher.PRIVATE_KEY, RSAPrivateCrtKeyImpl.newKey(privateKey))
        val ret = cipher2.doFinal(DatatypeConverter.parseBase64Binary(encrypted))
        //first get jwt
        val jwtResponse = Unirest.get("http://srvjavausers:8081/rest/services/getServiceToken")
                .queryString("name", "sample")
                .queryString("tempSecret", ret)
                .asJson()
        val jwt = jwtResponse.body.getObject().getString("BearerToken")
        val cartResponse = Unirest.get("http://srvjavausers:8081/rest/session/get")
                .header("Authorization", "bearer " + jwt)
                .queryString("key", "shopping.user." + date)
                .asJson()

        val arr = cartResponse.body.getArray()
        return Response.ok().entity("<html>\n" +
                "  <head>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <p>\n" +
                "      Well, hello there!\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      Here are the items in your cart" +
                arr +
                "    </p>\n" +
                "  </body>\n" +
                "  </head>\n" +
                "  </html>\n").build()
    }*/

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
            //example of using session set
    fun  addCart(@CookieParam("JwtToken") cookie: String, @QueryParam("date") date : String, body: String): Response {
        val mapper = ObjectMapper()

        val cartResponse= Unirest.post("http://srvjavausers:8081/rest/session/set/")
                     .queryString("key", "a")
                .queryString("id", "node016nwsi9xkhztj1sm70gw8lmy4g1" )
                .header("Content-Type", "application/json")
                     .header("authorization", "bearer " +  "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJzYW1wbGUiLCJUb2tlblR5cGUiOiJzZXJ2aWNlIiwiUGVybWlzc2lvbnMiOlsic2Vzc2lvbjptb2RpZnkiLCJzZXNzaW9uOnJlYWQiXSwiUm9sZXMiOlsic2Vzc2lvbk9wZXJhdG9yIl19.J2COZvbU5vhD-AvURnowL0qUglvyMKqo41jlr0M8utET59bja9dofIzSIiwQslStB8vShSCkoNzjzPCIkwtT2-_3IiuHVxxFAy69kc55fvcNmM8yasKsD4n2vJnM22E7ltyHyunAGVo5nKJVN8dtORswEdifgwgxNqX14ZxPhWjZGpkz_4QoiHFHYe_4MjyiTkib2rmi2dgn3TycuCKKo_2z6pb-lrlHk1Us7bs3uuowhhG16iujPVOwlhsTqwc_AZDy8mhyywD0q868g6MMjCUIM_I7Gsq1RvzSKNu_RNchJDXLPLaxkBWeJV84YPvoCMNoJunHNYXyY1gkQT6azg")
                     .body(mapper.writeValueAsString(mapper.readTree(body)))
                .asString()
        val obj = cartResponse.rawBody

        return Response.ok().entity(obj).build()
        }


}


