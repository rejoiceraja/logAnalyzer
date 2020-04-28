package edu.luc.logAnalyzer;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
/*import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory; */
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("production")
public class TomcatContainerConfig implements // EmbeddedServletContainerCustomizer {
					WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    private static final Logger logger = LoggerFactory.getLogger(TomcatContainerConfig.class);

    @Override
    //public void customize(final ConfigurableEmbeddedServletContainer container) {
        public void customize( final ConfigurableServletWebServerFactory container) {
        //if (container instanceof TomcatEmbeddedServletContainerFactory) {
    	if (container instanceof TomcatServletWebServerFactory) {
                final TomcatServletWebServerFactory tomcat = (TomcatServletWebServerFactory) container;
                tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
					@Override
					public void customize(Connector connector) {
						//connector.setScheme("https");
	                    //connector.setProxyPort(443);
					}});
                logger.info("Enabled secure scheme (https).");
        } else {
            logger.warn("Could not change protocol scheme because Tomcat is not used as servlet container.");
        }
    }
}
