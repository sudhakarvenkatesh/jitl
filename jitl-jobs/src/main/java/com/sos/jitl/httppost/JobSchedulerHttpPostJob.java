package com.sos.jitl.httppost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sos.jitl.httppost.JobSchedulerHttpPostJob;
import com.sos.jitl.httppost.JobSchedulerHttpPostJobOptions;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

import sos.util.SOSFile;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.localization.*;
import com.sos.scheduler.messages.JSMessages;
import com.sos.JSHelper.Basics.JSJobUtilities;

public class JobSchedulerHttpPostJob extends JSJobUtilitiesClass<JobSchedulerHttpPostJobOptions> {
    private final String conClassName = "JobSchedulerHttpPostJob"; //$NON-NLS-1$
    private static Logger logger = Logger.getLogger(JobSchedulerHttpPostJob.class);

    protected JobSchedulerHttpPostJobOptions objOptions = null;
    private JSJobUtilities objJSJobUtilities = this;

    protected Vector<File> inputFileList = null;

    protected Iterator<File> inputFileListIterator = null;
    protected int timeout = 0;

    public JobSchedulerHttpPostJob() {
        super(new JobSchedulerHttpPostJobOptions());
    }

    public JobSchedulerHttpPostJobOptions Options() {

        @SuppressWarnings("unused")//$NON-NLS-1$
        final String conMethodName = conClassName + "::Options"; //$NON-NLS-1$

        if (objOptions == null) {
            objOptions = new JobSchedulerHttpPostJobOptions();
        }
        return objOptions;
    }

    public JobSchedulerHttpPostJobOptions Options(final JobSchedulerHttpPostJobOptions pobjOptions) {

        @SuppressWarnings("unused")//$NON-NLS-1$
        final String conMethodName = conClassName + "::Options"; //$NON-NLS-1$

        objOptions = pobjOptions;
        return objOptions;
    }

    public JobSchedulerHttpPostJob Execute() throws Exception {
        final String conMethodName = conClassName + "::Execute"; //$NON-NLS-1$

        logger.debug(String.format(JSMessages.JSJ_I_110.get(), conMethodName));

        try {
            Options().CheckMandatory();
            logger.debug(Options().toString());
            init();
            if (inputFileListIterator != null){
                while (inputFileListIterator.hasNext()) {
                    processSingleFile();
                }
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
            logger.error(String.format(JSMessages.JSJ_F_107.get(), conMethodName), e);
            throw e;
        } finally {
            logger.debug(String.format(JSMessages.JSJ_I_111.get(), conMethodName));
        }

        return this;
    }

    public void processSingleFile() {

        File inputFile = null;
        File outputFile = null;
        String contentType;
        String url = null;

        try {

            outputFile = new File(objOptions.output.Value());
            inputFile = this.inputFileListIterator.next();
            contentType = objOptions.content_type.Value();
            url = objOptions.url.Value();

            if (!inputFile.exists())
                throw new Exception("input file [" + inputFile.getCanonicalPath() + "] does not exist.");
            if (url == null || url.length() == 0)
                throw new Exception("no URL was given to post files.");

            if (objOptions.content_type.isNotDirty()) {
                if (inputFile.getName().endsWith(".xml")) {
                    contentType = "text/xml";
                } else if (inputFile.getName().endsWith(".htm") || inputFile.getName().endsWith(".html")) {
                    contentType = "text/html";
                }

                // encoding ermitteln
                if (contentType.equals("text/html") || contentType.equals("text/xml")) {
                    BufferedReader br = new BufferedReader(new FileReader(inputFile));
                    String buffer = "";
                    String line = null;
                    int c = 0;

                    while ((line = br.readLine()) != null || ++c > 5)
                        buffer += line;
                    Pattern p = Pattern.compile("encoding[\\s]*=[\\s]*['\"](.*?)['\"]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(buffer);
                    if (m.find())
                        contentType += "; charset=" + m.group(1);
                    br.close();
                }
            }

            int responseCode = postFile(inputFile, objOptions.input_filespec.Value(), outputFile, contentType, url);
            logger.info("input file [" + inputFile.getCanonicalPath() + "] processed for URL [" + url + "] with response code " + responseCode);

        } catch (Exception e) {
            String message = "error occurred processing";
            if (inputFile != null) {
                message += " file [" + inputFile.getAbsolutePath() + "]";
            }
            if (url != null && url.length() > 0) {
                message += " for url [" + url + "]";
            }
            logger.warn(message + ": " + e);
        }
    }

    public int postFile(File inputFile, String inputFileSpec, File outputFile, String contentType, String url) throws Exception {

        int rc = 0;

        try {
            if (inputFile.isDirectory()) {
                Vector<File> filelist = SOSFile.getFilelist(inputFile.getCanonicalPath(), inputFileSpec, 0);
                Iterator<File> iterator = filelist.iterator();
                while (iterator.hasNext()) {
                    rc = this.postFile(iterator.next(), inputFileSpec, outputFile, contentType, url);
                }
                return rc;
            } else {
                PostMethod post = new PostMethod(url);
                String content = SOSFile.readFile(inputFile);
                logger.debug("post before replacements: " + content);
                content = objJSJobUtilities.replaceSchedulerVars(false, content);
                logger.debug("Posting: " + content);
                StringRequestEntity req = new StringRequestEntity(content);
                
                post.setRequestEntity(req);
                post.setRequestHeader("Content-type", contentType);

                HttpClient httpClient = new HttpClient();
                if (objOptions.timeout.value() > 0) {
                    HttpConnectionManager httpManager = httpClient.getHttpConnectionManager();
                    HttpConnectionManagerParams httpParams = new HttpConnectionManagerParams();
                    httpParams.setConnectionTimeout(objOptions.timeout.value() * 1000);
                    httpManager.setParams(httpParams);
                }
                rc = httpClient.executeMethod(post);

                if (outputFile != null)
                    logResponse(inputFile, outputFile, post.getResponseBodyAsStream());
                return rc;
            }
        } catch (Exception e) {
            throw new Exception("error occurred in HTTP POST: " + e.getMessage());
        }
    }

    private void logResponse(File inputFile, File outputFile, InputStream responseStream) throws Exception {

        if (outputFile == null)
            throw new Exception("cannot write response: output file is null");
        if (responseStream == null)
            throw new Exception("cannot write response: response is null");

        if (!outputFile.canRead()) {
            File path = new File(outputFile.getParent());
            if (!path.canRead())
                path.mkdirs();
            outputFile.createNewFile();
        }

        if (!outputFile.canWrite())
            throw new Exception("cannot write to file: " + outputFile.getCanonicalPath());

        if (outputFile.isDirectory())
            outputFile = new File(outputFile.getCanonicalPath() + "/" + inputFile.getName());

        FileOutputStream outputStream = new FileOutputStream(outputFile);

        try {
            byte buffer[] = new byte[1000];
            int numOfBytes = 0;

            while ((numOfBytes = responseStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, numOfBytes);
        } catch (Exception e) {
            throw new Exception("error occurred while logging to file [" + outputFile.getCanonicalPath() + "]: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null) {
                    responseStream.close();
                }
            } catch (Exception ex) {
            } // gracefully ignore this error
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception ex) {
            } // gracefully ignore this error
        }
    }

    public void init() {
        @SuppressWarnings("unused")//$NON-NLS-1$
        final String conMethodName = conClassName + "::init"; //$NON-NLS-1$
        doInitialize();
    }

    private void doInitialize() {

        try {

            File inputFile = new File(objOptions.input.Value());
            if (inputFile.isDirectory()) {
                if (!inputFile.canRead()){
                    throw new Exception("input directory is not accessible: " + inputFile.getCanonicalPath());
                }
                logger.info("retrieving files from directory: " + objOptions.input.Value() + " for file specification: " + objOptions.input_filespec.Value());
                this.inputFileList = SOSFile.getFilelist(objOptions.input.Value(), objOptions.input_filespec.Value(), 0);
            } else {
                if (!inputFile.canRead())
                    throw new Exception("input file is not accessible: " + inputFile.getCanonicalPath());
                this.inputFileList = new Vector<File>();
                this.inputFileList.add(inputFile);
            }

            if (this.inputFileList.size() > 0){
                logger.info(this.inputFileList.size() + " input files found");
            }
            this.inputFileListIterator = this.inputFileList.iterator();

        } catch (Exception e) {
            logger.warn("failed to retrieve input files from directory [" + objOptions.input.Value() + ", " + objOptions.input_filespec.Value() + "]: " + e.getMessage());
        }

    }

    public String myReplaceAll(String pstrSourceString, String pstrReplaceWhat, String pstrReplaceWith) {

        String newReplacement = pstrReplaceWith.replaceAll("\\$", "\\\\\\$");
        return pstrSourceString.replaceAll("(?m)" + pstrReplaceWhat, newReplacement);
    }

     
    @Override
    public String replaceSchedulerVars(boolean isWindows, String pstrString2Modify) {
        logger.debug("replaceSchedulerVars as Dummy-call executed. No Instance of JobUtilites specified.");
        return pstrString2Modify;
    }

   
    @Override
    public void setJSParam(String pstrKey, String pstrValue) {

    }

    @Override
    public void setJSParam(String pstrKey, StringBuffer pstrValue) {

    }

    
    public void setJSJobUtilites(JSJobUtilities pobjJSJobUtilities) {

        if (pobjJSJobUtilities == null) {
            objJSJobUtilities = this;
        } else {
            objJSJobUtilities = pobjJSJobUtilities;
        }
        logger.debug("objJSJobUtilities = " + objJSJobUtilities.getClass().getName());
    }

} // class JobSchedulerHttpPostJob