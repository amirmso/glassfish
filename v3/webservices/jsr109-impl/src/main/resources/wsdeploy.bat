@echo off

REM
REM Copyright 1997-2008 Sun Microsystems, Inc.  All rights reserved.
REM Use is subject to license terms.
REM

java -cp "%~dp0..\modules\jaxb.jar;%~dp0..\modules\webservices.jar;%~dp0..\modules\javax.mail-@VERSION@.jar;%JAVA_HOME%/lib/tools.jar" com.sun.xml.rpc.tools.wsdeploy.Main "%*"
