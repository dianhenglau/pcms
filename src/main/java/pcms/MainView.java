package pcms;

import javax.swing.JPanel;
import pcms.menu.MenuView;

/** Main view. */
public final class MainView {
    /** Menu view. */
    public final MenuView menuView;
    /** Content view. */
    public final ContentView contentView;

    /** Pane. */
    public final JPanel pane;

    /** Construct. */
    public MainView(final MenuView menuView, final ContentView contentView) {
        this.menuView = menuView;
        this.contentView = contentView;
        pane = ViewUtil.createHorizontalPane();
        pane.add(menuView.pane);
        pane.add(contentView.pane);
    }
}
