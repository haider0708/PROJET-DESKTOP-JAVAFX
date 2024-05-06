package controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Patient;
import org.apache.pdfbox.text.PDFTextStripper;
import services.Service;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class View implements Initializable  {
    private Service Service;

    @FXML
    private TableView<Patient> PatientTable;

    @FXML
    private TableColumn<Patient, Integer> idcol;

    @FXML
    private TableColumn<Patient, String> emailcol;

    @FXML
    private TableColumn<Patient, String[]> rolescol;

    @FXML
    private TableColumn<Patient, String> passwordcol;

    @FXML
    private TableColumn<Patient, String> firstnamecol;

    @FXML
    private TableColumn<Patient, String> lastnamecol;

    @FXML
    private TableColumn<Patient, String> sexecol;

    @FXML
    private TableColumn<Patient, Integer> agecol;

    @FXML
    private TableColumn<Patient, String> numbercol;

    @FXML
    private TableColumn<Patient, String> imgpathcol;

    @FXML
    private TableColumn<Patient, String> addresscol;

    @FXML
    private TableColumn<Patient, Boolean> isverifiedcol;

    @FXML
    private TableColumn<Patient, String> resettokencol;

    @FXML
    private TableColumn<Patient, String> editCol;

    @FXML
    private Pane pnlOverview;

    @FXML
    private ImageView ascending;

    @FXML
    private ImageView descending;

    @FXML
    private Label numbersofusers;

    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    @FXML
    void generateChart() {
        Map<String, Long> roleCounts = Service.getRoleCounts(); // Assuming a method to retrieve role counts

        PieChart pieChart = new PieChart();
        pieChart.setTitle("User Roles");

        roleCounts.forEach((role, count) -> {
            PieChart.Data slice = new PieChart.Data(role, count);
            pieChart.getData().add(slice);
        });

        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);

        // Create a new stage
        Stage chartStage = new Stage();
        chartStage.setTitle("User Roles Chart");

        // Create a scene and set the chart as its root node
        Scene scene = new Scene(new Group(pieChart), 600, 400);

        // Set the scene to the stage
        chartStage.setScene(scene);

        // Show the stage
        chartStage.show();
    }
    @FXML
    void ascending(MouseEvent event) {
        sortTable(true);
    }

    @FXML
    void descending(MouseEvent event) {
        sortTable(false);
    }

    private void sortTable(boolean ascending) {
        Comparator<Patient> comparator = Comparator.comparing(Patient::getFirstname);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        List<Patient> sortedPatients = PatientTable.getItems().stream().sorted(comparator).collect(Collectors.toList());
        PatientTable.getItems().clear();
        PatientTable.getItems().addAll(sortedPatients);
    }


    @FXML
    private TextField search;

    @FXML
    void search(KeyEvent event) {
        String searchText = search.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            PatientTable.setItems(patients);
        } else {
            ObservableList<Patient> filteredList = patients.filtered(patient -> patientMatchesSearch(patient, searchText));
            PatientTable.setItems(filteredList);
        }

        // Refresh the table
        PatientTable.refresh();
    }




    private boolean patientMatchesSearch(Patient patient, String searchText) {
        String[] searchWords = searchText.split("\\s+");
        String firstName = patient.getFirstname().toLowerCase();
        String lastName = patient.getLastname().toLowerCase();

        for (String word : searchWords) {
            if (firstName.contains(word) || lastName.contains(word)) {
                return true;
            }
        }

        return false;
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Service = new Service();
        initializeColumns();
        loadData();


    }

    @FXML
    void refresh(MouseEvent event) {
        loadData();
    }



    @FXML
    void getAddView(MouseEvent event) {
        openAddUserView();
    }


    private void initializeColumns() {
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        rolescol.setCellValueFactory(new PropertyValueFactory<>("roles"));
        passwordcol.setCellValueFactory(new PropertyValueFactory<>("password"));
        firstnamecol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnamecol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        sexecol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        agecol.setCellValueFactory(new PropertyValueFactory<>("age"));
        numbercol.setCellValueFactory(new PropertyValueFactory<>("number"));
        imgpathcol.setCellValueFactory(new PropertyValueFactory<>("img_path"));
        addresscol.setCellValueFactory(new PropertyValueFactory<>("address"));
        isverifiedcol.setCellValueFactory(new PropertyValueFactory<>("is_verified"));
        resettokencol.setCellValueFactory(new PropertyValueFactory<>("reset_token"));
        editCol.setCellFactory(createEditCellFactory());
    }

    private Callback<TableColumn<Patient, String>, TableCell<Patient, String>> createEditCellFactory() {
        return (TableColumn<Patient, String> param) -> {
            final TableCell<Patient, String> cell = new TableCell<Patient, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setGraphic(createEditIcons());
                        setText(null);
                    }
                }
            };
            return cell;
        };
    }

    private HBox createEditIcons() {
        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

        deleteIcon.setStyle(
                "-fx-cursor: hand ;"
                        + "-glyph-size:28px;"
                        + "-fx-fill:#ff1744;"
        );
        editIcon.setStyle(
                "-fx-cursor: hand ;"
                        + "-glyph-size:28px;"
                        + "-fx-fill:#00E676;"

        );
        deleteIcon.setFill(Paint.valueOf("#ff1744")); // Set the fill color programmatically
        editIcon.setFill(Paint.valueOf("#00E676")); // Set the fill color programmatically


        deleteIcon.setOnMouseClicked(event -> {
            try {
                deletePatient();
            } catch (Exception ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        editIcon.setOnMouseClicked(event -> openEditUserView());

        HBox manageBtn = new HBox(editIcon, deleteIcon);
        manageBtn.setStyle("-fx-alignment:center");
        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
        return manageBtn;
    }

    private void loadData() {
        try {
            ArrayList<Patient> patientList = Service.afficherAll();
            PatientTable.getItems().setAll(patientList);
            numbersofusers.setText(""+patientList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddUserView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUser.fxml"));
            Pane addUserView = loader.load();
            pnlOverview.getChildren().setAll(addUserView);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void deletePatient() throws SQLException {
        Patient patient = PatientTable.getSelectionModel().getSelectedItem();
        Service.delete(patient.getId());
        loadData();
    }

    private void openEditUserView() {
        Patient patient = PatientTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUser.fxml"));
            Pane addUserView = loader.load();
            AddUser addpatient = loader.getController();
            addpatient.setUpdate(true);
            addpatient.setTextField(
                    patient.getId(),
                    patient.getEmail(),
                    patient.getFirstname(),
                    patient.getLastname(),
                    patient.getAge(),
                    patient.getNumber(),
                    patient.getImg_path(),
                    patient.getAddress()
            );

            pnlOverview.getChildren().setAll(addUserView);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void PDF(MouseEvent event) {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PDF File");

        // Set the extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file chooser dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (PDDocument document = PDDocument.load(file)) {
                // Extract text from PDF
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);
                System.out.println("Extracted Text:");
                System.out.println(text);

                // Process images
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
                    BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
                    File outputImageFile = new File("image_page_" + (pageIndex + 1) + ".png");
                    ImageIO.write(image, "png", outputImageFile);
                    System.out.println("Image saved: " + outputImageFile.getAbsolutePath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
