import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageViewer extends JComponent {

	private JLayeredPane layeredPane;
	private ImageIcon img;
	private JLabel captionLabel;
	private JPanel captionPanel;
	
	public ImageViewer() 
	{
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Point p = new Point ();
		p.setLocation(((d.getWidth()-280)*.45), (d.getHeight()*.75));
		captionPanel = new JPanel();
		captionPanel.setSize(new Dimension(0, 0));
		captionPanel.setLocation(0, 0);
		captionPanel.setOpaque(false);

		captionLabel = new JLabel("CAPTION");
		captionLabel.setFont(new Font(null, Font.PLAIN, 22));
		captionLabel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.75f));
		captionLabel.setOpaque(true);

		captionPanel.setLayout(null);
		captionLabel.setLocation(p);

		layeredPane = new JLayeredPane();
		layeredPane.setLayout(null);
		layeredPane.setLocation(0, 0);
		captionPanel.add(captionLabel);
		layeredPane.add(captionPanel, new Integer(2));
		add(layeredPane);

		repaint();
	}

	public void updateImage(CaptionedImage capImage) {
		img = new ImageIcon(capImage.getImagePath());
		captionLabel.setText(capImage.getImageCaption());
		captionLabel.setLocation(capImage.getCaptionLocation())	;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		layeredPane.setSize(getSize());
		captionPanel.setSize(getSize());
		captionLabel.setSize(captionLabel.getPreferredSize());
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		if (img != null) {
			float imgAspect = (float) img.getIconWidth() / img.getIconHeight();
			int newImgHeight = getHeight(), newImgWidth = getWidth();
			if (getWidth() / imgAspect > newImgHeight) {
				newImgWidth = (int) (newImgHeight * imgAspect);
			} else {
				newImgHeight = (int) (newImgWidth / imgAspect);
			}
			int newImgX = (getWidth() - newImgWidth) / 2;
			int newImgY = (getHeight() - newImgHeight) / 2;
			g2.drawImage(img.getImage(), newImgX, newImgY, this.getWidth()
					- newImgX, this.getHeight() - newImgY, 0, 0,
					img.getIconWidth(), img.getIconHeight(), null);
		}
		super.paint(g);
	}
}