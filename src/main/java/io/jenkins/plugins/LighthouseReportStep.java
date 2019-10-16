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
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;

@Extension
public class LighthouseReportStep extends Builder implements SimpleBuildStep, Serializable {

    @Nonnull
    private String json;

    public LighthouseReportStep() {
        this("");
    }

    @DataBoundConstructor
    public LighthouseReportStep(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        run.addAction(new LighthouseReportBuildAction(this.json));
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
