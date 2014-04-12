package views;

import models.*;
import java.util.*;
import controllers.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Graphical user interface
 * @author Phongphet
 */
public class GraphicalUserInterface extends javax.swing.JFrame implements UserInterface {
    private ActionController ctrl;
    private LinkedList<Node> listNode;

    public GraphicalUserInterface() {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            Logger.getLogger("sg").log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        //init user interface components
        initComponents();
        this.setTitle("Social Graph");
    }
    
    @Override
    public void setController(ActionController ctrl) {
        this.ctrl = ctrl;
        
        //add node's children into a Linkedlist and add all of them in the combo box
        this.listNode = new LinkedList(this.ctrl.getRoot().getChildren());
        this.addElementsToComboBox(listNode);

        //display the graph in the JTextBox
        displayGraphBox.setText(this.displayAllNode(listNode));
    }

    private String displayAllNode(LinkedList<Node> listNode) {
        String graph = "";

        for (int i = 0; i < listNode.size(); i++) {
            graph += listNode.get(i).getName() + " : \n";
            graph += displayRelationsOfNode(listNode.get(i), "");
            graph += "\n";
        }

        return graph;
    }

    public String displayRelationsOfNode(Node node, String mode) {
        String graph = "";
        LinkedList<Relation> listRelation = new LinkedList(node.getRelations());
        for (int j = 1; j < listRelation.size(); j++) {
            Relation r = listRelation.get(j);
            if(mode.equals("html")){
                graph += "&nbsp;&nbsp;&nbsp;&nbsp;" + r.getLink().getName();
                graph += "&nbsp;&nbsp;" + r.getLink().getDirection().name();
                graph += "&nbsp;&nbsp;" + r.getTarget().getName();
                graph += "<br />";
            }else{
                graph += "     " + r.getLink().getName();
                graph += " " + r.getLink().getDirection().name();
                graph += " " + r.getTarget().getName();
                graph += "\n";
            }
        }
        if(mode.equals("html")){
            graph += "<br />";
        }else{
            graph += "\n";
        }

        return graph;
    }

    private String displayResultQuery(LinkedList<Node> listNode) {
        String graph = "";
        for (int i = 0; i < listNode.size(); i++) {
            graph += " ," + listNode.get(i).getName();
        }
        
        if(graph.length() >2) graph = graph.substring(2);
        
        return graph;
    }

    private void addElementsToComboBox(LinkedList<Node> listNode) {
        nodeComboBox.addItem("");
        for (int i = 0; i < listNode.size(); i++) {
            nodeComboBox.addItem(listNode.get(i));
        }
    }

    public void highlightSelectedElement(LinkedList<Node> resultOfQuery) {
        displayGraphBox.setContentType("text/html");
        String graph = "";
        for (int i = 0; i < listNode.size(); i++) {
            if (resultOfQuery.contains(listNode.get(i))) {
                graph += "<span style=\"color: red; font-weight: bold; \">" + listNode.get(i).getName() + " : </span><br />";
            } else {
                graph += listNode.get(i).getName() + " : <br />";
            }
            graph += displayRelationsOfNode(listNode.get(i), "html");
        }
        displayGraphBox.setText(graph);
    }

    public TraversingConstraints.Mode returnMode(String mode) {
        TraversingConstraints.Mode typeMode = null;

        if (mode.compareTo("DEPTH") == 0) {
            typeMode = TraversingConstraints.Mode.DEPTH;
        } else if (mode.compareTo("BREADTH") == 0) {
            typeMode = TraversingConstraints.Mode.BREADTH;
        }

        return typeMode;
    }

    public TraversingConstraints.Uniqueness returnUniqueness(String uniqueness) {
        TraversingConstraints.Uniqueness typeUniqueness = null;

        if (uniqueness.compareTo("NO") == 0) {
            typeUniqueness = TraversingConstraints.Uniqueness.NO;
        } else if (uniqueness.compareTo("PARTIAL") == 0) {
            typeUniqueness = TraversingConstraints.Uniqueness.PARTIAL;
        } else if (uniqueness.compareTo("TOTAL") == 0) {
            typeUniqueness = TraversingConstraints.Uniqueness.TOTAL;
        }

        return typeUniqueness;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        requestBox = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultBox = new javax.swing.JTextArea();
        executeButton = new javax.swing.JButton();
        requestLabel = new javax.swing.JLabel();
        resultLabel = new javax.swing.JLabel();
        nodeComboBox = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        displayGraphBox = new javax.swing.JEditorPane();
        preFillButton = new javax.swing.JButton();
        modeLabel = new javax.swing.JLabel();
        modeComboBox = new javax.swing.JComboBox();
        linkUniquenessLabel = new javax.swing.JLabel();
        linkUniqCombo = new javax.swing.JComboBox();
        NodeUniquenessLabel1 = new javax.swing.JLabel();
        NodeUniqCombo = new javax.swing.JComboBox();
        limitLabel = new javax.swing.JLabel();
        limitText = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        radioActivateConstraint = new javax.swing.JRadioButton();
        helpButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        requestBox.setColumns(20);
        requestBox.setRows(5);
        jScrollPane1.setViewportView(requestBox);

        resultBox.setColumns(20);
        resultBox.setRows(5);
        jScrollPane2.setViewportView(resultBox);

        executeButton.setText("Execute");
        executeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeButtonActionPerformed(evt);
            }
        });

        requestLabel.setText("Query");

        resultLabel.setText("Result");

        nodeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeComboBoxActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(displayGraphBox);

        preFillButton.setText("Pre-Fill");
        preFillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preFillButtonActionPerformed(evt);
            }
        });

        modeLabel.setText("Mode");

        modeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DEPTH", "BREADTH" }));
        modeComboBox.setToolTipText("");
        modeComboBox.setEnabled(false);

        linkUniquenessLabel.setText("Node uniqueness");

        linkUniqCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NO", "PARTIAL", "TOTAL" }));
        linkUniqCombo.setEnabled(false);

        NodeUniquenessLabel1.setText("Link uniqueness");

        NodeUniqCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NO", "PARTIAL", "TOTAL" }));
        NodeUniqCombo.setEnabled(false);

        limitLabel.setText("Limit");

        limitText.setText("-1");
        limitText.setEnabled(false);

        radioActivateConstraint.setText("check constrains");
        radioActivateConstraint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioActivateConstraintActionPerformed(evt);
            }
        });

        helpButton.setText("help");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(requestLabel)
                    .addComponent(resultLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(modeLabel)
                        .addGap(2, 2, 2)
                        .addComponent(modeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(linkUniquenessLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(linkUniqCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NodeUniquenessLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NodeUniqCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(limitLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(limitText, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioActivateConstraint)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(preFillButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(executeButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(executeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(preFillButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(radioActivateConstraint))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(modeLabel)
                            .addComponent(modeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(linkUniquenessLabel)
                            .addComponent(linkUniqCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NodeUniquenessLabel1)
                            .addComponent(NodeUniqCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(limitLabel)
                            .addComponent(limitText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(requestLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(resultLabel))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(helpButton))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        requestLabel.getAccessibleContext().setAccessibleName("requestLabel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void executeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeButtonActionPerformed
        // TODO add your handling code here:

        String request = requestBox.getText();
        resultBox.setText("");
        Set<Node> setNode;

        if ("".equals(request)) {
            resultBox.setText("");
            displayGraphBox.setText("");
            
        } else {
            try {
                if (radioActivateConstraint.isSelected()) { TraversingConstraints.Mode mode = returnMode((String) modeComboBox.getSelectedItem());
                    int depth = Integer.parseInt(limitText.getText());
                    TraversingConstraints.Uniqueness linkUniq = returnUniqueness((String) linkUniqCombo.getSelectedItem());
                    TraversingConstraints.Uniqueness nodeUniq = returnUniqueness((String) NodeUniqCombo.getSelectedItem());
                    TraversingConstraints constraints = new TraversingConstraints(mode, depth, linkUniq, nodeUniq);
                    setNode = ctrl.handleQueryWithConstraints(request, constraints);

                } else {
                    setNode = ctrl.handleQuery(request);
                }

                LinkedList<Node> resultOfQuery = new LinkedList(setNode);

                //display result in resultBox
                resultBox.setText(this.displayResultQuery(resultOfQuery));
                highlightSelectedElement(resultOfQuery);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Limit must be a number", "Error", JOptionPane.ERROR_MESSAGE);

            } catch (QueryException | ExecutionException ex) {
                Logger.getLogger("sg").log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_executeButtonActionPerformed

    private void nodeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeComboBoxActionPerformed
        // TODO add your handling code here:

        displayGraphBox.setText("");
        int selectedIndex = nodeComboBox.getSelectedIndex();
        displayGraphBox.setContentType("");
        String textToDisplay = "";
        if (selectedIndex > 0) {
            Node nodeSelected = listNode.get(selectedIndex - 1);
            textToDisplay = this.displayRelationsOfNode(nodeSelected, "");
        }
        displayGraphBox.setText(textToDisplay);
    }//GEN-LAST:event_nodeComboBoxActionPerformed

    private void preFillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preFillButtonActionPerformed
        // TODO add your handling code here:
        this.requestBox.setText("");
        this.requestBox.setText("SELECT in<\nFROM SocialGraph\nWHERE NOT ~Barbara");
    }//GEN-LAST:event_preFillButtonActionPerformed

    private void radioActivateConstraintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioActivateConstraintActionPerformed
        // TODO add your handling code here:
        if (radioActivateConstraint.isSelected()) {
            modeComboBox.setEnabled(true);
            linkUniqCombo.setEnabled(true);
            NodeUniqCombo.setEnabled(true);
            limitText.setEnabled(true);
        } else {
            modeComboBox.setEnabled(false);
            linkUniqCombo.setEnabled(false);
            NodeUniqCombo.setEnabled(false);
            limitText.setEnabled(false);
        }
    }//GEN-LAST:event_radioActivateConstraintActionPerformed


    @Override
    public void run() {
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        // TODO add your handling code here:
        String messageToShow = "Utilisation: socialgraph FICHIER\n" 
                + "Parcours de graphe de réseau social.\n"
                + "\n"
                + "Arguments:\n"
                + "- FICHIER: fichier d'entrée au format CSV\n"
                + "\n"
                + "Syntaxe:\n"
                + "SELECT <link>, <link>\n"
                + "FROM <node>\n"
                + "[WHERE <filter> AND <filter>]\n"
                + "\n"
                + "<link> (Il faut imaginer X au bout de la flèche)\n"
                + "- employe< : lien entrant “Les employées de X”\n"
                + "- author> : lien sortant “Les oeuvres dont X est l’auteur”\n"
                + "- friends : bi-directionnel “les amis de X”\n"
                + "\n"
                + "<node> nom du noeud (ex: Bob, Société Corp …)\n"
                + "\n"
                + "<filter>\n"
                + "- opérator: rien pour la condition normale ou NOT pour inverser la condition\n"
                + "- type: ~ pour nom, @ pour attribut, # pour lien\n"
                + "- clé: sujet du filtre\n"
                + "- valeur: valeur du filtre. Laisser vide pour simplement vérifier l'existence de la clé\n"
                + "- portée: niveau du lien sur lequel s'applique le filtre ou rien pour le dernier lien (ON 1)\n"
                + "- exemple: WHERE #employee_of ON 1 AND ~Bob ON 2 AND NOT @to=21/05/2012\n"
                + "- noeuds avec des liens employées pour le niveau 1, noeuds dont les nom contient Bob pour le niveau 2, lien avec un attribut to=21/05/2012";
        JOptionPane.showMessageDialog(this, messageToShow);
    }//GEN-LAST:event_helpButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox NodeUniqCombo;
    private javax.swing.JLabel NodeUniquenessLabel1;
    private javax.swing.JEditorPane displayGraphBox;
    private javax.swing.JButton executeButton;
    private javax.swing.JButton helpButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel limitLabel;
    private javax.swing.JTextField limitText;
    private javax.swing.JComboBox linkUniqCombo;
    private javax.swing.JLabel linkUniquenessLabel;
    private javax.swing.JComboBox modeComboBox;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JComboBox nodeComboBox;
    private javax.swing.JButton preFillButton;
    private javax.swing.JRadioButton radioActivateConstraint;
    private javax.swing.JTextArea requestBox;
    private javax.swing.JLabel requestLabel;
    private javax.swing.JTextArea resultBox;
    private javax.swing.JLabel resultLabel;
    // End of variables declaration//GEN-END:variables
}
