package io.jenkins.plugins;

import hudson.model.Action;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.annotation.CheckForNull;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;

public class LighthouseReportBuildAction implements Action, Serializable {
    private final String json;

    public LighthouseReportBuildAction(String json) {
        this.json = json;
    }

    @CheckForNull
    @Override
    public String getIconFileName() {
        return "/plugin/lighthouse-report/images/128x128/lighthouse.png";
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        return Messages.LighthouseReportBuildAction_DisplayName();
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return "lighthousereport";
    }

    @SuppressWarnings("unused")
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        rsp.setStatus(HttpServletResponse.SC_OK);
        rsp.setContentType("text/html");
        req.getView(this, "client.jelly").forward(req, rsp);
    }

    @SuppressWarnings("unused")
    public void doReportJson(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        ServletOutputStream outputStream = rsp.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
        rsp.setContentType("text/javascript");
        osw.write("window.__LIGHTHOUSE_JSON__ = " + json + ";");
        osw.flush();
        osw.close();
    }

}
