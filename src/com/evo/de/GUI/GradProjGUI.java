/*
 * gradProjGUI.java
 *
 * Created on 03 Mayıs 2007 Perşembe, 19:38
 */

package com.evo.de.GUI;

import com.evo.de.UnitCommitment;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import java.awt.event.*;

/**
 *
 * @author  root
 */
public class GradProjGUI extends javax.swing.JFrame {
    
    private int numOfGenerator;
    
    private int numOfHour;
    
    private int algorithmType;
    
    private PowerGeneratorGUI generatorGUI[];
    
    private DemandGUI demandGUI[];
    
    private JPanel generatorPanel = new JPanel();
    
    private JPanel demandPanel = new JPanel();
    
    private JFrame frame = this;
    
    private File file = new File("input.txt");
    
    /** Creates new form gradProjGUI */
    private GradProjGUI() 
    {
        initComponents(); 
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setExtendedState(this.MAXIMIZED_BOTH);
        
        jMenuItem1.setText("Run From File");
        jMenuItem2.setText("Exit");
        
        jMenuItem2.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent e)
            {
                frame.dispose();
                System.exit(0);
            }
        }
        );
        
        ActionListener loadAction = new ActionListener()
        {
                public void actionPerformed(ActionEvent e )
                {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnVal = fileChooser.showOpenDialog( frame );

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File f = fileChooser.getSelectedFile();
                        //This is where a real application would open the file.
                        try{
                            readFile( f );
                            System.out.println("readed");
                        }
                        catch(Exception exc){
                            exc.printStackTrace();
                        }
                        repaint();
                        
                    } 
                }
        };
        
        jMenuItem1.addActionListener( loadAction );
        
        this.loadFromFile.addActionListener( loadAction );
        
        this.runButton.setCursor( new Cursor(Cursor.HAND_CURSOR) );
        
        this.loadFromFile.setCursor( new Cursor(Cursor.HAND_CURSOR) );
        
    }
    
    public GradProjGUI( final int numOfGenerator, final int numOfHour, final int type)
    {
        this();
        
        this.numOfGenerator = numOfGenerator;
        
        this.numOfHour = numOfHour;
        
        this.generatorPanel.setLayout(new GridLayout(numOfGenerator,1));
        
        this.demandPanel.setLayout( new GridLayout( 2, (int) (numOfHour / 1.5) ) );
        
        this.algorithmType = type;
        
        this.generatorGUI = new PowerGeneratorGUI[this.numOfGenerator];
        
        for(int i = 0; i < this.numOfGenerator; i++)
        {
            this.generatorGUI[i] = new PowerGeneratorGUI(i);
            
            this.generatorPanel.add( this.generatorGUI[i] );
        }
        
        this.demandGUI = new DemandGUI[ this.numOfHour ];
        
        for(int i = 0; i < this.numOfHour; i++)
        {
            this.demandGUI[i] = new DemandGUI(i);
            
            this.demandPanel.add(this.demandGUI[i]);
        }
            
        this.northPanel.setViewportView( generatorPanel );
        
        this.southPanel.setViewportView( demandPanel );
        
        this.southPanel.setBackground( new Color(153,153,153) );
        
    }
    
    public void writeFile()
    {
        String fileName = "input.txt";
        
        try{
            BufferedWriter file = new BufferedWriter(new FileWriter(fileName));    
            file.write("" + this.numOfGenerator + "\t" + this.numOfHour + "\n");
            
            file.write("Name"+ "\t" + "Pmax"+ "\t" + "Pmin" + "\t" +
                    "a" + "\t" + "b" + "\t" + "c" + "\t" + "t_up" + "\t" + "t_down" + "\t" +
                    "F_cost" + "\t" + "H_Strt" + "\t" + "C_Strt" + "\t" + "T_c_strt" + "\t"+ "initState" + "\n" );
            
            String temp;
            String data;
            for( int i = 0; i < this.numOfGenerator; i++)
            {
                PowerGeneratorGUI generator= this.generatorGUI[ i ];
                file.write(1 + "\t");

                temp = generator.Pmax.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data+ "\t");
                
                temp = generator.Pmin.getText();
                data = temp.equals("") ? "0" : temp;
                file.write(data + "\t");
                
                temp = generator.cost_a.getText();
                data = temp.equals("") ? "1" : temp;
                file.write(data + "\t");
                
                temp = generator.cost_b.getText();
                data = temp.equals("") ? "1" : temp;
                file.write(data + "\t");
                
                temp = generator.cost_c.getText();
                data = temp.equals("") ? "1" : temp;
                file.write( data + "\t");
                
                temp = generator.t_up.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\t");
                
                temp = generator.t_down.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\t");
                
                file.write( "1" + "\t");
                
                temp = generator.hot_start.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\t");
                
                temp = generator.cold_start.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\t");
                
                temp = generator.t_cold_start.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\t");
                
                temp = generator.initial_state.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\n");
            }
            
            file.write("\n");
            
            for(int i = 0; i < this.numOfHour; i++)
            {
                file.write("h"+ i + "\t" );
                
            }
            
            file.write("\n");
            
            for( int i = 0; i < this.numOfHour; i++ ){
                DemandGUI demand = this.demandGUI[i];
                temp = demand.demand.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\t");
            }
            file.write("\n");
            
            for( int i = 0; i < this.numOfHour; i++)
            {
                DemandGUI demand = this.demandGUI[i];
                
                temp = demand.reserve.getText();
                data = temp.equals("") ? "0" : temp;
                file.write( data + "\t");
               
            }
            file.close();
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    
    private void readFile(File file) throws Exception
    { 
        FileReader f = new FileReader(file);
        
        BufferedReader reader = new BufferedReader(f);
        
        String meta = reader.readLine();  //NUM_OF_GENERATOR * NUM_OF_HOUR
        
        String[] metaData = meta.split("\t");
        
        this.numOfGenerator = Integer.parseInt( metaData[0] );
        
        this.numOfHour = Integer.parseInt( metaData[1] );
        
        JPanel newCenterPanel = new JPanel();
        newCenterPanel.setLayout( new GridLayout(numOfGenerator,1) );
        
        JPanel newDemandPanel = new JPanel();
        newDemandPanel.setLayout(  new GridLayout( 2, (int) (numOfHour / 1.5) ) );
        
        this.generatorPanel = newCenterPanel;
        this.demandPanel = newDemandPanel;
        
        reader.readLine();  //header
        
        
        this.generatorGUI = new PowerGeneratorGUI[ this.numOfGenerator ];       
        for(int i = 0; i < this.numOfGenerator; i++){
            String line = reader.readLine();
            
            String column[] = line.split("\t");
            
            PowerGeneratorGUI genGUI = new PowerGeneratorGUI(i);
            this.generatorGUI[ i ] = genGUI;
            
            genGUI.Pmax.setText( column[ 1 ] );
            
            genGUI.Pmin.setText( column[ 2 ] );
            
            genGUI.cost_a.setText( column[ 3 ] );
            
            genGUI.cost_b.setText( column[ 4 ] );
            
            genGUI.cost_c.setText( column[ 5 ] );
            
            genGUI.t_up.setText( column[ 6 ] );
            
            genGUI.t_down.setText( column[ 7 ] );
            
            genGUI.hot_start.setText( column[ 9 ] );
            
            genGUI.cold_start.setText( column[ 10 ] );
            
            genGUI.t_cold_start.setText( column[ 11 ] );
            
            genGUI.initial_state.setText( column[ 12 ] );
            
            this.generatorPanel.add( genGUI );
            
        }
        
        reader.readLine();
        reader.readLine();  //header 
        String []demands = reader.readLine().split("\t");  //Power Demand
        
        this.demandGUI = new DemandGUI[ this.numOfHour ];
        for(int i = 0; i < this.numOfHour; i++)
        {
            DemandGUI dGUI = new DemandGUI(i);
            this.demandGUI[i] = dGUI;
            
            dGUI.demand.setText( demands[i] );
            
            demandGUI[i] = dGUI;
        }
        
        String []reserves = reader.readLine().split("\t");  //Power Reserve
        for(int i = 0; i < this.numOfHour; i++)
        {
            this.demandGUI[i].reserve.setText( reserves[i] );
            this.demandPanel.add( this.demandGUI[i] );
        }
        
        this.northPanel.setViewportView( generatorPanel );
        
        this.southPanel.setViewportView( demandPanel );
        
        f.close();        
    }
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        northPanel = new javax.swing.JScrollPane();
        southPanel = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        loadFromFile = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        allProgressBar = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        F = new javax.swing.JLabel();
        CR = new javax.swing.JLabel();
        F_BOX = new javax.swing.JTextField();
        CR_BOX = new javax.swing.JTextField();
        mutationType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        POP_BOX = new javax.swing.JTextField();
        ITER_BOX = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        run_BOX = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        K_BOX = new javax.swing.JTextField();
        M_BOX = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Unit Commitmet Problem");
        northPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data of Units"));

        southPanel.setBackground(new java.awt.Color(153, 153, 153));
        southPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data of Demand "));
        southPanel.setForeground(new java.awt.Color(153, 153, 153));
        southPanel.setOpaque(false);

        runButton.setBackground(new java.awt.Color(204, 204, 204));
        runButton.setForeground(new java.awt.Color(255, 255, 255));
        runButton.setText("RUN");
        runButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        runButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                runButtonMouseClicked(evt);
            }
        });

        loadFromFile.setBackground(new java.awt.Color(204, 204, 204));
        loadFromFile.setForeground(new java.awt.Color(255, 255, 255));
        loadFromFile.setText("Load From File");
        loadFromFile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(loadFromFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 107, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(runButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(556, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(runButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(loadFromFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Status of Current Run:   ");
        jPanel2.add(jLabel7);

        progressBar.setForeground(new java.awt.Color(102, 102, 102));
        progressBar.setStringPainted(true);
        jPanel2.add(progressBar);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Status of All Run:   ");
        jPanel2.add(jLabel8);

        allProgressBar.setForeground(new java.awt.Color(102, 102, 102));
        allProgressBar.setStringPainted(true);
        jPanel2.add(allProgressBar);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setForeground(new java.awt.Color(153, 153, 153));
        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        F.setText("F");

        CR.setText("Cr");

        F_BOX.setColumns(3);
        F_BOX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        F_BOX.setText("0.2");

        CR_BOX.setColumns(3);
        CR_BOX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        CR_BOX.setText("0.3");

        mutationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "RAND_1", "RAND_2", "BEST_1", "BEST_2" }));

        jLabel1.setText("Mutation");

        jLabel4.setText("Size of Pop.");

        jLabel5.setText("Max Num. Of Iteration");

        POP_BOX.setColumns(5);
        POP_BOX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        POP_BOX.setText("100");

        ITER_BOX.setColumns(5);
        ITER_BOX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ITER_BOX.setText("500");

        jLabel6.setText("Num. Of Run");

        run_BOX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        run_BOX.setText("1");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(mutationType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(F, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(25, 25, 25)
                        .add(F_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(CR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(CR_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 21, Short.MAX_VALUE)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel6)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(POP_BOX)
                    .add(ITER_BOX)
                    .add(run_BOX))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(POP_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4)
                    .add(F)
                    .add(F_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ITER_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5)
                    .add(CR)
                    .add(CR_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel6)
                    .add(run_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(mutationType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Penalty Constants"));
        jLabel2.setText("K");

        jLabel3.setText("M");

        K_BOX.setColumns(5);
        K_BOX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        K_BOX.setText("50");

        M_BOX.setColumns(5);
        M_BOX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        M_BOX.setText("200");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(25, 25, 25)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel3)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(K_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(M_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(K_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(M_BOX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jMenu1.setText("File");
        jMenuItem1.setText("Item");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Item");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, northPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .add(southPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(northPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(southPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 164, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_runButtonMouseClicked

            this.runButton.setEnabled(false);
            this.runButton.setText("Processing...");
        
        
        writeFile();
               
        try{ 
            
            Thread t = null;
            
                
            double F = Double.valueOf( this.F_BOX.getText() );
            double CR = Double.valueOf( this.CR_BOX.getText());
            int numOfPop = Integer.valueOf( this.POP_BOX.getText());
            int numOfIter = Integer.valueOf( this.ITER_BOX.getText() );
            double K = Double.valueOf( this.K_BOX.getText() );
            double M = Double.valueOf( this.M_BOX.getText() );
            int mutType = this.mutationType.getSelectedIndex();
            final int numOfRun = Integer.valueOf(this.run_BOX.getText());

            ProgressGUI progressGUI = new ProgressGUI(this.progressBar,this.allProgressBar,numOfRun, numOfIter);

            UnitCommitment unitCom = new UnitCommitment(progressGUI, this.file);


            unitCom.setConstants( F, CR, numOfPop, numOfIter, K, M, mutType, numOfRun);

            t = new Thread(unitCom);

            
            t.start();
 
            Thread tCheck = new Thread(){
                public void run(){
                    double previousPos = 0;
                    double currentPos = 0;
                    int totalProgress = 0;
                    while(true){
                        try{
                            Thread.sleep(100);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        
                        
                        if( allProgressBar.getPercentComplete() == 1 )
                        {
                            runButton.setText("Run");
                            runButton.setEnabled(true);
                            allProgressBar.setValue(0);
                            progressBar.setValue(0);
                            break;
                        }

                    }
                }
            };
            tCheck.start();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Please enter valid constants");
            e.printStackTrace();
        } 
        
    }//GEN-LAST:event_runButtonMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CR;
    private javax.swing.JTextField CR_BOX;
    private javax.swing.JLabel F;
    private javax.swing.JTextField F_BOX;
    private javax.swing.JTextField ITER_BOX;
    private javax.swing.JTextField K_BOX;
    private javax.swing.JTextField M_BOX;
    private javax.swing.JTextField POP_BOX;
    private javax.swing.JProgressBar allProgressBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton loadFromFile;
    private javax.swing.JComboBox mutationType;
    private javax.swing.JScrollPane northPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton runButton;
    private javax.swing.JTextField run_BOX;
    private javax.swing.JScrollPane southPanel;
    // End of variables declaration//GEN-END:variables
    
}
