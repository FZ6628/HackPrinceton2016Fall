import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class Dance{
	JFileChooser fc;
	private static JFrame secondframe;
	private static JFrame firstframe;

	private static JButton restartButton;

	private static EmbeddedMediaPlayerComponent mediaPlayerComponent;

	private static JButton pauseButton;

	private static JButton PlayBothButton;

	private  static JButton AdvanceButton;
	static String test;
	static String ytText;



	private static Robot robot;

	public static void main(final String[] args) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NativeInterface.open();
		new NativeDiscovery().discover();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Dance(args);
			}
		});
		NativeInterface.runEventPump();
		// don't forget to properly close native components
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				NativeInterface.close();
			}
		}));
	}

	public Dance(String[] args) {
		firstframe = new JFrame("It's so Sympoh");
		firstframe.setBounds(100, 100, 600, 400);
		firstframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane1 = new JPanel();
		JPanel stuff = new JPanel();
		final JTextField field = new JTextField(50);
		final JLabel label = new JLabel("URL:");
		stuff.setLayout(new BoxLayout(stuff, BoxLayout.PAGE_AXIS));
		contentPane1.setLayout(new BorderLayout());
		contentPane1.add(stuff, BorderLayout.CENTER);
		new JButton("File chooser");
		AdvanceButton = new JButton("Go!");
		stuff.add(new FileChooser());
		stuff.add(label);
		stuff.add(field);
		stuff.add(AdvanceButton);
		firstframe.setContentPane(contentPane1);
		firstframe.setVisible(true);
		AdvanceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ytText= field.getText();
				System.out.println(ytText);
				next();
			}
		});

	}
	protected void next(){
		secondframe = new JFrame("It's so Sympoh");
		secondframe.setBounds(100, 100, 600, 400);
		secondframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		secondframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println(e);
				mediaPlayerComponent.release();
				System.exit(0);
			}
		});
		JPanel contentPane2 = new JPanel();
		contentPane2.setLayout(new BorderLayout());
		JPanel viewing = new JPanel();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		contentPane2.add(viewing, BorderLayout.CENTER);
		viewing.setLayout(new BoxLayout(viewing, BoxLayout.LINE_AXIS));
		viewing.add(mediaPlayerComponent);
		viewing.add(getBrowserPanel());
		JPanel controlsPane = new JPanel();
		pauseButton = new JButton("Play/Pause");
		restartButton = new JButton("Restart");
		PlayBothButton = new JButton("Play/Pause Both");
		controlsPane.add(pauseButton);
		controlsPane.add(restartButton);
		controlsPane.add(PlayBothButton);
		contentPane2.add(controlsPane, BorderLayout.SOUTH);

		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().pause();
			}
		});

		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().playMedia(("res\\SampleVideo_1280x720_1mb.mp4"));
			}
		}); PlayBothButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().pause();
				robot.mouseMove(secondframe.getX() + 335, secondframe.getY() + 335);
				robot.mousePress( InputEvent.BUTTON1_MASK );
				robot.mouseRelease( InputEvent.BUTTON1_MASK );
			}
		});


		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			/*    @Override
         public void playing(MediaPlayer mediaPlayer) {
             SwingUtilities.invokeLater(new Runnable() {
                 @Override
                 public void run() {
                     secondframe.setTitle(String.format(
                         "My First Media Player - %s",
                         mediaPlayerComponent.getMediaPlayer().getMediaMeta().getTitle()
                     ));
                 }
             });
         }
			 */
			/*@Override
         public void finished(MediaPlayer mediaPlayer) {
             SwingUtilities.invokeLater(new Runnable() {
                 @Override
                 public void run() {
                      mediaPlayerComponent.getMediaPlayer().playMedia("res\\SampleVideo_1280x720_1mb.mp4");
                 }
             });
        }
			 */
			@Override
			public void error(MediaPlayer mediaPlayer) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(
								secondframe,
								"Failed to play media",
								"Error",
								JOptionPane.ERROR_MESSAGE
								);
						closeWindow();
					}
				});
			}
		});

		secondframe.setContentPane(contentPane2);
		secondframe.setVisible(true);

		mediaPlayerComponent.getMediaPlayer().playMedia(test);
	}
	private void closeWindow() {
		secondframe.dispatchEvent(new WindowEvent(secondframe, WindowEvent.WINDOW_CLOSING));
	}
	public static JPanel getBrowserPanel() {
		JPanel webBrowserPanel = new JPanel(new BorderLayout());
		JWebBrowser webBrowser = new JWebBrowser();
		webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
		webBrowser.setBarsVisible(false);
		webBrowser.navigate(ytText);
		return webBrowserPanel;
	}

	public class FileChooser extends JPanel
	implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JButton openButton;
		JFileChooser fc;

		public FileChooser() {
			super(new BorderLayout());


			//Create a file chooser
			fc = new JFileChooser();

			//Uncomment one of the following lines to try a different
			//file selection mode.  The first allows just directories
			//to be selected (and, at least in the Java look and feel,
			//shown).  The second allows both files and directories
			//to be selected.  If you leave these lines commented out,
			//then the default mode (FILES_ONLY) will be used.
			//
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			//Create the open button.  We use the image from the JLF
			//Graphics Repository (but we extracted it from the jar).
			openButton = new JButton("Open");
			openButton.addActionListener(this);

			//For layout purposes, put the buttons in a separate panel
			JPanel buttonPanel = new JPanel(); //use FlowLayout
			buttonPanel.add(openButton);
			
			//Add the buttons and the log to this panel.
			add(buttonPanel, BorderLayout.PAGE_START);
		}

		public void actionPerformed(ActionEvent e) {

			//Handle open button action.
			if (e.getSource() == openButton) {
				int returnVal = fc.showOpenDialog(FileChooser.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					test = fc.getSelectedFile().getAbsolutePath();
					System.out.println(test);
					//This is where a real application would open the file.

				} else {
				}
			}
		}
	}
}