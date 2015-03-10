package view;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;

import controller.Controller.CommandListner;
import model.Subject;
import model.TwitterDataSubject;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Color;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.io.IOException;
import java.awt.Font;
import java.awt.Component;
import java.awt.GridLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import net.miginfocom.swing.MigLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class FetchedTweetsView extends JScrollPane implements Observer {

	private TwitterDataSubject subject;
	private JPanel mainPanel;// = new JPanel();
	private ArrayList<JPanel> panelList; 
	private ArrayList<JPanel> impactPanelList;
	private ArrayList<JTextArea> textAreaList; 
	private ArrayList<JLabel> lableList;
	private Subject subjectRef; 
	private String viewRef;
	
	public FetchedTweetsView(TwitterDataSubject subjectReference, String viewRef) throws IOException  {
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// TODO Auto-generated constructor stub
		this.subject = subjectReference;
		this.viewRef = viewRef;
		Dimension d = new Dimension(15,15);
		this.setPreferredSize(d);
		this.mainPanel = new JPanel();
		subjectReference.registerObserver(this);	
		this.setLayout(new ScrollPaneLayout());	
		panelList = new ArrayList<JPanel>();
		impactPanelList = new ArrayList<JPanel>();
		textAreaList = new ArrayList<JTextArea>();
		lableList = new ArrayList<JLabel>();
		
		setViewportView(mainPanel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_panel);
		this.setViewportView(mainPanel);
	}
	

	@Override
	public void update(Subject subject) {
		// TODO Auto-generated method stub
		this.subject = (TwitterDataSubject) subject;
		//ArrayList<String> tweetList = this.subject.getTweets();
		mainPanel.validate();
		
		Map<String,Integer> tweetMap = this.subject.getTweetMap();
		
		int tweetCount = ((TwitterDataSubject) subject).getTweetCount(); 
		
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		
		int i=0;
		for(Map.Entry<String, Integer> entry  : tweetMap.entrySet()){
		//for(int i=0; i<tweetCount;i++){		
		panelList.add(new JPanel());
		impactPanelList.add(new JPanel());
		panelList.get(i).setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		impactPanelList.get(i).setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		impactPanelList.get(i).setLayout(new FlowLayout());
		textAreaList.add(new JTextArea());
		lableList.add(new JLabel());
		
		panelList.get(i).setName("panel_"+Integer.toString(i));
		impactPanelList.get(i).setName("impactPanel_"+Integer.toString(i));
		textAreaList.get(i).setName("txtArea_"+Integer.toString(i));
		lableList.get(i).setName("Label_"+Integer.toString(i));
		lableList.get(i).setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		//impactPanelList.get(i).
		//determioneTweetImpact(int reTweetCount)
		gbc_panel_3.gridy = i;
		mainPanel.add(panelList.get(i), gbc_panel_3);
		
		textAreaList.get(i).setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		textAreaList.get(i).setWrapStyleWord(true);
		lableList.get(i).setText("<html><center>RetweetCount ="+ entry.getValue()+"<br>"+determioneTweetImpact(entry.getValue())+"</center></html>");
		impactPanelList.get(i).setBackground(determineTweetImpactColor(determioneTweetImpact(entry.getValue())));
		textAreaList.get(i).setText(entry.getKey());
		textAreaList.get(i).setLineWrap(true);
		textAreaList.get(i).setEnabled(true);
		textAreaList.get(i).setEditable(false);
		impactPanelList.get(i).add(lableList.get(i));
		
		GroupLayout gl_panel_3 = new GroupLayout(panelList.get(i));
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(textAreaList.get(i), GroupLayout.PREFERRED_SIZE, 350, Short.MAX_VALUE)
					.addComponent(impactPanelList.get(i), GroupLayout.PREFERRED_SIZE, 50, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addComponent(textAreaList.get(i), GroupLayout.PREFERRED_SIZE, 50, Short.MAX_VALUE)
				.addComponent(impactPanelList.get(i), GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)
		);
		panelList.get(i).setLayout(gl_panel_3);
		i++;
		}
		System.out.println("THe text area is running and made "+ tweetCount +" panels & Views");
		
	}
	
	public String determioneTweetImpact(int reTweetCount){
		if(reTweetCount <= 1000){
			return "LOW IMPACT";
		}else if(reTweetCount > 1000 && reTweetCount <= 10000){
			return "MEDIUM IMPACT";
		}else if(reTweetCount > 10000 && reTweetCount <=100000){
			return "HIGH IMPACT";
		}else{
			return "EXTREME IMPACT";
		}
	}
	
	public Color determineTweetImpactColor(String impact){
		
		Color impactColor;
		
		switch(impact)
		{
		case "LOW IMPACT" :
			impactColor = Color.GREEN;
			break;
		case "MEDIUM IMPACT" :
			impactColor = Color.YELLOW;
			break;
		case "HIGH IMPACT" :
			impactColor = Color.RED;
			break;
		case "EXTREME IMPACT" :
			impactColor = Color.PINK;
			break;
		default :
			impactColor = Color.BLUE;
		}
		return impactColor;
	}

	@Override
	public void addActionListener(CommandListner commandListner) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Observer getView() {
	//	if(viewRef.equalsIgnoreCase(this.viewRef)){
			return this;
		//}
		//return null;
	}


	@Override
	public void setVisibility(boolean bool) {
		// TODO Auto-generated method stub
//		this.getParent().setEnabled(bool);;
		this.getViewport().setVisible(bool);
		//tidy this to make a ternary if statement
		if(!bool){
		this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		}else{
			this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		
	}


	@Override
	public String getViewRef() {
		// TODO Auto-generated method stub
		return this.viewRef;
	}
}
