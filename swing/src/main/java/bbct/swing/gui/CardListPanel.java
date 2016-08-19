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
package bbct.swing.gui;

import bbct.common.data.BaseballCardIO;
import bbct.common.exceptions.BBCTIOException;
import bbct.swing.BBCTStringResources;
import bbct.swing.FontResources;
import bbct.swing.gui.event.ShowCardActionListener;
import bbct.swing.gui.event.UpdateInstructionsAncestorListener;
import bbct.swing.gui.event.UpdateTitleAncestorListener;
import bbct.swing.model.CardTableModel;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

/**
 * Shows a list of all baseball cards. Can be filtered.
 */
public class CardListPanel extends JPanel {

    private BaseballCardIO bcio;

    /**
     * Creates a new {@link CardListPanel}.
     *
     * @param bcio The connection to the underlying persistent storage mechanism.
     */
    public CardListPanel(BaseballCardIO bcio) {
        this.bcio = bcio;
        this.initComponents();

    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        try {
            TableModel model = new CardTableModel(this.bcio);
            JTable table = new JTable(model);
            JScrollPane tableScroller = new JScrollPane(table);
            table.setFillsViewportHeight(true);
            this.add(tableScroller, BorderLayout.CENTER);
        } catch (BBCTIOException ex) {
            Logger.getLogger(CardListPanel.class.getName()).log(Level.SEVERE, "Unable to load card list", ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), BBCTStringResources.ErrorResources.IO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }

        JPanel buttonsPanel = new JPanel();

        JButton backButton = new JButton(BBCTStringResources.ButtonResources.BACK_BUTTON);
        backButton.setFont(FontResources.BUTTON_FONT);
        backButton.addActionListener(new ShowCardActionListener(this, BBCTFrame.MENU_CARD_NAME));
        buttonsPanel.add(backButton);

        this.add(buttonsPanel, BorderLayout.SOUTH);

        this.addAncestorListener(new UpdateTitleAncestorListener(BBCTStringResources.TitleResources.CARD_LIST_PANEL_TITLE));
        this.addAncestorListener(new UpdateInstructionsAncestorListener(BBCTStringResources.InstructionResources.CARD_LIST_INSTRUCTIONS));
    }

    /**
     * Tests for {@link CardListPanel}. Simply creates a {@link javax.swing.JFrame}
     * in which to display it.
     *
     * @param args
     *            Command-line arguments. (ignored)
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("CardListPanel Test");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(new CardListPanel(null));
        f.setSize(400, 400);
        f.setVisible(true);
    }

}
