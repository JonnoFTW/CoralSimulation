import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * @author Jonathan
 * A panel to view logs in a specified folder
 */
public class LogPanel extends JPanel {
    private static final long serialVersionUID = -1665608884566053419L;
    private File dir;
    private JList list;
    private JTextArea text;
    /**
     * @param string the directory to look for log files in
     */
    public LogPanel(String string) {
        setLayout(new BorderLayout(0, 0));
        dir = new File(string);
        
        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new FileListCellRenderer());
        list.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if(list.getModel().getSize() == 0)
                        return;
                    if(list.isSelectionEmpty())
                        list.setSelectedIndex(0);
                    BufferedReader log = new BufferedReader(
                            new FileReader((File) list.getSelectedValue()));
                    text.read(log, "File contents");
                    log.close();
                } catch (FileNotFoundException e1) {
                    text.setText(e1.toString());
                } catch (IOException e1) {
                    text.setText(e1.toString());
                }
            }
        });
        //
        text = new JTextArea();
        text.setFont(new Font("Monospaced",Font.PLAIN, 14));
        
        add(new JScrollPane(text), BorderLayout.CENTER);
        add(new JScrollPane(list), BorderLayout.EAST);
        loadLogList();
    }


    
    /**
     * Load the files from the log directory into the list
     * so that they can be viewed
     */
    private void loadLogList() {
        File[] files = dir.listFiles();

        Arrays.sort(files, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            } });
        list.setListData(files);
    }
    /**
     * Refresh the log list and show the latest log
     */
    public void refresh() {
        loadLogList();
    }
    /**
     * @author Jonathan
     * A cell renderer for the JList that only shows file names insteaf of their full path
     */
    private static class FileListCellRenderer extends DefaultListCellRenderer  {

        private static final long serialVersionUID = 1L;

        /* (non-Javadoc)
         * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof File) {
                File file = (File) value;
                setText(file.getName());
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                setEnabled(list.isEnabled());
                setFont(list.getFont());
                setOpaque(true);
            }
            return this;
        }
    }
    
}
