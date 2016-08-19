/*
 * This file is part of BBCT.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.swing.model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import bbct.common.data.BaseballCard;
import bbct.common.data.BaseballCardIO;
import bbct.common.exceptions.BBCTIOException;
import bbct.swing.BBCTStringResources;

public class CardTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 7;

    private static final String[] COLUMN_NAMES = {
            BBCTStringResources.ColumnNameResources.CARD_BRAND_LABEL,
            BBCTStringResources.ColumnNameResources.CARD_YEAR_LABEL,
            BBCTStringResources.ColumnNameResources.CARD_NUMBER_LABEL,
            BBCTStringResources.ColumnNameResources.CARD_VALUE_LABEL,
            BBCTStringResources.ColumnNameResources.CARD_COUNT_LABEL,
            BBCTStringResources.ColumnNameResources.PLAYER_NAME_LABEL,
            BBCTStringResources.ColumnNameResources.PLAYER_POSITION_LABEL
    };

    private BaseballCardIO bcio;

    public CardTableModel(BaseballCardIO bcio) throws BBCTIOException {
        if (bcio != null) {
            this.bcio = bcio;
        }
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        try {
            List<BaseballCard> cards = this.bcio.getAllBaseballCards();
            return cards.size();
        } catch (BBCTIOException ex) {
            Logger.getLogger(CardTableModel.class.getName()).log(Level.SEVERE, "Database read error.", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), BBCTStringResources.ErrorResources.IO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            List<BaseballCard> cards = this.bcio.getAllBaseballCards();
            BaseballCard card = cards.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return card.getBrand();

                case 1:
                    return card.getYear();

                case 2:
                    return card.getNumber();

                case 3:
                    return card.getValue() / 100.0;

                case 4:
                    return card.getCount();

                case 5:
                    return card.getPlayerName();

                case 6:
                    return card.getPlayerPosition();

                default:
                    Logger.getLogger(CardTableModel.class.getName()).log(Level.SEVERE, "Invalid column number: " + columnIndex);
                    throw new RuntimeException("Invalid column number: " + columnIndex);
            }
        } catch (BBCTIOException ex) {
            Logger.getLogger(CardTableModel.class.getName()).log(Level.SEVERE, "Database read error.", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), BBCTStringResources.ErrorResources.IO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns a default name for the column using spreadsheet conventions:
     * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     * returns an empty string.
     *
     * @param column the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     * @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 5:
            case 6:
                return String.class;

            case 1:
            case 2:
            case 4:
                return Integer.class;

            case 3:
                return Double.class;

            default:
                Logger.getLogger(CardTableModel.class.getName()).log(Level.SEVERE, "Invalid column number: " + columnIndex);
                throw new RuntimeException("Invalid column number: " + columnIndex);
        }
    }



}
