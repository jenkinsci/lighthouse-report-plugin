package io.jenkins.plugins;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import static org.apache.commons.lang.StringUtils.isBlank;

public class LighthouseReportStep extends Builder implements SimpleBuildStep, Serializable {

    @Nonnull
    private final String file;

    private String name;

    @DataBoundConstructor
    public LighthouseReportStep(final String file) {
        this.file = file;
        this.name = StringUtils.EMPTY;
    }

    /**
     * Sets the name of the report, if not set default name as lighthousereport is used
     * @param name name of the report
     */
    @DataBoundSetter
    public void setName(final String name) {
        this.name = name;
    }

    public String getFile() {
        return this.file;
    }

    /**
     * Returns report name, can be null if not configured
     * @return name of report
     */
    public String getName() {
        return this.name;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        if (!isBlank(this.getFile())) {
            FilePath f = workspace.child(this.getFile());
            if (f.exists() && !f.isDirectory()) {
                try (InputStream is = f.read()) {
                    run.addAction(new LighthouseReportBuildAction(
                        JSONSerializer.toJSON(IOUtils.toString(is, "UTF-8")).toString(),
                        name
                    ));
                }
            } else if (f.isDirectory()) {
                throw new IllegalArgumentException(Messages.LighthouseReportStep_fileIsDirectory(f.getRemote()));
            } else if (!f.exists()) {
                throw new FileNotFoundException(Messages.LighthouseReportStep_fileNotFound(f.getRemote()));
            }
        }
    }

    @Symbol("lighthouseReport")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.LighthouseReportStep_DescriptorImpl_DisplayName();
        }

    }

}
