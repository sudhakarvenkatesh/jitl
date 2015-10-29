package sos.scheduler.InstallationService;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.InstallationService.batchInstallationModel.JSInstallations;
import sos.scheduler.InstallationService.batchInstallationModel.JSinstallation;
import sos.scheduler.InstallationService.batchInstallationModel.installations.Globals;
 

public class JSBatchInstallerConfigurationEditor extends Dialog {

	@SuppressWarnings("unused")
	private final String	conClassName	= "JSBatchInstallerConfigurationEditor";
	protected Object		result;
	protected Shell			shlJobSchedulerAgent;
	private Text edGlobalSchedulerId;
	private Text edGlobalPort;
	private Text edGlobalLicenceKey;
	private Text edGlobalAllowedHosts;
	private Text edGlobalFTPPort;
	private Text edGlobalFTPUser;
	private Text edGlobalFTPPassword;
	private Text edGlobalFTPLocalDir;
	private Text edGlobalFTPRemoteDir;
	private Text edGlobalInstallPath;
	private Text edGlobalConfigurationPath;
	private Text edGlobalSSHPort;
	private Text edGlobalSSHUser;
	private Text edGlobalSSHAuthFile;
	private Text edGlobalSSHPassword;
	private Text edGlobalSSHSudoPassword;
	private Table installationItems;
	private Text edGlobalSSHCommand;
	private Text edSchedulerId;
	private Text edHost;
	private Text edPort;
	private Text edLicenceKey;
	private Text edAllowedHosts;
	private Text edInstallPath;
	private Text edConfigurationPath;
	private Text edFTPHost;
	private Text edFTPPort;
	private Text edFTPUser;
	private Text edFTPPassword;
	private Text edFTPLocalDir;
	private Text edFTPRemoteDir;
	private Text edSSHHost;
	private Text edSSHPort;
	private Text edSSHUser;
	private Text edSSHAuthFile;
	private Text edSSHPassword;
	private Text edSSHSudoPassword;
	private Text edSSHCommand;
	
	@SuppressWarnings("unused")
	private final JSBatchInstaller jsBatchInstaller	= null;

 

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public JSBatchInstallerConfigurationEditor(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 * @throws Exception 
	 */
	public Object open() throws Exception {
		createContents();
		
		shlJobSchedulerAgent.open();
		shlJobSchedulerAgent.layout();
		
		readInstallationFile();
		Display display = getParent().getDisplay();
		while (!shlJobSchedulerAgent.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlJobSchedulerAgent = new Shell(getParent(), getStyle());
		shlJobSchedulerAgent.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		shlJobSchedulerAgent.setSize(1018, 712);
		shlJobSchedulerAgent.setText("Job Scheduler Agent Batch Installer");
		shlJobSchedulerAgent.setLayout(new GridLayout(3, false));
		
		Group gpButtons = new Group(shlJobSchedulerAgent, SWT.NONE);
		gpButtons.setForeground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		gpButtons.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		gpButtons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		GridLayout gl_gpButtons = new GridLayout(3, false);
		gl_gpButtons.horizontalSpacing = 9;
		gpButtons.setLayout(gl_gpButtons);
		
		Button btnOk = new Button(gpButtons, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnOk.setText("Save");
		
		Button btnCancel = new Button(gpButtons, SWT.NONE);
		btnCancel.setText("Cancel");
		new Label(gpButtons, SWT.NONE);
		
		Group gpGlobalSettings = new Group(shlJobSchedulerAgent, SWT.NONE);
		gpGlobalSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		gpGlobalSettings.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		gpGlobalSettings.setLayout(new GridLayout(2, false));
		gpGlobalSettings.setText("Gobal Settings");
		
		Label lblSchedulerId = new Label(gpGlobalSettings, SWT.NONE);
		lblSchedulerId.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblSchedulerId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSchedulerId.setText("Scheduler ID");
		
		edGlobalSchedulerId = new Text(gpGlobalSettings, SWT.BORDER);
		GridData gd_edGlobalSchedulerId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_edGlobalSchedulerId.widthHint = 100;
		edGlobalSchedulerId.setLayoutData(gd_edGlobalSchedulerId);
		edGlobalSchedulerId.setText("scheduler_agent_${host}_${scheduler_port}");
		new Label(gpGlobalSettings, SWT.NONE);
		new Label(gpGlobalSettings, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(gpGlobalSettings, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblNewLabel_2.setBounds(0, 0, 49, 13);
		lblNewLabel_2.setText("Port");
		
		edGlobalPort = new Text(gpGlobalSettings, SWT.BORDER);
		edGlobalPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = new Label(gpGlobalSettings, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblNewLabel_3.setText("Licence Key");
		
		edGlobalLicenceKey = new Text(gpGlobalSettings, SWT.BORDER);
		edGlobalLicenceKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_4 = new Label(gpGlobalSettings, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblNewLabel_4.setText("Licence Type");
		
		Combo cbGlobalLicenceType = new Combo(gpGlobalSettings, SWT.NONE);
		cbGlobalLicenceType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cbGlobalLicenceType.setItems(new String[] {"GPL", "Commercial"});
		cbGlobalLicenceType.setVisibleItemCount(2);
		
		Label lblNewLabel_5 = new Label(gpGlobalSettings, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblNewLabel_5.setText("Allowed Hosts");
		
		edGlobalAllowedHosts = new Text(gpGlobalSettings, SWT.BORDER);
		edGlobalAllowedHosts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_6 = new Label(gpGlobalSettings, SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_6.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblNewLabel_6.setText("Install Path");
		
		edGlobalInstallPath = new Text(gpGlobalSettings, SWT.BORDER);
		edGlobalInstallPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_7 = new Label(gpGlobalSettings, SWT.NONE);
		lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_7.setBackground(getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		lblNewLabel_7.setText("Configuration Path");
		
		edGlobalConfigurationPath = new Text(gpGlobalSettings, SWT.BORDER);
		edGlobalConfigurationPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(gpGlobalSettings, SWT.NONE);
		new Label(gpGlobalSettings, SWT.NONE);
		
		TabFolder tabFolder = new TabFolder(gpGlobalSettings, SWT.NONE);
		tabFolder.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_tabFolder.heightHint = 251;
		tabFolder.setLayoutData(gd_tabFolder);
		
	
		TabItem tbtmFtp = new TabItem(tabFolder, SWT.NONE);
		tbtmFtp.setText("FTP");
		
		Group group = new Group(tabFolder, SWT.NONE);
		group.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		tbtmFtp.setControl(group);
		group.setLayout(new GridLayout(2, false));
		
		Label lblPort = new Label(group, SWT.NONE);
		lblPort.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText("Port");
		
		edGlobalFTPPort = new Text(group, SWT.BORDER);
		edGlobalFTPPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUser = new Label(group, SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText("User");
		
		edGlobalFTPUser = new Text(group, SWT.BORDER);
		edGlobalFTPUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_9 = new Label(group, SWT.NONE);
		lblNewLabel_9.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblNewLabel_9.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_9.setText("Password");
		
		edGlobalFTPPassword = new Text(group, SWT.BORDER | SWT.PASSWORD);
		edGlobalFTPPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLocalDir = new Label(group, SWT.NONE);
		lblLocalDir.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblLocalDir.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLocalDir.setText("Local dir");
		
		edGlobalFTPLocalDir = new Text(group, SWT.BORDER);
		edGlobalFTPLocalDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRemoteDir = new Label(group, SWT.NONE);
		lblRemoteDir.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblRemoteDir.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRemoteDir.setText("Remote dir");
		
		edGlobalFTPRemoteDir = new Text(group, SWT.BORDER);
		edGlobalFTPRemoteDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmSsh = new TabItem(tabFolder, SWT.NONE);
		tbtmSsh.setText("SSH");
		
		Group group_1 = new Group(tabFolder, SWT.NONE);
		group_1.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		tbtmSsh.setControl(group_1);
		group_1.setLayout(new GridLayout(2, false));
		
		Label lblPort_1 = new Label(group_1, SWT.NONE);
		lblPort_1.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblPort_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort_1.setText("Port");
		
		edGlobalSSHPort = new Text(group_1, SWT.BORDER);
		edGlobalSSHPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_11 = new Label(group_1, SWT.NONE);
		lblNewLabel_11.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblNewLabel_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_11.setText("User");
		
		edGlobalSSHUser = new Text(group_1, SWT.BORDER);
		edGlobalSSHUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAuthMethod = new Label(group_1, SWT.NONE);
		lblAuthMethod.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblAuthMethod.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAuthMethod.setText("Auth method");
		
		Combo edGlobalSSHAuthMethod = new Combo(group_1, SWT.NONE);
		edGlobalSSHAuthMethod.setItems(new String[] {"password", "publickey"});
		edGlobalSSHAuthMethod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_12 = new Label(group_1, SWT.NONE);
		lblNewLabel_12.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblNewLabel_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_12.setText("Auth file");
		
		edGlobalSSHAuthFile = new Text(group_1, SWT.BORDER);
		edGlobalSSHAuthFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(group_1, SWT.NONE);
		lblPassword.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");
		
		edGlobalSSHPassword = new Text(group_1, SWT.BORDER | SWT.PASSWORD);
		edGlobalSSHPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSudopwd = new Label(group_1, SWT.NONE);
		lblSudopwd.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		lblSudopwd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSudopwd.setText("SudoPwd");
		
		edGlobalSSHSudoPassword = new Text(group_1, SWT.BORDER | SWT.PASSWORD);
		GridData gd_edGlobalSSHSudoPassword = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_edGlobalSSHSudoPassword.heightHint = 17;
		edGlobalSSHSudoPassword.setLayoutData(gd_edGlobalSSHSudoPassword);
		
		Label lblCommand = new Label(group_1, SWT.NONE);
		lblCommand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCommand.setText("Command");
		
		edGlobalSSHCommand = new Text(group_1, SWT.BORDER);
		edGlobalSSHCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group gpInstallationItem = new Group(shlJobSchedulerAgent, SWT.NONE);
		gpInstallationItem.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		gpInstallationItem.setText("Installation Item");
		gpInstallationItem.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		gpInstallationItem.setLayout(new GridLayout(2, false));
		
		Label label = new Label(gpInstallationItem, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Scheduler ID");
		label.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		edSchedulerId = new Text(gpInstallationItem, SWT.BORDER);
		GridData gd_edSchedulerId = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_edSchedulerId.widthHint = 103;
		edSchedulerId.setLayoutData(gd_edSchedulerId);
		
		Label label_1 = new Label(gpInstallationItem, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Host");
		label_1.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		edHost = new Text(gpInstallationItem, SWT.BORDER);
		edHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_2 = new Label(gpInstallationItem, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("Port");
		label_2.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		edPort = new Text(gpInstallationItem, SWT.BORDER);
		edPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_3 = new Label(gpInstallationItem, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText("Licence Key");
		label_3.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		edLicenceKey = new Text(gpInstallationItem, SWT.BORDER);
		edLicenceKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_4 = new Label(gpInstallationItem, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_4.setText("Licence Type");
		label_4.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		Combo cbLicenceTyp = new Combo(gpInstallationItem, SWT.NONE);
		cbLicenceTyp.setVisibleItemCount(2);
		cbLicenceTyp.setItems(new String[] {"GPL", "Commercial"});
		cbLicenceTyp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_5 = new Label(gpInstallationItem, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_5.setText("Allowed Hosts");
		label_5.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		edAllowedHosts = new Text(gpInstallationItem, SWT.BORDER);
		edAllowedHosts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_6 = new Label(gpInstallationItem, SWT.NONE);
		label_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_6.setText("Install Path");
		label_6.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		edInstallPath = new Text(gpInstallationItem, SWT.BORDER);
		edInstallPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_7 = new Label(gpInstallationItem, SWT.NONE);
		label_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_7.setText("Configuration Path");
		label_7.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		edConfigurationPath = new Text(gpInstallationItem, SWT.BORDER);
		edConfigurationPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(gpInstallationItem, SWT.NONE);
		new Label(gpInstallationItem, SWT.NONE);
		
		TabFolder tabFolder_1 = new TabFolder(gpInstallationItem, SWT.NONE);
		GridData gd_tabFolder_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_tabFolder_1.heightHint = 251;
		tabFolder_1.setLayoutData(gd_tabFolder_1);
		tabFolder_1.setBackground(getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		
		 
		TabItem tbtmFtp_1 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmFtp_1.setText("FTP");
		
		Group group_3 = new Group(tabFolder_1, SWT.NONE);
		group_3.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		tbtmFtp_1.setControl(group_3);
		group_3.setLayout(new GridLayout(2, false));
		
		Label label_8 = new Label(group_3, SWT.NONE);
		label_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_8.setText("Host");
		label_8.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edFTPHost = new Text(group_3, SWT.BORDER);
		edFTPHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_9 = new Label(group_3, SWT.NONE);
		label_9.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_9.setText("Port");
		label_9.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edFTPPort = new Text(group_3, SWT.BORDER);
		edFTPPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUser_1 = new Label(group_3, SWT.NONE);
		lblUser_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser_1.setText("User");
		
		edFTPUser = new Text(group_3, SWT.BORDER);
		edFTPUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_11 = new Label(group_3, SWT.NONE);
		label_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_11.setText("Password");
		label_11.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edFTPPassword = new Text(group_3, SWT.BORDER | SWT.PASSWORD);
		edFTPPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_12 = new Label(group_3, SWT.NONE);
		label_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_12.setText("Local dir");
		label_12.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edFTPLocalDir = new Text(group_3, SWT.BORDER);
		edFTPLocalDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_13 = new Label(group_3, SWT.NONE);
		label_13.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_13.setText("Remote dir");
		label_13.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edFTPRemoteDir = new Text(group_3, SWT.BORDER);
		edFTPRemoteDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmSsh_1 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmSsh_1.setText("SSH");
		
		Group group_4 = new Group(tabFolder_1, SWT.NONE);
		group_4.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		tbtmSsh_1.setControl(group_4);
		group_4.setLayout(new GridLayout(2, false));
		
		Label label_14 = new Label(group_4, SWT.NONE);
		label_14.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_14.setText("Host");
		label_14.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edSSHHost = new Text(group_4, SWT.BORDER);
		edSSHHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_15 = new Label(group_4, SWT.NONE);
		label_15.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_15.setText("Port");
		label_15.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edSSHPort = new Text(group_4, SWT.BORDER);
		edSSHPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_16 = new Label(group_4, SWT.NONE);
		label_16.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_16.setText("User");
		label_16.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edSSHUser = new Text(group_4, SWT.BORDER);
		edSSHUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_17 = new Label(group_4, SWT.NONE);
		label_17.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_17.setText("Auth method");
		label_17.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		Combo edSSHAuthMethod = new Combo(group_4, SWT.NONE);
		edSSHAuthMethod.setItems(new String[] {"password", "publickey"});
		edSSHAuthMethod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_18 = new Label(group_4, SWT.NONE);
		label_18.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_18.setText("Auth file");
		label_18.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edSSHAuthFile = new Text(group_4, SWT.BORDER);
		edSSHAuthFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_19 = new Label(group_4, SWT.NONE);
		label_19.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_19.setText("Password");
		label_19.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edSSHPassword = new Text(group_4, SWT.BORDER | SWT.PASSWORD);
		edSSHPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_20 = new Label(group_4, SWT.NONE);
		label_20.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_20.setText("SudoPwd");
		label_20.setBackground(getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		edSSHSudoPassword = new Text(group_4, SWT.BORDER | SWT.PASSWORD);
		GridData gd_edSSHSudoPassword = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_edSSHSudoPassword.heightHint = 17;
		edSSHSudoPassword.setLayoutData(gd_edSSHSudoPassword);
		
		Label label_21 = new Label(group_4, SWT.NONE);
		label_21.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_21.setText("Command");
		
		edSSHCommand = new Text(group_4, SWT.BORDER);
		edSSHCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(gpInstallationItem, SWT.NONE);
		new Label(gpInstallationItem, SWT.NONE);
		
		
		installationItems = new Table(shlJobSchedulerAgent, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_installationItems = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_installationItems.widthHint = 360;
		installationItems.setLayoutData(gd_installationItems);
		installationItems.setHeaderVisible(true);
		installationItems.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(installationItems, SWT.NONE);
		tblclmnNewColumn.setWidth(81);
		tblclmnNewColumn.setText("Scheduler ID");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(installationItems, SWT.NONE);
		tblclmnNewColumn_1.setWidth(50);
		tblclmnNewColumn_1.setText("Host");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(installationItems, SWT.NONE);
		tblclmnNewColumn_2.setWidth(50);
		tblclmnNewColumn_2.setText("Port");
		
		TableColumn tblclmnNewColumn_3 = new TableColumn(installationItems, SWT.NONE);
		tblclmnNewColumn_3.setWidth(86);
		tblclmnNewColumn_3.setText("Licence Key");
		
		TableColumn tblclmnNewColumn_4 = new TableColumn(installationItems, SWT.NONE);
		tblclmnNewColumn_4.setWidth(85);
		tblclmnNewColumn_4.setText("Licence Type");
		
		TableColumn tblclmnNewColumn_5 = new TableColumn(installationItems, SWT.NONE);
		tblclmnNewColumn_5.setWidth(85);
		tblclmnNewColumn_5.setText("Allowed Hosts");
		
 
	}

	private final Display display = Display.getCurrent();

	public Color getSystemColor(int systemColorID) {
		return display.getSystemColor(systemColorID);
	}

	private void setText(Text t, String value) {
		if (value == null){
			value="";
		}
	}
	
	private void readInstallationFile() throws Exception {
		File installationDefinitionFile = new File("C:\\Dokumente und Einstellungen\\Uwe Risse\\Eigene Dateien\\sos-berlin.com\\jobscheduler.1.3.9\\scheduler_139\\config\\live\\batchAgentInstallation","scheduler_agent_installations.xml");
		JSInstallations jsInstallations = new JSInstallations(installationDefinitionFile);
		jsInstallations.readInstallationDefinitionFile();
		Globals globals = jsInstallations.getInstallations().getGlobals();		
		setText(edGlobalSchedulerId,globals.getSchedulerId());
    	edGlobalPort.setText(String.valueOf(globals.getSchedulerPort()));
		setText(edGlobalLicenceKey,globals.getLicence());
		setText(edGlobalAllowedHosts,globals.getLicenceOptions());
		edGlobalFTPPort.setText(String.valueOf(globals.getFtp().getPort()));
		setText(edGlobalFTPUser,globals.getFtp().getUser());
		setText(edGlobalFTPPassword,globals.getFtp().getPassword());
		setText(edGlobalFTPLocalDir,globals.getFtp().getLocalDir());
		setText(edGlobalFTPRemoteDir,globals.getFtp().getRemoteDir());
		setText(edGlobalInstallPath,globals.getInstallPath());
		setText(edGlobalConfigurationPath,globals.getUserPathPanelElement());
  		edGlobalSSHPort.setText(String.valueOf(globals.getSsh().getPort()));
		setText(edGlobalSSHUser,globals.getSsh().getUser());
		setText(edGlobalSSHAuthFile,globals.getSsh().getAuthFile());
		setText(edGlobalSSHPassword,globals.getSsh().getPassword());
		setText(edGlobalSSHSudoPassword,globals.getSsh().getSudoPassword());
 		
		while (!jsInstallations.eof()) {
			JSinstallation installation = jsInstallations.next();
  		}
	}
	
	public static void main(String[] args) {
		   try {
		       
		       Shell shell = new Shell();
		       JSBatchInstallerConfigurationEditor window = new JSBatchInstallerConfigurationEditor(shell,1);
		 	     
				 window.open();
			  } catch (Exception e) {
				 e.printStackTrace();
			  } 
	}
}