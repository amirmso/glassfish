/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.s1asdev.ejb.ejb30.hello.session3;

import java.net.*;
import java.io.*;
import javax.naming.*;
import javax.sql.*;
import java.sql.*;

import com.sun.ejte.ccl.reporter.SimpleReporterAdapter;


import javax.annotation.sql.*;


@DataSourceDefinitions(
        value = {


/*               @DataSourceDefinition(name = "java:global/env/Appclient_DataSource",
                        className = "org.apache.derby.jdbc.ClientDataSource",
                        portNumber = 1527,
                        serverName = "localhost",
                        user = "APP",
                        password = "APP",
                        databaseName = "hello-client",
                        properties = {"connectionAttributes=;create=true"}
                ),
*/

                @DataSourceDefinition(name = "java:comp/env/Appclient_DataSource",
                        className = "org.apache.derby.jdbc.ClientDataSource",
                        portNumber = 1527,
                        serverName = "localhost",
                        user = "APP",
                        password = "APP",
                        databaseName = "hello-client",
                        properties = {"connectionAttributes=;create=true"}
                )
        }
)

public class Client {

    private String host;
    private String port;

    private static SimpleReporterAdapter stat =
            new SimpleReporterAdapter("appserv-tests");


    public Client(String[] args) {
        host = (args.length > 0) ? args[0] : "localhost";
        port = (args.length > 1) ? args[1] : "4848";
    }

    public static void main(String[] args) {
        stat.addDescription("datasource-definitionclient");
        Client client = new Client(args);
        client.doTest();
        stat.printSummary("datasource-definitionID");
    }

    public void doTest() {

        String env = null;
        try {

/*
            InitialContext ic = new InitialContext();
            boolean global = lookupDataSource("java:global/env/Appclient_DataSource");
            boolean comp = lookupDataSource("java:comp/env/Appclient_DataSource");
            if (global && comp) {
                System.out.println("4444 appclient Success");
                System.out.println("AppClient successful injection of EMF references!");
            } else {
                System.out.println("4444 appclient failure");
                throw new RuntimeException("Appclient failure");
            }
*/



            InitialContext ic = new InitialContext();


            boolean globalServlet_DataSource = lookupDataSource("java:global/env/Servlet_DataSource", true);
            boolean compServlet_DataSource = lookupDataSource("java:comp/env/Servlet_DataSource", false);

            boolean globalHelloSfulEJB = lookupDataSource("java:global/env/HelloStatefulEJB_DataSource", true);
            boolean compHelloSfulEJB = lookupDataSource("java:comp/env/HelloStatefulEJB_DataSource", false);
            boolean appHelloStatefulEjb = lookupDataSource("java:app/env/HelloStatefulEJB_DataSource", false);

            boolean globalHelloEJB = lookupDataSource("java:global/env/HelloEJB_DataSource", true);
            boolean compHelloEJB = lookupDataSource("java:comp/env/HelloEJB_DataSource", false);

            boolean globalServlet_DD_DataSource = lookupDataSource("java:global/env/Servlet_DD_DataSource", true);
            boolean compServlet_DD_DataSource = lookupDataSource("java:comp/env/Servlet_DD_DataSource", false);

            boolean globalHelloStateful_DD_DataSource = lookupDataSource("java:global/env/HelloStatefulEJB_DD_DataSource", true);
            boolean compHelloStateful_DD_DataSource = lookupDataSource("java:comp/env/HelloStatefulEJB_DD_DataSource", false);

            boolean globalHello_DD_DataSource = lookupDataSource("java:global/env/HelloEJB_DD_DataSource", true);
            boolean compHello_DD_DataSource = lookupDataSource("java:comp/env/HelloEJB_DD_DataSource", false);

            boolean comp = lookupDataSource("java:comp/env/Appclient_DataSource",true);
            boolean comp_dd = lookupDataSource("java:comp/env/Appclient_DD_DataSource",true);
            

            if (comp && comp_dd && globalServlet_DataSource && !compServlet_DataSource && globalHelloSfulEJB &&
                    globalServlet_DD_DataSource && !compServlet_DD_DataSource
                    && !compHelloSfulEJB && globalHelloEJB
                    && !compHelloEJB && globalHelloStateful_DD_DataSource
                    && !compHelloStateful_DD_DataSource && globalHello_DD_DataSource
                    && !compHello_DD_DataSource && !appHelloStatefulEjb)
 {
                System.out.println("AppClient successful lookup of datasource definitions !");
            } else {
                throw new RuntimeException("Appclient failure during lookup of datasource definitions");
            }






            String url = "http://" + host + ":" + port +
                    "/datasource-definition/servlet";
            System.out.println("invoking webclient servlet at " + url);
            int code = invokeServlet(url);


            if (code != 200) {
                System.out.println("Incorrect return code: " + code);
                stat.addStatus("webclient main", stat.FAIL);
            } else {
                stat.addStatus("webclient main", stat.PASS);
            }
        } catch (Exception ex) {
            System.out.println("Jms web test failed.");
            stat.addStatus("webclient main", stat.FAIL);
            ex.printStackTrace();
        }

        return;

    }

    private boolean lookupDataSource(String dataSourceName, boolean expectSuccess) {
        Connection c = null;
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(dataSourceName);
            c = ds.getConnection();
            System.out.println("got connection : " + c);
            return true;
        } catch (Exception e) {
            if(expectSuccess){
            	e.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private int invokeServlet(String url) throws Exception {

        URL u = new URL(url);

        HttpURLConnection c1 = (HttpURLConnection) u.openConnection();
        int code = c1.getResponseCode();
        InputStream is = c1.getInputStream();
        BufferedReader input = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = input.readLine()) != null)
            System.out.println(line);
        if (code != 200) {
            System.out.println("Incorrect return code: " + code);
        }
        return code;
    }

}

