package com.webapp.sample

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import sun.security.rsa.RSAPublicKeyImpl
import java.util.*
import javax.annotation.security.RolesAllowed
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.Response
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext


@Path("hello2")
class HelloResource {


    fun checkToken(cookie: String): Claims {
        var publicKey = (this::class.java.classLoader).getResource("pki/Public.key").readText().toByteArray()
        publicKey = Base64.getDecoder().decode(publicKey)
        val res = Jwts.parser().setSigningKey(RSAPublicKeyImpl(publicKey)).parseClaimsJws(cookie).body
        return res
    }

    @GET
    //@RolesAllowed(value = *arrayOf("admin", "guest"))
    fun getHello(@CookieParam("JwtToken") cookie: String): Response {
        //this resource is purely for testing purposes
        val claims = checkToken(cookie)
        val roles = claims.get("Roles", ArrayList::class.java)
        if("admin" in roles)
        {
            return Response.status(Status.OK).type("text/plain").entity("HELLO WORLD").build()
        }
        return Response.status(403).build()

    }
}
