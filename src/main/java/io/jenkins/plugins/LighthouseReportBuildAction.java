package io.jenkins.plugins;

import hudson.model.Action;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.annotation.CheckForNull;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.regex.Pattern;

public class LighthouseReportBuildAction implements Action, Serializable {

    private static final Pattern FOLDER_NAME_PATTERN = Pattern.compile("[^\\p{L}\\p{Nd}]+");

    private static final String HYPHEN_SEPARATOR = "-";

    private final String json;
    private final String name;

    public LighthouseReportBuildAction(String json, String name) {
        this.json = json;
        this.name = name;
    }

    @CheckForNull
    @Override
    public String getIconFileName() {
        return "/plugin/lighthouse-report/images/128x128/lighthouse.png";
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        if (StringUtils.isNotEmpty(name)) {
            return Messages.LighthouseReportBuildAction_DisplayName_Configured(name);
        }
        return Messages.LighthouseReportBuildAction_DisplayName();
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        if (StringUtils.isNotEmpty(name)) {
            return "lighthousereport_" + sanitizeName(name);
        }
        return "lighthousereport";
    }

    /**
     * Sanitizes the name to only allowed characters [a-zA-Z-_0-9]
     * @param name name of report
     * @return sanitised string
     */
    private String sanitizeName(final String name) {
        return FOLDER_NAME_PATTERN.matcher(name).replaceAll(HYPHEN_SEPARATOR);
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
