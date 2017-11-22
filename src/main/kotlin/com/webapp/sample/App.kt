package com.webapp.sample

import org.eclipse.jetty.server.*
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature
import org.glassfish.jersey.servlet.ServletContainer

object App {

    @JvmStatic
    fun main(args: Array<String>) {
        var config = ResourceConfig()
        config.packages("com.webapp.sample")
        config.register(RolesAllowedDynamicFeature::class.java)
        config.register(JacksonFeature::class.java)

        val servlet = ServletHolder(ServletContainer(config))
        val server = Server()

        /*for to be used behind Nginx... (mostly for redirect)*/
        val http_config = HttpConfiguration()
        http_config.outputBufferSize = 32768
        http_config.addCustomizer(ForwardedRequestCustomizer())
        val http = ServerConnector(server, HttpConnectionFactory(http_config))
        http.port = 8000
        http.idleTimeout = 30000
        server.addConnector(http)
        /*----------------------------------------------------*/

        val context = ServletContextHandler(server, "/")
        context.addServlet(servlet, "/sample/*")

        try {
            server.start()
            server.join()
        } catch (e: InterruptedException) {
            server.destroy()
        }


    }
}
