package com.sos.jitl.reporting.job.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.JSHelper.Basics.JSToolBox;

public class AggregationJobMain extends JSToolBox {

    private final static String className = AggregationJobMain.class.getSimpleName();
    private static Logger logger = LoggerFactory.getLogger(AggregationJobMain.class);

    public final static void main(String[] args) {
        final String methodName = className + "::main";

        logger.info(String.format(methodName));
        int exitCode = 0;
        AggregationJob job = new AggregationJob();
        try {
            AggregationJobOptions options = job.getOptions();
            options.commandLineArgs(args);

            job.execute();
            logger.info(String.format("JSJ-I-106: %1$s - ended without errors", methodName));
        } catch (Exception e) {
            exitCode = 99;
            logger.error(String.format("JSJ-E-105: %1$s - terminated with exit-code %2$d", methodName, exitCode), e);
        } finally {
            job.exit();
        }
        System.exit(exitCode);
    }

}