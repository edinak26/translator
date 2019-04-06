package phoenix.general.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;

public class View extends Application {
    static List<String> types;
    static String[][] rel;

    public static void setTypes(List<String> types){
        View.types=types;
    }
    public static void setRel(String[][] rel){
        View.rel=rel;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");


        SpreadsheetView spread = new SpreadsheetView();


        GridBase grid = new GridBase(types.size(), types.size());
        ArrayList<String> arr = new ArrayList<>();
        grid.getRowHeaders().setAll(types);
        grid.getColumnHeaders().setAll(types);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < types.size(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < types.size(); ++column) {
                SpreadsheetCell cellIndex = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,null);
                Label label = new Label(rel[row][column]!=null?"       "+rel[row][column]+"       ":"       -       ");
                label.setFont(new Font(label.getFont().getFamily(), 20));
                label.setWrapText(true);
                label.setAlignment(Pos.CENTER);
                label.setTextAlignment(TextAlignment.CENTER);
                cellIndex.setGraphic(label);
                list.add(cellIndex);
            }
            rows.add(list);
        }
        grid.setRows(rows);
        SpreadsheetView spv = new SpreadsheetView(grid);
        spv.setRowHeaderWidth(150);
        spv.setEditable(false);
        spv.setPrefHeight(690);
        spv.setPrefWidth(1300);
        CodeArea code =new CodeArea("dhfgdfgdfnnnnnnnn\ngfhfdhdfghd\ngdfsgdfgsdfgs");
        code.setStyle(5, 10, Collections.singleton("green"));
        Group group = new Group(spv);//,code);
        group.prefHeight(500);
        group.prefWidth(500);
        Scene scene = new Scene(group, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void run() {
        launch();
    }
}
