package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.listeners.PaintListener;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Widget;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * WidgetExplorerFrame
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class WidgetExplorerFrame extends JFrame implements PaintListener {
    private static final Color[] COLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE,
            Color.ORANGE, Color.YELLOW, Color.BLACK, Color.CYAN, Color.DARK_GRAY, Color.LIGHT_GRAY,
            Color.MAGENTA, Color.PINK, Color.WHITE };

    private final ScriptContext context;

    private final JTree tree;
    private final WidgetTreeModel treeModel;
    private final JPanel infoArea;
    private final JTextField searchBox;
    private Rectangle highlightArea;

    public WidgetExplorerFrame(final ScriptContext context) {
        this.context = context;

        setTitle(Application.TITLE);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                setVisible(false);
                highlightArea = null;
            }
        });

        treeModel = new WidgetTreeModel();
        treeModel.update("");
        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(final TreeSelectionEvent e) {
                try {
                    final Object node = tree.getLastSelectedPathComponent();
                    if (node == null || node instanceof WidgetWrapper) return;

                    infoArea.removeAll();
                    Component component = null;
                    if (node instanceof ComponentWrapper) {
                        highlightArea = ((ComponentWrapper) node).get().getBounds();
                        component = ((ComponentWrapper) node).get();
                    }

                    if (component == null) return;

                    addInfo("Index: ", Integer.toString(component.getIndex()));
                    addInfo("Id: ", Integer.toString(component.getId()));
                    addInfo("Component id: ", Integer.toString(component.getComponentId()));
                    addInfo("Parent id: ", Integer.toString(component.getParentId()));
                    addInfo("X: ", Integer.toString(component.getX()));
                    addInfo("Y: ", Integer.toString(component.getY()));
                    addInfo("Width: ", Integer.toString(component.getWidth()));
                    addInfo("Height: ", Integer.toString(component.getHeight()));
                    addInfo("Texture id: ", Integer.toString(component.getTextureId()));
                    addInfo("Border thickness: ", Integer.toString(component.getBorderThickness()));
                    addInfo("Model id: ", Integer.toString(component.getModelId()));
                    addInfo("Model type: ", Integer.toString(component.getModelType()));
                    addInfo("Bounds index: ", Integer.toString(component.getBoundsIndex()));
                    addInfo("Type: ", Integer.toString(component.getType()));
                    addInfo("Hidden: ", "" + component.isHidden());
                    addInfo("Visible: ", "" + component.isVisible());
                    addInfo("Text: ", component.getText());
                    addInfo("Text color: ", Integer.toString(component.getTextColor()));
                    addInfo("Opacity: ", Integer.toString(component.getOpacity()));
                    addInfo("ScrollBarH: ", Integer.toString(component.getScrollBarH()));
                    addInfo("ScrollBarV: ", Integer.toString(component.getScrollBarV()));
                    addInfo("Component name: ", component.getComponentName());
                    addInfo("Item stack size: ", Integer.toString(component.getItemStackSize()));
                    addInfo("Rectagle: ", component.getBounds().toString());

                    addInfo("Items: ", getItems(component.getItems()));
                    addInfo("Stacks: ", getItems(component.getItemsStacks()));
                    addInfo("Actions: ", getItems(component.getActions()));

                    infoArea.validate();
                    infoArea.repaint();
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }

            private void addInfo(final String key, final String value) {
                final JPanel row = new JPanel();
                row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                for (final String data : new String[]{key, value}) {
                    final JLabel label = new JLabel(data);
                    label.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
                    row.add(label);
                }

                infoArea.add(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        add(scrollPane, BorderLayout.WEST);

        infoArea = new JPanel();
        infoArea.setLayout(new BoxLayout(infoArea, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(infoArea);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        add(scrollPane, BorderLayout.CENTER);

        final ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                treeModel.update(searchBox.getText());
                infoArea.removeAll();
                infoArea.validate();
                infoArea.repaint();
            }
        };

        final JPanel toolArea = new JPanel();
        toolArea.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolArea.add(new JLabel("Filter:"));

        searchBox = new JTextField(20);
        searchBox.addActionListener(actionListener);
        toolArea.add(searchBox);

        final JButton updateButton = new JButton("Update");
        updateButton.addActionListener(actionListener);
        toolArea.add(updateButton);
        add(toolArea, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(MainFrame.instance);
        setVisible(true);
    }

    private String getItems(final int[] items) {
        final StringBuilder sb = new StringBuilder();
        if (items != null) {
            for (final int index : items)
                sb.append(index).append(", ");

            sb.reverse();
            sb.delete(0, 2);
            sb.reverse();
        }

        return sb.toString();
    }

    private String getItems(final String[] items) {
        final StringBuilder sb = new StringBuilder();
        if (items != null) {
            for (final String index : items)
                sb.append(index).append(", ");

            sb.reverse();
            sb.delete(0, 2);
            sb.reverse();
        }

        return sb.toString();
    }

    /**
     * WidgetTreeModel
     *
     * @author Pat-ji
     */
    private final class WidgetTreeModel implements TreeModel {
        private final Object root = new Object();
        private final java.util.List<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();
        public final java.util.List<WidgetWrapper> widgetWrappers = new ArrayList<WidgetWrapper>();

        @Override
        public Object getRoot() {
            return root;
        }

        @Override
        public Object getChild(final Object parent, final int index) {
            try {
                if (parent == root) {
                    return widgetWrappers.get(index);
                } else if (parent instanceof WidgetWrapper) {
                    final ComponentWrapper componentWrapper = new ComponentWrapper(((WidgetWrapper) parent).get().getChildren()[index]);
                    componentWrapper.info = componentWrapper.component.isVisible() ? "" : "X- ";
                    return componentWrapper;
                } else if (parent instanceof ComponentWrapper) {
                    final ComponentWrapper componentWrapper = new ComponentWrapper(((ComponentWrapper) parent).get().getChildren()[index]);
                    componentWrapper.info = componentWrapper.component.isVisible() ? "" : "X- ";
                    return componentWrapper;
                }
            } catch (final Exception ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        public int getChildCount(final Object parent) {
            try {
                if (parent == root) {
                    return widgetWrappers.size();
                } else if (parent instanceof WidgetWrapper) {
                    return ((WidgetWrapper) parent).get().getChildren().length;
                } else if (parent instanceof ComponentWrapper) {
                    return ((ComponentWrapper) parent).get().getChildren().length;
                }
            } catch (final Exception ex){
                ex.printStackTrace();
            }

            return 0;
        }

        @Override
        public boolean isLeaf(final Object node) {
            try {
                return node instanceof ComponentWrapper && ((ComponentWrapper) node).get().getChildren().length == 0;
            } catch (final Exception ignored) { }

            return false;
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
        }

        @Override
        public int getIndexOfChild(final Object parent, final Object child) {
            if (parent == root) {
                return widgetWrappers.indexOf(child);
            } else if (parent instanceof WidgetWrapper) {
                return Arrays.asList(((WidgetWrapper) parent).get().getChildren()).indexOf(((ComponentWrapper) child).get());
            } else if (parent instanceof ComponentWrapper) {
                return Arrays.asList(((ComponentWrapper) parent).get().getChildren()).indexOf(((ComponentWrapper) child).get());
            }

            return -1;
        }

        @Override
        public void addTreeModelListener(final TreeModelListener l) {
            treeModelListeners.add(l);
        }

        @Override
        public void removeTreeModelListener(final TreeModelListener l) {
            treeModelListeners.remove(l);
        }

        private void fireTreeStructureChanged(final Object oldRoot) {
            final TreeModelEvent e = new TreeModelEvent(this, new Object[]{oldRoot});
            for (final TreeModelListener tml : treeModelListeners)
                tml.treeStructureChanged(e);
        }

        public void update(final String search) {
            widgetWrappers.clear();
            try {
                for (final Widget widget : context.widgets.getLoaded())
                    children: for (final Component component : widget.getChildren()) {
                        if (search(component, search)) {
                            final WidgetWrapper widgetWrapper = new WidgetWrapper(widget);
                            widgetWrapper.info = component.isVisible() ? "" : "X- ";
                            widgetWrappers.add(widgetWrapper);
                            break;
                        }

                        for (final Component widgetSubChild : component.getChildren())
                            if (search(widgetSubChild, search)) {
                                final WidgetWrapper widgetWrapper = new WidgetWrapper(widget);
                                widgetWrapper.info = component.isVisible() ? "" : "X- ";
                                widgetWrappers.add(widgetWrapper);
                                break children;
                            }
                    }
            } catch (final NullPointerException ignored) {}

            fireTreeStructureChanged(root);
        }

        private boolean search(final Component child, final String string) {
            return child.getText().toLowerCase().contains(string.toLowerCase());
        }

    }

    /**
     * WidgetWrapper
     *
     * @author Pat-ji
     */
    private final class WidgetWrapper {
        private final Widget widget;

        public String info;

        public WidgetWrapper(final Widget widget) {
            this.widget = widget;
        }

        public Widget get() {
            return widget;
        }

        @Override
        public boolean equals(final Object object) {
            return object != null && object instanceof WidgetWrapper && widget.equals(((WidgetWrapper) object).get());
        }

        @Override
        public String toString() {
            return info + "Widget-" + widget.getIndex();
        }

    }

    /**
     * ComponentWrapper
     *
     * @author Pat-ji
     */
    private final class ComponentWrapper {
        private final Component component;

        public String info;

        public ComponentWrapper(final Component component) {
            this.component = component;
        }

        public Component get() {
            return component;
        }

        @Override
        public boolean equals(final Object object) {
            return object != null && object instanceof ComponentWrapper && component.equals(((ComponentWrapper) object).get());
        }

        @Override
        public String toString() {
            return info + "Component-" + component.getIndex();
        }

    }

    @Override
    public void render(final Graphics g) {
        int index = 0;
        for (final WidgetWrapper wrapper : treeModel.widgetWrappers) {
            if (wrapper == null) continue;

            final Component[] components = wrapper.get().getChildren();
            if (components == null || components.length == 0) continue;

            for (final Component component : components) {
                if (component == null || !component.isVisible()) continue;

                final Rectangle rectangle;
                if ((rectangle = component.getBounds()) != null && rectangle.contains(context.mouse.getX(), context.mouse.getY())) {
                    g.setColor(COLORS[index % COLORS.length]);
                    g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    g.drawString(wrapper.get().getIndex() + " - " + component.getIndex(), 20, (index++) * 18 + 20);
                }

                final Component[] children = component.getChildren();
                if (children == null || children.length == 0) continue;

                int childIndex = 0;
                for (final Component child : children) {
                    childIndex++;
                    if (child == null || !child.isVisible()) continue;

                    final Rectangle childRect;
                    if ((childRect = child.getBounds()) != null && childRect.contains(context.mouse.getX(), context.mouse.getY())) {
                        g.setColor(COLORS[index % COLORS.length]);
                        g.drawRect(childRect.x, childRect.y, childRect.width, childRect.height);
                        g.drawString(wrapper.get().getIndex() + " - " + component.getIndex() + " - " + childIndex, 20, (index++) * 18 + 20);
                    }
                }
            }
        }

        g.setColor(Color.RED);
        if (highlightArea != null)
            g.drawRect(highlightArea.x, highlightArea.y, highlightArea.width, highlightArea.height);
    }

}