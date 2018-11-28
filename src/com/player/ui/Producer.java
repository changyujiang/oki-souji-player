package com.player.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.player.ui.Utils.*;
import static java.awt.BorderLayout.PAGE_END;
import static java.awt.BorderLayout.PAGE_START;
import static java.awt.Cursor.CROSSHAIR_CURSOR;

public class Producer {
    private static final int PRIMARY_VIDEO = 0;
    private static final int SECONDARY_VIDEO = 1;

    private File primaryDir;
    private File secondaryDir;
    private JFrame mJFrame = new JFrame();

    /**
     * primary panel
     */
    private JLabel mPrimaryImage = new JLabel();
    private BufferedImage mPrimaryBufferedImage;
    private JLabel mPrimaryFrameNumber = new JLabel();

    /**
     * secondary panel
     */
    private JLabel mSecondaryImage = new JLabel();
    private BufferedImage mSecondaryBufferedImage;
    private JLabel mSecondaryFrameNumber = new JLabel();

    public static void main(String[] args) {
        new Producer();
        log("Oki-Producer");
    }

    private Producer() {
        initProducer();
    }

    private void initProducer() {
        initFrame();

        JPanel actionPanel = initActionPanel();
        JPanel primaryPanel = initVideo(PRIMARY_VIDEO);
        JPanel secondaryPanel = initVideo(SECONDARY_VIDEO);

        mJFrame.add(actionPanel, PAGE_START);
        mJFrame.add(primaryPanel, BorderLayout.LINE_START);
        mJFrame.add(secondaryPanel, BorderLayout.LINE_END);
        mJFrame.setVisible(true);
    }

    private void initFrame() {
        mJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mJFrame.setTitle("Oki Player");
        mJFrame.setSize(734,408);
        mJFrame.setLocationRelativeTo(null);
    }

    private JPanel initActionPanel() {
        JPanel panel = new JPanel();
        panel.setSize(734, 100);
        FlowLayout layout = new FlowLayout();
        panel.setLayout(layout);

        setupActionList(panel);
        setupLinkList(panel);
        setupButtons(panel);
        return panel;
    }

    private void setupActionList(JPanel panel) {
        JLabel actionLabel = new JLabel("Action:");
        String[] actionOptions = {
                "Import Primary Video",
                "Import Secondary Video",
                "Create new hyperlink"
        };
        JList<String> actionList = new JList<>(actionOptions);
        actionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actionList.setLayoutOrientation(JList.VERTICAL);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = actionList.getSelectedIndex();
                    if (index == 0){
                        importPrimaryVideo();
                    } else if (index == 1) {
                        importSecondaryVideo();
                    } else if (index == 2) {
                        createNewLink();
                    }
                }
            }
        };
        actionList.addMouseListener(mouseListener);

        panel.add(actionLabel);
        panel.add(actionList);
    }

    private void importPrimaryVideo() {
        primaryDir = selectFile(mJFrame);
        if (primaryDir != null) {
//            updateImage(PRIMARY_VIDEO, 1);
            updatePrimaryImage(1);
        }
    }

    private void importSecondaryVideo() {
        secondaryDir = selectFile(mJFrame);
        if (secondaryDir != null) {
//            updateImage(SECONDARY_VIDEO, 1);
            updateSecondaryImage(1);
        }
    }

//    private void updateImage(int type, int frameNum) {
//        JLabel imageLabel;
//        JLabel frameNumText;
//        if (type == PRIMARY_VIDEO) {
//            imageLabel = mPrimaryImage;
//            frameNumText = mPrimaryFrameNumber;
//            mPrimaryBufferedImage = loadFrame(primaryDir, frameNum);
//        } else {
//            imageLabel = mSecondaryImage;
//            frameNumText = mSecondaryFrameNumber;
//            mSecondaryBufferedImage = loadFrame(secondaryDir, frameNum);
//        }
//        BufferedImage frame =
//        if (frame == null) {
//            log("Error: frame is null.");
//        } else {
//            imageLabel.setIcon(new ImageIcon(frame));
//            frameNumText.setText("Current Frame " + frameNum);
//        }
//    }

    private void updatePrimaryImage(int frameNum) {
        BufferedImage bufferedImage = loadFrame(primaryDir, frameNum);
        mPrimaryBufferedImage = bufferedImage;
        if (bufferedImage == null) {
            log("Error: frame is null.");
        } else {
            mPrimaryImage.setIcon(new ImageIcon(bufferedImage));
            mPrimaryFrameNumber.setText("Current Frame " + frameNum);
        }
    }

    private void updateSecondaryImage(int frameNum) {
        BufferedImage bufferedImage = loadFrame(secondaryDir, frameNum);
        mSecondaryBufferedImage = bufferedImage;
        if (bufferedImage == null) {
            log("Error: frame is null.");
        } else {
            mSecondaryImage.setIcon(new ImageIcon(bufferedImage));
            mSecondaryFrameNumber.setText("Current Frame " + frameNum);
        }
    }

    //TODO
    private void createNewLink() {
        setupPrimaryImageToSelect();
    }

    //TODO
    private void setupLinkList(JPanel panel) {
        JLabel linkLabel = new JLabel("Select Link:");
        String[] links = {
                "PlaceHolder1",
                "PlaceHolder2"
        };
        JList<String> linkList = new JList<>(links);
        linkList.setLayoutOrientation(JList.VERTICAL);
        panel.add(linkLabel);
        panel.add(linkList);
    }

    private void setupButtons(JPanel panel) {
        JButton connect_video = new JButton("Connect Video");
        panel.add(connect_video);

        JButton save_file = new JButton("Save File");
        panel.add(save_file);
    }

    // TODO improve layout setting
    private JPanel initVideo(int type) {
        JPanel panel = new JPanel();
        JSlider jSlider = setupSlider(type);
        panel.setSize(367, 308);
        panel.setLayout(new BorderLayout());
        if (type == 0) {
            panel.add(mPrimaryImage, PAGE_START);
            panel.add(mPrimaryFrameNumber, PAGE_END);
            panel.add(jSlider);
        } else if (type == 1) {
            panel.add(mSecondaryImage, PAGE_START);
            panel.add(mSecondaryFrameNumber, PAGE_END);
            panel.add(jSlider);
        }

        return panel;
    }

    private JSlider setupSlider(int type) {
        JSlider jSlider = new JSlider(JSlider.HORIZONTAL, 1, 9000, 1);
        jSlider.addChangeListener(e ->{
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                if (type == PRIMARY_VIDEO) {
                    updatePrimaryImage(source.getValue());
                } else {
                    updateSecondaryImage(source.getValue());
                }
//                updateImage(type, source.getValue());
            }
        });
        jSlider.setMinorTickSpacing(1);
        jSlider.setMajorTickSpacing(100);
        jSlider.setPaintTrack(true);
        jSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        return jSlider;
    }

    private int startX;
    private int startY;
    private int width;
    private int height;

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            log("mouseClicked");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startX = e.getX();
            startY = e.getY();
            log("mouserPressed: startX=" + startX + " startY=" + startY);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            width = e.getX() - startX;
            height = e.getY() - startY;
            log("mouserReleased: width=" + width + " height=" + height);
            updatePrimaryImage(e.getX(), e.getY(), false);
            mPrimaryImage.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            mPrimaryImage.removeMouseListener(this);
            mPrimaryImage.removeMouseMotionListener(mouseMotionListener);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            log("mouseEntered");
            mPrimaryImage.setCursor(new Cursor(CROSSHAIR_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            log("mouserExited");
        }
    };

    private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
//            log("mouserDragged");
            int currentX = e.getX();
            int currentY = e.getY();
            updatePrimaryImage(currentX, currentY, true);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
//            log("mouserMoved");
        }
    };

    private void setupPrimaryImageToSelect() {
        mPrimaryImage.addMouseListener(mouseListener);
        mPrimaryImage.addMouseMotionListener(mouseMotionListener);
    }

    private void updatePrimaryImage(int currentX, int currentY, boolean temporal) {
        BufferedImage image;
        if (temporal) {
            image = deepCopy(mPrimaryBufferedImage);
        } else {
            image = mPrimaryBufferedImage;
        }
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.drawRect(startX, startY, currentX - startX, currentY - startY);
        g2d.dispose();
        mPrimaryImage.setIcon(new ImageIcon(image));
    }

}
