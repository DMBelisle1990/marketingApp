package com.vl.marketing.util;


import com.vl.marketing.model.Authorization;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

public class CheckBoxCellFactory implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<Authorization,Boolean> checkBoxCell = new CheckBoxTableCell();
        return checkBoxCell;
    }
}