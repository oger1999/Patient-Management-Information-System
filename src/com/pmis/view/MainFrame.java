package com.pmis.view;

import com.pmis.controller.GuardianController;
import com.pmis.controller.PatientController;
import com.pmis.model.Guardian;
import com.pmis.model.Patient;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

/**
 * PMISMainFrame - Main GUI for the Patient Management Information System.
 *
 * View layer only. All "CONTROLLER STUB" comments mark where controller / DAO
 * calls will be wired in.
 */
public class MainFrame extends JFrame {

    // ── Patient form fields ──────────────────────────────────────────────────
    private JTextField tfPatientID, tfFullName, tfAge, tfDiagnosis, tfFee;
    private JComboBox<String> cbGender;

    // ── Guardian form fields ─────────────────────────────────────────────────
    private JTextField tfGuardianID, tfGuardFullName, tfPhone,
            tfRelationship, tfAddress, tfLinkedPatientID;

    // ── Tables ───────────────────────────────────────────────────────────────
    private JTable patientTable;
    private DefaultTableModel patientTableModel;

    private JTable guardianTable;
    private DefaultTableModel guardianTableModel;

    // ── Search fields ─────────────────────────────────────────────────────────
    private JTextField tfPatientSearch, tfGuardianSearch;

    public MainFrame() {
        setTitle("Patient Management Information System (PMIS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", buildPatientPanel());
        tabs.addTab("Guardians", buildGuardianPanel());

        add(tabs);
        setVisible(true);
        handleRefreshPatients();
        handleRefreshGuardians();
    }

    private JPanel buildPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(buildPatientForm(), BorderLayout.WEST);
        panel.add(buildPatientTable(), BorderLayout.CENTER);
        return panel;
    }

    // Patient form
    private JPanel buildPatientForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Patient Details"));
        panel.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;

        tfPatientID = new JTextField(15);
        tfFullName = new JTextField(15);
        tfAge = new JTextField(15);
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        tfDiagnosis = new JTextField(15);
        tfFee = new JTextField(15);

        int row = 0;
        addRow(panel, gc, row++, "Patient ID (PAT-####):", tfPatientID);
        addRow(panel, gc, row++, "Full Name:", tfFullName);
        addRow(panel, gc, row++, "Age:", tfAge);
        addRow(panel, gc, row++, "Gender:", cbGender);
        addRow(panel, gc, row++, "Diagnosis:", tfDiagnosis);
        addRow(panel, gc, row++, "Consultation Fee (Rwf):", tfFee);

        // Registration date is set automatically — show as read-only label
        JLabel lblRegDate = new JLabel("Auto: " + LocalDate.now());
        addRow(panel, gc, row++, "Registration Date:", lblRegDate);

        // Buttons
        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        JPanel btnRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        btnRow1.add(btnSave);
        btnRow1.add(btnClear);

        JPanel btnRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        btnRow2.add(btnUpdate);
        btnRow2.add(btnDelete);

        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        panel.add(btnRow1, gc);
        gc.gridy = ++row;
        panel.add(btnRow2, gc);
        gc.gridwidth = 1;

        btnSave.addActionListener(e -> {
            if (!validatePatientForm()) {
                return;
            }
            Patient p = collectPatientFromForm();

            PatientController pController = new PatientController();
            pController.addPatient(p);
            addPatientToTable(p);
            JOptionPane.showMessageDialog(this,
                    "Patient saved successfully.\nNet fee after discount: "
                    + String.format("%.0f Rwf", netFee(p.getAge(), p.getConsultationFee())),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearPatientForm();
        });

        btnUpdate.addActionListener(e -> {
            if (!validatePatientForm()) {
                return;
            }
            Patient p = collectPatientFromForm();

            PatientController pController = new PatientController();
            pController.updatePatient(p.getPatientID(), p);
            
            updateSelectedPatientRow(p);
            JOptionPane.showMessageDialog(this, "Patient updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearPatientForm();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }
            String id = (String) patientTableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete patient " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            PatientController pController = new PatientController();
            pController.deletePatient(id);
            patientTableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Patient deleted.",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);
            clearPatientForm();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
        });

        btnClear.addActionListener(e -> {
            clearPatientForm();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
        });

        // Clicking a table row fills the form and switches to edit mode
        // (listener is set up after patientTable is created — see buildPatientTable)
        // We store the buttons so the selection listener can toggle them
        JButton[] editButtons = {btnSave, btnUpdate, btnDelete};
        // passed via instance field trick: store as array reference in the table listener
        buildPatientTableAndAttachListener(editButtons);

        return panel;
    }

    // ── Patient table + search ────────────────────────────────────────────────
    /**
     * Builds the patient table, attaches the row-selection listener that fills
     * the form and toggles Save/Update/Delete. Must be called from
     * buildPatientForm so it has access to the button array.
     */
    private JPanel buildPatientTableAndAttachListener(JButton[] editButtons) {
        patientEditButtons = editButtons;
        return null; // not used directly; panel added in buildPatientPanel()
    }

    // Stored so buildPatientTable() can attach the selection listener
    private JButton[] patientEditButtons;

    private JPanel buildPatientTable() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Patient Summary"));

        tfPatientSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        searchRow.add(new JLabel("Search (ID or Name):"));
        searchRow.add(tfPatientSearch);
        searchRow.add(btnSearch);
        searchRow.add(btnRefresh);
        panel.add(searchRow, BorderLayout.NORTH);

        String[] columns = {
            "Patient ID", "Full Name", "Age", "Gender",
            "Diagnosis", "Fee (Rwf)", "Discount", "Net Fee (Rwf)", "Reg. Date"
        };
        patientTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        patientTable = new JTable(patientTableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setAutoCreateRowSorter(true);

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int row = patientTable.getSelectedRow();
            if (row < 0) {
                return;
            }
            fillPatientForm(row);
            if (patientEditButtons != null) {
                patientEditButtons[0].setEnabled(false); // Save
                patientEditButtons[1].setEnabled(true);  // Update
                patientEditButtons[2].setEnabled(true);  // Delete
            }
        });

        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String q = tfPatientSearch.getText().trim();
            if (q.isEmpty()) {
                handleRefreshPatients();
                return;
            }

            PatientController pController = new PatientController();
            List<Patient> results = pController.searchByName(q);
            refreshPatientTable(results);
        });

        btnRefresh.addActionListener(e -> handleRefreshPatients());

        return panel;
    }

    // ── Patient helpers ───────────────────────────────────────────────────────
    private boolean validatePatientForm() {
        String id = tfPatientID.getText().trim();
        String name = tfFullName.getText().trim();
        String ageS = tfAge.getText().trim();
        String diag = tfDiagnosis.getText().trim();
        String feeS = tfFee.getText().trim();

        if (!id.matches("PAT-\\d{4}")) {
            showError("Patient ID must follow the format PAT-#### (e.g. PAT-0001).");
            return false;
        }
        if (name.length() < 5) {
            showError("Full Name must be at least 5 characters.");
            return false;
        }
        try {
            int age = Integer.parseInt(ageS);
            if (age < 0 || age > 120) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showError("Age must be a whole number between 0 and 120.");
            return false;
        }
        if (diag.length() < 5) {
            showError("Diagnosis must be at least 5 characters.");
            return false;
        }
        try {
            double fee = Double.parseDouble(feeS);
            if (fee < 5000 || fee > 50000) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showError("Consultation Fee must be between 5,000 and 50,000 Rwf.");
            return false;
        }
        return true;
    }

    private Patient collectPatientFromForm() {
        return new Patient(
                tfPatientID.getText().trim(),
                tfFullName.getText().trim(),
                Integer.parseInt(tfAge.getText().trim()),
                (String) cbGender.getSelectedItem(),
                tfDiagnosis.getText().trim(),
                Double.parseDouble(tfFee.getText().trim()),
                LocalDate.now()
        );
    }

    private void addPatientToTable(Patient p) {
        patientTableModel.addRow(new Object[]{
            p.getPatientID(),
            p.getFullName(),
            p.getAge(),
            p.getGender(),
            p.getDiagnosis(),
            String.format("%.0f", p.getConsultationFee()),
            discountLabel(p.getAge()),
            String.format("%.0f", netFee(p.getAge(), p.getConsultationFee())),
            p.getRegistrationDate()
        });
    }

    private void updateSelectedPatientRow(Patient p) {
        int row = patientTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        patientTableModel.setValueAt(p.getPatientID(), row, 0);
        patientTableModel.setValueAt(p.getFullName(), row, 1);
        patientTableModel.setValueAt(p.getAge(), row, 2);
        patientTableModel.setValueAt(p.getGender(), row, 3);
        patientTableModel.setValueAt(p.getDiagnosis(), row, 4);
        patientTableModel.setValueAt(String.format("%.0f", p.getConsultationFee()), row, 5);
        patientTableModel.setValueAt(discountLabel(p.getAge()), row, 6);
        patientTableModel.setValueAt(String.format("%.0f", netFee(p.getAge(), p.getConsultationFee())), row, 7);
        patientTableModel.setValueAt(p.getRegistrationDate(), row, 8);
    }

    private void fillPatientForm(int row) {
        tfPatientID.setText((String) patientTableModel.getValueAt(row, 0));
        tfFullName.setText((String) patientTableModel.getValueAt(row, 1));
        tfAge.setText(patientTableModel.getValueAt(row, 2).toString());
        cbGender.setSelectedItem(patientTableModel.getValueAt(row, 3));
        tfDiagnosis.setText((String) patientTableModel.getValueAt(row, 4));
        tfFee.setText(patientTableModel.getValueAt(row, 5).toString());
    }

    private void clearPatientForm() {
        tfPatientID.setText("");
        tfFullName.setText("");
        tfAge.setText("");
        cbGender.setSelectedIndex(0);
        tfDiagnosis.setText("");
        tfFee.setText("");
        patientTable.clearSelection();
    }

    private void handleRefreshPatients() {
        PatientController pController = new PatientController();
        List<Patient> all = pController.getAllPatients();
        refreshPatientTable(all);
    }

    /**
     * Call this with real data once the controller is wired in.
     */
    public void refreshPatientTable(java.util.List<Patient> patients) {
        patientTableModel.setRowCount(0);
        for (Patient p : patients) {
            addPatientToTable(p);
        }
    }

    private String discountLabel(int age) {
        if (age < 12) {
            return "50%";
        }
        if (age > 60) {
            return "30%";
        }
        return "0%";
    }

    private double netFee(int age, double fee) {
        if (age < 12) {
            return fee * 0.50;
        }
        if (age > 60) {
            return fee * 0.70;
        }
        return fee;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GUARDIAN PANEL
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildGuardianPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(buildGuardianForm(), BorderLayout.WEST);
        panel.add(buildGuardianTable(), BorderLayout.CENTER);
        return panel;
    }

    // ── Guardian form ─────────────────────────────────────────────────────────
    private JPanel buildGuardianForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Guardian Details"));
        panel.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;

        tfGuardianID = new JTextField(15);
        tfGuardFullName = new JTextField(15);
        tfPhone = new JTextField(15);
        tfRelationship = new JTextField(15);
        tfAddress = new JTextField(15);
        tfLinkedPatientID = new JTextField(15);

        int row = 0;
        addRow(panel, gc, row++, "Guardian ID:", tfGuardianID);
        addRow(panel, gc, row++, "Full Name:", tfGuardFullName);
        addRow(panel, gc, row++, "Phone:", tfPhone);
        addRow(panel, gc, row++, "Relationship:", tfRelationship);
        addRow(panel, gc, row++, "Address:", tfAddress);
        addRow(panel, gc, row++, "Linked Patient (PAT-####):", tfLinkedPatientID);

        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        JPanel btnRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        btnRow1.add(btnSave);
        btnRow1.add(btnClear);

        JPanel btnRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        btnRow2.add(btnUpdate);
        btnRow2.add(btnDelete);

        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        panel.add(btnRow1, gc);
        gc.gridy = ++row;
        panel.add(btnRow2, gc);
        gc.gridwidth = 1;

        JButton[] editButtons = {btnSave, btnUpdate, btnDelete};
        guardianEditButtons = editButtons;

        // ── Wire buttons ─────────────────────────────────────────────────────
        btnSave.addActionListener(e -> {
            if (!validateGuardianForm()) {
                return;
            }
            Guardian g = collectGuardianFromForm();

            GuardianController gController = new GuardianController();
            gController.addGuardian(g);
            guardianTableModel.addRow(new Object[]{
                g.getGuardianID(), g.getFullName(), g.getPhone(),
                g.getRelationship(), g.getAddress(), g.getPatientID()
            });
            JOptionPane.showMessageDialog(this, "Guardian saved successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearGuardianForm();
        });

        btnUpdate.addActionListener(e -> {
            if (!validateGuardianForm()) {
                return;
            }
            Guardian g = collectGuardianFromForm();

            GuardianController gController = new GuardianController();
            gController.updateGuardian(g.getGuardianID(), g);
            int selRow = guardianTable.getSelectedRow();
            if (selRow >= 0) {
                guardianTableModel.setValueAt(g.getGuardianID(), selRow, 0);
                guardianTableModel.setValueAt(g.getFullName(), selRow, 1);
                guardianTableModel.setValueAt(g.getPhone(), selRow, 2);
                guardianTableModel.setValueAt(g.getRelationship(), selRow, 3);
                guardianTableModel.setValueAt(g.getAddress(), selRow, 4);
                guardianTableModel.setValueAt(g.getPatientID(), selRow, 5);
            }
            JOptionPane.showMessageDialog(this, "Guardian updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearGuardianForm();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
        });

        btnDelete.addActionListener(e -> {
            int selRow = guardianTable.getSelectedRow();
            if (selRow < 0) {
                return;
            }
            String id = (String) guardianTableModel.getValueAt(selRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete guardian " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            GuardianController gController = new GuardianController();
            gController.deleteGuardian(id);
            guardianTableModel.removeRow(selRow);
            JOptionPane.showMessageDialog(this, "Guardian deleted.",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);
            clearGuardianForm();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
        });

        btnClear.addActionListener(e -> {
            clearGuardianForm();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
        });

        return panel;
    }

    private JButton[] guardianEditButtons;

    // ── Guardian table + search ───────────────────────────────────────────────
    private JPanel buildGuardianTable() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Guardian Summary"));

        tfGuardianSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        searchRow.add(new JLabel("Search (ID or Name):"));
        searchRow.add(tfGuardianSearch);
        searchRow.add(btnSearch);
        searchRow.add(btnRefresh);
        panel.add(searchRow, BorderLayout.NORTH);

        String[] columns = {
            "Guardian ID", "Full Name", "Phone",
            "Relationship", "Address", "Patient ID"
        };
        guardianTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        guardianTable = new JTable(guardianTableModel);
        guardianTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        guardianTable.setAutoCreateRowSorter(true);

        guardianTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int row = guardianTable.getSelectedRow();
            if (row < 0) {
                return;
            }
            fillGuardianForm(row);
            if (guardianEditButtons != null) {
                guardianEditButtons[0].setEnabled(false); // Save
                guardianEditButtons[1].setEnabled(true);  // Update
                guardianEditButtons[2].setEnabled(true);  // Delete
            }
        });

        panel.add(new JScrollPane(guardianTable), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String q = tfGuardianSearch.getText().trim();
            if (q.isEmpty()) {
                handleRefreshGuardians();
                return;
            }

            GuardianController gController = new GuardianController();
            List<Guardian> results = gController.searchByName(q);
            refreshGuardianTable(results);
        });

        btnRefresh.addActionListener(e -> handleRefreshGuardians());

        return panel;
    }

    // ── Guardian helpers ──────────────────────────────────────────────────────
    private boolean validateGuardianForm() {
        String id = tfGuardianID.getText().trim();
        String name = tfGuardFullName.getText().trim();
        String ph = tfPhone.getText().trim();
        String rel = tfRelationship.getText().trim();
        String pid = tfLinkedPatientID.getText().trim();

        if (id.isEmpty()) {
            showError("Guardian ID is required.");
            return false;
        }
        if (name.length() < 5) {
            showError("Full Name must be at least 5 characters.");
            return false;
        }
        if (ph.isEmpty()) {
            showError("Phone number is required.");
            return false;
        }
        if (rel.isEmpty()) {
            showError("Relationship is required.");
            return false;
        }
        if (!pid.matches("PAT-\\d{4}")) {
            showError("Linked Patient ID must follow the format PAT-#### (e.g. PAT-0001).");
            return false;
        }
        return true;
    }

    private Guardian collectGuardianFromForm() {
        return new Guardian(
                tfGuardianID.getText().trim(),
                tfGuardFullName.getText().trim(),
                tfPhone.getText().trim(),
                tfRelationship.getText().trim(),
                tfAddress.getText().trim(),
                tfLinkedPatientID.getText().trim()
        );
    }

    private void fillGuardianForm(int row) {
        tfGuardianID.setText((String) guardianTableModel.getValueAt(row, 0));
        tfGuardFullName.setText((String) guardianTableModel.getValueAt(row, 1));
        tfPhone.setText((String) guardianTableModel.getValueAt(row, 2));
        tfRelationship.setText((String) guardianTableModel.getValueAt(row, 3));
        tfAddress.setText((String) guardianTableModel.getValueAt(row, 4));
        tfLinkedPatientID.setText((String) guardianTableModel.getValueAt(row, 5));
    }

    private void clearGuardianForm() {
        tfGuardianID.setText("");
        tfGuardFullName.setText("");
        tfPhone.setText("");
        tfRelationship.setText("");
        tfAddress.setText("");
        tfLinkedPatientID.setText("");
        guardianTable.clearSelection();
    }

    private void handleRefreshGuardians() {
        GuardianController gController = new GuardianController();
        List<Guardian> all = gController.getAllGuardians();
        refreshGuardianTable(all);
    }

    /**
     * Call this with real data once the controller is wired in.
     */
    public void refreshGuardianTable(java.util.List<Guardian> guardians) {
        guardianTableModel.setRowCount(0);
        for (Guardian g : guardians) {
            guardianTableModel.addRow(new Object[]{
                g.getGuardianID(), g.getFullName(), g.getPhone(),
                g.getRelationship(), g.getAddress(), g.getPatientID()
            });
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  SHARED HELPERS
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Adds a label + component pair as a row inside a GridBagLayout panel.
     */
    private void addRow(JPanel panel, GridBagConstraints gc,
            int row, String labelText, Component field) {
        gc.gridy = row;
        gc.gridx = 0;
        gc.weightx = 0;
        panel.add(new JLabel(labelText), gc);
        gc.gridx = 1;
        gc.weightx = 1.0;
        panel.add(field, gc);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
