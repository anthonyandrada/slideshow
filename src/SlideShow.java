import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SlideShow 
{
	JFrame window;
	JMenu fileMenu;
	JMenuBar menu;
	JToggleButton play;
	JButton previous;
	JButton next;
	CircularLinkedList slides;
	ImageViewer view = new ImageViewer();
	JLabel delayLabel;
	JSlider delay;
	int delayValue;
	CircularIterator iterator;
	final JFileChooser chooser = new JFileChooser();
	final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("SlideShows", "slideshow");
	TimerTask player;
	Timer timer;
	
	public SlideShow()
	{
		makeGUI();
		makeListeners();
	}
	
	public void makeGUI()
	{
		window = new JFrame("Slideshow Viewer");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		window.setSize(d);
		window.setLayout(new BorderLayout());
		
		menu = new JMenuBar();
		fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(open);
		fileMenu.add(exit);
		menu.add(fileMenu);
		
		JPanel control = new JPanel();
		control.setPreferredSize(new Dimension(280, window.getHeight()));
		
		JPanel playPanel = new JPanel();
		playPanel.setLayout(new BorderLayout());
		play = new JToggleButton("Play", false);
		play.setEnabled(false);
		playPanel.add(Box.createRigidArea(new Dimension(10, (int)d.getHeight()/3)));
		playPanel.add(play, BorderLayout.SOUTH);
		
		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new BorderLayout());
		previous = new JButton("Previous");
		previous.setEnabled(false);
		next = new JButton("Next");
		next.setEnabled(false);
		previous.setPreferredSize(new Dimension (120, 30));
		next.setPreferredSize(new Dimension (120, 30));
		JPanel navigatePanel = new JPanel();
		navigatePanel.add(Box.createRigidArea(new Dimension(0, 50)));
		navigatePanel.add(previous);
		navigatePanel.add(next);
		
		selectPanel.add(navigatePanel, BorderLayout.WEST);
		
		JLabel delaySliderLabel = new JLabel("Delay: ");
		delayLabel = new JLabel();
		delayLabel.setForeground(Color.BLUE);
		delay = new JSlider(10, 10000);
		delay.setEnabled(false);
		JPanel delayPanel = new JPanel();
		delayPanel.setLayout(new BorderLayout());
		delayPanel.add(Box.createRigidArea(new Dimension(10, 20)), BorderLayout.NORTH);
		delayPanel.add(delaySliderLabel, BorderLayout.WEST);
		delayPanel.add(delayLabel);
		delayPanel.add(delay, BorderLayout.SOUTH);
		selectPanel.add(playPanel, BorderLayout.NORTH);
		selectPanel.add(delayPanel, BorderLayout.SOUTH);
	
		control.add(selectPanel);
		
		window.add(control, BorderLayout.WEST);
		window.add(view);
		window.setJMenuBar(menu);
		window.setVisible(true);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setResizable(false);
	}
	
	public void makeListeners()
	{	
		menu.getMenu(0).getItem(0).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				selectFile();
			}
		});
		
		menu.getMenu(0).getItem(1).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				window.dispose();
			}
		});
		
		next.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (play.isSelected()) 
				{
					player.cancel();
					player = new play();
					view.updateImage(iterator.next());
					timer.schedule(player, delayValue, delayValue);
				}
				else
				{
					play.setSelected(true);	
					view.updateImage(iterator.next());
				}
			}
		});
		
		previous.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (play.isSelected()) 
				{
					player.cancel();
					player = new play();
					view.updateImage(iterator.previous());
					timer.schedule(player, delayValue, delayValue);
				}
				else
				{
					play.setSelected(true);	
					view.updateImage(iterator.previous());
				}
			}
		});
		
		play.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent arg0) 
			{
				if (arg0.getStateChange() == ItemEvent.SELECTED)
				{
					player = new play();
					timer.schedule(player, delayValue, delayValue);
					play.setText("Pause");
				}
				else if (arg0.getStateChange() == ItemEvent.DESELECTED)
				{
					player.cancel();
					play.setText("Play");
				}
			}
		});
		
		delay.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				if (delay.getValue() < 1000)
				{
					delayLabel.setText(String.valueOf(delay.getValue() + " ms"));
				}
				else if (delay.getValue()%1000 != 0)
				{
					delayLabel.setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(delay.getValue()) + " s " + delay.getValue()%1000 + " ms"));
				}
				else
				{
					delayLabel.setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(delay.getValue()) + " s"));
				}
			}	
		});
		
		delay.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				if(delay.isEnabled())
				{
					if (delay.getValue() < 1000)
					{
						delayLabel.setText(String.valueOf(delay.getValue() + " ms"));
					}
					else if (delay.getValue()%1000 != 0)
					{
						delayLabel.setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(delay.getValue()) + " s " + delay.getValue()%1000 + " ms"));
					}
					else
					{
						delayLabel.setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(delay.getValue()) + " s"));
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				delayLabel.setText(null);
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) 
			{
				delayValue = delay.getValue();
				if (play.isSelected()) 
				{
					player.cancel();
					player = new play();
					timer.schedule(player, delayValue, delayValue);
				}
				else
				{
					play.setSelected(true);	
				}
			}
		});
	}
	
	public void selectFile()
	{
		chooser.setSelectedFile(new File(""));
		chooser.setFileFilter(extensionFilter);
		chooser.setAcceptAllFileFilterUsed(false);
		int r = chooser.showOpenDialog(chooser);
		if (r == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			try
			{
				if (play.isSelected()) 
				{
					play.setSelected(false);
				}
				Scanner in = new Scanner(file);
				slides = new CircularLinkedList();
				while (in.hasNextLine())
				{
					String line = in.nextLine();
					if (in.hasNextLine())
					{
						in.nextLine();
					}
					String[] result = line.split("\\^");
					CaptionedImage img = new CaptionedImage(Integer.parseInt(result[0]), result[1], Integer.parseInt(result[2]), Integer.parseInt(result[3]), result[4]);
					slides.add(img);
				}
				in.close();
				iterator = slides.iterator();
				view.updateImage(slides.getFirst());
				timer = new Timer();
				enableButtons();
			}
			catch (FileNotFoundException e1)
			{
				JOptionPane.showMessageDialog(window, "Invalid File Selected.");
			}
		}
	}
	
	private void enableButtons()
	{
		play.setEnabled(true);
		next.setEnabled(true);
		previous.setEnabled(true);
		delay.setEnabled(true);
		delayValue = delay.getValue();
	}
	
	class play extends TimerTask
	{
		@Override
		public void run() 
		{
			view.updateImage(iterator.next());
		}
	}
}
