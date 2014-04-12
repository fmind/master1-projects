package views;

import components.CheckResult;
import config.GameConfig;
import controllers.GameController;
import controllers.LauncherController;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import models.Achievement;

/**
 * Game windows
 *
 * @author freaxmind
 */
public class GameFrame extends javax.swing.JFrame {

    /**
     * Timer task to handle the chronometer
     */
    private static class ChronoTask extends TimerTask {

        private JLabel label;

        /**
         * Constructor
         *
         * @param label label to use as chronometer
         */
        public ChronoTask(JLabel label) {
            this.label = label;
        }

        /**
         * +1 on label every second
         */
        @Override
        public void run() {
            String v = String.valueOf(Integer.valueOf(this.label.getText()) + 1);
            this.label.setText(v);
        }
    }
    private Timer timer;            // chronometer
    private String startingCode;    // code when the mission is loaded
    private boolean started;        // true if user has pressed start
    private static final String TEST_PREFIX = "                                          VOTRE TEST\n\n";
    private static final String STORY_PREFIX = "                                         HISTOIRE\n\n";

    /**
     * Creates new form GameFrame
     */
    public GameFrame() {
        initComponents();
        this.timer = null;
        this.started = false;

        StringBuilder str = new StringBuilder("");
        str.append("// Votre code\n"
                + "\n"
                + "// exemple:\n"
                + "\n"
                + "public int repartir(int... poids) {\n"
                + "	// à vous de jouer\n"
                + "}");
        this.startingCode = str.toString();

        // game icon
        try {
            URL imgURL = new File(GameConfig.APP_ICON).toURI().toURL();
            ImageIcon icon = new ImageIcon(imgURL);
            this.setIconImage(icon.getImage());
        } catch (MalformedURLException ex) {
            Logger.getLogger("").log(Level.WARNING, "Impossible de charger l'image de l'application");
        }
    }

    /**
     * Ask the user for a name
     *
     * @return a name (< 10 caracters)
     */
    public String askName() {
        String name = (String) JOptionPane.showInputDialog(
                this,
                "Pour figurer dans les meilleurs scores, entrez votre nom:\nappuyer sur annuler pour ne rien poster",
                "TOP SCORE",
                JOptionPane.PLAIN_MESSAGE, null, null, "Joueur 1");

        if (name == null) {
            return null;
        }

        // remove white spaces and limit to 10 characters
        name = name.replaceAll("\\s", "");
        if (name.length() > 10) {
            name = name.substring(0, 10);
        }

        return name;
    }

    /**
     * Load an achievement (scenario for a user) in the interface
     *
     * @param achievement
     */
    public void loadAchievement(Achievement achievement) {
        // fill components
        this.missionLabel.setText(achievement.getScenario().getName());
        this.testTextArea.setText(TEST_PREFIX + achievement.getScenario().getTest());
        this.testTextArea.setCaretPosition(0);
        this.storyTextArea.setText(STORY_PREFIX + achievement.getScenario().getStory());
        this.storyTextArea.setCaretPosition(0);
        this.triesLabel.setText(String.valueOf(achievement.getRemainingTries()));
        this.chronoLabel.setText(String.valueOf(achievement.getChrono()));
        this.codeTextArea.setText(this.startingCode);
        this.saveButton.setEnabled(true);

        // set start mode
        this.started = false;
        this.saveButton.setText("Commencer");
        this.tryButton.setEnabled(false);
        this.luckyButton.setEnabled(false);
        this.codeTextArea.setEditable(false);
    }

    /**
     * Display an unsuccessful result of a try (error + output)
     *
     * @param result
     */
    public void displayResult(CheckResult result, int remainingTries) {
        StringBuilder str = new StringBuilder("");
        str.append("ERREUR ").append(result.getError().getMessage());

        if (!result.getOutput().isEmpty()) {
            str.append("\n\nSORTIE:\n");
            str.append(result.getOutput());
        }

        this.triesLabel.setText(String.valueOf(remainingTries));
        JOptionPane.showMessageDialog(this, str.toString());
    }

    /**
     * Display the end of a scenario
     *
     * @param achievement
     */
    public void displayEndScenario(Achievement achievement) {
        this.stopChronometer();

        if (achievement.getScore() > 0) {
            StringBuilder str = new StringBuilder("");
            str.append("Félicitations Capitaine !\n\nVous avez obtenu un score de ").append(achievement.getScore());
            str.append("\n\n");
            str.append("Votre chrono: ").append(achievement.getChrono());
            str.append("\n\n");
            str.append("Nombre d'essais restants: ").append(achievement.getRemainingTries());
            if (achievement.isLastTryLucky()) {
                str.append("\n\n");
                str.append("BONUS: Votre dernier essai était chanceux !");
            }

            JOptionPane.showMessageDialog(this, str.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Dommage ... vous n'avez rapporté aucun point pour cette mission.");
        }
    }

    /**
     * Display the end of a game
     *
     * @param scoreFinal
     */
    public void displayEndGame(int scoreFinal) {
        String msg = "Vous avez terminé le jeu !\nVotre score final est de " + scoreFinal;
        msg += "\n\nCrédit: Médéric HURIER";

        this.codeTextArea.setEditable(false);
        this.tryButton.setEnabled(false);
        this.luckyButton.setEnabled(false);
        this.saveButton.setEnabled(false);
        this.missionLabel.setText("GAME OVER");

        JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * Start the chronometer when the user press start
     */
    private void startChronometer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new ChronoTask(this.chronoLabel), 0, 1000);
    }

    /**
     * Stop the chronometer
     */
    private void stopChronometer() {
        this.timer.cancel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        captainLabel = new javax.swing.JLabel();
        missionLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        tryButton = new javax.swing.JButton();
        luckyButton = new javax.swing.JButton();
        triesLabel = new javax.swing.JLabel();
        chronoLabel = new javax.swing.JLabel();
        lastingTriesLabel = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 40), new java.awt.Dimension(0, 40), new java.awt.Dimension(32767, 40));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 40), new java.awt.Dimension(0, 40), new java.awt.Dimension(32767, 40));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30), new java.awt.Dimension(32767, 30));
        jScrollPane1 = new javax.swing.JScrollPane();
        codeTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        testTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        storyTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Captain Duke - Mission en cours");
        setPreferredSize(new java.awt.Dimension(800, 650));
        setResizable(false);
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0, 25, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        getContentPane().setLayout(layout);

        captainLabel.setFont(new java.awt.Font("Ubuntu", 1, 26)); // NOI18N
        captainLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        captainLabel.setText("Capitain Duke");
        captainLabel.setPreferredSize(new java.awt.Dimension(250, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 0;
        getContentPane().add(captainLabel, gridBagConstraints);

        missionLabel.setFont(new java.awt.Font("Ubuntu", 1, 20)); // NOI18N
        missionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        missionLabel.setText("Mission: la mission");
        missionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        missionLabel.setPreferredSize(new java.awt.Dimension(300, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 2;
        getContentPane().add(missionLabel, gridBagConstraints);

        saveButton.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        saveButton.setText("Sauvegarder");
        saveButton.setPreferredSize(new java.awt.Dimension(150, 40));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 24;
        getContentPane().add(saveButton, gridBagConstraints);

        quitButton.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        quitButton.setText("Quitter");
        quitButton.setPreferredSize(new java.awt.Dimension(150, 40));
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 28;
        getContentPane().add(quitButton, gridBagConstraints);

        tryButton.setFont(new java.awt.Font("Ubuntu", 0, 20)); // NOI18N
        tryButton.setText("Faire un essai");
        tryButton.setPreferredSize(new java.awt.Dimension(190, 45));
        tryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tryButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 8;
        getContentPane().add(tryButton, gridBagConstraints);

        luckyButton.setFont(new java.awt.Font("Ubuntu", 0, 20)); // NOI18N
        luckyButton.setText("J'ai de la chance");
        luckyButton.setPreferredSize(new java.awt.Dimension(190, 45));
        luckyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                luckyButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 10;
        getContentPane().add(luckyButton, gridBagConstraints);

        triesLabel.setFont(new java.awt.Font("Ubuntu", 0, 22)); // NOI18N
        triesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        triesLabel.setText("...");
        triesLabel.setPreferredSize(new java.awt.Dimension(150, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 16;
        getContentPane().add(triesLabel, gridBagConstraints);

        chronoLabel.setFont(new java.awt.Font("Ubuntu", 0, 28)); // NOI18N
        chronoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chronoLabel.setText("0");
        chronoLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chronoLabel.setPreferredSize(new java.awt.Dimension(150, 33));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 20;
        getContentPane().add(chronoLabel, gridBagConstraints);

        lastingTriesLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        lastingTriesLabel.setText("Essais restants:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 14;
        getContentPane().add(lastingTriesLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        getContentPane().add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 22;
        getContentPane().add(filler2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 22;
        getContentPane().add(filler3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        getContentPane().add(filler4, gridBagConstraints);

        codeTextArea.setColumns(35);
        codeTextArea.setRows(7);
        codeTextArea.setTabSize(2);
        codeTextArea.setText("// Votre code\n\n// exemple:\n\npublic int repartir(int... poids) {\n\t// à vous de jouer\n}");
        jScrollPane1.setViewportView(codeTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 27;
        gridBagConstraints.gridheight = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        testTextArea.setEditable(false);
        testTextArea.setColumns(35);
        testTextArea.setRows(5);
        testTextArea.setTabSize(2);
        jScrollPane2.setViewportView(testTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 27;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jScrollPane2, gridBagConstraints);

        storyTextArea.setEditable(false);
        storyTextArea.setColumns(35);
        storyTextArea.setLineWrap(true);
        storyTextArea.setRows(5);
        storyTextArea.setTabSize(2);
        storyTextArea.setWrapStyleWord(true);
        jScrollPane3.setViewportView(storyTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 27;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jScrollPane3, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tryButtonActionPerformed
        // security
        if (!this.started) {
            return;
        }

        String code = this.codeTextArea.getText();
        int chrono = Integer.valueOf(this.chronoLabel.getText());
        GameController.getInstance().makeTry(code, chrono, false);
    }//GEN-LAST:event_tryButtonActionPerformed

    private void luckyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_luckyButtonActionPerformed
        // security
        if (!this.started) {
            return;
        }

        String code = this.codeTextArea.getText();
        int chrono = Integer.valueOf(this.chronoLabel.getText());
        GameController.getInstance().makeTry(code, chrono, true);
    }//GEN-LAST:event_luckyButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (this.started) {     // game has already started (save button)
            int chrono = Integer.valueOf(this.chronoLabel.getText());

            GameController.getInstance().saveGame(chrono);
        } else {                // game hasn't started yet (start button)
            this.started = true;
            this.saveButton.setText("Sauvegarder");
            this.tryButton.setEnabled(true);
            this.luckyButton.setEnabled(true);
            this.codeTextArea.setEditable(true);
            this.startChronometer();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitButtonActionPerformed
        int n = JOptionPane.showConfirmDialog(
                this,
                "Voulez vous sauvegarder la partie avant de quitter ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (n == 0) {   // user want to save
            int chrono = Integer.valueOf(this.chronoLabel.getText());

            GameController.getInstance().saveGame(chrono);
        }

        if (n != -1) {  // not cancelled
            LauncherController.getInstance().quit();
        }
    }//GEN-LAST:event_quitButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel captainLabel;
    private javax.swing.JLabel chronoLabel;
    private javax.swing.JTextArea codeTextArea;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lastingTriesLabel;
    private javax.swing.JButton luckyButton;
    private javax.swing.JLabel missionLabel;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextArea storyTextArea;
    private javax.swing.JTextArea testTextArea;
    private javax.swing.JLabel triesLabel;
    private javax.swing.JButton tryButton;
    // End of variables declaration//GEN-END:variables
}
