package sos.scheduler.file;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import com.sos.JSHelper.io.Files.JSTextFile;

import org.apache.log4j.Logger;
import org.junit.*;

import java.util.Date;

import static org.junit.Assert.*;

/** \class JSExistsFileJUnitTest - JUnit-Test for "check wether a file exist"
 *
 * \brief MainClass to launch JSExistFile as an executable command-line program
 *
 * 
 *
 * see \see C:\Users\KB\Documents\xmltest\JSExistFile.xml for (more) details.
 *
 * \verbatim ; mechanicaly created by
 * C:\Users\KB\eclipse\xsl\JSJobDoc2JSJUnitClass.xsl from
 * http://www.sos-berlin.com at 20110820121024 \endverbatim */
public class JSExistsFileJUnitTest extends JSToolBox {

    private static final int conNumberOfFiles2Skip = 2;
    private static final int conNumberOfTestFiles = 10;
    private static final String conTestRegularExpression1 = "^.*\\.kb$";
    private static final String conTestBaseFolderName = "j:/e/java/development-testdata/com.sos.scheduler/";
    @SuppressWarnings("unused")//$NON-NLS-1$
    private final static String conClassName = "JSExistsFileJUnitTest";								//$NON-NLS-1$
    @SuppressWarnings("unused")//$NON-NLS-1$
    private static Logger logger = Logger.getLogger(JSExistsFileJUnitTest.class);

    protected JSExistsFileOptions objOptions = null;
    private JSExistsFile objE = null;

    public JSExistsFileJUnitTest() {
        //
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        objE = new JSExistsFile();
        // objE.registerMessageListener(this);
        objOptions = objE.Options();
        // objO.registerMessageListener(this);

        JSListenerClass.bolLogDebugInformation = true;
        JSListenerClass.intMaxDebugLevel = 9;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testExecute() throws Exception {

        boolean flgResult = objE.Execute();
        assertTrue("Directory must exists", flgResult);

        objOptions.file.Value("abcdef");
        flgResult = objE.Execute();
        assertFalse("Directory must exists", flgResult);

        objOptions.file.Value(conTestBaseFolderName);
        flgResult = objE.Execute();
        assertTrue("Directory must exists", flgResult);

    }

    private void createTestFiles() throws Exception {
        String strT = "Testfile Testfile Testfile";
        long lngTimeLag = 3600000;

        for (int i = 0; i < conNumberOfTestFiles; i++) {
            JSTextFile objF = new JSTextFile(conTestBaseFolderName + "testfile" + i + ".kb");
            if (objF.exists()) {
                break;
            }
            objF.Write(strT);
            strT = strT + strT;
            objF.deleteOnExit();
            Date lastModified = new Date(objF.lastModified());
            objF.close();
            boolean blnSuccess = objF.setLastModified(lastModified.getTime() - lngTimeLag);
            lngTimeLag += 3600000;
        }
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testFileSpec() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", conNumberOfTestFiles, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testSkipFirstFile() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.skip_first_files.value(conNumberOfFiles2Skip);
        objOptions.min_file_size.Value("5");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", conNumberOfTestFiles - conNumberOfFiles2Skip, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testSkipLastFile() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.skip_last_files.value(conNumberOfFiles2Skip);
        objOptions.min_file_size.Value("5");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", conNumberOfTestFiles - conNumberOfFiles2Skip, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testMinFileSize() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.min_file_size.Value("4KB");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", 2, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testMaxFileSize() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.max_file_size.Value("4KB");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", 8, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testMinMaxFileSize() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.min_file_size.Value("2KB");
        objOptions.max_file_size.Value("4KB");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", 1, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testMinFileAge() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.min_file_age.Value("02:00:00");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", 8, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testMaxFileAge() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.max_file_age.Value("03:00:00");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", 3, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testMaxFileAge2() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.max_file_age.Value("60");
        long intF = objOptions.max_file_age.calculateFileAge();
        assertEquals("long milliseconds ", 60000L, intF);

        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", 3, objE.getResultList().size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testMinMaxFileAge() throws Exception {
        createTestFiles();
        objOptions.file.Value(conTestBaseFolderName);
        objOptions.file_spec.Value(conTestRegularExpression1);
        objOptions.min_file_age.Value("02:00:00");
        objOptions.max_file_age.Value("05:00:00");
        boolean flgResult = objE.Execute();

        assertTrue("Dateien wurden gefunden", flgResult);
        assertEquals("i expect exactly " + conNumberOfTestFiles + " files", 3, objE.getResultList().size());
    }

} // class JSExistsFileJUnitTest