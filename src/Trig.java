import javax.swing.JFrame;


public class Trig extends JFrame {

	public Trig(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		TrigPanel fp = new TrigPanel();
		this.add(fp);
	}
	
	public static void main(String[] args) {
		Trig f = new Trig();
		f.pack();
		f.setVisible(true);

	}

}
