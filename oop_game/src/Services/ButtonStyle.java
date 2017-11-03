package com.mycompany.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.plaf.Border;

/**
 * Attaches style to each button instantiated in Game class. Extends from Button. 
 */
public class ButtonStyle extends Button
{
  public ButtonStyle()
  {
    this.getUnselectedStyle().setBgColor(ColorUtil.BLUE); 
        this.getUnselectedStyle().setFgColor(ColorUtil.WHITE);
        this.getUnselectedStyle().setBgTransparency(200);
        this.getAllStyles().setBorder(Border.createInsetBorder(4));
        this.getSelectedStyle().setBgColor(ColorUtil.WHITE);
        this.getAllStyles().setPaddingBottom(5);
        this.getAllStyles().setPaddingTop(5);
  }
}