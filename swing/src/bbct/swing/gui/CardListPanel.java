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

import javax.swing.JFrame;
import javax.swing.JPanel;

import bbct.common.data.BaseballCardIO;

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
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new CardListPanel(null));
        f.setSize(400, 400);
        f.setVisible(true);
    }

}
