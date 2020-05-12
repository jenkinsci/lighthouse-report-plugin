package io.jenkins.plugins;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Label;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;

import static org.junit.Assert.assertNotNull;

public class LighthouseReportStepTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testConfigRoundtrip() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        File file = new File(getClass().getResource("report.json").getFile());
        project.getBuildersList().add(new LighthouseReportStep(file.getAbsolutePath(), "Report"));
        project = jenkins.configRoundtrip(project);
        jenkins.assertEqualDataBoundBeans(new LighthouseReportStep(file.getAbsolutePath(), "Report"), project.getBuildersList().get(0));
    }

    @Test
    public void testBuild() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        File file = new File(getClass().getResource("report.json").getFile());

        LighthouseReportStep builder = new LighthouseReportStep(file.getAbsolutePath(), "Report");
        project.getBuildersList().add(builder);

        FreeStyleBuild completedBuild = jenkins.buildAndAssertSuccess(project);
        assertNotNull(completedBuild.getAction(LighthouseReportBuildAction.class));
    }

    @Test
    public void testScriptedPipeline() throws Exception {
        String agentLabel = "my-agent";
        jenkins.createOnlineSlave(Label.get(agentLabel));
        File file = new File(getClass().getResource("report.json").getFile());

        WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-scripted-pipeline");
        String pipelineScript
                = "node {\n"
                + "  lighthouseReport (file:'" + file.getAbsolutePath() + "', name:'Report')\n"
                + "}";
        job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        WorkflowRun completedBuild = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
        assertNotNull(completedBuild.getAction(LighthouseReportBuildAction.class));
    }

}
