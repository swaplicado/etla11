/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.form;

import etla.mod.SModConsts;
import etla.mod.etl.db.SDbExtraCharge;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldBoolean;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Sergio Flores
 */
public class SFormExtraCharge extends SBeanForm implements ActionListener, ItemListener {
    
    private SDbExtraCharge moRegistry;
    
    /**
     * Creates new form SFormExtraCharge
     * @param client
     * @param title
     */
    public SFormExtraCharge(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.A_CHARGE, SLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel6 = new javax.swing.JPanel();
        jlChargePercentage = new javax.swing.JLabel();
        moDecChargePercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlChargePercentageHint = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jlDesItemId = new javax.swing.JLabel();
        moIntDesItemId = new sa.lib.gui.bean.SBeanFieldInteger();
        jbEditDesItemId = new javax.swing.JButton();
        jlDesItemIdHint = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jlDesUnitOfMeasureId = new javax.swing.JLabel();
        moIntDesUnitOfMeasureId = new sa.lib.gui.bean.SBeanFieldInteger();
        jbEditDesUnitOfMeasureId = new javax.swing.JButton();
        jlDesUnitOfMeasureIdHint = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        moBoolActive = new sa.lib.gui.bean.SBeanFieldBoolean();
        jlActiveHint = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlCode);
        jPanel3.add(moTextCode);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlName);

        moTextName.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel4.add(moTextName);

        jPanel2.add(jPanel4);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlChargePercentage.setText("% cargo extra:*");
        jlChargePercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlChargePercentage);
        jPanel6.add(moDecChargePercentage);

        jlChargePercentageHint.setForeground(java.awt.Color.gray);
        jlChargePercentageHint.setText("(% sobre el subtotal de la factura.)");
        jlChargePercentageHint.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(jlChargePercentageHint);

        jPanel2.add(jPanel6);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDesItemId.setText("ID ítem:*");
        jlDesItemId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDesItemId);
        jPanel12.add(moIntDesItemId);

        jbEditDesItemId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_edit.gif"))); // NOI18N
        jbEditDesItemId.setToolTipText("Modificar");
        jbEditDesItemId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbEditDesItemId);

        jlDesItemIdHint.setForeground(java.awt.Color.gray);
        jlDesItemIdHint.setText("(Primary Key SIIE)");
        jlDesItemIdHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDesItemIdHint);

        jPanel2.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDesUnitOfMeasureId.setText("ID unidad:*");
        jlDesUnitOfMeasureId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlDesUnitOfMeasureId);
        jPanel13.add(moIntDesUnitOfMeasureId);

        jbEditDesUnitOfMeasureId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_edit.gif"))); // NOI18N
        jbEditDesUnitOfMeasureId.setToolTipText("Modificar");
        jbEditDesUnitOfMeasureId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel13.add(jbEditDesUnitOfMeasureId);

        jlDesUnitOfMeasureIdHint.setForeground(java.awt.Color.gray);
        jlDesUnitOfMeasureIdHint.setText("(Primary Key SIIE)");
        jlDesUnitOfMeasureIdHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlDesUnitOfMeasureIdHint);

        jPanel2.add(jPanel13);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolActive.setText("El cargo extra está activado");
        moBoolActive.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel7.add(moBoolActive);

        jlActiveHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlActiveHint.setText("<active status hint>");
        jlActiveHint.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel7.add(jlActiveHint);

        jPanel2.add(jPanel7);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout(5, 0));
        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbEditDesItemId;
    private javax.swing.JButton jbEditDesUnitOfMeasureId;
    private javax.swing.JLabel jlActiveHint;
    private javax.swing.JLabel jlChargePercentage;
    private javax.swing.JLabel jlChargePercentageHint;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlDesItemId;
    private javax.swing.JLabel jlDesItemIdHint;
    private javax.swing.JLabel jlDesUnitOfMeasureId;
    private javax.swing.JLabel jlDesUnitOfMeasureIdHint;
    private javax.swing.JLabel jlName;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolActive;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecChargePercentage;
    private sa.lib.gui.bean.SBeanFieldInteger moIntDesItemId;
    private sa.lib.gui.bean.SBeanFieldInteger moIntDesUnitOfMeasureId;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);
        
        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode), 35);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName), 130);
        moDecChargePercentage.setDecimalSettings(SGuiUtils.getLabelName(jlChargePercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        moIntDesItemId.setIntegerSettings(SGuiUtils.getLabelName(jlDesItemId), SGuiConsts.GUI_TYPE_INT_RAW, true);
        moIntDesUnitOfMeasureId.setIntegerSettings(SGuiUtils.getLabelName(jlDesUnitOfMeasureId), SGuiConsts.GUI_TYPE_INT_RAW, true);
        moBoolActive.setBooleanSettings(moBoolActive.getText(), true);
        
        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moDecChargePercentage);
        moFields.addField(moIntDesItemId);
        moFields.addField(moIntDesUnitOfMeasureId);
        moFields.addField(moBoolActive);
        
        moFields.setFormButton(jbSave);
    }

    private void enableEditDesItemId(boolean enable) {
        moIntDesItemId.setEditable(enable);
        jbEditDesItemId.setEnabled(!enable);
    }
    
    private void actionPerformedEditDesItemId() {
        enableEditDesItemId(true);
        moIntDesItemId.requestFocus();
    }
    
    private void enableEditDesUnitOfMeasureId(boolean enable) {
        moIntDesUnitOfMeasureId.setEditable(enable);
        jbEditDesUnitOfMeasureId.setEnabled(!enable);
    }
    
    private void actionPerformedEditDesUnitOfMeasureId() {
        enableEditDesUnitOfMeasureId(true);
        moIntDesUnitOfMeasureId.requestFocus();
    }
    
    private void itemStateChangedActive() {
        if (moBoolActive.isSelected()) {
            jlActiveHint.setText("(Este cargo extra se agregará a las facturas de remisiones.)");
        }
        else {
            jlActiveHint.setText("(Este cargo extra se NO agregará a las facturas de remisiones.)");
        }
    }
    
    /*
     * Public methods
     */
    
    /*
     * Overriden methods
     */
    
    @Override
    public void addAllListeners() {
        jbEditDesItemId.addActionListener(this);
        jbEditDesUnitOfMeasureId.addActionListener(this);
        moBoolActive.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbEditDesItemId.removeActionListener(this);
        jbEditDesUnitOfMeasureId.removeActionListener(this);
        moBoolActive.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        // insert code here
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbExtraCharge) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.setActive(true);
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moTextCode.setValue(moRegistry.getCode());
        moTextName.setValue(moRegistry.getName());
        moDecChargePercentage.setValue(moRegistry.getChargePercentage());
        moIntDesItemId.setValue(moRegistry.getDesItemId());
        moIntDesUnitOfMeasureId.setValue(moRegistry.getDesUnitOfMeasureId());
        moBoolActive.setValue(moRegistry.isActive());
        itemStateChangedActive();

        setFormEditable(true);
        
        if (moRegistry.isRegistryNew()) {
            enableEditDesItemId(true);
            enableEditDesUnitOfMeasureId(true);
        }
        else {
            enableEditDesItemId(false);
            enableEditDesUnitOfMeasureId(false);
        }
        
        addAllListeners();
    }

    @Override
    public SDbExtraCharge getRegistry() throws Exception {
        SDbExtraCharge registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setDesItemId(moIntDesItemId.getValue());
        registry.setDesUnitOfMeasureId(moIntDesUnitOfMeasureId.getValue());
        registry.setChargePercentage(moDecChargePercentage.getValue());
        registry.setCode(moTextCode.getValue());
        registry.setName(moTextName.getValue());
        registry.setActive(moBoolActive.isSelected());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbEditDesItemId) {
                actionPerformedEditDesItemId();
            }
            else if (button == jbEditDesUnitOfMeasureId) {
                actionPerformedEditDesUnitOfMeasureId();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldBoolean) {
            SBeanFieldBoolean field = (SBeanFieldBoolean) e.getSource();
            if (field == moBoolActive) {
                itemStateChangedActive();
            }
        }
    }
}