/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.Hadj;

import Entities.Hadj;
import Luxand.FSDK;
import Luxand.FSDKCam;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumnModel;
import tools.MyInteger;
import view.mainFrame;

/**
 *
 * @author Amine
 */
public class HadjMan extends javax.swing.JInternalFrame {
    private EntityManagerFactory hadjFactory = Persistence.createEntityManagerFactory("HadjPU");
    private EntityManager hadjManager = hadjFactory.createEntityManager();
    
    private String OptionToApply = "EDIT";
    private String photoPath = "";
    private String fingerprint = "";
    
    private mainFrame Parent;
    
    public final Timer drawingTimer;
    private FSDKCam.HCamera cameraHandle;
    private String userName;
    
    private List<FSDK.FSDK_FaceTemplate.ByReference> faceTemplates; // set of face templates (we store 10)
    
    // program states: waiting for the user to click a face
    // and recognizing user's face
    final int programStateRemember = 1;
    final int programStateRecognize = 2;
    private int programState = programStateRecognize;
    
    private String TrackerMemoryFile = "tracker.dat";
    private int mouseX = 0;
    private int mouseY = 0;
    
    private long[] IDs = new long[256]; // maximum of 256 faces detected
    private long[] faceCount = new long[1];
    private long hadjId;
    
    private boolean finger_printed = false;
    private boolean faceCaptured = false;
    
    FSDK.HTracker tracker = new FSDK.HTracker();
    
    BufferedImage __bufImage = null;
    
    /**
     * Creates new form NewJInternalFrame
     */
    public HadjMan(mainFrame P) {
        setInternalFrameStyle();
        Parent = P;
        refreshLists();
        //**** FaceSdk
        final JPanel mainPanel = this.mainPanel;
        
        try {
            int r = FSDK.ActivateLibrary("kXJue60TMu6ftjGPI6upVk2KP7KTRwv5Hf86Fxu3d9Eur9AACYpeG7YoiXfK2aHEqaPcuNf/4NybYPh3mGavNjjTntkxd9PuwEH5wq8E3eyrBSMvfZfnM9nHmWMKSFXJ3WJ8Ft1rQiOZndjAEbWRyXZ0qVFKJJGj92ATiF/7JDQ=");
            if (r != FSDK.FSDKE_OK){
                JOptionPane.showMessageDialog(mainPanel, "Please run the License Key Wizard (Start - Luxand - FaceSDK - License Key Wizard)", "Error activating FaceSDK", JOptionPane.ERROR_MESSAGE); 
                System.exit(r);
            }
        } 
        catch(java.lang.UnsatisfiedLinkError e) {
            JOptionPane.showMessageDialog(mainPanel, e.toString(), "Link Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }    
            
        FSDK.Initialize();
           
        // creating a Tracker
        if (FSDK.FSDKE_OK != FSDK.LoadTrackerMemoryFromFile(tracker, TrackerMemoryFile)) // try to load saved tracker state
            FSDK.CreateTracker(tracker); // if could not be loaded, create a new tracker

        // set realtime face detection parameters
        int err[] = new int[1];
        err[0] = 0;
        FSDK.SetTrackerMultipleParameters(tracker, "HandleArbitraryRotations=false; DetermineFaceRotationAngle=false; InternalResizeWidth=100; FaceDetectionThreshold=5; DetectAge=true; DetectGender=true; HandleArbitraryRotations=false; DetermineFaceRotationAngle=false; InternalResizeWidth=100; FaceDetectionThreshold=5; DetectExpression=true; ", err);
        
        FSDKCam.InitializeCapturing();
                
        FSDKCam.TCameras cameraList = new FSDKCam.TCameras();
        int count[] = new int[1];
        FSDKCam.GetCameraList(cameraList, count);
        if (count[0] == 0){
            JOptionPane.showMessageDialog(mainPanel, "Please attach a camera"); 
            System.exit(1);
        }
        
        String cameraName = cameraList.cameras[0];
        
        FSDKCam.FSDK_VideoFormats formatList = new FSDKCam.FSDK_VideoFormats();
        FSDKCam.GetVideoFormatList(cameraName, formatList, count);
        FSDKCam.SetVideoFormat(cameraName, formatList.formats[0]);
        
        cameraHandle = new FSDKCam.HCamera();
        int r = FSDKCam.OpenVideoCamera(cameraName, cameraHandle);
        if (r != FSDK.FSDKE_OK){
            JOptionPane.showMessageDialog(mainPanel, "Error opening camera"); 
            System.exit(r);
        }
        
        
        // Timer to draw and process image from camera
        drawingTimer = new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FSDK.HImage imageHandle = new FSDK.HImage();
                if (FSDKCam.GrabFrame(cameraHandle, imageHandle) == FSDK.FSDKE_OK){
                    Image awtImage[] = new Image[1];
                    if (FSDK.SaveImageToAWTImage(imageHandle, awtImage, FSDK.FSDK_IMAGEMODE.FSDK_IMAGE_COLOR_24BIT) == FSDK.FSDKE_OK){
                        
                        BufferedImage bufImage = null;
                        FSDK.TFacePosition.ByReference facePosition = new FSDK.TFacePosition.ByReference();
                        
                        FSDK.FeedFrame(tracker, 0, imageHandle, faceCount, IDs); 
                        for (int facesIndex=0; facesIndex<faceCount[0]; ++facesIndex) {
                            FSDK.GetTrackerFacePosition(tracker, 0, IDs[facesIndex], facePosition);
                            
                            int left = facePosition.xc - (int)(facePosition.w * 0.6);
                            int top = facePosition.yc - (int)(facePosition.w * 0.5);
                            int w = (int)(facePosition.w * 1.2);
                            
                            __bufImage = new BufferedImage(awtImage[0].getWidth(null), awtImage[0].getHeight(null), BufferedImage.TYPE_INT_ARGB);
                            bufImage = new BufferedImage(awtImage[0].getWidth(null), awtImage[0].getHeight(null), BufferedImage.TYPE_INT_ARGB);
                            Graphics gr = bufImage.getGraphics(); 
                            gr.drawImage(awtImage[0], 0, 0, null);
                            gr.setColor(Color.green);
                            
                            Graphics gr1 = __bufImage.getGraphics(); 
                            gr1.drawImage(awtImage[0], 0, 0, null);
                            gr1.setColor(Color.green);
                            
    			    String[] name = new String[1];
                            int nameRes = FSDK.GetAllNames(tracker, IDs[facesIndex], name, 65536); // maximum of 65536 characters
                            
                            String [] AttributeValues = new String[1];
                            String [] AttributeValuesAge = new String[1];
                            int res1 = FSDK.GetTrackerFacialAttribute(tracker, 0, IDs[facesIndex], "Gender", AttributeValues, 1024);
                            int res2 = FSDK.GetTrackerFacialAttribute(tracker, 0, IDs[facesIndex], "Age", AttributeValuesAge, 1024);
                            
                            String [] smileAttributeValues = new String[1];
                            int smileRes = FSDK.GetTrackerFacialAttribute(tracker, 0, IDs[facesIndex], "Expression", smileAttributeValues, 1024);
                            
                            if (FSDK.FSDKE_OK == nameRes && FSDK.FSDKE_OK == smileRes && FSDK.FSDKE_OK == res1 && FSDK.FSDKE_OK == res2) { // draw
                                float [] ConfidenceSmile = new float[1];
                                float [] ConfidenceEyesOpen = new float[1];
                                FSDK.GetValueConfidence(smileAttributeValues[0], "Smile", ConfidenceSmile);
                                FSDK.GetValueConfidence(smileAttributeValues[0], "EyesOpen", ConfidenceEyesOpen);
                                
                                float [] ConfidenceAge = new float[1];
                                float [] ConfidenceMale = new float[1];
                                float [] ConfidenceFemale = new float[1];
                                FSDK.GetValueConfidence(AttributeValuesAge[0], "Age", ConfidenceAge);
                                FSDK.GetValueConfidence(AttributeValues[0], "Male", ConfidenceMale);
                                FSDK.GetValueConfidence(AttributeValues[0], "Female", ConfidenceFemale);
                                
                                String str = "Smile: " + Integer.toString((int)(ConfidenceSmile[0]*100))
                                             + "%; Eyes open: " + Integer.toString((int)(ConfidenceEyesOpen[0]*100)) + "%; "
                                             + "Name : "+name[0]+" Age: " + Integer.toString((int)(ConfidenceAge[0])) + ", ";
                                if (ConfidenceMale[0] > ConfidenceFemale[0])
                                    str += "Male, " + Integer.toString((int)(ConfidenceMale[0]*100));
                                else
                                    str += "Female, " + Integer.toString((int)(ConfidenceFemale[0]*100));
                                
                                gr.setFont(new Font("Arial", Font.BOLD, 16));
                                FontMetrics fm = gr.getFontMetrics();
                                java.awt.geom.Rectangle2D textRect = fm.getStringBounds(str, gr);
                                gr.drawString(str, (int)(facePosition.xc - textRect.getWidth()/2), (int)(top + w + textRect.getHeight()));
                                OptionToApply = "EDIT";
                                Hadj h = getHadjIdByName(name[0].trim());
                                if(!Name.getText().equals(name[0].trim())) {
                                    Name.setText(name[0].trim());
                                    if(name[0].trim().length() == 0) {
                                        setHadjId();
                                        Save.setText("Register new Hadj");
                                    }
                                    if(h != null) {
                                        if(!Pin.hasFocus()) Pin.setText(h.getPincode());
                                        Photo.setIcon(new javax.swing.ImageIcon(h.getPhoto_path().replace("\\", "/"))); // NOI18N
                                        Fingerprint.setIcon(new javax.swing.ImageIcon(h.getFingerprint().replace("\\", "/"))); // NOI18N
                                    }
                                }
                                
                                if(h == null) {
                                    if(!Pin.hasFocus() || Save.hasFocus()) Pin.setText("");
                                    if(!faceCaptured) Photo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/photo.png"))); // NOI18N
                                    if(!finger_printed) Fingerprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fingerprint_Idle.png"))); // NOI18N
                                }
                            }

                            if (mouseX >= left && mouseX <= left + w && mouseY >= top && mouseY <= top + w){
                                gr.setColor(Color.blue);
                                
                                if (programStateRemember == programState) {
                                    if (FSDK.FSDKE_OK == FSDK.LockID(tracker, IDs[facesIndex])) {
                                        OptionToApply = "ADD";
                                        Name.setText("");
                                        Pin.setText("");
                                        setHadjId();
                                        // get the user name
                                        userName = (String)JOptionPane.showInputDialog(mainPanel, "Type name:", "Enter the name", JOptionPane.QUESTION_MESSAGE, null, null, "Detected Face");
                                        FSDK.SetName(tracker, IDs[facesIndex], userName);
                                        if (userName == null || userName.length() <= 0) {
                                            FSDK.PurgeID(tracker, IDs[facesIndex]);
                                        }
                                        saveTracker();
                                        FSDK.UnlockID(tracker, IDs[facesIndex]);
                                        //drawingTimer.stop();
                                    }
                                }
                            }
                            programState = programStateRecognize;
                            
                            gr.drawRect(left, top, w, w); // draw face rectangle
                        }
                        
                        // display current frame
                        mainPanel.getRootPane().getGraphics().drawImage((bufImage != null) ? bufImage : awtImage[0], 0, 0, null);
                    }
                    FSDK.FreeImage(imageHandle); // delete the FaceSDK image handle
                }
            }
        });
    }

    public void saveTracker(){
        FSDK.SaveTrackerMemoryToFile(tracker, TrackerMemoryFile);
    }
    
    public void closeCamera(){
        FSDKCam.CloseVideoCamera(cameraHandle);
        //FSDKCam.FinalizeCapturing();
        //FSDK.Finalize();
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    } 
    
    private Vector listToVector(List<String> l) {
        Vector v = new Vector();
        for(String o:l) {
            v.addElement(o);
        }
        
        return v;
    }
    
    private void setInternalFrameStyle() {
        initComponents();
        
        //this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // Set Windows Icon
        List<ImageIcon> icons = new ArrayList<ImageIcon>();
        icons.add(new javax.swing.ImageIcon(getClass().getResource("/img/icon.png")));
        this.setFrameIcon(icons.get(0));
        
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);
    }
    
    //la fct refreshLists met à jour les listes
    public void refreshLists() {
        try {
            refreshItemsList("NOM", "");
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //la fct refreshFamillesList met à jour la liste d'items
    public void refreshItemsList(String constraint, String value) {
        try {
            
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void setColumnWidths(JTable table, int[] widths) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < widths.length; i++) {
            if (i < columnModel.getColumnCount()) {
                columnModel.getColumn(i).setMaxWidth(widths[i]);
            }
            else break;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Name = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Fingerprint = new javax.swing.JLabel();
        Photo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Save = new javax.swing.JButton();
        Pin = new javax.swing.JTextField();

        setTitle("Hajeej");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mainPanelMouseMoved(evt);
            }
        });
        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mainPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mainPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 643, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Face Identification :", mainPanel);

        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });

        jLabel1.setText("Id :");

        Id.setEditable(false);

        jLabel2.setText("Full Name :");

        jLabel3.setText("Finger Print :");

        Fingerprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fingerprint_Idle.png"))); // NOI18N
        Fingerprint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FingerprintMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FingerprintMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FingerprintMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fingerprint)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fingerprint)
        );

        Photo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/photo.png"))); // NOI18N
        Photo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PhotoMouseClicked(evt);
            }
        });

        jLabel5.setText("Photo :");

        jLabel7.setText("Pin Code :");

        Save.setText("Save");
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Name)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Id, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(Pin, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Photo)))
                                .addGap(0, 17, Short.MAX_VALUE))))
                    .addComponent(Save, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(Photo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(Pin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addComponent(Save)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        drawingTimer.start();
    }//GEN-LAST:event_formInternalFrameOpened

    private void mainPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseMoved
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_mainPanelMouseMoved

    private void mainPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseExited
        mouseX = 0;
        mouseY = 0;
    }//GEN-LAST:event_mainPanelMouseExited

    private void mainPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseReleased
        programState = programStateRemember;
    }//GEN-LAST:event_mainPanelMouseReleased

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        //closeCamera();
        
    }//GEN-LAST:event_formInternalFrameClosing

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        closeCamera();
        //timer.cancel();
        //timer.purge();
    }//GEN-LAST:event_formInternalFrameClosed

    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
        Hadj item = new Hadj();
        item.setName(Name.getText());
        item.setPhoto_path(photoPath);
        item.setFingerprint(fingerprint);
        item.setPincode(Pin.getText());

        if(OptionToApply.equals("ADD")) {
            try {
                Hadj selected = hadjManager.createNamedQuery("Hadj.FindByName", Hadj.class)
                    .setParameter("name", item.getName())
                    .getSingleResult();
                if(selected != null) {
                    JOptionPane.showMessageDialog(this, "Error, Name Already Exist !");
                    return;
                }
            } catch(Exception ex) {}
            
            if(saveHadj(item)) {
                JOptionPane.showMessageDialog(this, "Add Success !");
                this.refreshLists();
            }
            else JOptionPane.showMessageDialog(this, "Error Adding !");
        } else if(OptionToApply.equals("EDIT")) {
            Hadj selected = hadjManager.createNamedQuery("Hadj.FindById", Hadj.class)
                    .setParameter("id", MyInteger.parseInt(Id.getText()))
                    .getSingleResult();
            try {
                hadjManager.getTransaction().begin();
                selected.setName(Name.getText());
                selected.setPhoto_path(photoPath);
                selected.setFingerprint(fingerprint);
                selected.setPincode(Pin.getText());
                hadjManager.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "Modification Success !");
                this.refreshLists();
                refreshLists();
                return;
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Altring");
                return;
            }
        }
    }//GEN-LAST:event_SaveActionPerformed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        
    }//GEN-LAST:event_formKeyReleased

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        
    }//GEN-LAST:event_formKeyPressed

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed
        System.out.println(evt.getKeyCode());
    }//GEN-LAST:event_jPanel1KeyPressed

    private void FingerprintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FingerprintMouseClicked
        /*String filename = File.separator+".jpg";
        JFileChooser fc = new JFileChooser(new File(filename));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG FILES", "jpg", "png");
        fc.setFileFilter(filter);
        // Show open dialog; this method does not return until the dialog is closed
        fc.showOpenDialog(this);
        File selFile = fc.getSelectedFile();
        Fingerprint.setIcon(new javax.swing.ImageIcon(selFile.getAbsolutePath())); // NOI18N
        fingerprint = selFile.getAbsolutePath();*/
    }//GEN-LAST:event_FingerprintMouseClicked

    private void PhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PhotoMouseClicked
        /*String filename = File.separator+".jpg";
        JFileChooser fc = new JFileChooser(new File(filename));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG FILES", "jpg", "png");
        fc.setFileFilter(filter);
        // Show open dialog; this method does not return until the dialog is closed
        fc.showOpenDialog(this);
        File selFile = fc.getSelectedFile();
        Photo.setIcon(new javax.swing.ImageIcon(selFile.getAbsolutePath())); // NOI18N
        photoPath = selFile.getAbsolutePath();*/
        try {
            photoPath = new File(".").getCanonicalPath()+"\\images\\"+Name.getText()+".png";
            //System.out.println(photoPath);
            File outputfile = new File(photoPath);
            __bufImage = resize(__bufImage, 175, 192);
            ImageIO.write(__bufImage, "png", outputfile);
            Photo.setIcon(new javax.swing.ImageIcon(__bufImage)); // NOI18N
            faceCaptured = true;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        


    }//GEN-LAST:event_PhotoMouseClicked

    private void FingerprintMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FingerprintMousePressed
        if(evt.getButton() == MouseEvent.BUTTON1) {
            Fingerprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fingerprint_scanning.png"))); // NOI18N
        }
        /*
        System.out.println(evt.getButton());
        if(evt.getButton() == MouseEvent.BUTTON1 && !scan_terminated) {
            
            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    scan_terminated = true;
                    scaning = false;
                    Status.setText("Idle.");
                    Fingerprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Fingerprint_01.jpg"))); // NOI18N
                    //System.out.println(System.currentTimeMillis());
                }
            }, 3000, 1000);
            scaning = true;
            Status.setText("Scanning ...");
            Fingerprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fingerprint_scanning.png"))); // NOI18N
        }*/
    }//GEN-LAST:event_FingerprintMousePressed

    private void FingerprintMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FingerprintMouseReleased
        if(evt.getButton() == MouseEvent.BUTTON1) {
           try {
               Fingerprint.setIcon(new javax.swing.ImageIcon(new File(".").getCanonicalPath()+"\\images\\Fingerprint_01.jpg")); // NOI18N
               finger_printed = true;
               fingerprint = new File(".").getCanonicalPath()+"\\images\\Fingerprint_01.jpg";
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_FingerprintMouseReleased

    private void setHadjId() {                                    
        try {
            hadjManager = hadjFactory.createEntityManager();
            long _id = hadjManager.createNamedQuery("Hadj.getLastId", Long.class).getSingleResult() + 1;
            Id.setText(""+_id);
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error Gethring new ID");
            return;
        }   
    }
    
    private Hadj getHadjIdByName(String Name) {  
        Hadj h;
        if(Name.trim().length() == 0) return null;
        try {
            hadjManager = hadjFactory.createEntityManager();
            h = hadjManager.createNamedQuery("Hadj.FindByName", Hadj.class).setParameter("name", Name).getSingleResult();
            Id.setText(""+h.getId());
            Save.setText("Update Hadj");
            OptionToApply = "EDIT";
        } catch(Exception ex) {
            if(ex.getMessage().contains("did not retrieve any entities")) {
                setHadjId();
                Save.setText("Register new Hadj");
                OptionToApply = "ADD";
            } else {
                ex.printStackTrace();
            }
            
            //JOptionPane.showMessageDialog(this, "Error Gethring ID");
            return null;
        }  
        
        return h;
    }
    
    private boolean saveHadj(Hadj a) {
        try {
            hadjManager.getTransaction().begin();
            hadjManager.persist(a);
            hadjManager.getTransaction().commit();
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            hadjManager.getTransaction().rollback();
            return false;
        }
    }
    
    //delFamille supprime un item
    private void delHadj(Hadj a) {
        if(a == null) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");
            return;
        }
        try {
            hadjManager.getTransaction().begin();
            hadjManager.remove(a);
            hadjManager.getTransaction().commit();
            JOptionPane.showMessageDialog(this, "Correctement supprimé");
            this.refreshLists();
        } catch(Exception ex) {
            ex.printStackTrace();
            hadjManager.getTransaction().rollback();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");
        }
    }
    
    //editFamille modifie un item
    private void editHadj(Hadj a) {
        
    }
    
    private int findItemInList(JComboBox cb, String target) {
        for (int i = 0; i < cb.getModel().getSize(); i++) {
            //System.out.println("'"+cb.getModel().getElementAt(i).toString().toLowerCase()+"'\t'"+target.toLowerCase()+"'");
            if (cb.getModel().getElementAt(i).toString().toLowerCase().equals(target.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Fingerprint;
    private javax.swing.JTextField Id;
    private javax.swing.JTextField Name;
    private javax.swing.JLabel Photo;
    private javax.swing.JTextField Pin;
    private javax.swing.JButton Save;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
