<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<l:settingsGroup title="RunDeck Parameters">


    <props:workingDirectory />

    <tr>
        <th>
            <label for="runDeckUrl">Rundeck URL:</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:textProperty name="runDeckUrl" className="longField"/>
            </div>
            <span class="smallNote">Rundeck URL:</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="runDeckAPIToken">API Token:</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:textProperty name="runDeckAPIToken" className="longField"/>
            </div>
            <span class="smallNote">API Token</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="runDeckJobID">Job Identifier:</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:textProperty name="runDeckJobID" className="longField"/>
            </div>
            <span class="smallNote">Job identifier</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="runDeckJobOptions">Job Options:</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:multilineProperty linkTitle="Job Options" name="runDeckJobOptions" className="longField" cols="20" rows="5"/>
            </div>
            <span class="smallNote">Enter each option on its own line, with format name=value</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="runDeckNodeFilter">Node Filter</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:multilineProperty linkTitle="Filters" name="runDeckNodeFilter" className="longField" cols="20" rows="5"/>
            </div>
            <span class="smallNote">Enter each node on its own line</span>
        </td>
    </tr>

    <tr>
        <th>
            <label for="runDeckIncludeOutput">Include job output in log:</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:checkboxProperty name="runDeckIncludeOutput" className="checkboxField"/>
            </div>
            <span class="smallNote">Include output from job</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="runDeckFailBuild">Fail build if job fails:</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:checkboxProperty name="runDeckFailBuild" className="checkboxField"/>
            </div>
            <span class="smallNote">Make the build fail</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="runDeckWaitForJob">Wait for job to finish:</label>
        </th>
        <td>
            <div class="completionIconWrapper">
                <props:checkboxProperty name="runDeckWaitForJob" className="checkboxField"/>
            </div>
            <span class="smallNote">Wait until the job has finished. Please note that if this is not checked, build won't fail if job fails</span>
        </td>
    </tr>




</l:settingsGroup>
